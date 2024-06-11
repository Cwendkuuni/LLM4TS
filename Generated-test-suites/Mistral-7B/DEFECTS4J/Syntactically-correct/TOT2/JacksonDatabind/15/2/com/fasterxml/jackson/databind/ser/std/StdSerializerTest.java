import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class StdSerializerTest {

    @Test
    public void testDefaultSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        String value = "Hello World";
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        String result = mapper.readValue(new StringReader(gen.getOutputContent()), String.class);
        assertEquals("Hello World", result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), String.class);
        assertTrue(schema.toString().contains("\"type\":\"string\""));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(SerializableFormat.FullSerializedForm.class, JsonType.STRING);
        serializer.acceptJsonFormatVisitor(visitor, JsonType.STRING);
        assertTrue(visitor.getFormatIds().toString().contains("STRING_FORMAT"));
    }

    @Test
    public void testWrapAndThrow() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);
        try {
            serializer.serialize(null, null, mapper.getSerializerProvider());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription desc = mapper.constructTypeDescription(List.class);
        StdSerializer<List<String>> serializer = mapper.findValueSerializer(desc.getType());
        JsonSerializer<Object> existingSerializer = mapper.findValueSerializer(Object.class);
        ObjectNode node = mapper.createObjectNode();
        JsonNode testNode = node.pojoValue(new MyClassWithListProperty());
        JsonSerializer<Object> convertingSerializer = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), null, testNode);
        assertNotNull(convertingSerializer);
        assertEquals(existingSerializer.getClass().getName(), convertingSerializer.getClass().getSuperclass().getName());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "testFilter", "testString");
        assertNotNull(filter);
    }
}

// Test helper class
class MyClassWithListProperty {
    private List<String> listProperty;
}