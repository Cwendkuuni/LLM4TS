package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest {

    private static final String NAME = "testKey";
    private static final int INT_VALUE = 10;
    private static final long LONG_VALUE = 20L;
    private static final boolean BOOLEAN_VALUE = true;
    private static final char CHAR_VALUE = 'A';
    private static final float FLOAT_VALUE = 3.14F;
    private static final double DOUBLE_VALUE = 3.14;
    private static final Locale LOCALE_VALUE = Locale.US;
    private static final Currency CURRENCY_VALUE = Currency.getInstance("USD");
    private static final Date DATE_VALUE = new Date();
    private static final Calendar CALENDAR_VALUE = Calendar.getInstance();
    private static final URI URI_VALUE;
    private static final URL URL_VALUE;
    private static final Enum<?> ENUM_VALUE;
    private static final UUID UUID_VALUE;

    static {
        try {
            URI_VALUE = new URI("http://example.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            URL_VALUE = new URL("http://example.com");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        ENUM_VALUE = EnumSet.allOf(MyEnum.class).stream().filter(e -> e.getName().equals("TEST")).findFirst().orElse(null);
        UUID_VALUE = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    }

    @InjectMocks
    private StdKeyDeserializer deserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeserializeInt() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(String.valueOf(INT_VALUE), new DeserializationContext(mapper, null));
        assertEquals(INT_VALUE, result);
    }

    @Test
    public void testDeserializeLong() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(String.valueOf(LONG_VALUE), new DeserializationContext(mapper, null));
        assertEquals(LONG_VALUE, result);
    }

    @Test
    public void testDeserializeBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey("true", new DeserializationContext(mapper, null));
        assertTrue(result instanceof Boolean);
        assertEquals(BOOLEAN_VALUE, ((Boolean) result).booleanValue());

        result = deserializer.deserializeKey("false", new DeserializationContext(mapper, null));
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testDeserializeChar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(String.valueOf(CHAR_VALUE), new DeserializationContext(mapper, null));
        assertEquals(CHAR_VALUE, result);
    }

    @Test
    public void testDeserializeFloat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(String.valueOf(FLOAT_VALUE), new DeserializationContext(mapper, null));
        assertEquals(FLOAT_VALUE, result, 0.001F);
    }

    @Test
    public void testDeserializeDouble() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(String.valueOf(DOUBLE_VALUE), new DeserializationContext(mapper, null));
        assertEquals(DOUBLE_VALUE, result, 0.001);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(LOCALE_VALUE.toString(), new DeserializationContext(mapper, null));
        assertEquals(LOCALE_VALUE, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(CURRENCY_VALUE.toString(), new DeserializationContext(mapper, null));
        assertEquals(CURRENCY_VALUE, result);
    }

    @Test
    public void testDeserializeDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(DATE_VALUE.toString(), new DeserializationContext(mapper, null));
        assertEquals(DATE_VALUE, result);
    }

    @Test
    public void testDeserializeCalendar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(CALENDAR_VALUE.toString(), new DeserializationContext(mapper, null));
        assertEquals(CALENDAR_VALUE, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        deserializer.deserializeKey(URI_VALUE.toString(), new DeserializationContext(mapper, null));
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        deserializer.deserializeKey(URL_VALUE.toString(), new DeserializationContext(mapper, null));
        fail("Expected IOException");
    }

    @Test
    public void testDeserializeEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(ENUM_VALUE.name(), new DeserializationContext(mapper, null));
        assertEquals(ENUM_VALUE, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnknownEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String unknownEnumName = "UnknownEnumValue";
        deserializer.deserializeKey(unknownEnumName, new DeserializationContext(mapper, null));
        fail("Expected IOException");
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> clazz = MyClass.class;
        Object result = deserializer.deserializeKey(clazz.getName(), new DeserializationContext(mapper, null));
        assertEquals(clazz, result.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnknownClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String unknownClassName = "UnknownClass";
        deserializer.deserializeKey(unknownClassName, new DeserializationContext(mapper, null));
        fail("Expected IOException");
    }

    @Test
    public void testDeserializeUuid() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey(UUID_VALUE.toString(), new DeserializationContext(mapper, null));
        assertEquals(UUID_VALUE, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnknownUuid() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String unknownUuidString = "111e4567-e89b-12d3-a456-426614175000";
        deserializer.deserializeKey(unknownUuidString, new DeserializationContext(mapper, null));
        fail("Expected IOException");
    }

    @Test
    public void testDeserializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey("testString", new DeserializationContext(mapper, null));
        assertEquals("testString", result);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = deserializer.deserializeKey("{\"name\":\"testObject\"}", new DeserializationContext(mapper, null));
        assertNotNull(result);
    }
}