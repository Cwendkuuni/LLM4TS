import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyArray() throws IOException {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleElementArray() throws IOException {
        String json = "[1]";
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
        Assert.assertEquals(Arrays.asList(1), result);
    }

    @Test
    public void testNullElement() throws IOException {
        String json = "[null]";
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
        Assert.assertEquals(Arrays.asList(null), result);
    }

    @Test
    public void testStringElements() throws IOException {
        String json = "[\"foo\", \"bar\"]";
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, String.class);
        Collection<String> result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
        Assert.assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testInvalidJson() throws IOException {
        String json = "{foo: 1}"; // not an array
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        try {
            deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Cannot construct instance of \"java.util.Collection<Object>\": not a collection (type: START_OBJECT)", e.getMessage());
        }
    }

    @Test
    public void testNonCollectionType() throws IOException {
        String json = "[1, 2, 3]";
        ObjectMapper mapper = new ObjectMapper();
        ObjectDeserializer deserializer = mapper.findValueDeserializer(Integer[].class, null);
        try {
            deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Expected BEGIN_ARRAY but was START_OBJECT", e.getMessage());
        }
    }

    @Test
    public void testNestedCollections() throws IOException {
        String json = "[1, [2, 3]], 4]";
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
        Assert.assertEquals(Arrays.asList(1, Arrays.asList(2, 3), 4), result);
    }

    @Test
    public void testCircularReference() throws IOException {
        String json = "[{ \"id\": 1, \"ref\": 1 }]";
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = mapper.getSerializationConfig()
                .getDefaultObjectIdReader();
        mapper.getDeserializationConfig()
                .findValueDeserializerValue(ObjectIdReader.class)
                .setValue(objectIdReader);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);

        try {
            deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertTrue(e.getCause() instanceof CircularReferenceException);
        }
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        String json = "[\"foo\", \"bar\"]";
        ObjectMapper mapper = new ObjectMapper();
        CustomCollectionDeserializer deserializer = new CustomCollectionDeserializer();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(Collection.class, deserializer));
        Collection<String> result = mapper.readValue(json, Collection.class);
        Assert.assertEquals(Arrays.asList("foo", "bar"), result);
    }

    static class CustomCollectionDeserializer extends CollectionDeserializer {
        @Override
        public CollectionDeserializer withResolved(JsonDeserializer<?> dd,
                JsonDeserializer<?> vd, TypeDeserializer vtd,
                Boolean unwrapSingle) {
            return new CustomCollectionDeserializer();
        }

        @Override
        public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            Object[] values = p.readValueAs(Object[].class);
            return Arrays.asList(values);
        }

        @Override
        public void handleNonArray(JsonParser p, DeserializationContext ctxt,
                Collection<Object> result) throws IOException {
            // Do nothing, as this test case uses a custom deserializer
        }
    }
}