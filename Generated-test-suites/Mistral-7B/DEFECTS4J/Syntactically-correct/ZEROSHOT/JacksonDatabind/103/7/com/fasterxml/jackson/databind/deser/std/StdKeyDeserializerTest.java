package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyBoolean() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        Assert.assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", new TestContext()));
        Assert.assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyByte() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        Assert.assertEquals((byte) 5, deserializer.deserializeKey("5", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyShort() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        Assert.assertEquals((short) 5, deserializer.deserializeKey("5", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyChar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        Assert.assertEquals('A', deserializer.deserializeKey("A", new TestContext()));

        // Invalid input (multi-character string) should return null
        Assert.assertNull(deserializer.deserializeKey("AB", new TestContext()));
    }

    @Test
    public void testDeserializeKeyInt() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        Assert.assertEquals(5, deserializer.deserializeKey("5", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyLong() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        Assert.assertEquals(1L, deserializer.deserializeKey("1", new TestContext()));
        Assert.assertEquals(1000L, deserializer.deserializeKey("1000", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyFloat() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        Assert.assertEquals(5.0f, deserializer.deserializeKey("5.0", new TestContext()), 0.01f);

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyDouble() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        Assert.assertEquals(5.0, deserializer.deserializeKey("5.0", new TestContext()), 0.001);
        Assert.assertEquals(1000.0, deserializer.deserializeKey("1000.0", new TestContext()), 0.001);

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyLocale() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        Assert.assertEquals(Locale.US, deserializer.deserializeKey("US", new TestContext()));
        Assert.assertEquals(Locale.FRANCE, deserializer.deserializeKey("fr_FR", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyDate() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        String dateString = "2023-03-01";
        Assert.assertEquals(new Date(2023, 2, 1), deserializer.deserializeKey(dateString, new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyCalendar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        String dateString = "2023-03-01";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2023, 2, 1));

        Assert.assertEquals(calendar, deserializer.deserializeKey(dateString, new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyUUID() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        String uuidString = "123e4567-e89b-12d3-a456-426614174000";
        UUID uuid = UUID.fromString(uuidString);

        Assert.assertEquals(uuid, deserializer.deserializeKey(uuidString, new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyURI() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        String uriString = "http://example.com/path?query=value#fragment";
        URI uri = new URI(uriString);

        Assert.assertEquals(uri, deserializer.deserializeKey(uriString, new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyURL() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        String urlString = "http://example.com/path?query=value#fragment";
        URL url = new URL(urlString);

        Assert.assertEquals(url, deserializer.deserializeKey(urlString, new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyClass() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        Assert.assertEquals(String.class, deserializer.deserializeKey("java.lang.String", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyEnum() throws Exception {
        enum MyEnum {
            ONE, TWO, THREE
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);

        Assert.assertEquals(MyEnum.ONE, deserializer.deserializeKey("ONE", new TestContext()));
        Assert.assertEquals(MyEnum.THREE, deserializer.deserializeKey("THREE", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyByteArray() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(byte[].class);

        byte[] byteArray = {1, 2, 3};
        String base64EncodedByteArray = "AQIDBA=="; // base64 encoding of {1, 2, 3}

        Assert.assertArrayEquals(byteArray, deserializer.deserializeKey(base64EncodedByteArray, new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyCurrency() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);

        Assert.assertEquals(Currency.getInstance("USD"), deserializer.deserializeKey("USD", new TestContext()));

        // Invalid input should return null
        Assert.assertNull(deserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDeserializeKeyURLWithSpecialCharacters() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        String urlStringWithSpecialCharacters = "http://example.com/%2Fpath%3Fquery%3Dvalue%23fragment";
        URL urlWithSpecialCharacters = new URL(urlStringWithSpecialCharacters);

        Assert.assertEquals(urlWithSpecialCharacters, deserializer.deserializeKey(urlStringWithSpecialCharacters, new TestContext()));
    }

    @Test
    public void testDeserializeKeyURIWithSpecialCharacters() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        String uriStringWithSpecialCharacters = "http://example.com/%2Fpath%3Fquery%3Dvalue%23fragment";
        URI uriWithSpecialCharacters = new URI(uriStringWithSpecialCharacters);

        Assert.assertEquals(uriWithSpecialCharacters, deserializer.deserializeKey(uriStringWithSpecialCharacters, new TestContext()));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectFactory getFactory() {
            return ObjectMapper.getDefaultInstance().getFactory();
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return new SimpleBeanPropertyDefinition();
        }

        @Override
        public DeserializationConfig getConfig() {
            return new DeserializationConfig(new AnnotationIntrospector(), new ObjectMapper());
        }

        @Override
        public BeanDescription getBeanDescription() {
            return new BeanDescription(getClass(), Arrays.asList(new PropertyDescription[]{}), EnumSet.noneOf(JsonDeserializer.NullValueProvider.class));
        }
    }
}