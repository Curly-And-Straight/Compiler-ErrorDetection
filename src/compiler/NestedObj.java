package compiler;

public class NestedObj extends  Scopes{
    public NestedObj(String name){
        super(name);
    }

    @Override
    public String toString() {
        return "NestedObj{" +
                "name='" + name + '\'' +
                '}';
    }
}
