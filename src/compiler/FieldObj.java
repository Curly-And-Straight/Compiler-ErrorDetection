package compiler;

public class FieldObj extends Scopes{
    String type;

    public FieldObj(String type) {
        this.type = type;
    }

    public FieldObj(String id,String name) {
        super(id,name);
    }

    public FieldObj(String id,String name,String type) {
        super(id,name);
        this.type = type;
    }

    @Override
    public String toString() {
        return "FieldObj{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
