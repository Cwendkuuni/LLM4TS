import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeBinding;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeCreation() {
        // Test constructing CollectionLikeType using no-args constructor
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        
        // Test constructing CollectionLikeType using bindings constructor
        TypeBinding<Integer> intBinding = TypeBindings.create(Integer.class);
        JavaType javaListTypeWithIntBinding = TypeFactory.defaultInstance().constructCollectionType(List.class, intBinding);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(javaListTypeWithIntBinding.getRawClass(), intBinding);

        // Test upgrading JavaType to CollectionLikeType
        JavaType javaArrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType3 = CollectionLikeType.upgradeFrom(javaArrayListType, Integer.class);

        // Test equality and hashCode for different CollectionLikeType instances
        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
        assertFalse(collectionLikeType1.equals(collectionLikeType3));
        assertEquals(collectionLikeType1.hashCode(), collectionLikeType1.hashCode());
        assertEquals(collectionLikeType1.hashCode(), collectionLikeType1.hashCode());
        assertNotEquals(collectionLikeType1.hashCode(), collectionLikeType2.hashCode());
        assertNotEquals(collectionLikeType1.hashCode(), collectionLikeType3.hashCode());

        // Test getClass() and getRawType() for CollectionLikeType
        assertEquals(javaListType.getRawClass(), collectionLikeType1.getClass());
        assertEquals(javaListType.getRawClass(), collectionLikeType1.getRawType());
    }

    @Test
    public void testCollectionLikeTypeRefining() {
        // Test refining CollectionLikeType to actual Collection type
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        JavaType javaArrayListType = collectionLikeType.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(javaArrayListType instanceof ArrayList);
    }

    @Test
    public void testCollectionLikeTypeNarrowing() {
        // Test narrowing CollectionLikeType to subclass
        JavaType javaArrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaArrayListType.getRawClass(), TypeBindings.emptyBindings());
        JavaType javaLinkedListType = collectionLikeType._narrow(LinkedList.class);
        assertTrue(javaLinkedListType instanceof LinkedList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionLikeTypeUpgradeFromInvalidBaseType() {
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType.upgradeFrom(javaStringType, Integer.class);
    }

    @Test
    public void testCollectionLikeTypeIsCollectionLikeType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testCollectionLikeTypeIsContainerType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testCollectionLikeTypeGetContentType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        JavaType contentType = collectionLikeType.getContentType();
        assertEquals(Integer.class, contentType.getRawClass());
    }

    @Test
    public void testCollectionLikeTypeGetContentTypeHandler() {
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        Object contentTypeHandler = collectionLikeType.getContentTypeHandler();
        assertNotNull(contentTypeHandler);
    }

    @Test
    public void testCollectionLikeTypeGetContentValueHandler() {
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        Object contentValueHandler = collectionLikeType.getContentValueHandler();
        assertNotNull(contentValueHandler);
    }

    @Test
    public void testCollectionLikeTypeHasHandlers() {
        JavaType javaListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings());
        assertTrue(collectionLikeType.hasHandlers());
    }
}