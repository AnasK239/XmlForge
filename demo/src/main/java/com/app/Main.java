package com.app;
import com.ui.MainApp;

public class Main {
    public static void main(String[] args) {
        if (args.length >0) {
            CliRunner cliRunner = new CliRunner();
            cliRunner.run(args);
            // Add logic to utilize cliRunner with args
        }
        else
        {
            // go to the GUI
            MainApp mainApp = new MainApp();
            mainApp.launch();
        }
    }
}