package com.editor.app;
import com.editor.structures.graph.Post;
import com.editor.structures.graph.UserNode;
import com.editor.ui.MainApp;
import com.editor.xml.converter.XmlToJson;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //creating two test users
        Post p1 = new Post();
        p1.setText("hello world");
        List<String> topics1 = new ArrayList<>();
        topics1.add("t1");
        topics1.add("t2");
        p1.setTopics(topics1);
        Post p2 = new Post();
        p2.setText("hello ya abo neka");
        List<String> topics2 = new ArrayList<>();
        topics2.add("t3");
        topics2.add("t4");
        p2.setTopics(topics2);
        UserNode u1 = new UserNode();
        u1.addFollower(1);
        u1.addFollower(2);
        u1.addFollower(3);
        u1.setId(5);
        u1.setName("Kareem");
        u1.addPost(p1);
        u1.addPost(p2);
        //user 2
        UserNode u2=new UserNode();
        u2.addFollower(1);
        u2.addFollower(5);
        u2.addFollower(3);
        u2.setId(2);
        u2.setName("zaki");
        u2.addPost(p1);
        u2.addPost(p2);

        List<UserNode> users=new ArrayList<>();
        users.add(u1);
        users.add(u2);
        XmlToJson json=new XmlToJson();
        System.out.println(json.toJson(users));

//        if (args.length >0) {
//            CliRunner cliRunner = new CliRunner();
//            cliRunner.run(args);
//            // Add logic to utilize cliRunner with args
//        }
//        else
//        {
//            // go to the GUI
//            MainApp mainApp = new MainApp();
//            mainApp.launch();
//        }
    }
}