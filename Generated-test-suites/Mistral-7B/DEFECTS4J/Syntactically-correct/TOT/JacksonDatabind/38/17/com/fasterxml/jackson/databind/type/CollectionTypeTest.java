import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeModifier;
import com.fasterxml.jackson.databind.type.TypeUtil;

public class CollectionTypeTest {

 // Test for construct method with valid arguments
 @Test
 public void testConstructValidArguments() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType[] superInts = new JavaType[0];
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemType);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(ArrayList.class, collectionType.getRawClass(TypeModifier.WildcardType.with(TypeUtil.VoidType)));
 assertEquals(elemType, collectionType.getContentType());
 assertEquals(Integer.class, collectionType.getElementType());
 }

 // Test for construct method with deprecated arguments
 @Test(expected = DeprecationException.class)
 public void testConstructDeprecatedArguments() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, elemType);
 fail("Expected DeprecationException not thrown");
 }

 // Test for narrow method with valid arguments
 @Test
 public void testNarrowValidArguments() throws Exception {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 JavaType listType = CollectionType.construct(List.class, null, arrayListClass, new JavaType[0], TypeFactory.defaultInstance().constructType(Integer.class));
 CollectionType listTypeNarrowed = listType.narrow(ArrayList.class);
 assertEquals(ArrayList.class, listTypeNarrowed.getRawClass());
 }

 // Test for narrow method with incorrect arguments
 @Test(expected = ClassCastException.class)
 public void testNarrowIncorrectArguments() throws Exception {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 JavaType listType = CollectionType.construct(List.class, null, arrayListClass, new JavaType[0], TypeFactory.defaultInstance().constructType(Integer.class));
 CollectionType listTypeNarrowed = listType.narrow(HashMap.class);
 fail("Expected ClassCastException not thrown");
 }

 // Test for withContentType method
 @Test
 public void testWithContentType() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType contentType = TypeFactory.defaultInstance().constructType(Long.class);
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemType);
 CollectionType collectionTypeWithContentType = collectionType.withContentType(contentType);
 assertEquals(List.class, collectionTypeWithContentType.getRawClass());
 assertEquals(Long.class, collectionTypeWithContentType.getContentType().getRawClass());
 }

 // Test for withTypeHandler method
 @Test
 public void testWithTypeHandler() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemType);
 CollectionType collectionTypeWithTypeHandler = collectionType.withTypeHandler(mapper.getTypeFactory().constructType(TypeDeserializer.None.class));
 assertNotNull(collectionTypeWithTypeHandler.getTypeHandler());
 }

 // Test for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemType);
 Object valueHandler = mapper.getTypeFactory().constructValueHandler(Object[].class);
 CollectionType collectionTypeWithContentTypeHandler = collectionType.withContentTypeHandler(valueHandler);
 assertNotNull(collectionTypeWithContentTypeHandler.getValueHandler());
 }

 // Test for withValueHandler method
 @Test
 public void testWithValueHandler() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemType);
 Object valueHandler = mapper.getTypeFactory().constructValueHandler(Integer[].class);
 CollectionType collectionTypeWithValueHandler = collectionType.withValueHandler(valueHandler);
 assertNotNull(collectionTypeWithValueHandler.getValueHandler());
 }

 // Test for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemType);
 Object valueHandler = mapper.getTypeFactory().constructValueHandler(Integer.class);
 CollectionType collectionTypeWithContentValueHandler = collectionType.withContentValueHandler(valueHandler);
 assertNotNull(collectionTypeWithContentValueHandler.getValueHandler());
 }

 // Test for withStaticTyping method
 @Test
 public void testWithStaticTyping() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemType);
 CollectionType collectionTypeWithStaticTyping = collectionType.withStaticTyping();
 assertTrue(collectionTypeWithStaticTyping.isStaticTyping());
 }

 // Test for refine method with valid arguments
 @Test
 public void testRefineValidArguments() throws Exception {
 Class<List<String>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType[] superInterfaces = new JavaType[] {TypeFactory.defaultInstance().constructType(Serializable.class)};
 CollectionType collectionType = CollectionType.construct(listClass, bindings, null, superInterfaces, TypeFactory.defaultInstance().constructType(String.class));
 JavaType refinedType = collectionType.refine(List.class, bindings, superClass, superInterfaces);
 assertEquals(collectionType.getRawClass(), refinedType.getRawClass());
 }

 // Test for toString method
 @Test
 public void testToString() throws Exception {
 Class<List<Integer>> listClass = List.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemType);
 String toString = collectionType.toString();
 assertTrue(toString.contains("[collection type; class"));
 assertTrue(toString.contains("contains " + listClass.getName()));
 }
}