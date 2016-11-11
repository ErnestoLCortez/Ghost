package com.google.engedu.ghost;

import org.w3c.dom.Node;

import java.util.HashMap;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        add(s, 0);
    }

    private void add(String s, int index){
        if(index == s.length()){
            isWord = true;
            return;
        }
        String current = s.substring(index, 1);
        TrieNode child = children.get(current);
        if(child == null){
            child = new TrieNode();
            children.put(current, child);
        }
        child.add(s, index+1);
    }

    public boolean isWord(String s) {

        return isWord(s, 0);
    }

    private boolean isWord(String s, int index){
        if(index == s.length()){
            return isWord;
        }
        String current = s.substring(index, 1);
        TrieNode child = children.get(current);
        return child.isWord(s, index+1);
    }

    public String getAnyWordStartingWith(String s) {

        return getAnyWordStartingWith(s, 0);
    }

    private String getAnyWordStartingWith(String s, int index){
        if(index == s.length()){
            return getRandomWord()
        }
        String current = s.substring(index, 1);
        TrieNode child = children.get(current);
        return child.getAnyWordStartingWith(s, index+1);
    }

    private String getRandomWord(){

    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
