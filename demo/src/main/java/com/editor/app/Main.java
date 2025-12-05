package com.editor.app;
import com.editor.ui.MainApp;

/**
 * Entry point of the application.
 * Decides whether to run the CLI or launch the GUI based on command-line arguments.
 */
public class Main {

    public static void main(String[] args) {
        // ------------------ CLI Mode ------------------
        // If there are any arguments passed, treat the application as CLI.
        // Example usage: java -jar app.jar verify -i input.xml -o output.xml -f
        if (args.length > 0) {
            // CliRunner handles all command parsing and execution
            // It reads the input file, executes the requested command,
            // and writes the output if required.
            CliRunner.run(args);

            // Notes:
            // - No need to create a CliRunner instance since `run` is static.
            // - The `args` array is passed directly to CliRunner,
            //   which internally uses CommandLineOptions.parse(args)
            //   to decide which command handler to invoke.
        } 
        // ------------------ GUI Mode ------------------
        // If no arguments are provided, assume user wants the GUI version.
        else {
            // MainApp is the JavaFX or Swing GUI entry point.
            // This launches the interactive application for users
            // who prefer not to use CLI commands.
            MainApp mainApp = new MainApp();
            mainApp.launch();

            // Notes:
            // - GUI handles file selection, formatting, verification, etc.
            //   visually.
            // - This separation allows the same application to support
            //   both CLI automation and interactive usage.
        }
    }
}
