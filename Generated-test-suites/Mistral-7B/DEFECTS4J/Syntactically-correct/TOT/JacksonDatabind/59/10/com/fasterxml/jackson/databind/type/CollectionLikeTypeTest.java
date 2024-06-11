import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testBasicConstruction() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Builder().build(),
                _bogusSuperClass(java.util.ArrayList.class), null,
                JavaType.construct(int.class));
        CollectionLikeType list = (CollectionLikeType) listType;
        assertEquals(int.class, list.getContentType());
    }

    @Test
    public void testDeprecatedConstruction() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class,
                JavaType.construct(int.class));
        CollectionLikeType list = (CollectionLikeType) listType;
        assertEquals(int.class, list.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType intType = JavaType.construct(int.class);
        JavaType listType = CollectionLikeType.upgradeFrom(intType, JavaType.construct(String.class));
        CollectionLikeType list = (CollectionLikeType) listType;
        assertEquals(String.class, list.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType intType = JavaType.construct(int.class);
        JavaType listType = CollectionLikeType.upgradeFrom(intType, JavaType.construct(String[].class));
    }

    @Test
    public void testIsContainerType() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Builder().build(),
                _bogusSuperClass(java.util.ArrayList.class), null,
                JavaType.construct(int.class));
        assertTrue(listType.isContainerType());
        JavaType intType = JavaType.construct(int.class);
        assertFalse(intType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Builder().build(),
                _bogusSuperClass(java.util.ArrayList.class), null,
                JavaType.construct(int.class));
        assertTrue(listType.isCollectionLikeType());
        JavaType intType = JavaType.construct(int.class);
        assertFalse(intType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Builder().build(),
                _bogusSuperClass(java.util.ArrayList.class), null,
                JavaType.construct(int.class));
        assertEquals(int.class, listType.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Builder().build(),
                _bogusSuperClass(java.util.ArrayList.class), null,
                JavaType.construct(int.class));
        assertTrue(listType.isTrueCollectionType());
        JavaType intType = JavaType.construct(int.class);
        assertFalse(intType.isTrueCollectionType());
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Builder().build(),
                _bogusSuperClass(java.util.ArrayList.class), null,
                JavaType.construct(int.class));
        JavaType refinedListType = listType.refine(java.util.List.class, new TypeBindings.Builder().build(),
                _bogusSuperClass(java.util.List.class), null);
        assertEquals(JavaType.construct(java.util.List.class), refinedListType);
    }
}