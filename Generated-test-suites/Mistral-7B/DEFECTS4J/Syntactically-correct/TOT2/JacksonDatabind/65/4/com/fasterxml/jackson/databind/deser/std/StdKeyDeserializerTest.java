import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("5", new TestContext());
        assertEquals((byte) 5, key);

        key = deserializer.deserializeKey("-127", new TestContext());
        assertEquals((byte) -127, key);

        key = deserializer.deserializeKey("128", new TestContext());
        assertNull(key);
    }

    // Add tests for other data types (short, char, int, long, float, double, locale, date, calendar, UUID, URI, URL, Class)

    @Test
    public void testUnknownType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        Object key = deserializer.deserializeKey("customType", new TestContext());
        assertEquals("customType", key);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        deserializer.deserializeKey(null, new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        deserializer.deserializeKey("{invalid}", new TestContext());
    }

    // Add tests for other edge cases and exception handling scenarios (empty string, negative numbers, overflow, non-numeric strings)
}

class TestContext extends DeserializationContext {
    // Implement methods required by DeserializationContext interface
}