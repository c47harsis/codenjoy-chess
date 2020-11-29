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


import com.codenjoy.dojo.chess.model.figures.*;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static com.codenjoy.dojo.chess.model.Elements.*;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Figure> getFigures(boolean isWhite) {
        return LevelUtils.getObjects(xy, map,
                new HashMap<Elements, Function<Point, Figure>>(){{
                    put(WHITE_FERZ, pt -> new Ferz(pt, true));
                    put(WHITE_KON, pt -> new Kon(pt, true));
                    put(WHITE_KOROL, pt -> new Korol(pt, true));
                    put(WHITE_LADIA, pt -> new Ladia(pt, true));
                    put(WHITE_PESHKA, pt -> new Peshka(pt, true));
                    put(WHITE_SLON, pt -> new Slon(pt, true));
                    put(BLACK_FERZ, pt -> new Ferz(pt, false));
                    put(BLACK_KON, pt -> new Kon(pt, false));
                    put(BLACK_KOROL, pt -> new Korol(pt, false));
                    put(BLACK_LADIA, pt -> new Ladia(pt, false));
                    put(BLACK_PESHKA, pt -> new Peshka(pt, false));
                    put(BLACK_SLON, pt -> new Slon(pt, false));
                }});
    }

    private char upper(char ch) {
        return ("" + ch).toUpperCase().charAt(0);
    }
}
