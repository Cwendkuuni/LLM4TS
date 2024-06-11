import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {
    @Test
    public void testStandardSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test StdSerializer for String
        StdSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        String value = "Hello World";
        ObjectNode result = mapper.writeValueAsObject(value);
        assertEquals("{\"@type\":\"String\",\"value\":\"Hello World\"}", result.toString());

        // Test getSchema method
        JsonNode schema = stringSerializer.getSchema(mapper.getSerializerProvider(), String.class);
        assertEquals("{\"type\":\"string\"}", schema.toString());

        // Test serialize method with null value
        assertNull(stringSerializer.serialize(null, mapper.getJsonFactory().createJsonGenerator(System.out), mapper.getSerializerProvider()));

        // Test StdSerializer for Integer
        StdSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);
        Integer valueInt = 123;
        ObjectNode resultInt = mapper.writeValueAsObject(valueInt);
        assertEquals("{\"@type\":\"Number\",\"value\":123}", resultInt.toString());

        // Test getSchema method
        JsonNode schemaInt = integerSerializer.getSchema(mapper.getSerializerProvider(), Integer.class);
        assertEquals("{\"type\":\"number\"}", schemaInt.toString());

        // Test serialize method with null value
        assertNull(integerSerializer.serialize(null, mapper.getJsonFactory().createJsonGenerator(System.out), mapper.getSerializerProvider()));

        // Test StdSerializer for Boolean
        StdSerializer<Boolean> booleanSerializer = mapper.findValueSerializer(Boolean.class);
        Boolean valueBool = true;
        ObjectNode resultBool = mapper.writeValueAsObject(valueBool);
        assertEquals("{\"@type\":\"Boolean\",\"value\":true}", resultBool.toString());

        // Test getSchema method
        JsonNode schemaBool = booleanSerializer.getSchema(mapper.getSerializerProvider(), Boolean.class);
        assertEquals("{\"type\":\"boolean\"}", schemaBool.toString());

        // Test serialize method with null value
        assertNull(booleanSerializer.serialize(null, mapper.getJsonFactory().createJsonGenerator(System.out), mapper.getSerializerProvider()));

        // Test error scenario: serializing a String instead of a Boolean
        String booleanValue = "true";
        try {
            booleanSerializer.serialize(booleanValue, mapper.getJsonFactory().createJsonGenerator(System.out), mapper.getSerializerProvider());
            fail("Expected JsonMappingException when serializing a String instead of a Boolean");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test edge cases with large positive and negative integers
        Integer largePositive = Integer.MAX_VALUE;
        ObjectNode largePositiveResult = mapper.writeValueAsObject(largePositive);
        assertEquals("{\"@type\":\"Number\",\"value\":" + largePositive + "}", largePositiveResult.toString());

        Integer largeNegative = Integer.MIN_VALUE;
        ObjectNode largeNegativeResult = mapper.writeValueAsObject(largeNegative);
        assertEquals("{\"@type\":\"Number\",\"value\":" + largeNegative + "}", largeNegativeResult.toString());
    }

    @Test
    public void testGetDefaultSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> objectSerializer = mapper.findValueSerializer(Object.class);
        Object value = new Object();
        ObjectNode result = mapper.writeValueAsObject(value);
        assertEquals("{\"@type\":\"Object\"}", result.toString());
    }
}