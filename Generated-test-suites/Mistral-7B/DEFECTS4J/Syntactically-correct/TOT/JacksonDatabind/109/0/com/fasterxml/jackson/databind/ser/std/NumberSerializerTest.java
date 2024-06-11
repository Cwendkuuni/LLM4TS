package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    /**
     * Initialize ObjectMapper for testing.
     */
    @SuppressWarnings("ResourceLeak")
    @Before
    public void init() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Test serialize method for {@code BigDecimal} type.
     */
    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        BigDecimal number = new BigDecimal("123.456");
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123.456");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code BigInteger} type.
     */
    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        BigInteger number = BigInteger.TEN;
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("10");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code Long} type.
     */
    @Test
    public void testSerializeLong() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        Long number = 123L;
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code Double} type.
     */
    @Test
    public void testSerializeDouble() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        Double number = 123.456;
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123.456");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code Float} type.
     */
    @Test
    public void testSerializeFloat() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        Float number = 123.456f;
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123.456");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code Integer} type.
     */
    @Test
    public void testSerializeInteger() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        Integer number = 123;
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code Byte} type.
     */
    @Test
    public void testSerializeByte() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        Byte number = (byte) 123;
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code Short} type.
     */
    @Test
    public void testSerializeShort() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        Short number = (short) 123;
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonNode node = mapper.readTree(parser);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123");
        assertEquals(expected, node);

        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(number, deserialized);
    }

    /**
     * Test serialize method for {@code String} type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeString() throws JsonGenerationException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        String number = "abc";
        String json = mapper.writerWithView(NumberSerializer.class)
                .writeValueAsString(number);

        JsonParser parser = mapper.getFactory().createParser(json);
        // This should throw an IllegalArgumentException
        mapper.readerForUpdating(Number.class).readValue(parser);
    }

    /**
     * Test createContextual method for {@code JsonFormat.Value} with string format.
     */
    @Test
    public void testCreateContextualStringFormat() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.STRING);

        JsonSerializer<?> serializerWithFormat = serializer.createContextual(mapper.getSerializerProvider(), null);

        assertEquals(ToStringSerializer.class, serializerWithFormat.getClass());
    }

    /**
     * Test createContextual method for {@code JsonFormat.Value} with number format.
     */
    @Test
    public void testCreateContextualNumberFormat() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.NUMBER);

        JsonSerializer<?> serializerWithFormat = serializer.createContextual(mapper.getSerializerProvider(), null);

        assertEquals(serializer.getClass(), serializerWithFormat.getClass());
    }

    /**
     * Test getSchema method.
     */
    @Test
    public void testGetSchema() throws JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Number.class);

        JsonNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", "object");
        expected.put("properties", JsonNodeFactory.instance.objectNode());
        expected.get("properties").put("$ref", "#/definitions/Number");

        assertEquals(expected, schema);
    }
}