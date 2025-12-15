package com.editor.structures.graph;

// SocialNetwork is the entire thing the graph structure + the database of users
// it's an interface to interact with both at the same time ( add to the graph and database )
public class SocialNetwork {

    private UserDirectory userDirectory;
    private Graph graph;

    private int initialCap = 100;

    public SocialNetwork() {
        userDirectory = new UserDirectory();
        graph = new Graph(initialCap);
    }

    public void addUser(UserNode user) {
        userDirectory.addUser(user);
        graph.addNode(user.getId());
    }

    public void addConnection(int followerId, int followeeId) {
        graph.addEdge(followerId, followeeId);
    }

    public UserDirectory getUserDirectory() {
        return userDirectory;
    }

    public Graph getGraph() {
        return graph;
    }
}
