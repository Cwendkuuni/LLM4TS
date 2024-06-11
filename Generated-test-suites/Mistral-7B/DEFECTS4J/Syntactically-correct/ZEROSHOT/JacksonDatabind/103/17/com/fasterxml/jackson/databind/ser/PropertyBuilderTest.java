package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyBuilderTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testBuildWriter_SimpleType() throws JsonMappingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mock(SerializationConfig.class);
 when(config.getDefaultPropertyInclusion(eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 when(config.getDefaultPropertyInclusion(eq(String.class), eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 BeanDescription beanDesc = new BeanDescription(String.class);
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 SerializerProvider prov = mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 Class<?> rawPropertyType = String.class;
 when(propDef.getAccessor()).thenReturn(am);
 when(am.getRawType()).thenReturn(rawPropertyType);

 // When
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);

 // Then
 verify(prov, times(1)).getSerializerProvider();
 verify(propDef, times(1)).getAccessor();
 verify(am, times(1)).getValue(any());
 assertNull(bpw.getValueToSuppress());
 assertNull(bpw.suppressNulls());
 assertEquals(declaredType, bpw.getDeclaredType());
 assertEquals(serializer, bpw.getSerializer());
 assertEquals(typeSerializer, bpw.getTypeSerializer());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testBuildWriter_ContainerType() throws JsonMappingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mock(SerializationConfig.class);
 when(config.getDefaultPropertyInclusion(eq(List.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 when(config.getDefaultPropertyInclusion(eq(String.class), eq(List.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 BeanDescription beanDesc = new BeanDescription(List.class);
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 SerializerProvider prov = mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(List.class);
 JavaType contentType = mapper.getTypeFactory().constructType(String.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 Class<?> rawPropertyType = List.class;
 when(propDef.getAccessor()).thenReturn(am);
 when(am.getRawType()).thenReturn(rawPropertyType);

 // When
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, contentTypeSerializer, am, false);

 // Then
 verify(prov, times(1)).getSerializerProvider();
 verify(propDef, times(1)).getAccessor();
 assertEquals(declaredType, bpw.getDeclaredType());
 assertEquals(contentTypeSerializer, bpw.getTypeSerializer());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testBuildWriter_CustomNullSerializer() throws JsonMappingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mock(SerializationConfig.class);
 when(config.getDefaultPropertyInclusion(eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 when(config.getDefaultPropertyInclusion(eq(String.class), eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 BeanDescription beanDesc = new BeanDescription(String.class);
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 SerializerProvider prov = mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 Class<?> rawPropertyType = String.class;
 when(propDef.getAccessor()).thenReturn(am);
 when(am.getRawType()).thenReturn(rawPropertyType);
 Object serDef = mock(JsonSerializer.class);
 when(annotationIntrospector.findNullSerializer(am)).thenReturn(serDef);

 // When
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);

 // Then
 verify(prov, times(1)).getSerializerProvider();
 verify(propDef, times(1)).getAccessor();
 verify(am, times(1)).getValue(any());
 verify(annotationIntrospector, times(1)).findNullSerializer(am);
 verify(prov, times(1)).serializerInstance(am, serDef);
 assertNull(bpw.getValueToSuppress());
 assertNull(bpw.suppressNulls());
 assertEquals(declaredType, bpw.getDeclaredType());
 assertEquals(serializer, bpw.getSerializer());
 assertEquals(typeSerializer, bpw.getTypeSerializer());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testBuildWriter_UnwrappingWriter() throws JsonMappingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mock(SerializationConfig.class);
 when(config.getDefaultPropertyInclusion(eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 when(config.getDefaultPropertyInclusion(eq(String.class), eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 BeanDescription beanDesc = new BeanDescription(String.class);
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 SerializerProvider prov = mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 Class<?> rawPropertyType = String.class;
 when(propDef.getAccessor()).thenReturn(am);
 when(am.getRawType()).thenReturn(rawPropertyType);
 NameTransformer unwrapper = mock(NameTransformer.class);
 when(annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(unwrapper);

 // When
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);

 // Then
 verify(prov, times(1)).getSerializerProvider();
 verify(propDef, times(1)).getAccessor();
 verify(am, times(1)).getValue(any());
 verify(annotationIntrospector, times(1)).findUnwrappingNameTransformer(am);
 verify(bw, times(1)).unwrappingWriter(unwrapper);
 assertNull(bpw.getValueToSuppress());
 assertNull(bpw.suppressNulls());
 assertEquals(declaredType, bpw.getDeclaredType());
 assertEquals(serializer, bpw.getSerializer());
 assertEquals(typeSerializer, bpw.getTypeSerializer());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testBuildWriter_NonDefault() throws JsonMappingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mock(SerializationConfig.class);
 when(config.getDefaultPropertyInclusion(eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder()
 .valueInclusion(JsonInclude.Include.NON_DEFAULT)
 .build());
 when(config.getDefaultPropertyInclusion(eq(String.class), eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 BeanDescription beanDesc = new BeanDescription(String.class);
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 SerializerProvider prov = mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 Class<?> rawPropertyType = String.class;
 when(propDef.getAccessor()).thenReturn(am);
 when(am.getRawType()).thenReturn(rawPropertyType);
 Object defaultBean = new DefaultBean();
 when(annotationIntrospector.findNullSerializer(am)).thenReturn(null);

 // When
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);

 // Then
 verify(prov, times(1)).getSerializerProvider();
 verify(propDef, times(1)).getAccessor();
 verify(annotationIntrospector, times(1)).findNullSerializer(am);
 assertNull(bpw.getValueToSuppress());
 assertFalse(bpw.suppressNulls());
 assertEquals(declaredType, bpw.getDeclaredType());
 assertEquals(serializer, bpw.getSerializer());
 assertEquals(typeSerializer, bpw.getTypeSerializer());

 // And
 assertEquals("", bpw.getValueToSuppress());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriter_IllegalType() throws JsonMappingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 SerializationConfig config = mock(SerializationConfig.class);
 when(config.getDefaultPropertyInclusion(eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 when(config.getDefaultPropertyInclusion(eq(String.class), eq(String.class), any()))
 .thenReturn(JsonInclude.Value.builder().build());
 BeanDescription beanDesc = new BeanDescription(Boolean.class);
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 SerializerProvider prov = mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 Class<?> rawPropertyType = String.class;
 when(propDef.getAccessor()).thenReturn(am);
 when(am.getRawType()).thenReturn(rawPropertyType);

 // When
 BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);

 // Then
 // Exception thrown
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testGetClassAnnotations() throws Exception {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription beanDesc = new BeanDescription(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

 // When
 Annotations annotations = propertyBuilder.getClassAnnotations();

 // Then
 assertNotNull(annotations);
 }
}

class DefaultBean {

 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private String name;

 public String getName() {
 return "test";
 }
}