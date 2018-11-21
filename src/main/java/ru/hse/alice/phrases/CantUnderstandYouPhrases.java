package ru.hse.alice.phrases;

import ru.hse.alice.contracts.IResponsePhrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CantUnderstandYouPhrases implements IResponsePhrase {
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Хмм, не могу понять, что ты меня просишь.\nПопробуй еще раз.");
        phrases.add("Что ты сказал?\nПовтори, может я что-то не так расслышала.");
        phrases.add("Аааа? Что?");
    }

    @Override
    public String getRandom() {
        Random generator = new Random();
        return phrases.get(generator.nextInt(phrases.size()));
    }
}
