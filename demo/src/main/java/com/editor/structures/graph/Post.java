package com.editor.structures.graph;

import java.util.List;

public class Post {
    //Represent one post.
    private String text;
    private List<String> topics;

    public Post(){
        topics = new ArrayList<>();
    }

    //setter and getters
    public List<String> getTopics() {
        return topics;
    }
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    // add getters
    boolean containsWord(String word){
        return text.contains(word);
    }
    boolean containsTopic(String topic){
        return topics.contains(topic);
    }
}
