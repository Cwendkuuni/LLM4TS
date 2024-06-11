package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.ser.std.*;
import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class InnerClassPropertyTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testInnerClassProperty() throws Exception {
  Class<?> outerClass = OuterClass.class;
  Class<?> innerClass = OuterClass.InnerClass.class;
  AnnotatedClass acOuter = mapper.getTypeFactory().constructType(outerClass);
  AnnotatedClass acInner = mapper.getTypeFactory().constructType(innerClass);
  PropertyName name = PropertyName.construct("inner");
  Constructor<?> innerConstructor = innerClass.getDeclaredConstructor();
  innerConstructor.setAccessible(true);
  JsonDeserializer<Object> noOpDeserializer = new StdSerializer<Object>(Object.class) {};

  InnerClassProperty property = new InnerClassProperty(
      new SettableBeanProperty(acOuter, name), innerConstructor);

  Object outerInstance = mapper.readValue(new ByteArrayInputStream(
      ("{" +
       "\"outer\": {" +
       "\"inner\": {}" +
       "}" +
       "}").getBytes()), outerClass);
  Object innerInstance = mapper.readValue(new ByteArrayInputStream(
      ("{" +
       "\"inner\": {" +
       "}" +
       "}" +
       "}").getBytes()), innerClass);
  assertEquals(innerInstance, property.get(outerInstance));

  PropertyName newName = PropertyName.construct("newInner");
  InnerClassProperty newProperty = new InnerClassProperty(property, newName);
  assertEquals(newProperty.getName(), newName);
  assertEquals(newProperty.getClass(), InnerClassProperty.class);

  JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
      return new Object();
    }
  };
  InnerClassProperty deserializedProperty = new InnerClassProperty(property, deserializer);
  assertEquals(deserializer, deserializedProperty.getValueDeserializer());

  InnerClassProperty copiedProperty = new InnerClassProperty(property);
  assertEquals(property, copiedProperty);
  assertEquals(copiedProperty.getClass(), InnerClassProperty.class);

  JsonDeserializer<Object> annotatedDeserializer = new JsonDeserializer<Object>() {
    @JsonDeserialize
    static class Annotated {}

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
      return new Annotated();
    }
  };
  InnerClassProperty annotatedProperty = new InnerClassProperty(property, annotatedDeserializer);
  assertNotNull(annotatedProperty.getAnnotated());
  assertEquals(annotatedDeserializer, annotatedProperty.getValueDeserializer());

  InnerClassProperty jdkSerializedProperty = new InnerClassProperty(property);
  Object jdkSerializedInstance = jdkSerializedProperty.getValue();
  assertEquals(jdkSerializedInstance.getClass(), InnerClassProperty.class);
  assertTrue(jdkSerializedInstance instanceof Serializable);

  InnerClassProperty jdkDeserializedProperty = (InnerClassProperty) mapper.readValue(
      new ByteArrayInputStream(mapper.writeValueAsBytes(jdkSerializedInstance)),
      InnerClassProperty.class);
  assertEquals(property, jdkDeserializedProperty);
 }

 @Test
 public void testInnerClassPropertyWithInvalidConstructor() throws Exception {
  Class<?> outerClass = OuterClass.class;
  Class<?> innerClass = OuterClass.InnerClass.class;
  PropertyName name = PropertyName.construct("inner");
  Constructor<?> innerConstructor = innerClass.getDeclaredConstructor(String.class);
  innerConstructor.setAccessible(true);

  try {
   InnerClassProperty property = new InnerClassProperty(
       new SettableBeanProperty(mapper.getTypeFactory().constructType(outerClass), name),
       innerConstructor);
   fail("Should not be able to create InnerClassProperty with non-matching constructor");
  } catch (IllegalArgumentException e) {
   // expected
  }
 }
}

class OuterClass {
 @JsonDeserialize(using = InnerClassDeserializer.class)
 static class InnerClass {
  // empty inner class
 }
}