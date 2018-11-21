package ru.hse.alice.phrases;

import ru.hse.alice.contracts.IResponsePhrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SayYourNamePhrases implements IResponsePhrase {
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Сейчас-сейчас!\nКак тебя зовут?");
        phrases.add("Одну минутку...\nЧьи оценки будем смотреть?");
        phrases.add("Как тебя зовут?");
    }

    @Override
    public String getRandom() {
        Random generator = new Random();
        return phrases.get(generator.nextInt(phrases.size()));
    }
}
