package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class StdKeyDeserializerTest {

    private DeserializationContext context;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
    }

    @Test
    public void testStringKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StringKD.forType(String.class);
        String key = "test";
        Assert.assertEquals(key, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testObjectKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StringKD.forType(Object.class);
        String key = "test";
        Assert.assertEquals(key, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testBooleanKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        String keyTrue = "true";
        String keyFalse = "false";
        Assert.assertEquals(Boolean.TRUE, deserializer.deserializeKey(keyTrue, context));
        Assert.assertEquals(Boolean.FALSE, deserializer.deserializeKey(keyFalse, context));
    }

    @Test
    public void testByteKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        String key = "127";
        Byte expected = (byte) 127;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testShortKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        String key = "127";
        Short expected = (short) 127;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testCharKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        String key = "A";
        Character expected = 'A';
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testIntKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        String key = "127";
        Integer expected = 127;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testLongKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        String key = "12345678901234567890";
        Long expected = 12345678901234567890L;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        String key = "123.45";
        Float expected = 123.45f;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        String key = "123.45";
        Double expected = 123.45;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_LOCALE, Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
        String key = "en_US";
        Locale expected = Locale.US;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testCurrencyKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_CURRENCY, Currency.class, FromStringDeserializer.findDeserializer(Currency.class));
        String key = "USD";
        Currency expected = Currency.getInstance("USD");
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testDateKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        String key = "2023-03-21";
        Date expected = new Date(2023 - 1900, 2 - 1, 21);
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        String key = "2023-03-21";
        Calendar expected = Calendar.getInstance();
        expected.set(Calendar.YEAR, 2023);
        expected.set(Calendar.MONTH, 2 - 1);
        expected.set(Calendar.DAY_OF_MONTH, 21);
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testUUIDKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        String key = "123e4567-e89b-12d3-a456-426614174000";
        UUID expected = new UUID(123L, 4567893456789L);
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testURIKeyDeserializer() throws IOException, JsonProcessingException, MalformedURLException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        String key = "http://example.com";
        URI expected = new URI(key);
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testURLKeyDeserializer() throws IOException, JsonProcessingException, MalformedURLException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        String key = "http://example.com";
        URL expected = new URL(key);
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testClassKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        String key = "[Ljava.lang.String;";
        Class<?> expected = String[].class;
        Assert.assertEquals(expected, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testDelegateKeyDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = mapper.getTypeFactory().constructKeyDeserializer(HashMap.class, String.class);
        String key = "testMap";
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("test", "test");
        Object result = keyDeserializer.deserializeKey(key, context);
        Assert.assertEquals(expectedMap, result);
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException, JsonProcessingException {
        EnumSet<DayOfWeek> days = EnumSet.allOf(DayOfWeek.class);
        StdKeyDeserializer keyDeserializer = new EnumKD(context.getAnnotationIntrospector().findValueAnnotation(DayOfWeek[].class), context.findMethod("values", DayOfWeek[].class));
        String key = "SUNDAY";
        DayOfWeek expected = DayOfWeek.SUNDAY;
        Assert.assertEquals(expected, keyDeserializer.deserializeKey(key, context));
    }

    @Test
    public void testStringConstructorKeyDeserializer() throws IOException, JsonProcessingException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String className = "java.util.HashMap";
        Constructor<?> constructor = Class.forName(className).getConstructor(String.class);
        StdKeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(constructor);
        String key = "testMap";
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("test", "test");
        Object result = keyDeserializer.deserializeKey(key, context);
        Assert.assertEquals(expectedMap, result);
    }

    @Test
    public void testStaticFactoryMethodKeyDeserializer() throws IOException, JsonProcessingException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String className = "java.util.Collections";
        Method method = Class.forName(className).getMethod("unmodifiableMap", Map.class);
        StdKeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(method);
        String key = "testMap";
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("test", "test");
        Object result = keyDeserializer.deserializeKey(key, context);
        Assert.assertEquals(expectedMap, result);
    }
}