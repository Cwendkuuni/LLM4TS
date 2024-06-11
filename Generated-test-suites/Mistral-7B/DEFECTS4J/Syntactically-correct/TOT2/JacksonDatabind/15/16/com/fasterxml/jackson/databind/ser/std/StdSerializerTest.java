import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdSerializerTest {
    private ObjectMapper objectMapper;
    private StdSerializer<String> stdSerializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        stdSerializer = objectMapper.findValueSerializer(String.class);
    }

    @Test
    public void testSerializeString() throws JsonGenerationException {
        String input = "Hello World";
        ObjectNode output = objectMapper.createObjectNode();
        stdSerializer.serialize(input, objectMapper.getFactory().createGenerator(output), objectMapper.getSerializerProvider());
        String actual = output.get("@type").asText();
        assertEquals("string", actual);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectNode schema = stdSerializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        String actual = schema.get("type").asText();
        assertEquals("string", actual);
    }
}