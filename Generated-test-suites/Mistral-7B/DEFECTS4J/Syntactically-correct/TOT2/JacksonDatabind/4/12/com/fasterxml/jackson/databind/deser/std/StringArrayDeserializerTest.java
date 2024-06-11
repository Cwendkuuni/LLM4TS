import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        // Test for method 1: public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[]";
        JsonParser jp = mapper.getFactory().createParser(json);
        String[] result = deserializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleElementArray() throws IOException {
        // Test for method 1: public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[\"test\"]";
        JsonParser jp = mapper.getFactory().createParser(json);
        String[] result = deserializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertArrayEquals(new String[]{"test"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeMissingStartArrayToken() throws IOException {
        // Test for method 1: public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "{\"test\":\"value\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer.deserialize(jp, new DeserializationContext(null, null, null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullElement() throws IOException {
        // Test for method 1: public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[null]";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer.deserialize(jp, new DeserializationContext(null, null, null));
    }

    @Test
    public void testDeserializeMultipleElementsArray() throws IOException {
        // Test for method 1: public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String json = "[\"test1\",\"test2\"]";
        JsonParser jp = mapper.getFactory().createParser(json);
        String[] result = deserializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test
    public void testDeserializeEmptyArrayWithCustomDeserializer() throws IOException {
        // Test for method 2: protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        String json = "[]";
        JsonParser jp = mapper.getFactory().createParser(json);
        String[] result = deserializer._deserializeCustom(jp, new DeserializationContext(null, null, null));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleElementArrayWithCustomDeserializer() throws IOException {
        // Test for method 2: protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        String json = "[\"test\"]";
        JsonParser jp = mapper.getFactory().createParser(json);
        String[] result = deserializer._deserializeCustom(jp, new DeserializationContext(null, null, null));
        assertArrayEquals(new String[]{"test"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeMissingStartArrayTokenWithCustomDeserializer() throws IOException {
        // Test for method 2: protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        String json = "{\"test\":\"value\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer._deserializeCustom(jp, new DeserializationContext(null, null, null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullElementWithCustomDeserializer() throws IOException {
        // Test for method 2: protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        String json = "[null]";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer._deserializeCustom(jp, new DeserializationContext(null, null, null));
    }

    @Test
    public void testDeserializeMultipleElementsArrayWithCustomDeserializer() throws IOException {
        // Test for method 2: protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        String json = "[\"test1\",\"test2\"]";
        JsonParser jp = mapper.getFactory().createParser(json);
        String[] result = deserializer._deserializeCustom(jp, new DeserializationContext(null, null, null));
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }
}