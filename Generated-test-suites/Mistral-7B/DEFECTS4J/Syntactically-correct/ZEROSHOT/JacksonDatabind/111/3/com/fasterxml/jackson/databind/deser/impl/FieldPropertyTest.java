package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

 private static class TestClass {
 @JsonProperty("field1")
 private int field1;
 @JsonProperty("field2")
 private String field2;

 @SuppressWarnings("unused")
 private transient Field field1Field;
 @SuppressWarnings("unused")
 private transient Field field2Field;

 private TestClass(int field1, String field2) {
 this.field1 = field1;
 this.field2 = field2;
 }

 public int getField1() {
 return field1;
 }

 public String getField2() {
 return field2;
 }
 }

 @org.junit.Test
 public void testFieldProperty() throws IOException {
 ObjectMapper mapper = new ObjectMapper();

 // Test constructor with valid arguments
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("field1", IntType.INSTANCE, null, null);
 JavaType type = mapper.getTypeFactory().construct(TestClass.class);
 TypeDeserializer typeDeser = null;
 Annotations contextAnnotations = null;
 AnnotatedField field1 = new AnnotatedField(TestClass.class.getDeclaredField("field1"), new JsonProperty("field1"));
 FieldProperty fieldProperty1 = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field1);
 assertEquals("field1", fieldProperty1.getName());
 assertEquals(IntType.INSTANCE, fieldProperty1.getType());
 assertTrue(fieldProperty1.isWritable());
 assertTrue(fieldProperty1.isReadable());

 // Test constructor with copied properties
 FieldProperty fieldProperty1Copy = new FieldProperty(fieldProperty1);
 assertEquals(fieldProperty1, fieldProperty1Copy);

 // Test constructor with renamed property
 PropertyName newName = PropertyName.construct("newName");
 FieldProperty fieldProperty1Renamed = new FieldProperty(fieldProperty1, newName);
 assertEquals(newName, fieldProperty1Renamed.getName());

 // Test deserializeAndSet method
 TestClass instance = new TestClass(0, null);
 JsonParser p = mapper.getFactory().createParser("{\"field1\": 1, \"field2\": null}");
 p.nextToken(); // [START, {"field1":1,"field2":null}]
 fieldProperty1.deserializeAndSet(p, null, instance);
 assertEquals(1, instance.getField1());

 p.nextToken(); // NULL
 fieldProperty1.deserializeAndSet(p, null, instance);
 assertEquals(0, instance.getField1());

 // Test deserializeSetAndReturn method
 Object result = fieldProperty1.deserializeSetAndReturn(p, null, instance);
 assertEquals(instance, result);

 // Test set method
 Field field1Field = TestClass.class.getDeclaredField("field1");
 field1Field.setAccessible(true);
 fieldProperty1.set(instance, 2);
 assertEquals(2, instance.getField1());

 // Test setAndReturn method
 result = fieldProperty1.setAndReturn(instance, 3);
 assertEquals(instance, result);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testMissingField() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("missingField", StringType.INSTANCE, null, null);
 JavaType type = mapper.getTypeFactory().construct(TestClass.class);
 TypeDeserializer typeDeser = null;
 Annotations contextAnnotations = null;
 AnnotatedField field = new AnnotatedField(TestClass.class.getDeclaredField("field1"), new JsonProperty("field1"));
 FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
 }
}