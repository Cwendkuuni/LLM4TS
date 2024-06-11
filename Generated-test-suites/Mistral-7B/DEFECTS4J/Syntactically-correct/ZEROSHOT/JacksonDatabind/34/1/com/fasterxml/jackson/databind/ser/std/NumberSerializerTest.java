package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderMutator;
import com.fasterxml.jackson.databind.SerializerProvider.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerImpl;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    public NumberSerializerTest() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new BigDecimal("123.45");
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "\"123.45\"";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new BigInteger("12345678901234567890");
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "\"12345678901234567890\"";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeInt() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new Integer(123);
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new Long(1234567890123L);
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "1234567890123";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new Double(123.45);
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123.45";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new Float(123.45F);
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123.45";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new Byte((byte) 123);
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        Number number = new Short((short) 123);
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedJson = "123";
        String actualJson = jsonGenerator.getOutputContent().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof BigDecimal);
        assertEquals(new BigDecimal("123.45"), number);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof BigInteger);
        assertEquals(new BigInteger("12345678901234567890"), number);
    }

    @Test
    public void testDeserializeInt() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof Integer);
        assertEquals(123, number.intValue());
    }

    @Test
    public void testDeserializeLong() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof Long);
        assertEquals(1234567890123L, number.longValue());
    }

    @Test
    public void testDeserializeDouble() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof Double);
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test
    public void testDeserializeFloat() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof Float);
        assertEquals(123.45F, number, 0.001f);
    }

    @Test
    public void testDeserializeByte() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof Byte);
        assertEquals((byte) 123, number.byteValue());
    }

    @Test
    public void testDeserializeShort() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input.json"));

        Number number = objectReader.readValueAs(Number.class);
        assertTrue(number instanceof Short);
        assertEquals((short) 123, number.shortValue());
    }

    @Test
    public void testDeserializeUnsupportedNumberType() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.getFactory().createObjectReader(NumberSerializerTest.class.getResource("/input_unsupported.json"));

        try {
            Number number = objectReader.readValueAs(Number.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        SchemaSerializerMap schemaSerializerMap = objectMapper.getSerializationConfig()
                .getDefaultSerializerProvider().getSerializerProviderMutator()
                .findAndAddSerializer(Number.class, numberSerializer);

        JsonNode schema = numberSerializer.getSchema(objectMapper.getSerializerProvider(), Number.class);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getSerializationConfig()
                .getDefaultSerializerProvider().getSerializerProviderMutator()
                .findAndAddSerializer(Number.class, numberSerializer);

        jsonFormatVisitorWrapper.expectNumberFormat(Number.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimalTypeHint() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getSerializationConfig()
                .getDefaultSerializerProvider().getSerializerProviderMutator()
                .findAndAddSerializer(Number.class, numberSerializer);

        jsonFormatVisitorWrapper.expectNumberFormat(new TypeReference<Number>() {}.getType());
        TypeDeserializer typeDeserializer = jsonFormatVisitorWrapper.findValueDeserializer(new TypeReference<Number>() {}.getType());
        assertEquals(TypeSerializer.Empty.class, typeDeserializer.getClass());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigIntegerTypeHint() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getSerializationConfig()
                .getDefaultSerializerProvider().getSerializerProviderMutator()
                .findAndAddSerializer(Number.class, numberSerializer);

        jsonFormatVisitorWrapper.expectNumberFormat(BigInteger.class);
        TypeDeserializer typeDeserializer = jsonFormatVisitorWrapper.findValueDeserializer(BigInteger.class);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof TypeSerializerImpl);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithUnsupportedTypeHint() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getSerializationConfig()
                .getDefaultSerializerProvider().getSerializerProviderMutator()
                .findAndAddSerializer(Number.class, numberSerializer);

        jsonFormatVisitorWrapper.expectNumberFormat(String.class);
        try {
            jsonFormatVisitorWrapper.visitStringFormat(String.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private static class Input {
        private Number number;
    }

}