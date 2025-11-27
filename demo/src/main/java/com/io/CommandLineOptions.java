package com.io;

public class CommandLineOptions {
    // Parse and store CLI args (verify, format, -i, -o, -f, -ids, -w, -t, -id).

    private String command;
    private String inputPath;
    private String outputPath;
    private boolean fix;
    private int[] ids;
    private Integer singleId;
    private String word;
    private String topic;

    public static CommandLineOptions parse(String[] args)
    {

    }
    String getCommand()
    {
        return command;
    }
    // Getters for other fields...
    //handle FIXing and any needed methods
}
