import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        List<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertEquals(Collections.emptyList(), collection);
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        List<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertEquals(Collections.singletonList(1), collection);
    }

    @Test
    public void testCollectionDeserializationWithNullValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null, null]";
        List<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertEquals(Collections.singletonList(Collections.singletonList(null)), collection);
    }

    @Test
    public void testCollectionDeserializationWithDifferentTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, \"hello\", true]";
        List<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertEquals(Collections.singletonList(Arrays.asList(1, "hello", true)), collection);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "invalidJson";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithMissingArrayToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "1";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithNonNullValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        String json = "[\"hello\"]";
        mapper.registerDeserializer(String.class, stringDeserializer);
        mapper.readValue(json, CollectionDeserializer.class);
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        protected StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new IOException("StringDeserializer should not be called");
        }
    }

    @Test
    public void testCollectionDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<List<Integer>> listDeserializer = new ListDeserializer();
        mapper.registerDeserializer(List.class, listDeserializer);
        String json = "[1, 2, 3]";
        List<Object> collection = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertEquals(Collections.singletonList(Arrays.asList(1, 2, 3)), collection);
    }

    static class ListDeserializer extends JsonDeserializer<List<Integer>> {
        @Override
        public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ArrayList<>();
        }
    }
}