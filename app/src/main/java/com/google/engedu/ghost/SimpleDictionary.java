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
    public String getGoodWordStartingWith(String prefix, boolean userTurn) {

        if(prefix.length() == 0){
            return words.get(new Random().nextInt(words.size()));
        }
        //ArrayList<String> goodWords;
        ArrayList<String> evenWords = null;
        ArrayList<String> oddWords = null;

        int mid, left = 0, right = words.size()-1;
        do {
            mid = (left + right) / 2;

            if (words.get(mid).startsWith(prefix)) {
                //goodWords = (ArrayList<String>)words.subList(findStartIndex(prefix, mid),findEndIndex(prefix,mid));
                int startIndex = findStartIndex(prefix, mid);
                int endIndex = findEndIndex(prefix,mid);
                evenWords = findEvenWords(startIndex, endIndex);
                oddWords = findOddWords(startIndex,endIndex);
                break;
            } else if (prefix.compareTo(words.get(mid)) < 0) {
                right = mid-1;
            } else {
                left = mid+1;
            }
        }while(left != right);

        Random random = new Random();

        if(evenWords == null && oddWords == null){
            return null;
        } else if(evenWords == null || oddWords == null){
            return (oddWords == null) ?
                    evenWords.get(random.nextInt(evenWords.size())) :
                    oddWords.get(random.nextInt(oddWords.size()));
        } else if(userTurn){
            return evenWords.get(random.nextInt(evenWords.size()));
        } else{
            return oddWords.get(random.nextInt(oddWords.size()));
        }
    }

    protected ArrayList<String> findEvenWords(int startIndex, int endIndex){
        ArrayList<String> evenWords = new ArrayList<>();

        for(int i = startIndex; i <= endIndex; ++i){
            if(words.get(i).length() % 2 == 0)
                evenWords.add(words.get(i));
        }

        return evenWords;
    }

    protected ArrayList<String> findOddWords(int startIndex, int endIndex){
        ArrayList<String> oddWords = new ArrayList<>();

        for(int i = startIndex; i <= endIndex; ++i){
            if(words.get(i).length() % 2 != 0)
                oddWords.add(words.get(i));
        }

        return oddWords;
    }

    protected int findStartIndex(String prefix, int startIndex){
        while(words.get(startIndex-1).startsWith(prefix)){
            startIndex--;
        }
        return startIndex;
    }

    protected int findEndIndex(String prefix, int endIndex){
        while(words.get(endIndex+1).startsWith(prefix)){
            endIndex++;
        }
        return endIndex;
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
