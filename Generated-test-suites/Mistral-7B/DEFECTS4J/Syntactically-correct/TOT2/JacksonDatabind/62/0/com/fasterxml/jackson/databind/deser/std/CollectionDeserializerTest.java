import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyArrayCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleElementArrayCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    public void testNullValueCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Collections.singleton(null), result);
    }

    @Test
    public void testListOfObjectsCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testListOfObjectsWithCustomTypeIdentifierCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"id\": 1}, {\"id\": 2}, {\"id\": 3}]";
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializers().findValueDeserializer(MyClass.class, null);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(new TypeReference<Collection<MyClass>>() {}, null);
        Collection<MyClass> result = deserializer.deserialize(mapper.readTree(json), null, typeDeserializer);
        assertEquals(Arrays.asList(new MyClass(1), new MyClass(2), new MyClass(3)), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(mapper.readTree(json), null);
        fail("Expected JsonMappingException, but got " + result);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonParsingFailureCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]"; // malformed JSON
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        try {
            Collection<Object> result = deserializer.deserialize(mapper.readTree(json), null);
            fail("Expected JsonMappingException, but got " + result);
        } catch (JsonProcessingException e) {
            // expected
        }
    }
}