package com.editor.structures.graph;

import java.util.List;

public class UserNode {
    //Represent one user.

    private int id;
    private String name;
    private List<Post> posts;
    private List<Integer> followerIds; // raw IDs from XML


    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void addFollower(int followerId){}
    // add getters and setters 
}
