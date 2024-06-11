import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = SimpleType.construct(int.class);
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType collectionType = CollectionType.construct(listClass, bindings, null, null, elemType);
 assertNotNull(collectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, collectionType.getRawClass().getModifiers());
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testDeprecatedConstruct() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = SimpleType.construct(int.class);
 JavaType collectionType = CollectionType.construct(listClass, elemType);
 assertNotNull(collectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, collectionType.getRawClass().getModifiers());
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testRefine() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = SimpleType.construct(int.class);
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = ClassUtil.findConcreteClass(List.class, bindings);
 JavaType[] superInterfaces = new JavaType[]{Collection.class};
 JavaType collectionType = CollectionType.construct(listClass, bindings, null, superInterfaces, elemType).refine(listClass, bindings, superClass, superInterfaces);
 assertNotNull(collectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, collectionType.getRawClass().getModifiers());
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInterfaces, collectionType.getSuperInterfaces());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testNarrow() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = ArrayList.class;
 JavaType elemType = SimpleType.construct(int.class);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 JavaType narrowedCollectionType = collectionType.narrow(subListClass);
 assertNotNull(narrowedCollectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, narrowedCollectionType.getRawClass().getModifiers());
 assertEquals(subListClass, narrowedCollectionType.getRawClass());
 assertEquals(elemType, narrowedCollectionType.getContentType());
 }

 @Test
 public void testWithContentType() {
 Class<?> listClass = ArrayList.class;
 Class<?> elemClass = Integer.class;
 JavaType elemType = SimpleType.construct(elemClass);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withContentType(SimpleType.construct(String.class));
 assertNotNull(newCollectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, newCollectionType.getRawClass().getModifiers());
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotEquals(elemType, newCollectionType.getContentType());
 assertEquals(SimpleType.construct(String.class), newCollectionType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = SimpleType.construct(int.class);
 JavaType handler = SimpleType.construct(IntegerStringSerializer.class);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withTypeHandler(handler);
 assertNotNull(newCollectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, newCollectionType.getRawClass().getModifiers());
 assertEquals(listClass, newCollectionType.getRawClass());
 assertEquals(elemType, newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 Class<?> elemClass = Integer.class;
 JavaType elemType = SimpleType.construct(elemClass);
 JavaType handler = SimpleType.construct(IntegerStringSerializer.class);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withContentTypeHandler(handler);
 assertNotNull(newCollectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, newCollectionType.getRawClass().getModifiers());
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotEquals(elemType, newCollectionType.getContentType());
 assertEquals(SimpleType.construct(IntegerStringSerializer.class), newCollectionType.getContentTypeHandler());
 assertEquals(handler, newCollectionType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = SimpleType.construct(int.class);
 JavaType handler = SimpleType.construct(IntegerDeserializer.class);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withValueHandler(handler);
 assertNotNull(newCollectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, newCollectionType.getRawClass().getModifiers());
 assertEquals(listClass, newCollectionType.getRawClass());
 assertEquals(elemType, newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 Class<?> elemClass = Integer.class;
 JavaType elemType = SimpleType.construct(elemClass);
 JavaType handler = SimpleType.construct(IntegerDeserializer.class);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withContentValueHandler(handler);
 assertNotNull(newCollectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, newCollectionType.getRawClass().getModifiers());
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotEquals(elemType, newCollectionType.getContentType());
 assertEquals(SimpleType.construct(IntegerDeserializer.class), newCollectionType.getContentValueHandler());
 assertEquals(handler, newCollectionType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = SimpleType.construct(int.class);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 JavaType newCollectionType = collectionType.withStaticTyping();
 assertNotNull(newCollectionType);
 assertEquals(JavaType.TypeFlags.COLLECTION, newCollectionType.getRawClass().getModifiers());
 assertEquals(listClass, newCollectionType.getRawClass());
 assertTrue(newCollectionType.isStatic());
 }

}