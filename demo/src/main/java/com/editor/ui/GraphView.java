package com.editor.ui;

import com.editor.structures.graph.Graph;
import com.editor.structures.graph.SocialNetwork;

import java.io.*;
import java.util.List;

//Make the dot string that will be saved in .dot file to visualize
public class GraphView {
    public String generateGraphViz(SocialNetwork network) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph SocialNetwork {\n");
        sb.append("    rankdir=BT;\n");
        sb.append("    node [shape=circle, style=filled, fillcolor=lightblue];\n");

        Graph graph = network.getGraph();

        // Loop on the nodes , for each node make its children(ID'S) point to it in the string
        // ex user 1 has followers 2 , 3 ,,  2->1 , 3->1
        for (int userId = 0; userId < graph.getSize(); userId++) {
            List<Integer> followers = graph.getNeighbors(userId);
            if (followers != null && !followers.isEmpty()) {
                sb.append("    { ");
                for (int followerId : followers) {
                    sb.append(followerId).append(" ");
                }
                sb.append("} -> ").append(userId).append(";\n");
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    public void run(SocialNetwork network, String outputImagePath) throws IOException, InterruptedException {
        //Get the DOT string
        String dotData = generateGraphViz(network);

        //Write DOT string to a temporary file
        File tempDotFile = File.createTempFile("graph", ".dot");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempDotFile))) {
            writer.write(dotData);
        }


        //Command: dot -Tjpg temp.dot -o output.jpg
        ProcessBuilder pb = new ProcessBuilder(
                "dot",
                "-Tjpg",
                tempDotFile.getAbsolutePath(),
                "-o",
                outputImagePath
        );

        // Redirect errors to console so you can see if GraphViz is missing
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Capture output for debugging
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("GraphViz: " + line);
            }
        }

        int exitCode = process.waitFor();

        // Clean up temp file
        tempDotFile.delete();

        if (exitCode == 0) {
            System.out.println("Graph saved successfully to: " + outputImagePath);
        } else {
            System.err.println("GraphViz exited with error code: " + exitCode);
        }
    }
}
