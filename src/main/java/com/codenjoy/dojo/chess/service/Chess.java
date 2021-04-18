package com.codenjoy.dojo.chess.service;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.model.level.Level;
import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.WAIT_UNTIL_MAKE_A_MOVE;

public class Chess implements GameField<Player> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Chess.class);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Dice dice;

    private final Rotator rotator;
    private final List<Player> players = Lists.newLinkedList();
    private final GameHistory history = new GameHistory();

    private final GameSettings settings;
    private final GameBoard board;

    private Color currentColor;

    private boolean playerAskedColor;

    public Chess(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        this.settings = settings;
        this.board = new GameBoard(level);
        this.rotator = new Rotator(board.getSize());
        this.currentColor = getColors().stream()
                .min(Comparator.comparingInt(Color::getPriority))
                .orElseThrow(() -> new IllegalArgumentException("Level " + level + " is invalid"));
    }

    @Override
    public void tick() {
        Marker marker = MarkerFactory.getMarker("GAME__TICK");
        LOGGER.debug(marker, "Start tick with color: {}", currentColor);
        Player player = getPlayer(currentColor);
        if ((playerAskedColor = player.askedForColor())) {
            LOGGER.debug(marker, "{} player asked for his color", currentColor);
            player.answeredColor();
            LOGGER.debug(marker, "Tick ended up");
            return;
        }
        Move move = player.makeMove();
        if (move == null) {
            LOGGER.debug(marker, "{} move was NOT committed", currentColor);
            if (settings.bool(WAIT_UNTIL_MAKE_A_MOVE)) {
                LOGGER.debug(marker,
                        "{} player's move didn't committed; " +
                                "Option {} set to true, so right to move is not transferred further",
                        currentColor, WAIT_UNTIL_MAKE_A_MOVE
                );
                return;
            }
        } else {
            LOGGER.debug(marker, "{} {} was SUCCESSFULLY committed", currentColor, move);
            history.add(currentColor, move);
        }

        currentColor = nextColor();
        LOGGER.debug(marker, "Color changed to {}", currentColor);
        checkGameOvers();
        checkWinner();
        LOGGER.debug(marker, "Tick ended up");
    }

    private void checkGameOvers() {
        players.stream()
                .filter(p -> !p.isAlive())
                .forEach(p -> p.event(Events.GAME_OVER));
    }

    private void checkWinner() {
        List<Player> alivePlayers = players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
        if (alivePlayers.size() == 1) {
            alivePlayers.get(0).event(Events.WIN);
        }
    }

    @Override
    public void newGame(Player player) {
        if (players.size() == getColors().size()) {
            LOGGER.warn("Trying to add new player [{}], but the game is already full", player);
            return;
        }

        players.add(player);
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public BoardReader<Player> reader() {
        return new ChessBoardReader(this);
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public List<Color> getColors() {
        return board.getColors();
    }

    public Color getAvailableColor() {
        List<Color> usedColors = players.stream()
                .map(Player::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return board.getColors().stream()
                .filter(c -> !usedColors.contains(c))
                .findAny()
                .orElse(null);
    }

    private Player getPlayer(Color color) {
        return players.stream()
                .filter(p -> p.getColor() == color)
                .findAny()
                .orElse(null);
    }

    public GameBoard getBoard() {
        return board;
    }

    private List<Player> getAlivePlayers() {
        return players.stream()
                .filter(Player::isAlive)
                .collect(Collectors.toList());
    }

    private Color nextColor() {
        List<Player> alivePlayers = getAlivePlayers();
        alivePlayers.sort(Comparator.comparingInt(p -> p.getColor().getPriority()));
        return alivePlayers.stream()
                .map(Player::getColor)
                .filter(color -> color.getPriority() > currentColor.getPriority())
                .findAny()
                .orElse(alivePlayers.get(0).getColor());
    }

    public int getBoardSize() {
        return board.getSize();
    }

    public Rotator getRotator() {
        return rotator;
    }

    public boolean isCurrentPlayerAskedColor() {
        return playerAskedColor;
    }
}