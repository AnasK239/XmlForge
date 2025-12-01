package com.editor.structures.graph;

import java.util.List;

public class Post {
    //Represent one post.
    private String text;
    private List<String> topics;

    // add getters
    boolean containsWord(String word){
        return text.contains(word);
    }
    boolean containsTopic(String topic){
        return topics.contains(topic);
    }
}
