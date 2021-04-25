package compiler;


import java.util.Hashtable;

public class Node {

    public Node parent;
    public String name;
    public Hashtable<String,Scopes> symbolTable = new Hashtable();

    public Node(Node parent, String name){
        this.parent = parent;
        this.name = name;
    }

    public Node(String name){
        this.name = name;
    }

    public void insert(String key, Scopes value){
        symbolTable.put(key,value);
    }

    public Scopes lookup(String key){
        return symbolTable.get(key);
    }

}
