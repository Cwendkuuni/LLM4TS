import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeBindings(), null, new JavaType[]{}, new JavaType(String.class));
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getType(), null);
        assertNotNull(collectionLikeType);
        assertEquals(String.class, collectionLikeType.getContentType());

        try {
            CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(new Object().getClass(), null);
            fail("Should throw an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, new JavaType(Integer.class), null, null, false);
        assertFalse(collectionLikeType.isTrueCollectionType());

        ArrayList list = new ArrayList();
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(list.getClass(), null, null, null, new JavaType(Integer.class), null, null, false);
        assertTrue(collectionLikeType2.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, new JavaType(Integer.class), null, null, false);
        assertEquals(Integer.class, collectionLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, new JavaType(Integer.class), null, null, false);
        JavaType javaType = collectionLikeType.withContentType(new JavaType(String.class));
        assertEquals(String.class, javaType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = new TypeFactory().constructType(ArrayList.class, new TypeBindings(), null, new JavaType[]{}, new JavaType(Integer.class));
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getType(), null);
        JavaType upgradedCollectionLikeType = CollectionLikeType.upgradeFrom(collectionLikeType, new JavaType(String.class));
        assertEquals(String.class, upgradedCollectionLikeType.getContentType());

        try {
            JavaType upgradedCollectionLikeType2 = CollectionLikeType.upgradeFrom(new JavaType("InvalidType"), null);
            fail("Should throw an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType javaType = new TypeFactory().constructType(String.class, null, null, null, null, null, null, false);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getType(), null);
        CollectionLikeType.upgradeFrom(collectionLikeType, new JavaType(ArrayList.class));
    }

    @Test
    public void testEmptyConstructor() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        assertNull(collectionLikeType._elementType);
    }
}