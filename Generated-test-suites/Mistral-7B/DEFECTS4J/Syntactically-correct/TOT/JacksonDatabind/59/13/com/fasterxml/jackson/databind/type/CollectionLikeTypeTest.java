import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructWithCollectionType() {
 Class<?> collectionType = java.util.ArrayList.class;
 JavaType javaType = CollectionLikeType.construct(collectionType, null, null, null, null);
 CollectionLikeType actual = (CollectionLikeType) javaType;
 JavaType expectedElementType = JavaType.constructCollectionType(collectionType, String.class);
 assertEquals(expectedElementType.getTypeId(), actual._elementType.getTypeId());
 }

 @Test
 public void testConstructWithJavaType() {
 JavaType listType = JavaType.constructCollectionType(java.util.ArrayList.class, String.class);
 JavaType javaType = CollectionLikeType.construct(null, null, null, null, listType);
 CollectionLikeType actual = (CollectionLikeType) javaType;
 assertEquals(listType.getTypeId(), actual._elementType.getTypeId());
 }

 @Test
 public void testUpgradeFromBasicType() {
 JavaType javaType = JavaType.constructRawType(java.util.List.class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 assertEquals(javaType.getTypeId(), collectionLikeType.getBaseType().getTypeId());
 JavaType expectedElementType = JavaType.constructCollectionType(java.util.List.class, String.class);
 assertEquals(expectedElementType.getTypeId(), collectionLikeType._elementType.getTypeId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromWrongBaseType() {
 JavaType javaType = JavaType.constructRawType(java.lang.String.class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testNarrow() {
 JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructRawType(Number.class), null, null, false);
 JavaType narrowedType = javaType._narrow(java.util.LinkedList.class);
 assertEquals(javaType.getTypeId(), narrowedType.getTypeId());
 }

 @Test
 public void testWithContentType() {
 JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, null, null, null, false);
 JavaType withContentType = javaType.withContentType(JavaType.constructCollectionType(java.util.HashSet.class, String.class));
 assertEquals(javaType.getTypeId() + ".HashSet<String>", withContentType.getTypeId());
 }

 @Test
 public void testIsContainerType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, null, null, null, false);
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, null, null, null, false);
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(java.util.LinkedList.class, String.class), null, null, false);
 JavaType actualElementType = collectionLikeType.getContentType();
 JavaType expectedElementType = JavaType.constructCollectionType(java.util.LinkedList.class, String.class);
 assertEquals(expectedElementType.getTypeId(), actualElementType.getTypeId());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(java.util.LinkedList.class, String.class), null, null, false);
 assertTrue(collectionLikeType.hasHandlers());
 }

 @Test
 public void testGetErasedSignature() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(java.util.LinkedList.class, String.class), null, null, false);
 StringBuilder expected = new StringBuilder();
 expected.append("java.util.List<java.lang.String>");
 StringBuilder actual = collectionLikeType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(java.util.LinkedList.class, String.class), null, null, false);
 StringBuilder expected = new StringBuilder();
 expected.append("<java.util.List<java.lang.String>>");
 StringBuilder actual = new StringBuilder();
 collectionLikeType.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testBuildCanonicalName() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(java.util.LinkedList.class, String.class), null, null, false);
 String expected = "java.util.ArrayList<java.util.LinkedList<java.lang.String>>";
 String actual = collectionLikeType.buildCanonicalName();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(java.util.LinkedList.class, String.class), null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }
}