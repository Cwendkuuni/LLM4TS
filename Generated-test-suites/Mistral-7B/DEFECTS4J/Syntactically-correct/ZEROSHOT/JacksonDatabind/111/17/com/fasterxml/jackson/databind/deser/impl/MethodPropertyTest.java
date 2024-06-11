package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.AnnotationIntrospector;

public class MethodPropertyTest {

 private MethodProperty methodProperty;
 private Object testObject;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TestBean testBean = new TestBean();
 JsonSerializer<TestBean> serializer = mapper.finderForSerializable(TestBean.class)
 .withResolvedSerializer(TestBeanSerializer.instance);
 mapper.registerSerializer(TestBean.class, serializer);
 ObjectWriter writer = mapper.writerFor(TestBean.class);
 String json = writer.writeValueAsString(testBean);
 JsonParser parser = mapper.parserForStandardDeserialization();
 parser.readValueAs(TestBean.class); // read the json to prime the parser

 Class<?> clazz = TestBean.class;
 Method setFooMethod = clazz.getMethod("setFoo", String.class);
 methodProperty = new MethodProperty(new AnnotatedMethod(setFooMethod),
 new TypeDeserializer() {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
 TypeDeserializer valueTypeDeserializer) throws IOException {
 return p.getValueAsString();
 }
 }, null, new AnnotationIntrospector().findAnnotation(clazz, JsonDeserialize.class));
 testObject = new TestBean();
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String testValue = "testValue";
 methodProperty.deserializeAndSet(parser, new TestDeserializationContext(), testObject);
 assertEquals(testValue, methodProperty.getName().get(testObject));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String testValue = "testValue";
 Object testResult = methodProperty.deserializeSetAndReturn(parser, new TestDeserializationContext(), testObject);
 assertEquals(testObject, testResult);
 assertEquals(testValue, methodProperty.getName().get(testObject));
 }

 @Test
 public void testSet() throws IOException, IllegalAccessException, InvocationTargetException {
 String testValue = "testValue";
 methodProperty.set(testObject, testValue);
 assertEquals(testValue, methodProperty.getName().get(testObject));
 }

 @Test
 public void testSetAndReturn() throws IOException, IllegalAccessException, InvocationTargetException {
 String testValue = "testValue";
 Object testResult = methodProperty.setAndReturn(testObject, testValue);
 assertEquals(testObject, testResult);
 assertEquals(testValue, methodProperty.getName().get(testObject));
 }

 @Test
 public void testSkipNulls() throws IOException {
 assertNull(methodProperty.deserializeAndSet(parser, new TestDeserializationContext(), testObject));
 assertNull(testObject);
 }

 @Test
 public void testFixAccess() throws IOException {
 methodProperty.fixAccess(new TestDeserializationConfig());
 assertEquals("private final java.lang.reflect.Method _setter",
 methodProperty.getAnnotated().getModifiersAsInt() + "",
 "_annotated.getModifiers()");
 }

}

class TestBean {
 private String foo;

 public String getFoo() {
 return foo;
 }

 @JsonDeserialize
 public void setFoo(String value) {
 this.foo = value;
 }
}

class TestBeanSerializer extends JsonSerializer<TestBean> {
 static final TestBeanSerializer instance = new TestBeanSerializer();

 @Override
 public void serialize(TestBean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString(value.getFoo());
 }
}

class TestDeserializationContext extends DeserializationContext {
 @Override
 public ResultTypeTypeResolver typeResolver() {
 return new ResultTypeTypeResolver() {
 @Override
 public JavaType resolveType(DeserializationConfig config, BeanProperty property, TypeDeserializer valueDeserializer) {
 return null;
 }
 };
 }

 @Override
 public Object findValueInRoot(String name) {
 return null;
 }
}

class TestDeserializationConfig extends DeserializationConfig {
 @Override
 public Object findBeanProperty(BeanPropertyDefinition propertyDefinition,
 JavaType type) {
 return null;
 }
}