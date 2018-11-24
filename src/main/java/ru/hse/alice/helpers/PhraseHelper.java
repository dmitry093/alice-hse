package ru.hse.alice.helpers;

import java.util.List;
import java.util.Random;

public class PhraseHelper {
    public static Boolean containsKeyWord(String command, List<String> keyWords) {
        boolean containsKeyWord = false;
        for (String keyword: keyWords){
            if (command.toLowerCase().contains(keyword.toLowerCase())){
                containsKeyWord = true;
                break;
            }
        }
        return containsKeyWord;
    }

    public static String getRandom(List<String> phrases) {
        Random generator = new Random();
        return phrases.get(generator.nextInt(phrases.size()));
    }
}
