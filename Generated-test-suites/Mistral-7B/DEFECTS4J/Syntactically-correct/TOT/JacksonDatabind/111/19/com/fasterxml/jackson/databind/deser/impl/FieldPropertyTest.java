package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class FieldPropertyTest {

 private static final String TEST_CLASS_NAME = "TestClass";
 private static final String FIELD_NAME = "testField";
 private static final Class<?> FIELD_TYPE = Integer.class;
 private static final int FIELD_VALUE = 1;

 private ObjectMapper objectMapper;
 private JsonDeserializer<Object> valueDeserializer;
 private NullValueProvider nullValueProvider;
 private TypeDeserializer typeDeserializer;
 private AnnotatedField annotatedField;
 private FieldProperty fieldProperty;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 valueDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 typeDeserializer = mock(TypeDeserializer.class);
 annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(mock(Field.class));
 when(annotatedField.getType()).thenReturn(FIELD_TYPE);
 when(annotatedField.getName()).thenReturn(FIELD_NAME);
 fieldProperty = new FieldProperty(
 new BeanPropertyDefinition(FIELD_NAME, Object.class, null, null),
 JsonTypeFactory.instance.constructType(TEST_CLASS_NAME),
 typeDeserializer,
 Annotations.empty(),
 annotatedField);
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 Object testInstance = mock(TEST_CLASS_NAME);
 when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(FIELD_VALUE);

 fieldProperty.deserializeAndSet(objectMapper.getFactory().createParser(getClass().getResourceAsStream("/fixtures/valid_json.json")),
 objectMapper.getDeserializationContext(), testInstance);

 verify(testInstance, times(1)).setInt(eq(FIELD_NAME), eq(FIELD_VALUE));
 }

 @Test
 public void testDeserializeAndSet_NullValue() throws JsonProcessingException {
 Object testInstance = mock(TEST_CLASS_NAME);

 fieldProperty.deserializeAndSet(objectMapper.getFactory().createParser(getClass().getResourceAsStream("/fixtures/null_json.json")),
 objectMapper.getDeserializationContext(), testInstance);

 if (fieldProperty._skipNulls) {
 verifyNoInteractions(testInstance);
 } else {
 verify(testInstance, times(1)).setInt(eq(FIELD_NAME), null);
 }
 }

 @Test
 public void testDeserializeAndSet_JsonParserException() throws JsonProcessingException {
 Object testInstance = mock(TEST_CLASS_NAME);
 doThrow(new JsonProcessingException("Test error")).when(objectMapper.getFactory())
 .createParser(getClass().getResourceAsStream("/fixtures/error_json.json"));

 fieldProperty.deserializeAndSet(objectMapper.getFactory().createParser(getClass().getResourceAsStream("/fixtures/error_json.json")),
 objectMapper.getDeserializationContext(), testInstance);

 verifyNoInteractions(testInstance);
 }

 @Test
 public void testSet() throws Exception {
 Object testInstance = mock(TEST_CLASS_NAME);
 setFieldValue(testInstance, FIELD_NAME, FIELD_VALUE);

 fieldProperty.set(testInstance, FIELD_VALUE);

 verify(testInstance, times(1)).setInt(eq(FIELD_NAME), eq(FIELD_VALUE));
 }

 @Test
 public void testSet_NullValue() throws Exception {
 Object testInstance = mock(TEST_CLASS_NAME);
 setFieldValue(testInstance, FIELD_NAME, null);

 fieldProperty.set(testInstance, null);

 if (fieldProperty._skipNulls) {
 verifyNoInteractions(testInstance);
 } else {
 verify(testInstance, times(1)).setInt(eq(FIELD_NAME), null);
 }
 }

 @Test
 public void testSet_JsonProcessingException() throws Exception {
 Object testInstance = mock(TEST_CLASS_NAME);
 doThrow(new JsonProcessingException("Test error")).when(objectMapper).readValue(anyString(), any(Type.class));

 try {
 fieldProperty.set(testInstance, FIELD_VALUE);
 } catch (JsonProcessingException e) {
 // Expected
 }

 verifyNoInteractions(testInstance);
 }

 private void setFieldValue(Object testInstance, String fieldName, Object fieldValue) throws IllegalAccessException {
 Field field = testInstance.getClass().getField(fieldName);
 field.setAccessible(true);
 field.set(testInstance, fieldValue);
 }
}