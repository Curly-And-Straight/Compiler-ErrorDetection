package compiler;

import gen.CoolParser;

import java.util.ArrayList;
import java.util.List;

public class MethodObj extends Scopes {
    String return_type;
    List<CoolParser.ParameterContext> params;


    public MethodObj(String id,String name, String return_type, List<CoolParser.ParameterContext> params) {
        super(id,name);
        this.return_type = return_type;
        this.params = params;
    }

    @Override
    public String toString() {
        ArrayList parameters = new ArrayList();
        for (int i = 0; i < params.size(); i++) {
            parameters.add(params.get(i).getText());
        }
        return "MethodObj{" +
                "name='" + name + '\'' +
                ", return_type='" + return_type + '\'' +
                ", params=" + parameters.toString() +
                '}';
    }
}
