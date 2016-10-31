package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import static android.R.attr.key;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

        if(prefix.length() == 0){
            return words.get(new Random().nextInt(words.size()));
        }

        return binarySearch(prefix, 0, words.size()-1);

    }

    @Override
    public String getGoodWordStartingWith(String prefix) {




        String selected = null;
        return selected;
    }

    private String binarySearch(String key, int left, int right){
        int mid;
        do {
            mid = (left + right) / 2;

            if (words.get(mid).startsWith(key)) {
                return words.get(mid);
            } else if (key.compareTo(words.get(mid)) < 0) {
                right = mid-1;
            } else {
                left = mid+1;
            }
        }while(left != right);

        return null;
    }
}
