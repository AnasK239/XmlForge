package com.editor.structures.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Database of userId -> UserNode
public class UserDirectory {

    private Map<Integer,UserNode> users;

    public UserDirectory() {
        this.users = new HashMap<>();
    }

    public void addUser(UserNode user) {
        users.put(user.getId() , user );
    }

    public UserNode getById(int id) {
        return users.get(id);
    }

    public List<UserNode> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public int getSize(){
        return users.size();
    }



}
