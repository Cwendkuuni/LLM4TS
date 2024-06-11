import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructRawListType(String.class));
        CollectionLikeType listTypeInstance = new CollectionLikeType(listType);

        assertEquals(java.util.ArrayList.class, listTypeInstance.getRawClass());
        assertEquals(String.class, listTypeInstance.getContentType().getRawClass());
        assertTrue(listTypeInstance.isCollectionLikeType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType rawListType = JavaType.constructRawListType(String.class);
        JavaType listType = CollectionLikeType.upgradeFrom(rawListType, String.class);

        CollectionLikeType listTypeInstance = new CollectionLikeType(listType);

        assertEquals(java.util.List.class, listTypeInstance.getRawClass());
        assertEquals(String.class, listTypeInstance.getContentType().getRawClass());
        assertTrue(listTypeInstance.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructRawListType(String.class));
        CollectionLikeType listTypeInstance = new CollectionLikeType(listType);

        assertTrue(listTypeInstance.isTrueCollectionType());

        JavaType hashSetType = CollectionLikeType.construct(java.util.HashSet.class, null, null, null, JavaType.constructRawType(java.util.Set.class, String.class));
        CollectionLikeType hashSetTypeInstance = new CollectionLikeType(hashSetType);

        assertFalse(hashSetTypeInstance.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructRawListType(String.class));
        CollectionLikeType listTypeInstance = new CollectionLikeType(listType);

        assertEquals(JavaType.constructRawListType(String.class), listTypeInstance.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType intType = JavaType.constructRawType(int[].class);
        CollectionLikeType.upgradeFrom(JavaType.constructRawType(String.class), intType);
    }
}