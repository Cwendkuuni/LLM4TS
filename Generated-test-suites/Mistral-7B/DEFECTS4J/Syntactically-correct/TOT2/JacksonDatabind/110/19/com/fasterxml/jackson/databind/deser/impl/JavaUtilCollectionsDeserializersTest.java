import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializationContext());
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }
}