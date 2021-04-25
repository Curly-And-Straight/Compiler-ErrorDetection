package compiler;

public class ClassObj {

    String id ,name,inherit_class;

    public ClassObj(){
        this.id = "class";
    }

    public ClassObj(String name) {
        this.id = "class";
        this.name = name;
    }
    public ClassObj(String name, String inherit_class) {
        this.id = "class";
        this.name = name;
        this.inherit_class = inherit_class;
    }

    @Override
    public String toString() {
        return "ClassObj{" +
                "name='" + name + '\'' +
                ", inherit_class='" + inherit_class + '\'' +
                '}';
    }
}
