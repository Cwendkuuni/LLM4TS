import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StdSerializerTest {

    @Test
    public void testSerialize() throws JsonMappingException, JsonGenerationException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        SerializerProvider provider = new JsonTestSerializerProvider();
        serializer.serialize("test", provider.getJsonGenerator(node), provider);
        assertEquals("test", node.get("test").asText());
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }
        };
        JsonNode schema = serializer.getSchema(new JsonTestSerializerProvider(), String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrow() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                throw new RuntimeException("Test exception");
            }
        };
        try {
            serializer.wrapAndThrow(new JsonTestSerializerProvider(), new RuntimeException("Test exception"), null, null);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}