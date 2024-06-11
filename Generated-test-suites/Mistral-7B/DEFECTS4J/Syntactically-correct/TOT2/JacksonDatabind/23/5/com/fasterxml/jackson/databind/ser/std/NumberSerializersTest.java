import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = mapper.findValueSerializer(Integer.class);

        // When
        JsonNode result = mapper.valueToTree(123);

        // Then
        JsonNode expected = mapper.valueToTree(123);
        assertEquals(expected, result);
    }

    @Test
    public void testIntegerSerializerWithType() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = mapper.findValueSerializer(Integer.class);

        // When
        JsonNode result = mapper.valueToTree(123, new TypeSerializer());

        // Then
        JsonNode expected = mapper.valueToTree(123);
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializerWithNonInteger() throws JsonProcessingException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = mapper.findValueSerializer(Integer.class);

        // When & Then
        try {
            JsonNode result = mapper.valueToTree("not an integer");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Similar test cases for other serializers...
}