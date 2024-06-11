import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingleList() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        when(type.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST)).thenReturn(true);
        when(type.findSuperType(List.class)).thenReturn(ArrayList.class);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    // Add edge case tests for other types and null input
}