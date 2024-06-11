package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    public StdKeyDeserializerTest() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("true", ctxt);
        Assert.assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", ctxt);
        Assert.assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("127", ctxt);
        Assert.assertEquals((byte) 127, result);

        result = keyDeserializer.deserializeKey("-128", ctxt);
        Assert.assertEquals((byte) -128, result);

        result = keyDeserializer.deserializeKey("256", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testShortKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("32767", ctxt);
        Assert.assertEquals((short) 32767, result);

        result = keyDeserializer.deserializeKey("-32768", ctxt);
        Assert.assertEquals((short) -32768, result);

        result = keyDeserializer.deserializeKey("65536", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testCharKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("A", ctxt);
        Assert.assertEquals('A', result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testIntKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("2147483647", ctxt);
        Assert.assertEquals(Integer.MAX_VALUE, result);

        result = keyDeserializer.deserializeKey("-2147483648", ctxt);
        Assert.assertEquals(Integer.MIN_VALUE, result);

        result = keyDeserializer.deserializeKey("4294967296", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testLongKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("9223372036854775807", ctxt);
        Assert.assertEquals(Long.MAX_VALUE, result);

        result = keyDeserializer.deserializeKey("-9223372036854775808", ctxt);
        Assert.assertEquals(Long.MIN_VALUE, result);

        result = keyDeserializer.deserializeKey("18446744073709551616", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testFloatKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("1.7F", ctxt);
        Assert.assertEquals(Float.valueOf((float) 1.7f), result);

        result = keyDeserializer.deserializeKey("NaN", ctxt);
        Assert.assertEquals(Float.NaN, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDoubleKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("1.7", ctxt);
        Assert.assertEquals(Double.valueOf(1.7), result);

        result = keyDeserializer.deserializeKey("NaN", ctxt);
        Assert.assertEquals(Double.NaN, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Locale expectedLocale = Locale.US;
        Object result = keyDeserializer.deserializeKey("en_US", ctxt);
        Assert.assertEquals(expectedLocale, result);

        expectedLocale = Locale.CHINESE;
        result = keyDeserializer.deserializeKey("zh_CN", ctxt);
        Assert.assertEquals(expectedLocale, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Date expectedDate = new Date(1620523526000L);
        Object result = keyDeserializer.deserializeKey("2021-08-16T16:25:26Z", ctxt);
        Assert.assertEquals(expectedDate, result);

        expectedDate = new Date(1616131200000L);
        result = keyDeserializer.deserializeKey("2021-03-20T00:00:00Z", ctxt);
        Assert.assertEquals(expectedDate, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.set(Calendar.YEAR, 2021);
        expectedCalendar.set(Calendar.MONTH, Calendar.AUGUST);
        expectedCalendar.set(Calendar.DAY_OF_MONTH, 16);
        expectedCalendar.set(Calendar.HOUR_OF_DAY, 16);
        expectedCalendar.set(Calendar.MINUTE, 25);
        expectedCalendar.set(Calendar.SECOND, 26);
        expectedCalendar.set(Calendar.MILLISECOND, 0);
        Object result = keyDeserializer.deserializeKey("2021-08-16T16:25:26Z", ctxt);
        Assert.assertEquals(expectedCalendar, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testUUIDKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        UUID expectedUuid = new UUID(1, 2);
        Object result = keyDeserializer.deserializeKey("1-2", ctxt);
        Assert.assertEquals(expectedUuid, result);

        expectedUuid = new UUID(3, 4);
        result = keyDeserializer.deserializeKey("3-4", ctxt);
        Assert.assertEquals(expectedUuid, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testURIPrefixKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        URI expectedUri = new URI("http://example.com");
        Object result = keyDeserializer.deserializeKey("http://example.com", ctxt);
        Assert.assertEquals(expectedUri, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testURLKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        URL expectedUrl = new URL("http://example.com");
        Object result = keyDeserializer.deserializeKey("http://example.com", ctxt);
        Assert.assertEquals(expectedUrl, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testEnumKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(new MyEnum[] { MyEnum.VALUE1, MyEnum.VALUE2 });

        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        AnnotatedClass annotatedClass = introspector.findAnnotationInfo(MyEnum.class, null);
        AnnotatedMethod annotatedMethod = annotatedClass.findMethod("values", null);

        Object result = keyDeserializer.deserializeKey("VALUE1", ctxt);
        Assert.assertEquals(MyEnum.VALUE1, result);

        result = keyDeserializer.deserializeKey("VALUE2", ctxt);
        Assert.assertEquals(MyEnum.VALUE2, result);

        result = keyDeserializer.deserializeKey("INVALID", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testClassKeyDeserialization() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyClass.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("com.example.MyClass", ctxt);
        Assert.assertInstanceOf(MyClass.class, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testMissingKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MissingKeyDeserializerTestClass.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Assert.assertNull(keyDeserializer.deserializeKey("key", ctxt));
    }

    @Test
    public void testCustomKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = new CustomKeyDeserializer();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = keyDeserializer.deserializeKey("CustomKey", ctxt);
        Assert.assertEquals("CustomKeyValue", result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    static class MyEnum {
        public static final MyEnum VALUE1 = new MyEnum("VALUE1");
        public static final MyEnum VALUE2 = new MyEnum("VALUE2");

        private final String value;

        MyEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    static class MyClass {
    }

    static class CustomKeyDeserializer extends StdKeyDeserializer {
        CustomKeyDeserializer() {
            super(-1, String.class);
        }

        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            if (key.equals("CustomKey")) {
                return "CustomKeyValue";
            } else {
                return super.deserializeKey(key, ctxt);
            }
        }
    }

    static class MissingKeyDeserializerTestClass {
    }
}