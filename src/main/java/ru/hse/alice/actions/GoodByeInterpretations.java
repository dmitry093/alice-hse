package ru.hse.alice.actions;

import ru.hse.alice.contracts.IRequestActions;

import java.util.ArrayList;
import java.util.List;

public class GoodByeInterpretations implements IRequestActions {
    private static final List<String> phrases = new ArrayList<>();

    static {
        phrases.add("Пока");
        phrases.add("До свидания");
        phrases.add("Спасибо");
        phrases.add("Ничего не нужно");
    }

    @Override
    public Boolean contains(String commandText) {
        return phrases.contains(commandText);
    }
}
