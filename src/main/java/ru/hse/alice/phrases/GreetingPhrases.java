package ru.hse.alice.phrases;

import ru.hse.alice.contracts.IResponsePhrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GreetingPhrases implements IResponsePhrase {
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Привет!\nКак тебя зовут?");
        phrases.add("Хеллоу!\nКак к тебе обращаться?");
        phrases.add("Приветствую!\nКак могу к тебе обратиться?");
    }

    @Override
    public String getRandom() {
        Random generator = new Random();
        return phrases.get(generator.nextInt(phrases.size()));
    }
}
