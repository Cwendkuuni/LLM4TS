import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstructor() {
        JavaType elementType = new SimpleType("java.lang.Integer");
        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, bindings, null, null, elementType);
        assertNotNull(collectionLikeType);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getElementType());
    }
}