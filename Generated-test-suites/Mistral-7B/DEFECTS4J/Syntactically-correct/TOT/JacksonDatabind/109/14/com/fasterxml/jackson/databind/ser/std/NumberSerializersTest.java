import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonNode json = mapper.writeValueAsTree(new IntegerSerializer(Integer.class)
                .serialize(new Integer(10), null, null));
        assertEquals("10", json.get("@type").asText());
        assertEquals(10, json.get("numberValue").asInt());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonNode json = mapper.writeValueAsTree(new IntLikeSerializer().serialize(new Byte((byte) 5), null, null));
        assertEquals("5", json.get("@type").asText());
        assertEquals(5, json.get("numberValue").asInt());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonNode json = mapper.writeValueAsTree(new LongSerializer(Long.class)
                .serialize(new Long(100), null, null));
        assertEquals("100", json.get("@type").asText());
        assertEquals(100, json.get("numberValue").asLong());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonNode json = mapper.writeValueAsTree(new FloatSerializer().serialize(new Float(3.14f), null, null));
        assertEquals("3.14", json.get("@type").asText());
        assertEquals(3.14f, json.get("numberValue").asFloat(), 0.01f);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonNode json = mapper.writeValueAsTree(new DoubleSerializer(Double.class)
                .serialize(new Double(3.14159265358979323846), null, null));
        assertEquals("3.14159265358979323846", json.get("@type").asText());
        assertEquals(3.14159265358979323846, json.get("numberValue").asDouble(), 0.0000000000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedTypeSerializer() throws JsonProcessingException {
        mapper.writeValueAsTree(new IntegerSerializer(String.class)
                .serialize("Hello World", null, null));
    }
}