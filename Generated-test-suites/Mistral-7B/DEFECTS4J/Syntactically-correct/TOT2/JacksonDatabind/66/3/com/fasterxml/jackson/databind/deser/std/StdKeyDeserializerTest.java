import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest extends BaseMapTest {

    @Test
    public void testBooleanDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("true", ctxt);
        assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", ctxt);
        assertEquals(Boolean.FALSE, key);
    }

    @Test
    public void testByteDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("127", ctxt);
        assertEquals((Byte) 127, key);

        try {
            key = deserializer.deserializeKey("-128", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = deserializer.deserializeKey("256", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("32767", ctxt);
        assertEquals((Short) 32767, key);

        try {
            key = deserializer.deserializeKey("-32768", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = deserializer.deserializeKey("65536", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("A", ctxt);
        assertEquals((Character) 'A', key);

        try {
            key = deserializer.deserializeKey("♥", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("42", ctxt);
        assertEquals(Integer.valueOf(42), key);
    }

    @Test
    public void testLongDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("9223372036854775807", ctxt);
        assertEquals(Long.MAX_VALUE, key);

        try {
            key = deserializer.deserializeKey("-9223372036854775808", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDateDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("2022-03-01", ctxt);
        assertEquals(new Date(0), key); // default date is considered equal to 'Date' object with zero time

        try {
            key = deserializer.deserializeKey("2022-03-32", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testEnumDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Color.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("RED", ctxt);
        assertEquals(Color.RED, key);

        try {
            key = deserializer.deserializeKey("GREEN", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            key = deserializer.deserializeKey("BLUE", ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testNullKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey(null, ctxt);
        assertNull(key);
    }

    @Test
    public void testUnknownKeyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = mapper.getConfig().getContext();

        Object key = deserializer.deserializeKey("unknown", ctxt);
        assertEquals("unknown", key);
    }

    // Add test cases for other data types: UUID, URI, URL, Class, Locale, Currency
}