import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[1, 2, 3, 4]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonArray);

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.findValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);

        List<Integer> list = deserializer.deserialize(jsonParser, new TestContext());
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
    }

    @Test
    public void testCollectionDeserializationWithTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[\"apple\", \"banana\", \"orange\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonArray);

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                mapper.findValueDeserializer(String.class),
                mapper.findValueDeserializer(String.class),
                null,
                null,
                null);

        List<String> list = deserializer.deserialize(jsonParser, new TestContext());
        assertEquals(3, list.size());
        assertEquals("apple", list.get(0));
        assertEquals("banana", list.get(1));
        assertEquals("orange", list.get(2));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonArray);

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.findValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);

        deserializer.deserialize(jsonParser, new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationNonArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"name\": \"John\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.findValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);

        deserializer.deserialize(jsonParser, new TestContext());
    }

    @Test
    public void testCollectionDeserializationEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonArray);

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.findValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);

        List<Integer> list = deserializer.deserialize(jsonParser, new TestContext());
        assertEquals(0, list.size());
    }

    @Test
    public void testCollectionDeserializationNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[null]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonArray);

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.findValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);

        List<Integer> list = deserializer.deserialize(jsonParser, new TestContext());
        assertEquals(1, list.size());
        assertNull(list.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationInvalidType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[1, \"apple\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(jsonArray);

        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                mapper.findValueDeserializer(Integer.class),
                null,
                null,
                null,
                null);

        deserializer.deserialize(jsonParser, new TestContext());
    }

    private static class TestContext extends DeserializationContext {
        // Empty for this example
    }
}