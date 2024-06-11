package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 StdValueInstantiator valueInstantiator;
 ObjectMapper objectMapper;

 @SuppressWarnings({ "unchecked" })
 @Before
 public void setUp() {
 valueInstantiator = new StdValueInstantiator(null, null);
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 valueInstantiator.configureFromObjectSettings(null, null, null, null, null, null);
 assertEquals("Default instance of 'com.fasterxml.jackson.databind.deser.std.StdValueInstantiator'",
 valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDefaultWithNullValueClass() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 try {
 valueInstantiator.createUsingDefault(objectMapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("No ValueInstantiator for type class [null]", e.getMessage());
 }
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 JavaType delegateType = objectMapper.getTypeFactory().constructType(StdValueInstantiator.class);
 valueInstantiator = new StdValueInstantiator(null, delegateType);
 AnnotatedWithParams delegateCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(0);
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[] { new AnnotatedField("_valueTypeDesc"),
 new AnnotatedField("_valueClass") };
 valueInstantiator.configureFromDelegateSettings(delegateCreator, delegateArguments);
 StdValueInstantiator delegateInstance = new StdValueInstantiator(null, null);
 assertEquals("Delegate instance of 'com.fasterxml.jackson.databind.deser.std.StdValueInstantiator'",
 valueInstantiator.createUsingDelegate(objectMapper.getDeserializationContext(), delegateInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDelegateWithNullDelegate() throws Exception {
 JavaType delegateType = objectMapper.getTypeFactory().constructType(StdValueInstantiator.class);
 valueInstantiator = new StdValueInstantiator(null, delegateType);
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[] { new AnnotatedField("_valueTypeDesc"),
 new AnnotatedField("_valueClass") };
 valueInstantiator.configureFromDelegateSettings(null, delegateArguments);
 try {
 valueInstantiator.createUsingDelegate(objectMapper.getDeserializationContext(), null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("No ValueInstantiator for type class [Lcom.fasterxml.jackson.databind.deser.std.StdValueInstantiator;]",
 e.getMessage());
 }
 }

 @Test
 public void testCreateUsingArrayDelegate() throws Exception {
 JavaType arrayDelegateType = objectMapper.getTypeFactory().constructType(Object[].class);
 valueInstantiator = new StdValueInstantiator(null, null);
 valueInstantiator.configureFromArraySettings(null, arrayDelegateType, null);
 Object[] arrayDelegateArguments = new Object[] {"Array delegate instance"};
 valueInstantiator.configureFromArrayCreator(null, arrayDelegateType, arrayDelegateArguments);
 assertEquals("Array delegate instance", valueInstantiator.createUsingArrayDelegate(objectMapper.getDeserializationContext(), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingArrayDelegateWithNullDelegate() throws Exception {
 JavaType arrayDelegateType = objectMapper.getTypeFactory().constructType(Object[].class);
 valueInstantiator = new StdValueInstantiator(null, null);
 valueInstantiator.configureFromArraySettings(null, arrayDelegateType, null);
 try {
 valueInstantiator.createUsingArrayDelegate(objectMapper.getDeserializationContext(), null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Unable to find instantiator for value [null]", e.getMessage());
 }
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 AnnotatedWithParams withArgsCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(1);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[] { new AnnotatedField("_valueClass") };
 valueInstantiator.configureFromObjectSettings(null, null, null, null, withArgsCreator, constructorArguments);
 Object input = new Object();
 Object output = valueInstantiator.createFromObjectWith(objectMapper.getDeserializationContext(), new Object[] { input });
 assertNotNull(output);
 assertEquals(input.getClass(), output.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWithNullValueClass() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 AnnotatedWithParams withArgsCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(1);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[] { new AnnotatedField("_valueClass") };
 valueInstantiator.configureFromObjectSettings(null, null, null, null, withArgsCreator, constructorArguments);
 try {
 valueInstantiator.createFromObjectWith(objectMapper.getDeserializationContext(), null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("No ValueInstantiator for type class [null]", e.getMessage());
 }
 }

 @Test
 public void testCreateFromString() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 AnnotatedWithParams fromStringCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(2);
 assertEquals("Created from string: 'StdValueInstantiatorStringCreator'",
 valueInstantiator.createFromString(objectMapper.getDeserializationContext(), "StdValueInstantiatorStringCreator"));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromStringWithNullCreator() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 try {
 valueInstantiator.createFromString(objectMapper.getDeserializationContext(), null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("No String deserializer for class [Lcom.fasterxml.jackson.databind.deser.std.StdValueInstantiator;]",
 e.getMessage());
 }
 }

 @Test
 public void testCreateFromInt() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 AnnotatedWithParams fromIntCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(3);
 assertEquals("Created from int: 10", valueInstantiator.createFromInt(objectMapper.getDeserializationContext(), 10));
 }

 @Test
 public void testCreateFromLong() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 AnnotatedWithParams fromLongCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(4);
 assertEquals("Created from long: 20", valueInstantiator.createFromLong(objectMapper.getDeserializationContext(), 20L));
 }

 @Test
 public void testCreateFromDouble() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 AnnotatedWithParams fromDoubleCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(5);
 assertEquals("Created from double: 3.14", valueInstantiator.createFromDouble(objectMapper.getDeserializationContext(), 3.14));
 }

 @Test
 public void testCreateFromBoolean() throws Exception {
 valueInstantiator = new StdValueInstantiator(null, null);
 AnnotatedWithParams fromBooleanCreator = objectMapper.introspect(StdValueInstantiator.class)
 .getParameterAnnotations(6);
 assertEquals("Created from boolean: true", valueInstantiator.createFromBoolean(objectMapper.getDeserializationContext(), true));
 }
}