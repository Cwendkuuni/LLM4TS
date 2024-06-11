package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.JsonTestUtils;
import com.fasterxml.jackson.databind.util.TypeUtil;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

 private Object testObject;
 private Object emptyObject;
 private FieldProperty testProperty;
 private FieldProperty emptyProperty;
 private JsonParser testParser;
 private DeserializationContext testContext;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 testObject = new TestClass();
 emptyObject = new TestClass();
 testParser = JsonTestUtils.createParser(getClass().getResourceAsStream("/test.json"));
 testContext = new TestObjectMapper().getDeserializationContext();
 mapper = new TestObjectMapper();

 testProperty = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "testField"),
 null, null, new AnnotatedField(new TestAnnotatedField()));

 emptyProperty = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "emptyField"),
 null, null, new AnnotatedField(new TestAnnotatedField()));
 }

 @Test
 public void testEmptySetter() throws Exception {
 FieldProperty property = emptyProperty;
 Object instance = emptyObject;
 property.set(instance, null);
 assertEquals(emptyObject, instance);
 }

 @Test
 public void testSetter() throws Exception {
 Object value = "testValue";
 FieldProperty property = testProperty;
 Object instance = testObject;
 property.set(instance, value);
 assertEquals(value, testObject.getTestField());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectParser parser = mapper.readerForUpdating(TestClass.class);
 Object value = "testValue";
 testParser.nextToken(); // consume [START_OBJECT]
 testParser.nextToken(); // consume "testField": null
 testParser.nextToken(); // consume ": "
 testParser.getValueAsString(); // consume "testValue"
 testParser.nextToken(); // consume ","
 testParser.nextToken(); // consume "emptyField": null
 testParser.nextToken(); // consume "}"
 testParser.close();
 testParser = JsonTestUtils.createParser(parser.getValueAsString());
 property.deserializeAndSet(testParser, testContext, testObject);
 assertEquals(value, testObject.getTestField());
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectParser parser = mapper.readerForUpdating(TestClass.class);
 Object value = "testValue";
 testParser.nextToken(); // consume [START_OBJECT]
 testParser.nextToken(); // consume "testField": null
 testParser.nextToken(); // consume ": "
 testParser.getValueAsString(); // consume "testValue"
 testParser.nextToken(); // consume ","
 testParser.nextToken(); // consume "emptyField": null
 testParser.nextToken(); // consume "}"
 testParser.close();
 TestClass expected = new TestClass(value, null);
 TestClass result = (TestClass) property.deserializeSetAndReturn(testParser, testContext, testObject);
 assertEquals(expected, result);
 }

 @Test
 public void testNullDeserialization() throws Exception {
 ObjectParser parser = mapper.readerForUpdating(TestClass.class);
 testParser.nextToken(); // consume [START_OBJECT]
 testParser.nextToken(); // consume "testField": null
 testParser.nextToken(); // consume ","
 testParser.nextToken(); // consume "emptyField": null
 testParser.nextToken(); // consume "}"
 testParser.close();
 TestClass expected = new TestClass(null, null);
 TestClass result = (TestClass) property.deserializeSetAndReturn(testParser, testContext, testObject);
 assertEquals(expected, result);
 }

 @Test
 public void testNullValueProvider() throws Exception {
 PropertyName propertyName = JsonTestUtils.constructPropertyName("testField");
 NullValueProvider nullValueProvider = new TestNullValueProvider();
 property = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "testField"),
 null, null, new AnnotatedField(new TestAnnotatedField()));
 property = property.withNullProvider(nullValueProvider);
 Object value = "testValue";
 testParser.nextToken(); // consume [START_OBJECT]
 testParser.nextToken(); // consume "testField": null
 testParser.nextToken(); // consume ": "
 testParser.getValueAsString(); // consume "testValue"
 testParser.nextToken(); // consume ","
 testParser.nextToken(); // consume "emptyField": null
 testParser.nextToken(); // consume "}"
 testParser.close();
 TestClass expected = new TestClass(value, null);
 TestClass result = (TestClass) property.deserializeAndSet(testParser, testContext, testObject);
 assertEquals(expected, result);
 }

 @Test
 public void testValueDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = new TestTypeDeserializer();
 property = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "testField"),
 typeDeserializer, null, new AnnotatedField(new TestAnnotatedField()));
 Object value = new TestValue();
 testParser.nextToken(); // consume [START_OBJECT]
 testParser.nextToken(); // consume "testField": null
 testParser.nextToken(); // consume ": "
 JsonTestUtils.expectValueType(testParser, TestTypeFactory.getSimpleValueType(TestClass.class, "testField"));
 testParser.nextToken(); // consume "testValue": TestValue.class
 testParser.nextToken(); // consume ","
 testParser.nextToken(); // consume "emptyField": null
 testParser.nextToken(); // consume "}"
 testParser.close();
 TestClass expected = new TestClass(null, value);
 TestClass result = (TestClass) property.deserializeAndSet(testParser, testContext, testObject);
 assertEquals(expected, result);
 }

 @Test
 public void testValueDeserializerWithType() throws Exception {
 TypeDeserializer typeDeserializer = new TestTypeDeserializer();
 TypeDeserializer valueTypeDeserializer = new TestValueTypeDeserializer();
 property = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "testField"),
 typeDeserializer, null, new AnnotatedField(new TestAnnotatedField()));
 Object value = new TestValue();
 testParser.nextToken(); // consume [START_OBJECT]
 testParser.nextToken(); // consume "testField": null
 testParser.nextToken(); // consume ": "
 JsonTestUtils.expectValueType(testParser, TestTypeFactory.getSimpleValueType(TestClass.class, "testField"));
 testParser.nextToken(); // consume "testValue": TestValue.class
 testParser.nextToken(); // consume "type": "TestValueType"
 testParser.nextToken(); // consume ","
 testParser.nextToken(); // consume "emptyField": null
 testParser.nextToken(); // consume "}"
 testParser.close();
 TestClass expected = new TestClass(null, value);
 TestClass result = (TestClass) property.deserializeAndSet(testParser, testContext, testObject);
 assertEquals(expected, result);
 }

 @Test
 public void testFixedAccess() throws Exception {
 Class<?> testClass = TestClass.class;
 Field testField = testClass.getDeclaredField("testField");
 testField.setAccessible(true);
 FieldProperty property = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "testField"),
 null, null, new AnnotatedField(new TestAnnotatedField()));
 property.fixAccess(testContext);
 Object value = "testValue";
 testParser.nextToken(); // consume [START_OBJECT]
 testParser.nextToken(); // consume "testField": null
 testParser.nextToken(); // consume ": "
 testParser.getValueAsString(); // consume "testValue"
 testParser.nextToken(); // consume ","
 testParser.nextToken(); // consume "emptyField": null
 testParser.nextToken(); // consume "}"
 testParser.close();
 TestClass expected = new TestClass(value, null);
 TestClass result = (TestClass) property.deserializeAndSet(testParser, testContext, testObject);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingField() throws Exception {
 property = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "missingField"),
 null, null, new AnnotatedField(new TestAnnotatedField()));
 }

 @Test
 public void testAnnotations() throws Exception {
 Annotation jsonPropertyAnnotation = new JsonProperty("testAnnotation");
 AnnotatedField annotatedField = new AnnotatedField(new TestAnnotatedField());
 annotatedField.setAnnotation(jsonPropertyAnnotation);
 FieldProperty property = new FieldProperty(TestPropertyDefinition.INSTANCE,
 TestTypeFactory.getSimpleValueType(TestClass.class, "testField"),
 null, null, annotatedField);
 assertEquals(jsonPropertyAnnotation, property.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testAnnotatedMember() throws Exception {
 AnnotatedMember member = property.getMember();
 assertEquals(member.getAnnotation(JsonProperty.class), testPropertyDefinition.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testClone() throws Exception {
 FieldProperty property = testProperty;
 FieldProperty clonedProperty = (FieldProperty) property.clone();
 assertEquals(property, clonedProperty);
 }

 @Test
 public void testCloneWithValueDeserializer() throws Exception {
 FieldProperty property = testProperty;
 FieldProperty clonedProperty = new FieldProperty(property, new TestJsonDeserializer(), null);
 assertEquals(property, clonedProperty);
 }

 @Test
 public void testCloneWithNullProvider() throws Exception {
 FieldProperty property = testProperty;
 FieldProperty clonedProperty = new FieldProperty(property, null, new TestNullValueProvider());
 assertEquals(property, clonedProperty);
 }

 @Test
 public void testCloneWithName() throws Exception {
 PropertyName newName = JsonTestUtils.constructPropertyName("testField2");
 FieldProperty property = testProperty;
 FieldProperty clonedProperty = new FieldProperty(property, newName);
 assertEquals(property, clonedProperty);
 }

 @Test
 public void testReadResolve() throws Exception {
 Object resolvedObject = testProperty.readResolve();
 assertEquals(testProperty, resolvedObject);
 }
}

 class TestClass {
 @JsonProperty
 private Object testField;
 private Object emptyField;

 public TestClass() {
 }

 public TestClass(Object testField, Object emptyField) {
 this.testField = testField;
 this.emptyField = emptyField;
 }

 public Object getTestField() {
 return testField;
 }
 }

 class TestAnnotatedField extends AnnotatedMember {
 @Override
 public Field getAnnotated() {
 return TestClass.class.getDeclaredField("testField");
 }

 @Override
 public BeanPropertyDefinition getBeanPropertyDefinition() {
 return TestPropertyDefinition.INSTANCE;
 }

 @Override
 public Annotation[] getValueAnnotations() {
 return new Annotation[] { new JsonProperty("testField") };
 }
 }

 class TestPropertyDefinition extends BeanPropertyDefinition {
 static final TestPropertyDefinition INSTANCE = new TestPropertyDefinition();

 @Override
 public String getName() {
 return "testField";
 }

 @Override
 public JsonGetter getGetter() {
 return null;
 }

 @Override
 public JsonSetter getSetter() {
 return null;
 }

 @Override
 public JsonTypeDeserializer getValueDeserializer() {
 return null;
 }

 @Override
 public JsonTypeSerializer getValueSerializer() {
 return null;
 }

 @Override
 public Annotation[] getAnnnotations() {
 return new Annotation[] { new JsonProperty("testField") };
 }
 }

 class TestTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserializeTypedFromObject(DeserializationContext ctxt, Object bean, Type type)
 throws IOException {
 return null;
 }

 @Override
 public Object deserializeTypedFromAny(DeserializationContext ctxt, JsonParser p, Type type)
 throws IOException {
 return null;
 }
}

 class TestValueTypeDeserializer extends TypeDeserializer {
 @Override
 public Object deserializeTypedFromObject(DeserializationContext ctxt, Object bean, Type type)
 throws IOException {
 return new TestValue();
 }

 @Override
 public Object deserializeTypedFromAny(DeserializationContext ctxt, JsonParser p, Type type)
 throws IOException {
 return new TestValue();
 }
}

 class TestValue {

 }

 class TestNullValueProvider implements NullValueProvider {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return null;
 }
}

 class TestJsonDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new TestValue();
 }
}