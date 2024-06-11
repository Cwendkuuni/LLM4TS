package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

/**
 * Test cases for NumberSerializer class.
 */
public class NumberSerializerTest {

    /**
     * Test serialization and deserialization of basic number types.
     */
    @Test
    public void testBasicNumberTypes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        // Test serialization and deserialization of int, long, float, and double.
        int intValue = 42;
        long longValue = 42L;
        float floatValue = 3.14F;
        double doubleValue = 1.23456;

        JsonGenerator generator = jsonFactory.createGenerator();
        mapper.writeValue(generator, intValue);
        mapper.writeValue(generator, longValue);
        mapper.writeValue(generator, floatValue);
        mapper.writeValue(generator, doubleValue);
        generator.close();

        JsonParser parser = jsonFactory.createParser("[" + generator.getOutputContentAsString() + "]");
        int deserializedInt = mapper.readValue(parser, int.class);
        long deserializedLong = mapper.readValue(parser, long.class);
        float deserializedFloat = mapper.readValue(parser, float.class);
        double deserializedDouble = mapper.readValue(parser, double.class);

        assertEquals(intValue, deserializedInt);
        assertEquals(longValue, deserializedLong);
        assertEquals(floatValue, deserializedFloat, 0.001F); // allow for small floating point error
        assertEquals(doubleValue, deserializedDouble, 0.0001); // allow for small floating point error

        // Test serialization and deserialization of BigDecimal and BigInteger.
        BigDecimal bigDecimal = new BigDecimal("123.45");
        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");

        generator = jsonFactory.createGenerator();
        mapper.writeValue(generator, bigDecimal);
        mapper.writeValue(generator, bigInteger);
        generator.close();

        parser = jsonFactory.createParser("[" + generator.getOutputContentAsString() + "]");
        BigDecimal deserializedBigDecimal = mapper.readValue(parser, BigDecimal.class);
        BigInteger deserializedBigInteger = mapper.readValue(parser, BigInteger.class);

        assertEquals(bigDecimal, deserializedBigDecimal);
        assertEquals(bigInteger, deserializedBigInteger);
    }

    /**
     * Test serialization and deserialization of Number instance.
     */
    @Test
    public void testNumberInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        Number number = new BigInteger("42");

        JsonGenerator generator = jsonFactory.createGenerator();
        mapper.writeValue(generator, number);
        generator.close();

        JsonParser parser = jsonFactory.createParser("[" + generator.getOutputContentAsString() + "]");
        Number deserializedNumber = mapper.readValue(parser, Number.class);

        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialization and deserialization of Number with JsonFormat annotation.
     */
    @Test
    public void testJsonFormatAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        Number number = new BigDecimal("123.45");

        // Use JsonFormat annotation to set custom serialization/deserialization.
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Number annotatedNumber = number;

        JsonGenerator generator = jsonFactory.createGenerator();
        mapper.writeValue(generator, annotatedNumber);
        generator.close();

        JsonParser parser = jsonFactory.createParser("[" + generator.getOutputContentAsString() + "]");
        Number deserializedNumber = mapper.readValue(parser, Number.class);

        assertEquals(number, deserializedNumber);

        // Verify that the deserialized value is serialized back correctly.
        generator = jsonFactory.createGenerator();
        mapper.writeValue(generator, deserializedNumber);
        String serializedValue = generator.getOutputContentAsString();
        generator.close();

        parser = jsonFactory.createParser(serializedValue);
        Number deserializedNumber2 = mapper.readValue(parser, Number.class);

        assertEquals(number, deserializedNumber2);
    }

    /**
     * Test serialization and deserialization of Number with custom serializer.
     */
    @Test
    public void testCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        Number number = new BigInteger("42");

        // Register a custom serializer for Number type.
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupSerializerInjection(SerializerProvider provider) throws JsonMappingException {
                provider.findValueSerializer(Number.class, null).setNullValueSerializer(new ToStringSerializer(Number.class));
            }
        });

        JsonGenerator generator = jsonFactory.createGenerator();
        mapper.writeValue(generator, number);
        generator.close();

        JsonParser parser = jsonFactory.createParser("[" + generator.getOutputContentAsString() + "]");
        Number deserializedNumber = mapper.readValue(parser, Number.class);

        assertEquals(number, deserializedNumber);
    }

    /**
     * Test serialization and deserialization of Number with unsupported types.
     */
    @Test(expected = JsonProcessingException.class)
    public void testUnsupportedTypes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        Object unsupportedObject = new Object();

        JsonGenerator generator = jsonFactory.createGenerator();
        mapper.writeValue(generator, unsupportedObject);
        generator.close();

        JsonParser parser = jsonFactory.createParser("[" + generator.getOutputContentAsString() + "]");
        mapper.readValue(parser, Number.class);
    }

    /**
     * Test serialization and deserialization of Number with JsonNode.
     */
    @Test
    public void testJsonNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        Number number = new BigInteger("42");

        JsonGenerator generator = jsonFactory.createGenerator();
        JsonNode jsonNode = mapper.valueToTree(number);
        generator.writeTree(jsonNode);
        generator.close();

        JsonParser parser = jsonFactory.createParser("[" + generator.getOutputContentAsString() + "]");
        Number deserializedNumber = mapper.treeToValue(parser.readValueAsTree(), Number.class);

        assertEquals(number, deserializedNumber);
    }
}