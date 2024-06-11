package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Test;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() throws Exception {
        Class<?> stringClass = String.class;
        Class<?> booleanClass = Boolean.class;
        Class<?> byteClass = Byte.class;
        Class<?> shortClass = Short.class;
        Class<?> charClass = Character.class;
        Class<?> intClass = Integer.class;
        Class<?> longClass = Long.class;
        Class<?> floatClass = Float.class;
        Class<?> doubleClass = Double.class;
        Class<?> localeClass = Locale.class;
        Class<?> dateClass = Date.class;
        Class<?> calendarClass = Calendar.class;
        Class<?> uriClass = URI.class;
        Class<?> urlClass = URL.class;
        Class<?> classClass = Class.class;
        Class<?> uuidClass = UUID.class;
        Class<?> enumClass = Enum.class;

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(stringClass);
        assertEquals("StringKD", keyDeserializer.getClass().getSimpleName());
        assertEquals(String.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(booleanClass);
        assertEquals("StdKeyDeserializer for type boolean", keyDeserializer.getClass().getSimpleName());
        assertEquals(Boolean.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(byteClass);
        assertEquals("StdKeyDeserializer for type byte", keyDeserializer.getClass().getSimpleName());
        assertEquals(Byte.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(shortClass);
        assertEquals("StdKeyDeserializer for type short", keyDeserializer.getClass().getSimpleName());
        assertEquals(Short.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(charClass);
        assertEquals("StdKeyDeserializer for type char", keyDeserializer.getClass().getSimpleName());
        assertEquals(Character.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(intClass);
        assertEquals("StdKeyDeserializer for type int", keyDeserializer.getClass().getSimpleName());
        assertEquals(Integer.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(longClass);
        assertEquals("StdKeyDeserializer for type long", keyDeserializer.getClass().getSimpleName());
        assertEquals(Long.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(floatClass);
        assertEquals("StdKeyDeserializer for type float", keyDeserializer.getClass().getSimpleName());
        assertEquals(Float.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(doubleClass);
        assertEquals("StdKeyDeserializer for type double", keyDeserializer.getClass().getSimpleName());
        assertEquals(Double.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(localeClass);
        assertEquals("StdKeyDeserializer for type Locale", keyDeserializer.getClass().getSimpleName());
        assertEquals(Locale.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(dateClass);
        assertEquals("StdKeyDeserializer for type Date", keyDeserializer.getClass().getSimpleName());
        assertEquals(Date.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(calendarClass);
        assertEquals("StdKeyDeserializer for type Calendar", keyDeserializer.getClass().getSimpleName());
        assertEquals(Calendar.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(uriClass);
        assertEquals("StdKeyDeserializer for type URI", keyDeserializer.getClass().getSimpleName());
        assertEquals(URI.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(urlClass);
        assertEquals("StdKeyDeserializer for type URL", keyDeserializer.getClass().getSimpleName());
        assertEquals(URL.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(classClass);
        assertEquals("StdKeyDeserializer for type Class", keyDeserializer.getClass().getSimpleName());
        assertEquals(Class.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(uuidClass);
        assertEquals("StdKeyDeserializer for type UUID", keyDeserializer.getClass().getSimpleName());
        assertEquals(UUID.class, keyDeserializer.getKeyClass());

        keyDeserializer = StdKeyDeserializer.forType(enumClass);
        assertEquals("EnumKD", keyDeserializer.getClass().getSimpleName());
        assertEquals(Enum.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testDeserializeKey() throws Exception {
        Class<?> stringClass = String.class;
        Class<?> booleanClass = Boolean.class;
        Class<?> byteClass = Byte.class;
        Class<?> shortClass = Short.class;
        Class<?> charClass = Character.class;
        Class<?> intClass = Integer.class;
        Class<?> longClass = Long.class;
        Class<?> floatClass = Float.class;
        Class<?> doubleClass = Double.class;
        Class<?> localeClass = Locale.class;
        Class<?> dateClass = Date.class;
        Class<?> calendarClass = Calendar.class;
        Class<?> uriClass = URI.class;
        Class<?> urlClass = URL.class;
        Class<?> classClass = Class.class;
        Class<?> enumClass = Enum.class;

        ObjectMapper mapper = new ObjectMapper();

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(stringClass);
        String key = "true";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals("true", deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(booleanClass);
        key = "true";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Boolean);
        assertEquals(Boolean.TRUE, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(byteClass);
        key = "100";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Byte);
        assertEquals((byte) 100, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(shortClass);
        key = "200";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Short);
        assertEquals((short) 200, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(charClass);
        key = "A";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Character);
        assertEquals('A', deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(intClass);
        key = "300";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Integer);
        assertEquals(300, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(longClass);
        key = "400";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Long);
        assertEquals(400L, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(floatClass);
        key = "5.5";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Float);
        assertEquals(5.5f, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(doubleClass);
        key = "6.6";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Double);
        assertEquals(6.6, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(localeClass);
        key = "en_US";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Locale);
        assertEquals(Locale.US, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(dateClass);
        key = "2022-01-01";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Date);
        assertEquals(new Date(0), deserializedKey); // Default Date constructor uses current time

        keyDeserializer = StdKeyDeserializer.forType(calendarClass);
        key = "2022-01-01";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Calendar);
        assertNotNull(deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(uriClass);
        key = "http://example.com";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof URI);
        assertEquals(new URI("http://example.com"), deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(urlClass);
        key = "http://example.com";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof URL);
        assertEquals(new URL("http://example.com"), deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(classClass);
        key = "java.util.List";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Class);
        assertEquals(List.class, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(enumClass);
        key = "MyEnum.VALUE_1";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Enum);
        assertEquals("MyEnum", deserializedKey.getClass().getSimpleName());
        assertEquals("VALUE_1", deserializedKey.toString());

        keyDeserializer = StdKeyDeserializer.forType(null);
        key = null;
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}