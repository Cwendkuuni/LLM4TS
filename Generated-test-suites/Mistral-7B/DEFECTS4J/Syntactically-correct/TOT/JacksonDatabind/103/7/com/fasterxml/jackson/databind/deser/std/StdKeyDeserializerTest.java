package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper = new JsonMapper();

    @Test
    public void testBooleanKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testByteKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("5", context);
        assertEquals((byte) 5, result);

        result = keyDeserializer.deserializeKey("127", context);
        assertEquals((byte) 127, result);

        result = keyDeserializer.deserializeKey("128", context);
        assertNull(result);
    }

    @Test
    public void testShortKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("10", context);
        assertEquals((short) 10, result);

        result = keyDeserializer.deserializeKey("32767", context);
        assertEquals((short) 32767, result);

        result = keyDeserializer.deserializeKey("32768", context);
        assertNull(result);
    }

    @Test
    public void testCharKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("A", context);
        assertEquals((char) 'A', result);

        result = keyDeserializer.deserializeKey("Z", context);
        assertEquals((char) 'Z', result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testIntKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("10", context);
        assertEquals(10, result);

        result = keyDeserializer.deserializeKey("Integer.MAX_VALUE", context);
        assertEquals(Integer.MAX_VALUE, result);

        result = keyDeserializer.deserializeKey("Integer.MIN_VALUE", context);
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testLongKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("10", context);
        assertEquals(10L, result);

        result = keyDeserializer.deserializeKey("Long.MAX_VALUE", context);
        assertEquals(Long.MAX_VALUE, result);

        result = keyDeserializer.deserializeKey("Long.MIN_VALUE", context);
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testFloatKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("10.5", context);
        assertEquals(10.5f, result, 0.01f);

        result = keyDeserializer.deserializeKey("Float.NaN", context);
        assertEquals(Float.NaN, result);

        result = keyDeserializer.deserializeKey("Float.POSITIVE_INFINITY", context);
        assertEquals(Float.POSITIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("Float.NEGATIVE_INFINITY", context);
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDoubleKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("10.5", context);
        assertEquals(10.5, result, 0.01);

        result = keyDeserializer.deserializeKey("Double.NaN", context);
        assertEquals(Double.NaN, result);

        result = keyDeserializer.deserializeKey("Double.POSITIVE_INFINITY", context);
        assertEquals(Double.POSITIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("Double.NEGATIVE_INFINITY", context);
        assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testLocaleKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, result);

        result = keyDeserializer.deserializeKey("fr_FR", context);
        assertEquals(Locale.FRANCE, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDateKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("2022-12-31", context);
        assertEquals(new Date(System.currentTimeMillis()), result); // Using current date and time

        result = keyDeserializer.deserializeKey("2022-12-31T12:00:00Z", context);
        assertEquals(new Date(1671657600000L), result); // Using a fixed date and time

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testCalendarKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("2022-12-31", context);
        assertEquals(Calendar.getInstance(), result); // Using current date

        result = keyDeserializer.deserializeKey("2022-12-31T12:00:00Z", context);
        assertEquals(Calendar.getInstance(), result); // Using current date and time

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testEnumKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("VALUE_ONE", context);
        assertEquals(MyEnum.VALUE_ONE, result);

        result = keyDeserializer.deserializeKey("VALUE_TWO", context);
        assertEquals(MyEnum.VALUE_TWO, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testURIKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("https://example.com", context);
        assertEquals(new URI("https://example.com"), result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testURLKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testClassKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyClass.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("com.example.MyClass", context);
        assertEquals(MyClass.class, result.getClass());

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testByteArrayKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte[].class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("RqQyV1PbT2rczJs0Qz9jwio1mw==", context);
        byte[] expected = {(byte) 0x52, (byte) 0x61, (byte) 0x71, (byte) 0x6d, (byte) 0x79, (byte) 0x73, (byte) 0x73, (byte) 0x74, (byte) 0x72, (byte) 0x6f, (byte) 0x63, (byte) 0x6d, (byte) 0x69, (byte) 0x73, (byte) 0x73, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x65, (byte) 0x74};
        assertArrayEquals(expected, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testNullKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey(null, context);
        assertNull(result);
    }

    @Test
    public void testInvalidKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = objectMapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("invalidKey", context);
        assertNull(result);
    }

    @Test
    public void testDeserializationWithUnknownClass() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"key\": \"MyUnknownClass\"}";
        DeserializationContext context = mapper.getDeserializationContext();

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object result = keyDeserializer.deserializeKey("MyUnknownClass", context);
        assertNull(result);
    }

    @Test
    public void testDeserializationWithNullKey() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"key\": null}";
        DeserializationContext context = mapper.getDeserializationContext();

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object result = keyDeserializer.deserializeKey(null, context);
        assertNull(result);
    }
}