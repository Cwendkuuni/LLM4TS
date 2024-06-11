package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 assertEquals("Default test for String class", instantiator.createUsingDefault(mapper.getDeserializationContext()));
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureFromDelegateSettings(
 new AnnotatedWithParams(String.class, "createUsingDelegate"),
 new AnnotatedClass[]{String[].class},
 new SettableBeanProperty[]{null},
 mapper.getDeserializationContext());
 instantiator.configureFromObjectSettings(null, null, null, null, null, null);

 String[] args = new String[]{"Test"};
 ObjectNode node = mapper.valueToTree(args);
 Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), mapper.readValue(node.toString(), String[].class)[0]);
 assertEquals("Test", result);
 }

 @Test
 public void testCreateUsingArrayDelegate() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(int[].class);
 instantiator.configureFromArraySettings(
 new AnnotatedWithParams(int[].class, "createUsingArrayDelegate"),
 new JavaType(int[].class),
 new SettableBeanProperty[]{null, null, null});
 instantiator.configureFromObjectSettings(null, null, null, null, null, null);

 ObjectNode node = mapper.valueToTree(new int[]{1, 2, 3});
 Object result = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), mapper.readValue(node.toString(), Object.class));
 assertArrayEquals(new int[]{1, 2, 3}, (int[]) result);
 }

 @Test
 public void testCreateFromString() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 instantiator.configureFromStringCreator(new AnnotatedWithParams(Integer.class, "createFromString"));

 Object result = instantiator.createFromString(mapper.getDeserializationContext(), "42");
 assertEquals(42, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromInvalidString() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 instantiator.configureFromStringCreator(new AnnotatedWithParams(Integer.class, "createFromString"));

 Object result = instantiator.createFromString(mapper.getDeserializationContext(), "abc");
 fail("Expected JsonMappingException");
 }

 @Test
 public void testCreateFromInt() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 instantiator.configureFromIntCreator(new AnnotatedWithParams(Integer.class, "createFromInt"));

 Object result = instantiator.createFromInt(mapper.getDeserializationContext(), 42);
 assertEquals(42, result);
 }

 @Test
 public void testCreateFromLong() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Long.class);
 instantiator.configureFromLongCreator(new AnnotatedWithParams(Long.class, "createFromLong"));

 Object result = instantiator.createFromLong(mapper.getDeserializationContext(), 42L);
 assertEquals(42L, result);
 }

 @Test
 public void testCreateFromDouble() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Double.class);
 instantiator.configureFromDoubleCreator(new AnnotatedWithParams(Double.class, "createFromDouble"));

 Object result = instantiator.createFromDouble(mapper.getDeserializationContext(), 42.0);
 assertEquals(42.0, result, 0.0);
 }

 @Test
 public void testCreateFromBoolean() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
 instantiator.configureFromBooleanCreator(new AnnotatedWithParams(Boolean.class, "createFromBoolean"));

 Object result = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
 assertEquals(true, result);
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(MyClass.class);
 MyClass arg1 = new MyClass();
 arg1.setField1("Test1");
 arg1.setField2(42);

 MyClass arg2 = new MyClass();
 arg2.setField1("Test2");
 arg2.setField2(new String[]{"Test3", "Test4"});

 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[]{
 new SettableBeanProperty("field1", String.class),
 new SettableBeanProperty("field2", String[].class)
 };

 instantiator.configureFromObjectSettings(
 new AnnotatedWithParams(MyClass.class, "createFromObjectWith"),
 null,
 null,
 mapper.getDeserializationContext());
 instantiator.configureFromObjectSettings(null, null, constructorArguments, null);

 ObjectNode node = mapper.valueToTree(arg1);
 Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), mapper.readValue(node.toString(), MyClass.class));
 assertEquals(arg1.getField1(), result.getField1());
 assertEquals(arg1.getField2(), result.getField2());

 node = mapper.valueToTree(arg2);
 result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), mapper.readValue(node.toString(), MyClass.class));
 assertEquals(arg2.getField1(), result.getField1());
 assertEquals(arg2.getField2(), result.getField2());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWithNullArguments() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(MyClass.class);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[]{
 new SettableBeanProperty("field1", String.class),
 new SettableBeanProperty("field2", String[].class)
 };

 instantiator.configureFromObjectSettings(
 new AnnotatedWithParams(MyClass.class, "createFromObjectWith"),
 null,
 null,
 mapper.getDeserializationContext());
 instantiator.configureFromObjectSettings(null, null, constructorArguments, null);

 Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), null);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWithInvalidArgumentsType() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(MyClass.class);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[]{
 new SettableBeanProperty("field1", String.class),
 new SettableBeanProperty("field2", Number[].class)
 };

 instantiator.configureFromObjectSettings(
 new AnnotatedWithParams(MyClass.class, "createFromObjectWith"),
 null,
 null,
 mapper.getDeserializationContext());
 instantiator.configureFromObjectSettings(null, null, constructorArguments, null);

 Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{});
 fail("Expected JsonMappingException");
 }
}