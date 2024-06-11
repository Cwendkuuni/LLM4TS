import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StdKeyDeserializerTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void testBooleanType() throws IOException {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);

        String key1 = "true";
        Object result1 = booleanDeserializer.deserializeKey(key1, new TestContext());
        Assert.assertEquals(Boolean.TRUE, result1);

        String key2 = "false";
        Object result2 = booleanDeserializer.deserializeKey(key2, new TestContext());
        Assert.assertEquals(Boolean.FALSE, result2);

        String key3 = "invalid";
        Object result3 = booleanDeserializer.deserializeKey(key3, new TestContext());
        Assert.assertNull(result3);
    }

    @Test
    public void testByteType() throws IOException {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);

        String key1 = "0";
        Object result1 = byteDeserializer.deserializeKey(key1, new TestContext());
        Assert.assertEquals((byte) 0, result1);

        String key2 = "127";
        Object result2 = byteDeserializer.deserializeKey(key2, new TestContext());
        Assert.assertEquals((byte) 127, result2);

        String key3 = "128";
        Object result3 = byteDeserializer.deserializeKey(key3, new TestContext());
        Assert.assertNull(result3);

        String key4 = "256";
        Object result4 = byteDeserializer.deserializeKey(key4, new TestContext());
        Assert.assertNull(result4);
    }

    // ... test other types (Short, Char, Int, Long, Float, Double, Locale, Date, Calendar, UUID, URI, URL, Class)

    @Test
    public void testExceptionHandling() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        String key = "InvalidKey";
        Object result = deserializer.deserializeKey(key, new TestContext());
        Assert.assertNull(result);

        String weirdKey = "not a valid representation";
        Object resultWeird = deserializer.deserializeKey(weirdKey, new TestContext());
        Assert.assertEquals("not a valid representation", resultWeird);
    }

    @Test
    public void testHandleWeirdKey() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        String key = "InvalidKey";
        String expectedMessage = "not a valid representation: InvalidKey";
        Object result = deserializer.deserializeKey(key, new TestContext());
        Assert.assertNull(result);

        String contextClass = deserializer.getClass().getName();
        Assert.assertEquals(expectedMessage, deserializer.handleWeirdKey(String.class, key, "not a valid representation: %s").getMessage());
        Assert.assertEquals(contextClass, deserializer.getClass().getName());
    }

    @Test
    public void testHandleWeirdKeyWithCustomMessage() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        String key = "InvalidKey";
        String customMessage = "Invalid key: %s";
        Object result = deserializer.deserializeKey(key, new TestContextWithCustomMessage(customMessage));
        Assert.assertNull(result);

        String contextClass = deserializer.getClass().getName();
        String expectedMessage = String.format(customMessage, key);
        Assert.assertEquals(expectedMessage, deserializer.handleWeirdKey(String.class, key, "not a valid representation: %s").getMessage());
        Assert.assertEquals(contextClass, deserializer.getClass().getName());
    }

    // ... test edge cases and other scenarios

    private static class TestContext extends DeserializationContext {
        @Override
        public JavaType getTypeFactory() {
            return TypeFactory.defaultInstance();
        }

        @Override
        public Object findValueInMap(Map<String, Object> map, String key) {
            return map.getOrDefault(key, null);
        }

        // ... you may add other methods you need
    }

    // alternative implementation of DeserializationContext for testing
    private static class TestContextWithCustomMessage extends TestContext {
        private final String customMessage;

        public TestContextWithCustomMessage(String customMessage) {
            this.customMessage = customMessage;
        }

        @Override
        public String handleWeirdKey(Class<?> keyType, String key, String defaultMessage) {
            String message = super.handleWeirdKey(keyType, key, defaultMessage).getMessage();
            return String.format(customMessage, key);
        }
    }
}