package com.editor;

import com.editor.analysis.GraphBuilder;
import com.editor.analysis.NetworkAnalyzer;
import com.editor.io.FileManager;
import com.editor.structures.graph.SocialNetwork;
import com.editor.structures.xml.XmlDocument;
import com.editor.ui.GraphView;
import com.editor.xml.parser.XmlParser;

public class AnalyzerTest {
    static Integer cnt = 0;
    public static void main(String[] args) {
        String original = FileManager.readFile("demo\\src\\main\\resources\\samples\\sample.xml");
        tester(original);
        System.err.println("Done    ");
        String s2 = FileManager.readFile("demo\\src\\main\\resources\\samples\\sample2.xml");
        tester(s2);
    }
    static void tester(String s)
    {
        String path = "demo\\output"+cnt+".jpg";
        cnt++;
        XmlParser parser = new XmlParser();
        XmlDocument doc = new XmlDocument();
        doc = parser.parse(s);
        GraphBuilder builder = new GraphBuilder();
        SocialNetwork network =  builder.build(doc);
        GraphView graphView = new GraphView();
        try {
            graphView.run(network, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetworkAnalyzer analyzer = new NetworkAnalyzer();
        System.out.println(analyzer.mostInfluencerId(network));
        System.out.println(analyzer.mostActiveId(network));
        System.out.println(analyzer.mutualFollowersIds(network, new int[]{1,2}));
        System.out.println(analyzer.mutualFollowersIds(network, new int[]{6,8,4}));
        System.out.println(analyzer.suggestUsersToFollowIds(network, 1));
    }
}
