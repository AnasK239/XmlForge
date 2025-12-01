package com.editor.app;

import com.editor.io.CommandLineOptions;

public class CliRunner {
    public static void run(String[] args) {
        System.out.println("Running in CLI mode with arguments:");

        CommandLineOptions Options = CommandLineOptions.parse(args);

        if(Options.getCommand() == "mini"){
            String filepath = Options.getInputPath();
            // Read the input XML File from its path.
            // Convert to XMLDOCUMENT
            // Call .toStringFormatted("mini") on the document
            String outputpath = Options.getOutputPath();
            // Save the output file
        }

        // verfiy the other options and execute corresponding actions
        // format output to console or files as needed
    }
    private static void runVerify(CommandLineOptions options) {
        // FileManager.readFile
        //XmlValidator.validate
        // if there are errors, call XmlValidator.fix
    }

    // Additional private methods for other CLI operations can be added here
    // check PDF for all the options needed
    //each of these just wires services together; no business logic here.

}
