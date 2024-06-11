import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(1, result.iterator().next());
    }

    @Test
    public void testCollectionDeserializationWithNullElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertNull(result.iterator().next());
    }

    @Test
    public void testCollectionDeserializationWithBasicTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 'three']";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(1));
        Assert.assertTrue(result.contains(2));
        Assert.assertTrue(result.contains("three"));
    }

    @Test
    public void testCollectionDeserializationWithComplexTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{ \"name\": \"John\", \"age\": 30 }, { \"name\": \"Jane\", \"age\": 35 }]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());

        Assert.assertTrue(result.iterator().hasNext());
        Map<String, Object> firstMap = (Map<String, Object>) result.iterator().next();
        Assert.assertEquals("John", firstMap.get("name"));
        Assert.assertEquals(30, firstMap.get("age"));

        Assert.assertTrue(result.iterator().hasNext());
        Map<String, Object> secondMap = (Map<String, Object>) result.iterator().next();
        Assert.assertEquals("Jane", secondMap.get("name"));
        Assert.assertEquals(35, secondMap.get("age"));
    }

    @Test
    public void testCollectionDeserializationWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());

        String emptyStringJson = "\"\"";
        Collection<Object> emptyStringResult = mapper.readValue(emptyStringJson, CollectionDeserializer.class);
        Assert.assertNotNull(emptyStringResult);
        Assert.assertTrue(emptyStringResult.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionDeserializationWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithUnresolvableForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, $ref]";
        mapper.registerModule(new SimpleModule()
                .addDeserializer(Integer.class, new CustomIntegerDeserializer()));

        JsonDeserializer<Collection<Object>> collectionDeserializer = mapper.deserializationConfig()
                .findValueDeserializer(Collection.class, null);
        JsonParser jsonParser = mapper.getFactory()
                .createParser(new ByteArrayInputStream("[".getBytes()));
        jsonParser.nextToken(); // START_ARRAY

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, null);
        CustomIntegerDeserializer customIntegerDeserializer = (CustomIntegerDeserializer) mapper.deserializationConfig()
                .findValueDeserializer(Integer.class, null);
        customIntegerDeserializer.handleUnresolvedForwardReference(new UnresolvedForwardReference(null, "1"), null);

        jsonParser.nextToken(); // VALUE_NUMBER_INT
        jsonParser.nextToken(); // END_ARRAY

        jsonParser.nextToken(); // START_ARRAY
        jsonParser.nextToken(); // VALUE_STRING
        jsonParser.nextToken(); // END_ARRAY

        jsonParser.nextToken(); // VALUE_NULL

        jsonParser.nextToken(); // START_ARRAY
        jsonParser.nextToken(); // VALUE_NUMBER_INT
        jsonParser.nextToken(); // VALUE_NULL
        jsonParser.nextToken(); // END_ARRAY

        jsonParser.nextToken(); // VALUE_STRING
        jsonParser.nextToken(); // END_ARRAY

        jsonParser.nextToken(); // VALUE_NULL

        jsonParser.nextToken(); // VALUE_STRING

        collectionDeserializer.deserialize(jsonParser, null);
    }

    @Test
    public void testCollectionDeserializationWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, $ref]";
        mapper.registerModule(new SimpleModule()
                .addDeserializer(Integer.class, new CustomIntegerDeserializer()));

        JsonDeserializer<Collection<Object>> collectionDeserializer = mapper.deserializationConfig()
                .findValueDeserializer(Collection.class, null);
        JsonParser jsonParser = mapper.getFactory()
                .createParser(new ByteArrayInputStream("[".getBytes()));
        jsonParser.nextToken(); // START_ARRAY

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, null);

        CustomIntegerDeserializer customIntegerDeserializer = (CustomIntegerDeserializer) mapper.deserializationConfig()
                .findValueDeserializer(Integer.class, null);
        customIntegerDeserializer.handleUnresolvedForwardReference(new UnresolvedForwardReference(null, "1"), null);

        jsonParser.nextToken(); // VALUE_NUMBER_INT

        jsonParser.nextToken(); // START_ARRAY
        jsonParser.nextToken(); // VALUE_NULL

        jsonParser.nextToken(); // END_ARRAY

        jsonParser.nextToken(); // VALUE_STRING
        jsonParser.nextToken(); // END_ARRAY

        jsonParser.nextToken(); // VALUE_NULL

        jsonParser.nextToken(); // VALUE_STRING

        Collection<Object> resolvedResult = new ArrayList<>();
        resolvedResult.add(1);
        resolvedResult.add(customIntegerDeserializer.getValue());

        JsonDeserializer<Collection<Object>> resolvedCollectionDeserializer = mapper.deserializationConfig()
                .findValueDeserializer(Collection.class, null);
        Collection<Object> deserializedResult = resolvedCollectionDeserializer.deserialize(jsonParser, null);

        Assert.assertEquals(resolvedResult, deserializedResult);
    }

    private static class CustomIntegerDeserializer extends StdDeserializer<Integer> {
        private Integer _value;

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            _value = p.getValueAsInt();
            return _value;
        }

        public Integer getValue() {
            return _value;
        }

        @Override
        public void handleUnresolvedForwardReference(UnresolvedForwardReference reference, Object id)
                throws IOException {
            if (id != null && this.getClass().getName().equals(id.getClass().getName())) {
                _value = (Integer) id;
            }
        }
    }
}