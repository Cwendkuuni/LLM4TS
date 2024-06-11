import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        KeyDeserializer keyDeserializer = mapper.getDeserializationConfig().findKeyDeserializer(Boolean.class);
        assertEquals(serializer, keyDeserializer);

        JsonParser parser = mapper.getFactory().createParser("true");
        Object key = serializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, key);

        parser = mapper.getFactory().createParser("false");
        key = serializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, key);

        parser = mapper.getFactory().createParser("invalid");
        key = serializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        KeyDeserializer keyDeserializer = mapper.getDeserializationConfig().findKeyDeserializer(Byte.class);
        assertEquals(serializer, keyDeserializer);

        JsonParser parser = mapper.getFactory().createParser("10");
        Object key = serializer.deserializeKey("10", new TestContext());
        assertEquals((byte) 10, key);

        parser = mapper.getFactory().createParser("256");
        try {
            key = serializer.deserializeKey("256", new TestContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("overflow, value cannot be represented as 8-bit value", e.getMessage());
        }

        parser = mapper.getFactory().createParser("invalid");
        key = serializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    // Add more test methods for other types (short, char, int, long, float, double, Locale, Date, Calendar, UUID, URI, URL, Class, byte[])

    @Test
    public void testUnknownEnumKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EnumResolver enumResolver = new EnumResolver(MyEnum.class);
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, MyEnum.class);
        serializer._deser = enumResolver;
        KeyDeserializer keyDeserializer = mapper.getDeserializationConfig().findKeyDeserializer(MyEnum.class);
        assertEquals(serializer, keyDeserializer);

        JsonParser parser = mapper.getFactory().createParser("INVALID_ENUM_VALUE");
        try {
            Object key = serializer.deserializeKey("INVALID_ENUM_VALUE", new TestContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("not one of values excepted for Enum class: [INVALID_ENUM_VALUE]", e.getMessage());
        }

        parser = mapper.getFactory().createParser("VALID_ENUM_VALUE");
        Object enumValue = serializer.deserializeKey("VALID_ENUM_VALUE", new TestContext());
        assertEquals(MyEnum.VALID_ENUM_VALUE, enumValue);
    }

    @Test
    public void testMalformedURLKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        KeyDeserializer keyDeserializer = mapper.getDeserializationConfig().findKeyDeserializer(URL.class);
        assertEquals(serializer, keyDeserializer);

        JsonParser parser = mapper.getFactory().createParser("http://example.com/invalid");
        try {
            Object key = serializer.deserializeKey("http://example.com/invalid", new TestContext());
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // Expected
        }

        parser = mapper.getFactory().createParser("http://example.com");
        Object url = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), url);
    }
}