package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

public class SetterlessPropertyTest extends ObjectMapperTestBase {

 private static class TestBean {
 private List<String> list;

 @JsonProperty("list")
 public List<String> getList() {
 return list;
 }

 public void setList(List<String> list) {
 this.list = list;
 }
 }

 @SuppressWarnings("unchecked")
 private static SettableBeanProperty<TestBean, List<String>> getSetterlessProperty(ObjectMapper mapper)
 throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 BeanPropertyDefinition def = mapper.findPropertyDefinition(TestBean.class, "list");
 TypeDeserializer typeDeser = mapper.getTypeDeserializerForProperty(def);
 AnnotatedMethod method = def.getGetter();
 return (SettableBeanProperty<TestBean, List<String>>) mapper.findPropertyValueDeserializer(def)
 .findPropertyResolver(TestBean.class, def, mapper.getFactory());
 }

 @SuppressWarnings("unchecked")
 private static void testDeserializeAndSet(SettableBeanProperty<TestBean, List<String>> property, ObjectMapper mapper)
 throws IOException {
 TestBean instance = new TestBean();
 JsonParser p = mapper.getFactory().createParser("['element1', 'element2']");
 property.deserializeAndSet(p, mapper, instance);
 assertEquals("['element1', 'element2']", instance.getList().toString());
 }

 @SuppressWarnings("unchecked")
 private static void testDeserializeSetAndReturn(SettableBeanProperty<TestBean, List<String>> property, ObjectMapper mapper)
 throws IOException {
 TestBean instance = new TestBean();
 JsonParser p = mapper.getFactory().createParser("['element1', 'element2']");
 Object result = property.deserializeSetAndReturn(p, mapper, instance);
 assertEquals(instance, result);
 }

 @SuppressWarnings("unchecked")
 private static void testSetAndReturn(SettableBeanProperty<TestBean, List<String>> property, ObjectMapper mapper)
 throws IOException {
 TestBean instance = new TestBean();
 List<String> value = new ArrayList<>();
 value.add("newElement");
 property.setAndReturn(instance, value);
 assertEquals("newElement", instance.getList().get(0));
 }

 @SuppressWarnings("unchecked")
 private static void testThrowExceptionWhenSetCalled(SettableBeanProperty<TestBean, List<String>> property) {
 TestBean instance = new TestBean();
 assertThrows(UnsupportedOperationException.class, () -> property.set(instance, null));
 }

 @org.junit.Test
 public void testSetterlessProperty() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 SettableBeanProperty<TestBean, List<String>> property = getSetterlessProperty(mapper);
 testDeserializeAndSet(property, mapper);
 testDeserializeSetAndReturn(property, mapper);
 testSetAndReturn(property, mapper);
 testThrowExceptionWhenSetCalled(property);
 }

}