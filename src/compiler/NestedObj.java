package compiler;

public class NestedObj extends  ClassObj{
    String name;
    public NestedObj(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "NestedObj{" +
                "name='" + name + '\'' +
                '}';
    }
}
