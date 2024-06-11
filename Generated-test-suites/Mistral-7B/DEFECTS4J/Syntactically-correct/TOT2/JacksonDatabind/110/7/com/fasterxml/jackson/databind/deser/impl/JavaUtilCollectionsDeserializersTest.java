import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JavaType javaTypeList = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);

        List<Integer> inputList = Arrays.asList(1, 2, 3);
        String jsonString = mapper.writerWithView(new StdSerializer<List<Integer>>(javaTypeList){}.getClass()).writeValueAsString(inputList);
        Object deserializedObject = mapper.readValue(jsonString, deserializer.getType());

        assertEquals(inputList, deserializedObject);
    }
}