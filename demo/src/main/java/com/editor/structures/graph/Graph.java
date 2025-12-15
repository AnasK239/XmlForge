package com.editor.structures.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    // Not used (dont know if we will need it yet)
    private int[] userIds;

    private int size; // number of users
    private List<Integer>[] adj; // adjacency list: adj[i] = List of (followers or followings ?)

    public Graph(int maxNodes) {
        this.size = maxNodes+1;
        this.adj = new ArrayList[size];
        for(int i = 0; i < size; i++){
            adj[i] = new ArrayList<>();
        }
    }

    public void addNode(int userId){
        if(userId >= adj.length){
            resize(userId*2);
        }
    }

    public void addEdge(int fromUserId , int toUserId ){
        // Make sure both are in the bounds of graph size
        addNode(fromUserId);
        addNode(toUserId);

        if (!adj[fromUserId].contains(toUserId)) {
            adj[fromUserId].add(toUserId);
        }
    }

    // If user id bigger than the current capacity , the adj list is doubled
    private void resize(int newCapacity) {
        List<Integer>[] newAdj = new ArrayList[newCapacity];
        for (int i = 0; i < adj.length; i++) {
            newAdj[i] = adj[i];
        }
        for (int i = adj.length; i < newCapacity; i++) {
            newAdj[i] = new ArrayList<>();
        }
        this.adj = newAdj;
    }


    public List<Integer> getNeighbors(int userId){
        if(userId < adj.length){
            return adj[userId];
        }
        return new ArrayList<>();
    }

    public int getDegree(int userId) {
        return getNeighbors(userId).size();
    }

    // probably will not need this because UserDirectory has all users
    public int getSize() {
        return adj.length;
    }
}
