package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Tests for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeInt() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(1, generator, null);
        generator.flush();
        String expectedOutput = "1";
        assertEquals(expectedOutput, "1");
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(1L, generator, null);
        generator.flush();
        String expectedOutput = "1";
        assertEquals(expectedOutput, "1");
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(1.0f, generator, null);
        generator.flush();
        String expectedOutput = "1.0";
        assertEquals(expectedOutput, "1.0");
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(1.0, generator, null);
        generator.flush();
        String expectedOutput = "1.0";
        assertEquals(expectedOutput, "1.0");
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize((byte) 1, generator, null);
        generator.flush();
        String expectedOutput = "1";
        assertEquals(expectedOutput, "1");
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize((short) 1, generator, null);
        generator.flush();
        String expectedOutput = "1";
        assertEquals(expectedOutput, "1");
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(BigInteger.valueOf(1), generator, null);
        generator.flush();
        String expectedOutput = "1";
        assertEquals(expectedOutput, "1");
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(new BigDecimal("1"), generator, null);
        generator.flush();
        String expectedOutput = "1";
        assertEquals(expectedOutput, "1");
    }

    @Test
    public void testDeserializeInt() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1"));
        Number number = reader.readValue(parser);
        assertEquals(1, number.intValue());
    }

    @Test
    public void testDeserializeLong() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1"));
        Number number = reader.readValue(parser);
        assertEquals(1L, number.longValue());
    }

    @Test
    public void testDeserializeFloat() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1.0"));
        Number number = reader.readValue(parser);
        assertEquals(1.0f, number.floatValue(), 0.0f);
    }

    @Test
    public void testDeserializeDouble() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1.0"));
        Number number = reader.readValue(parser);
        assertEquals(1.0, number.doubleValue(), 0.0);
    }

    @Test
    public void testDeserializeByte() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1"));
        Number number = reader.readValue(parser);
        assertEquals((byte) 1, number.byteValue());
    }

    @Test
    public void testDeserializeShort() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1"));
        Number number = reader.readValue(parser);
        assertEquals((short) 1, number.shortValue());
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1"));
        Number number = reader.readValue(parser);
        assertTrue(number instanceof BigInteger);
        assertEquals(BigInteger.ONE, number);
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);
        JsonParser parser = objectMapper.getFactory().createParser(new StringReader("1"));
        Number number = reader.readValue(parser);
        assertTrue(number instanceof BigDecimal);
        assertEquals(new BigDecimal("1"), number);
    }

    @Test
    public void testContextualSerialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerForValueType(Number.class);
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Default serialization should use NumberSerializer
        Number number = 1;
        writer.writeValue(generator, number);
        generator.flush();
        assertEquals("1", generator.getOutputBuffer().toString());

        // Custom serialization using ToStringSerializer
        @JsonSerialize(using = ToStringSerializer.class)
        Number customNumber = 1;
        writer.writeValue(generator, customNumber);
        generator.flush();
        assertEquals("1", generator.getOutputBuffer().toString());

        // Custom serialization using custom JSONFormat annotation
        @JsonFormat(serializableAs = BigInteger.class, shape = JsonFormat.Shape.STRING)
        Number customNumberWithAnnotation = 1;
        writer.writeValue(generator, customNumberWithAnnotation);
        generator.flush();
        assertEquals("1", generator.getOutputBuffer().toString());
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        ObjectReader reader = objectMapper.readerForValueType(Number.class);

        // Default deserialization should use NumberSerializer
        String json = "1";
        Number number = reader.readValue(new StringReader(json));
        assertEquals(1, number.intValue());

        // Custom deserialization using FromStringDeserializer
        @JsonDeserialize(using = FromStringDeserializer.class)
        String customJson = "1";
        Number customNumber = reader.readValue(new StringReader(customJson));
        assertEquals(1, customNumber.intValue());

        // Custom deserialization using custom JSONFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String customJsonWithAnnotation = "1";
        Number customNumberWithAnnotation = reader.readValue(new StringReader(customJsonWithAnnotation));
        assertEquals(1, customNumberWithAnnotation.intValue());
    }
}