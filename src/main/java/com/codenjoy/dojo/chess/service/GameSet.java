package com.codenjoy.dojo.chess.service;

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

import com.codenjoy.dojo.chess.model.Color;
import com.codenjoy.dojo.chess.model.Move;
import com.codenjoy.dojo.chess.model.item.piece.King;
import com.codenjoy.dojo.chess.model.item.piece.Piece;
import com.codenjoy.dojo.chess.model.item.piece.Rook;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.List;
import java.util.Optional;

public class GameSet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSet.class);

    private final Color color;
    private final List<Piece> pieces;
    private final GameBoard board;
    private Move lastMove;

    public GameSet(Color color, GameBoard board, List<Piece> pieces) {
        this.color = color;
        this.board = board;
        if (pieces.stream().anyMatch(p -> p.getColor() != color)) {
            throw new IllegalArgumentException("All pieces should be " + color);
        }
        if (pieces.stream().filter(p -> p.getType() == Piece.Type.KING).count() != 1) {
            throw new IllegalArgumentException("Should be exactly one king in game set");
        }
        this.pieces = pieces;
    }

    public boolean isKingAlive() {
        return pieces.stream()
                .filter(p -> p.getType() == Piece.Type.KING)
                .findAny()
                .map(Piece::isAlive)
                .orElse(false);
    }

    public Color getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean makeMove(final Move command) {
        Marker marker = MarkerFactory.getMarker("GAME_SET__MAKE_MOVE");
        LOGGER.debug(marker, "MakeMove started");
        if (command == null) {
            LOGGER.debug(marker, "Trying to make a NULL move with {} game set", color);
            return false;
        }
        Piece piece = board.getPieceAt(command.getFrom())
                .orElse(null);
        if (piece == null || piece.getColor() != getColor()) {
            LOGGER.debug(marker, "Trying to move enemy's piece from {} game set", color);
            return false;
        }
        if (piece.getAvailableMoves().contains(command)) {
            if (isCastling(command)) {
                LOGGER.debug(marker, "{} is castling", command);
                // castling
                if (tryCastling((Rook) board.getPieceAt(command.getTo()).get())) {
                    lastMove = command;
                    LOGGER.debug(marker, "Castling has been committed");
                    return true;
                } else {
                    LOGGER.debug(marker, "Castling has not been committed");
                    return false;
                }
            } else {
                piece.move(command);
                if (command.withPromotion()) {
                    LOGGER.debug(marker, "{} is with promotion", command);
                    pieces.remove(piece);
                    pieces.add(Piece.create(command.getPromotion(), getColor(), board, piece.getPosition()));
                }
                LOGGER.debug(marker, "Piece successfully moved");
                lastMove = command;
                return true;
            }
        }
        LOGGER.debug(marker, "{} is not available for {}", command, piece);
        LOGGER.debug(marker, "MakeMove ended");
        return false;
    }

    private boolean isCastling(Move command) {
        Optional<Piece> optPieceOne = board.getPieceAt(command.getFrom());
        Optional<Piece> optPieceTwo = board.getPieceAt(command.getTo());
        if (optPieceOne.isEmpty() || optPieceTwo.isEmpty()) {
            return false;
        }
        Piece pieceOne = optPieceOne.get();
        Piece pieceTwo = optPieceTwo.get();
        if (pieceOne.getColor() != pieceTwo.getColor()) {
            return false;
        }
        return pieceOne.getType() == Piece.Type.KING && pieceTwo.getType() == Piece.Type.ROOK;
    }

    private boolean tryCastling(Rook rook) {
        King king = getKing();
        if (king == null) {
            throw new IllegalStateException("King is null");
        }
        if (rook.isMoved() || king.isMoved()) {
            return false;
        }
        if (board.isUnderAttack(king.getPosition(), getColor())) {
            return false;
        }
        Direction direction = defineDirection(king.getPosition(), rook.getPosition());
        if (direction == null) {
            throw new IllegalStateException();
        }
        Point rookPosition = direction.change(king.getPosition());
        if (board.getPieceAt(rookPosition).isPresent() || board.isUnderAttack(rookPosition, getColor())) {
            return false;
        }
        Point kingPosition = direction.change(rookPosition);
        if (board.getPieceAt(kingPosition).isPresent() || board.isUnderAttack(kingPosition, getColor())) {
            return false;
        }
        rook.move(rookPosition);
        king.move(kingPosition);
        return true;
    }

    private Direction defineDirection(Point from, Point to) {
        if (from.equals(to)) {
            return null;
        }
        if (from.getX() == to.getX()) {
            return from.getY() < to.getY() ? Direction.UP : Direction.DOWN;
        }
        if (from.getY() == to.getY()) {
            return from.getX() < to.getX() ? Direction.RIGHT : Direction.LEFT;
        }
        return null;
    }

    private King getKing() {
        return (King) pieces.stream()
                .filter(p -> p.getType() == Piece.Type.KING)
                .findFirst().orElse(null);
    }

    public void die() {
        pieces.forEach(p -> p.setAlive(false));
    }

    public Move getLastMove() {
        return lastMove;
    }
}