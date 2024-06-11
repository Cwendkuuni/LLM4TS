package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class InnerClassPropertyTest {

 private static class TestBean {
 private InnerClass innerClass;

 static class InnerClass {
 InnerClass(String s) {
 this.s = s;
 }

 String s;
 }
 }

 @SuppressWarnings("unchecked")
 static class TestSerializer extends StdSerializer<TestBean> {
 TestSerializer() {
 this(null);
 }

 TestSerializer(Class<TestBean> t) {
 super(t);
 }

 @Override
 public TestBean createInstance(DeserializationContext ctxt) throws IOException {
 return new TestBean();
 }
 }

 @SuppressWarnings("deprecation")
 @org.junit.Test
 public void testInnerClassProperty() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 TestBean bean = mapper.readValue(ObjectNode.class.getDeclaredField("root").setAccessible(true)
 .get(new ObjectNode(mapper.createObjectNode())), TestBean.class);

 // Test constructor with no arguments
 InnerClassProperty property = findProperty("innerClass", TestBean.class, null);
 Constructor<?> constructor = TestBean.InnerClass.class.getConstructor();
 InnerClassProperty instance = new InnerClassProperty(null, property, constructor);
 assertEquals(property, instance.getDelegate());
 assertEquals(constructor, instance.getCreator());

 // Test constructor with value deserializer
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 property = findProperty("innerClass", TestBean.class, stringDeserializer);
 AnnotatedConstructor constructorWithDeserializer = TestBean.InnerClass.class.getDeclaredConstructor(String.class);
 InnerClassProperty instanceWithDeserializer = new InnerClassProperty(instance, property, constructorWithDeserializer);
 assertEquals(property, instanceWithDeserializer.getDelegate());
 assertEquals(constructorWithDeserializer, instanceWithDeserializer.getCreator());
 assertEquals(stringDeserializer, instanceWithDeserializer.getValueDeserializer());

 // Test deserialization
 ObjectNode json = mapper.createObjectNode();
 json.put("innerClass", "test");
 TestBean deserializedBean = mapper.readValue(json, TestBean.class);
 assertEquals("test", deserializedBean.innerClass.s);

 // Test deserialization with transient constructor
 TestBean deserializedBeanTransient = mapper.readValue(json, TestBean.class, new SimpleModule().addDeserializer(TestBean.class, new TestSerializer()));
 assertEquals("test", deserializedBeanTransient.innerClass.s);

 // Test readResolve
 deserializedBeanTransient = (TestBean) deserializedBeanTransient.getClass().cast(deserializedBeanTransient.readResolve());
 assertEquals("test", deserializedBeanTransient.innerClass.s);

 // Test writeReplace
 ObjectNode jsonWriteReplace = mapper.createObjectNode();
 jsonWriteReplace.put("innerClass", "testWriteReplace");
 ObjectNode result = mapper.writeValueAsObject(new TestBean());
 ObjectNode expected = mapper.readTree(mapper.writeValueAsString(result.get("innerClass")));
 assertEquals(expected, jsonWriteReplace);

 // Test set and get
 ObjectNode jsonSetGet = mapper.createObjectNode();
 jsonSetGet.put("innerClass", new ObjectNode(mapper.createObjectNode()).putPOJOProperty("s", "testSetGet"));
 TestBean setGetBean = mapper.readValue(jsonSetGet, TestBean.class);
 assertEquals("testSetGet", setGetBean.innerClass.s);
 setGetBean.innerClass = new TestBean.InnerClass("testSetGetModified");
 ObjectNode resultSetGet = mapper.writeValueAsObject(setGetBean);
 ObjectNode expectedSetGet = mapper.readValue(mapper.writeValueAsString(resultSetGet), TestBean.class);
 assertEquals(expectedSetGet, setGetBean);
 }

 private static PropertyName findProperty(String name, Class<?> clazz, JsonDeserializer<?> deserializer) {
 BeanDescription beanDescription = Introspectora.findProperty(name, clazz);
 if (beanDescription == null) {
 throw new IllegalStateException("Could not find property: " + name);
 }
 return beanDescription.getBeanPropertyDefinition().getName();
 }
}