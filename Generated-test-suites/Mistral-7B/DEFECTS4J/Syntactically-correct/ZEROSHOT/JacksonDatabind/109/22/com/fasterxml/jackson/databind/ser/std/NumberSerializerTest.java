package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeInt() throws IOException {
        JsonGenerator generator = setupGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        int testValue = 42;
        serializer.serialize(testValue, generator, new ObjectMapper().getSerializerProvider());
        assertEquals("42", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = setupGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        long testValue = 42L;
        serializer.serialize(testValue, generator, new ObjectMapper().getSerializerProvider());
        assertEquals("42", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = setupGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        float testValue = 42.0f;
        serializer.serialize(testValue, generator, new ObjectMapper().getSerializerProvider());
        assertEquals("42.0", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = setupGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        double testValue = 42.0;
        serializer.serialize(testValue, generator, new ObjectMapper().getSerializerProvider());
        assertEquals("42.0", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = setupGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger testValue = new BigInteger("42");
        serializer.serialize(testValue, generator, new ObjectMapper().getSerializerProvider());
        assertEquals("42", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = setupGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal testValue = new BigDecimal("42.123");
        serializer.serialize(testValue, generator, new ObjectMapper().getSerializerProvider());
        assertEquals("42.123", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testContextualSerializerForNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Number> serializer = mapper.findValueSerializer(Number.class);
        assertTrue(serializer instanceof NumberSerializer);
    }

    @Test
    public void testContextualSerializerForBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Number> serializer = mapper.findValueSerializer(BigDecimal.class);
        assertTrue(serializer instanceof ToStringSerializer);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().findRootValueSerializer(JsonType.NUMBER)
                .acceptJsonFormatVisitor(null, null);
        assertTrue(visitor.hasFeatures(JsonSerializable.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT));
    }

    /**
     * Helper method to setup {@link JsonGenerator}.
     *
     * @return {@link JsonGenerator} instance
     * @throws IOException if an error occurs
     */
    private JsonGenerator setupGenerator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator();
        generator.setCodec(mapper);
        return generator;
    }

    @Test
    @JsonSerialize(using = NumberSerializer.class)
    @JsonDeserialize(using = NumberSerializer.class)
    static class TestBean {
        private Number value;

        public Number getValue() {
            return value;
        }

        public void setValue(Number value) {
            this.value = value;
        }
    }

    @Test
    public void testRoundTrip() throws IOException {
        JsonParser parser = setupParser();
        ObjectMapper mapper = new ObjectMapper();
        TestBean testBean = new TestBean();
        testBean.setValue(new BigDecimal("42.123"));

        JsonGenerator generator = setupGenerator();
        mapper.writeValue(generator, testBean);
        generator.flush();
        String serializedString = generator.getOutputBuffer().toString();
        generator.close();

        Object readValue = parser.parseValue(serializedString);
        TestBean readBean = mapper.readValue(JsonNode.class, TestBean.class);
        assertEquals(testBean.getValue(), readValue);
        assertEquals(testBean, readBean);
        parser.close();
    }

    @Test
    public void testRoundTripWithJsonFormat() throws IOException {
        JsonParser parser = setupParser();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        TestBean testBean = new TestBean();
        testBean.setValue(new BigDecimal("42.123"));

        JsonGenerator generator = setupGenerator();
        generator.writeStartObject();
        generator.writeFieldName("value");
        NumberSerializer serializer = NumberSerializer.instance;
        serializer.acceptJsonFormatVisitor(generator, JSONType.NUMBER);
        serializer.serialize(testBean.getValue(), generator, new ObjectMapper().getSerializerProvider());
        generator.writeEndObject();
        generator.flush();
        String serializedString = generator.getOutputBuffer().toString();
        generator.close();

        Object readValue = parser.parse(serializedString);
        TestBean readBean = mapper.readValue(JsonNode.class, TestBean.class);
        assertEquals(testBean.getValue(), readValue);
        assertEquals(testBean, readBean);
        parser.close();
    }

    private JsonParser setupParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new StringReader("{\"value\":42}"));
        parser.setCodec(mapper);
        return parser;
    }

    @Test
    public void testSerializationWithJsonFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        TestBean testBean = new TestBean();
        testBean.setValue(new BigDecimal("42.123"));

        JsonGenerator generator = setupGenerator();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<TestBean> serializer = mapper
                .findValueSerializer(TestBean.class, new TypeReference<JsonSerializer<TestBean>>() {});
        serializer.acceptJsonFormatVisitor(generator, new JavaType(TestBean.class));
        serializer.serialize(testBean, generator, new ObjectMapper().getSerializerProvider(), jsonFormat);
        generator.writeEndObject();
        generator.flush();
        String serializedString = generator.getOutputBuffer().toString();
        generator.close();

        assertEquals("{\"value\":\"42.123\"}", serializedString);
    }

    @Test
    public void testSerializationWithJsonFormatWithNumberType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        TestBean testBean = new TestBean();
        testBean.setValue(new BigDecimal("42.123"));

        JsonGenerator generator = setupGenerator();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<TestBean> serializer = mapper
                .findValueSerializer(TestBean.class, new TypeReference<JsonSerializer<TestBean>>() {});
        serializer.acceptJsonFormatVisitor(generator, new JavaType(TestBean.class));
        serializer.serialize(testBean, generator, new ObjectMapper().getSerializerProvider(), jsonFormat,
                JsonParser.NumberType.BIG_DECIMAL);
        generator.writeEndObject();
        generator.flush();
        String serializedString = generator.getOutputBuffer().toString();
        generator.close();

        assertEquals("{\"value\":\"42.123\"}", serializedString);
    }

    @Test
    public void testSerializationWithJsonFormatWithIntType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        TestBean testBean = new TestBean();
        testBean.setValue(new BigInteger("42"));

        JsonGenerator generator = setupGenerator();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<TestBean> serializer = mapper
                .findValueSerializer(TestBean.class, new TypeReference<JsonSerializer<TestBean>>() {});
        serializer.acceptJsonFormatVisitor(generator, new JavaType(TestBean.class));
        serializer.serialize(testBean, generator, new ObjectMapper().getSerializerProvider(), jsonFormat,
                JsonParser.NumberType.INT);
        generator.writeEndObject();
        generator.flush();
        String serializedString = generator.getOutputBuffer().toString();
        generator.close();

        assertEquals("{\"value\":\"42\"}", serializedString);
    }

    @Test
    public void testDeserializationWithJsonFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean testBean = new TestBean();

        String serializedString = "{\"value\":\"42.123\"}";
        JsonParser parser = setupParser(serializedString);

        JsonDeserializer<TestBean> deserializer = mapper
                .findValueDeserializer(TestBean.class, new TypeReference<JsonDeserializer<TestBean>>() {});
        TestBean deserializedBean = deserializer.deserialize(parser, null);
        assertEquals(new BigDecimal("42.123"), deserializedBean.getValue());
        parser.close();
    }

    @Test
    public void testDeserializationWithJsonFormatWithNumberType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean testBean = new TestBean();

        String serializedString = "{\"value\":\"42.123\"}";
        JsonParser parser = setupParser(serializedString);

        JsonDeserializer<TestBean> deserializer = mapper
                .findValueDeserializer(TestBean.class, new TypeReference<JsonDeserializer<TestBean>>() {});
        TestBean deserializedBean = deserializer.deserialize(parser, JsonParser.NumberType.BIG_DECIMAL.getClass());
        assertEquals(new BigDecimal("42.123"), deserializedBean.getValue());
        parser.close();
    }

    @Test
    public void testDeserializationWithJsonFormatWithIntType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean testBean = new TestBean();

        String serializedString = "{\"value\":\"42\"}";
        JsonParser parser = setupParser(serializedString);

        JsonDeserializer<TestBean> deserializer = mapper
                .findValueDeserializer(TestBean.class, new TypeReference<JsonDeserializer<TestBean>>() {});
        TestBean deserializedBean = deserializer.deserialize(parser, JsonParser.NumberType.INT.getClass());
        assertEquals(new BigInteger("42"), deserializedBean.getValue());
        parser.close();
    }

    private JsonParser setupParser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        parser.setCodec(mapper);
        return parser;
    }

    private JsonParser setupParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(""));
        parser.setCodec(mapper);
        return parser;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializationWithJsonFormatWithoutNumberType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean testBean = new TestBean();

        String serializedString = "{\"value\":\"42.123\"}";
        JsonParser parser = setupParser();

        JsonDeserializer<TestBean> deserializer = mapper
                .findValueDeserializer(TestBean.class, new TypeReference<JsonDeserializer<TestBean>>() {});
        TestBean deserializedBean = deserializer.deserialize(parser, null);
        fail();
        parser.close();
    }
}