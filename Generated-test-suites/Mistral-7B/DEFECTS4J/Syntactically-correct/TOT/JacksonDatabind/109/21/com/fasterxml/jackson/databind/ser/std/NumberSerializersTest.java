package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test serialization of an integer
        Integer intValue = 123;
        String json = mapper.writeValueAsString(intValue, new JsonFactory());
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(123, parser.readValueAs(Integer.class));

        // Test serialization of an negative integer
        Integer negativeIntValue = -123;
        json = mapper.writeValueAsString(negativeIntValue, new JsonFactory());
        parser = mapper.getFactory().createParser(json);
        assertEquals(-123, parser.readValueAs(Integer.class));

        // Test serialization of an integer with leading zeros
        Integer intWithLeadingZeros = 00123;
        json = mapper.writeValueAsString(intWithLeadingZeros, new JsonFactory());
        parser = mapper.getFactory().createParser(json);
        assertEquals(123, parser.readValueAs(Integer.class));

        // Test deserialization of an integer with leading zeros
        String jsonWithLeadingZeros = "00123";
        Integer deserializedInt = mapper.readValue(jsonWithLeadingZeros, Integer.class);
        assertEquals(123, deserializedInt.intValue());

        // Test serialization and deserialization of the minimum and maximum integers
        Integer minInt = Integer.MIN_VALUE;
        json = mapper.writeValueAsString(minInt, new JsonFactory());
        parser = mapper.getFactory().createParser(json);
        assertEquals(minInt, parser.readValueAs(Integer.class));

        Integer maxInt = Integer.MAX_VALUE;
        json = mapper.writeValueAsString(maxInt, new JsonFactory());
        parser = mapper.getFactory().createParser(json);
        assertEquals(maxInt, parser.readValueAs(Integer.class));

        // Test serialization and deserialization of an integer with no leading zeros
        Integer intNoLeadingZeros = 123;
        json = mapper.writeValueAsString(intNoLeadingZeros, new JsonFactory());
        parser = mapper.getFactory().createParser(json);
        assertEquals(123, parser.readValueAs(Integer.class));

        // Test serialization and deserialization of null
        assertNull(mapper.readValue(JsonFactory.emptyValueIdentifier, Integer.class));
        assertNull(parser.readValueAs(Integer.class));
    }

    // Similar test cases can be added for other serializers like LongSerializer,
    // FloatSerializer, DoubleSerializer, etc.
}