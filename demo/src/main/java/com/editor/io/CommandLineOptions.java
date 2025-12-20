package com.editor.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses and stores command-line arguments for the CLI application.
 * Supports commands and flags like -i, -o, -f, -id, -ids, -w, -t
 */
public class CommandLineOptions {

    private String command;        // The main command (e.g., verify, minify)
    private String inputPath;      // Input file path
    private String outputPath;     // Output file path
    private boolean fix;           // -f flag to fix XML errors automatically
    private int[] ids;             // -ids multiple IDs
    private Integer singleId;      // -id single ID
    private String word;           // -w search word
    private String topic;          // -t search topic

    private CommandLineOptions() {} 

    public static CommandLineOptions parse(String[] args) {

        CommandLineOptions opt = new CommandLineOptions();

        if (args.length == 0) {
            throw new IllegalArgumentException("No command provided!");
        }

        // The first argument is always the command
        opt.command = args[0];

        List<Integer> idList = new ArrayList<>();

        // Loop through remaining arguments to parse flags
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    if (++i < args.length)
                        opt.inputPath = args[i];
                    break;

                case "-o":
                    if (++i < args.length)
                        opt.outputPath = args[i];
                    break;

                case "-f":
                    opt.fix = true;
                    break;

                case "-id":        
                    if (++i < args.length)
                        opt.singleId = Integer.parseInt(args[i]);
                    break;

                case "-ids":      
                    if (++i < args.length) {
                        String[] idStrings = args[i].split(",");
                        for (String s : idStrings) {
                            idList.add(Integer.parseInt(s.trim()));
                        }
                    }
                    break;

                case "-w":
                    if (++i < args.length)
                        opt.word = args[i];
                    break;

                case "-t":
                    if (++i < args.length)
                        opt.topic = args[i];
                    break;

                default:
                    throw new IllegalArgumentException("Unknown flag: " + args[i]);
            }
        }

        // Convert List<Integer> to int[]
        opt.ids = idList.stream().mapToInt(Integer::intValue).toArray();
        return opt;
    }


    public String getCommand() { return command; }
    public String getInputPath() { return inputPath; }
    public String getOutputPath() { return outputPath; }
    public boolean isFixEnabled() { return fix; }
    public int[] getIds() { return ids; }
    public Integer getSingleId() { return singleId; }
    public String getWord() { return word; }
    public String getTopic() { return topic; }
}
