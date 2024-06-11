package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testDefaultConstruction() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Assert.assertNotNull(keyDeserializer);
        Assert.assertEquals(keyDeserializer.getKeyClass(), String.class);
        Assert.assertNull(keyDeserializer._deser);
    }

    @Test
    public void testBooleanTypeConstruction() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Assert.assertNotNull(keyDeserializer);
        Assert.assertEquals(keyDeserializer.getKeyClass(), Boolean.class);
        Assert.assertNull(keyDeserializer._deser);
    }

    @Test
    public void testPrimitiveIntegerTypesConstruction() {
        StdKeyDeserializer intKeyDeserializer = StdKeyDeserializer.forType(int.class);
        Assert.assertNotNull(intKeyDeserializer);
        Assert.assertEquals(intKeyDeserializer.getKeyClass(), int.class);
        Assert.assertNull(intKeyDeserializer._deser);

        StdKeyDeserializer shortKeyDeserializer = StdKeyDeserializer.forType(short.class);
        Assert.assertNotNull(shortKeyDeserializer);
        Assert.assertEquals(shortKeyDeserializer.getKeyClass(), short.class);
        Assert.assertNull(shortKeyDeserializer._deser);

        StdKeyDeserializer byteKeyDeserializer = StdKeyDeserializer.forType(byte.class);
        Assert.assertNotNull(byteKeyDeserializer);
        Assert.assertEquals(byteKeyDeserializer.getKeyClass(), byte.class);
        Assert.assertNull(byteKeyDeserializer._deser);
    }

    @Test
    public void testStringTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, key);
    }

    @Test
    public void testBooleanTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertTrue((Boolean) result);

        key = "false";
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testByteTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte.class);
        String key = "123";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, (byte) 123);
    }

    @Test
    public void testShortTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(short.class);
        String key = "12345";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, (short) 12345);
    }

    @Test
    public void testCharTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(char.class);
        String key = "A";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, 'A');
    }

    @Test
    public void testIntTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(int.class);
        String key = "123456";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, 123456);
    }

    @Test
    public void testLongTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(long.class);
        String key = "12345678901234567890";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, 12345678901234567890L);
    }

    @Test
    public void testFloatTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(float.class);
        String key = "123.45";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, Float.parseFloat(key), 0.01f);
    }

    @Test
    public void testDoubleTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(double.class);
        String key = "123.45";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, Double.parseDouble(key), 0.0001);
    }

    @Test
    public void testLocaleTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        String key = "de_DE";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, Locale.forLanguageTag(key));
    }

    @Test
    public void testDateTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        String key = "2023-02-13";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, new Date(System.currentTimeMillis())); // date should be set to the current date and time
        Assert.assertEquals(result.getClass(), Date.class);
    }

    @Test
    public void testCalendarTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        String key = "2023-02-13";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertTrue(result instanceof Calendar);
        Assert.assertEquals(result.getClass(), Calendar.class);
        Calendar calendar = (Calendar) result;
        Assert.assertEquals(calendar.get(Calendar.YEAR), 2023);
        Assert.assertEquals(calendar.get(Calendar.MONTH), Calendar.FEBRUARY);
        Assert.assertEquals(calendar.get(Calendar.DATE), 13);
    }

    @Test
    public void testUriTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        String key = "https://www.example.com";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertTrue(result instanceof URI);
        Assert.assertEquals(result.getClass(), URI.class);
        URI uri = (URI) result;
        Assert.assertEquals(uri.getScheme(), "https");
        Assert.assertEquals(uri.getHost(), "www.example.com");
    }

    @Test
    public void testUrlTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        String key = "https://www.example.com";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals(result.getClass(), URL.class);
        URL url = (URL) result;
        Assert.assertEquals(url.getProtocol(), "https:");
        Assert.assertEquals(url.getHost(), "www.example.com");
    }

    @Test
    public void testEnumTypeDeserialization() throws IOException {
        Enum testEnum = TestEnum.ONE;
        EnumResolver enumResolver = new EnumResolver(TestEnum.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(2, TestEnum.class, enumResolver);
        String key = testEnum.name();
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, testEnum);
    }

    @Test
    public void testClassTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        String key = "[Ljava.lang.String;";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertTrue(result.getClass().isArray());
        Assert.assertTrue(result.getClass().getComponentType().equals(String.class));
    }

    @Test
    public void testCurrencyTypeDeserialization() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        String key = "USD";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertTrue(result instanceof Currency);
        Assert.assertEquals(result.getClass(), Currency.class);
        Currency currency = (Currency) result;
        Assert.assertEquals(currency.getCurrencyCode(), "USD");
    }

    @Test
    public void testCustomKeyDeserializer() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        CustomKeyDeserializer customKeyDeserializer = new CustomKeyDeserializer();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, CustomKeyDeserializer.class, customKeyDeserializer);
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(null, null, null, null, null));
        Assert.assertEquals(result, customKeyDeserializer.deserializeKey(key));
    }

    private static class CustomKeyDeserializer {
        public Object deserializeKey(String key) {
            return key.toUpperCase();
        }
    }
}