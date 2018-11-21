package ru.hse.alice.actions;

import ru.hse.alice.contracts.IRequestActions;

import java.util.ArrayList;
import java.util.List;

public class GreetingInterpretations implements IRequestActions {
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Привет");
        phrases.add("Здорова");
        phrases.add("Хай");
        phrases.add("Приветик");
    }

    @Override
    public Boolean contains(String commandText) {
        return phrases.contains(commandText);
    }
}
