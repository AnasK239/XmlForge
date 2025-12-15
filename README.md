
## Prerequisites

Before running the application, ensure you have the following installed:


1. **Apache Maven**: Required to build and run the Java project.
    * [Download Maven](https://maven.apache.org/download.cgi)
    * Guide: How to install Maven on [Windows](https://phoenixnap.com/kb/install-maven-windows)
    * Add maven to system PATH
    * Run mvn -version in command prompt to be sure it's correctly installed.
2. **GraphViz**: Required for the Graph Visualization (draw) feature.
    * [Download GraphViz](https://graphviz.org/download/)
    * **Important (Windows):** During installation, select **"Add GraphViz to the system PATH for all users"**.

## Usage

You can now run the application using the `xml_editor` command.
Be careful that you are running these commands inside a WINDOWS Command Prompt. and that you are inside the directory XmlForge/demo.
### Command 1:
```
xml_editor draw -i src/main/resources/samples/sample.xml -o output.jpg
```
Parses the XML, builds the social network graph, and generates an image using GraphViz.


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