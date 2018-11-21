package ru.domru.tv.alicehse.phrases;

import ru.domru.tv.alicehse.contracts.IPhrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Greetings implements IPhrase{
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Привет!\nЧем я могу помочь?");
        phrases.add("Хеллоу!\nВот что я умею!");
        phrases.add("Приветствую!\nЧем могу быть полезна?");
    }

    @Override
    public String getRandom() {
        Random generator = new Random();
        return phrases.get(generator.nextInt(phrases.size()));
    }
}
