import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstructor() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listClass, bindings, null, null, javaType);

        assertEquals(List.class, collectionLikeType.getClass());
        assertEquals(Integer.class, collectionLikeType.getElementType().getTypeClass());
    }
}