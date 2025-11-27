package com.model.graph;

import java.util.List;

public class Graph {
    //Our own graph DS: adjacency list of followers.

    private int[] userIds; // index → userId

    private int size; // number of users

    private List<Integer>[] adj; // adjacency list: adj[i] = followers or followings


    public Graph(int maxNodes) (or build from list)

    public void addNode(int userId)

    public void addEdge(int fromUserId, int toUserId)

    public List<Integer> getNeighbors(int userId)

    public int getDegree(int userId) // for activity
}
