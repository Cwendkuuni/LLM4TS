import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType singleListType = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, singleListType);
        assertNotNull(deserializer);

        JavaType unknownType = mapper.getTypeFactory().constructType(Object.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, unknownType);
        assertNull(deserializer);
    }
}