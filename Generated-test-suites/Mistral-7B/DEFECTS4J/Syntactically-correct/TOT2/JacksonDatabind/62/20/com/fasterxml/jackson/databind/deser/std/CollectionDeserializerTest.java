import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectionWithSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.iterator().next());
    }

    @Test
    public void testCollectionWithMultipleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, result.size());
        List<Object> list = new ArrayList<>(result);
        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
        assertEquals(Integer.valueOf(3), list.get(2));
    }

    @Test(expected = IOException.class)
    public void testMalformedJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{invalid: json}";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testSingleValueAsArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "1";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testWrongTypeJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{name: 'John'}";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testNullContentType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        JsonDeserializer<Object> deserializer = mapper.getPolymorphicDeserializer(CollectionDeserializer.class, null);
        Collection<Object> result = deserializer.deserialize(JsonFactory.instance.createParser(json), null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testCollectionWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json1 = "[1, ${ref1}]";
        String json2 = "{\"ref1\": []}";
        ObjectWriter writer = mapper.writer();
        JsonNode jsonNode1 = writer.writeValueAsTree(json1);
        JsonNode jsonNode2 = writer.writeValueAsTree(json2);
        String json = writer.writeValueAsString(jsonNode1);

        JsonParser parser = mapper.parser();
        parser.readValueAsTree(jsonNode2);
        parser.setCurrentName("ref1");
        ObjectReader reader = mapper.readerForTree(CollectionDeserializer.class);
        Collection<Object> result = reader.readValue(parser);
        assertEquals(2, result.size());
        List<Object> list = new ArrayList<>(result);
        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Collections.emptyList(), list.get(1));
    }
}