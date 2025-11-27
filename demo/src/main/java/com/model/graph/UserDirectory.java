package com.model.graph;

public class UserDirectory {
    //Custom mapping from user id → UserNode.
    private int[] ids;

    private UserNode[] users;

    private int size;

    public void addUser(UserNode user){}

    public UserNode getById(int id)
    {
        // linear search or binary search if kept sorted.
    }
    public UserNode[] getAllUsers()
    {
        return users;
    }

}
