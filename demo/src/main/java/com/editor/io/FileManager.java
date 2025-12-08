package com.editor.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileManager {


    public static String readFile(String path)  {
        // Convert the String path to a Path object and read all bytes
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }   

    }


    public static void writeFile(String path, String content)  {
        // Write bytes to the file, creating/overwriting it
        try {
            Files.write(Paths.get(path), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
