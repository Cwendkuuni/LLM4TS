import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructFromClassAndTypeBindings() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemType);

 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test
 public void testConstructFromClassAndElementType() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
 JavaType collectionLikeType = CollectionLikeType.construct(rawType, elemType);

 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test
 public void testUpgradeFromSimpleType() {
 JavaType baseType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);

 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
 }

 @Test
 public void testNarrowType() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 JavaType narrowedType = collectionLikeType.narrow(LinkedList.class);

 assertEquals(LinkedList.class, narrowedType.getRawClass());
 assertEquals(collectionLikeType.getContentType(), narrowedType.getContentType());
 }

 @Test
 public void testWithContentType() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 JavaType newContentType = TypeFactory.defaultInstance().constructType(int[].class);
 JavaType newCollectionLikeType = collectionLikeType.withContentType(newContentType);

 assertEquals(ArrayList.class, newCollectionLikeType.getRawClass());
 assertEquals(newContentType, newCollectionLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithContentTypeInvalidType() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 JavaType invalidContentType = new Object() {};
 JavaType newCollectionLikeType = collectionLikeType.withContentType(invalidContentType);
 }

 @Test
 public void testWithTypeHandler() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 Object handler = new Object() {};
 JavaType newCollectionLikeType = collectionLikeType.withTypeHandler(handler);

 assertEquals(ArrayList.class, newCollectionLikeType.getRawClass());
 assertNotNull(newCollectionLikeType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 Object handler = new Object() {};
 JavaType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);

 assertEquals(ArrayList.class, newCollectionLikeType.getRawClass());
 assertNotNull(newCollectionLikeType.getTypeHandler());
 assertEquals(handler, newCollectionLikeType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 Object handler = new Object() {};
 JavaType newCollectionLikeType = collectionLikeType.withValueHandler(handler);

 assertEquals(ArrayList.class, newCollectionLikeType.getRawClass());
 assertNotNull(newCollectionLikeType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 Object handler = new Object() {};
 JavaType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);

 assertEquals(ArrayList.class, newCollectionLikeType.getRawClass());
 assertNotNull(newCollectionLikeType.getValueHandler());
 assertEquals(handler, newCollectionLikeType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 JavaType newCollectionLikeType = collectionLikeType.withStaticTyping();

 assertEquals(ArrayList.class, newCollectionLikeType.getRawClass());
 assertTrue(newCollectionLikeType.isStatic());
 }

 @Test
 public void testRefine() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 Class<?> rawType = LinkedList.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType newCollectionLikeType = collectionLikeType.refine(rawType, bindings, null, null);

 assertEquals(LinkedList.class, newCollectionLikeType.getRawClass());
 assertEquals(collectionLikeType.getContentType(), newCollectionLikeType.getContentType());
 }

 @Test
 public void testIsContainerType() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testGetContentValueHandler() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 assertEquals(String.class, collectionLikeType.getContentValueHandler().getClass());
 }

 @Test
 public void testGetContentTypeHandler() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 assertEquals(String.class, collectionLikeType.getContentTypeHandler().getClass());
 }

 @Test
 public void testHasHandlers() {
 JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
 assertTrue(collectionLikeType.hasHandlers());
 }
}