import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object[].class);
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType listType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);

 assertEquals(listClass, listType.getRawClass());
 assertEquals(bindings, listType.getTypeBindings());
 assertEquals(superClass, listType.getCollectionClass());
 assertArrayEquals(superInts, listType.getSuperTypes());
 assertEquals(elemT, listType.getElementType());
 assertNull(listType.getValueHandler());
 assertNull(listType.getTypeHandler());
 assertFalse(listType.isStatic());
 }

 @Test
 public void testConstructWithDeprecatedMethod() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType listType = CollectionType.construct(listClass, elemT);

 assertEquals(listClass, listType.getRawClass());
 assertNull(listType.getTypeBindings());
 // !!! TODO: This is wrong, the superclass and superinterfaces are not null
 assertNull(listType.getCollectionClass());
 assertArrayIsNull(listType.getSuperTypes());
 assertEquals(elemT, listType.getElementType());
 assertNull(listType.getValueHandler());
 assertNull(listType.getTypeHandler());
 assertFalse(listType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullRawType() {
 CollectionType listType = CollectionType.construct(null, null, null, null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullBindings() {
 CollectionType listType = CollectionType.construct(List.class, null, null, null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullSuperClass() {
 CollectionType listType = CollectionType.construct(List.class, null, null, null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullSuperInterfaces() {
 CollectionType listType = CollectionType.construct(List.class, null, null, null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullElementType() {
 CollectionType listType = CollectionType.construct(List.class, null, null, null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 CollectionType listType = CollectionType.construct(List.class, null, null, null, JavaType.construct(Integer.class));
 CollectionType arrayListType = listType.narrow(arrayListClass);

 assertEquals(arrayListClass, arrayListType.getRawClass());
 assertSame(listType.getRawClass(), arrayListType.getRawBaseType());
 assertSame(arrayListType, listType.withType(arrayListClass));
 }

 @Test(expected = ClassCastException.class)
 public void testNarrowWithDifferentClass() {
 CollectionType listType = CollectionType.construct(List.class, null, null, null, JavaType.construct(Integer.class));
 CollectionType stringListType = listType.narrow(String.class);
 fail("Should throw ClassCastException");
 }

 @Test
 public void testWithContentType() {
 Class<List<String>> listClass = List.class;
 JavaType stringType = JavaType.construct(String.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, JavaType.construct(Integer.class));
 CollectionType stringListType = listType.withContentType(stringType);

 assertEquals(listClass, stringListType.getRawClass());
 assertEquals(stringType, stringListType.getElementType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 CollectionType listType = CollectionType.construct(listClass, null, null, null, JavaType.construct(Integer.class));
 CollectionType listTypeWithHandler = listType.withTypeHandler(new Object());

 assertSame(listType, listTypeWithHandler); // The testTypeHandler is not used
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType intTypeHandler = JavaType.construct(IntegerHandler.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, JavaType.construct(Integer.class));
 CollectionType listTypeWithHandler = listType.withContentTypeHandler(intTypeHandler);

 assertSame(listType, listTypeWithHandler); // The testTypeHandler is not used
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = List.class;
 CollectionType listType = CollectionType.construct(listClass, null, null, null, JavaType.construct(Integer.class));
 CollectionType listTypeWithHandler = listType.withValueHandler(new Object());

 assertSame(listType, listTypeWithHandler); // The testValueHandler is not used
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType intValueHandler = JavaType.construct(IntegerValueHandler.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, JavaType.construct(Integer.class));
 CollectionType listTypeWithHandler = listType.withContentValueHandler(intValueHandler);

 assertSame(listType, listTypeWithHandler); // The testValueHandler is not used
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = List.class;
 CollectionType listType = CollectionType.construct(listClass, null, null, null, JavaType.construct(Integer.class));
 CollectionType listTypeStatic = listType.withStaticTyping();

 assertTrue(listTypeStatic.isStatic());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 CollectionType listType = CollectionType.construct(List.class, bindings, null, null, JavaType.construct(Integer.class));
 CollectionType arrayListType = listType.refine(arrayListClass, bindings, null, null);

 assertEquals(arrayListClass, arrayListType.getRawClass());
 }

}