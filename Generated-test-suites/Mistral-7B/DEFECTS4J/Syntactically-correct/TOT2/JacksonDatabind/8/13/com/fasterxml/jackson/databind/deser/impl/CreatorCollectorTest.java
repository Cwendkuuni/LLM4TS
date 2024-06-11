package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.MethodAnnotatedWith;
import com.fasterxml.jackson.databind.introspect.ValuePropertyAnnotation;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private DeserializationConfig config;
 private BeanDescription beanDesc;

 @Before
 public void setUp() throws Exception {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", String.class);
 beanDesc = new BeanDescription(String.class, null, null, null, null, null, null, null, null, null, null);
 beanDesc.addPropertyDefinition(propDef);
 config = new DeserializationConfig(new AnnotationIntrospector(), new StandardSerializerProvider());
 collector = new CreatorCollector(beanDesc, true);
 }

 @Test
 public void testDefaultCreator() throws Exception {
 collector.setDefaultCreator(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("CreatorCollector", BeanDescription.class, boolean.class)));
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testStringCreator() throws Exception {
 String creatorName = "testStringCreator";
 Class<?> stringClass = String[].class;
 AnnotatedMethod stringMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addStringCreator(stringMethod, true);
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(String[].class, type.getRawClass());
 }

 @Test
 public void testIntCreator() throws Exception {
 String creatorName = "testIntCreator";
 Class<?> intClass = Integer.TYPE;
 AnnotatedMethod intMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addIntCreator(intMethod, true);
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(Integer.class, type.getRawClass());
 }

 @Test
 public void testLongCreator() throws Exception {
 String creatorName = "testLongCreator";
 Class<?> longClass = Long.TYPE;
 AnnotatedMethod longMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addLongCreator(longMethod, true);
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(Long.class, type.getRawClass());
 }

 @Test
 public void testDoubleCreator() throws Exception {
 String creatorName = "testDoubleCreator";
 Class<?> doubleClass = Double.TYPE;
 AnnotatedMethod doubleMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addDoubleCreator(doubleMethod, true);
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(Double.class, type.getRawClass());
 }

 @Test
 public void testBooleanCreator() throws Exception {
 String creatorName = "testBooleanCreator";
 Class<?> booleanClass = Boolean.TYPE;
 AnnotatedMethod booleanMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addBooleanCreator(booleanMethod, true);
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(Boolean.class, type.getRawClass());
 }

 @Test
 public void testDelegatingCreator() throws Exception {
 String creatorName = "testDelegatingCreator";
 Class<?> delegateType = String[].class;
 String argName = "arg";
 AnnotatedParameter argParam = new AnnotatedParameter(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class, AnnotatedParameter[].class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName), 2, argName);
 AnnotatedMethod delegateMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addDelegatingCreator(delegateMethod, true, new AnnotatedParameter[] { argParam });
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(ArrayList.class, type.getRawClass());
 }

 @Test
 public void testPropertyCreator() throws Exception {
 String creatorName = "testPropertyCreator";
 String argName1 = "arg1";
 String argName2 = "arg2";
 String[] argTypes = { String.class, String.class };
 BeanPropertyDefinition[] argProps = new BeanPropertyDefinition[] { new BeanPropertyDefinition(argName1, String.class), new BeanPropertyDefinition(argName2, String.class) };
 AnnotatedMethod method = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class, AnnotatedParameter[].class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addPropertyCreator(method, true, new CreatorProperty[] { new CreatorProperty(argProps[0], null), new CreatorProperty(argProps[1], null) });
 JavaType type = collector.constructValueInstantiator(config).getValueType();
 assertNotNull(type);
 assertEquals(LinkedHashMap.class, type.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 String creatorName = "testCreator";
 // duplicate creator
 Class<?> stringClass = String[].class;
 AnnotatedMethod stringMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addStringCreator(stringMethod, true);
 collector.addStringCreator(stringMethod, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateExplicitCreator() throws Exception {
 String creatorName = "testCreator";
 // duplicate creator, both explicitly annotated
 Class<?> stringClass = String[].class;
 AnnotatedMethod stringMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addStringCreator(stringMethod, false);
 collector.addStringCreator(stringMethod, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIncompatibleCreatorTypes() throws Exception {
 String creatorName = "testCreator";
 // incompatible creator types
 Class<?> stringClass = String[].class;
 Class<?> intClass = Integer.TYPE;
 AnnotatedMethod stringMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 AnnotatedMethod intMethod = new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod(creatorName, BeanDescription.class, boolean.class), new MethodAnnotatedWith(new ValuePropertyAnnotation(ValueInstantiator.ValueCreator.class), creatorName));
 collector.addStringCreator(stringMethod, true);
 collector.addIntCreator(intMethod, true);
 }

 @Test
 public void testIncompleteParameter() throws Exception {
 AnnotatedParameter incompleteParam = new AnnotatedParameter(CreatorCollector.class.getDeclaredMethod("testIncompleteCreator", BeanDescription.class, boolean.class), 1);
 collector.addIncompleteParameter(incompleteParam);
 collector.constructValueInstantiator(config);
 }

}