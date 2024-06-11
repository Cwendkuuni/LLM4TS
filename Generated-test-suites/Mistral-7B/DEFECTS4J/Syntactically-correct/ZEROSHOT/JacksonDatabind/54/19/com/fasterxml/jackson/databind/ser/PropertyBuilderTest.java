package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private SerializationConfig serializationConfig;
 private BeanDescription beanDescription;
 private SerializerProvider serializerProvider;
 private Object defaultBean;

 @Before
 public void setup() {
 serializationConfig = mock(SerializationConfig.class);
 beanDescription = mock(BeanDescription.class);
 serializerProvider = mock(SerializerProvider.class);
 defaultBean = new Object() {};
 propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 }

 @Test
 public void testGetClassAnnotations() {
 Assert.assertNotNull(propertyBuilder.getClassAnnotations());
 }

 @Test
 public void testBuildWriter() throws Exception {
 AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
 when(beanDescription.getClassAnnotations()).thenReturn(annotationIntrospector);
 when(annotationIntrospector.findNullSerializer(any())).thenReturn(mock(JsonSerializer.class));
 when(annotationIntrospector.findUnwrappingNameTransformer(any())).thenReturn(null);

 JsonInclude.Value defaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_NULL);
 when(beanDescription.findPropertyInclusion(any())).thenReturn(new JsonInclude.PropertyAccessor(new JsonInclude.Value(JsonInclude.Include.ALWAYS)));
 when(serializationConfig.getDefaultPropertyInclusion(any())).thenReturn(defaultInclusion);
 when(beanDescription.getType()).thenReturn(new SimpleType(String.class));

 BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
 when(beanPropertyDefinition.getName()).thenReturn("testProperty");
 when(beanPropertyDefinition.findInclusion()).thenReturn(new JsonInclude.PropertyAccessor(defaultInclusion));

 JavaType declaredType = new SimpleType(String.class);
 JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);

 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider,
 beanPropertyDefinition, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer,
 null, false);

 Assert.assertNotNull(beanPropertyWriter);
 }

 @Test
 public void testFindSerializationType() throws Exception {
 Annotated a = mock(Annotated.class);
 when(beanDescription.getClassAnnotations()).thenReturn(Collections.emptyList());
 when(beanDescription.getType()).thenReturn(new SimpleType(String[].class));

 JavaType declaredType = new SimpleType(Integer.class);
 JavaType serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
 assertNull(serializationType);

 when(beanDescription.getClassAnnotations()).thenReturn(Collections.singletonList(mock(JsonInclude.class)));

 serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
 assertNull(serializationType);

 JsonInclude jsonIncludeAnnotation = mock(JsonInclude.class);
 when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.NON_NULL);
 when(beanDescription.getClassAnnotations()).thenReturn(Collections.singletonList(jsonIncludeAnnotation));

 serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
 assertNull(serializationType);

 when(jsonIncludeAnnotation.value()).thenReturn(JsonInclude.Include.ALWAYS);

 serializationType = propertyBuilder.findSerializationType(a, true, declaredType);
 assertEquals(declaredType, serializationType);
 }

 @Test
 public void testGetPropertyDefaultValue() throws Exception {
 String name = "testProperty";
 AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
 when(annotatedMember.getValue(defaultBean)).thenReturn(new Integer(42));

 JavaType type = new SimpleType(Integer.class);
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, annotatedMember, type);
 assertEquals(Integer.valueOf(42), propertyDefaultValue);
 }

 @Test
 public void testGetDefaultValue() {
 JavaType type = new SimpleType(String.class);
 Object defaultValue = propertyBuilder.getDefaultValue(type);
 assertNull(defaultValue);
 }

 @Test
 public void testGetDefaultBean() throws Exception {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean);
 }
}