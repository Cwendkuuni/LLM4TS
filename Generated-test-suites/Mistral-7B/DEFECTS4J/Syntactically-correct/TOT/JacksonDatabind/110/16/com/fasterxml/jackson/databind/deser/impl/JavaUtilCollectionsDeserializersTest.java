package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 static <T> T createSingleton(Class<T> clazz) {
 return (T) Collections.singleton(clazz.cast(new Object()));
 }

 @SuppressWarnings("unchecked")
 static <T> List<T> createSingletonList(Class<T> clazz) {
 return (List<T>) Collections.singletonList(clazz.cast(new Object()));
 }

 @SuppressWarnings("unchecked")
 static <K, V> Map<K, V> createSingletonMap(Class<K> keyType, Class<V> valueType) {
 return (Map<K, V>) Collections.singletonMap(keyType.cast(new Object()), valueType.cast(new Object()));
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @BeforeClass
 static void setupModules() {
 SimpleModule module = new SimpleModule();
 module.addDeserializer(Set.class, new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, Set.class));
 module.addDeserializer(List.class, new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, List.class));
 module.addDeserializer(Map.class, new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, Map.class));
 MAPPER.registerModule(module);
 }

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 Object input = createSingleton(HashSet.class);
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(List.class, Boolean.TYPE);
 Object input = createSingletonList(ArrayList.class);
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollectionSingletonMap() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Map.class, String.class, String.class);
 Object input = createSingletonMap(String.class, String.class);
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 Object input = Collections.unmodifiableSet(createSingleton(HashSet.class));
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(List.class, Boolean.TYPE);
 Object input = Collections.unmodifiableList(createSingletonList(ArrayList.class));
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollectionUnmodifiableMap() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Map.class, String.class, String.class);
 Object input = Collections.unmodifiableMap(createSingletonMap(String.class, String.class));
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollectionUnexpectedType() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Map.class, Integer.TYPE, String.class);
 Object input = createSingletonMap(String.class, String.class);
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 fail("Expected JsonMappingException for unexpected type");
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Map.class, String.class, String.class);
 Object input = createSingletonMap(HashMap.class, String.class, String.class);
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Map.class, String.class, String.class);
 Object input = Collections.unmodifiableMap(createSingletonMap(HashMap.class, String.class, String.class));
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertEquals(input, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMapUnexpectedType() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(List.class, String.class, String.class);
 Object input = createSingletonMap(HashMap.class, String.class, String.class);
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 fail("Expected JsonMappingException for unexpected type");
 }

 @Test
 public void testFindForCollectionEmptyList() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(List.class, Boolean.TYPE);
 Object input = Collections.emptyList();
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertNotNull(output);
 assertEquals(0, output.size());
 }

 @Test
 public void testFindForCollectionEmptySet() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 Object input = Collections.emptySet();
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertNotNull(output);
 assertEquals(0, output.size());
 }

 @Test
 public void testFindForCollectionEmptyMap() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(Map.class, String.class, String.class);
 Object input = Collections.emptyMap();
 Object output = MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 assertNotNull(output);
 assertEquals(0, output.size());
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollectionNullInput() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(List.class, Boolean.TYPE);
 Object input = null;
 MAPPER.readValue(MAPPER.writeValueAsString(input), type);
 fail("Expected JsonMappingException for null input");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollectionNullOutput() throws Exception {
 JavaType type = MAPPER.getTypeFactory().constructType(List.class, Boolean.TYPE);
 Object input = createSingletonList(ArrayList.class);
 Object output = null;
 MAPPER.readValue(MAPPER.writeValueAsString(input), type, output);
 fail("Expected JsonMappingException for null output");
 }
}