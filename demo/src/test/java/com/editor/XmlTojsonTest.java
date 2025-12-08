package com.editor;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;
import com.editor.xml.converter.XmlToJson;

public class XmlTojsonTest {
    public static void main(String[] args) {
        XmlNode user = new XmlNode("user", null);

// <id>1</id>
        XmlNode id = new XmlNode("id", null);
        id.addChild(new XmlNode(null, "1"));
        user.addChild(id);

// <name>Ahmed Ali</name>
        XmlNode name = new XmlNode("name", null);
        name.addChild(new XmlNode(null, "Ahmed Ali"));
        user.addChild(name);

// <posts>
        XmlNode posts = new XmlNode("posts", null);

// First <post>
        XmlNode post1 = new XmlNode("post", null);

// body
        XmlNode body1 = new XmlNode("body", null);
        body1.addChild(new XmlNode(null, "Lorem ipsum dolor sit amet..."));
        post1.addChild(body1);

// topics
        XmlNode topics1 = new XmlNode("topics", null);
        XmlNode t1 = new XmlNode("topic", null);
        t1.addChild(new XmlNode(null, "economy"));
        XmlNode t2 = new XmlNode("topic", null);
        t2.addChild(new XmlNode(null, "finance"));
        topics1.addChild(t1);
        topics1.addChild(t2);

        post1.addChild(topics1);
        posts.addChild(post1);

// Second <post>
        XmlNode post2 = new XmlNode("post", null);

        XmlNode body2 = new XmlNode("body", null);
        body2.addChild(new XmlNode(null, "Lorem ipsum dolor sit amet..."));
        post2.addChild(body2);

        XmlNode topics2 = new XmlNode("topics", null);
        XmlNode t3 = new XmlNode("topic", null);
        t3.addChild(new XmlNode(null, "solar_energy"));
        topics2.addChild(t3);

        post2.addChild(topics2);
        posts.addChild(post2);

        user.addChild(posts);

// <followers>
        XmlNode followers = new XmlNode("followers", null);

// follower 1
        XmlNode follower1 = new XmlNode("follower", null);
        XmlNode fid1 = new XmlNode("id", null);
        fid1.addChild(new XmlNode(null, "2"));
        follower1.addChild(fid1);

// follower 2
        XmlNode follower2 = new XmlNode("follower", null);
        XmlNode fid2 = new XmlNode("id", null);
        fid2.addChild(new XmlNode(null, "3"));
        follower2.addChild(fid2);

        followers.addChild(follower1);
        followers.addChild(follower2);

        user.addChild(followers);

        XmlDocument doc = new XmlDocument(user);
        System.out.println(new XmlToJson().toJson(doc));

    }
}
