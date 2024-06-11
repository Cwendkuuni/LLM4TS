package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("true", ctxt);
        Assert.assertEquals(key, Boolean.TRUE);

        key = deserializer.deserializeKey("false", ctxt);
        Assert.assertEquals(key, Boolean.FALSE);

        key = deserializer.deserializeKey("false5", ctxt);
        Assert.assertEquals(key, Boolean.FALSE);
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("10", ctxt);
        Assert.assertEquals(key, (byte) 10);
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("100", ctxt);
        Assert.assertEquals(key, (short) 100);
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("A", ctxt);
        Assert.assertEquals(key, 'A');
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("1000", ctxt);
        Assert.assertEquals(key, 1000);
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("1000000", ctxt);
        Assert.assertEquals(key, 1000000L);
    }

    @Test
    public void testDeserializeKey_Float() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("1.5", ctxt);
        Assert.assertEquals(key, 1.5f);
    }

    @Test
    public void testDeserializeKey_Double() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("1.5", ctxt);
        Assert.assertEquals(key, 1.5);
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("en_US", ctxt);
        Assert.assertEquals(key, Locale.US);
    }

    @Test
    public void testDeserializeKey_Date() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("2023-03-01", ctxt);
        Assert.assertInstanceOf(key, Date.class);
    }

    @Test
    public void testDeserializeKey_Calendar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("2023-03-01", ctxt);
        Assert.assertInstanceOf(key, Calendar.class);
    }

    @Test
    public void testDeserializeKey_UUID() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("4e4b7d61-58e9-438d-b21c-0c454efbdb9e", ctxt);
        Assert.assertEquals(key, UUID.fromString("4e4b7d61-58e9-438d-b21c-0c454efbdb9e"));
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("http://example.com", ctxt);
        Assert.assertEquals(key, new URI("http://example.com"));
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("http://example.com", ctxt);
        Assert.assertEquals(key, new URL("http://example.com"));
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("java.lang.Integer", ctxt);
        Assert.assertEquals(key, Integer.class);
    }

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        enum TestEnum {
            VALUE1, VALUE2, VALUE3
        }
        
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("VALUE2", ctxt);
        Assert.assertEquals(key, TestEnum.VALUE2);
    }

    @Test
    public void testDeserializeKey_UnknownEnumValue() throws Exception {
        enum TestEnum {
            VALUE1, VALUE2, VALUE3
        }
        
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);

        DeserializationContext ctxt = createContext();
        Object key = deserializer.deserializeKey("UNKNOWN_VALUE", ctxt);
        Assert.assertNull(key);
    }

    @Test
    public void testDeserializeKey_UnknownType() throws Exception {
        DeserializationContext ctxt = createContext();

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        Object key = deserializer.deserializeKey("anObject", ctxt);
        Assert.assertNull(key);
    }

    private DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }
}