package App.EditParameters;

import java.util.Vector;

public class Parameter {
    public ParameterType type;
    public String name;
    public String description;
    public String value;

    public Parameter(ParameterType type, String name, String description, String value) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.value = value;
    }
}
