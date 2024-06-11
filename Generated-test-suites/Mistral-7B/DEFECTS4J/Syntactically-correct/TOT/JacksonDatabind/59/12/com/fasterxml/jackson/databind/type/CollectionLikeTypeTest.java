import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.construct(String.class));
        assertEquals(JavaType.construct(java.util.Collection.class), collectionType.getRawType());
        assertEquals(JavaType.construct(String.class), collectionType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = JavaType.construct(java.util.ArrayList.class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(listType, JavaType.construct(Integer.class));
        assertEquals(JavaType.construct(java.util.Collection.class), collectionLikeType.getRawType());
        assertEquals(JavaType.construct(Integer.class), collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.construct(String.class));
        assertEquals(JavaType.construct(String.class), collectionType.getContentType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.construct(String.class));
        assertTrue(collectionType.isCollectionLikeType());
    }

    @Test
    public void testIsContainerType() {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.construct(String.class));
        assertTrue(collectionType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalid() {
        JavaType collectionType = CollectionLikeType.construct(int[].class, null, null, null, JavaType.construct(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalid() {
        JavaType baseType = JavaType.construct(java.util.ArrayList.class);
        JavaType elementType = JavaType.construct(int.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        CollectionLikeType invalidCollectionLikeType = CollectionLikeType.upgradeFrom(collectionLikeType, JavaType.construct(int[].class));
    }
}