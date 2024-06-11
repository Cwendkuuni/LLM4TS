package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestUtil;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static org.testng.Assert.*;

public class StdKeyDeserializerTest extends TestBase {

    private ObjectMapper mapper;

    @BeforeMethod
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBoolean() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);

        Object key = kd.deserializeKey("true", getContext());
        assertTrue(key instanceof Boolean);
        assertEquals(key, Boolean.TRUE);

        key = kd.deserializeKey("false", getContext());
        assertTrue(key instanceof Boolean);
        assertEquals(key, Boolean.FALSE);
    }

    @Test
    public void testByte() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);

        Object key = kd.deserializeKey("127", getContext());
        assertTrue(key instanceof Byte);
        assertEquals(key, Byte.valueOf((byte) 127));

        try {
            key = kd.deserializeKey("-128", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = kd.deserializeKey("256", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShort() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);

        Object key = kd.deserializeKey("32767", getContext());
        assertTrue(key instanceof Short);
        assertEquals(key, Short.valueOf((short) 32767));

        try {
            key = kd.deserializeKey("-32768", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = kd.deserializeKey("65536", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testChar() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);

        Object key = kd.deserializeKey("'A'", getContext());
        assertTrue(key instanceof Character);
        assertEquals(key, Character.valueOf('A'));

        try {
            key = kd.deserializeKey("'\\u00E9'", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testInt() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);

        Object key = kd.deserializeKey("123456", getContext());
        assertTrue(key instanceof Integer);
        assertEquals(key, Integer.valueOf(123456));

        try {
            key = kd.deserializeKey("-123456", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = kd.deserializeKey("2147483648", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLong() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);

        Object key = kd.deserializeKey("12345678901234567890", getContext());
        assertTrue(key instanceof Long);
        assertEquals(key, Long.valueOf(12345678901234567890L));

        try {
            key = kd.deserializeKey("-12345678901234567890", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = kd.deserializeKey("9223372036854775808", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testFloat() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Float.class);

        Object key = kd.deserializeKey("123.45", getContext());
        assertTrue(key instanceof Float);
        assertEquals(key, Float.valueOf((float) 123.45));

        try {
            key = kd.deserializeKey("-123.45", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = kd.deserializeKey("1.23e100", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDouble() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);

        Object key = kd.deserializeKey("123.45", getContext());
        assertTrue(key instanceof Double);
        assertEquals(key, Double.valueOf(123.45));

        key = kd.deserializeKey("-123.45", getContext());
        assertTrue(key instanceof Double);
        assertEquals(key, Double.valueOf(-123.45));

        try {
            key = kd.deserializeKey("1.23e100", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLocale() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);

        Object key = kd.deserializeKey("en_US", getContext());
        assertTrue(key instanceof Locale);
        assertEquals(key, Locale.US);

        try {
            key = kd.deserializeKey("fr_CA", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDate() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Date.class);

        Object key = kd.deserializeKey("2019-01-01", getContext());
        assertTrue(key instanceof Date);
        assertEquals(key, new Date(0));

        try {
            key = kd.deserializeKey("2019-02-30", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCalendar() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);

        Object key = kd.deserializeKey("2019-01-01", getContext());
        assertTrue(key instanceof Calendar);
        assertSame(key, GregorianCalendar.getInstance(2019, 0, 1));

        try {
            key = kd.deserializeKey("2019-02-30", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testURI() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);

        Object key = kd.deserializeKey("http://foo.bar.com/baz", getContext());
        assertTrue(key instanceof URI);
        assertEquals(key, new URI("http", "foo.bar.com", "/baz"));

        try {
            key = kd.deserializeKey("http://foo.bar.com/baz/quux", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testURL() throws IOException {
        KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);

        Object key = kd.deserializeKey("http://foo.bar.com/baz", getContext());
        assertTrue(key instanceof URL);
        assertEquals(key, new URL("http", "foo.bar.com", "/baz"));

        try {
            key = kd.deserializeKey("http://foo.bar.com/baz/quux", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testEnum() throws IOException {
        final @JsonTypeName("FOO") EnumType enumType = EnumType.class;

        KeyDeserializer kd = StdKeyDeserializer.forType(enumType);

        Object key = kd.deserializeKey("FOO_BAR", getContext());
        assertTrue(key instanceof Enum);
        assertEquals(key, EnumType.FOO_BAR);

        try {
            key = kd.deserializeKey("BAZ_QUX", getContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidEnum() throws IOException {
        final @JsonTypeName("FOO") EnumType enumType = EnumType.class;

        KeyDeserializer kd = StdKeyDeserializer.forType(enumType);

        Object key = kd.deserializeKey("INVALID", getContext());
        fail();
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testUnknownEnumValue() throws IOException {
        final @JsonTypeName("FOO") EnumType enumType = EnumType.class;

        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper.DefaultTyping typings = mapper.getFactory().getDefaultTyping();
        mapper.setDefaultTyping(new TypeDeserializer.Base() {
            @Override
            public Object deserializeTypedFromObject(Object value, DeserializationContext ctxt) throws IOException {
                if (value instanceof Enum) {
                    Enum e = (Enum) value;
                    return EnumType.valueOf(e.name());
                }
                return value;
            }

            @Override
            public TypeDeserializer getTypeDeserializer() {
                return null;
            }
        });

        String json = "{\"k\":\"INVALID\"}";
        MapperTestBuilder mtb = new MapperTestBuilder(mapper);
        mtb.checkMapping(json, EnumType[].class);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testUnknownEnumValueWithNullDefault() throws IOException {
        final @JsonTypeName("FOO") EnumType enumType = EnumType.class;

        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper.DefaultTyping typings = mapper.getFactory().getDefaultTyping();
        mapper.setDefaultTyping(new TypeDeserializer.Base() {
            @Override
            public Object deserializeTypedFromObject(Object value, DeserializationContext ctxt) throws IOException {
                if (value instanceof Enum) {
                    Enum e = (Enum) value;
                    return EnumType.valueOf(e.name());
                }
                return null;
            }

            @Override
            public TypeDeserializer getTypeDeserializer() {
                return null;
            }
        });

        String json = "{\"k\":\"INVALID\"}";
        MapperTestBuilder mtb = new MapperTestBuilder(mapper);
        mtb.checkMapping(json, EnumType[].class, true);
    }

    private static class EnumType {
        @JsonTypeName("FOO_BAR")
        public enum EnumValue1 {
            FOO_BAR;
        }

        @JsonTypeName("BAZ_QUX")
        public enum EnumValue2 {
            BAZ_QUX;
        }

        public static EnumValue1 FOO_BAR = EnumValue1.FOO_BAR;
        public static EnumValue2 BAZ_QUX = EnumValue2.BAZ_QUX;
    }
}