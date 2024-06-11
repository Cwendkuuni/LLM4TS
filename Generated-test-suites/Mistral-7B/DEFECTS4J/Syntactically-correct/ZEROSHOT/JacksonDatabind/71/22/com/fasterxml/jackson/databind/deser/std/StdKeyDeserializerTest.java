package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsonschema.SchemaBuilderFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test file for StdKeyDeserializer class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest
{
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testStringKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey("test", MAPPER.getDeserializationContext());
        assertEquals("test", key);
    }

    @Test
    public void testBooleanKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", MAPPER.getDeserializationContext());
        assertTrue((Boolean) key);
        key = deserializer.deserializeKey("false", MAPPER.getDeserializationContext());
        assertFalse((Boolean) key);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testByteKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        Object key = deserializer.deserializeKey("1", MAPPER.getDeserializationContext());
        assertEquals((byte) 1, key);

        try {
            deserializer.deserializeKey("128", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testShortKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        Object key = deserializer.deserializeKey("1", MAPPER.getDeserializationContext());
        assertEquals((short) 1, key);

        try {
            deserializer.deserializeKey("-128", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCharKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        Object key = deserializer.deserializeKey("a", MAPPER.getDeserializationContext());
        assertEquals('a', key);

        try {
            deserializer.deserializeKey("abcd", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testIntKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        Object key = deserializer.deserializeKey("1", MAPPER.getDeserializationContext());
        assertEquals(1, key);

        try {
            deserializer.deserializeKey("-128", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLongKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        Object key = deserializer.deserializeKey("1", MAPPER.getDeserializationContext());
        assertEquals(1L, key);

        try {
            deserializer.deserializeKey("-128", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testFloatKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        Object key = deserializer.deserializeKey("1.0", MAPPER.getDeserializationContext());
        assertEquals(1.0F, key, 0.001f);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDoubleKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        Object key = deserializer.deserializeKey("1.0", MAPPER.getDeserializationContext());
        assertEquals(1.0, key, 0.001);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        Object key = deserializer.deserializeKey("en_US", MAPPER.getDeserializationContext());
        assertEquals(Locale.US, key);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDateKD() throws IOException
    {
        ObjectWriter writer = MAPPER.writerForMapConfig(MAPPER.getSerializationConfig().withSerializationInclusion(JsonInclude.Include.NON_NULL));
        String json = writer.writeValueAsString(new Date());

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        Object key = deserializer.deserializeKey(json, MAPPER.getDeserializationContext());
        assertEquals(new Date(), key);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCalendarKD() throws IOException
    {
        ObjectWriter writer = MAPPER.writerForMapConfig(MAPPER.getSerializationConfig().withSerializationInclusion(JsonInclude.Include.NON_NULL));
        String json = writer.writeValueAsString(Calendar.getInstance());

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        Object key = deserializer.deserializeKey(json, MAPPER.getDeserializationContext());
        assertEquals(Calendar.getInstance(), key);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testUUIDKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        Object key = deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", MAPPER.getDeserializationContext());
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), key);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testURIKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        Object key = deserializer.deserializeKey("http://www.example.com", MAPPER.getDeserializationContext());
        assertEquals(new URI("http://www.example.com"), key);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testURLKD() throws IOException
    {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        Object key = deserializer.deserializeKey("http://www.example.com", MAPPER.getDeserializationContext());
        assertEquals(new URL("http://www.example.com"), key);

        try {
            deserializer.deserializeKey("invalid", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testClassKD() throws IOException
    {
        StdKeyDeserializer<Class<?>> deserializer = new StdKeyDeserializer<Class<?>>(TypeDescriptor.construct(Class.class), null);

        Object key = deserializer.deserializeKey("java.lang.String", MAPPER.getDeserializationContext());
        assertEquals(String.class, key);

        try {
            deserializer.deserializeKey("InvalidClass", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testEnumKD() throws IOException
    {
        // Test Enum by name
        AnnotatedClass ac = MAPPER.findConcreteType(Locale.class);
        AnnotatedMethod arrayMethod = ac.findMethod("getValues", new Class[0]);
        StdKeyDeserializer deserializer = new EnumKD(new EnumResolver(ac, arrayMethod), arrayMethod);

        Object key = deserializer.deserializeKey("ITALIAN", MAPPER.getDeserializationContext());
        assertEquals(Locale.ITALIAN, key);

        try {
            deserializer.deserializeKey("INVALID", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test Enum by toString
        ac = MAPPER.findConcreteType(Locale.class);
        EnumResolver toStringResolver = EnumResolver.constructUsingToString(ac);
        deserializer = new EnumKD(toStringResolver, null);

        key = deserializer.deserializeKey("ITALIAN_LOCALES", MAPPER.getDeserializationContext());
        assertEquals(Locale.ITALY, key);

        try {
            deserializer.deserializeKey("INVALID_ENUM", MAPPER.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDelegatingKD() throws IOException
    {
        Class<?> clazz = AnnotatedClass.class;
        JsonDeserializer<AnnotatedClass> deserializer = MAPPER.findValueDeserializer(clazz);

        Object key = StdKeyDeserializer.forType(clazz).deserializeKey("TestClass", MAPPER.getDeserializationContext());
        assertEquals(deserializer.deserializeValue(MAPPER.getParser(), MAPPER.getDeserializationContext()), key);
    }

    @Test
    public void testConstructorBasedStringKD() throws IOException
    {
        Constructor<Date> constructor = Date.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        Object key = new StdKeyDeserializer(TypeDescriptor.construct(Date.class), constructor).deserializeKey("2023-03-31T12:34:56.789Z", MAPPER.getDeserializationContext());
        assertEquals(new Date("2023-03-31T12:34:56.789Z"), key);
    }

    @Test
    public void testFactoryMethodBasedStringKD() throws IOException
    {
        Class<? extends Calendar> calendarClass = Calendar.class;
        Method factoryMethod = ClassUtil.findMethod(calendarClass, "getInstance", new Class[0]);

        Object key = new StdKeyDeserializer(TypeDescriptor.construct(calendarClass), factoryMethod).deserializeKey("GREGORIAN", MAPPER.getDeserializationContext());
        assertEquals(Calendar.getInstance(), key);
    }
}