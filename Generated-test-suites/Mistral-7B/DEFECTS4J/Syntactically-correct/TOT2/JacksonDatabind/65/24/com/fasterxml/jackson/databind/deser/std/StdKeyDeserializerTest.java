import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;

public class StdKeyDeserializerTest {

    @Test
    public void testIntKeyDeserialization() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        String key = "42";

        // When
        Integer deserializedKey = mapper.readValue("{\"key\":\"" + key + "\"}", Integer.class, new DeserializationContext());
        Object keyInstance = keyDeserializer.deserializeKey(key, new DeserializationContext(mapper.getConfiguration()));

        // Then
        assertEquals(deserializedKey, keyInstance);
    }

    @Test
    public void testNullKeyDeserialization() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        String key = null;

        // When
        Object keyInstance = keyDeserializer.deserializeKey(key, new DeserializationContext(mapper.getConfiguration()));

        // Then
        assertNull(keyInstance);
    }

    @Test(expected = IOException.class)
    public void testInvalidIntKeyDeserialization() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        String key = "invalid_key";

        // When & Then
        keyDeserializer.deserializeKey(key, new DeserializationContext(mapper.getConfiguration()));
    }

    // Add similar test cases for other types (Boolean, Byte, Short, Character, etc.)
}