package com.editor.analysis;
import com.editor.structures.graph.Post;
import com.editor.structures.graph.SocialNetwork;
import com.editor.structures.graph.UserNode;
import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;

public class GraphBuilder {

    public SocialNetwork build(XmlDocument doc){
        SocialNetwork network = new SocialNetwork();
        if (doc == null || doc.getRoot() == null) return network;

        XmlNode root = doc.getRoot();
        for (XmlNode userNode : root.getChildren("user")) {
            UserNode user = new UserNode();
            XmlNode idNode = userNode.getChild("id");
            if (idNode != null && idNode.getNodeValue() != null) {
                user.setId(Integer.parseInt(idNode.getNodeValue().trim()));
            }
            XmlNode nameNode = userNode.getChild("name");
            if (nameNode != null && nameNode.getNodeValue() != null) {
                user.setName(nameNode.getNodeValue().trim());
            }

            XmlNode postsNode = userNode.getChild("posts");
            if (postsNode != null) {
                for (XmlNode postNode : postsNode.getChildren("post")) {
                    Post post = new Post();
                    XmlNode bodyNode = postNode.getChild("body");
                    if (bodyNode != null && bodyNode.getNodeValue() != null) {
                        post.setText(bodyNode.getNodeValue().trim());
                    }
                    XmlNode topicsNode = postNode.getChild("topics");
                    if (topicsNode != null) {
                        for (XmlNode topicNode : topicsNode.getChildren("topic")) {
                            if (topicNode.getNodeValue() != null) {
                                post.getTopics().add(topicNode.getNodeValue().trim());
                            }
                        }
                    }
                    user.addPost(post);
                }
            }
            XmlNode followersNode = userNode.getChild("followers");
            if (followersNode != null) {
                for (XmlNode followerNode : followersNode.getChildren("follower")) {
                    XmlNode followerIdNode = followerNode.getChild("id");
                    if (followerIdNode != null && followerIdNode.getNodeValue() != null) {
                        user.addFollower(Integer.parseInt(followerIdNode.getNodeValue().trim()));
                    }
                }
            }
            network.addUser(user);
        }
        for (UserNode user : network.getUserDirectory().getAllUsers()) {
            for (int followerId : user.getFollowerIds()) {
                network.addConnection(followerId, user.getId());
            }
        }
        buildFollowing(network);

        
        return network;
    }

    public void buildFollowing(SocialNetwork network)
    {
        for(UserNode user : network.getUserDirectory().getAllUsers())
        {
            for(int followerId : user.getFollowerIds())
            {
                UserNode follower = network.getUserDirectory().getById(followerId);
                if(follower != null)
                {
                    follower.addFollowing(user.getId());
                }
            }
        }
    }
}
