import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        JsonParser parser = new JsonFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleElementArray() throws IOException {
        String json = "[\"test\"]";
        JsonParser parser = new JsonFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertArrayEquals(new String[]{"test"}, result);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        String json = "[null]";
        JsonParser parser = new JsonFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[\"test1\"," +
                      "{\"name\": \"test2\", \"value\": \"test3\"}]";
        JsonNode node = objectMapper.readTree(json);
        JsonParser parser = node.asParser();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new MyCustomStringDeserializer());
        String[] result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnexpectedToken() throws IOException {
        String json = "{ \"name\": \"test\" }";
        JsonParser parser = new JsonFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(parser, new TestingDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeNegativeSizeArray() throws IOException {
        String json = "[\"test\"][-1]";
        JsonParser parser = new JsonFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(parser, new TestingDeserializationContext());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "[\"\"]";
        JsonParser parser = new JsonFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeNullArray() throws IOException {
        String json = "null";
        JsonParser parser = new JsonFactory().createParser(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, new TestingDeserializationContext());
        assertNull(result);
    }
}