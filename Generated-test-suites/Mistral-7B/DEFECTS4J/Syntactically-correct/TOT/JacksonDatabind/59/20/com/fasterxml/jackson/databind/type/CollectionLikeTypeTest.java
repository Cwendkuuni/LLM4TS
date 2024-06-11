import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionLikeType.construct(listClass, null, null, null, JavaType.constructNumberType(Integer.TYPE));
        CollectionLikeType collectionLikeType = (CollectionLikeType) listType;
        assertEquals(listClass, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructNumberType(Integer.TYPE), collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeUpgrade() {
        Class<List<Integer>> listClass = List.class;
        JavaType intType = JavaType.constructNumberType(Integer.TYPE);
        JavaType listType = CollectionLikeType.construct(listClass, null, null, null, intType);
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(listType, intType);
        assertEquals(listClass, upgradedType.getRawClass());
        assertEquals(intType, upgradedType.getContentType());
        assertTrue(upgradedType.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeWithHandlers() {
        Class<List<String>> listClass = List.class;
        JavaType stringType = JavaType.constructType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listClass, null, null, null, stringType);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        CollectionLikeType withHandlersType = collectionLikeType.withValueHandler(valueHandler).withTypeHandler(typeHandler);
        assertEquals(valueHandler, withHandlersType.getValueHandler());
        assertEquals(typeHandler, withHandlersType.getTypeHandler());
    }

    @Test
    public void testCollectionLikeTypeIsContainerTypeAndIsCollectionLikeType() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionLikeType.construct(listClass, null, null, null, JavaType.constructNumberType(Integer.TYPE));
        assertTrue(listType.isContainerType());
        assertTrue(listType.isCollectionLikeType());
    }

    @Test
    public void testCollectionLikeTypeEquals() {
        Class<List<Integer>> listClass = List.class;
        JavaType intType = JavaType.constructNumberType(Integer.TYPE);
        JavaType firstListType = CollectionLikeType.construct(listClass, null, null, null, intType);
        JavaType secondListType = CollectionLikeType.construct(listClass, null, null, null, intType);
        assertTrue(firstListType.equals(secondListType));
        assertFalse(firstListType.equals(new Object()));
    }

    @Test
    public void testCollectionLikeTypeGetCanonicalName() {
        Class<List<Integer>> listClass = List.class;
        JavaType intType = JavaType.constructNumberType(Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listClass, null, null, null, intType);
        String canonicalName = collectionLikeType.buildCanonicalName();
        String expectedCanonicalName = "[Ljava.util.List;Ljava/lang/Integer;";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testCollectionLikeTypeGetErasedSignature() {
        Class<List<Integer>> listClass = List.class;
        JavaType intType = JavaType.constructNumberType(Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listClass, null, null, null, intType);
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);
        String expectedErasedSignature = "Ljava/util/List;";
        assertEquals(expectedErasedSignature, sb.toString());
    }

    @Test
    public void testCollectionLikeTypeGetGenericSignature() {
        Class<List<Integer>> listClass = List.class;
        JavaType intType = JavaType.constructNumberType(Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listClass, null, null, null, intType);
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getGenericSignature(sb);
        String expectedGenericSignature = "[Ljava.util.List<Ljava/lang/Integer;>;]";
        assertEquals(expectedGenericSignature, sb.toString());
    }
}