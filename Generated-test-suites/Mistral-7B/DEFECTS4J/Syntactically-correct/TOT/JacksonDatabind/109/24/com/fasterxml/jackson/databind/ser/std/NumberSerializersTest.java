import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);

        // Test serialization of positive integers
        Integer intValue = 123;
        String json = mapper.writerWithRootValueSerializer(integerSerializer).writeValueAsString(intValue);
        assertEquals("123", json);

        // Test serialization of negative integers
        Integer negativeIntValue = -123;
        json = mapper.writerWithRootValueSerializer(integerSerializer).writeValueAsString(negativeIntValue);
        assertEquals("-123", json);

        // Test serialization of max and min integers
        Integer maxIntValue = Integer.MAX_VALUE;
        json = mapper.writerWithRootValueSerializer(integerSerializer).writeValueAsString(maxIntValue);
        assertEquals(String.valueOf(Integer.MAX_VALUE), json);

        Integer minIntValue = Integer.MIN_VALUE;
        json = mapper.writerWithRootValueSerializer(integerSerializer).writeValueAsString(minIntValue);
        assertEquals(String.valueOf(Integer.MIN_VALUE), json);

        // Test serialization of zero
        Integer zeroValue = 0;
        json = mapper.writerWithRootValueSerializer(integerSerializer).writeValueAsString(zeroValue);
        assertEquals("0", json);

        // Test deserialization from JSON String
        Integer deserializedInt = mapper.readValue("123", Integer.class);
        assertEquals(123, deserializedInt.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonSerializer<Number> intLikeSerializer = mapper.findValueSerializer(Number.class);

        // Test serialization of positive integers
        Byte byteValue = 123;
        Short shortValue = 123;
        Integer intValue = 123;
        Long longValue = 123L;
        Number numberValue = 123;

        String json = mapper.writerWithRootValueSerializer(intLikeSerializer).writeValueAsString(byteValue);
        assertEquals("123", json);

        json = mapper.writerWithRootValueSerializer(intLikeSerializer).writeValueAsString(shortValue);
        assertEquals("123", json);

        json = mapper.writerWithRootValueSerializer(intLikeSerializer).writeValueAsString(intValue);
        assertEquals("123", json);

        json = mapper.writerWithRootValueSerializer(intLikeSerializer).writeValueAsString(longValue);
        assertEquals("123", json);

        json = mapper.writerWithRootValueSerializer(intLikeSerializer).writeValueAsString(numberValue);
        assertEquals("123", json);

        // Test deserialization from JSON String
        Number deserializedNumber = mapper.readValue("123", Number.class);
        assertEquals(123, deserializedNumber.intValue());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonSerializer<Long> longSerializer = mapper.findValueSerializer(Long.class);

        // Test serialization of positive longs
        Long longValue1 = 1234567890L;
        String json = mapper.writerWithRootValueSerializer(longSerializer).writeValueAsString(longValue1);
        assertEquals("1234567890", json);

        // Test serialization of negative longs
        Long longValue2 = -1234567890L;
        json = mapper.writerWithRootValueSerializer(longSerializer).writeValueAsString(longValue2);
        assertEquals("-1234567890", json);

        // Test serialization of max and min longs
        Long maxLongValue = Long.MAX_VALUE;
        json = mapper.writerWithRootValueSerializer(longSerializer).writeValueAsString(maxLongValue);
        assertEquals(String.valueOf(Long.MAX_VALUE), json);

        Long minLongValue = Long.MIN_VALUE;
        json = mapper.writerWithRootValueSerializer(longSerializer).writeValueAsString(minLongValue);
        assertEquals(String.valueOf(Long.MIN_VALUE), json);

        // Test serialization of zero
        Long zeroLongValue = 0L;
        json = mapper.writerWithRootValueSerializer(longSerializer).writeValueAsString(zeroLongValue);
        assertEquals("0", json);

        // Test deserialization from JSON String
        Long deserializedLong = mapper.readValue("1234567890", Long.class);
        assertEquals(1234567890, deserializedLong.longValue());
    }
}