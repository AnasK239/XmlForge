package com.editor;

import com.editor.compression.BpeCompressor;

public class TestCompressionMain {
    public static void main(String[] args) {
        // try {
        // String xml = "<users><user
        // id='1'><posts><p>Hello</p></posts></user></users>";
        // BpeCompressor comp = new BpeCompressor();

        // String compressed = comp.compress(xml);
        // System.out.println("--- COMPRESSED ---\n" + compressed);

        // String decompressed = comp.decompress(compressed);
        // System.out.println("--- DECOMPRESSED ---\n" + decompressed);

        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        BpeCompressor bpe = new BpeCompressor();
        String original = "<users>" + //
                "<user>" + //
                "<id>1</id>" + //
                "<name>Ahmed Ali</name>" + //
                "<posts>" + //
                "<post>" + //
                "<body>" + //
                "Test" + //
                "</body>" + //
                "<topics>" + //
                "<topic>" + //
                "economy" + //
                "</topic>" + //
                "<topic>" + //
                "finance" + //
                "</topic>" + //
                "</topics>" + //
                "</post>" + //
                "<post>" + //
                "<body>" + //
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                + //
                "</body>" + //
                "<topics>" + //
                "<topic>" + //
                "solar_energy" + //
                "</topic>" + //
                "</topics>" + //
                "</post>" + //
                "</posts>" + //
                "<followers>" + //
                "<follower>" + //
                "<id>2</id>" + //
                "</follower>" + //
                "<follower>" + //
                "<id>3</id>" + //
                "</follower>" + //
                "</followers>" + //
                "</user>" + //
                "<user>" + //
                "<id>2</id>" + //
                "<name>Yasser Ahmed</name>" + //
                "<posts>" + //
                "<post>" + //
                "<body>" + //
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                + //
                "</body>" + //
                "<topics>" + //
                "<topic>" + //
                "education" + //
                "</topic>" + //
                "</topics>" + //
                "</post>" + //
                "</posts>" + //
                "<followers>" + //
                "<follower>" + //
                "<id>1</id>" + //
                "</follower>" + //
                "</followers>" + //
                "</user>" + //
                "<user>" + //
                "<id>3</id>" + //
                "<name>Mohamed Sherif</name>" + //
                "<posts>" + //
                "<post>" + //
                "<body>" + //
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                + //
                "</body>" + //
                "<topics>" + //
                "<topic>" + //
                "sports" + //
                "</topic>" + //
                "</topics>" + //
                "</post>" + //
                "</posts>" + //
                "<followers>" + //
                "<follower>" + //
                "<id>1</id>" + //
                "</follower>" + //
                "</followers>" + //
                "</user>" + //
                "</users>";
        String compressed = bpe.compress(original);
        System.out.println("Compressed:" + compressed);
        String decompressed = bpe.decompress(compressed);
        System.out.println("Decompressed:" + decompressed);
        System.out.println("Match: " + original.equals(decompressed));
        // compare sizes
        System.out.println("Original size: " + original.length());
        System.out.println("Compressed size: " + compressed.length());
    }
}
