import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionLikeTypeTest {
    // 
    @Test
    public void testCollectionLikeTypeConstructor() {
        Class<List> listClass = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(List.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.TYPE;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listClass, bindings, superClass, superInts, elemT);
        JavaType expectedType = new CollectionType(List.class, elemT);
        assertEquals(expectedType.toString(), collectionLikeType.toString());
    }
    // 
}