package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Test file for NumberSerializer class.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    public NumberSerializerTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        BigDecimal number = new BigDecimal("123.45");
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("123.45", serializedNumber);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        BigInteger number = new BigInteger("123456789012345678901234567890");
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("123456789012345678901234567890", serializedNumber);
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        int number = 123;
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("123", serializedNumber);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        long number = 1234567890123456789L;
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("1234567890123456789", serializedNumber);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        double number = 123.45;
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("123.45", serializedNumber);
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        float number = 123.45f;
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("123.45", serializedNumber);
    }

    @Test
    public void testSerializeByte() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        byte number = 127;
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("127", serializedNumber);
    }

    @Test
    public void testSerializeShort() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        short number = 127;
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("127", serializedNumber);
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        String number = "123.45";
        serializer.serialize(number, gen, provider);
        gen.flush();

        String serializedNumber = gen.getOutputContent();
        assertEquals("123.45", serializedNumber);
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("123.45");
        BigDecimal number = mapper.readValue(rootNode.traverse(), BigDecimal.class);
        assertEquals(new BigDecimal("123.45"), number);
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("123456789012345678901234567890");
        BigInteger number = mapper.readValue(rootNode.traverse(), BigInteger.class);
        assertEquals(new BigInteger("123456789012345678901234567890"), number);
    }

    @Test
    public void testDeserializeInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("123");
        int number = mapper.readValue(rootNode.traverse(), int.class);
        assertEquals(123, number);
    }

    @Test
    public void testDeserializeLong() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("123456789012345678901234567890");
        long number = mapper.readValue(rootNode.traverse(), long.class);
        assertEquals(12345678901234567890L, number);
    }

    @Test
    public void testDeserializeDouble() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("123.45");
        double number = mapper.readValue(rootNode.traverse(), double.class);
        assertEquals(123.45, number, 0.001);
    }

    @Test
    public void testDeserializeFloat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("123.45");
        float number = mapper.readValue(rootNode.traverse(), float.class);
        assertEquals(123.45f, number, 0.001f);
    }

    @Test
    public void testDeserializeByte() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("127");
        byte number = mapper.readValue(rootNode.traverse(), byte.class);
        assertEquals((byte) 127, number);
    }

    @Test
    public void testDeserializeShort() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("127");
        short number = mapper.readValue(rootNode.traverse(), short.class);
        assertEquals((short) 127, number);
    }

    @Test
    public void testDeserializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("\"123.45\"");
        String number = mapper.readValue(rootNode.traverse(), String.class);
        assertEquals("123.45", number);
    }

    @JsonSerialize(using = NumberSerializer.class)
    @JsonDeserialize(using = NumberDeserializer.class)
    static class TestNumber {
        private Number number;

        public Number getNumber() {
            return number;
        }

        public void setNumber(Number number) {
            this.number = number;
        }
    }

    @Test
    public void testDeserializeCustomNumberClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("123.45");
        TestNumber testNumber = mapper.readValue(rootNode.traverse(), TestNumber.class);
        assertEquals(new BigDecimal("123.45"), testNumber.getNumber());
    }
}

class NumberDeserializer extends StdDeserializer<Number> {

    public NumberDeserializer() {
        this(null);
    }

    public NumberDeserializer(Class<? extends Number> vc) {
        super(vc);
    }

    @Override
    public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Number number;
        String text = p.getText();
        try {
            number = new BigDecimal(text);
        } catch (NumberFormatException e) {
            try {
                number = new BigInteger(text);
            } catch (NumberFormatException e1) {
                number = text;
            }
        }
        return number;
    }
}