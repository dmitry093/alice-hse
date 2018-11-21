package ru.hse.alice.phrases;

import java.util.ArrayList;
import java.util.List;

public class CantFindYouPhrases {
    public static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Извини, не могу тебя найти.\nПопробуй еще раз.");
        phrases.add("Не вижу тебя в списке.\nПовтори, может я что-то не так расслышала.");
        phrases.add("Вроде бы видела тебя, но сейчас не нахожу.\nНазови свое имя еще раз.");
    }
}
