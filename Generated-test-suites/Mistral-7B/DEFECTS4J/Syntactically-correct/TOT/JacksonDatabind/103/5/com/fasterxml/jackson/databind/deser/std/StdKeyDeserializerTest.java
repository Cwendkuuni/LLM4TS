package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringCtorKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 ctxt = new TestDeserializationContext();
 mapper = new ObjectMapper();
 }

 @Test
 public void testStringKeyDeserializer() throws JsonProcessingException {
 StringKD deserializer = StdKeyDeserializer.forType(String.class);
 assertEquals("expected String key", "testKey", deserializer.deserializeKey("testKey", ctxt));
 }

 @Test
 public void testIntKeyDeserializer() throws JsonProcessingException {
 int key = 123;
 StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
 assertEquals("expected Integer key", key, intDeserializer.deserializeKey("123", ctxt));
 }

 @Test
 public void testEnumKeyDeserializer() throws JsonProcessingException {
 // Create test enum
 @JsonDeserialize(using = EnumKD.class)
 enum TestEnum {
 FOOBAR, BARFOO, FOOFOO
 }

 // Test deserialization with valid keys
 TestEnum deserializedEnum1 = (TestEnum) StdKeyDeserializer.forType(TestEnum.class).deserializeKey("FOOBAR", ctxt);
 TestEnum deserializedEnum2 = (TestEnum) StdKeyDeserializer.forType(TestEnum.class).deserializeKey("BARFOO", ctxt);
 // Test deserialization with invalid key
 assertNull(StdKeyDeserializer.forType(TestEnum.class).deserializeKey("INVALID", ctxt));
 }

 @Test
 public void testCustomStringKeyDeserializer() throws Exception {
 class CustomKey {
 public static CustomKey create(String key) {
 return new CustomKey(key);
 }

 private final String _key;

 CustomKey(String key) {
 _key = key;
 }

 public String getKey() {
 return _key;
 }
 }

 AnnotatedClass ac = mapper.getAnnotationIntrospector().findAnnotation(CustomKey.class, CustomKey.class);
 AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
 JsonDeserializer<CustomKey> deserializer = ai.findDeserializer(CustomKey.class, ctxt);

 assertEquals("expected CustomKey key", "testKey", deserializer.deserializeKey(new TextNode("testKey"), ctxt).getKey());
 }

 @Test
 public void testClassKeyDeserializer() throws Exception {
 Class<?> className = String.class;
 // Test deserialization with valid key
 assertEquals(className, StdKeyDeserializer.forType(className).deserializeKey("java.lang.String", ctxt));
 // Test deserialization with invalid key
 assertNull(StdKeyDeserializer.forType(int.class).deserializeKey("java.lang.String", ctxt));
 }

 @Test
 public void testCustomClassKeyDeserializer() throws Exception {
 // Create test class
 class TestClass {
 public static TestClass create(String key) {
 return new TestClass(key);
 }

 private final String _key;

 TestClass(String key) {
 _key = key;
 }

 public String getKey() {
 return _key;
 }
 }

 AnnotatedClass ac = mapper.getAnnotationIntrospector().findAnnotation(TestClass.class, TestClass.class);
 AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
 JsonDeserializer<TestClass> deserializer = ai.findDeserializer(TestClass.class, ctxt);

 assertEquals("expected TestClass key", "testKey", deserializer.deserializeKey(new TextNode("testKey"), ctxt).getKey());
 }

 @Test
 public void testNullValueProvider() throws Exception {
 NullValueProvider nullValueProvider = new StdKeyDeserializer.StringKD(String.class);
 assertNull(nullValueProvider.getNullValue(null, null));
 }

 @Test
 public void testStdKeyDeserializer_forType_UnknownType() throws Exception {
 assertNull(StdKeyDeserializer.forType((Class<?>)null));
 }
}