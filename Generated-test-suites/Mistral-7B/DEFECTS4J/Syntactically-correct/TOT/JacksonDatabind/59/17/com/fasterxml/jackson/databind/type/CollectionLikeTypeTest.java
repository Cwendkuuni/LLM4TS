import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructWithCollectionType() {
 CollectionLikeType collectionType = CollectionLikeType.construct(java.util.Collection.class, null, null, null, JavaType.of(String.class));
 assertEquals(String.class, collectionType.getContentType().getRawClass());
 }

 @Test
 public void testConstructWithTypeVariables() {
 Class<MyList> myListType = new Class<MyList>() {}.getGenericSuperclass();
 JavaType javaType = JavaType.construct(myListType, null);
 CollectionLikeType collectionType = CollectionLikeType.construct(myListType, null, null, null, JavaType.of(String.class));
 assertEquals(String.class, collectionType.getContentType().getRawClass());
 }

 @Test
 public void testUpgradeFromSimpleType() {
 JavaType simpleType = JavaType.construct(String[].class);
 CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(simpleType, JavaType.of(String.class));
 assertEquals(String.class, collectionType.getContentType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidType() {
 JavaType invalidType = JavaType.construct(Integer.TYPE);
 CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(invalidType, JavaType.of(String.class));
 fail("Should not be able to upgrade from an Integer type");
 }

 @Test
 public void testRefine() {
 Class<MyMap> myMapType = new Class<MyMap>() {}.getGenericSuperclass();
 JavaType javaType = JavaType.construct(myMapType, null);
 CollectionLikeType collectionType = CollectionLikeType.construct(JavaType.construct(Map.class, null), JavaType.of(String.class));
 JavaType refinedType = collectionType.refine(MyMap.class, null, null, null);
 assertEquals(MyMap.class, refinedType.getRawClass());
 }

 @Test
 public void testIsContainerType() {
 assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class))).isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class))).isCollectionLikeType());
 }

 @Test
 public void testIsTrueCollectionType() {
 assertTrue(new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class)).isTrueCollectionType());
 assertFalse(new CollectionLikeType(String[].class, null, null, null, JavaType.of(String.class)).isTrueCollectionType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class));
 assertEquals(String.class, collectionType.getContentType().getRawClass());
 }

 @Test
 public void testGetContentValueHandler() {
 CollectionLikeType collectionType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class));
 assertNull(collectionType.getContentValueHandler());
 }

 @Test
 public void testGetContentTypeHandler() {
 CollectionLikeType collectionType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class));
 assertNotNull(collectionType.getContentTypeHandler());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class));
 assertTrue(collectionType.hasHandlers());
 }

 @Test
 public void testGetErasedSignature() {
 CollectionLikeType collectionType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class));
 StringBuilder sb = new StringBuilder();
 collectionType.getErasedSignature(sb);
 assertEquals("java.util.ArrayList", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 CollectionLikeType collectionType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class));
 StringBuilder sb = new StringBuilder();
 collectionType.getGenericSignature(sb);
 assertEquals("java.util.List<java.lang.String>", sb.toString());
 }

 @Test
 public void testBuildCanonicalName() {
 CollectionLikeType collectionType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.of(String.class));
 assertEquals("java.util.ArrayList<java.lang.String>", collectionType.buildCanonicalName());
 }
}