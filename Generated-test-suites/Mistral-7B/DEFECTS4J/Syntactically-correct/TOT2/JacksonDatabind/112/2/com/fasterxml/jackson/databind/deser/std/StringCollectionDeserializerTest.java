import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyStringCollectionDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testStringCollectionDeserializationWithStandardDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"test1\", \"test2\", \"test3\"]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(Arrays.asList("test1", "test2", "test3"), result);
    }

    @Test
    public void testStringCollectionDeserializationWithCustomDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"value\": \"test1\"}, {\"value\": \"test2\"}, {\"value\": \"test3\"}]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        JsonDeserializer<String> customDeserializer = new StringDeserializer();
        deserializer = deserializer.withResolved(null, customDeserializer, null, null);
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(Arrays.asList("test1", "test2", "test3"), result);
    }

    @Test
    public void testNullValueDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCollectionTypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }
}