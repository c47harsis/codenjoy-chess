package com.codenjoy.dojo.chess.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.common.AbstractGameTest;
import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Move.*;
import static com.codenjoy.dojo.chess.model.Events.*;
import static org.junit.Assert.assertFalse;

@Ignore
public class GameTest4x4 extends AbstractGameTest {

    private String whiteKnightSurroundedByPieces() {
        return "  ....w...  \n" +
                "  ........  \n" +
                "....q.......\n" +
                ".......z....\n" +
                ".....K......\n" +
                "...Q........\n" +
                "Y.....Z....y\n" +
                "............\n" +
                "............\n" +
                "............\n" +
                "  ........  \n" +
                "  ....W...  \n";
    }

    @Test
    public void shouldBeAbleToTakePiecesWithDifferentColor_whiteBlack() {

        givenFl(whiteKnightSurroundedByPieces());
        Piece blackQueen = getPieceAt(4, 9);

        // when
        move(WHITE, from(5, 7).to(4, 9));

        // then
        assertE("  ....w...  \n" +
                "  ........  \n" +
                "....K.......\n" +
                ".......z....\n" +
                "............\n" +
                "...Q........\n" +
                "Y.....Z....y\n" +
                "............\n" +
                "............\n" +
                "............\n" +
                "  ........  \n" +
                "  ....W...  \n");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(blackQueen.isAlive());
    }

    @Test
    public void shouldBeAbleToTakePiecesWithDifferentColor_whiteBlue() {

        givenFl(whiteKnightSurroundedByPieces());
        Piece bluePawn = getPieceAt(7, 8);

        // when
        move(WHITE, from(5, 7).to(7, 8));

        // then
        assertE("  ....w...  \n" +
                "  ........  \n" +
                "....q.......\n" +
                ".......K....\n" +
                "............\n" +
                "...Q........\n" +
                "Y.....Z....y\n" +
                "............\n" +
                "............\n" +
                "............\n" +
                "  ........  \n" +
                "  ....W...  \n");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(bluePawn.isAlive());
    }

    @Test
    public void shouldBeAbleToTakePiecesWithDifferentColor_whiteRed() {

        givenFl(whiteKnightSurroundedByPieces());
        Piece redPawn = getPieceAt(6, 5);

        // when
        move(WHITE, from(5, 7).to(6, 5));

        // then
        assertE("  ....w...  \n" +
                "  ........  \n" +
                "....q.......\n" +
                ".......z....\n" +
                "............\n" +
                "...Q........\n" +
                "Y.....K....y\n" +
                "............\n" +
                "............\n" +
                "............\n" +
                "  ........  \n" +
                "  ....W...  \n");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(redPawn.isAlive());
    }
}
