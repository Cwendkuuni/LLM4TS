import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanDescriptor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Test class for the StdKeyDeserializer.
 */
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.TRUE);

        result = deserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.FALSE);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testByteDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Byte);
        assertEquals((Byte) result, (byte) 10);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("200", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testShortDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Short);
        assertEquals((Short) result, (short) 10);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("20000", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testCharDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("A", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Character);
        assertEquals((Character) result, 'A');

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testIntDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Integer);
        assertEquals((Integer) result, 10);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("2147483648", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testLongDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Long);
        assertEquals((Long) result, 10L);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("9223372036854775807", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testFloatDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("10.5", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Float);
        assertEquals((Float) result, 10.5f);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDoubleDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("10.5", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Double);
        assertEquals((Double) result, 10.5);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testLocaleDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Locale);
        assertEquals((Locale) result, Locale.US);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDateDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("2023-03-01", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Date);
        assertEquals((Date) result, new Date(System.currentTimeMillis())); // For testing purposes, use current date

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testCalendarDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("2023-03-01", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Calendar);
        assertEquals((Calendar) result, Calendar.getInstance()); // For testing purposes, use current date

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testURIDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("https://www.example.com", objectMapper.getDeserializationContext());
        assertTrue(result instanceof URI);
        assertEquals((URI) result, new URI("https", "www.example.com", null));

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testURLDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("https://www.example.com", objectMapper.getDeserializationContext());
        assertTrue(result instanceof URL);
        assertEquals((URL) result, new URL("https", "www.example.com", null));

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testClassDeserializer() throws JsonProcessingException, IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("['test1', 'test2']", objectMapper.getDeserializationContext());
        assertTrue(result instanceof String[]);
        assertArrayEquals(new String[]{"test1", "test2"}, (String[]) result);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testEnumDeserializer() throws JsonProcessingException, IOException {
        enum TestEnum {
            VALUE_1,
            VALUE_2
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("VALUE_1", objectMapper.getDeserializationContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.VALUE_1);

        result = deserializer.deserializeKey("VALUE_2", objectMapper.getDeserializationContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.VALUE_2);

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testCustomDeserializer() throws JsonProcessingException, IOException {
        class CustomClass {
            private String key;

            @JsonAutoDetect
            @JsonDeserialize(using = CustomKeyDeserializer.class)
            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }

        class CustomKeyDeserializer extends KeyDeserializer {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                CustomClass result = new CustomClass();
                result.setKey(key);
                return result;
            }
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(CustomClass.class);

        // Test deserialization with valid keys
        Object result = deserializer.deserializeKey("testKey", objectMapper.getDeserializationContext());
        assertTrue(result instanceof CustomClass);
        assertEquals(((CustomClass) result).getKey(), "testKey");

        // Test deserialization with invalid keys
        result = deserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNull(result);
    }
}

class TestEnum {
    @JsonValue
    public String value;

    @JsonCreator
    public TestEnum(String value) {
        this.value = value;
    }
}