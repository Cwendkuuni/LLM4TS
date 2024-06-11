import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 assertNotNull(collectionType);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInterfaces, collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test
 public void testConstructWithDeprecatedArguments() {
 Class<?> listClass = new ArrayList<String>().getClass();
 JavaType elemT = String.class;
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 assertNotNull(collectionType);
 assertEquals(listClass, collectionType.getRawClass());
 assertNull(collectionType.getTypeBindings());
 assertNull(collectionType.getSuperClass());
 assertNull(collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidSuperClass() {
 Class<?> listClass = new ArrayList<String>().getClass();
 JavaType elemT = String.class;
 JavaType bogusSuperClass = Integer.class;
 CollectionType collectionType = new CollectionType(listClass, null, bogusSuperClass, null, elemT);
 fail("Should not be able to instantiate CollectionType with invalid superclass");
 }

 @Test
 public void testNarrowWithValidSubclass() {
 Class<?> listClass = ArrayList.class;
 Class<?> arrayListSubclass = ArrayList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, _bogusSuperClass(listClass), null, String.class);
 CollectionType narrowedCollectionType = collectionType.narrow(arrayListSubclass);
 assertNotNull(narrowedCollectionType);
 assertEquals(arrayListSubclass, narrowedCollectionType.getRawClass());
 assertEquals(collectionType.getTypeBindings(), narrowedCollectionType.getTypeBindings());
 assertEquals(collectionType.getSuperClass(), narrowedCollectionType.getSuperClass());
 assertEquals(collectionType.getSuperInterfaces(), narrowedCollectionType.getSuperInterfaces());
 assertEquals(collectionType.getElementType(), narrowedCollectionType.getElementType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidSubclass() {
 Class<?> listClass = ArrayList.class;
 Class<?> intClass = Integer.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, _bogusSuperClass(listClass), null, String.class);
 CollectionType narrowedCollectionType = collectionType.narrow(intClass);
 fail("Should not be able to instantiate CollectionType with invalid subclass");
 }

 @Test
 public void testWithContentTypeWithValidContentType() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 JavaType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 JavaType contentType = String[].class;
 CollectionType newStringListType = stringListType.withContentType(contentType);
 assertNotNull(newStringListType);
 assertEquals(listClass, newStringListType.getRawClass());
 assertEquals(bindings, newStringListType.getTypeBindings());
 assertEquals(superClass, newStringListType.getSuperClass());
 assertEquals(superInterfaces, newStringListType.getSuperInterfaces());
 assertEquals(contentType, newStringListType.getElementType());
 }

 @Test
 public void testWithContentTypeWithInvalidContentType() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 JavaType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 JavaType invalidContentType = Number[].class;
 CollectionType newStringListType = stringListType.withContentType(invalidContentType);
 fail("Should not be able to instantiate CollectionType with invalid content type");
 }

 @Test
 public void testWithTypeHandlerWithValidTypeHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object stringHandler = new StringHandler();
 CollectionType newStringListType = stringListType.withTypeHandler(stringHandler);
 assertNotNull(newStringListType);
 assertEquals(listClass, newStringListType.getRawClass());
 assertEquals(bindings, newStringListType.getTypeBindings());
 assertEquals(superClass, newStringListType.getSuperClass());
 assertEquals(superInterfaces, newStringListType.getSuperInterfaces());
 assertEquals(elemT, newStringListType.getElementType());
 assertEquals(stringHandler, newStringListType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandlerWithValidContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object stringHandler = new StringHandler();
 JavaType stringArrayType = JavaType.constructArrayType(String.class);
 CollectionType newStringListType = stringListType.withContentTypeHandler(stringHandler);
 CollectionType newStringListTypeWithContentType = newStringListType.withContentType(stringArrayType);
 assertNotNull(newStringListTypeWithContentType);
 assertEquals(listClass, newStringListTypeWithContentType.getRawClass());
 assertEquals(bindings, newStringListTypeWithContentType.getTypeBindings());
 assertEquals(superClass, newStringListTypeWithContentType.getSuperClass());
 assertEquals(superInterfaces, newStringListTypeWithContentType.getSuperInterfaces());
 assertEquals(stringArrayType, newStringListTypeWithContentType.getElementType());
 assertEquals(stringHandler, newStringListTypeWithContentType.getTypeHandler());
 }

 @Test
 public void testWithValueHandlerWithValidValueHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object valueHandler = new ValueHandler();
 CollectionType newStringListType = stringListType.withValueHandler(valueHandler);
 assertNotNull(newStringListType);
 assertEquals(listClass, newStringListType.getRawClass());
 assertEquals(bindings, newStringListType.getTypeBindings());
 assertEquals(superClass, newStringListType.getSuperClass());
 assertEquals(superInterfaces, newStringListType.getSuperInterfaces());
 assertEquals(elemT, newStringListType.getElementType());
 assertEquals(valueHandler, newStringListType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandlerWithValidContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object valueHandler = new ValueHandler();
 JavaType stringArrayType = JavaType.constructArrayType(String.class);
 CollectionType newStringListType = stringListType.withContentValueHandler(valueHandler);
 CollectionType newStringListTypeWithContentType = newStringListType.withContentType(stringArrayType);
 assertNotNull(newStringListTypeWithContentType);
 assertEquals(listClass, newStringListTypeWithContentType.getRawClass());
 assertEquals(bindings, newStringListTypeWithContentType.getTypeBindings());
 assertEquals(superClass, newStringListTypeWithContentType.getSuperClass());
 assertEquals(superInterfaces, newStringListTypeWithContentType.getSuperInterfaces());
 assertEquals(stringArrayType, newStringListTypeWithContentType.getElementType());
 assertEquals(valueHandler, newStringListTypeWithContentType.getValueHandler());
 }

 @Test
 public void testWithStaticTypingWithFalse() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newStringListType = stringListType.withStaticTyping();
 assertFalse(newStringListType.isStaticTyping());
 }

 @Test
 public void testWithStaticTypingWithTrue() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newStringListType = stringListType.withStaticTyping();
 CollectionType newStringListTypeWithStaticTyping = newStringListType.withStaticTyping();
 assertTrue(newStringListTypeWithStaticTyping.isStaticTyping());
 }

 @Test
 public void testRefineWithValidArguments() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Class<?> stringListSubclass = ArrayList.class;
 TypeBindings bindingsSubclass = new TypeBindings();
 JavaType[] superInterfacesSubclass = new JavaType[0];
 CollectionType newStringListType = stringListType.refine(stringListSubclass, bindingsSubclass, superClass, superInterfaces);
 assertNotNull(newStringListType);
 assertEquals(stringListSubclass, newStringListType.getRawClass());
 assertEquals(bindingsSubclass, newStringListType.getTypeBindings());
 assertEquals(superClass, newStringListType.getSuperClass());
 assertEquals(superInterfaces, newStringListType.getSuperInterfaces());
 assertEquals(elemT, newStringListType.getElementType());
 }

 @Test
 public void testToString() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = String.class;
 CollectionType stringListType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 String toStringResult = stringListType.toString();
 String expectedResult = "[collection type; class " + listClass.getName() + ", contains " + elemT + "]";
 assertEquals(expectedResult, toStringResult);
 }
}