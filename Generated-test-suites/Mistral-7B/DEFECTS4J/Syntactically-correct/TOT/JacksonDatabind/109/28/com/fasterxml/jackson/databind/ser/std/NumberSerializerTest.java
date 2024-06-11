package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.TimeZone;

public class NumberSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getGenerator(new Object(), mapper, null);

        BigDecimal bigDecimal = new BigDecimal("123.456");
        serializer.serialize(bigDecimal, generator, null);

        JsonNode actual = generator.getOutputTree();
        JsonNode expected = mapper.readTree(new TextNode("123.456"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getGenerator(new Object(), mapper, null);

        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        serializer.serialize(bigInteger, generator, null);

        JsonNode actual = generator.getOutputTree();
        JsonNode expected = mapper.readTree(new TextNode("123456789012345678901234567890"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeInt() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getGenerator(new Object(), mapper, null);

        int intValue = 123;
        serializer.serialize(intValue, generator, null);

        JsonNode actual = generator.getOutputTree();
        JsonNode expected = mapper.readTree(new TextNode("123"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeJsonFormatAnnotation() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getGenerator(new Object(), mapper, null);

        JsonFormat annotation = new JsonFormat();
        annotation.shape(JsonFormat.Shape.STRING);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setFormat(annotation);

        Object obj = new Object() {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            public Number value = new BigDecimal("123.456");
        };

        serializer.createContextual(mapper.getSerializerProvider(), mapper.getBeanPropertyFinder().findProperty("value", obj.getClass(), null));
        serializer.serialize(obj.value, generator, null);

        JsonNode actual = generator.getOutputTree();
        JsonNode expected = mapper.readTree(new TextNode("123.456"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeJsonFormatAnnotationWithCustomSerializer() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getGenerator(new Object(), mapper, null);

        JsonFormat annotation = new JsonFormat();
        annotation.serializer(ToStringSerializer.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setFormat(annotation);

        Object obj = new Object() {
            @JsonFormat(serializer = ToStringSerializer.class)
            public Number value = new BigDecimal("123.456");
        };

        serializer.createContextual(mapper.getSerializerProvider(), mapper.getBeanPropertyFinder().findProperty("value", obj.getClass(), null));
        serializer.serialize(obj.value, generator, null);

        JsonNode actual = generator.getOutputTree();
        JsonNode expected = mapper.readTree(new TextNode("123.456"));
        Assert.assertEquals(expected.asText(), actual.asText());
        Assert.assertNotEquals(expected, actual);
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeUnsupportedNumberType() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getGenerator(new Object(), mapper, null);

        serializer.serialize(new Object(), generator, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProperty() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.createContextual(mapper.getSerializerProvider(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualWithUnsupportedType() throws JsonProcessingException, JsonGenerationException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        serializer.createContextual(mapper.getSerializerProvider(), mapper.getBeanPropertyFinder().findProperty("value", String.class, null));
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Number.class);

        JsonNode expected = mapper.readTree(new TextNode("number"));
        Assert.assertEquals(expected.asText(), schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.getFactory().createParser(node);
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getGeneratorFactory().createNumberFormatVisitor(null, null);

        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertEquals(JsonParser.NumberType.BIG_DECIMAL, visitor.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigInteger() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.getFactory().createParser(node);
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getGeneratorFactory().createNumberFormatVisitor(null, null);

        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitorForInt() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.getFactory().createParser(node);
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getGeneratorFactory().createNumberFormatVisitor(null, null);

        NumberSerializer serializer = new NumberSerializer(Integer.class);
        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertEquals(JsonParser.NumberType.INT, visitor.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitorForUnsupportedType() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.getFactory().createParser(node);
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getGeneratorFactory().createNumberFormatVisitor(null, null);

        NumberSerializer serializer = new NumberSerializer(String.class);
        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.fail();
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTimeZone() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.getFactory().createParser(node);
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getGeneratorFactory().createNumberFormatVisitor(null, new TimeZone("UTC"));

        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertEquals(JsonParser.NumberType.BIG_DECIMAL, visitor.getType());
        Assert.assertEquals("UTC", visitor.getTimeZone().getDisplayName());
    }
}