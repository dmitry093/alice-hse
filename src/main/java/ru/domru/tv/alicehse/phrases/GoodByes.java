package ru.domru.tv.alicehse.phrases;

import ru.domru.tv.alicehse.contracts.IPhrase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoodByes implements IPhrase{
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Всего наилучшего!");
        phrases.add("Пока-пока!");
        phrases.add("До свидания!");
    }

    @Override
    public String getRandom() {
        Random generator = new Random();
        return phrases.get(generator.nextInt(phrases.size()));
    }
}
