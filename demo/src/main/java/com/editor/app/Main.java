package com.editor.app;
import com.editor.ui.MainApp;

public class Main extends MainApp {
    public static void main(String[] args) {
        if (args.length >0) {
            CliRunner.run(args);
        }
        else
        {
            MainApp.launch(args);
        }
    }
}