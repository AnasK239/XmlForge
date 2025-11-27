package com.analysis;
import com.model.graph.Graph;
import com.model.graph.UserDirectory;
import com.model.xml.XmlDoc;
public class SocialNetworkGraphBuilder {
    // Parse XML into UserNodes and our Graph.

    public SocialNetwork build(XmlDoc doc){}

    /*
    Internals:

        Scan XML tree for <user> tags:

        Read id, name.

        Read followers (list of ids).

        Read posts & topics.
            
    */ 
}


class SocialNetwork {
    // Represents the social network graph.
    UserDirectory userDirectory;
    Graph graph;

    // add what is needed 
}