package compiler;

public class Scopes {
    String id;
    String name;

    public Scopes(){

    }

    public Scopes(String name){
        this.name = name;
    }

    public Scopes(String id,String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
