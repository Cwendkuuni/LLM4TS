import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructWithCollectionClass() {
        JavaType collectionType = CollectionLikeType.construct( java.util.Collection.class, null, null, null, null);
        assertEquals(java.util.Collection.class, collectionType.getRawClass());
    }

    @Test
    public void testConstructWithArrayListClassAndElementType() {
        JavaType arrayListType = CollectionLikeType.construct( java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertEquals(java.util.ArrayList.class, arrayListType.getRawClass());
        assertEquals(java.Integer.TYPE, arrayListType.getContentType());
    }

    @Test
    public void testUpgradeFromSimpleType() {
        JavaType intType = JavaType.construct(int[].class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(intType, java.Integer.TYPE);
        assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
        assertEquals(java.Integer.TYPE, collectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType stringType = JavaType.construct(String.class);
        CollectionLikeType.upgradeFrom(stringType, java.Integer.TYPE);
    }

    @Test
    public void testIsContainerType() {
        JavaType intArrayType = JavaType.construct(int[].class);
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertEquals(true, intArrayType.isContainerType());
        assertEquals(true, listType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType intArrayType = JavaType.construct(int[].class);
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertEquals(true, intArrayType.isCollectionLikeType());
        assertEquals(true, listType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertEquals(java.Integer.TYPE, listType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertEquals(java.lang.Integer.class, listType.getContentValueHandler().getClass());
    }

    @Test
    public void testHasHandlers() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertTrue(listType.hasHandlers());
    }

    @Test
    public void testEquals() {
        JavaType listType1 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        JavaType listType2 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertEquals(listType1, listType2);
    }

    @Test
    public void testNotEqualsDifferentTypes() {
        JavaType listType1 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        JavaType listType2 = CollectionLikeType.construct(java.util.LinkedList.class, null, null, null, java.Integer.TYPE);
        assertNotEquals(listType1, listType2);
    }

    @Test
    public void testToString() {
        JavaType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.Integer.TYPE);
        assertEquals("[collection-like type; class java.util.ArrayList, contains java.Integer]", listType.toString());
    }
}