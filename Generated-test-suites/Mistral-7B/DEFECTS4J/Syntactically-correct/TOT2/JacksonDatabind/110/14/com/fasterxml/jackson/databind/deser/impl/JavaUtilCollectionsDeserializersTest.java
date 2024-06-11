import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);

        JsonNode result = mapper.readTree(json);
        List<Integer> list = mapper.treeToValue(result, JavaType.constructCollectionType(List.class, Integer.class));
        List<Integer> expected = Arrays.asList(1);

        assertEquals(expected, deserializer.deserialize(mapper, result, JavaType.constructCollectionType(List.class, Integer.class)));
    }

    @Test
    public void testFindForCollection_EmptyList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);

        JsonNode result = mapper.readTree(json);
        List<Integer> list = mapper.treeToValue(result, JavaType.constructCollectionType(List.class, Integer.class));
        List<Integer> expected = Collections.emptyList();

        assertEquals(expected, deserializer.deserialize(mapper, result, JavaType.constructCollectionType(List.class, Integer.class)));
    }

    // Add test cases for other types (singleton set, singleton map, unmodifiable list, unmodifiable set, unmodifiable map)

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_InvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>(){});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);

        JsonNode result = mapper.readTree(json);
        List<String> list = mapper.treeToValue(result, JavaType.constructCollectionType(List.class, String.class));

        deserializer.deserialize(mapper, result, JavaType.constructCollectionType(List.class, Integer.class));
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"key\":\"value\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>(){});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mockContext(), javaType);

        JsonNode result = mapper.readTree(json);
        Map<String, String> map = mapper.treeToValue(result, JavaType.constructCollectionType(Map.class, String.class, String.class));
        Map<String, String> expected = Collections.singletonMap("key", "value");

        assertEquals(expected, deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, String.class, String.class)));
    }

    // Add test cases for other types (unmodifiable map)

    @Test
    public void testConverter_SingletonSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectConverter converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_SET, mapper.getTypeFactory().constructType(HashSet.class), Set.class);

        HashSet<Integer> set = new HashSet<Integer>() {{ add(1); }};
        Object result = converter.readValue(mapper.writeValueAsString(set));

        Set<Integer> expected = Collections.singleton(1);
        assertEquals(expected, result);
    }

    // Add test cases for other types (singleton list, singleton map, unmodifiable set, unmodifiable list, unmodifiable map)

    @Test(expected = JsonMappingException.class)
    public void testConverter_InvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectConverter converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_SET, mapper.getTypeFactory().constructType(ArrayList.class), Set.class);

        Set<Integer> set = new HashSet<Integer>() {{ add(1); }};
        Object result = converter.readValue(mapper.writeValueAsString(set));

        assertEquals(set, result);
    }

    private DeserializationContext mockContext() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.getFactory().createObjectReader();
        ObjectReader rootReader = reader.readValueAsTree("{}");
        return mapper.getDeserializationContext();
    }
}