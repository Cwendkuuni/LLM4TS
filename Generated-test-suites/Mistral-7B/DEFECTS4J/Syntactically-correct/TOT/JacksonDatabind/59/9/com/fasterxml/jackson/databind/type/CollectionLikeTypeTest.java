import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeSerializerType;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

 // Test for constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInts, JavaType elemT
 @Test
 public void testConstruct() {
 Class<List<String>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, bindings, javaType, null, null);
 assertEquals(listClass, collectionLikeType.getRawClass());
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 // Test for construct with deprecated constructor
 @Test
 public void testConstructDeprecated() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, javaType);
 assertEquals(listClass, collectionLikeType.getRawClass());
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 // Test for upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType javaTypeListString = TypeFactory.defaultInstance().constructType(List.class, String.class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 JavaType expectedCollectionLikeType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), javaTypeListString, null, null);
 assertEquals(expectedCollectionLikeType, collectionLikeType);
 }

 // Test for isTrueCollectionType method
 @Test
 public void testIsTrueCollectionType() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 assertTrue(collectionLikeType.isTrueCollectionType());
 JavaType arrayType = TypeFactory.defaultInstance().constructType(String[].class);
 JavaType collectionLikeArrayType = CollectionLikeType.construct(arrayType, TypeBindings.defaultBindings(), null, null, null);
 assertFalse(collectionLikeArrayType.isTrueCollectionType());
 }

 // Test for equals method
 @Test
 public void testEquals() {
 Class<ArrayList<String>> arrayListClass = ArrayList.class;
 JavaType javaTypeArrayListString = TypeFactory.defaultInstance().constructType(arrayListClass);
 JavaType javaTypeArrayListString1 = TypeFactory.defaultInstance().constructType(arrayListClass);
 JavaType collectionLikeType = CollectionLikeType.construct(arrayListClass, TypeBindings.defaultBindings(), javaTypeArrayListString, null, null);
 JavaType collectionLikeType1 = CollectionLikeType.construct(arrayListClass, TypeBindings.defaultBindings(), javaTypeArrayListString, null, null);
 JavaType collectionLikeType2 = CollectionLikeType.construct(ArrayList.class, TypeBindings.defaultBindings(), javaTypeArrayListString, null, null);
 assertFalse(collectionLikeType.equals(collectionLikeType2));
 assertFalse(collectionLikeType.equals(new Object()));
 assertTrue(collectionLikeType.equals(collectionLikeType1));
 }

 // Test for getContentType method
 @Test
 public void testGetContentType() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 // Test for getContentValueHandler method
 @Test
 public void testGetContentValueHandler() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 TypeDeserializer valueHandler = collectionLikeType.getValueHandler();
 assertNotNull(valueHandler);
 }

 // Test for getContentTypeHandler method
 @Test
 public void testGetContentTypeHandler() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 TypeSerializerType typeHandler = (TypeSerializerType) collectionLikeType.getTypeHandler();
 assertNotNull(typeHandler);
 }

 // Test for hasHandlers method
 @Test
 public void testHasHandlers() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 assertTrue(collectionLikeType.hasHandlers());
 }

 // Test for getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 StringBuilder actual = new StringBuilder();
 collectionLikeType.getErasedSignature(actual);
 StringBuilder expected = new StringBuilder();
 expected.append("[Ljava.util.List;");
 assertEquals(expected.toString(), actual.toString());
 }

 // Test for getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 StringBuilder actual = new StringBuilder();
 collectionLikeType.getGenericSignature(actual);
 StringBuilder expected = new StringBuilder();
 expected.append("java.util.List<Ljava.lang.String;>");
 assertEquals(expected.toString(), actual.toString());
 }

 // Test for buildCanonicalName method
 @Test
 public void testBuildCanonicalName() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 assertEquals("[Ljava.util.List;", collectionLikeType.buildCanonicalName());
 }

 // Test for withContentType method
 @Test
 public void testWithContentType() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 JavaType newContentType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType expectedCollectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, newContentType);
 JavaType actualCollectionLikeType = collectionLikeType.withContentType(newContentType);
 assertEquals(expectedCollectionLikeType, actualCollectionLikeType);
 }

 // Test for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 Object handler = new Object();
 JavaType expectedCollectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null).withTypeHandler(handler);
 JavaType actualCollectionLikeType = collectionLikeType.withTypeHandler(handler);
 assertEquals(expectedCollectionLikeType, actualCollectionLikeType);
 }

 // Test for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 Object handler = new Object();
 JavaType expectedContentTypeHandler = TypeFactory.defaultInstance().constructType(String.class).withTypeHandler(handler);
 JavaType actualContentTypeHandler = collectionLikeType.getContentType().withTypeHandler(handler);
 JavaType expectedCollectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, expectedContentTypeHandler);
 JavaType actualCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);
 assertEquals(expectedCollectionLikeType, actualCollectionLikeType);
 }

 // Test for withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 JavaType expectedCollectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null).withStaticTyping();
 JavaType actualCollectionLikeType = collectionLikeType.withStaticTyping();
 assertEquals(expectedCollectionLikeType, actualCollectionLikeType);
 }

 // Test for refine method
 @Test
 public void testRefine() {
 Class<List<String>> listClass = List.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
 JavaType collectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), javaType, null, null);
 JavaType refinedType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType expectedCollectionLikeType = CollectionLikeType.construct(listClass, TypeBindings.defaultBindings(), refinedType, null, null);
 JavaType actualCollectionLikeType = collectionLikeType.refine(ArrayList.class, TypeBindings.defaultBindings(), javaType, null);
 assertEquals(expectedCollectionLikeType, actualCollectionLikeType);
 }
}