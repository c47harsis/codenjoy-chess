package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.piece.Piece;
import com.codenjoy.dojo.chess.service.Event;
import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.chess.model.Color.*;
import static com.codenjoy.dojo.chess.model.Move.*;
import static com.codenjoy.dojo.chess.service.Event.*;
import static org.junit.Assert.assertFalse;

@Ignore
public class GameTest4x4 extends AbstractGameTest {

    private String whiteKnightSurroundedByPieces() {
        return "  ....w...  " +
                "  ........  " +
                "....q......." +
                ".......z...." +
                ".....K......" +
                "...Q........" +
                "Y.....Z....y" +
                "............" +
                "............" +
                "............" +
                "  ........  " +
                "  ....W...  ";
    }

    @Test
    public void shouldBeAbleToTakePiecesWithDifferentColor_whiteBlack() {

        givenFl(whiteKnightSurroundedByPieces());
        Piece blackQueen = getPieceAt(4, 9);

        // when
        move(WHITE, from(5, 7).to(4, 9));

        // then
        assertE("  ....w...  " +
                "  ........  " +
                "....K......." +
                ".......z...." +
                "............" +
                "...Q........" +
                "Y.....Z....y" +
                "............" +
                "............" +
                "............" +
                "  ........  " +
                "  ....W...  ");
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
        assertE("  ....w...  " +
                "  ........  " +
                "....q......." +
                ".......K...." +
                "............" +
                "...Q........" +
                "Y.....Z....y" +
                "............" +
                "............" +
                "............" +
                "  ........  " +
                "  ....W...  ");
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
        assertE("  ....w...  " +
                "  ........  " +
                "....q......." +
                ".......z...." +
                "............" +
                "...Q........" +
                "Y.....K....y" +
                "............" +
                "............" +
                "............" +
                "  ........  " +
                "  ....W...  ");
        neverFired(WHITE, WRONG_MOVE);
        assertFalse(redPawn.isAlive());
    }
}