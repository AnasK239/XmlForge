package com.editor.structures.graph;

import java.util.List;

public class Graph {
    //Our own graph DS: adjacency list of followers.

    private int[] userIds; // index → userId

    private int size; // number of users

    private List<Integer>[] adj; // adjacency list: adj[i] = followers or followings

    //or build from list
    public Graph(int maxNodes) {}

    public void addNode(int userId){}

    public void addEdge(int fromUserId, int toUserId){}

    public List<Integer> getNeighbors(int userId){}

    public int getDegree(int userId) {}// for activity
}
