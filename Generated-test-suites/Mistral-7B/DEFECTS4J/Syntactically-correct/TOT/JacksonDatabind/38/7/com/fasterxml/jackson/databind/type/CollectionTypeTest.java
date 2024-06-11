import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerBinding;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerBase;
import org.junit.Test;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType collectionType = CollectionType.construct(rawType, bindings, null, null, elemType);
 assertEquals(rawType, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 JavaType collectionType = CollectionType.construct(rawType, elemType);
 assertEquals(rawType, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testNarrow() {
 Class<?> rawType = ArrayList.class;
 Class<?> subClass = LinkedList.class;
 JavaType elemType = String.class;
 JavaType collectionType = CollectionType.construct(rawType, elemType);
 JavaType narrowedType = collectionType._narrow(subClass);
 assertEquals(LinkedList.class, narrowedType.getRawClass());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowThrowsException() {
 Class<?> rawType = ArrayList.class;
 Class<?> subClass = Object.class;
 JavaType elemType = String.class;
 JavaType collectionType = CollectionType.construct(rawType, elemType);
 JavaType narrowedType = collectionType._narrow(subClass);
 }

 @Test
 public void testWithContentType() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 JavaType contentType = Integer.class;
 JavaType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withContentType(contentType);
 assertEquals(rawType, newCollectionType.getRawClass());
 assertEquals(contentType, newCollectionType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 TypeSerializerBinding handlerBinding = new TypeSerializerBinding(String.class, null, null);
 JavaType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withTypeHandler(handlerBinding);
 assertInstanceOf(TypeDeserializer.class, newCollectionType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 TypeSerializer handler = new TypeSerializerBase() {};
 JavaType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withContentTypeHandler(handler);
 assertInstanceof(TypeDeserializerBase.class, newCollectionType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 Object valueHandler = new Object() {};
 JavaType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withValueHandler(valueHandler);
 assertEquals(valueHandler, newCollectionType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 Object contentValueHandler = new Object() {};
 JavaType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
 assertEquals(contentValueHandler, newCollectionType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 JavaType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withStaticTyping();
 assertTrue(newCollectionType._asStatic);
 }

 @Test
 public void testRefine() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = List.class;
 JavaType[] superInterfaces = {Collection.class};
 JavaType collectionType = CollectionType.construct(rawType, elemType);
 JavaType newCollectionType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(rawType, newCollectionType.getRawClass());
 assertEquals(elemType, newCollectionType.getContentType());
 assertEquals(superClass, newCollectionType.getSuperclass());
 assertArrayEquals(new Class<?>[]{Collection.class}, newCollectionType.getSuperInterfaces());
 }

 @Test
 public void testToString() {
 Class<?> rawType = ArrayList.class;
 JavaType elemType = String.class;
 JavaType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
 assertEquals("[collection type; class "+rawType.getName()+", contains "+elemType+"]", collectionType.toString());
 }
}