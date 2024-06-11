package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.MemberPropertyType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private static class TestBean {

 @JsonProperty("name")
 private String name;

 @JsonProperty("value")
 private int value;

 @JsonCreator(mode = Mode.PROPERTIES)
 public TestBean(@JsonProperty("name") String name, @JsonProperty("value") int value) {
 this.name = name;
 this.value = value;
 }

 @JsonCreator
 public TestBean() {
 }

 @JsonProperty
 public String getName() {
 return name;
 }

 @JsonProperty
 public int getValue() {
 return value;
 }
 }

 private ObjectMapper mapper;
 private BeanDescription beanDescription;
 private CreatorCollector collector;
 private DeserializationConfig config;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 beanDescription = mapper.findBeanDescription(TestBean.class);
 config = mapper.getConfig();
 collector = new CreatorCollector(beanDescription, true);
 }

 @Test
 public void testDefaultCreator() throws Exception {
 final AnnotatedWithParams defaultCreator = new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(), null);
 collector.setDefaultCreator(defaultCreator);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, null);
 assertNull(instance.getName());
 assertEquals(0, instance.getValue());
 }

 @Test
 public void testStringCreator() throws Exception {
 String name = "test";
 final AnnotatedWithParams stringCreator = new AnnotatedWithParams(TestBean.class.getConstructor(String.class), new Class[] { String.class });
 collector.addStringCreator(stringCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { name });
 assertEquals(name, instance.getName());
 assertNull(instance.getValue());
 }

 @Test
 public void testIntCreator() throws Exception {
 final AnnotatedWithParams intCreator = new AnnotatedWithParams(TestBean.class.getConstructor(int.class), new Class[] { int.class });
 collector.addIntCreator(intCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { 1 });
 assertNull(instance.getName());
 assertEquals(1, instance.getValue());
 }

 @Test
 public void testLongCreator() throws Exception {
 final AnnotatedWithParams longCreator = new AnnotatedWithParams(TestBean.class.getConstructor(long.class), new Class[] { long.class });
 collector.addLongCreator(longCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { 1L });
 assertNull(instance.getName());
 assertEquals(1, instance.getValue());
 }

 @Test
 public void testDoubleCreator() throws Exception {
 final AnnotatedWithParams doubleCreator = new AnnotatedWithParams(TestBean.class.getConstructor(double.class), new Class[] { double.class });
 collector.addDoubleCreator(doubleCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { 1.0 });
 assertNull(instance.getName());
 assertEquals(1, instance.getValue());
 }

 @Test
 public void testBooleanCreator() throws Exception {
 final AnnotatedWithParams booleanCreator = new AnnotatedWithParams(TestBean.class.getConstructor(boolean.class), new Class[] { boolean.class });
 collector.addBooleanCreator(booleanCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { true });
 assertNull(instance.getName());
 assertEquals(1, instance.getValue());
 }

 @Test
 public void testDelegateCreator() throws Exception {
 final Class<?> delegateType = TestDelegate.class;
 final AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateType.getDeclaredConstructor(), new Class[] {});
 final CreatorProperty[] delegateArgs = new CreatorProperty[] { new CreatorProperty() };
 collector.addDelegatingCreator(delegateCreator, true, delegateArgs);
 final JavaType delegateTypeReference = mapper.getTypeFactory().constructType(delegateType);
 collector.constructValueInstantiator(config); // needed to initialize _delegateArgs
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { new TestDelegate() });
 assertNotNull(instance.getName());
 assertNotNull(instance.getValue());
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 final AnnotatedWithParams propertyCreator = new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(String.class, int.class), new Class[] { String.class, int.class });
 final CreatorProperty nameProperty = new CreatorProperty() {
 @Override
 public String getName() {
 return "name";
 }
 };
 final CreatorProperty valueProperty = new CreatorProperty() {
 @Override
 public String getName() {
 return "value";
 }
 };
 final CreatorProperty[] propertyBasedArgs = new CreatorProperty[] { nameProperty, valueProperty };
 collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { "test", 1 });
 assertEquals("test", instance.getName());
 assertEquals(1, instance.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 final AnnotatedWithParams stringCreator = new AnnotatedWithParams(TestBean.class.getConstructor(String.class), new Class[] { String.class });
 collector.addStringCreator(stringCreator, true);
 final AnnotatedWithParams stringCreator2 = new AnnotatedWithParams(TestBean.class.getConstructor(String.class), new Class[] { String.class });
 collector.addStringCreator(stringCreator2, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateStringCreator() throws Exception {
 final AnnotatedWithParams stringCreator = new AnnotatedWithParams(TestBean.class.getConstructor(String.class), new Class[] { String.class });
 collector.addStringCreator(stringCreator, true);
 final AnnotatedWithParams stringCreator2 = new AnnotatedWithParams(TestBean.class.getConstructor(String.class), new Class[] { String.class });
 collector.addStringCreator(stringCreator2, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateIntCreator() throws Exception {
 final AnnotatedWithParams intCreator = new AnnotatedWithParams(TestBean.class.getConstructor(int.class), new Class[] { int.class });
 collector.addIntCreator(intCreator, true);
 final AnnotatedWithParams intCreator2 = new AnnotatedWithParams(TestBean.class.getConstructor(int.class), new Class[] { int.class });
 collector.addIntCreator(intCreator2, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateLongCreator() throws Exception {
 final AnnotatedWithParams longCreator = new AnnotatedWithParams(TestBean.class.getConstructor(long.class), new Class[] { long.class });
 collector.addLongCreator(longCreator, true);
 final AnnotatedWithParams longCreator2 = new AnnotatedWithParams(TestBean.class.getConstructor(long.class), new Class[] { long.class });
 collector.addLongCreator(longCreator2, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDoubleCreator() throws Exception {
 final AnnotatedWithParams doubleCreator = new AnnotatedWithParams(TestBean.class.getConstructor(double.class), new Class[] { double.class });
 collector.addDoubleCreator(doubleCreator, true);
 final AnnotatedWithParams doubleCreator2 = new AnnotatedWithParams(TestBean.class.getConstructor(double.class), new Class[] { double.class });
 collector.addDoubleCreator(doubleCreator2, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateBooleanCreator() throws Exception {
 final AnnotatedWithParams booleanCreator = new AnnotatedWithParams(TestBean.class.getConstructor(boolean.class), new Class[] { boolean.class });
 collector.addBooleanCreator(booleanCreator, true);
 final AnnotatedWithParams booleanCreator2 = new AnnotatedWithParams(TestBean.class.getConstructor(boolean.class), new Class[] { boolean.class });
 collector.addBooleanCreator(booleanCreator2, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDelegateCreator() throws Exception {
 final Class<?> delegateType = TestDelegate.class;
 final AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateType.getDeclaredConstructor(), new Class[] {});
 final CreatorProperty[] delegateArgs = new CreatorProperty[] { new CreatorProperty() };
 collector.addDelegatingCreator(delegateCreator, true, delegateArgs);
 final AnnotatedWithParams delegateCreator2 = new AnnotatedWithParams(delegateType.getDeclaredConstructor(), new Class[] {});
 collector.addDelegatingCreator(delegateCreator2, true, delegateArgs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicatePropertyCreator() throws Exception {
 final AnnotatedWithParams propertyCreator = new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(String.class, int.class), new Class[] { String.class, int.class });
 final CreatorProperty nameProperty = new CreatorProperty() {
 @Override
 public String getName() {
 return "name";
 }
 };
 final CreatorProperty valueProperty = new CreatorProperty() {
 @Override
 public String getName() {
 return "value";
 }
 };
 final CreatorProperty[] propertyBasedArgs = new CreatorProperty[] { nameProperty, valueProperty };
 collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
 final AnnotatedWithParams propertyCreator2 = new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(String.class, int.class), new Class[] { String.class, int.class });
 collector.addPropertyCreator(propertyCreator2, true, propertyBasedArgs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicatePropertyCreatorWithDuplicateName() throws Exception {
 final AnnotatedWithParams propertyCreator = new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(String.class, int.class), new Class[] { String.class, int.class });
 final CreatorProperty nameProperty = new CreatorProperty() {
 @Override
 public String getName() {
 return "name";
 }
 };
 final CreatorProperty valueProperty = new CreatorProperty() {
 @Override
 public String getName() {
 return "name";
 }
 };
 final CreatorProperty[] propertyBasedArgs = new CreatorProperty[] { nameProperty, valueProperty };
 collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
 final AnnotatedWithParams propertyCreator2 = new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(String.class, int.class), new Class[] { String.class, int.class });
 collector.addPropertyCreator(propertyCreator2, true, propertyBasedArgs);
 }

 @Test
 public void testCanFixAccess() throws Exception {
 final TestBean testBean = new TestBean();
 final BeanPropertyDefinition nameDef = AnnotationIntrospector.findProperty("name", beanDescription);
 final BeanPropertyDefinition valueDef = AnnotationIntrospector.findProperty("value", beanDescription);
 final Method nameSetter = TestBean.class.getMethod("setName", String.class);
 final Method valueSetter = TestBean.class.getMethod("setValue", int.class);
 final AnnotatedWithParams nameSetterAnnotated = new AnnotatedWithParams(nameSetter, new Class[] { String.class });
 final AnnotatedWithParams valueSetterAnnotated = new AnnotatedWithParams(valueSetter, new Class[] { int.class });
 collector = new CreatorCollector(beanDescription, false);
 collector.setDefaultCreator(null);
 collector.addStringCreator(new AnnotatedWithParams(TestBean.class.getConstructor(String.class), new Class[] { String.class }), false);
 collector.addIntCreator(new AnnotatedWithParams(TestBean.class.getConstructor(int.class), new Class[] { int.class }), false);
 collector.addPropertyCreator(new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(String.class, int.class), new Class[] { String.class, int.class }), false, new CreatorProperty[] { nameProperty, valueProperty });
 collector.addIncompleteParameter(new AnnotatedParameter(valueDef));
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 TestBean instance = instantiator.createValueInstance(config, new Object[] { "test", 1 });
 assertEquals("test", instance.getName());
 assertEquals(1, instance.getValue());
 // test fix access
 testBean.setName("fixed");
 testBean.setValue(2);
 TestBean instance2 = instantiator.createValueInstance(config, null);
 assertEquals("fixed", instance2.getName());
 assertEquals(2, instance2.getValue());
 }

 private static class TestDelegate {
 }
}