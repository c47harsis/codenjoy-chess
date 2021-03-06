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


import com.codenjoy.dojo.chess.model.Chess;
import com.codenjoy.dojo.chess.model.Elements;
import com.codenjoy.dojo.chess.client.Board;
import com.codenjoy.dojo.chess.client.ai.AISolver;
import com.codenjoy.dojo.chess.model.Player;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType<GameSettings> {
    private static final String GAME_NAME = "chess";

    @Override
    public GameSettings getSettings() {
        return new GameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(Object score, GameSettings settings) {
        return new Scores(Integer.parseInt(score.toString()), settings);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public GameField createGame(int levelNumber, GameSettings settings) {
        return new Chess(settings.level(), getDice(), settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(GameSettings settings) {
        return v(settings.level().getSize());
    }

    @Override
    public String name() {
        return GAME_NAME;
    }

    @Override
    public CharElements[] getPlots() {
        return Elements.values();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType(GameSettings settings) {
        int playersCount = settings.level().presentedColors().size();
        switch (playersCount) {
            case 2:
                return MultiplayerType.TOURNAMENT;
            case 3:
                return MultiplayerType.TRIPLE;
            case 4:
                return MultiplayerType.QUADRO;
            default:
                throw new IllegalStateException("Players count should be in range [2; 4]");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public GamePlayer createPlayer(EventListener listener, String playerId, GameSettings settings) {
        return new Player(listener, settings);
    }
}