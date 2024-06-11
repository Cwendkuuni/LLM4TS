package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Unit tests for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal bigDecimal = new BigDecimal("123.45");
        serializer.serialize(bigDecimal, generator, new SerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new StringReader(generator.getOutputStr()));
        assertEquals(bigDecimal, parser.readValueAs(BigDecimal.class));
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger bigInteger = BigInteger.valueOf(12345);
        serializer.serialize(bigInteger, generator, new SerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new StringReader(generator.getOutputStr()));
        assertEquals(bigInteger, parser.readValueAs(BigInteger.class));
    }

    @Test
    public void testSerializeInteger() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        int intValue = 123;
        serializer.serialize(intValue, generator, new SerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new StringReader(generator.getOutputStr()));
        assertEquals(intValue, parser.readValueAs(Integer.class));
    }

    @Test
    public void testSerializeLong() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        long longValue = 123456;
        serializer.serialize(longValue, generator, new SerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new StringReader(generator.getOutputStr()));
        assertEquals(longValue, parser.readValueAs(Long.class));
    }

    @Test
    public void testSerializeDouble() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        double doubleValue = 123.45;
        serializer.serialize(doubleValue, generator, new SerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new StringReader(generator.getOutputStr()));
        assertEquals(doubleValue, parser.readValueAs(Double.class), 0.001);
    }

    @Test
    public void testSerializeFloat() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        float floatValue = 123.45f;
        serializer.serialize(floatValue, generator, new SerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new StringReader(generator.getOutputStr()));
        assertEquals(floatValue, parser.readValueAs(Float.class), 0.001f);
    }

    @Test
    public void testSerializeOtherNumberTypes() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        Byte byteValue = (byte) 127;
        Short shortValue = (short) 123;
        serializer.serialize(byteValue, generator, new SerializerProvider());
        serializer.serialize(shortValue, generator, new SerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new StringReader(generator.getOutputStr()));
        assertEquals(byteValue.intValue(), parser.readValueAs(Integer.class));
        assertEquals(shortValue.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testGetSchema() throws Exception {
        JsonSchemaFactory factory = new JsonSchemaFactory();
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = NumberSerializer.instance;
        Schema schema = serializer.getSchema(new SerializerProvider(), Object.class);
        JsonSchema jsonSchema = mapper.schemaForSerialization(schema);
        assertEquals("number", jsonSchema.getDescription().getTitle());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        NumberSerializer serializer = NumberSerializer.instance;
        serializer.acceptJsonFormatVisitor(visitor, IntType.INSTANCE);
        // Unsupported case for BigDecimal and BigInteger
    }
}