package compiler;

public class FieldObj extends ClassObj{
    String id,name,type;

    public FieldObj(String name, String type,String id) {
        this.id = id;
        this.name = name;
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
