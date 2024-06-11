import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonGenerator;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        StdKeySerializer stdKeySerializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = rootNode.startObject();

        Object value = "testValue";
        stdKeySerializer.serialize(value, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();

        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
        assertEquals("testValue", jsonParser.getValueAsString());
    }
}