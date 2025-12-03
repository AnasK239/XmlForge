package com.editor.compression;

import java.util.List;

public class PairTable {

    private String[] pairs;
    private String[] symbols;
    private int count;

    public PairTable(int capacity) {
        pairs = new String[capacity];
        symbols = new String[capacity];
        count = 0;
    }

    public void add(String pair, String symbol) {
        if (count < pairs.length) {
            pairs[count] = pair;
            symbols[count] = symbol;
            count++;
        }
    }

    public String findSymbol(String pair) {
        for (int i = 0; i < count; i++) {
            if (pairs[i].equals(pair)) {
                return symbols[i];
            }
        }
        return null;
    }

    public String findPair(String symbol) {
        for (int i = 0; i < count; i++) {
            if (symbols[i].equals(symbol)) {
                return pairs[i];
            }
        }
        return null;
    }

    public String getPair(int index) {
        if (index >= 0 && index < count) {
            return pairs[index];
        }
        return null;
    }

    public String getSymbol(int index) {
        if (index >= 0 && index < count) {
            return symbols[index];
        }
        return null;
    }

    public int getCount() {
        return count;
    }

    public String serialize() {
        String result = "";
        for (int i = 0; i < count; i++) {
            result += pairs[i] + ":" + symbols[i] + '\n';
        }
        return result;
    }

    public static PairTable deserialize(List<String> lines) {
        PairTable table = new PairTable(lines.size());
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                table.add(parts[0], parts[1]);
            }
        }
        return table;
    }
}
