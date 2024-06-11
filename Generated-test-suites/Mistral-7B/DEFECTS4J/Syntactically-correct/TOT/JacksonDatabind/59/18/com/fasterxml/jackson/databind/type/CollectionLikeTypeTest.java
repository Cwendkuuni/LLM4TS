import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructWithCollectionType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.Collection.class, null, null, null, JavaType.constructCollectionType(Collection.class, String.class));
 assertEquals(Collection.class, collectionLikeType.getRawClass());
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testConstructWithTypeVariable() {
 TypeVariable<?> typeVariable = new TypeVariable<Collection<?>>() {}.setName("T");
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, null, null, null, JavaType.constructTypeVariable(TypeVariable.class, "T"));
 assertEquals(List.class, collectionLikeType.getRawClass());
 assertEquals(TypeVariable.class, collectionLikeType.getTypeVariables()[0]);
 }

 @Test
 public void testUpgradeFromSimpleType() {
 JavaType javaType = JavaType.constructType(int[].class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, JavaType.constructCollectionType(ArrayList.class, Integer.class));
 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(Integer.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromWrongType() {
 JavaType javaType = JavaType.constructType(String.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, JavaType.constructCollectionType(ArrayList.class, Integer.class));
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testNarrowType() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType narrowType = collectionLikeType.narrow(LinkedList.class);
 assertEquals(LinkedList.class, narrowType.getRawClass());
 }

 @Test
 public void testWithContentType() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType withContentType = collectionLikeType.withContentType(JavaType.constructCollectionType(LinkedHashSet.class, Integer.class));
 assertEquals(LinkedHashSet.class, withContentType.getRawClass());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType withTypeHandler = collectionLikeType.withTypeHandler(new CollectionTypeHandler());
 assertNotNull(withTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType withContentTypeHandler = collectionLikeType.withContentTypeHandler(new CollectionValueHandler());
 assertNotNull(withContentTypeHandler.getValueHandler());
 }

 @Test
 public void testWithValueHandler() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType withValueHandler = collectionLikeType.withValueHandler(new CollectionValueHandler());
 assertNotNull(withValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType withContentValueHandler = collectionLikeType.withContentValueHandler(new CollectionValueHandler());
 assertNotNull(withContentValueHandler.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType withStaticTyping = collectionLikeType.withStaticTyping();
 assertTrue(withStaticTyping.isStatic());
 }

 @Test
 public void testRefine() {
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, JavaType.constructCollectionType(LinkedList.class, Integer.class), null, null, false);
 CollectionLikeType refinedType = collectionLikeType.refine(LinkedList.class, null, null, null);
 assertEquals(LinkedList.class, refinedType.getRawClass());
 }

 @Test
 public void testIsContainerType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertEquals(JavaType.constructCollectionType(ArrayList.class, String.class), collectionLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertEquals(null, collectionLikeType.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertTrue(collectionLikeType.hasHandlers());
 }

 @Test
 public void testGetErasedSignature() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertEquals("[collection-like type; class java.util.ArrayList, contains class [Ljava.lang.Object;]", collectionLikeType.getErasedSignature(new StringBuilder()).toString());
 }

 @Test
 public void testGetGenericSignature() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertEquals("[collection-like type; class java.util.ArrayList<T>, contains class java.lang.String]", collectionLikeType.getGenericSignature(new StringBuilder()).toString());
 }

 @Test
 public void testBuildCanonicalName() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.String]", collectionLikeType.buildCanonicalName());
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class), null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }
}