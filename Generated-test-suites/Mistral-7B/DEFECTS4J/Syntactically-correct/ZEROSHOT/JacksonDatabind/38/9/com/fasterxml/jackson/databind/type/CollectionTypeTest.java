package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(Integer.class);
 CollectionType actualCollectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 CollectionType expectedCollectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listType = new ArrayList<>() {}.getClass();
 JavaType elemT = JavaType.constructType(Integer.class);
 CollectionType actualCollectionType = CollectionType.construct(listType, elemT);
 CollectionType expectedCollectionType = new CollectionType(listType, null, _bogusSuperClass(listType), null, elemT, null, null, false);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<Integer>> arrayListType = new ArrayList<>() {}.getClass();
 CollectionType collectionType = CollectionType.construct(arrayListType, null, null, null, JavaType.constructType(Integer.class));
 JavaType narrowedType = collectionType.narrow(HashSet.class);
 assertNotNull(narrowedType);
 assertFalse(collectionType.equals(narrowedType));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowDeprecated() {
 Class<ArrayList<Integer>> arrayListType = new ArrayList<>() {}.getClass();
 CollectionType collectionType = CollectionType.construct(arrayListType, null, null, null, JavaType.constructType(Integer.class));
 JavaType narrowedType = collectionType.narrow(ArrayList.class);
 }

 @Test
 public void testWithContentType() {
 Class<List<String>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(String.class);
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 JavaType contentType = JavaType.constructType(Integer.class);
 CollectionType actualCollectionType = collectionType.withContentType(contentType);
 CollectionType expectedCollectionType = new CollectionType(listType, bindings, superClass, superInts, contentType, null, null, false);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<String>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(String.class);
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 Object typeHandler = new Object() {};
 CollectionType actualCollectionType = collectionType.withTypeHandler(typeHandler);
 CollectionType expectedCollectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, typeHandler, false);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<String>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(String.class);
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 Object contentTypeHandler = new Object() {};
 CollectionType actualCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
 CollectionType expectedCollectionType = new CollectionType(listType, bindings, superClass, superInts, elemT.withTypeHandler(contentTypeHandler), null, null, false);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<String>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(String.class);
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 Object valueHandler = new Object() {};
 CollectionType actualCollectionType = collectionType.withValueHandler(valueHandler);
 CollectionType expectedCollectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, valueHandler, null, false);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<String>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(String.class);
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 Object contentValueHandler = new Object() {};
 CollectionType actualCollectionType = collectionType.withContentValueHandler(contentValueHandler);
 CollectionType expectedCollectionType = new CollectionType(listType, bindings, superClass, superInts, elemT.withValueHandler(contentValueHandler), null, null, false);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<String>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(String.class);
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 CollectionType actualCollectionType = collectionType.withStaticTyping();
 CollectionType expectedCollectionType = new CollectionType(listType, bindings, superClass, superInts, elemT.withStaticTyping(), null, null, true);
 assertEquals(expectedCollectionType.getClass(), actualCollectionType.getClass());
 assertEquals(expectedCollectionType.toString(), actualCollectionType.toString());
 }

 @Test
 public void testRefine() {
 Class<List<Integer>> listType = new ArrayList<>() {}.getClass();
 TypeBindings bindings = TypeBindings.getDefault();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(Integer.class);
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT, null, null, false);
 JavaType refinedType = collectionType.refine(HashSet.class, bindings, null, null);
 CollectionType expectedCollectionType = new CollectionType(HashSet.class, bindings, null, null, elemT, null, null, false);
 assertEquals(expectedCollectionType.getClass(), refinedType.getClass());
 assertEquals(expectedCollectionType.toString(), refinedType.toString());
 }
}