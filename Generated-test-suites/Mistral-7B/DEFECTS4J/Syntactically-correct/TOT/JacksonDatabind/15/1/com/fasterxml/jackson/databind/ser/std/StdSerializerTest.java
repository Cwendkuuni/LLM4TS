import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSimpleSerializer() throws JsonMappingException, JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        String value = "test";

        ObjectNode expectedSchema = serializer.getSchema(provider, String.class);
        ObjectNode actualSchema = mapper.readTree(serializer.getClass().getResource("/getSchema.json")); // Assuming getSchema.json contains expected schema
        assertEquals(expectedSchema, actualSchema);

        ObjectNode jsonNode = mapper.createObjectNode();
        serializer.serialize(value, mapper.getJsonFactory().createJsonGenerator(jsonNode), provider);
        ObjectNode serializedNode = mapper.readTree(jsonNode.toString());
        assertEquals("test", serializedNode.getTextValue());

        // Test serialization with null value
        serializer.serialize(null, mapper.getJsonFactory().createJsonGenerator(jsonNode), provider);
        assertEquals("null", serializedNode.getTextValue());

        // Test serialization with exception
        try {
            serializer.serialize(new IOException("Test Exception"), mapper.getJsonFactory().createJsonGenerator(jsonNode), provider);
        } catch (JsonMappingException e) {
            assertNotNull(e);
        }

        // Test deserialization
        String deserializedValue = mapper.readValue(serializedNode.toString(), String.class);
        assertEquals("test", deserializedValue);

        // Test deserialization with null value
        String deserializedNullValue = mapper.readValue(mapper.createObjectNode().put("null", null).toString(), String.class);
        assertNull(deserializedNullValue);
    }
}