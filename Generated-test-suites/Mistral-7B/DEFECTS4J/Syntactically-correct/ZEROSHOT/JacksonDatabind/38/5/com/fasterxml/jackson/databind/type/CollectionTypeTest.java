import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<String>> listType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 assertNotNull(listType);
 assertEquals(List.class, listType.getRawClass());
 CollectionType collectionType = (CollectionType) listType;
 assertEquals(String.class, collectionType.getContentType());
 }

 @Test
 public void testConstructWithBindings() {
 TypeBindings bindings = new TypeBindings();
 Class<List<String>> listType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class, bindings);
 assertNotNull(listType);
 assertEquals(List.class, listType.getRawClass());
 CollectionType collectionType = (CollectionType) listType;
 assertNotNull(collectionType.getBindings());
 assertEquals(bindings, collectionType.getBindings());
 }

 @Test
 public void testConstructWithSuperClass() {
 Class<List<String>> listType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class, Collection.class);
 assertNotNull(listType);
 assertEquals(List.class, listType.getRawClass());
 CollectionType collectionType = (CollectionType) listType;
 assertEquals(Collection.class, collectionType.getSuperClass());
 }

 @Test
 public void testConstructWithSuperInterfaces() {
 Class<List<String>> listType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class, Collection.class, HashSet.class);
 assertNotNull(listType);
 assertEquals(List.class, listType.getRawClass());
 CollectionType collectionType = (CollectionType) listType;
 JavaType[] superInterfaces = collectionType.getSuperInterfaces();
 assertNotNull(superInterfaces);
 assertEquals(2, superInterfaces.length);
 assertEquals(Collection.class, superInterfaces[0]);
 assertEquals(HashSet.class, superInterfaces[1]);
 }

 @Test
 public void testConstructWithElementType() {
 Class<List<String>> listType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 assertNotNull(listType);
 assertEquals(List.class, listType.getRawClass());
 CollectionType collectionType = (CollectionType) listType;
 assertEquals(String.class, collectionType.getElementType());
 }

 @Test
 public void testConstructWithValueHandler() {
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, stringType);
 CollectionType newCollectionType = collectionType.withValueHandler(new StringValueHandler());
 assertNotSame(collectionType, newCollectionType);
 assertEquals(String.class, newCollectionType.getValueHandler().getClass());
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType arrayListCollectionType = (CollectionType) arrayListType;
 Class<LinkedList<String>> linkedListType = TypeFactory.defaultInstance().constructType(LinkedList.class, String.class);
 CollectionType linkedListCollectionType = (CollectionType) linkedListType;
 CollectionType narrowedType = arrayListCollectionType.narrow(LinkedList.class);
 assertNotSame(arrayListCollectionType, narrowedType);
 assertEquals(LinkedList.class, narrowedType.getRawClass());
 }

 @Test
 public void testWithContentType() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType arrayListCollectionType = (CollectionType) arrayListType;
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 CollectionType newCollectionType = arrayListCollectionType.withContentType(javaType);
 assertNotSame(arrayListCollectionType, newCollectionType);
 assertEquals(Integer.class, newCollectionType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType arrayListCollectionType = (CollectionType) arrayListType;
 StringTypeHandler stringTypeHandler = new StringTypeHandler();
 CollectionType newCollectionType = arrayListCollectionType.withTypeHandler(stringTypeHandler);
 assertNotSame(arrayListCollectionType, newCollectionType);
 assertEquals(stringTypeHandler, newCollectionType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType arrayListCollectionType = (CollectionType) arrayListType;
 StringTypeHandler stringTypeHandler = new StringTypeHandler();
 JavaType stringJavaType = TypeFactory.defaultInstance().constructType(String.class);
 CollectionType newCollectionType = arrayListCollectionType.withContentTypeHandler(stringTypeHandler);
 assertNotSame(arrayListCollectionType, newCollectionType);
 assertEquals(stringJavaType, newCollectionType.getContentType());
 assertEquals(stringTypeHandler, newCollectionType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType arrayListCollectionType = (CollectionType) arrayListType;
 StringValueHandler stringValueHandler = new StringValueHandler();
 CollectionType newCollectionType = arrayListCollectionType.withValueHandler(stringValueHandler);
 assertNotSame(arrayListCollectionType, newCollectionType);
 assertEquals(stringValueHandler, newCollectionType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType arrayListCollectionType = (CollectionType) arrayListType;
 StringValueHandler stringValueHandler = new StringValueHandler();
 JavaType stringJavaType = TypeFactory.defaultInstance().constructType(String.class);
 CollectionType newCollectionType = arrayListCollectionType.withContentValueHandler(stringValueHandler);
 assertNotSame(arrayListCollectionType, newCollectionType);
 assertEquals(stringJavaType, newCollectionType.getContentType());
 assertEquals(stringValueHandler, newCollectionType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType arrayListCollectionType = (CollectionType) arrayListType;
 CollectionType newCollectionType = arrayListCollectionType.withStaticTyping();
 assertNotSame(arrayListCollectionType, newCollectionType);
 assertEquals(true, newCollectionType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<List<String>> listType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 assertNotNull(listType);
 CollectionType collectionType = (CollectionType) listType;
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
 assertNotNull(arrayListType);
 CollectionType refinedType = collectionType.refine(ArrayList.class, null, null, null);
 assertNotSame(collectionType, refinedType);
 assertEquals(ArrayList.class, refinedType.getRawClass());
 }

 @Test
 public void testToString() {
 Class<ArrayList<String>> arrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
 CollectionType collectionType = (CollectionType) arrayListType;
 String result = collectionType.toString();
 String expected = "[collection type; class ArrayList, contains class java.lang.String]";
 assertEquals(expected, result);
 }
}