package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.DefaultSerializers;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class InnerClassPropertyTest {

 private static class TestBean {
 private InnerClass innerClass;

 public InnerClass getInnerClass() {
 return innerClass;
 }

 public void setInnerClass(InnerClass innerClass) {
 this.innerClass = innerClass;
 }

 public static class InnerClass {
 private int value;

 public InnerClass() {
 // public constructor for deserialization
 }

 public InnerClass(int value) {
 this.value = value;
 }

 public int getValue() {
 return value;
 }

 public void setValue(int value) {
 this.value = value;
 }
 }
 }

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 PropertyName name = PropertyName.construct("innerClass");
 JsonDeserializer<InnerClass> deserializer = mapper.deserializerForClass(InnerClass.class);
 SettableBeanProperty settableBeanProperty = new InnerClassProperty(null, name, InnerClass.class, null, null, null, deserializer);
 _testingProperty = settableBeanProperty;
 _testingDeserializer = deserializer;
 _annotationIntrospector = mapper.getAnnotationIntrospector();
 _constructorIntrospector = mapper.getConstructorIntrospector();
 _valueDeserializer = mapper.getValueFactory().getValueDeserializerInstance(Integer.class);
 }

 @Test
 public void testDefaultConstructor() throws IOException, JsonProcessingException {
 // Test default constructor
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{}");
 InnerClassProperty property = _testingProperty;
 property.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 assertEquals(deserializedBean.getInnerClass(), null);
 }

 @Test
 public void testSingleArgumentConstructor() throws IOException, JsonProcessingException {
 // Test single argument constructor
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 InnerClassProperty property = _testingProperty;
 property.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass.getValue(), actualInnerClass.getValue());
 }

 @Test
 public void testValueDeserializer() throws IOException, JsonProcessingException {
 // Test value deserializer
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 InnerClassProperty property = _testingProperty;
 Converter<Integer, TestBean.InnerClass> valueDeserializer = (Converter<Integer, TestBean.InnerClass>) _testingDeserializer;
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean deserializedBean = property.deserialize(parser, _testingContext, null);
 TestBean actualBean = _mapper.readValue(parser, TestBean.class);
 TestBean.InnerClass actualInnerClass = actualBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSetAndReturn() throws IOException, JsonProcessingException {
 // Test set and return
 Object bean = new TestBean();
 TestBean.InnerClass innerClassToSet = new TestBean.InnerClass(42);
 TestBean deserializedBean = _mapper.readValue(new StringReader("{\"innerClass\": 42}"), TestBean.class);
 InnerClassProperty property = _testingProperty;
 assertEquals(deserializedBean.getInnerClass(), null);
 property.set(deserializedBean, innerClassToSet);
 TestBean updatedBean = _mapper.readValue(new StringReader("{\"innerClass\": 42}"), TestBean.class);
 assertEquals(updatedBean.getInnerClass().getValue(), 42);
 }

 @Test
 public void testJdkSerialization() throws IOException {
 // Test JDK serialization
 Object bean = new TestBean();
 ObjectWriter writer = _mapper.writerForringContext(new ObjectWriter.DefaultSerializers.NullSafeSerializerProvider());
 String json = writer.writeValueAsString(bean);
 Object deserializedBean = _mapper.readValue(new StringReader(json), TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws IOException, JsonProcessingException {
 // Test missing constructor
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 InnerClassProperty property = new InnerClassProperty(null, PropertyName.construct("innerClass"), InnerClass.class, null, null, null, null);
 property.deserializeAndSet(parser, _testingContext, new TestBean());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testSettableBeanPropertyDelegation() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty delegation
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 InnerClassProperty property = _testingProperty;
 property.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 _testingProperty = _annotationIntrospector.findPropertyAnnotation(TestBean.class, "innerClass");
 SettableBeanProperty settableBeanProperty = (SettableBeanProperty) _testingProperty;
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = settableBeanProperty.getDelegate().get(deserializedBean);
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testConstructorIntrospection() throws IOException, JsonProcessingException {
 // Test constructor introspection
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 AnnotatedConstructor annotatedConstructor = _constructorIntrospector.findConstructor(_testingClass, InnerClass.class);
 InnerClassProperty property = new InnerClassProperty(null, PropertyName.construct("innerClass"), _testingClass, annotatedConstructor, null, null, null);
 property.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSettableBeanPropertyWithValueDeserializer() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty with value deserializer
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 JsonDeserializer<InnerClass> deserializer = _mapper.deserializerForClass(InnerClass.class);
 SettableBeanProperty property = new InnerClassProperty(null, PropertyName.construct("innerClass"), _testingClass, null, null, null, deserializer);
 InnerClassProperty propertyWithValueDeserializer = new InnerClassProperty(property, deserializer);
 propertyWithValueDeserializer.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSettableBeanPropertyWithName() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty with name
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 String name = "testName";
 InnerClassProperty property = new InnerClassProperty(null, PropertyName.construct("innerClass"), _testingClass, null, null, null, null);
 InnerClassProperty propertyWithName = new InnerClassProperty(property, PropertyName.construct(name));
 propertyWithName.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSettableBeanPropertyWithValueDeserializerAndName() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty with value deserializer and name
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 JsonDeserializer<InnerClass> deserializer = _mapper.deserializerForClass(InnerClass.class);
 String name = "testName";
 InnerClassProperty property = new InnerClassProperty(null, PropertyName.construct("innerClass"), _testingClass, null, null, null, null);
 InnerClassProperty propertyWithValueDeserializerAndName = new InnerClassProperty(property, PropertyName.construct(name), deserializer);
 propertyWithValueDeserializerAndName.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSettableBeanPropertyWithAnnotation() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty with Annotation
 Object bean = new TestBean();
 JsonParser parser = _mapper.createParser("{\"innerClass\": 42}");
 TestBean.InnerClass annotation = new TestBean.InnerClassAnnotation();
 InnerClassProperty property = new InnerClassProperty(null, PropertyName.construct("innerClass"), _testingClass, null, null, null, null);
 InnerClassProperty propertyWithAnnotation = new InnerClassProperty(property, null, null, null, null, null, annotation);
 propertyWithAnnotation.deserializeAndSet(parser, _testingContext, bean);
 TestBean deserializedBean = _mapper.readValue(parser, TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 TestBean.InnerClassAnnotation actualAnnotation = deserializedBean.getAnnotation(TestBean.InnerClassAnnotation.class);
 TestBean.InnerClassAnnotation expectedAnnotation = new TestBean.InnerClassAnnotation();
 assertEquals(expectedAnnotation, actualAnnotation);
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSettableBeanPropertySerialization() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty serialization
 Object bean = new TestBean();
 bean.setInnerClass(new TestBean.InnerClass(42));
 ObjectWriter writer = _mapper.writerForringContext(new ObjectWriter.DefaultSerializers.NullSafeSerializerProvider());
 String json = writer.writeValueAsString(bean);
 TestBean deserializedBean = _mapper.readValue(new StringReader(json), TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSettableBeanPropertySerializationWithValueDeserializer() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty serialization with value deserializer
 Object bean = new TestBean();
 bean.setInnerClass(new TestBean.InnerClass(42));
 JsonDeserializer<InnerClass> deserializer = _mapper.deserializerForClass(InnerClass.class);
 ObjectWriter writer = _mapper.writerForringContext(new ObjectWriter.DefaultSerializers.NullSafeSerializerProvider());
 String json = writer.writeValueAsString(bean);
 TestBean deserializedBean = _mapper.readValue(new StringReader(json), TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @Test
 public void testSettableBeanPropertySerializationWithValueDeserializerAndName() throws IOException, JsonProcessingException {
 // Test SettableBeanProperty serialization with value deserializer and name
 Object bean = new TestBean();
 bean.setInnerClass(new TestBean.InnerClass(42));
 JsonDeserializer<InnerClass> deserializer = _mapper.deserializerForClass(InnerClass.class);
 String name = "testName";
 ObjectWriter writer = _mapper.writerForringContext(new ObjectWriter.DefaultSerializers.NullSafeSerializerProvider());
 String json = writer.writeValueAsString(bean);
 TestBean deserializedBean = _mapper.readValue(new StringReader(json), TestBean.class);
 TestBean.InnerClass expectedInnerClass = new TestBean.InnerClass(42);
 TestBean.InnerClass actualInnerClass = deserializedBean.getInnerClass();
 TestBean.InnerClassAnnotation actualAnnotation = deserializedBean.getAnnotation(TestBean.InnerClassAnnotation.class);
 TestBean.InnerClassAnnotation expectedAnnotation = new TestBean.InnerClassAnnotation();
 assertEquals(expectedAnnotation, actualAnnotation);
 assertEquals(expectedInnerClass, actualInnerClass);
 }

 @After
 public void teardown() {
 _testingProperty = null;
 _testingDeserializer = null;
 _annotationIntrospector = null;
 _constructorIntrospector = null;
 _valueDeserializer = null;
 }

 private ObjectMapper _mapper;
 private SettableBeanProperty _testingProperty;
 private JsonDeserializer<InnerClass> _testingDeserializer;
 private AnnotationIntrospector _annotationIntrospector;
 private ConstructorIntrospector _constructorIntrospector;
 private JsonDeserializer<Integer> _valueDeserializer;
}