package com.editor.io;

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

    private CommandLineOptions() {}

    public static CommandLineOptions parse(String[] args)
    {
        CommandLineOptions options = new CommandLineOptions();
        options.command = args[0];

        for(int i = 1; i < args.length; i++){
            if(args[i].equals("-i") && i+1 < args.length){
                options.inputPath = args[i+1];
                i++;
            }
            else if(args[i].equals("-o") && i+1 < args.length){
                options.outputPath = args[i+1];
                i++;
            }

            // Need to add the other arguments same way.
        }


        return options;
    }

    public String getCommand()
    {
        return command;
    }
    public String getInputPath(){
        return inputPath;
    }
    public String getOutputPath(){
        return outputPath;
    }
    // Getters for other fields...
    //handle FIXing and any needed methods
}
