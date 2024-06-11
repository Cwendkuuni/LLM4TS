import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {
    // Test for addAll method
    @Test
    public void testAddAll() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(6, allDeserializers.size());
    }

    // Test for IntegerSerializer
    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> integerSerializer = NumberSerializers.findValueSerializer(mapper, Integer.class);
        Integer number = 123;
        String jsonString = mapper.writerWithRootValueSerializer(integerSerializer).writeValueAsString(number);
        Integer deserializedNumber = mapper.readValue(jsonString, Integer.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for IntegerSerializer with negative number
    @Test
    public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> integerSerializer = NumberSerializers.findValueSerializer(mapper, Integer.class);
        Integer number = -123;
        String jsonString = mapper.writerWithRootValueSerializer(integerSerializer).writeValueAsString(number);
        Integer deserializedNumber = mapper.readValue(jsonString, Integer.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for ShortSerializer
    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Short> shortSerializer = NumberSerializers.findValueSerializer(mapper, Short.class);
        Short number = 127;
        String jsonString = mapper.writerWithRootValueSerializer(shortSerializer).writeValueAsString(number);
        Short deserializedNumber = mapper.readValue(jsonString, Short.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for ShortSerializer with minimum short value
    @Test
    public void testShortSerializerMinValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Short> shortSerializer = NumberSerializers.findValueSerializer(mapper, Short.class);
        Short number = Short.MIN_VALUE;
        String jsonString = mapper.writerWithRootValueSerializer(shortSerializer).writeValueAsString(number);
        Short deserializedNumber = mapper.readValue(jsonString, Short.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for LongSerializer
    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Long> longSerializer = NumberSerializers.findValueSerializer(mapper, Long.class);
        Long number = 9223372036854775807L; // Long.MAX_VALUE
        String jsonString = mapper.writerWithRootValueSerializer(longSerializer).writeValueAsString(number);
        Long deserializedNumber = mapper.readValue(jsonString, Long.class);
        assertEquals(number, deserializedNumber);
    }

    // Test for LongSerializer with exception handling
    @Test(expected = JsonMappingException.class)
    public void testLongSerializerException() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Long> longSerializer = NumberSerializers.findValueSerializer(mapper, Long.class);
        Long number = Long.MAX_VALUE + 1; // Long.MAX_VALUE + 1 is not representable as a Long
        String jsonString = mapper.writerWithRootValueSerializer(longSerializer).writeValueAsString(number);
        mapper.readValue(jsonString, Long.class);
    }
}