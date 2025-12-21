package com.editor.analysis;

import com.editor.structures.graph.Post;
import com.editor.structures.graph.SocialNetwork;
import com.editor.structures.graph.UserNode;

import java.util.ArrayList;
import java.util.List;

public class PostSearcher {
    // Role: Search posts by word or topic.

    public List<Post> searchByWord(SocialNetwork network, String word){
        List<Post> posts = new ArrayList<Post>();

        if (word.trim().isEmpty()) {
            return posts;
        }

        for (UserNode user : network.getUserDirectory().getAllUsers()){
            for(Post post : user.getPosts()){
                if (post.getText() == null) continue;

                String[] words = post.getText().split(" ");

                for (String word1 : words) {
                    if (word1.equalsIgnoreCase(word)) {
                        posts.add(post);
                        break;
                    }
                }
            }
        }
        return posts;
    }

    public List<Post> searchByTopic(SocialNetwork network, String topic){
        List<Post> posts = new ArrayList<Post>();

        if (topic.trim().isEmpty()) {
            return posts;
        }

        for (UserNode user : network.getUserDirectory().getAllUsers()){
            for(Post post : user.getPosts()){
                if (post.getTopics() == null) continue;

                for (String theTopic : post.getTopics()) {
                    if (theTopic.trim().equalsIgnoreCase(topic.trim())) {
                        posts.add(post);
                        break;
                    }
                }
            }
        }
        return posts;
    }

}
