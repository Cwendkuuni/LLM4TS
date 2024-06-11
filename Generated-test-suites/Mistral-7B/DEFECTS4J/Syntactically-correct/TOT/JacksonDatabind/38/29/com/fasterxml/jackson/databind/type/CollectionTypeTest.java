import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = JavaType.construct(int.class);
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType collectionType = CollectionType.construct(listClass, bindings, null, null, elemType);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testConstructWithNullArguments() {
 try {
 CollectionType.construct(null, null, null, null, null);
 fail("Should not be able to construct CollectionType with null arguments");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 TypeBindings bindings = new TypeBindings();
 bindings = null;
 CollectionType.construct(List.class, bindings, null, null, int.class);
 fail("Should not be able to construct CollectionType with null bindings");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 JavaType elemType = JavaType.construct(int[].class);
 CollectionType.construct(List.class, null, null, null, elemType);
 fail("Should not be able to construct CollectionType with array type as element type");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testConstructDeprecatedMethod() {
 try {
 CollectionType collectionType = CollectionType.construct(List.class, int.class);
 fail("Deprecated method should not be used");
 } catch (DeprecatedException e) {
 // Expected
 }
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<String>> arrayListClass = ArrayList.class;
 JavaType stringListType = CollectionType.construct(List.class, null, null, null, String.class);
 JavaType narrowedArrayListType = stringListType.narrow(arrayListClass);
 assertEquals(ArrayList.class, narrowedArrayListType.getRawClass());
 }

 @Test
 public void testWithContentType() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
 JavaType listWithCustomContentsType = listType.withContentType(JavaType.construct(String.class));
 assertEquals(String.class, listWithCustomContentsType.getContentType().getRawClass());
 }

 @Test
 public void testWithTypeHandlers() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
 Object valueHandler = new Object();
 Object typeHandler = new Object();
 JavaType listWithValueHandler = listType.withValueHandler(valueHandler);
 JavaType listWithTypeHandler = listType.withTypeHandler(typeHandler);
 JavaType listWithBothHandlers = listType.withValueHandler(valueHandler).withTypeHandler(typeHandler);
 assertEquals(valueHandler, listWithValueHandler.getValueHandler());
 assertEquals(typeHandler, listWithTypeHandler.getTypeHandler());
 assertEquals(valueHandler, listWithBothHandlers.getValueHandler());
 assertEquals(typeHandler, listWithBothHandlers.getTypeHandler());
 }

 @Test
 public void testWithStaticTyping() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
 JavaType staticListType = listType.withStaticTyping();
 assertFalse(listType.isStatic());
 assertTrue(staticListType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<LinkedList<Double>> linkedListClass = LinkedList.class;
 JavaType doubleListType = CollectionType.construct(List.class, null, null, null, Double.TYPE);
 JavaType refinedLinkedListType = doubleListType.refine(linkedListClass, null, null, null);
 assertEquals(LinkedList.class, refinedLinkedListType.getRawClass());
 }
}