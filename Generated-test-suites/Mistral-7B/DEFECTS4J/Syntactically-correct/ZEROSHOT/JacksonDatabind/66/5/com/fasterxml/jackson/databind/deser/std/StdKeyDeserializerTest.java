package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest extends TestBase {

    @Test
    public void testBooleanDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertNull(deserializer.deserializeKey("", context));
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", context));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", context));
    }

    @Test
    public void testByteDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 0, deserializer.deserializeKey("0", context));
        assertEquals((byte) 127, deserializer.deserializeKey("127", context));
        assertEquals((byte) -128, deserializer.deserializeKey("-128", context));
        assertNull(deserializer.deserializeKey("128", context));
        assertNull(deserializer.deserializeKey("-256", context));
    }

    @Test
    public void testShortDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 0, deserializer.deserializeKey("0", context));
        assertEquals((short) 127, deserializer.deserializeKey("127", context));
        assertEquals((short) -128, deserializer.deserializeKey("-128", context));
        assertNull(deserializer.deserializeKey("128", context));
        assertNull(deserializer.deserializeKey("-32768", context));
    }

    @Test
    public void testCharDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('A', deserializer.deserializeKey("A", context));
        assertEquals('_', deserializer.deserializeKey("_", context));
        assertNull(deserializer.deserializeKey("", context));
        assertNull(deserializer.deserializeKey("65536", context));
    }

    @Test
    public void testIntDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(0, deserializer.deserializeKey("0", context));
        assertEquals(1, deserializer.deserializeKey("1", context));
        assertEquals(-1, deserializer.deserializeKey("-1", context));
        assertNull(deserializer.deserializeKey("2147483647", context));
        assertNull(deserializer.deserializeKey("-2147483648", context));
    }

    @Test
    public void testLongDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(0L, deserializer.deserializeKey("0", context));
        assertEquals(1L, deserializer.deserializeKey("1", context));
        assertEquals(-1L, deserializer.deserializeKey("-1", context));
        assertEquals(Long.MAX_VALUE, deserializer.deserializeKey("9223372036854775807", context));
        assertNull(deserializer.deserializeKey("-9223372036854775808", context));
    }

    @Test
    public void testFloatDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(0.0f, deserializer.deserializeKey("0", context), 0.0f);
        assertEquals(Float.POSITIVE_INFINITY, deserializer.deserializeKey("Inf", context));
        assertEquals(Float.NEGATIVE_INFINITY, deserializer.deserializeKey("-Inf", context));
        assertEquals(Float.NaN, deserializer.deserializeKey("NaN", context));
        assertNull(deserializer.deserializeKey("1.4E45F", context));
    }

    @Test
    public void testDoubleDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(0.0, deserializer.deserializeKey("0", context), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, deserializer.deserializeKey("Inf", context));
        assertEquals(Double.NEGATIVE_INFINITY, deserializer.deserializeKey("-Inf", context));
        assertEquals(Double.NaN, deserializer.deserializeKey("NaN", context));
        assertNull(deserializer.deserializeKey("1.79769313486231E308", context));
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Locale expected = Locale.US;
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(Locale.class);
        AnnotatedMethod deserializerMethod = annotatedClass.findMethod("fromString", String.class);
        ObjectMock localeMock = mock(Object.class);
        when(localeMock.toString()).thenReturn("en_US");
        doReturn(localeMock).when(deserializer).deserializeKey("en_US", context);
        assertEquals(expected, deserializer.deserializeKey("en_US", context));
        assertNull(deserializer.deserializeKey("", context));
    }

    @Test
    public void testUriDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        URI expected = new URI("http://example.com");
        when(context.parseUri(expected.toASCIIString())).thenReturn(expected);
        assertEquals(expected, deserializer.deserializeKey("http://example.com", context));
        assertNull(deserializer.deserializeKey("", context));
    }

    @Test
    public void testUrlDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        URL expected = new URL("http://example.com");
        when(context.buildUrl(Mockito.anyString(), Mockito.any(URI.class), Mockito.anyBoolean()))
                .thenReturn(expected);
        assertEquals(expected, deserializer.deserializeKey("http://example.com", context));
        assertNull(deserializer.deserializeKey("", context));
    }

    @Test
    public void testEnumDeserialization() throws Exception {
        EnumResolver enumResolver = mock(EnumResolver.class);
        AnnotatedField annotatedField = mapper.findAnnotation(Enum.class, MyEnum.class);
        StdKeyDeserializer deserializer = new EnumKD(enumResolver, annotatedField.getAnnotatedMethod("valueOf", String.class));
        MyEnum expected = MyEnum.VALUE_ONE;
        when(enumResolver.findEnum(Mockito.eq("VALUE_ONE"))).thenReturn(expected);
        assertEquals(expected, deserializer.deserializeKey("VALUE_ONE", context));
        assertNull(deserializer.deserializeKey("", context));
    }

    @Test
    public void testClassDeserialization() throws Exception {
        Class<?> clazz = String[].class;
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(clazz);
        assertEquals(clazz, deserializer.deserializeKey("class [Ljava.lang.String;", context));
        assertNull(deserializer.deserializeKey("", context));
    }

    @Test(expected = IllegalStateException.class)
    public void testUnknownKeyType() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, Object.class);
        deserializer.deserializeKey("", context);
    }

    @Test
    public void testNullKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertNull(deserializer.deserializeKey(null, context));
    }

    @Test
    public void testInvalidKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        String key = "invalid_key";
        assertNull(deserializer.deserializeKey(key, context));
    }

    @Test
    public void testIntDeserializationWithNegativeValue() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(-1, deserializer.deserializeKey("-1", context));
    }

    @Test
    public void testFloatDeserializationWithNaN() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(Float.NaN, deserializer.deserializeKey("NaN", context), Float.NaN);
    }

    @Test
    public void testDoubleDeserializationWithNaN() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(Double.NaN, deserializer.deserializeKey("NaN", context), Double.NaN);
    }

    @Test
    public void testInvalidLongDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertNull(deserializer.deserializeKey("294967296850717283235517", context));
    }

    @Test
    public void testInvalidDoubleDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertNull(deserializer.deserializeKey("1.79769313486231E308", context));
    }
}