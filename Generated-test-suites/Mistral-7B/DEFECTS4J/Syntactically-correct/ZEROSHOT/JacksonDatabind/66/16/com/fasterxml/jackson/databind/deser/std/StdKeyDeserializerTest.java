package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@JacksonStdImpl
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer stringCtorKeyDeserializer;
    private StdKeyDeserializer stringFactoryKeyDeserializer;
    private StdKeyDeserializer booleanKD;
    private StdKeyDeserializer byteKD;
    private StdKeyDeserializer shortKD;
    private StdKeyDeserializer charKD;
    private StdKeyDeserializer intKD;
    private StdKeyDeserializer longKD;
    private StdKeyDeserializer floatKD;
    private StdKeyDeserializer doubleKD;
    private StdKeyDeserializer localeKD;
    private StdKeyDeserializer dateKD;
    private StdKeyDeserializer calendarKD;
    private StdKeyDeserializer uriKD;
    private StdKeyDeserializer urlKD;
    private StdKeyDeserializer classKD;
    private StdKeyDeserializer enumKD;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        stringKD = StdKeyDeserializer.forType(String.class);
        stringCtorKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        stringFactoryKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class, null);
        booleanKD = StdKeyDeserializer.forType(Boolean.class);
        byteKD = StdKeyDeserializer.forType(Byte.class);
        shortKD = StdKeyDeserializer.forType(Short.class);
        charKD = StdKeyDeserializer.forType(Character.class);
        intKD = StdKeyDeserializer.forType(Integer.class);
        longKD = StdKeyDeserializer.forType(Long.class);
        floatKD = StdKeyDeserializer.forType(Float.class);
        doubleKD = StdKeyDeserializer.forType(Double.class);
        localeKD = StdKeyDeserializer.forType(Locale.class);
        dateKD = StdKeyDeserializer.forType(Date.class);
        calendarKD = StdKeyDeserializer.forType(Calendar.class);
        uriKD = StdKeyDeserializer.forType(URI.class);
        urlKD = StdKeyDeserializer.forType(URL.class);
        classKD = StdKeyDeserializer.forType(Class.class);
        enumKD = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Enum.class);
    }

    @Test
    public void testStringDeserializer() throws IOException {
        Object key = objectMapper.readValue("\"test\"", String.class);
        Assert.assertEquals("test", key);
    }

    @Test
    public void testStringKDDeserializer() throws IOException {
        String key = (String) stringKD.deserializeKey("test", objectMapper.getDeserializationContext());
        Assert.assertEquals("test", key);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String key = (String) stringCtorKeyDeserializer.deserializeKey("test", objectMapper.getDeserializationContext());
        Assert.assertEquals("test", key);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws IOException {
        String key = (String) stringFactoryKeyDeserializer.deserializeKey("test", objectMapper.getDeserializationContext());
        Assert.assertEquals("test", key);
    }

    @Test(expected = JsonMappingException.class)
    public void testBooleanDeserializer() throws IOException {
        Object key = objectMapper.readValue("false", Boolean.class);
        Assert.assertFalse((Boolean) key);
    }

    @Test
    public void testBooleanKDDeserializer() throws IOException {
        Boolean key = (Boolean) booleanKD.deserializeKey("false", objectMapper.getDeserializationContext());
        Assert.assertFalse((Boolean) key);
    }

    @Test
    public void testByteDeserializer() throws IOException {
        Object key = objectMapper.readValue("1", Byte.class);
        Assert.assertEquals((byte) 1, (Byte) key);
    }

    @Test
    public void testByteKDDeserializer() throws IOException {
        Byte key = (Byte) byteKD.deserializeKey("1", objectMapper.getDeserializationContext());
        Assert.assertEquals((byte) 1, key);
    }

    @Test
    public void testShortDeserializer() throws IOException {
        Object key = objectMapper.readValue("123", Short.class);
        Assert.assertEquals((short) 123, (Short) key);
    }

    @Test
    public void testShortKDDeserializer() throws IOException {
        Short key = (Short) shortKD.deserializeKey("123", objectMapper.getDeserializationContext());
        Assert.assertEquals((short) 123, key);
    }

    @Test
    public void testCharDeserializer() throws IOException {
        Object key = objectMapper.readValue("'a'", Character.class);
        Assert.assertEquals('a', (Character) key);
    }

    @Test
    public void testCharKDDeserializer() throws IOException {
        Character key = (Character) charKD.deserializeKey("'a'", objectMapper.getDeserializationContext());
        Assert.assertEquals('a', key);
    }

    @Test
    public void testIntDeserializer() throws IOException {
        Object key = objectMapper.readValue("12345", Integer.class);
        Assert.assertEquals(12345, (Integer) key);
    }

    @Test
    public void testIntKDDeserializer() throws IOException {
        Integer key = (Integer) intKD.deserializeKey("12345", objectMapper.getDeserializationContext());
        Assert.assertEquals(12345, key);
    }

    @Test
    public void testLongDeserializer() throws IOException {
        Object key = objectMapper.readValue("123456789012345", Long.class);
        Assert.assertEquals(123456789012345L, (Long) key);
    }

    @Test
    public void testLongKDDeserializer() throws IOException {
        Long key = (Long) longKD.deserializeKey("123456789012345", objectMapper.getDeserializationContext());
        Assert.assertEquals(123456789012345L, key);
    }

    @Test
    public void testFloatDeserializer() throws IOException {
        Object key = objectMapper.readValue("1.23", Float.class);
        Assert.assertEquals(1.23f, (Float) key);
    }

    @Test
    public void testFloatKDDeserializer() throws IOException {
        Float key = (Float) floatKD.deserializeKey("1.23", objectMapper.getDeserializationContext());
        Assert.assertEquals(1.23f, key);
    }

    @Test
    public void testDoubleDeserializer() throws IOException {
        Object key = objectMapper.readValue("1.23456789", Double.class);
        Assert.assertEquals(1.23456789, (Double) key, 0.0);
    }

    @Test
    public void testDoubleKDDeserializer() throws IOException {
        Double key = (Double) doubleKD.deserializeKey("1.23456789", objectMapper.getDeserializationContext());
        Assert.assertEquals(1.23456789, key, 0.0);
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        Object key = objectMapper.readValue("en_US", Locale.class);
        Assert.assertEquals(Locale.US, (Locale) key);
    }

    @Test
    public void testLocaleKDDeserializer() throws IOException {
        Locale key = (Locale) localeKD.deserializeKey("en_US", objectMapper.getDeserializationContext());
        Assert.assertEquals(Locale.US, key);
    }

    @Test
    public void testDateDeserializer() throws IOException {
        Object key = objectMapper.readValue("2022-03-03", Date.class);
        Assert.assertEquals(new Date(1646326400000L), key);
    }

    @Test
    public void testDateKDDeserializer() throws IOException {
        Date key = (Date) dateKD.deserializeKey("2022-03-03", objectMapper.getDeserializationContext());
        Assert.assertEquals(new Date(1646326400000L), key);
    }

    @Test
    public void testCalendarDeserializer() throws IOException {
        Object key = objectMapper.readValue("2022-03-03", Calendar.class);
        Calendar calendar = (Calendar) key;
        Assert.assertEquals(2022, calendar.get(Calendar.YEAR));
        Assert.assertEquals(3, calendar.get(Calendar.MONTH) + 1);
        Assert.assertEquals(3, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testCalendarKDDeserializer() throws IOException {
        Calendar key = (Calendar) calendarKD.deserializeKey("2022-03-03", objectMapper.getDeserializationContext());
        Assert.assertEquals(2022, key.get(Calendar.YEAR));
        Assert.assertEquals(3, key.get(Calendar.MONTH) + 1);
        Assert.assertEquals(3, key.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testURIDeserializer() throws IOException {
        Object key = objectMapper.readValue("http://test.com", URI.class);
        Assert.assertEquals(new URI("http://test.com"), key);
    }

    @Test
    public void testURIKDDeserializer() throws IOException {
        URI key = (URI) uriKD.deserializeKey("http://test.com", objectMapper.getDeserializationContext());
        Assert.assertEquals(new URI("http://test.com"), key);
    }

    @Test
    public void testURIDeserializer() throws IOException {
        Object key = objectMapper.readValue("http://test.com", URL.class);
        Assert.assertEquals(new URL("http://test.com"), key);
    }

    @Test
    public void testURILKDDeserializer() throws IOException {
        URL key = (URL) urlKD.deserializeKey("http://test.com", objectMapper.getDeserializationContext());
        Assert.assertEquals(new URL("http://test.com"), key);
    }

    @Test
    public void testClassDeserializer() throws IOException {
        Object key = objectMapper.readValue("java.lang.String", Class.class);
        Assert.assertEquals(String.class, (Class<?>) key);
    }

    @Test
    public void testClassKDDeserializer() throws IOException {
        Class<?> key = (Class<?>) classKD.deserializeKey("java.lang.String", objectMapper.getDeserializationContext());
        Assert.assertEquals(String.class, key);
    }

    @Test
    public void testEnumDeserializer() throws IOException {
        Object key = objectMapper.readValue("RED", EnumSet.allOf(Color.class).getClass());
        Assert.assertEquals(Color.RED, (Enum<?>) key);
    }

    @Test
    public void testEnumKDDeserializer() throws IOException {
        Enum<?> key = (Enum<?>) enumKD.deserializeKey("RED", objectMapper.getDeserializationContext());
        Assert.assertEquals(Color.RED, key);
    }
}