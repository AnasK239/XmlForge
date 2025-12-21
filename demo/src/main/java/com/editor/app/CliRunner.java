package com.editor.app;

import java.util.List;

import com.editor.analysis.GraphBuilder;
import com.editor.analysis.NetworkAnalyzer;
import com.editor.analysis.PostSearcher;
import com.editor.io.CommandLineOptions;
import com.editor.io.FileManager;
import com.editor.structures.graph.Post;
import com.editor.structures.graph.SocialNetwork;
import com.editor.structures.xml.XmlDocument;
import com.editor.ui.GraphView;
import com.editor.xml.parser.XmlValidator;
import com.editor.xml.formatter.XmlMinifier;
import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.converter.XmlToJson;
import com.editor.xml.parser.ValidationResult;
import com.editor.xml.parser.XmlParser;
import com.editor.compression.Compressor;


public class CliRunner {


    public static void run(String[] args) {
        // Parse CLI arguments
        CommandLineOptions opt = CommandLineOptions.parse(args);
        String cmd = opt.getCommand();

        // Switch based on the main command
        switch (cmd) {
            case "verify":
                runVerify(opt);
                break;
            case "format":
                runFormat(opt);
                break;
            case "minify":
            case "mini":
                runMinify(opt);
                break;
            case "compress":
                runCompress(opt);
                break;
            case "decompress":
                runDecompress(opt);
                break;
            case "json":
                runJson(opt);
                break;
            case "search":
                runSearch(opt);
                break;
            case "print":
                runPrint(opt);
                break;
            case "draw":
                runDraw(opt);
                break;
            case "most_active":
                runMostActive(opt);
                break;
            case "most_influencer":
                runMostPopular(opt);
                break;
            case "mutual":
                runMutual(opt);
                break;
            case "suggest":
                runSuggest(opt);
                break;
            default:
                System.out.println("Unknown command: " + cmd);
        }
    }

    // ----------------------- COMMAND HANDLERS -----------------------


    private static void runVerify(CommandLineOptions opt) {
        try {
            String xml = FileManager.readFile(opt.getInputPath());

            XmlValidator validator = new XmlValidator();
            ValidationResult result = validator.validate(xml);

            if (result.isValid()) {
                System.out.println("XML is VALID.");
            } 
            else {
                
                System.out.println("XML is NOT valid. Found " + result.getErrorCount() + " error(s):");
                int temp = result.getErrorCount()<10 ? result.getErrorCount():10;
                for (int i = 0; i < temp ; i++) {
                    System.out.println("  [Line " + result.getErrorLines().get(i) + "] " + result.getErrorMessages().get(i));
                }
                if(result.getErrorCount()>10)
                    System.out.println("And more...");
                // Fix XML if -f flag is used
                if (opt.isFixEnabled() && opt.getOutputPath() != null) {
                    String fixed = validator.fix(xml);
                    XmlFormatter formatter = new XmlFormatter();
                    fixed = formatter.formatString(fixed);
                    FileManager.writeFile(opt.getOutputPath(), fixed);
                    System.out.println("XML has been FIXED and saved to: " + opt.getOutputPath());
                }
            }

        } catch (Exception e) {
            System.out.println("Error during verify: " + e.getMessage());
        }
    }

    private static void runMinify(CommandLineOptions opt) {
        try {
            String xml = FileManager.readFile(opt.getInputPath());
            XmlDocument doc = new XmlParser().parse(xml);
            XmlMinifier minifier = new XmlMinifier();
            String minified = minifier.minify(doc);

            FileManager.writeFile(opt.getOutputPath(), minified);
            System.out.println("XML minification completed.");
            System.out.println("Saved to: " + opt.getOutputPath());

        } catch (Exception e) {
            System.out.println("Error during minify: " + e.getMessage());
        }
    }

    
    //  Formats XML nicely and writes output to file.
     
    private static void runFormat(CommandLineOptions opt) {
        try {
            String xml = FileManager.readFile(opt.getInputPath());
            XmlFormatter formatter = new XmlFormatter();
            String formatted = formatter.formatString(xml);
            FileManager.writeFile(opt.getOutputPath(), formatted);

            System.out.println("Formatting completed.");
            System.out.println("Saved to: " + opt.getOutputPath());

        } catch (Exception e) {
            System.out.println("Error during formatting: " + e.getMessage());
        }
    }

    
    //   XML compression logic.
     
    private static void runCompress(CommandLineOptions opt) {
        // first check the type of file
        String path = opt.getInputPath();
        if (path.endsWith(".xml")) {
            try {
                String xml = FileManager.readFile(opt.getInputPath());
                Compressor compressor = new Compressor();
                String compressed = compressor.compress(xml);
                FileManager.writeFile(opt.getOutputPath(), compressed);
                System.out.println("XML compression completed.");
                System.out.println("Saved to: " + opt.getOutputPath());
            } catch (Exception e) {
                System.out.println("Error during compression: " + e.getMessage());
            }
        } else {
            try {
                String xml = FileManager.readFile(opt.getInputPath());
                Compressor compressor = new Compressor();
                String compressed = compressor.compressJson(xml);
                FileManager.writeFile(opt.getOutputPath(), compressed);
                System.out.println("XML compression completed.");
                System.out.println("Saved to: " + opt.getOutputPath());
            } catch (Exception e) {
                System.out.println("Error during compression: " + e.getMessage());
            }
        }
    }


    // XML decompression logic.

    private static void runDecompress(CommandLineOptions opt) {
        // check the output type
        String path = opt.getOutputPath();

        if (path.endsWith(".xml")) {
            try {
                String compressed = FileManager.readFile(opt.getInputPath());
                Compressor compressor = new Compressor();
                String decompressed = compressor.decompress(compressed);
                FileManager.writeFile(opt.getOutputPath(), decompressed);
                System.out.println("XML decompression completed.");
                System.out.println("Saved to: " + opt.getOutputPath());
            } catch (Exception e) {
                System.out.println("Error during decompression: " + e.getMessage());
            }
        } else {
            try {
                String compressed = FileManager.readFile(opt.getInputPath());
                Compressor compressor = new Compressor();
                String decompressed = compressor.decompressToJson(compressed);
                FileManager.writeFile(opt.getOutputPath(), decompressed);
                System.out.println("JSON decompression completed.");
                System.out.println("Saved to: " + opt.getOutputPath());
            } catch (Exception e) {
                System.out.println("Error during decompression: " + e.getMessage());
            }
        }
    }

    // Converts XML to JSON and writes output.
    private static void runJson(CommandLineOptions opt) {
        try {
            String xml = FileManager.readFile(opt.getInputPath());
            XmlDocument doc = new XmlParser().parse(xml);
            XmlToJson converter = new XmlToJson();
            String json = converter.toJson(doc);
            FileManager.writeFile(opt.getOutputPath(), json);

            System.out.println("JSON conversion completed!");
            System.out.println("Saved to: " + opt.getOutputPath());

        } catch (Exception e) {
            System.out.println("Error during JSON conversion: " + e.getMessage());
        }
    }

    //  Placeholder for search functionality.
     
    private static void runSearch(CommandLineOptions opt) {
        String xml = FileManager.readFile(opt.getInputPath());
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(xml);
        GraphBuilder builder = new GraphBuilder();
        SocialNetwork network = builder.build(doc);
        PostSearcher searcher = new PostSearcher();
        if (opt.getWord() != null) {
            List<Post> posts = searcher.searchByWord(network, opt.getWord());
            for (Post post : posts) {
                System.out.println(post.getText());
            }

        } else {
            List<Post> posts = searcher.searchByTopic(network, opt.getTopic());
            for (Post post : posts) {
                System.out.println(post.getText());
            }
        } 

    }

    // Prints XML content directly to console.
    
    private static void runPrint(CommandLineOptions opt) {
        try {
            String xml = FileManager.readFile(opt.getInputPath());
            System.out.println(xml);

        } catch (Exception e) {
            System.out.println("Error in print: " + e.getMessage());
        }
    }

    // Generates visual image for the network graph.
     
    private static void runDraw(CommandLineOptions opt) {
        try {
            // 1. Read and Parse XML
            String xml ="";
            try {
                xml = FileManager.readFile(opt.getInputPath());
            } catch (Exception e) {
                System.out.println("Error in print: " + e.getMessage());
            }

            SocialNetwork network = new GraphBuilder().build(new XmlParser().parse(xml));
            System.out.println("Generating graph from: " + opt.getInputPath());

            // 2. Render Image
            if (network != null) {
                GraphView view = new GraphView();
                view.run(network, opt.getOutputPath());
            }

        } catch (Exception e) {
            System.out.println("Error generating graph: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runSuggest(CommandLineOptions opt){
        String xml = FileManager.readFile(opt.getInputPath());
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(xml);
        GraphBuilder builder = new GraphBuilder();
        SocialNetwork network = builder.build(doc);
        NetworkAnalyzer analyzer = new NetworkAnalyzer();
        List<Integer> suggestedUsers = analyzer.suggestUsersToFollowIds(network, opt.getSingleId());
        System.out.println("Suggested users: ");
        for (Integer user : suggestedUsers) {
            System.out.println(user);
        }
    }
    private static void runMostActive(CommandLineOptions opt){
        String xml = FileManager.readFile(opt.getInputPath());
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(xml);
        GraphBuilder builder = new GraphBuilder();
        SocialNetwork network = builder.build(doc);
        NetworkAnalyzer analyzer = new NetworkAnalyzer();
        System.out.println("Most active user id: "+analyzer.mostActiveId(network));
    }
    private static void runMostPopular(CommandLineOptions opt){
        String xml = FileManager.readFile(opt.getInputPath());
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(xml);
        GraphBuilder builder = new GraphBuilder();
        SocialNetwork network = builder.build(doc);
        NetworkAnalyzer analyzer = new NetworkAnalyzer();
        System.out.println("Most influencer user id: "+analyzer.mostInfluencerId(network));
    }
    private static void runMutual(CommandLineOptions opt){
        String xml = FileManager.readFile(opt.getInputPath());
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(xml);
        GraphBuilder builder = new GraphBuilder();
        SocialNetwork network = builder.build(doc);
        NetworkAnalyzer analyzer = new NetworkAnalyzer();
        List<Integer> mutualFollowers = analyzer.mutualFollowersIds(network, opt.getIds());
        for (Integer follower : mutualFollowers) {
            System.out.println("mutual connection: "+follower);
        }
    }

}
