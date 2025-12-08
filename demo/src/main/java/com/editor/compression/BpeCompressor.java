package com.editor.compression;

import java.util.Scanner;

public class BpeCompressor {

    private static final int MAX_ITERATIONS = 100;
    private static final int TOKEN_START = 256;

    public String compress(String input) {

        if (input == null || input.isEmpty())
            return "";
        // add minifier here later

        PairTable pt = new PairTable(MAX_ITERATIONS);
        // System.err.println(input);
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            String bestPair = findMostFrequentPair(input);

            if (bestPair == null)
                break;

            String token = Character.toString((char) (TOKEN_START + i));

            pt.add(bestPair, token);

            input = input.replace(bestPair, token);
        }

        StringBuilder output = new StringBuilder();

        output.append("BPE1\n");

        output.append(pt.getCount()).append("\n");
        for (int i = 0; i < pt.getCount(); i++) {
            output.append(pt.getPair(i))
                    .append("->")
                    .append(pt.getSymbol(i))
                    .append("\n");
        }

        output.append("----\n");

        output.append(input);

        return output.toString();
    }

    public String decompress(String compressed) throws IllegalArgumentException {
        if (compressed == null || compressed.isEmpty())
            return "";

        Scanner scanner = new Scanner(compressed);

        if (!scanner.hasNextLine() || !scanner.nextLine().trim().equals("BPE1")) {
            scanner.close();
            throw new IllegalArgumentException("Invalid file format: Missing BPE1 header");
        }

        int n = 0;
        try {
            String nLine = scanner.nextLine().trim();
            n = Integer.parseInt(nLine);
        } catch (NumberFormatException | java.util.NoSuchElementException e) {
            scanner.close();
            throw new IllegalArgumentException("Invalid file format: Missing rule count");
        }

        PairTable pt = new PairTable(n);

        for (int i = 0; i < n; i++) {
            if (!scanner.hasNextLine())
                break;
            String line = scanner.nextLine();

            int splitIndex = line.lastIndexOf("->");
            if (splitIndex != -1) {
                String pair = line.substring(0, splitIndex);
                String token = line.substring(splitIndex + 2);
                pt.add(pair, token);
            }
        }
        System.err.println("Decompression rules loaded: " + pt.getCount());
        if (scanner.hasNextLine()) {
            String separator = scanner.nextLine().trim();
            if (!separator.equals("----")) {
                scanner.close();
                throw new IllegalArgumentException("Invalid file format: Missing separator");
            }
        }

        scanner.useDelimiter("\\A");
        String body = scanner.hasNext() ? scanner.next() : "";

        if (body.startsWith("\n")) {
            body = body.substring(1);
        }

        for (int i = pt.getCount() - 1; i >= 0; i--) {
            String token = pt.getSymbol(i);
            String pair = pt.getPair(i);
            body = body.replace(token, pair);
        }


        scanner.close();
        return body;
    }

    private String findMostFrequentPair(String text) {
        String bestPair = null;
        int bestCount = 1;
        int length = text.length();
        for (int i = 0; i < length - 1; i++) {
            String pair = text.substring(i, i + 2);
            int count = 0;
            for (int j = 0; j < length - 1; j++) {
                if (text.substring(j, j + 2).equals(pair)) {
                    count++;
                }
            }
            if (count > bestCount) {
                bestCount = count;
                bestPair = pair;
            }
        }
        return bestPair;
    }

}
