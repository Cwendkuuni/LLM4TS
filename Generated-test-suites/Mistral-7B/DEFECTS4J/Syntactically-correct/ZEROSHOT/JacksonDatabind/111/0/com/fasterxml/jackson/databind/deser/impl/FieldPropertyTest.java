package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FieldPropertyTest {

 private static final String FIELD_NAME = "testField";
 private static final String FIELD_TYPE = "java.lang.String";
 private static final String JSON_INPUT = "{\"testField\": \"testValue\"}";

 @Mock
 private JsonParser mockJsonParser;
 @Mock
 private DeserializationContext mockContext;
 @Mock
 private ObjectInstanceContext mockObjectInstanceContext;
 @Mock
 private TypeDeserializer mockTypeDeserializer;
 @Mock
 private AnnotatedField mockAnnotatedField;
 @Mock
 private JavaType mockJavaType;
 @Mock
 private Annotations mockAnnotations;
 @Mock
 private NullValueProvider mockNullValueProvider;

 private FieldProperty fieldProperty;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 BeanPropertyDefinition propertyDefinition = mock(BeanPropertyDefinition.class);
 when(propertyDefinition.getName()).thenReturn(FIELD_NAME);
 when(propertyDefinition.getType()).thenReturn(Object.class);

 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(mockField());
 when(annotatedField.getAnnotation(JsonProperty.class)).thenReturn(mockJsonProperty());
 when(annotatedField.getAnnotation(JsonTypeName.class)).thenReturn(mockJsonTypeName());
 when(annotatedField.getType()).thenReturn(Object.class);
 when(annotatedField.getMemberName()).thenReturn("testField");
 when(annotatedField.getName()).thenReturn(FIELD_NAME);

 fieldProperty = new FieldProperty(propertyDefinition, mockJavaType, mockTypeDeserializer, mockAnnotations, annotatedField);
 }

 @Test
 public void testFieldPropertyConstructor() {
 // Test constructor with no arguments
 FieldProperty fieldProperty = new FieldProperty();
 assertNull(fieldProperty._annotated);
 assertNull(fieldProperty._field);
 assertNull(fieldProperty._skipNulls);

 // Test constructor with all arguments
 BeanPropertyDefinition propertyDefinition = new BeanPropertyDefinition(FIELD_NAME, Object.class, null, null);
 FieldProperty fieldPropertyWithArgs = new FieldProperty(propertyDefinition, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
 assertSame(propertyDefinition, fieldPropertyWithArgs.getBeanPropertyDefinition());
 assertSame(mockJavaType, fieldPropertyWithArgs.getType());
 assertSame(mockTypeDeserializer, fieldPropertyWithArgs.getTypeDeserializer());
 assertSame(mockAnnotations, fieldPropertyWithArgs.getContextAnnotations());
 assertSame(mockAnnotatedField, fieldPropertyWithArgs._annotated);
 assertSame(mockAnnotatedField.getAnnotated(), fieldPropertyWithArgs._field);
 assertFalse(fieldPropertyWithArgs._skipNulls);
 }

 @Test
 public void testWithName() {
 SettableBeanProperty result = fieldProperty.withName(new PropertyName("newName"));
 assertSame(fieldProperty, result);
 assertSame(FIELD_NAME, result.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
 SettableBeanProperty result = fieldProperty.withValueDeserializer(valueDeserializer);
 assertSame(fieldProperty, result);
 assertSame(valueDeserializer, result.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 SettableBeanProperty result = fieldProperty.withNullProvider(nullValueProvider);
 assertSame(fieldProperty, result);
 assertSame(nullValueProvider, result._nullProvider);
 }

 @Test
 public void testFixAccess() {
 fieldProperty.fixAccess(mockContext);
 verify(mockField()).setAccessible(true);
 }

 @Test
 public void testGetAnnotation() {
 JsonProperty jsonProperty = mock(JsonProperty.class);
 when(mockJsonProperty().value()).thenReturn("testPropertyName");

 JsonTypeName jsonTypeName = mock(JsonTypeName.class);
 when(mockJsonTypeName().value()).thenReturn("testTypeName");

 when(mockAnnotatedField.getAnnotation(JsonProperty.class)).thenReturn(jsonProperty);
 when(mockAnnotatedField.getAnnotation(JsonTypeName.class)).thenReturn(jsonTypeName);

 Annotation result = fieldProperty.getAnnotation(JsonProperty.class);
 assertSame(jsonProperty, result);

 Annotation result2 = fieldProperty.getAnnotation(JsonTypeName.class);
 assertSame(jsonTypeName, result2);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String testValue = "testValue";
 when(mockJsonParser.getValueAsString()).thenReturn(JSON_INPUT);

 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 assertNull(fieldProperty.deserializeAndSet(mockJsonParser, mockContext, mockInstance()));

 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getValueAsString()).thenReturn(testValue);

 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, mockInstance());
 verify(mockField()).set(mockInstance(), testValue);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String testValue = "testValue";
 when(mockJsonParser.getValueAsString()).thenReturn(JSON_INPUT);

 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 assertSame(mockInstance(), fieldProperty.deserializeSetAndReturn(mockJsonParser, mockContext, mockInstance()));

 when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getValueAsString()).thenReturn(testValue);

 Object result = fieldProperty.deserializeSetAndReturn(mockJsonParser, mockContext, mockInstance());
 verify(mockField()).set(mockInstance(), testValue);
 assertSame(mockInstance(), result);
 }

 @Test
 public void testSet() throws IOException {
 String testValue = "testValue";
 fieldProperty.set(mockInstance(), testValue);
 verify(mockField()).set(mockInstance(), testValue);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 String testValue = "testValue";
 Object result = fieldProperty.setAndReturn(mockInstance(), testValue);
 verify(mockField()).set(mockInstance(), testValue);
 assertSame(mockInstance(), result);
 }

 @Test
 public void testReadResolve() {
 FieldProperty result = (FieldProperty) fieldProperty.readResolve();
 assertSame(fieldProperty, result);
 }

 private Object mockInstance() {
 return mock(Object.class);
 }

 private Field mockField() {
 return mock(Field.class);
 }

 private JsonProperty mockJsonProperty() {
 return mock(JsonProperty.class);
 }

 private JsonTypeName mockJsonTypeName() {
 return mock(JsonTypeName.class);
 }
}