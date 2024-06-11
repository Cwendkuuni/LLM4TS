import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer number = 123;
        String json = mapper.writerWithView(JsonSerializationView.StandardSerializers.CLASS_VIEW)
                .writeValueAsString(number);
        assertEquals("123", json);

        JsonNode deserialized = mapper.readTree(json);
        Integer deserializedNumber = deserialized.asInt();
        assertEquals(number, deserializedNumber);

        try {
            deserializedNumber = deserialized.asLong();
            fail("Expected IntDeserializer to fail when deserializing to long");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float number = 123.45f;
        String json = mapper.writerWithView(JsonSerializationView.StandardSerializers.CLASS_VIEW)
                .writeValueAsString(number);
        assertEquals("123.45", json);

        JsonNode deserialized = mapper.readTree(json);
        Float deserializedNumber = deserialized.asFloat();
        assertEquals(number, deserializedNumber);

        try {
            deserializedNumber = deserialized.asInt();
            fail("Expected FloatDeserializer to fail when deserializing to int");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long number = 1234567890L;
        String json = mapper.writerWithView(JsonSerializationView.StandardSerializers.CLASS_VIEW)
                .writeValueAsString(number);
        assertEquals("1234567890", json);

        JsonNode deserialized = mapper.readTree(json);
        Long deserializedNumber = deserialized.asLong();
        assertEquals(number, deserializedNumber);

        try {
            deserializedNumber = deserialized.asInt();
            fail("Expected LongDeserializer to fail when deserializing to int");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short number = Short.parseShort("123");
        String json = mapper.writerWithView(JsonSerializationView.StandardSerializers.CLASS_VIEW)
                .writeValueAsString(number);
        assertEquals("123", json);

        JsonNode deserialized = mapper.readTree(json);
        Short deserializedNumber = deserialized.asShort();
        assertEquals(number, deserializedNumber);

        try {
            deserializedNumber = deserialized.asInt();
            fail("Expected ShortDeserializer to fail when deserializing to int");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double number = 123.456789;
        String json = mapper.writerWithView(JsonSerializationView.StandardSerializers.CLASS_VIEW)
                .writeValueAsString(number);
        assertEquals("123.456789", json);

        JsonNode deserialized = mapper.readTree(json);
        Double deserializedNumber = deserialized.asDouble();
        assertEquals(number, deserializedNumber, 0.00001); // allow small delta for comparison

        try {
            deserializedNumber = deserialized.asInt();
            fail("Expected DoubleDeserializer to fail when deserializing to int");
        } catch (JsonProcessingException e) {
            // expected
        }
    }
}