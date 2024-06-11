package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest extends TestBase {

    @Test
    public void testForTypeInt() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
        assertEquals(Integer.class, kd.getKeyClass());
        assertEquals(Integer.class, kd.deserializeKey("123", context()));
        assertEquals(Integer.class, kd.deserializeKey("127", context()));
        assertEquals(Integer.class, kd.deserializeKey("255", context()));
        try {
            kd.deserializeKey("256", context());
            fail();
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testForTypeBoolean() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.class, kd.getKeyClass());
        assertEquals(Boolean.TRUE, kd.deserializeKey("true", context()));
        assertEquals(Boolean.FALSE, kd.deserializeKey("false", context()));
        try {
            kd.deserializeKey("invalid", context());
            fail();
        } catch (IOException e) {
            assertEquals("not a valid representation, problem: (boolean) false", e.getMessage());
        }
    }

    @Test
    public void testForTypeURI() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
        assertEquals(URI.class, kd.getKeyClass());
        assertEquals(new URI("http://example.com/"), kd.deserializeKey("http://example.com/", context()));
        try {
            kd.deserializeKey("http://example.com/ invalid", context());
            fail();
        } catch (IOException e) {
            assertEquals("problem: Invalid URI: The scheme must not be empty", e.getMessage());
        }
    }

    @Test
    public void testForTypeURL() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
        assertEquals(URL.class, kd.getKeyClass());
        assertEquals(new URL("http://example.com/"), kd.deserializeKey("http://example.com/", context()));
        try {
            kd.deserializeKey("http://example.com/ invalid", context());
            fail();
        } catch (IOException e) {
            assertEquals("problem: Invalid URL malformed", e.getMessage());
        }
    }

    @Test
    public void testForTypeLocale() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.class, kd.getKeyClass());
        assertEquals(Locale.US, kd.deserializeKey("en_US", context()));
        assertEquals(Locale.US, kd.deserializeKey("US", context()));
        try {
            kd.deserializeKey("invalid", context());
            fail();
        } catch (IOException e) {
            assertEquals("unable to parse key as locale", e.getMessage());
        }
    }

    @Test
    public void testForTypeEnum() throws Exception {
        // Test with simple Enum
        EnumSet<MyEnum> enums = EnumSet.allOf(MyEnum.class);
        KeyDeserializer kd = StdKeyDeserializer.forType(MyEnum.class);
        assertEquals(MyEnum.class, kd.getKeyClass());
        for (MyEnum e : enums) {
            assertEquals(e, kd.deserializeKey(e.name(), context()));
        }
        try {
            kd.deserializeKey("invalid", context());
            fail();
        } catch (IOException e) {
            assertEquals("not one of values excepted for Enum class: [MY_ENUM_1, MY_ENUM_2, MY_ENUM_3]", e.getMessage());
        }

        // Test with Enum with simple constructor
        EnumSet<MyEnumWithConstructor> enums2 = EnumSet.allOf(MyEnumWithConstructor.class);
        KeyDeserializer kd2 = new StdKeyDeserializer.StringCtorKeyDeserializer(MyEnumWithConstructor.class.getConstructor(String.class));
        assertEquals(MyEnumWithConstructor.class, kd2.getKeyClass());
        for (MyEnumWithConstructor e : enums2) {
            assertEquals(e, kd2.deserializeKey(e.name(), context()));
        }

        // Test with Enum with static factory method
        EnumSet<MyEnumWithFactory> enums3 = EnumSet.allOf(MyEnumWithFactory.class);
        KeyDeserializer kd3 = new StdKeyDeserializer.StringFactoryKeyDeserializer(MyEnumWithFactory.class.getMethod("valueOf", String.class));
        assertEquals(MyEnumWithFactory.class, kd3.getKeyClass());
        for (MyEnumWithFactory e : enums3) {
            assertEquals(e, kd3.deserializeKey(e.name(), context()));
        }
    }

    @Test
    public void testDeserializeKeyNull() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(String.class);
        assertNull(kd.deserializeKey(null, context()));
    }

    @Test
    public void testDeserializeKeyInvalid() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
        try {
            kd.deserializeKey("invalid", context());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKeyUnknownEnumValue() throws Exception {
        MyEnum unknown = new MyEnum("Unknown");
        KeyDeserializer kd = StdKeyDeserializer.forType(MyEnum.class);
        try {
            kd.deserializeKey("Unknown", context());
            fail();
        } catch (IOException e) {
            // expected
        }
        assertEquals(MyEnum.class, kd.getKeyClass());
    }

    @Test
    public void testDeserializeKeyUnknownEnumValueAsNull() throws Exception {
        MyEnum unknown = new MyEnum("Unknown");
        KeyDeserializer kd = StdKeyDeserializer.forType(MyEnum.class);
        Object result = kd.deserializeKey("Unknown", context());
        assertTrue(result instanceof MyEnum);
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyEnumWithToStringResolver() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(MyEnumWithToString.class);
        assertEquals(MyEnumWithToString.class, kd.getKeyClass());
        assertEquals(MyEnumWithToString.VALUE_1, kd.deserializeKey("VALUE_1", context()));
    }

    private static class MyEnum {
        @JsonValue
        public String name() {
            return name;
        }

        String name;

        MyEnum(String name) {
            this.name = name;
        }
    }

    private static class MyEnumWithConstructor {
        @JsonValue
        public String toString() {
            return name;
        }

        String name;

        MyEnumWithConstructor(String name) {
            this.name = name;
        }
    }

    private static class MyEnumWithFactory {
        @JsonValue
        public static MyEnumWithFactory VALUE_1 = new MyEnumWithFactory("VALUE_1");
        @JsonValue
        public static MyEnumWithFactory VALUE_2 = new MyEnumWithFactory("VALUE_2");
        @JsonValue
        public static MyEnumWithFactory VALUE_3 = new MyEnumWithFactory("VALUE_3");

        String name;

        MyEnumWithFactory(String name) {
            this.name = name;
        }

        public static MyEnumWithFactory valueOf(String name) {
            if ("VALUE_1".equals(name)) {
                return VALUE_1;
            }
            if ("VALUE_2".equals(name)) {
                return VALUE_2;
            }
            if ("VALUE_3".equals(name)) {
                return VALUE_3;
            }
            throw new IllegalArgumentException("Unknown value: " + name);
        }
    }
}