import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

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
        String json = "[\"element\"]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, result.size());
        assertEquals("element", result.iterator().next());
    }

    @Test
    public void testCollectionWithMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(3, result.size());
        List<Object> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionWithNullElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertTrue(result.contains(null));
    }

    @Test(expected = IOException.class)
    public void testMissingStartArrayToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "not an array";
        mapper.readValue(new ByteArrayInputStream(json.getBytes()), CollectionDeserializer.class);
    }

    @Test(expected = IOException.class)
    public void testNonValueStringAsArrayStart() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"key\": \"not an array\"}";
        mapper.readValue(new ByteArrayInputStream(json.getBytes()), CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyStringAsArrayStart() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"\"";
        mapper.readValue(new ByteArrayInputStream(json.getBytes()), CollectionDeserializer.class);
    }

    @Test
    public void testCollectionWithNestedObjects() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{ \"key\": 1 }, { \"key\": 2 }]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        List<Object> expected = new ArrayList<>();
        expected.add(new StandardObjectDeserializer().deserialize(new JsonFactory(), new JsonParser(new StringReader("{\"key\":1}")), null));
        expected.add(new StandardObjectDeserializer().deserialize(new JsonFactory(), new JsonParser(new StringReader("{\"key\":2}")), null));
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionWithEmptyNestedObjects() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{}]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        List<Object> expected = new ArrayList<>();
        expected.add(new StandardObjectDeserializer().deserialize(new JsonFactory(), new JsonParser(new StringReader("{}")), null));
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionWithPolymorphicElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, \"two\", 3.0]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        List<Object> expected = Arrays.asList(1, "two", 3.0);
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionWithCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> customDeserializer = new Base64Deserializer();
        mapper.registerDeserializer(String.class, customDeserializer);
        String json = "[\"SGVsbG8gV29ybGQ=\"]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals("alice", result.iterator().next());
    }
}