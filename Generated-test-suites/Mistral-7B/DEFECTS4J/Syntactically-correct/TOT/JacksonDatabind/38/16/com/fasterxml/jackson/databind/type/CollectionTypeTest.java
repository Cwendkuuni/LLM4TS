import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 assertEquals(listClass, collectionType.getRawClass());
 assertNotNull(collectionType.getSuperClass());
 assertNotNull(collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getComponentType());
 }

 @Test
 public void testConstructWithDeprecatedArguments() {
 Class<?> listClass = ArrayList.class;
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 assertEquals(listClass, collectionType.getRawClass());
 assertNull(collectionType.getSuperClass());
 assertNull(collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getComponentType());
 }

 @Test
 public void testNarrowMethod() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = SubList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, _bogusSuperClass(listClass), null, Integer.TYPE);
 CollectionType narrowedType = collectionType.narrow(subListClass);
 assertEquals(subListClass, narrowedType.getRawClass());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowMethodWithInvalidArgument() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, _bogusSuperClass(listClass), null, Integer.TYPE);
 collectionType.narrow(String.class);
 }

 @Test
 public void testWithContentTypeMethod() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = Integer.TYPE;
 JavaType stringType = String.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newCollectionType = collectionType.withContentType(stringType);
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotNull(newCollectionType.getSuperClass());
 assertNotNull(newCollectionType.getSuperInterfaces());
 assertEquals(stringType, newCollectionType.getComponentType());
 }

 @Test
 public void testWithTypeHandlerMethod() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = Integer.TYPE;
 Object intHandler = new IntTypeHandler();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newCollectionType = collectionType.withTypeHandler(intHandler);
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotNull(newCollectionType.getSuperClass());
 assertNotNull(newCollectionType.getSuperInterfaces());
 assertEquals(elemT, newCollectionType.getComponentType());
 assertSame(intHandler, newCollectionType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandlerMethod() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = Integer.TYPE;
 Object intHandler = new IntTypeHandler();
 JavaType stringType = String.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newCollectionType = collectionType.withContentTypeHandler(intHandler);
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotNull(newCollectionType.getSuperClass());
 assertNotNull(newCollectionType.getSuperInterfaces());
 assertEquals(elemT.withTypeHandler(intHandler), newCollectionType.getComponentType());
 }

 @Test
 public void testWithValueHandlerMethod() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = Integer.TYPE;
 Object intHandler = new IntTypeHandler();
 Object valueHandler = new ValueTypeHandler();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotNull(newCollectionType.getSuperClass());
 assertNotNull(newCollectionType.getSuperInterfaces());
 assertNotNull(newCollectionType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandlerMethod() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = Integer.TYPE;
 Object intHandler = new IntTypeHandler();
 Object valueHandler = new ValueTypeHandler();
 JavaType stringType = String.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler);
 assertEquals(listClass, newCollectionType.getRawClass());
 assertNotNull(newCollectionType.getSuperClass());
 assertNotNull(newCollectionType.getSuperInterfaces());
 assertEquals(elemT.withValueHandler(valueHandler), newCollectionType.getComponentType());
 assertSame(valueHandler, newCollectionType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTypingMethod() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, _bogusSuperClass(listClass), null, Integer.TYPE);
 CollectionType staticTypedCollectionType = collectionType.withStaticTyping();
 assertTrue(staticTypedCollectionType.isStatic());
 }

 @Test
 public void testRefineMethod() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType refinedCollectionType = collectionType.refine(LinkedList.class, bindings, superClass, superInterfaces);
 assertEquals(LinkedList.class, refinedCollectionType.getRawClass());
 }
}