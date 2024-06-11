package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link PropertyBuilder}.
 */
public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private BeanPropertyDefinition propDef;

    @Mock
    private JsonSerializer<?> ser;

    @Mock
    private TypeSerializer typeSer;

    @Mock
    private TypeSerializer contentTypeSer;

    @Mock
    private AnnotatedMember am;

    @Mock
    private JavaType javaType;

    @Mock
    private JavaType javaType2;

    @InjectMocks
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(any(Class.class))).thenReturn(JsonInclude.Value.STANDARD_INCLUDE);
        when(annotationIntrospector.findNullSerializer(any())).thenReturn(ser);
        when(annotationIntrospector.findUnwrappingNameTransformer(any())).thenReturn(null);

        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getDeclaredName()).thenReturn("testProperty");
        when(propDef.getDeclaredAnnotations()).thenReturn(new Annotations());
        when(propDef.getMember()).thenReturn(am);
        when(propDef.getBeanDescription()).thenReturn(beanDesc);
        when(am.getGenericType()).thenReturn(javaType);

        when(javaType.isReferenceType()).thenReturn(false);
        when(javaType.isContainerType()).thenReturn(true);
        when(javaType.getContentType()).thenReturn(javaType2);
        when(javaType.getRawClass()).thenReturn(String.class);

        when(typeSer.createSerializerInstance(any(), any(), any())).thenReturn(ser);

        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, javaType, ser, typeSer, contentTypeSer, am, false);
        verify(beanDesc, times(1)).getClassAnnotations();
        verify(annotationIntrospector, times(1)).findNullSerializer(am);
        verify(annotationIntrospector, times(1)).findUnwrappingNameTransformer(am);
        assertNotNull(bpw);
    }

    @Test
    public void testBuildWriterWithNullSerializer() throws JsonMappingException {
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(any(Class.class))).thenReturn(JsonInclude.Value.STANDARD_INCLUDE);
        when(annotationIntrospector.findNullSerializer(any())).thenReturn(null);
        when(annotationIntrospector.findUnwrappingNameTransformer(any())).thenReturn(null);

        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getDeclaredName()).thenReturn("testProperty");
        when(propDef.getDeclaredAnnotations()).thenReturn(new Annotations());
        when(propDef.getMember()).thenReturn(am);
        when(propDef.getBeanDescription()).thenReturn(beanDesc);
        when(am.getGenericType()).thenReturn(javaType);

        when(javaType.isReferenceType()).thenReturn(false);
        when(javaType.isContainerType()).thenReturn(true);
        when(javaType.getContentType()).thenReturn(javaType2);
        when(javaType.getRawClass()).thenReturn(String.class);

        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, javaType, null, typeSer, contentTypeSer, am, false);
        verify(beanDesc, times(1)).getClassAnnotations();
        verify(annotationIntrospector, times(1)).findNullSerializer(am());
        verify(annotationIntrospector, times(1)).findUnwrappingNameTransformer(am());
        assertNull(bpw);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        JsonInclude.Include include = JsonInclude.Include.NON_DEFAULT;
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(any(Class.class))).thenReturn(JsonInclude.Value.STANDARD_INCLUDE);
        when(annotationIntrospector.refineSerializationType(eq(config), any(), eq(javaType))).thenReturn(javaType2);

        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getDeclaredName()).thenReturn("testProperty");
        when(propDef.getDeclaredAnnotations()).thenReturn(new Annotations());
        when(propDef.getMember()).thenReturn(am);
        when(propDef.getBeanDescription()).thenReturn(beanDesc);
        when(am.getGenericType()).thenReturn(javaType);

        JavaType expectedType = javaType.withContentTypeHandler(typeSer);
        JavaType actualType = propertyBuilder.findSerializationType(am, false, javaType);
        assertEquals(expectedType, actualType);

        when(annotationIntrospector.refineSerializationType(eq(config), any(), eq(javaType))).thenReturn(null);
        actualType = propertyBuilder.findSerializationType(am, false, javaType);
        assertNull(actualType);
    }

    @Test
    public void testFindSerializationTypeWithJsonSerialize() throws JsonMappingException {
        JsonInclude.Include include = JsonInclude.Include.NON_DEFAULT;
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(any(Class.class))).thenReturn(JsonInclude.Value.STANDARD_INCLUDE);
        when(annotationIntrospector.refineSerializationType(eq(config), any(), eq(javaType))).thenReturn(javaType2);

        JavaType expectedType = javaType.withContentTypeHandler(typeSer);
        JavaType expectedTypeSuper = javaType.getSuperClass();

        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getDeclaredName()).thenReturn("testProperty");
        when(propDef.getDeclaredAnnotations()).thenReturn(new Annotations());
        when(propDef.getMember()).thenReturn(am);
        when(propDef.getBeanDescription()).thenReturn(beanDesc);
        when(am.getGenericType()).thenReturn(javaType);
        when(javaType.getRawClass()).thenReturn(JsonSerialize.class);
        when(javaType.getRawClass().getDeclaredField("value")).thenReturn(javaTypeSuper);
        when(javaType.getRawClass().getMethod("getValue")).thenReturn(anyMethod());

        when(annotationIntrospector.findSerializationTypeAnnotation(any())).thenReturn(new JsonSerialize());

        JavaType actualType = propertyBuilder.findSerializationType(am, true, javaType);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithJsonSerializeInvalidType() throws JsonMappingException {
        JsonInclude.Include include = JsonInclude.Include.NON_DEFAULT;
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.findPropertyInclusion(any(Class.class))).thenReturn(JsonInclude.Value.STANDARD_INCLUDE);
        when(annotationIntrospector.refineSerializationType(eq(config), any(), eq(javaType))).thenReturn(javaType2);

        JavaType expectedType = javaType.withContentTypeHandler(typeSer);
        JavaType expectedTypeSuper = javaType.getSuperClass();

        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getDeclaredName()).thenReturn("testProperty");
        when(propDef.getDeclaredAnnotations()).thenReturn(new Annotations());
        when(propDef.getMember()).thenReturn(am);
        when(propDef.getBeanDescription()).thenReturn(beanDesc);
        when(am.getGenericType()).thenReturn(javaType);
        when(javaType.getRawClass()).thenReturn(Mockito.mock(JsonSerialize.class));

        when(annotationIntrospector.findSerializationTypeAnnotation(any())).thenReturn(new JsonSerialize());

        propertyBuilder.findSerializationType(am, false, javaType);
    }

    @Test
    public void testGetDefaultBean() {
        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(new Object());

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(new Object(), defaultBean);

        verify(beanDesc, times(1)).instantiateBean(anyBoolean());
    }

    @Test
    public void testGetDefaultBeanNull() {
        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(null);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        verify(beanDesc, times(1)).instantiateBean(anyBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueNull() throws JsonMappingException {
        when(annotationIntrospector.findNullSerializer(any())).thenReturn(ser);
        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(new Object());

        String propName = "testProperty";
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, am, javaType);

        assertEquals(null, propertyDefaultValue);
        verify(annotationIntrospector, times(1)).findNullSerializer(am());
        verify(beanDesc, times(1)).instantiateBean(anyBoolean());

        when(am.getValue(anyObject())).thenThrow(new RuntimeException());

        try {
            propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, am, javaType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verify(am, times(1)).getValue(anyObject());
            verifyNoMoreInteractions(beanDesc, annotationIntrospector);
        }
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        when(annotationIntrospector.findNullSerializer(any())).thenReturn(ser);
        when(beanDesc.instantiateBean(anyBoolean())).thenReturn(new Object());

        String propName = "testProperty";
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, am, javaType);

        assertEquals(null, propertyDefaultValue);
        verify(annotationIntrospector, times(1)).findNullSerializer(am());
        verify(beanDesc, times(1)).instantiateBean(anyBoolean());

        when(am.getValue(defaultBean)).thenReturn("testValue");

        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propName, am, javaType);
        assertEquals("testValue", propertyDefaultValue);
        verify(am, times(1)).getValue(defaultBean);
    }

    @Test
    public void testGetDefaultValuePrimitive() {
        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        assertEquals(Integer.MIN_VALUE, defaultValue);
        verifyNoInteractions(beanDesc);
    }

    @Test
    public void testGetDefaultValueString() {
        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        assertEquals("", defaultValue);
        verifyNoInteractions(beanDesc);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetDefaultValueArray() throws JsonMappingException {
        when(javaType.isArray()).thenReturn(true);
        when(javaType.getContentType()).thenReturn(javaType2);

        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        verify(javaType).isArray();
        verify(javaType).getContentType();
        verifyThrows(JsonMappingException.class, () -> propertyBuilder.getDefaultValue(javaType.getContentType()));
    }

    @Test
    public void testGetDefaultValueMap() throws JsonMappingException {
        when(javaType.isMapType()).thenReturn(true);
        when(javaType.getContentType()).thenReturn(javaType2);

        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        verify(javaType).isMapType();
        verify(javaType).getContentType();
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
        verifyNoMoreInteractions(beanDesc);
    }

    @Test
    public void testGetDefaultValueWrapperType() throws JsonMappingException {
        when(javaType.getRawClass()).thenReturn(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        assertEquals("", defaultValue);
        verifyNoMoreInteractions(beanDesc);
    }

    @Test
    public void testGetDefaultValueReferenceType() throws JsonMappingException {
        when(javaType.isReferenceType()).thenReturn(true);

        Object defaultValue = propertyBuilder.getDefaultValue(javaType);

        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
        verifyNoMoreInteractions(beanDesc);
    }

    private Method anyMethod() {
        try {
            return BeanUtil.findMethod(JavaType.class, "getValue", new Class[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}