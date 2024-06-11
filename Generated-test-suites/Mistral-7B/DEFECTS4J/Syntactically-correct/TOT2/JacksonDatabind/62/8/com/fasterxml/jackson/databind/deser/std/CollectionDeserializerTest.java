import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
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
    public void testMultipleElementsCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        List<Object> list = new ArrayList<>(result);
        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyStringIsNotCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullIsNotCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonIsNotCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{invalid: json}";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testValueDeserializerThrowsException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> failingDeserializer = new StubDeserializer();
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.registerValuedDeserializer(Integer.class, failingDeserializer);
        String json = "[1]";
        customMapper.readValue(json, CollectionDeserializer.class);
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.registerDeserializer(String.class, stringDeserializer);
        String json = "[\"Hello\", \"World\"]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        List<String> list = new ArrayList<>(result);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("Hello", list.get(0));
        Assert.assertEquals("World", list.get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testUnwrapSingleValueIsFalse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        mapper.findConfig().setBoolean(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(1, result.iterator().next());
    }

    @Test
    public void testUnwrapSingleValueIsTrue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        mapper.findConfig().setBoolean(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(java.util.Arrays.asList(1), result);
    }
}

class StubDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        throw new IOException("Stub deserializer for testing");
    }
}

class StringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}