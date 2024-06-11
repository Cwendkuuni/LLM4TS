import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 assertNull(collectionType.getSuperClass());
 assertArrayEquals(new JavaType[0], collectionType.getSuperTypes());
 }

 @Test
 public void testConstructWithBindings() {
 TypeBindings bindings = new TypeBindings();
 Class<List<String>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionType = CollectionType.construct(listClass, bindings, null, null, elemType);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 assertNotNull(collectionType.getBindings());
 assertNull(collectionType.getSuperClass());
 assertArrayEquals(new JavaType[0], collectionType.getSuperTypes());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType collectionType = CollectionType.construct(listClass, elemType);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 assertNull(collectionType.getSuperClass());
 assertArrayEquals(new JavaType[0], collectionType.getSuperTypes());
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType narrowedType = listType.narrow(arrayListClass);
 assertEquals(ArrayList.class, narrowedType.getRawClass());
 assertEquals(listType.getContentType(), narrowedType.getContentType());
 assertNull(narrowedType.getSuperClass());
 assertArrayEquals(new JavaType[0], narrowedType.getSuperTypes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowUnsupported() {
 Class<String[]> arrayClass = String[].class;
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 listType.narrow(arrayClass);
 }

 @Test
 public void testWithContentType() {
 Class<List<String>> listClass = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType newListType = listType.withContentType(elemType);
 assertEquals(listClass, newListType.getRawClass());
 assertEquals(elemType, newListType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(listClass);
 Object handler = new Object();
 JavaType newListType = listType.withTypeHandler(handler);
 assertEquals(listClass, newListType.getRawClass());
 assertEquals(listType.getContentType(), newListType.getContentType());
 assertEquals(handler, newListType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(listClass);
 Object contentHandler = new Object();
 Object handler = new Object();
 JavaType newListType = listType.withContentTypeHandler(contentHandler).withTypeHandler(handler);
 assertEquals(listClass, newListType.getRawClass());
 assertEquals(listType.getContentType(), newListType.getContentType());
 assertEquals(contentHandler, newListType.getContentTypeHandler());
 assertEquals(handler, newListType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(listClass);
 Object handler = new Object();
 JavaType newListType = listType.withValueHandler(handler);
 assertEquals(listClass, newListType.getRawClass());
 assertEquals(listType.getContentType(), newListType.getContentType());
 assertEquals(handler, newListType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(listClass);
 Object contentHandler = new Object();
 Object handler = new Object();
 JavaType newListType = listType.withContentValueHandler(contentHandler).withValueHandler(handler);
 assertEquals(listClass, newListType.getRawClass());
 assertEquals(listType.getContentType(), newListType.getContentType());
 assertEquals(contentHandler, newListType.getContentValueHandler());
 assertEquals(handler, newListType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType staticListType = listType.withStaticTyping();
 assertEquals(listClass, staticListType.getRawClass());
 assertEquals(listType.getContentType(), staticListType.getContentType());
 assertTrue(staticListType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<String>> arrayListClass = ArrayList.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType refinedType = listType.refine(arrayListClass.getComponentType(), null, null, null);
 assertEquals(ArrayList.class, refinedType.getRawClass());
 assertEquals(listType.getContentType(), refinedType.getContentType());
 }

 @Test
 public void testToString() {
 Class<List<String>> listClass = List.class;
 JavaType listType = TypeFactory.defaultInstance().constructType(listClass);
 String result = listType.toString();
 assertTrue(result.contains("collection type; class " + listClass.getName() + ", contains "));
 }
}