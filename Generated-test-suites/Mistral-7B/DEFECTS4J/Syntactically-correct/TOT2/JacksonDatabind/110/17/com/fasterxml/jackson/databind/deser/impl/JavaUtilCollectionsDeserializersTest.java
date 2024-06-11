import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"JavaUtilCollectionsDeserializers\"]";
        List expectedList = Arrays.asList("JavaUtilCollectionsDeserializers");
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
        JsonDeserializer<List<String>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        List actualList = mapper.readValue(json, deserializer);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"JavaUtilCollectionsDeserializers\"]";
        List<String> expectedList = Arrays.asList("JavaUtilCollectionsDeserializers");
        List<String> unmodifiableList = Collections.unmodifiableList(expectedList);
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
        JsonDeserializer<List<String>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        List actualList = mapper.readValue(json, deserializer);
        assertEquals(unmodifiableList, actualList);
    }

    // Add test cases for findForMap, findForCollection(JavaType that has rawClass of CLASS_AS_ARRAYS_LIST),
    // findForCollection(JavaType that has rawClass of CLASS_SINGLETON_SET),
    // findForCollection(JavaType that has rawClass of CLASS_UNMODIFIABLE_SET)
}