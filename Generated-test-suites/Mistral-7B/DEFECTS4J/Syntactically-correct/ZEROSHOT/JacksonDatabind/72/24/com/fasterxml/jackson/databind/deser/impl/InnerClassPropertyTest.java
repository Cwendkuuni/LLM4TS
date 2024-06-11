package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedIntrospector;
import com.fasterxml.jackson.databind.json.JsonTestBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class InnerClassPropertyTest extends JsonTestBase {

 private static class DummyClass {
 private InnerClass inner = new InnerClass();
 }

 private static class DummyClass$Inner {
 }

 private static class InnerClass {
 private int i;
 }

 private BeanPropertyDefinition p;
 private AnnotatedClass ac;
 private InnerClassProperty icp;
 private JsonDeserializer<InnerClass> deserializer;
 private JsonSerializer<InnerClass> serializer;

 @Before
 public void setUp() throws Exception {
 ac = new AnnotatedClass(DummyClass.class);
 NestedIntrospector ni = new NestedIntrospector(ObjectMapper.getDefaultInstance(), ac);
 p = ni.findProperty("inner");
 deserializer = Mockito.mock(JsonDeserializer.class);
 serializer = new StdSerializer<InnerClass>(InnerClass.class) {};
 icp = new InnerClassProperty(p, Mockito.mock(Constructor.class), deserializer, serializer);
 }

 @Test
 public void testInnerClassProperty_defaultConstructor() throws Exception {
 InnerClass result = icp.get(new DummyClass(), null);
 assertNotNull(result);
 assertEquals(0, result.i);
 }

 @Test
 public void testInnerClassProperty_customConstructor() throws Exception {
 Constructor<InnerClass> constructor = new DummyClass$Inner().getClass().getDeclaredConstructor();
 constructor.setAccessible(true);
 InnerClass result = icp.get(new DummyClass(), constructor.newInstance());
 assertNotNull(result);
 assertEquals(0, result.i);

 // Test setting value
 InnerClass value = new InnerClass();
 value.i = 42;
 icp.set(new DummyClass(), value);
 InnerClass actual = icp.get(new DummyClass(), null);
 assertEquals(value, actual);
 }

 @Test
 public void testInnerClassProperty_deserialize() throws Exception {
 ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 String json = "{\"inner\":{\"i\":1}}";
 DummyClass dummy = mapper.readValue(json, DummyClass.class);
 InnerClass inner = icp.get(dummy, null);
 assertEquals(1, inner.i);
 }

 @Test
 public void testInnerClassProperty_deserializeWithType() throws Exception {
 ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 String json = "{\"inner\":{\"i\":1}}";
 DummyClass dummy = mapper.readValue(json, DummyClass.class);
 InnerClass inner = mapper.readerForUpdating(DummyClass.class)
 .readValue(json);
 assertEquals(1, inner.inner.i);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInnerClassProperty_readResolve() throws Exception {
 Object obj = icp.readResolve();
 fail("readResolve should not be called");
 }

 @Test
 public void testInnerClassProperty_writeReplace() throws Exception {
 Object obj = icp;
 Object obj2 = icp.writeReplace();
 assertNotSame(obj, obj2);
 }

 @Test
 public void testInnerClassProperty_deserializeWithValueDeserializer() throws Exception {
 JsonParser jp = createParser(new ObjectMapper(), "[1]");
 JsonDeserializer<Integer> deserializer = Mockito.mock(JsonDeserializer.class);
 InnerClassProperty innerClassProperty = new InnerClassProperty(p, deserializer);
 ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 mapper.setDeserializationConfig(MapperConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
 Object result = mapper.readValue(jp, DummyClass.class);
 assertEquals(1, result.inner.i);
 }
}