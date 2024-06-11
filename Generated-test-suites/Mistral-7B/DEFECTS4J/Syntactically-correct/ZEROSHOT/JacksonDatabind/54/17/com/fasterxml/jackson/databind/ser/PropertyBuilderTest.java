package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.AnnotatedMember;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDescription;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setUp() {
        config = Mockito.mock(SerializationConfig.class);
        beanDescription = Mockito.mock(BeanDescription.class);
        annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);

        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuildWriterNull() {
        Assert.assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriterNotNull() throws JsonMappingException {
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        JavaType serializationType = Mockito.mock(JavaType.class);
        when(beanDescription.getType()).thenReturn(serializationType);

        when(propertyBuilder.findSerializationType(am, false, declaredType)).thenReturn(serializationType);

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() throws JsonMappingException {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        JsonSerialize serializationAnnotation = Mockito.mock(JsonSerialize.class);
        JsonSerialize.Typing serializationTypingAnnotation = Mockito.mock(JsonSerialize.Typing.class);

        when(a.getAnnotation(JsonSerialize.class)).thenReturn(serializationAnnotation);
        when(serializationAnnotation.getType()).thenReturn(serializationTypingAnnotation);
        when(serializationTypingAnnotation.getValue()).thenReturn(JsonSerialize.Typing.STATIC);

        JavaType secondary = Mockito.mock(JavaType.class);
        when(annotationIntrospector.refineSerializationType(config, a, declaredType)).thenReturn(secondary);

        boolean useStaticTyping = true;
        JavaType type = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        Assert.assertEquals(secondary, type);

        Assert.assertTrue(useStaticTyping);
    }

    @Test
    public void testFindSerializationTypeWithoutAnnotation() throws JsonMappingException {
        Annotated a = Mockito.mock(Annotated.class);
        JavaType declaredType = Mockito.mock(JavaType.class);

        JavaType secondary = Mockito.mock(JavaType.class);
        when(beanDescription.getType()).thenReturn(secondary);

        boolean useStaticTyping = false;
        JavaType type = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        Assert.assertNull(type);

        Assert.assertFalse(useStaticTyping);
    }

    @Test
    public void testGetClassAnnotations() {
        Class<?> clazz = Mockito.mock(Class.class);
        when(beanDescription.getClassAnnotations()).thenReturn(clazz);

        Class<?>[] annotations = propertyBuilder.getClassAnnotations();
        Assert.assertArrayEquals(new Class<?>[]{clazz}, annotations);
    }

    @Test
    public void testGetPropertyDefaultValueString() throws JsonMappingException {
        Object defaultBean = Mockito.mock(Object.class);
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        when(defaultBean.getClass()).thenReturn(Object.class);

        Method getter = Mockito.mock(Method.class);
        when(member.getValue(defaultBean)).thenReturn("default");
        when(member.getAnnotatedMember()).thenReturn(getter);

        String name = "name";
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("default", defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueObject() throws JsonMappingException {
        Object defaultBean = Mockito.mock(Object.class);
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        when(defaultBean.getClass()).thenReturn(Object.class);

        Method getter = Mockito.mock(Method.class);
        when(member.getValue(defaultBean)).thenReturn(new Object[]{});
        when(member.getAnnotatedMember()).thenReturn(getter);

        String name = "name";
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertNull(defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueArray() throws JsonMappingException {
        Object defaultBean = Mockito.mock(Object.class);
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Arrays.asJava(int[].class);

        when(defaultBean.getClass()).thenReturn(Object.class);

        Method getter = Mockito.mock(Method.class);
        when(member.getValue(defaultBean)).thenReturn(new int[]{});
        when(member.getAnnotatedMember()).thenReturn(getter);

        String name = "name";
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertArrayEquals(new int[]{0}, defaultValue);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    @Test
    public void testGetDefaultBeanWithInstance() {
        Object defaultBean = new Object();
        propertyBuilder.setDefaultBean(defaultBean);

        Object defaultValue = propertyBuilder.getDefaultBean();
        Assert.assertEquals(defaultBean, defaultValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetPropertyDefaultValueThrowsException() throws JsonMappingException {
        Object defaultBean = Mockito.mock(Object.class);
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);

        when(defaultBean.getClass()).thenReturn(Object.class);

        Method getter = Mockito.mock(Method.class);
        when(member.getValue(defaultBean)).thenThrow(new IllegalAccessException());
        when(member.getAnnotatedMember()).thenReturn(getter);

        String name = "name";
        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }
}