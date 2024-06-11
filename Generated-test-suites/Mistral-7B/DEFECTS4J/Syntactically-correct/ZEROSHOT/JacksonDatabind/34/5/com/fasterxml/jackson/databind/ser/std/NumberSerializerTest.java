package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializationTest;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest extends SerializationTest {

    /**
     * Test serialization of various Number types.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testSerialize() throws IOException, JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization of various number types
        BigDecimal bigDecimal = new BigDecimal("123.45");
        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        Integer intValue = 123;
        Long longValue = 1234567890123456789L;
        Double doubleValue = 123.45;
        Float floatValue = 123.45f;
        Byte byteValue = 127;
        Short shortValue = 123;

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(null);
        ObjectWriter writer = mapper.writerWithView(NumberSerializer.class);
        writer.writeValue(jsonGenerator, numberSerializer);
        jsonGenerator.writeObject(numberSerializer);
        jsonGenerator.writeValue(numberSerializer, bigDecimal);
        jsonGenerator.writeValue(numberSerializer, bigInteger);
        jsonGenerator.writeValue(numberSerializer, intValue);
        jsonGenerator.writeValue(numberSerializer, longValue);
        jsonGenerator.writeValue(numberSerializer, doubleValue);
        jsonGenerator.writeValue(numberSerializer, floatValue);
        jsonGenerator.writeValue(numberSerializer, byteValue);
        jsonGenerator.writeValue(numberSerializer, shortValue);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(jsonGenerator.getOutputStream());
        ObjectReader reader = mapper.readerFor(Number.class);

        assertEquals(numberSerializer, reader.readValue(jsonParser));
        assertEquals(numberSerializer, writer.readValue(jsonParser));
        assertEquals(bigDecimal, reader.readValue(jsonParser));
        assertEquals(bigInteger, reader.readValue(jsonParser));
        assertEquals(intValue, reader.readValue(jsonParser));
        assertEquals(longValue, reader.readValue(jsonParser));
        assertEquals(doubleValue, reader.readValue(jsonParser));
        assertEquals(floatValue, reader.readValue(jsonParser));
        assertEquals(byteValue, reader.readValue(jsonParser));
        assertEquals(shortValue, reader.readValue(jsonParser));

        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        assertEquals(numberSerializer.toString(), reader.readValue(jsonParser).toString());

        // Test serialization with _isInt flag set
        NumberSerializer intNumberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator intJsonGenerator = mapper.getFactory().createGenerator(null);
        ObjectWriter intWriter = mapper.writerWithView(intNumberSerializer);
        intWriter.writeValue(intJsonGenerator, intNumberSerializer);
        intJsonGenerator.writeValue(intNumberSerializer, intValue);
        intJsonGenerator.writeValue(intNumberSerializer, longValue);
        intJsonGenerator.writeValue(intNumberSerializer, doubleValue);
        intJsonGenerator.writeValue(intNumberSerializer, floatValue);
        intJsonGenerator.writeValue(intNumberSerializer, byteValue);
        intJsonGenerator.writeValue(intNumberSerializer, shortValue);
        intJsonGenerator.writeValue(intNumberSerializer, new BigDecimal("123.45"));
        intJsonGenerator.writeValue(intNumberSerializer, new BigInteger("123456789012345678901234567890"));
        intJsonGenerator.flush();

        JsonParser intJsonParser = mapper.getFactory().createParser(intJsonGenerator.getOutputStream());
        ObjectReader intReader = mapper.readerFor(Number.class);

        assertEquals(intNumberSerializer, intReader.readValue(intJsonParser));
        assertEquals(intNumberSerializer, intWriter.readValue(intJsonParser));
        assertEquals(intValue, intReader.readValue(intJsonParser));
        assertEquals(longValue, intReader.readValue(intJsonParser));
        assertEquals(doubleValue, intReader.readValue(intJsonParser));
        assertEquals(floatValue, intReader.readValue(intJsonParser));
        assertEquals(byteValue, intReader.readValue(intJsonParser));
        assertEquals(shortValue, intReader.readValue(intJsonParser));
        assertEquals(new BigDecimal("123.45"), intReader.readValue(intJsonParser));
        assertEquals(new BigInteger("123456789012345678901234567890"), intReader.readValue(intJsonParser));

        // Test serialization with _isInt flag not set
        NumberSerializer nonIntNumberSerializer = new NumberSerializer(Double.class);
        JsonGenerator nonIntJsonGenerator = mapper.getFactory().createGenerator(null);
        ObjectWriter nonIntWriter = mapper.writerWithView(nonIntNumberSerializer);
        nonIntWriter.writeValue(nonIntJsonGenerator, nonIntNumberSerializer);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, bigDecimal);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, bigInteger);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, intValue);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, longValue);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, doubleValue);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, floatValue);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, byteValue);
        nonIntJsonGenerator.writeValue(nonIntNumberSerializer, shortValue);
        nonIntJsonGenerator.flush();

        JsonParser nonIntJsonParser = mapper.getFactory().createParser(nonIntJsonGenerator.getOutputStream());
        ObjectReader nonIntReader = mapper.readerFor(Number.class);

        assertEquals(nonIntNumberSerializer, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(nonIntNumberSerializer, nonIntWriter.readValue(nonIntJsonParser));
        assertEquals(bigDecimal, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(bigInteger, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(intValue, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(longValue, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(doubleValue, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(floatValue, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(byteValue, nonIntReader.readValue(nonIntJsonParser));
        assertEquals(shortValue, nonIntReader.readValue(nonIntJsonParser));

        // Test deserialization
        JsonNode jsonNode = JsonNodeFactory.instance.text("123.45");
        Number deserializedNumber = mapper.readValue(jsonNode, Number.class);
        assertEquals(new BigDecimal("123.45"), deserializedNumber);

        jsonNode = JsonNodeFactory.instance.text("123456789012345678901234567890");
        deserializedNumber = mapper.readValue(jsonNode, Number.class);
        assertEquals(new BigInteger("123456789012345678901234567890"), deserializedNumber);

        jsonNode = JsonNodeFactory.instance.number(123);
        deserializedNumber = mapper.readerFor(Number.class).readValue(jsonNode);
        assertEquals(123, deserializedNumber.intValue());

        jsonNode = JsonNodeFactory.instance.number(1234567890123456789L);
        deserializedNumber = mapper.readerFor(Number.class).readValue(jsonNode);
        assertEquals(1234567890123456789L, deserializedNumber.longValue());

        jsonNode = JsonNodeFactory.instance.number(123.45);
        deserializedNumber = mapper.readerFor(Number.class).readValue(jsonNode);
        assertEquals(123.45, deserializedNumber.doubleValue(), 0.001);

        jsonNode = JsonNodeFactory.instance.number(123.45f);
        deserializedNumber = mapper.readerFor(Number.class).readValue(jsonNode);
        assertEquals(123.45f, deserializedNumber, 0.001f);

        jsonNode = JsonNodeFactory.instance.number((byte) 127);
        deserializedNumber = mapper.readerFor(Number.class).readValue(jsonNode);
        assertEquals((byte) 127, deserializedNumber.byteValue());

        jsonNode = JsonNodeFactory.instance.number((short) 123);
        deserializedNumber = mapper.readerFor(Number.class).readValue(jsonNode);
        assertEquals((short) 123, deserializedNumber.shortValue());
    }

    /**
     * Test schema generation.
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testGetSchema() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonNode schema = numberSerializer.getSchema(null, null);
        assertEquals("number", schema.asText());

        NumberSerializer intNumberSerializer = new NumberSerializer(Integer.class);
        schema = intNumberSerializer.getSchema(null, null);
        assertEquals("integer", schema.asText());
    }

    /**
     * Test acceptJsonFormatVisitor.
     *
     * @throws JsonMappingException
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = createFormatVisitor();
        numberSerializer.acceptJsonFormatVisitor(visitor, null);
    }
}