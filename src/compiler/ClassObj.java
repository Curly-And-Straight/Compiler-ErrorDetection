package compiler;

public class ClassObj extends Scopes{

    String inherit_class;

    public ClassObj(){

    }

    public ClassObj(String name) {
        super(name);

    }

    public ClassObj(String id,String name) {
        super(id,name);

    }
    public ClassObj(String id,String name,String inherit_class) {
        super(id,name);
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
