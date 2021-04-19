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

import com.codenjoy.dojo.chess.common.AbstractGameTest;
import com.codenjoy.dojo.chess.model.item.piece.Piece;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.WHITE;
import static com.codenjoy.dojo.chess.model.Events.WRONG_MOVE;
import static com.codenjoy.dojo.chess.model.Move.from;

// Wiki: https://en.wikipedia.org/wiki/Promotion_(chess)
@SuppressWarnings("SpellCheckingInspection")
public class PromotionTest extends AbstractGameTest {

    @Test
    public void shouldBeFiredWrongMove_whenTryingToPromoteKing() {

        givenFl("w.......\n" +
                ".......P\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKBQWBKR\n");

        // when
        move(WHITE, from(7, 6).to(7, 7).promotion(Piece.Type.KING));

        // then
        assertE("w.......\n" +
                ".......P\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKBQWBKR\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAllowed_whenTryingToPromoteIfPawnNotAtLastLine() {

        givenFl("w.......\n" +
                "........\n" +
                ".......P\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKBQWBKR\n");

        // when
        move(WHITE, from(7, 5).to(7, 6).promotion(Piece.Type.QUEEN));

        // then
        assertE("w.......\n" +
                "........\n" +
                ".......P\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKBQWBKR\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldNotBeAllowed_whenTryingToMakePromotionByNotAPawn() {

        givenFl("w.......\n" +
                ".......Q\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKB.WBKR\n");

        // when
        move(WHITE, from(7, 6).to(7, 5).promotion(Piece.Type.PAWN));

        // then
        assertE("w.......\n" +
                ".......Q\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKB.WBKR\n");
        fired(WHITE, WRONG_MOVE);
    }

    @Test
    public void shouldBeAllowed_whenTryingToPromoteAPieceExceptKing() {

        givenFl("w.......\n" +
                ".......P\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKBQWBKR\n");

        // when
        move(WHITE, from(7, 6).to(7, 7).promotion(Piece.Type.QUEEN));

        // then
        assertE("w......Q\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "........\n" +
                "RKBQWBKR\n");
        neverFired(WHITE, WRONG_MOVE);
    }
}
