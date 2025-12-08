package com.editor.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for reading and writing files.
 * Encapsulates basic file I/O operations using NIO.
 */
public class FileManager {

    /**
     * Reads the entire content of a file and returns it as a String.
     * @param path Path to the input file
     * @return file content as String
     * @throws IOException if the file cannot be read
     */
    public static String readFile(String path) throws IOException {
        // Convert the String path to a Path object and read all bytes
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    /**
     * Writes a string content to a file.
     * @param path Path to the output file
     * @param content String content to write
     * @throws IOException if the file cannot be written
     */
    public static void writeFile(String path, String content) throws IOException {
        // Write bytes to the file, creating/overwriting it
        Files.write(Paths.get(path), content.getBytes());
    }
}
