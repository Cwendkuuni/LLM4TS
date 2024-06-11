package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(Object.class));
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 // Test with default values
 JsonSerializer<Object> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mock(JavaType.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 BeanDescription beanDesc = mock(BeanDescription.class);
 SerializationConfig config = mock(SerializationConfig.class);

 when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
 when(beanDesc.getType()).thenReturn(Object.class);

 propertyBuilder._config = config;
 propertyBuilder._beanDesc = beanDesc;
 propertyBuilder._defaultInclusion = JsonInclude.Value.builder().build();
 propertyBuilder._annotationIntrospector = mock(AnnotationIntrospector.class);

 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, serializer, typeSerializer, null, am, false);
 assertNotNull(beanPropertyWriter);

 // Test with custom inclusion value
 JsonInclude.Include inclusion = JsonInclude.Include.NON_EMPTY;
 JsonInclude.Value inclusionValue = JsonInclude.Value.builder().withInclude(inclusion).build();
 propertyBuilder._defaultInclusion = inclusionValue;

 BeanPropertyWriter beanPropertyWriterWithCustomInclusion = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, serializer, typeSerializer, null, am, false);
 assertNotNull(beanPropertyWriterWithCustomInclusion);
 assertNotEquals(beanPropertyWriter, beanPropertyWriterWithCustomInclusion);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 JsonSerialize annotation = mock(JsonSerialize.class);
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType javaType = mock(JavaType.class);
 Class<?> clazz = mock(Class.class);

 when(annotation.type()).thenReturn(JsonSerialize.Typing.STATIC);
 when(annotatedMember.getAnnotations()).thenReturn(new Annotations());
 when(annotatedMember.getGenericType()).thenReturn(javaType);
 when(javaType.getRawClass()).thenReturn(clazz);

 PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(Object.class));

 JavaType result = propertyBuilder.findSerializationType(annotatedMember, false, javaType);
 assertEquals(javaType, result);
 }

 @Test
 public void testFindSerializationTypeWithAnnotationOverride() throws JsonMappingException {
 JsonSerialize annotation = mock(JsonSerialize.class);
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType javaType = mock(JavaType.class);
 Class<?> clazz = mock(Class.class);
 Class<?> clazzOverridden = mock(Class.class);

 when(annotation.type()).thenReturn(JsonSerialize.Typing.STATIC);
 when(annotation.value()).thenReturn(clazzOverridden);
 when(annotatedMember.getAnnotations()).thenReturn(new Annotations());
 when(annotatedMember.getGenericType()).thenReturn(javaType);
 when(javaType.getRawClass()).thenReturn(clazz);
 when(clazz.isAssignableFrom(clazzOverridden)).thenReturn(true);

 PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(Object.class));

 JavaType result = propertyBuilder.findSerializationType(annotatedMember, false, javaType);
 assertEquals(clazzOverridden, result);
 }

 @Test
 public void testFindSerializationTypeWithNoAnnotation() throws JsonMappingException {
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType javaType = mock(JavaType.class);

 when(annotatedMember.getAnnotations()).thenReturn(new Annotations());
 when(annotatedMember.getGenericType()).thenReturn(javaType);
 when(javaType.getRawClass()).thenReturn(Object.class);

 PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(Object.class));

 JavaType result = propertyBuilder.findSerializationType(annotatedMember, false, javaType);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithInvalidAnnotation() throws JsonMappingException {
 JsonSerialize annotation = mock(JsonSerialize.class);
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 JavaType javaType = mock(JavaType.class);
 Class<?> clazz = mock(Class.class);

 when(annotation.type()).thenReturn(JsonSerialize.Typing.STATIC);
 when(annotation.value()).thenReturn(clazz);
 when(annotatedMember.getAnnotations()).thenReturn(new Annotations());
 when(annotatedMember.getGenericType()).thenReturn(javaType);
 when(javaType.getRawClass()).thenReturn(clazz);
 when(clazz.isAssignableFrom(javaType.getRawClass())).thenReturn(false);

 PropertyBuilder propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(Object.class));

 propertyBuilder.findSerializationType(annotatedMember, false, javaType);
 }
}