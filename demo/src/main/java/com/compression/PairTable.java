package com.compression;

import java.util.List;

public class PairTable {
    //Custom DS instead of HashMap. Stores pairs → replacement token.
    private List<String> pairList; // like "ab"
    private List<String> symbolList; // like "Z1"


    public void addPair(String pair, String symbol)
    public String findSymbol(String pair) // linear search
    public int size()
    public String serialize()
}
