import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext context = new TestingDeserializationContext();
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, listType);
        assertNotNull(deserializer);
    }
}