package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private ObjectNode beanNode;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() {
 config = new SerializationConfig();
 ObjectMapper objectMapper = new ObjectMapper(config);
 beanNode = objectMapper.createObjectNode();
 beanNode.put("name", "John Doe");
 beanNode.put("age", 30);
 }

 @Test
 public void testBuilder() {
 BeanDescription beanDescription = new BeanDescription(Object.class);
 propertyBuilder = new PropertyBuilder(config, beanDescription);
 assertNotNull(propertyBuilder);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations classAnnotations = propertyBuilder.getClassAnnotations();
 assertNotNull(classAnnotations);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", JsonType.STRING_TYPE, null);
 JavaType declaredType = JsonTypeFactory.valueType(String.class);
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getGenericType()).thenReturn(declaredType);
 when(am.getName()).thenReturn("name");

 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 assertNotNull(actualWriter);
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithException() throws JsonMappingException {
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", JsonType.STRING_TYPE, null);
 JavaType declaredType = JsonTypeFactory.valueType(String.class);
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getGenericType()).thenReturn(declaredType);
 when(am.getName()).thenReturn("name");
 when(am.getRawType()).thenThrow(new JsonMappingException("Test exception"));

 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
 fail("Unexpected result");
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getName()).thenReturn("name");
 JavaType declaredType = JsonTypeFactory.valueType(String.class);
 when(propertyBuilder._annotationIntrospector.findSerializationType(am, false, declaredType)).thenReturn(JsonTypeFactory.valueType(String.class));

 JavaType actualType = propertyBuilder.findSerializationType(am, false, declaredType);
 assertEquals(JsonTypeFactory.valueType(String.class), actualType);
 }

 @Test
 public void testFindSerializationTypeWithException() throws JsonMappingException {
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getName()).thenReturn("name");
 JavaType declaredType = JsonTypeFactory.valueType(String.class);
 when(propertyBuilder._annotationIntrospector.findSerializationType(am, false, declaredType)).thenThrow(new JsonMappingException("Test exception"));

 try {
 propertyBuilder.findSerializationType(am, false, declaredType);
 fail("Unexpected result");
 } catch (JsonMappingException e) {
 assertEquals("Test exception", e.getMessage());
 }
}

 @Test
 public void testGetPropertyDefaultValue() {
 String propertyName = "name";
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getName()).thenReturn(propertyName);
 JavaType type = JsonTypeFactory.valueType(String.class);
 when(propertyBuilder._beanDesc.getClassInfo()).thenReturn(new ClassInfo(beanNode.getClass()));
 when(propertyBuilder._beanDesc.getPropertyDefinition(propertyName)).thenReturn(new BeanPropertyDefinition(propertyName, JsonType.STRING_TYPE, null));

 Object actualDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, am, type);
 assertEquals("", actualDefaultValue);
 }

 @Test
 public void testGetPropertyDefaultValueWithException() {
 String propertyName = "name";
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(am.getName()).thenReturn(propertyName);
 JavaType type = JsonTypeFactory.valueType(String.class);
 when(propertyBuilder._beanDesc.getClassInfo()).thenReturn(new ClassInfo(beanNode.getClass()));
 when(propertyBuilder._beanDesc.getPropertyDefinition(propertyName)).thenThrow(new JsonMappingException("Test exception"));

 try {
 propertyBuilder.getPropertyDefaultValue(propertyName, am, type);
 fail("Unexpected result");
 } catch (JsonMappingException e) {
 assertEquals("Test exception", e.getMessage());
 }
}

 @Test
 public void testGetDefaultBean() {
 Object actualDefaultBean = propertyBuilder.getDefaultBean();
 assertNull(actualDefaultBean);
 }

 @Test
 public void testGetDefaultValue() {
 JavaType type = JsonTypeFactory.valueType(String.class);
 Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
 assertEquals("", actualDefaultValue);
 }

 @Test
 public void testGetDefaultValueWithArrayType() {
 JavaType type = JsonTypeFactory.constructCollectionType(ArrayList.class, Integer.class);
 Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
 assertNotNull(actualDefaultValue);
 assertTrue(actualDefaultValue instanceof Object[]);
 assertEquals(0, ((Object[]) actualDefaultValue).length);
 }

 @Test
 public void testGetDefaultValueWithReferenceType() {
 JavaType type = JsonTypeFactory.valueType(String.class);
 Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
 assertEquals("", actualDefaultValue);

 Object actualDefaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(actualDefaultBean);
 assertTrue(actualDefaultBean instanceof String);
 assertEquals("", (String) actualDefaultBean);
 }

 @Test
 public void testGetDefaultValueWithNonEmptyType() {
 JavaType type = JsonTypeFactory.valueType(JsonInclude.Include.NON_EMPTY.toString());
 Object actualDefaultValue = propertyBuilder.getDefaultValue(type);
 assertTrue(actualDefaultValue instanceof JsonInclude.Include);
 assertEquals(JsonInclude.Include.NON_EMPTY, actualDefaultValue);
 }
}