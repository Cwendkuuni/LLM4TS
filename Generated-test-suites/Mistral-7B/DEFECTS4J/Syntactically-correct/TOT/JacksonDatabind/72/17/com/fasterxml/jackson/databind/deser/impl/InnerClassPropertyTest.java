package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InnerClassPropertyTest {

 private static final Object MockedBean = new Object() {};
 private static final Object MockedInnerClass = new Object() {};
 private static final Class<? extends Object> MockedInnerClassType = MockedInnerClass.getClass();
 private static final Constructor<? extends Object> MockedInnerClassConstructor = MockedInnerClassType.getDeclaredConstructor();
 private static final AnnotatedConstructor<?> mockedAnnotatedConstructor = new AnnotatedConstructor<>(null, MockedInnerClassConstructor, null, null);
 private static final JsonDeserializer<?> mockedDeserializer = new JsonDeserializer<Object>() {};
 private static final PropertyName mockedPropertyName = PropertyName.construct("testProperty");

 @Before
 public void setUp() {
 super.setUp();
 InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(new PropertyName("testDelegateProperty"), JsonTestUtil.createSimpleContextDeserializer()), mockedAnnotatedConstructor);
 property = new InnerClassProperty(property, mockedDeserializer);
 property = new InnerClassProperty(property, mockedPropertyName);
 }

 @Test
 public void testDefaultConstructor() throws IOException {
 InnerClassProperty property = new InnerClassProperty(null, null);
 assertNull(property._delegate);
 assertNull(property._creator);
 assertNull(property._annotated);
 }

 @Test
 public void testSettableBeanPropertyDelegate() throws IOException {
 Object bean = MockedBean;
 assertNull(property.getDelegate().get(bean));
 property.setValue(bean, MockedInnerClass);
 assertEquals(MockedInnerClass, property.getDelegate().get(bean));
 }

 @Test
 public void testSettableBeanPropertyWithValueDeserializer() throws IOException {
 Object bean = MockedBean;
 JsonParser parser = new JsonTestBuilder().buildParser(new ObjectMapper().writeValueAsString(new ObjectNode().put("test", "testInnerClass")));
 property.deserializeAndSet(parser, JsonTestUtil.createSimpleContext(), bean);
 assertEquals(MockedInnerClass, property.getDelegate().get(bean));
 }

 @Test
 public void testTransientConstructor() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
 Object bean = MockedBean;
 property.deserializeAndSet(new JsonTestBuilder().buildParser("null"), JsonTestUtil.createSimpleContext(), bean);
 assertNull(property.getDelegate().get(bean));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws IOException {
 InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(new PropertyName("testDelegateProperty"), JsonTestUtil.createSimpleContextDeserializer()), null);
 }

 @Test
 public void testGetAnnotation() {
 assertNull(property.getAnnotation(String.class));
 }

 @Test
 public void testGetMember() {
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object bean = MockedBean;
 JsonParser parser = new JsonTestBuilder().buildParser(new ObjectMapper().writeValueAsString(new ObjectNode().put("test", "testInnerClass")));
 property.deserializeAndSet(parser, JsonTestUtil.createSimpleContext(), bean);
 assertEquals(MockedInnerClass, property.getDelegate().get(bean));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object bean = MockedBean;
 JsonParser parser = new JsonTestBuilder().buildParser(new ObjectMapper().writeValueAsString(new ObjectNode().put("test", "testInnerClass")));
 assertEquals(MockedInnerClass, property.deserializsetAndReturn(parser, JsonTestUtil.createSimpleContext(), bean));
 }

 @Test
 public void testSet() throws IOException {
 Object bean = MockedBean;
 property.set(bean, MockedInnerClass);
 assertEquals(MockedInnerClass, property.getDelegate().get(bean));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object bean = MockedBean;
 assertEquals(MockedBean, property.setAndReturn(bean, MockedInnerClass));
 assertEquals(MockedInnerClass, property.getDelegate().get(bean));
 }

 @Test
 public void testReadResolve() {
 InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(new PropertyName("testDelegateProperty"), JsonTestUtil.createSimpleContextDeserializer()), mockedAnnotatedConstructor);
 property = (InnerClassProperty) property.readResolve();
 assertNotNull(property._delegate);
 assertNull(property._creator);
 assertNotNull(property._annotated);
 }

 @Test
 public void testWriteReplace() {
 InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(new PropertyName("testDelegateProperty"), JsonTestUtil.createSimpleContextDeserializer()), mockedAnnotatedConstructor);
 Object proxy = property.writeReplace();
 assertInstanceOf(InnerClassProperty.class, proxy);
 }
}