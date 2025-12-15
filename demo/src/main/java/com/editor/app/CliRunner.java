package com.editor.app;

import com.editor.analysis.GraphBuilder;
import com.editor.io.CommandLineOptions;
import com.editor.io.FileManager;
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

/**
 * Handles CLI execution for commands like verify, minify, format, json, etc.
 * Uses CommandLineOptions to decide which function to run.
 */
public class CliRunner {

    /**
     * Entry point to run a CLI command with given arguments.
     * @param args command-line arguments
     */
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
            default:
                System.out.println("Unknown command: " + cmd);
        }
    }

    // ----------------------- COMMAND HANDLERS -----------------------

    /**
     * Validates XML file and optionally fixes errors.
     */
    private static void runVerify(CommandLineOptions opt) {
        try {
            String xml = FileManager.readFile(opt.getInputPath());

            XmlValidator validator = new XmlValidator();
            ValidationResult result = validator.validate(xml);

            if (result.isValid()) {
                System.out.println("XML is VALID.");
            } else {
                System.out.println("XML is INVALID!");
                System.out.println("Errors found: " + result.getErrorCount());
                System.out.println("Error lines: " + result.getErrorLines());

                // Fix XML if -f flag is used
                if (opt.isFixEnabled() && opt.getOutputPath() != null) {
                    String fixed = validator.fix(xml);
                    FileManager.writeFile(opt.getOutputPath(), fixed);
                    System.out.println("XML has been FIXED and saved to: " + opt.getOutputPath());
                }
            }

        } catch (Exception e) {
            System.out.println("Error during verify: " + e.getMessage());
        }
    }

    /**
     * Minifies XML and writes output to file.
     */
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

    /**
     * Formats XML nicely and writes output to file.
     */
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

    /**
     * Placeholder for future XML compression logic.
     */
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

    /**
     * Placeholder for future XML decompression logic.
     */
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

    /**
     * Converts XML to JSON and writes output.
     */
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

    /**
     * Placeholder for search functionality.
     */
    private static void runSearch(CommandLineOptions opt) {
        System.out.println("Search feature is Level 2 (post search).");
        System.out.println("Word: " + opt.getWord());
        System.out.println("Topic: " + opt.getTopic());
    }

    /**
     * Prints XML content directly to console.
     */
    private static void runPrint(CommandLineOptions opt) {
        try {
            String xml = FileManager.readFile(opt.getInputPath());
            System.out.println(xml);

        } catch (Exception e) {
            System.out.println("Error in print: " + e.getMessage());
        }
    }

    /**
     * Generates visual image for the network graph.
     */
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
}
