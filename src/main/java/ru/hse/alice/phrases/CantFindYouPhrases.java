package ru.hse.alice.phrases;

import ru.hse.alice.contracts.IResponsePhrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CantFindYouPhrases implements IResponsePhrase {
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Извини, не могу тебя найти. Повтори, пожалуйста.");
        phrases.add("Не вижу тебя в списке. Ты точно назвал свое имя?");
    }

    @Override
    public String getRandom() {
        Random generator = new Random();
        return phrases.get(generator.nextInt(phrases.size()));
    }
}
