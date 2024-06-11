import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    @Test
    public void testDefaultSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        String value = "Hello World";
        String json = serializer.serializeValue(value, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        assertEquals("\"Hello World\"", json);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Integer> serializer = mapper.findValueSerializer(Integer.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Integer.TYPE);
        assertEquals("{\"type\":\"integer\"}", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Double> serializer = mapper.findValueSerializer(Double.class);
        JsonFormatVisitorWrapper wrapper = new JsonFormatVisitorWrapper(mapper.getSerializerProvider().getSerializerProvider());
        serializer.acceptJsonFormatVisitor(wrapper, Double.TYPE);
        assertTrue(wrapper.isAccepted(JsonFormat.Feature.AUTO_UNBOX_NON_NUMERIC));
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        try {
            serializer.serialize(null, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIsDefaultSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Integer> defaultSerializer = mapper.findValueSerializer(Integer.class);
        JsonSerializer<Integer> customSerializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeNumber(value.intValue() * 2);
            }
        };
        assertTrue(StdSerializer.this.isDefaultSerializer(defaultSerializer));
        assertFalse(StdSerializer.this.isDefaultSerializer(customSerializer));
    }
}