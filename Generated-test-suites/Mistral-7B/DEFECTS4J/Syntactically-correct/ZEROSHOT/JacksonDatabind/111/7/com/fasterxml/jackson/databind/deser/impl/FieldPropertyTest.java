package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

 private static final String FIELD_NAME = "testField";
 private static final Class<Integer> INT_TYPE = Integer.class;
 private static final Class<String> STRING_TYPE = String.class;
 private static final JsonDeserializer<String> STRING_DESERIALIZER = mock(JsonDeserializer.class);

 @BeforeClass
 public static void setup() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(new AnnotatedMember() {
 @Override public Field getAnnotated() {
 return new Field() {
 @Override public String getName() {
 return FIELD_NAME;
 }
 @Override public Class<?> getType() {
 return INT_TYPE;
 }
 @Override public void setAccessible(boolean b) {
 }
 };
 }});
 when(annotatedField.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty());
 when(annotatedField.getAnnotation(JsonValue.class)).thenReturn(new JsonValue());
 when(annotatedField.getAnnotation(JsonDeserialize.class)).thenReturn(new JsonDeserialize() {
 @Override public JsonDeserializer<?> getContentDeserializer() {
 return STRING_DESERIALIZER;
 }
 });
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(FIELD_NAME, INT_TYPE, null, null, null, null, null, null);
 TypeDeserializer typeDeser = null;
 JsonDeserializer<Object> valueDeserializer = null;
 FieldProperty fieldProperty = new FieldProperty(propDef, INT_TYPE, typeDeser, new Annotations(), annotatedField);
 }

 @Test
 public void testSetAndGet() throws Exception {
 Object instance = new Object();
 int value = 42;
 FieldProperty fieldProperty = new FieldProperty(propDef, INT_TYPE, typeDeser, new Annotations(), annotatedField);
 fieldProperty.set(instance, value);
 assertEquals(value, fieldProperty.get(instance));
 }

 @Test
 public void testDeserializeNull() throws Exception {
 Object instance = new Object();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertNull(instance.getClass().getDeclaredField(FIELD_NAME).get(instance));
 }

 @Test
 public void testDeserializeValue() throws Exception {
 Object instance = new Object();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(STRING_DESERIALIZER.deserialize(jsonParser, deserializationContext)).thenReturn("test");
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals("test", fieldProperty.get(instance));
 }

 @Test
 public void testDeserializeWithType() throws Exception {
 Object instance = new Object();
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(STRING_DESERIALIZER.deserializeWithType(jsonParser, deserializationContext, STRING_TYPE)).thenReturn("test");
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals("test", fieldProperty.get(instance));
 }

 @Test
 public void testSet() throws Exception {
 Object instance = new Object();
 String value = "test";
 fieldProperty.set(instance, value);
 assertEquals(value, fieldProperty.get(instance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object instance = new Object();
 String value = "test";
 Object returnValue = new Object();
 fieldProperty.setAndReturn(instance, value);
 assertEquals(instance, returnValue);
 assertEquals(value, fieldProperty.get(instance));
 }

 @Test
 public void testReadResolve() throws Exception {
 Object instance = new Object();
 fieldProperty.set(instance, "test");
 Object resolvedObject = new FieldProperty(fieldProperty);
 assertEquals("test", resolvedObject.get(instance));
 }
}