package com.codenjoy.dojo.chess.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class Scores implements PlayerScores {

    private final Parameter<Integer> winScore;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        winScore = settings.addEditBox("Win score").type(Integer.class).def(30);
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.WIN)) {
            score += winScore.getValue();
        }
    }
}
