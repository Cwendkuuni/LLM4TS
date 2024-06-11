package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.TimeZone;

/**
 * Test for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a {@link BigDecimal} instance.
     */
    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal number = new BigDecimal("123.45");
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123.45", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#createContextual} method when given a {@link JsonFormat} with {@link String} shape.
     */
    @Test
    public void testCreateContextualStringFormat() throws JsonMappingException {
        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<?> serializer = new NumberSerializer(Integer.class).createContextual(mapper.getSerializerProvider(), null);
        Assert.assertNotSame(ToStringSerializer.instance, serializer);
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a {@link BigInteger} instance.
     */
    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger number = new BigInteger("123456789012345678901234567890");
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123456789012345678901234567890", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given an {@link Integer} instance.
     */
    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer number = 123;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a {@link Long} instance.
     */
    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long number = 123L;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a {@link Double} instance.
     */
    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double number = 123.45;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123.45", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a {@link Float} instance.
     */
    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float number = 123.45f;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123.45", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a primitive {@code int} value.
     */
    @Test
    public void testSerializeInt() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(int.class);
        int number = 123;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a primitive {@code long} value.
     */
    @Test
    public void testSerializeLong_Primitive() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(long.class);
        long number = 123L;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a primitive {@code double} value.
     */
    @Test
    public void testSerializeDouble_Primitive() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(double.class);
        double number = 123.45;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123.45", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a primitive {@code float} value.
     */
    @Test
    public void testSerializeFloat_Primitive() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(float.class);
        float number = 123.45f;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        Assert.assertEquals("123.45", generator.getOutputContentAsString());
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a {@link JsonFormat} with {@link JsonFormat.Shape#NUMBER} shape.
     */
    @Test
    public void testCreateContextualNumberFormat() throws JsonMappingException {
        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.NUMBER);
        JsonSerializer<?> serializer = new NumberSerializer(Integer.class).createContextual(mapper.getSerializerProvider(), null);
        Assert.assertSame(serializer, ToStringSerializer.instance);
    }

    /**
     * Test for {@link NumberSerializer#serialize} method when given a null value.
     */
    @Test(expected = JsonProcessingException.class)
    public void testSerializeNull() throws IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(null, generator, mapper.getSerializerProvider());
        generator.flush();
    }

    /**
     * Test for {@link NumberSerializer#getSchema} method.
     */
    @Test
    public void testGetSchema() throws IOException {
        ObjectNode schema = new NumberSerializer(Number.class).getSchema(mapper.getSerializerProvider(), Number.class);
        Assert.assertEquals("number", schema.get("type").asText());
    }
}