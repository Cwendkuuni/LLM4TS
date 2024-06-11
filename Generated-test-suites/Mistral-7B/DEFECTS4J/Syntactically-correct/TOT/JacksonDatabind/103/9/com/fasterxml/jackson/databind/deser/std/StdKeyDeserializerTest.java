package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("true", ctxt);
        Assert.assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", ctxt);
        Assert.assertEquals(Boolean.FALSE, key);

        try {
            key = serializer.deserializeKey("not-a-boolean", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testByteKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("1", ctxt);
        Assert.assertEquals((byte) 1, key);

        try {
            key = serializer.deserializeKey("-128", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = serializer.deserializeKey("256", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("12345", ctxt);
        Assert.assertEquals((short) 12345, key);

        try {
            key = serializer.deserializeKey("-32768", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = serializer.deserializeKey("32768", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("A", ctxt);
        Assert.assertEquals('A', key);

        try {
            key = serializer.deserializeKey("äöü", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("1234567890", ctxt);
        Assert.assertEquals(1234567890, key);
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("12345678901234567890", ctxt);
        Assert.assertEquals(12345678901234567890L, key);
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("1.23456789", ctxt);
        Assert.assertEquals(1.23456789f, key, 0.001f);
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("1.234567891234567890", ctxt);
        Assert.assertEquals(1.234567891234567890, key, 0.000001);
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("en_US", ctxt);
        Assert.assertEquals(Locale.US, key);

        try {
            key = serializer.deserializeKey("wrong", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("2022-01-01", ctxt);
        Assert.assertNotNull(key);
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("2022-01-01", ctxt);
        Assert.assertNotNull(key);
    }

    @Test
    public void testUriKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("http://example.com", ctxt);
        Assert.assertEquals(new URI("http://example.com"), key);

        try {
            key = serializer.deserializeKey("wrong", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testUrlKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("http://example.com", ctxt);
        Assert.assertEquals(new URL("http://example.com"), key);

        try {
            key = serializer.deserializeKey("wrong", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(EnumSet.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("of", ctxt);
        Assert.assertEquals(EnumSet.of("A", "B"), key);

        try {
            key = serializer.deserializeKey("wrong", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testClassKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("java.lang.String", ctxt);
        Assert.assertEquals(String.class, key);
    }

    @Test
    public void testByteArrayKeyDeserializer() throws IOException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(byte[].class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("A==", ctxt);
        Assert.assertArrayEquals(new byte[]{65, 65}, key);

        try {
            key = serializer.deserializeKey("wrong", ctxt);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}