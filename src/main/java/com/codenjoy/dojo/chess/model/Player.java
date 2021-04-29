package com.codenjoy.dojo.chess.model;

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


import com.codenjoy.dojo.chess.service.Events;
import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

import static com.codenjoy.dojo.chess.service.GameSettings.Option.LAST_PLAYER_STAYS;

public class Player extends GamePlayer<Hero, Chess> {

    private Chess game;
    private boolean winner = false;
    private boolean alive = true;

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    @Override
    public Hero getHero() {
        return hero;
    }

    @Override
    public void newHero(Chess game) {
        this.game = game;
        this.alive = true;
        this.winner = false;
        Color color = game.getAvailableColor();
        this.hero = new Hero(color, game);
    }

    @Override
    public boolean isAlive() {
        return alive && hero.isAlive();
    }

    @Override
    public boolean isWin() {
        return winner;
    }

    public Color getColor() {
        return hero == null ? null : hero.getColor();
    }

    public Move makeMove() {
        hero.tick();
        hero.getEvents().forEach(this::event);
        return hero.getLastMove();
    }

    @Override
    public boolean shouldLeave() {
        return true;
    }

    public boolean askedForColor() {
        return hero.askedForColor();
    }

    @Override
    public boolean wantToStay() {
        return settings.bool(LAST_PLAYER_STAYS) &&
                game.getBoard().getPieces().stream()
                        .anyMatch(p -> !p.getColor().equals(hero.getColor()));
    }

    public void win() {
        winner = true;
        event(Events.WIN);
    }

    public void gameOver() {
        alive = false;
        event(Events.GAME_OVER);
    }
}
