package App.EditParameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Serialize {
    public static String serialize(ArrayList<Parameter> parameters) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode object = mapper.createObjectNode();

        for (Parameter p : parameters) {
            object.put(p.name, p.value);
        }

        try {
            return mapper.writeValueAsString(object);
        }
        catch(JsonProcessingException e) {
            System.err.println("Unable to process json objects");
        }

        return "";
    }


    public static void flushRunToDisk() {

    }
}
