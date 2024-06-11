package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "true";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(Boolean.TRUE, deserialized);

        key = "false";
        deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(Boolean.FALSE, deserialized);
    }

    @Test
    public void testByteDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "127";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals((Byte) 127, deserialized);

        key = "-128";
        deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals((Byte) -128, deserialized);

        key = "256";
        assertThrows(DeserializationFeature.HandleWeirdKeyException.class, () -> keyDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testShortDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "32767";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals((Short) 32767, deserialized);

        key = "-32768";
        deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals((Short) -32768, deserialized);

        key = "65536";
        assertThrows(DeserializationFeature.HandleWeirdKeyException.class, () -> keyDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testCharDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "A";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals((Character) 'A', deserialized);
    }

    @Test
    public void testIntDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "42";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(42, deserialized);
    }

    @Test
    public void testLongDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "9223372036854775807";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(9223372036854775807L, deserialized);

        key = "-9223372036854775808";
        deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(-9223372036854775808L, deserialized);
    }

    @Test
    public void testFloatDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "3.14";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(3.14f, deserialized);
    }

    @Test
    public void testDoubleDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "6.2831853";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(6.2831853, deserialized);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "fr_FR";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(Locale.FRENCH, deserialized);
    }

    @Test
    public void testUUIDDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "2f8bcbd2-3b1d-4a71-80b4-7e2efa12d2e4";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(new UUID("2f8bcbd2-3b1d-4a71-80b4-7e2efa12d2e4"), deserialized);
    }

    @Test
    public void testURIDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "http://www.example.com";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(new URI("http://www.example.com"), deserialized);
    }

    @Test
    public void testURLDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "http://www.example.com";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(new URL("http://www.example.com"), deserialized);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "java.lang.String[]";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(String[].class, deserialized.getClass());
    }

    @Test
    public void testNullKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object deserialized = keyDeserializer.deserializeKey(null, context);
        assertNull(deserialized);
    }

    @Test
    public void testInvalidKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "invalid_key";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertThrows(DeserializationFeature.HandleWeirdKeyException.class, () -> keyDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testInvalidNumberKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "invalid_number_key";
        assertThrows(DeserializationFeature.HandleWeirdKeyException.class, () -> keyDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testUnknownEnumKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "en_US_Unknown";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertNull(deserialized);
    }

    @Test
    public void testUnknownEnumKeyWithNullValues() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mapper.getDeserializationContext();
        context.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        String key = "en_US_Unknown";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertEquals(null, deserialized);
    }

    @Test
    public void testUnknownEnumKeyWithException() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "en_US_Unknown";
        assertThrows(DeserializationFeature.ReadUnknownEnumValueException.class, () -> {
            keyDeserializer.deserializeKey(key, context);
        });
    }

    @Test
    public void testUnknownClassKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TypeDeseralizer.class);
        DeserializationContext context = mapper.getDeserializationContext();
        String key = "com.unknown.TypeDeserializer";
        Object deserialized = keyDeserializer.deserializeKey(key, context);
        assertNull(deserialized);
    }
}