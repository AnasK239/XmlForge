package com.editor.analysis;
import com.editor.structures.graph.Post;
import com.editor.structures.graph.SocialNetwork;
import com.editor.structures.graph.UserNode;
import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;

public class GraphBuilder {

    public  SocialNetwork build(XmlDocument doc){
        XmlNode root = doc.getRoot();
        if(root == null || doc == null) return null;
        SocialNetwork network = new SocialNetwork();

        if (!"users".equals(root.getName())) {
            System.out.println("Error: Root tag is not <users>");
            return null;
        }
        // Loop until you find user tag
        for(XmlNode node : root.getChildren()){
            if(! "user".equals(node.getName())) continue;
            int userId =-1;
            String username = "";

            //Loop until you find id tag
            for (XmlNode child : node.getChildren()) {
                if ("id".equals(child.getName())) {
                    try {
                        userId = Integer.parseInt(child.getNodeValue());
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping user with invalid ID");
                    }
                } else if ("name".equals(child.getName())) {
                    username = child.getNodeValue();
                }
            }
            //**
            if(userId == -1) continue;
            UserNode user = new UserNode();
            user.setId(userId);
            user.setName(username);
            network.addUser(user);

            //Iam Sorry for this
            //Lets find the followers and connect them
            for(XmlNode child : node.getChildren()){
                //<followers>
                if("followers".equals(child.getName())){
                    for(XmlNode followerNode : child.getChildren()){
                        //<follower>
                        if("follower".equals(followerNode.getName())){
                            // find <id> tag for followers
                            for(XmlNode followingNode : followerNode.getChildren()){
                                if("id".equals(followingNode.getName())){
                                    try{
                                        int followerId = Integer.parseInt(followingNode.getNodeValue());
                                        user.addFollower(followerId);
                                        network.addConnection(followerId, userId);
                                    } catch (NumberFormatException e) {}
                                }
                            }
                        }
                    }
                }
                /* Handle Posts + Topics , Maybe make a helper function ? because this looks awful
                else if ("posts".equals(child.getName())){
                    for(XmlNode postNode : child.getChildren()){
                        if("post".equals(postNode.getName())){

                        }
                    }
                }
                 */
            }

        }
        return network;
    }

    /*
    Internals:

        Scan XML tree for <user> tags:

        Read id, name.

        Read followers (list of ids).

        Read posts & topics.
            
    */ 
}


