package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanProperty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyDescriptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDescription;
    private PropertyBuilder propertyBuilder;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() {
        config = mock(SerializationConfig.class);
        beanDescription = mock(BeanDescription.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testGetClassAnnotations() {
        when(beanDescription.getClassAnnotations()).thenReturn(Collections.emptyList());
        List<Annotation> result = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testBuildWriter() throws JsonProcessingException {
        ObjectNode node = Mockito.mock(ObjectNode.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> serializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(beanDescription.getType()).thenReturn(declaredType);
        when(propDef.getName()).thenReturn("testProperty");

        when(annotationIntrospector.findSerializationType(am, defaultUseStaticTyping, declaredType)).thenReturn(declaredType);
        when(annotationIntrospector.findNullSerializer(am)).thenReturn(serializer);
        when(annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(null);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, defaultUseStaticTyping);

        Assert.assertNotNull(beanPropertyWriter);
        Assert.assertEquals(propDef, beanPropertyWriter.getPropertyDefinition());
        Assert.assertEquals(am, beanPropertyWriter.getMember());
        Assert.assertEquals(declaredType, beanPropertyWriter.getType());
        Assert.assertEquals(serializer, beanPropertyWriter.getSerializer());
        Assert.assertEquals(typeSerializer, beanPropertyWriter.getTypeSerializer());
        Assert.assertEquals(contentTypeSerializer, beanPropertyWriter.getContentTypeSerializer());
        Assert.assertFalse(beanPropertyWriter.isSuppressNulls());
        Assert.assertNull(beanPropertyWriter.getValueToSuppress());
    }

    @Test
    public void testFindSerializationType() throws JsonProcessingException {
        AnnotatedElement annotatedElement = mock(AnnotatedElement.class);
        JavaType declaredType = mock(JavaType.class);
        when(annotatedElement.getType()).thenReturn(declaredType);

        PropertyBuilder propertyBuilderSUT = new PropertyBuilder(config, beanDescription);
        JavaType result = propertyBuilderSUT.findSerializationType(annotatedElement, true, declaredType);
        Assert.assertNull(result);

        JsonSerialize jsonSerializeAnnotation = mock(JsonSerialize.class);
        when(jsonSerializeAnnotation.type()).thenReturn(JsonSerialize.Type.STATIC);
        when(annotatedElement.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);

        result = propertyBuilderSUT.findSerializationType(annotatedElement, true, declaredType);
        Assert.assertEquals(declaredType, result);

        when(jsonSerializeAnnotation.type()).thenReturn(JsonSerialize.Type.WRAPPER_MAP);
        result = propertyBuilderSUT.findSerializationType(annotatedElement, true, declaredType);
        Assert.assertEquals(declaredType.withContentTypeHandler(TypeSerializer.instance), result);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        Object defaultBean = new Object() {
            public String testProperty = "defaultValue";
        };
        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getValue(defaultBean)).thenReturn("testValue");

        when(beanDescription.instantiateBean(true)).thenReturn(defaultBean);

        String result = propertyBuilder.getPropertyDefaultValue("testProperty", am, null);
        Assert.assertEquals("testValue", result);

        when(beanDescription.instantiateBean(true)).thenThrow(new Exception());

        try {
            propertyBuilder.getPropertyDefaultValue("testProperty", am, null);
            Assert.fail();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testGetDefaultValue() {
        Class testClass = Integer.TYPE;
        Object result = propertyBuilder.getDefaultValue(Object.class.getType(testClass));
        Assert.assertEquals(0, result);

        result = propertyBuilder.getDefaultValue(Object.class.getType(String.class));
        Assert.assertEquals("", result);

        result = propertyBuilder.getDefaultValue(Object.class.getType(List.class.getType(String.class)));
        Assert.assertEquals(Collections.emptyList(), result);

        result = propertyBuilder.getDefaultValue(Object.class.getType(Map.class.getType(String.class, Integer.TYPE)));
        Assert.assertEquals(Collections.emptyMap(), result);

        result = propertyBuilder.getDefaultValue(Object.class.getType(MyClass.class));
        Assert.assertNull(result);
    }

    private static class MyClass {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int testProperty;
    }
}