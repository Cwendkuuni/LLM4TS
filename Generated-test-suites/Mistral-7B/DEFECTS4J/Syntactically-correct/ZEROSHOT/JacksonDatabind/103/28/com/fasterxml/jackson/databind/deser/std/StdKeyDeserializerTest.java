import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.EnumResolver;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private DeserializationContext deserializationContext;
    private AnnotationIntrospector annotationIntrospector;
    private EnumResolver enumResolver;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        deserializationContext = Mockito.mock(DeserializationContext.class);
        annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        enumResolver = Mockito.mock(EnumResolver.class);
        objectMapper.setAnnotationIntrospector(annotationIntrospector);
        objectMapper.setEnumResolver(enumResolver);
    }

    @Test
    public void testDeserializeKey_boolean() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("true", deserializationContext);
        assertEquals(Boolean.TRUE, key);

        key = keyDeserializer.deserializeKey("false", deserializationContext);
        assertEquals(Boolean.FALSE, key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_byte() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = keyDeserializer.deserializeKey("10", deserializationContext);
        assertEquals((byte) 10, key);

        key = keyDeserializer.deserializeKey("127", deserializationContext);
        assertEquals((byte) 127, key);

        key = keyDeserializer.deserializeKey("-128", deserializationContext);
        assertEquals((byte) -128, key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_short() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = keyDeserializer.deserializeKey("100", deserializationContext);
        assertEquals((short) 100, key);

        key = keyDeserializer.deserializeKey("32767", deserializationContext);
        assertEquals((short) 32767, key);

        key = keyDeserializer.deserializeKey("-32768", deserializationContext);
        assertEquals((short) -32768, key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_char() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = keyDeserializer.deserializeKey("A", deserializationContext);
        assertEquals('A', key);

        key = keyDeserializer.deserializeKey("Z", deserializationContext);
        assertEquals('Z', key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_int() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = keyDeserializer.deserializeKey("1000", deserializationContext);
        assertEquals(1000, key);

        key = keyDeserializer.deserializeKey("Integer.MAX_VALUE", deserializationContext);
        assertEquals(Integer.MAX_VALUE, key);

        key = keyDeserializer.deserializeKey("-1000", deserializationContext);
        assertEquals(-1000, key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_long() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = keyDeserializer.deserializeKey("1000000", deserializationContext);
        assertEquals(1000000L, key);

        key = keyDeserializer.deserializeKey("Long.MAX_VALUE", deserializationContext);
        assertEquals(Long.MAX_VALUE, key);

        key = keyDeserializer.deserializeKey("-1000000", deserializationContext);
        assertEquals(-1000000L, key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_float() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = keyDeserializer.deserializeKey("1.0", deserializationContext);
        assertEquals(1.0f, key);

        key = keyDeserializer.deserializeKey("3.14159265358979323846", deserializationContext);
        assertEquals(3.14159265358979323846f, key, 0.000001f);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_double() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = keyDeserializer.deserializeKey("1.0", deserializationContext);
        assertEquals(1.0, key, 0.0);

        key = keyDeserializer.deserializeKey("3.14159265358979323846", deserializationContext);
        assertEquals(3.14159265358979323846, key, 0.0000001);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_locale() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = keyDeserializer.deserializeKey("en_US", deserializationContext);
        assertEquals(Locale.US, key);

        key = keyDeserializer.deserializeKey("fr_FR", deserializationContext);
        assertEquals(Locale.FRANCE, key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_date() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object key = keyDeserializer.deserializeKey("2023-03-27", deserializationContext);
        assertNotNull(key);
        assertEquals("Tue Mar 28 00:00:00 CST 2023", key.toString());

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_calendar() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = keyDeserializer.deserializeKey("2023-03-27", deserializationContext);
        assertNotNull(key);

        Calendar calendar = (Calendar) key;
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(3, calendar.get(Calendar.MONTH) + 1);
        assertEquals(27, calendar.get(Calendar.DAY_OF_MONTH));

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_uri() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = keyDeserializer.deserializeKey("http://example.com", deserializationContext);
        assertEquals("http://example.com", key.toString());

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_url() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = keyDeserializer.deserializeKey("http://example.com", deserializationContext);
        assertEquals("http://example.com", key.toString());

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_class() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        Object key = keyDeserializer.deserializeKey("java.lang.String[]", deserializationContext);
        assertArrayEquals(new String[]{"java.lang.String"}, (Object[]) key);

        keyDeserializer = StdKeyDeserializer.forType(MyClass.class);
        MyClass myClass = new MyClass();
        JavaType javaType = objectMapper.getTypeFactory().constructType(MyClass.class);
        BeanPropertyWriter writer = objectMapper.writerFor(javaType).getSerializerProvider().findPropertyWriter("name", MyClass.class);
        writer.setValue("Test");
        Object keyObject = objectMapper.writeValueAsString(myClass);
        Object key = keyDeserializer.deserializeKey(keyObject, deserializationContext);
        assertEquals(myClass, key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @Test
    public void testDeserializeKey_byteArray() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte[].class);
        Object key = keyDeserializer.deserializeKey("dGVzdA==", deserializationContext);
        assertArrayEquals(new byte[]{68, 101, 118, 101, 114}, (Object[]) key);

        key = keyDeserializer.deserializeKey("invalid", deserializationContext);
        assertNull(key);
    }

    @JsonAutoDetect
    @JsonCreator
    static class MyClass {
        @JsonProperty("name")
        private String name;

        public MyClass() {
        }

        public MyClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}