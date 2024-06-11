package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 // Test constructor with valid arguments
 @Test
 public void testObjectIdInfoConstruction() {
  PropertyName propName = new PropertyName("testProperty");
  Class<String> scope = String.class;
  Class<MyCustomIdGenerator> generator = MyCustomIdGenerator.class;
  Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

  ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);

  assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
  assertEquals(String.class, objectIdInfo.getScope());
  assertEquals(MyCustomIdGenerator.class, objectIdInfo.getGeneratorType());
  assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
  assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test constructor with valid but null arguments
 @Test
 public void testObjectIdInfoConstructionNullArgs() {
  PropertyName propName = null;
  Class<String> scope = String.class;
  Class<MyCustomIdGenerator> generator = MyCustomIdGenerator.class;

  try {
   ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, null);
   fail("Expected IllegalStateException");
  } catch (IllegalStateException e) {
   // Expected
  }

  // Test no-args constructor
  ObjectIdInfo noArgsObjectIdInfo = new ObjectIdInfo();
  assertNull(noArgsObjectIdInfo.getPropertyName());
  assertNull(noArgsObjectIdInfo.getScope());
  assertNull(noArgsObjectIdInfo.getGeneratorType());
  assertNull(noArgsObjectIdInfo.getResolverType());
  assertFalse(noArgsObjectIdInfo.getAlwaysAsId());
 }

 // Test constructor with valid but deprecated arguments
 @Test
 public void testObjectIdInfoConstructionDeprecated() {
  PropertyName propName = new PropertyName("testProperty");
  Class<String> scope = String.class;
  Class<MyCustomIdGenerator> generator = MyCustomIdGenerator.class;

  ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);

  assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
  assertEquals(String.class, objectIdInfo.getScope());
  assertEquals(MyCustomIdGenerator.class, objectIdInfo.getGeneratorType());
  assertNull(objectIdInfo.getResolverType());
  assertFalse(objectIdInfo.getAlwaysAsId());
 }

 // Test withAlwaysAsId method
 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
  PropertyName propName = new PropertyName("testProperty");
  Class<String> scope = String.class;
  Class<MyCustomIdGenerator> generator = MyCustomIdGenerator.class;
  ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, null);

  ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

  assertTrue(withAlwaysAsId.getAlwaysAsId());
  assertNotSame(objectIdInfo, withAlwaysAsId);
 }

 // Test toString method
 @Test
 public void testObjectIdInfoToString() {
  PropertyName propName = new PropertyName("testProperty");
  Class<String> scope = String.class;
  Class<MyCustomIdGenerator> generator = MyCustomIdGenerator.class;

  ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, false, null);

  String toString = objectIdInfo.toString();
  String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.mypackage.MyCustomIdGenerator, alwaysAsId=false";
  assertEquals(expected, toString);
 }
}

class MyCustomIdGenerator implements ObjectIdGenerator<Object> {
 // Implementation of ObjectIdGenerator goes here
}