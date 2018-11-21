package ru.hse.alice.actions;

import ru.hse.alice.contracts.IRequestActions;
import ru.hse.alice.contracts.IResponsePhrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShowRatingInterpretations implements IRequestActions {
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Узнать свой рейтинг");
        phrases.add("Покажи мой рейтинг");
        phrases.add("Какие у меня оценки");
    }

    @Override
    public Boolean contains(String commandText) {
        return phrases.contains(commandText);
    }
}
