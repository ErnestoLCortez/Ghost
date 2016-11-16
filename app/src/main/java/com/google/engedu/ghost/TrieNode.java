package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {

        String current = s.substring(0, 1);
        TrieNode child = children.get(current);
        if(child == null) {
            child = new TrieNode();
            children.put(current, child);
        }
        child.add(s, 1);
    }

    public void add(String s, int index){

        if (index == s.length()){
            isWord = true;
            return;
        }
        String current = s.substring(index, index+1);
        TrieNode child = children.get(current);
        if(child == null) {
            child = new TrieNode();
            children.put(current, child);
        }

        child.add(s, index + 1);
    }

    public boolean isWord(String s){
        if(0 == s.length()){
            return isWord;
        }
        String current = s.substring(0, 1);
        TrieNode child = children.get(current);
        if(child == null) {
            return false;
        }
        return child.isWord(s.substring(1));

    }

    public String getAnyWordStartingWith(String s) {

        if(s.length() == 0)
            return getRandomWord();
        else {

            String current = s.substring(0, 1);
            TrieNode child = children.get(current);
            if (child == null) {
                return null;
            }
            String recurseWord = child.getGoodWordStartingWith(s.substring(1));
            return (recurseWord == null) ? null : current + recurseWord;
        }
    }

    public String getRandomWord(){

        ArrayList<String> goodWords = new ArrayList<>(children.keySet());
        Random random = new Random();
        String current = goodWords.get(random.nextInt(goodWords.size()));
        TrieNode child = children.get(current);
        if(child.isWord)
            return current;
        return current + child.getRandomWord();
    }

    public String getRandomGoodWord(){

        ArrayList<String> goodWords = getGoodWordList();
        Random random = new Random();
        String current = goodWords.get(random.nextInt(goodWords.size()));
        TrieNode child = children.get(current);
        if(child.isWord)
            return current;
        return current + child.getRandomWord();
    }

    //Determine if all children are valid words and return resulting arraylist
    public ArrayList<String> getGoodWordList(){
        ArrayList<String> goodWords = new ArrayList<>(children.keySet());
        ArrayList<String> editedGoodWords = new ArrayList<>(goodWords);
        for(String word : goodWords){
            if(children.get(word).isWord)
                editedGoodWords.remove(word);
        }

        if(goodWords != null)
            return goodWords;
        return editedGoodWords;
    }

    public String getGoodWordStartingWith(String s) {
        if(s.length() == 0)
            return getRandomGoodWord();
        else {

            String current = s.substring(0, 1);
            TrieNode child = children.get(current);
            if (child == null) {
                return null;
            }
            String recurseWord = child.getGoodWordStartingWith(s.substring(1));
            return (recurseWord == null) ? null : current + recurseWord;
        }
    }
}
