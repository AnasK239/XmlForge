package com.editor;

import java.lang.reflect.Array;
import java.util.List;

import com.editor.analysis.GraphBuilder;
import com.editor.io.FileManager;
import com.editor.structures.graph.Graph;
import com.editor.structures.graph.SocialNetwork;
import com.editor.structures.graph.UserNode;
import com.editor.structures.xml.XmlDocument;
import com.editor.xml.parser.XmlParser;

public class GraphBuilderTest {
    
    public static void main(String[] args) {
         String original = FileManager.readFile("demo\\src\\main\\resources\\samples\\sample.xml");
        tester(original);
        System.err.println("Done    ");
        String s2 = FileManager.readFile("demo\\src\\main\\resources\\samples\\sample2.xml");
        tester(s2);
    }

    static void tester(String s)
    {
        XmlParser parser = new XmlParser();
        XmlDocument doc = new XmlDocument();
        doc = parser.parse(s);
        GraphBuilder builder = new GraphBuilder();
        SocialNetwork network =  builder.build(doc);
        List<UserNode> users =network.getUserDirectory().getAllUsers();
        for(UserNode user : users)
        {
            System.out.print(user.getId()+" ");
            System.out.print(user.getName()+" ");
            System.out.print("size of followers"+user.getFollowerIds().size()+" ");
            System.out.print("size of following"+user.getFollowingIds().size()+" ");   
            System.err.println("size of posts"+user.getPosts().size()+" ");
        }
        Graph graph = network.getGraph();
        // print the graph
        for(int i = 0; i < graph.getSize(); i++)
        {
            if(graph.getDegree(i) == 0) continue;
            System.out.print(i+" ");
            List<Integer> followers = graph.getNeighbors(i);
            for(int follower : followers)
            {
                System.out.print(follower+" ");
            }
            System.out.println();
        }

    }
}
