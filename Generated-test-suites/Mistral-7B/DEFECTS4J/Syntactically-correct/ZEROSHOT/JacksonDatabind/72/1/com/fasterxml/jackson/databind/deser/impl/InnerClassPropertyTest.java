package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Introspector;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();
 private static final JsonFactory JSON_FACTORY = MAPPER.getFactory();
 private static final Introspector INTROSPECTOR = MAPPER.getSerializationConfig().getIntrospector();
 private static final AnnotationIntrospector ANNOTATION_INTROSPECTOR = INTROSPECTOR.getClassAnnotations();
 private static final PropertyName PROPERTY_NAME = PropertyName.construct("propertyName");
 private InnerClassProperty innerClassProperty;
 private SettableBeanProperty delegate;
 private AnnotatedConstructor constructor;
 private Class<?> innerClassType;

 @Before
 public void setUp() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException {
 Class<?> outerClassType = GetOuterClass.class;
 innerClassType = GetOutterClassInnerClass.class;
 Class<?>[] constructorArgs = new Class[] { outerClassType };
 constructor = INTROSPECTOR.getConstructorForDeserialization(innerClassType, constructorArgs);
 delegate = new SettableBeanProperty(PropertyName.construct("delegateProperty"), new StdSerializer<>());
 innerClassProperty = new InnerClassProperty(delegate, constructor);
 }

 @Test
 public void testConstructor() throws InstantiationException, IllegalAccessException, InvocationTargetException {
 Constructor<?> innerClassPropertyConstructor = InnerClassProperty.class.getDeclaredConstructor(SettableBeanProperty.class,
 Constructor.class);
 innerClassPropertyConstructor.setAccessible(true);
 InnerClassProperty innerClassProperty2 = (InnerClassProperty) innerClassPropertyConstructor.newInstance(delegate, constructor);
 assertEquals(innerClassProperty, innerClassProperty2);
 }

 @Test
 public void testGetDelegate() {
 assertEquals(delegate, innerClassProperty._delegate);
 }

 @Test
 public void testGetCreator() {
 assertEquals(constructor, innerClassProperty._creator);
 }

 @Test
 public void testGetAnnotated() {
 assertEquals(constructor, innerClassProperty._annotated);
 }

 @Test
 public void testWithName() {
 assertEquals(new InnerClassProperty(innerClassProperty, PROPERTY_NAME), innerClassProperty.withName(PROPERTY_NAME));
 }

 @Test
 public void testWithValueDeserializer() {
 assertEquals(new InnerClassProperty(innerClassProperty, new None()), innerClassProperty.withValueDeserializer(new None()));
 }

 @Test
 public void testAssignIndex() throws IllegalAccessException {
 int index = 0;
 innerClassProperty.assignIndex(index);
 assertEquals(index, innerClassProperty.getPropertyIndex());
 }

 @Test
 public void testGetPropertyIndex() {
 assertEquals(-1, innerClassProperty.getPropertyIndex());
 }

 @Test
 public void testSerializeAndSet() throws JsonProcessingException {
 Object outerInstance = new GetOuterClass();
 String value = "value";
 MAPPER.writeValue(outerInstance, value);
 assertEquals(value, innerClassProperty.get(outerInstance));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser jsonParser = JSON_FACTORY.createParser("{\"propertyName\":\"value\"}");
 DeserializationContext context = MAPPER.getDeserializationContext();
 innerClassProperty.deserializeAndSet(jsonParser, context, new GetOuterClass());
 assertEquals("value", innerClassProperty.get(new GetOuterClass()));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 JsonParser jsonParser = JSON_FACTORY.createParser("{\"propertyName\":\"value\"}");
 Object instance = new GetOuterClass();
 assertEquals(instance, innerClassProperty.deserializeSetAndReturn(jsonParser, context, instance));
 assertEquals("value", innerClassProperty.get(instance));
 }

 @Test
 public void testSet() throws IOException {
 GetOuterClass outerInstance = new GetOuterClass();
 String value = "value";
 innerClassProperty.set(outerInstance, value);
 assertEquals(value, innerClassProperty.get(outerInstance));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 GetOuterClass outerInstance = new GetOuterClass();
 String value = "value";
 assertEquals(outerInstance, innerClassProperty.setAndReturn(outerInstance, value));
 assertEquals(value, innerClassProperty.get(outerInstance));
 }

 @Test
 public void testReadResolve() {
 InnerClassProperty resolved = (InnerClassProperty) innerClassProperty.readResolve();
 assertEquals(innerClassProperty, resolved);
 }

 @Test
 public void testWriteReplace() {
 InnerClassProperty innerClassPropertyWriterReplace = new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, constructor, null, null));
 Object writerReplace = innerClassPropertyWriterReplace.writeReplace();
 assertEquals(innerClassPropertyWriterReplace, writerReplace);
 }

}

class GetOuterClass {
 private GetOutterClassInnerClass innerClass;

 public GetOuterClass() {
 innerClass = new GetOutterClassInnerClass();
 }

 public GetOutterClassInnerClass getInnerClass() {
 return innerClass;
 }
}

class GetOutterClassInnerClass {
 // empty class
}