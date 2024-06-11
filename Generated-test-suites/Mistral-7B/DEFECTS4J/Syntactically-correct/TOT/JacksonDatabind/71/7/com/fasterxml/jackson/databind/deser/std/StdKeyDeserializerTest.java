package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertTrue((Boolean) result);
        
        key = "false";
        result = keyDeserializer.deserializeKey(key, context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "50";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Byte) 50, result);
        
        try {
            key = "256";
            result = keyDeserializer.deserializeKey(key, context);
            fail("Expected JsonMappingException for byte key deserialization.");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testShortKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "127";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Short) 127, result);
        
        try {
            key = "32768";
            result = keyDeserializer.deserializeKey(key, context);
            fail("Expected JsonMappingException for short key deserialization.");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testCharKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "A";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Character) 'A', result);
    }

    @Test
    public void testIntKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "10";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Integer) 10, result);
    }

    @Test
    public void testLongKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "1000";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Long) 1000, result);
    }

    @Test
    public void testFloatKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "3.14";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Float) 3.14f, result);
    }

    @Test
    public void testDoubleKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "3.14159";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((Double) 3.14159, result);
    }

    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "en_US";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCurrencyKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, FromStringDeserializer.findDeserializer(Currency.class));
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "USD";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "2022-01-01";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(new Date(2022, 0, 1), result);
    }

    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "2022-01-01";
        Object result = keyDeserializer.deserializeKey(key, context);
        Calendar calendar = (Calendar) result;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUuidKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(UUID.fromString(key), result);
    }

    @Test
    public void testUriKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "https://www.example.com";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(new URI(key), result);
    }

    @Test
    public void testUrlKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "https://www.example.com";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(new URL(key), result);
    }

    @Test
    public void testClassKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "[Ljava.lang.String;@12345678";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(new String[0].getClass(), result.getClass());
        assertEquals("[]", result.toString());
    }

    @Test
    public void testEnumKeyDeserialization() throws Exception {
        enum TestEnum { A, B, C }
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "B";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(TestEnum.B, result);

        try {
            key = "D";
            result = keyDeserializer.deserializeKey(key, context);
            fail("Expected JsonMappingException for unknown enum value.");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testUnknownKeyType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "MyUnknownClass";
        try {
            keyDeserializer.deserializeKey(key, context);
            fail("Expected JsonMappingException for unknown key type.");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}