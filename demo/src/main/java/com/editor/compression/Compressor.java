package com.editor.compression;

public class Compressor {
    //Role: API for compression.
    String compress(String input){
        String bpeCompressed = new BpeCompressor().compress(input);
        return bpeCompressed;
    }
    String decompress(String compressed){
        String bpeDecompressed = new BpeCompressor().decompress(compressed);
        return bpeDecompressed;
    }
}
