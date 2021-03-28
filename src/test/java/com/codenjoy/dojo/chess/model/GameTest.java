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


import com.codenjoy.dojo.chess.service.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class GameTest {

    private Chess game;
    private Dice dice;
    private EventListener listener1;
    private EventListener listener2;
    private Player player1;
    private Player player2;
    private PrinterFactory printerFactory;
    private GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new GameSettings();
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);

        game = new Chess(level, dice, settings);

        listener1 = mock(EventListener.class);
        player1 = new Player(listener1, settings);
        game.newGame(player1);

        listener2 = mock(EventListener.class);
        player2 = new Player(listener2, settings);
        game.newGame(player2);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), player1).print());
    }

    private void standardBoard() {
        givenFl("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
    }

    @Test
    public void shouldProperlyDrawField() {

        // when given
        standardBoard();

        // then
        assertE("rkbqwbkr" +
                "pppppppp" +
                "........" +
                "........" +
                "........" +
                "........" +
                "PPPPPPPP" +
                "RKBQWBKR");
    }

    @Test
    @Ignore
    public void shouldWalk() {

        // given
        standardBoard();

        // when
        player1.getHero();
        // then
        assertE("tksfaskt" +
                "pppppppp" +
                "........" +
                "........" +
                "....P..." +
                "........" +
                "PPPP.PPP" +
                "TKSFASKT");
    }

}
