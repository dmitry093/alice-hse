package ru.hse.alice.phrases;

import java.util.ArrayList;
import java.util.List;

public class GreetingPhrases {
    public static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Привет!\nКак тебя зовут?");
        phrases.add("Хеллоу!\nКак к тебе обращаться?");
        phrases.add("Приветствую!\nКак могу к тебе обратиться?");
    }
}
