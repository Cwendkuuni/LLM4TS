import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeDeserializerBinding;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));
        CollectionLikeType actualCollectionLikeType = new CollectionLikeType(collectionType);
        JavaType expectedCollectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class), null, null, false);
        assertEquals(expectedCollectionLikeType, actualCollectionLikeType);
    }

    @Test
    public void testCollectionLikeTypeUpgrade() {
        JavaType javaType = JavaType.construct(java.util.List.class);
        JavaType elementType = JavaType.construct(int.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType);
        JavaType upgradedCollectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);
        assertEquals(collectionLikeType.getClass(), upgradedCollectionLikeType.getClass());
        assertEquals(collectionLikeType.getContentType(), upgradedCollectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeIsContainerType() {
        JavaType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testCollectionLikeTypeIsCollectionLikeType() {
        JavaType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testCollectionLikeTypeGetContentType() {
        JavaType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));
        JavaType expectedContentType = JavaType.construct(int.class);
        JavaType actualContentType = collectionLikeType.getContentType();
        assertEquals(expectedContentType, actualContentType);
    }

    @Test
    public void testCollectionLikeTypeGetContentValueHandler() {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));
        TypeDeserializerBinding intBinding = TypeBindings.findDeserializerBinding(JavaType.construct(int.class));
        TypeDeserializer intDeserializer = intBinding.getValueHandler();
        Object expectedContentValueHandler = intDeserializer;
        Object actualContentValueHandler = collectionType.getContentValueHandler();
        assertEquals(expectedContentValueHandler, actualContentValueHandler);
    }

    @Test
    public void testCollectionLikeTypeHasHandlers() {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));
        boolean hasHandlers = collectionType.hasHandlers();
        assertTrue(hasHandlers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionLikeTypeUpgradeFromInvalidBaseType() {
        JavaType javaType = JavaType.construct(String.class);
        JavaType elementType = JavaType.construct(int.class);
        CollectionLikeType.upgradeFrom(javaType, elementType);
    }
}