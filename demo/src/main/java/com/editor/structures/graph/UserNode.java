package com.editor.structures.graph;

import java.util.List;

public class UserNode {
    //Represent one user.

    private int id;
    private String name;
    private List<Post> posts;
    private List<Integer> followerIds; // raw IDs from XML

    public UserNode(){
        posts = new ArrayList<>();
        followerIds = new ArrayList<>();}
    //setters and getters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Post> getPosts() {
        return posts;
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    public List<Integer> getFollowerIds() {
        return followerIds;
    }
    public void setFollowerIds(List<Integer> followerIds) {this.followerIds = followerIds;}
    public void addPost(Post post) {
        this.posts.add(post);
    }
    public void addFollower(int followerId){
        this.followerIds.add(followerId);
    }

    public void addFollower(int followerId){}
    // add getters and setters 
}
