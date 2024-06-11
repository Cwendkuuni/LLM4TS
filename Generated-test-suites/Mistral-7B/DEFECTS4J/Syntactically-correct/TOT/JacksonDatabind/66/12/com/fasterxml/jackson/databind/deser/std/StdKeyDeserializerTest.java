package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void testBooleanType() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("true", context);
        Assert.assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", context);
        Assert.assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        Assert.assertNull(result);
    }

    @Test
    public void testByteType() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("127", context);
        Assert.assertEquals((byte) 127, result);

        result = keyDeserializer.deserializeKey("-128", context);
        Assert.assertEquals((byte) -128, result);

        result = keyDeserializer.deserializeKey("256", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", context);
        Assert.assertNull(result);
    }

    @Test
    public void testShortType() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("32767", context);
        Assert.assertEquals((short) 32767, result);

        result = keyDeserializer.deserializeKey("-32768", context);
        Assert.assertEquals((short) -32768, result);

        result = keyDeserializer.deserializeKey("65536", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("invalid", context);
        Assert.assertNull(result);
    }

    @Test
    public void testCharType() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("A", context);
        Assert.assertEquals('A', result);

        result = keyDeserializer.deserializeKey("invalid", context);
        Assert.assertNull(result);
    }

    // Add other test cases for int, long, float, double, Locale, Date, Calendar, UUID, URI, URL, Class, and Currency types.

    @Test
    public void testNullKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey(null, context);
        Assert.assertNull(result);
    }

    @Test
    public void testEnumType() throws IOException {
        @JsonDeserialize(using = StdKeyDeserializer.EnumKD.class)
        enum MyEnum {
            VALUE_1, VALUE_2, VALUE_3
        }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("VALUE_1", context);
        Assert.assertEquals(MyEnum.VALUE_1, result);

        result = keyDeserializer.deserializeKey("VALUE_4", context);
        Assert.assertNull(result);
    }

    @Test
    public void testStringType() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("test", context);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testClassTypeDeserializer() throws IOException {
        class MyClass {
            @JsonDeserialize(using = StdKeyDeserializer.StringFactoryKeyDeserializer.class)
            private final String name;

            public MyClass(String name) {
                this.name = name;
            }
        }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyClass.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("MyClass", context);
        Assert.assertTrue(result instanceof MyClass);
    }

    @Test
    public void testTypeDeserializer() throws IOException {
        @JsonDeserialize(using = StdKeyDeserializer.DelegateingKD.class)
        class MyClassDeserializer
                extends StdKeyDeserializer {
            public MyClassDeserializer() {
                super(-1, MyClass.class);
            }

            @Override
            public Object deserializeKey(String key, DeserializationContext context)
                    throws IOException {
                Object deserialized = super.deserializeKey(key, context);
                if (deserialized == null) {
                    return deserializeKeyUsingDefault(key, context);
                }
                return deserialized;
            }

            protected Object deserializeKeyUsingDefault(String key, DeserializationContext context)
                    throws IOException {
                ObjectMapper defaultMapper = new ObjectMapper();
                ObjectMapper.DefaultTyping typing = defaultMapper.getDefaultTyping();
                Object deserialized = defaultMapper.readValue(key, typing.findValueType(MyClass.class));
                return deserialized;
            }
        }

        KeyDeserializer keyDeserializer = new MyClassDeserializer();

        DeserializationContext context = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("test", context);
        Assert.assertTrue(result instanceof MyClass);
    }
}