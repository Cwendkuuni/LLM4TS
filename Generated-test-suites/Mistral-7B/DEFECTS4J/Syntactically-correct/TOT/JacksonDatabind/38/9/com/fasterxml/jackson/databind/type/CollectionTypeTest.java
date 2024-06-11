import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 assertNotNull(collectionType);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getBindings());
 assertEquals(superClass, collectionType.getSuperclass());
 assertArrayEquals(superInts, collectionType.getSuperinterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test
 public void testConstructDeprecatedWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 assertNotNull(collectionType);
 assertEquals(listClass, collectionType.getRawClass());
 assertNull(collectionType.getBindings());
 assertNull(collectionType.getSuperclass());
 assertNull(collectionType.getSuperinterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidArguments() {
 Class<?> listClass = ArrayList.class;
 JavaType elemT = null;
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testRefineWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] { List.class };
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = new CollectionType(listClass, bindings);
 JavaType refinedType = collectionType.refine(listClass, bindings, superClass, superInterfaces);
 assertSame(collectionType, refinedType);
 }

 @Test
 public void testRefineWithDifferentArguments() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = HashSet.class;
 JavaType[] superInterfaces = new JavaType[] { Collection.class };
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = new CollectionType(listClass, bindings);
 JavaType refinedType = collectionType.refine(listClass, bindings, superClass, superInterfaces);
 assertNotSame(collectionType, refinedType);
 }

 @Test
 public void testWithContentTypeWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 JavaType collectionType = new CollectionType(listClass, null);
 JavaType elemT = Long.TYPE;
 CollectionType newCollectionType = collectionType.withContentType(elemT);
 assertNotSame(collectionType, newCollectionType);
 assertEquals(elemT, newCollectionType.getElementType());
 }

 @Test
 public void testWithContentTypeHandlerWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = new CollectionType(listClass, null);
 Object handler = new Object();
 CollectionType newCollectionType = collectionType.withContentTypeHandler(handler);
 assertNotSame(collectionType, newCollectionType);
 assertSame(handler, newCollectionType.getContentTypeHandler());
 }

 @Test
 public void testWithTypeHandlerWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = new CollectionType(listClass, null);
 Object handler = new Object();
 CollectionType newCollectionType = collectionType.withTypeHandler(handler);
 assertNotSame(collectionType, newCollectionType);
 assertSame(handler, newCollectionType.getTypeHandler());
 }

 @Test
 public void testWithStaticTypingWithStaticTypingFalse() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = new CollectionType(listClass, null);
 CollectionType newCollectionType = collectionType.withStaticTyping();
 assertFalse(newCollectionType.isStaticTyping());
 }

 @Test
 public void testWithStaticTypingWithStaticTypingTrue() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = new CollectionType(listClass, null);
 CollectionType newCollectionType = collectionType.withStaticTyping();
 assertTrue(newCollectionType.isStaticTyping());
 }
}