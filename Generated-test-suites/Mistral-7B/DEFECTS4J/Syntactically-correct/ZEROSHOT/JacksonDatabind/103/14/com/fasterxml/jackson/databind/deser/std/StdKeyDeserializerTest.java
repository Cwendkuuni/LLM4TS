package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.ObjectIdReader;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Test class for StdKeyDeserializer.
 */
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testForType() throws Exception {
        Class<?>[] testTypes = {String.class, Object.class, CharSequence.class, UUID.class,
                Integer.class, Long.class, Date.class, Calendar.class, Boolean.class, Byte.class,
                Character.class, Short.class, Float.class, Double.class, URI.class, URL.class,
                Class.class, Locale.class, Currency.class, byte[].class};

        for (Class<?> testType : testTypes) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(testType);
            Assert.assertNotNull(keyDeserializer);
            Assert.assertEquals(testType, keyDeserializer.getKeyClass());
        }
    }

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer booleanKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        Object result = booleanKeyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        result = booleanKeyDeserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        result = booleanKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer byteKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);

        Object result = byteKeyDeserializer.deserializeKey("5", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Byte);
        Assert.assertEquals((byte) 5, result);

        result = byteKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer shortKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);

        Object result = shortKeyDeserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Short);
        Assert.assertEquals((short) 10, result);

        result = shortKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer charKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);

        Object result = charKeyDeserializer.deserializeKey("A", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Character);
        Assert.assertEquals('A', result);

        result = charKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer intKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        Object result = intKeyDeserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Integer);
        Assert.assertEquals(10, result);

        result = intKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer longKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);

        Object result = longKeyDeserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(10L, result);

        result = longKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        StdKeyDeserializer floatKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);

        Object result = floatKeyDeserializer.deserializeKey("10.5", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Float);
        Assert.assertEquals(10.5f, result);

        result = floatKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        StdKeyDeserializer doubleKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);

        Object result = doubleKeyDeserializer.deserializeKey("10.5", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(10.5, result, 0.001);

        result = doubleKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer localeKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);

        Object result = localeKeyDeserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Locale);
        Assert.assertEquals(Locale.US, result);

        result = localeKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testCurrencyKeyDeserializer() throws Exception {
        StdKeyDeserializer currencyKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);

        Object result = currencyKeyDeserializer.deserializeKey("USD", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Currency);
        Assert.assertEquals(Currency.getInstance("USD"), result);

        result = currencyKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer urlKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);

        Object result = urlKeyDeserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals(new URL("http://example.com"), result);

        result = urlKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer uriKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);

        Object result = uriKeyDeserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof URI);
        Assert.assertEquals(new URI("http://example.com"), result);

        result = uriKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        enum TestEnum {
            VALUE_1, VALUE_2, VALUE_3
        }

        StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(TestEnum.class);

        Object result = enumKeyDeserializer.deserializeKey("VALUE_1", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof TestEnum);
        Assert.assertEquals(TestEnum.VALUE_1, result);

        result = enumKeyDeserializer.deserializeKey("VALUE_4", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer classKeyDeserializer = new StdKeyDeserializer(String.class);

        Object result = classKeyDeserializer.deserializeKey("java.lang.String", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Class);
        Assert.assertEquals(String.class, result);

        result = classKeyDeserializer.deserializeKey("java.util.Map", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Class);
        Assert.assertEquals(Map.class, result);

        result = classKeyDeserializer.deserializeKey("InvalidClass", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testByteArrayKeyDeserializer() throws Exception {
        StdKeyDeserializer byteArrayKeyDeserializer = new StdKeyDeserializer(byte[].class);

        Object result = byteArrayKeyDeserializer.deserializeKey("RGVjb2RlIGlzIG15IHVwIHlvdSBmaWxlIG9mIHdpdGhpbmdzIHNlc3Npb24=",
                objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof byte[]);
        byte[] expectedByteArray = "test".getBytes();
        Assert.assertArrayEquals(expectedByteArray, result);

        result = byteArrayKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testStringKeyDeserializer() throws Exception {
        StdKeyDeserializer stringKeyDeserializer = new StdKeyDeserializer(String.class);

        Object result = stringKeyDeserializer.deserializeKey("test", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof String);
        Assert.assertEquals("test", result);

        result = stringKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testObjectKeyDeserializer() throws Exception {
        StdKeyDeserializer objectKeyDeserializer = new StdKeyDeserializer(Object.class);

        Object result = objectKeyDeserializer.deserializeKey("{\"name\":\"test\"}", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Map);
        Map<String, Object> mapResult = (Map<String, Object>) result;
        Assert.assertEquals("test", mapResult.get("name"));

        result = objectKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testCharSequenceKeyDeserializer() throws Exception {
        StdKeyDeserializer charSequenceKeyDeserializer = new StdKeyDeserializer(CharSequence.class);

        Object result = charSequenceKeyDeserializer.deserializeKey("test", objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof CharSequence);
        Assert.assertEquals("test", result);

        result = charSequenceKeyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        Assert.assertNull(result);
    }

    @Test
    public void testEnumSetKeyDeserializer() throws Exception {
        enum TestEnum {
            VALUE_1, VALUE_2, VALUE_3
        }

        Set<TestEnum> expectedSet = EnumSet.of(TestEnum.VALUE_1, TestEnum.VALUE_2);

        Constructor<EnumSet<TestEnum>> constructor = EnumSet.enumSet(TestEnum.class).getDeclaredConstructor((Class<TestEnum>) null);
        constructor.setAccessible(true);

        AnnotatedClass ac = objectMapper.getAnnotationIntrospector().findAnnotated(EnumSet.class);
        AnnotatedMethod am = ac.getDeclaredMember("newInstance");
        Method method = am.getRawMember();

        Object result = new StdKeyDeserializer(new StringCtorKeyDeserializer(constructor), method).deserializeKey("[VALUE_1, VALUE_2]",
                objectMapper.getDeserializationContext());

        Assert.assertTrue(result instanceof EnumSet);
        Assert.assertEquals(expectedSet, result);

        result = new StdKeyDeserializer(new StringFactoryKeyDeserializer(method), constructor).deserializeKey("[VALUE_1, VALUE_2]",
                objectMapper.getDeserializationContext());

        Assert.assertTrue(result instanceof EnumSet);
        Assert.assertEquals(expectedSet, result);
    }
}