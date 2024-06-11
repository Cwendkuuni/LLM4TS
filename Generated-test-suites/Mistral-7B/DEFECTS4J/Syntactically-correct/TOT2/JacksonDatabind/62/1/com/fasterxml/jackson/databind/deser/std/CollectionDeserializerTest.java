import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonEmptyArray = "[]";
        Collection<Object> result = mapper.readValue(jsonEmptyArray, CollectionDeserializer.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleValueCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonSingleValue = "[\"Hello World\"]";
        Collection<Object> result = mapper.readValue(jsonSingleValue, CollectionDeserializer.class);
        assertEquals(1, result.size());
        assertEquals("Hello World", result.iterator().next());
    }

    @Test
    public void testCollectionDeserializationWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonNullValue = "[null]";
        Collection<Object> result = mapper.readValue(jsonNullValue, CollectionDeserializer.class);
        assertEquals(1, result.size());
        assertNull(result.iterator().next());
    }

    @Test
    public void testCollectionDeserializationWithMultipleValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMultipleValues = "[1, 2, 3]";
        Collection<Object> result = mapper.readValue(jsonMultipleValues, CollectionDeserializer.class);
        assertEquals(3, result.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1, result.iterator().next());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithInvalidToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInvalidToken = "{\"invalid\": []}";
        mapper.readValue(jsonInvalidToken, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithMissingArrayToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMissingArrayToken = "[1]";
        mapper.readValue(jsonMissingArrayToken, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithValueDeserializationFailure() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> failingDeserializer = new FailingStringDeserializer();
        ObjectMapper contextualMapper = new ObjectMapper();
        contextualMapper.registerDeserializer(String.class, failingDeserializer);
        String jsonFailingDeserialization = "[\"fail\"]";
        contextualMapper.readValue(jsonFailingDeserialization, CollectionDeserializer.class);
    }

    @Test
    public void testCollectionDeserializationWithTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonTypeDeserialization = "[{\"type\": \"String\", \"value\": \"Hello World\"}]";
        Collection<Object> result = mapper.readValue(jsonTypeDeserialization, CollectionDeserializer.class);
        assertEquals(1, result.size());
        Object deserializedItem = result.iterator().next();
        JsonNode deserializedNode = mapper.readTree(new ByteArrayInputStream(mapper.writerWithView(deserializedItem).writeValueAsBytes()));
        assertEquals("Hello World", deserializedNode.get("value").asText());
        assertEquals(String.class, deserializedNode.getType());
    }

    @Test
    public void testCollectionDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonDelegateDeserialization = "[{\"name\": \"John\", \"age\": 30}]";
        Collection<Object> result = mapper.readValue(jsonDelegateDeserialization, new CollectionDelegateDeserializer());
        assertEquals(1, result.size());
        Object deserializedItem = result.iterator().next();
        JsonNode deserializedNode = mapper.readTree(new ByteArrayInputStream(mapper.writerWithView(deserializedItem).writeValueAsBytes()));
        assertEquals("John", deserializedNode.get("name").asText());
        assertEquals(30, deserializedNode.get("age").asInt());
    }

    @Test
    public void testCollectionDeserializationWithUnwrappedSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonUnwrappedSingleValue = "[1]";
        Boolean unwrapSingle = true;
        Collection<Object> result = mapper.readValue(jsonUnwrappedSingleValue, CollectionDeserializer.class);
        assertEquals(1, result.size());
        assertEquals(1, result.iterator().next());
    }

    static class FailingStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            throw new IOException("Deserialization failure");
        }
    }

    static class CollectionDelegateDeserializer extends CollectionDeserializer {
        @Override
        public CollectionDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd) {
            return this.withResolved(dd, vd, vtd, null);
        }

        @Override
        public JsonDeserializer<Object> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            JsonDeserializer<Object> delegateDeserializer = new DelegateObjectDeserializer();
            return this.withResolved(delegateDeserializer, null, null, null);
        }
    }

    static class DelegateObjectDeserializer extends StdDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            ObjectName nameNode = jsonParser.getCodec().readTree(jsonParser);
            return new ObjectName(nameNode.get("name").asText());
        }
    }

    static class ObjectName {
        private final String name;

        ObjectName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}