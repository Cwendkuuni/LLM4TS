import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;

 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);

 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(Integer.class, collectionType.getContentType());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemT = Integer.TYPE;

 CollectionType collectionType = CollectionType.construct(listClass, elemT);

 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(Integer.class, collectionType.getContentType());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<String>> arrayListClass = ArrayList.class;
 CollectionType listType = new CollectionType(List.class, null, Object.class, new JavaType[0], String.class);

 CollectionType arrayListType = listType._narrow(arrayListClass);

 assertEquals(ArrayList.class, arrayListType.getRawClass());
 assertEquals(String.class, arrayListType.getContentType());
 assertFalse(arrayListType.isStatic());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowException() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 CollectionType listType = new CollectionType(List.class, null, Object.class, new JavaType[0], String.class);

 listType._narrow(arrayListClass);
 }

 @Test
 public void testWithContentType() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 JavaType contentType = new TypeReference<List<String>>() {}.getType();

 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
 JavaType newCollectionType = collectionType.withContentType(contentType);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(String.class, newCollectionType.getContentType());
 assertFalse(newCollectionType.isStatic());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object typeHandler = new Object() {
 @Override
 public JavaType resolveType(TypeBindings bindings, JavaType baseType) {
 return new TypeReference<List<String>>() {}.getType();
 }
 };

 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
 JavaType newCollectionType = collectionType.withTypeHandler(typeHandler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(String.class, newCollectionType.getContentType());
 assertFalse(newCollectionType.isStatic());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object valueHandler = new Object() {
 @Override
 public Object getValue(Object jv) {
 return new ArrayList<String>();
 }
 };
 Object typeHandler = new Object() {
 @Override
 public JavaType resolveType(TypeBindings bindings, JavaType baseType) {
 return new TypeReference<List<String>>() {}.getType();
 }
 };

 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
 JavaType newCollectionType = collectionType.withContentTypeHandler(typeHandler);
 JavaType newCollectionTypeWithValueHandler = newCollectionType.withValueHandler(valueHandler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(String.class, newCollectionType.getContentType());
 assertTrue(newCollectionType.isStatic());
 List<String> list = newCollectionTypeWithValueHandler.getValue(null);
 assertTrue(list instanceof ArrayList);
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object valueHandler = new Object() {
 @Override
 public Object getValue(Object jv) {
 return new ArrayList<String>();
 }
 };

 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
 JavaType newCollectionType = collectionType.withValueHandler(valueHandler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(String.class, newCollectionType.getContentType());
 assertTrue(newCollectionType.isStatic());
 List<String> list = newCollectionType.getValueHandler().getValue(null);
 assertTrue(list instanceof ArrayList);
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object valueHandler = new Object() {
 @Override
 public Object getValue(Object jv) {
 return new ArrayList<String>();
 }
 };
 Object contentValueHandler = new Object() {
 @Override
 public Object getValue(Object jv) {
 return Arrays.asList(new String[]{"foo", "bar"});
 }
 };

 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
 JavaType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
 JavaType newCollectionTypeWithValueHandler = newCollectionType.withValueHandler(valueHandler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(String[].class, newCollectionType.getContentType());
 assertTrue(newCollectionType.isStatic());
 List<List<String>> lists = newCollectionTypeWithValueHandler.getValueHandler().getValue(null);
 for (List<String> list : lists) {
 assertEquals(2, list.size());
 assertTrue(list.containsAll(Arrays.asList("foo", "bar")));
 }
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;

 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
 CollectionType newCollectionType = collectionType.withStaticTyping();

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(Integer.class, newCollectionType.getContentType());
 assertTrue(newCollectionType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<List<Integer>> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;

 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
 JavaType newCollectionType = collectionType.refine(List.class, bindings, superClass, superInts);

 assertEquals(ArrayList.class, newCollectionType.getRawClass());
 assertEquals(Integer.class, newCollectionType.getContentType());
 assertFalse(newCollectionType.isStatic());
 }
}