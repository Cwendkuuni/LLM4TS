package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

 private AnnotatedField annotatedField;
 private AnnotatedMember annotatedMember;
 private BeanPropertyDefinition propDef;
 private FieldProperty fieldProperty;
 private Object instance;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object type;
 private TypeDeserializer typeDeserializer;
 private JsonDeserializer<?> valueDeserializer;
 private NullValueProvider nullValueProvider;

 @Before
 public void setUp() throws Exception {
 annotatedField = mock(AnnotatedField.class);
 annotatedMember = mock(AnnotatedMember.class);
 when(annotatedField.getAnnotated()).thenReturn(annotatedMember);
 propDef = mock(BeanPropertyDefinition.class);
 type = mock(JavaType.class);
 typeDeserializer = mock(TypeDeserializer.class);
 valueDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 instance = new Object();
 fieldProperty = new FieldProperty(propDef, (JavaType) type, typeDeserializer, Annotations.empty(), annotatedField);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 FieldProperty defaultFieldProperty = new FieldProperty();
 assertNull(defaultFieldProperty._annotated);
 assertNull(defaultFieldProperty._field);
 assertFalse(defaultFieldProperty._skipNulls);

 // Test constructor with arguments
 Field field = new Field(null, null, null, null);
 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(annotatedMember);
 FieldProperty fieldProperty = new FieldProperty(propDef, (JavaType) type, typeDeserializer, annotatedField);
 assertNotNull(fieldProperty._annotated);
 assertSame(annotatedField, fieldProperty._annotated);
 assertSame(field, fieldProperty._field);
 assertFalse(fieldProperty._skipNulls);
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = mock(PropertyName.class);
 SettableBeanProperty result = fieldProperty.withName(newName);
 assertSame(fieldProperty, result);
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
 SettableBeanProperty result = fieldProperty.withValueDeserializer(deserializer);
 assertSame(fieldProperty, result);
 assertSame(deserializer, result._valueDeserializer);
 }

 @Test
 public void testWithNullProvider() throws Exception {
 NullValueProvider nullProvider = mock(NullValueProvider.class);
 SettableBeanProperty result = fieldProperty.withNullProvider(nullProvider);
 assertSame(fieldProperty, result);
 assertSame(nullProvider, result._nullProvider);
 }

 @Test
 public void testFixAccess() throws Exception {
 fieldProperty.fixAccess(deserializationContext);
 ClassUtil.checkAccess(instance.getClass().getDeclaredField("testField"), true);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Class<? extends Annotation> annotationType = mock(Annotation.class);
 Object annotation = mock(Annotation.class);
 when(annotatedField.getAnnotation(annotationType)).thenReturn(annotation);
 Object result = fieldProperty.getAnnotation(annotationType);
 assertSame(annotation, result);
 }

 @Test
 public void testGetMember() throws Exception {
 assertSame(annotatedMember, fieldProperty.getMember());
 }

 @Test
 public void testSerializeAndSet() throws Exception {
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("testValue");
 when(typeDeserializer.deserialize(jsonParser, deserializationContext, _ -> null)).thenReturn("testValue");
 when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);

 fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 Object expectedValue = "testValue";
 assertSame(expectedValue, fieldProperty._field.get(instance));
 }

 @Test
 public void testSerializeSetAndReturn() throws Exception {
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenReturn("testValue");
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("testValue");
 when(typeDeserializer.deserialize(jsonParser, deserializationContext, _ -> null)).thenReturn("testValue");
 when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);

 Object result = fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertSame(instance, result);
 assertSame(expectedValue, fieldProperty._field.get(instance));
 }

 @Test
 public void testSet() throws Exception {
 Object value = new Object();
 fieldProperty.set(instance, value);
 assertSame(value, fieldProperty._field.get(instance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object value = new Object();
 Object result = fieldProperty.setAndReturn(instance, value);
 assertSame(instance, result);
 assertSame(value, fieldProperty._field.get(instance));
 }

 @Test
 public void testReadResolve() throws Exception {
 Object result = fieldProperty.readResolve();
 assertInstanceOf(FieldProperty.class, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullAnnotatedField() throws Exception {
 FieldProperty fieldProperty = new FieldProperty(propDef, (JavaType) type, typeDeserializer, null);
 }
}