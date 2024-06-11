import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        Class<?> collectionType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(collectionType);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionType, bindings, superClass, superInts, elemT);

        assertEquals(collectionType, collectionLikeType.getRawClass());
        assertEquals(bindings, collectionLikeType._bindings);
        assertEquals(superClass, collectionLikeType._superClass);
        assertArrayEquals(superInts, collectionLikeType._superInterfaces);
        assertEquals(elemT, collectionLikeType._elementType);
    }
}