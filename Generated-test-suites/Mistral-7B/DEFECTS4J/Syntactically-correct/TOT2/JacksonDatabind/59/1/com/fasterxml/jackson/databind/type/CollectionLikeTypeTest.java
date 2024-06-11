import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testEmptyCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertNotNull(collectionLikeType);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertNull(collectionLikeType.getElementType());
    }
}