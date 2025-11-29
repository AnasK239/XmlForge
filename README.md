```
src/main/java/com/editor
├── app                        (Application Entry)
│   ├── Main.java
│   └── CliRunner.java
├── structures                 (Data Structures)
│   ├── xml                    (XML Data Structures)
│   │   ├── XmlDocument.java   
│   │   ├── XmlNode.java
│   │   └── XmlAttribute.java
│   └── graph                  (Graph Data Structures)
│       ├── SocialNetwork.java 
│       ├── Graph.java
│       ├── UserNode.java
│       ├── Post.java
│       └── UserDirectory.java
├── xml                        (XML Processing Logic)
│   ├── parser
│   │   ├── XmlParser.java
│   │   ├── XmlTokenizer.java
│   │   └── XmlValidator.java
│   ├── formatter
│   │   ├── XmlFormatter.java
│   │   └── XmlMinifier.java
│   └── converter
│       └── XmlToJson.java
├── analysis                   (Algorithms & Business Logic)
│   ├── NetworkAnalyzer.java
│   ├── PostSearcher.java
│   └── GraphBuilder.java      
├── io                         (Input/Output)
│   ├── FileManager.java       
│   └── CommandLineOptions.java
├── compression                (Compression Logic)
│   ├── Compressor.java
│   ├── BpeCompressor.java
│   └── PairTable.java
├── ui                         (User Interface)
│   ├── MainApp.java
│   ├── MainController.java
│   └── GraphView.java
└── util                         (Misc)
    ├── JsonBuilder.java
    └── StringUtils.java
```