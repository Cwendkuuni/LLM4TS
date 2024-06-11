import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBooleanKeyDeserialization() throws IOException {
        String key = "true";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testByteKeyDeserialization() throws IOException {
        String key = "127";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Byte);
        Assert.assertEquals((byte) 127, result);

        key = "-128";
        result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Byte);
        Assert.assertEquals((byte) -128, result);

        key = "256";
        result = deserializer.deserializeKey(key, null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof IllegalArgumentException);
    }

    @Test
    public void testShortKeyDeserialization() throws IOException {
        String key = "32767";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Short);
        Assert.assertEquals((short) 32767, result);

        key = "-32768";
        result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Short);
        Assert.assertEquals((short) -32768, result);

        key = "65536";
        result = deserializer.deserializeKey(key, null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof IllegalArgumentException);
    }

    @Test
    public void testCharKeyDeserialization() throws IOException {
        String key = "A";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Character);
        Assert.assertEquals('A', result);

        key = "ö";
        result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Character);
        Assert.assertEquals('\u00D6', result.byteValue());
    }

    @Test
    public void testIntKeyDeserialization() throws IOException {
        String key = "123456";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Integer);
        Assert.assertEquals(123456, result);
    }

    @Test
    public void testLongKeyDeserialization() throws IOException {
        String key = "1234567890123456";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(1234567890123456L, result);
    }

    @Test
    public void testFloatKeyDeserialization() throws IOException {
        String key = "3.14159";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Float);
        Assert.assertEquals(3.14159f, result);
    }

    @Test
    public void testDoubleKeyDeserialization() throws IOException {
        String key = "3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555964233731281609631859574466091737313793105118721454430466421125130493620414133231130331010336";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555964233731281609631859574466091737313793105118721454430466421125130493620414133231130331010336d, result);
    }

    @Test
    public void testLocaleKeyDeserialization() throws IOException {
        String key = "en_US";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Locale);
        Assert.assertEquals(Locale.US, result);

        key = "de_DE";
        result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Locale);
        Assert.assertEquals(Locale.GERMANY, result);
    }

    @Test
    public void testDateKeyDeserialization() throws IOException {
        String key = "2022-03-01";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Date);
        Assert.assertEquals("Tue Mar 01 00:00:00 CET 2022", result.toString());
    }

    @Test
    public void testCalendarKeyDeserialization() throws IOException {
        String key = "2022-03-01";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Calendar);
        Assert.assertEquals(2022, ((Calendar) result).get(Calendar.YEAR));
        Assert.assertEquals(3, ((Calendar) result).get(Calendar.MONTH) + 1);
        Assert.assertEquals(1, ((Calendar) result).get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUriKeyDeserialization() throws IOException {
        String key = "http://example.com";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof URI);
        Assert.assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testUrlKeyDeserialization() throws IOException {
        String key = "http://example.com";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testClassKeyDeserialization() throws IOException {
        String key = "java.lang.String";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Class);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testEnumKeyDeserialization() throws IOException {
        String key = "RED";
        EnumResolver resolver = mapper.getFactory().getAnnotationIntrospector().findValueAnnotation(StdKeyDeserializer.class, null);
        EnumResolver enumResolver = resolver != null ? resolver.getValue() : null;
        AnnotatedMethod factoryMethod = enumResolver != null ? enumResolver.findMethodAnnotation(JsonDeserializer.Method.class, StdKeyDeserializer.class, "createEnum") : null;
        Class<?> enumClass = null;
        if (factoryMethod != null) {
            enumClass = factoryMethod.getParameterType(0).getType();
        }
        StdKeyDeserializer deserializer = new StdKeyDeserializer(enumResolver, factoryMethod != null ? factoryMethod : null);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertTrue(result instanceof Enum);
        Assert.assertEquals(Enum.RED, result);
    }

    @Test
    public void testUnknownEnumKeyDeserialization() throws IOException {
        String key = "UNKNOWN_ENUM";
        EnumResolver resolver = mapper.getFactory().getAnnotationIntrospector().findValueAnnotation(StdKeyDeserializer.class, null);
        EnumResolver enumResolver = resolver != null ? resolver.getValue() : null;
        StdKeyDeserializer deserializer = new StdKeyDeserializer(enumResolver, null);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertNull(result);
    }

    @Test
    public void testNullKeyDeserialization() throws IOException {
        String key = null;
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertNull(result);
    }

    @Test
    public void testInvalidKeyDeserialization() throws IOException {
        String key = "invalidKey";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        try {
            deserializer.deserializeKey(key, null);
            Assert.fail("Should throw an exception");
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testCustomKeyDeserializer() throws IOException {
        String key = "customKey";
        JsonDeserializer<String> customDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        StdKeyDeserializer deserializer = new StdKeyDeserializer(null, customDeserializer);
        Object result = deserializer.deserializeKey(key, null);
        Assert.assertEquals("customKey", result);
    }
}