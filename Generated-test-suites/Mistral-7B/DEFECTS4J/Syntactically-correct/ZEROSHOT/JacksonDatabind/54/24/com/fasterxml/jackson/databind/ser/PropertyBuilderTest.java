package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StringSerializerWithBOM());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);

        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = BeanDescription.forDeserialization(String.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter() throws Exception {
        SerializerProvider prov = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("value");
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
        JsonSerializer<Object> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;

        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("value");

        BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(actual);
        assertEquals(propDef, actual.getPropertyDefinition());
        assertEquals(declaredType, actual.getType());
        assertNull(actual.getSerializer());
        assertNull(actual.getTypeSerializer());
        assertNull(actual.getContentTypeSerializer());
        assertFalse(actual.suppressNulls());
        assertNull(actual.getValueToSuppress());
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Annotated a = mock(Annotated.class);
        when(a.getName()).thenReturn("value");
        Class<?> clazz = String[].class;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType actual = propertyBuilder.findSerializationType(a, false, clazz);

        assertNull(actual);

        JsonSerialize annotation = mock(JsonSerialize.class);
        when(annotation.type()).thenReturn(JsonSerialize.Typing.STATIC);
        when(a.findAnnotation(JsonSerialize.class)).thenReturn(annotation);

        actual = propertyBuilder.findSerializationType(a, true, clazz);

        assertEquals(clazz, actual);
        assertEquals(JsonSerialize.Typing.STATIC, actual.getAnnotation(JsonSerialize.class).type());
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        BeanDescription beanDesc = BeanDescription.forDeserialization(String.class);

        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        when(introspector.findClassAnnotations(beanDesc.getClassInfo())).thenReturn(new Annotations());

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        Annotations actual = propertyBuilder.getClassAnnotations();

        assertEquals(new Annotations(), actual);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        BeanDescription beanDesc = BeanDescription.forDeserialization(String.class);
        Object defaultBean = "defaultValue";

        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getValue(defaultBean)).thenReturn("testValue");
        when(member.getName()).thenReturn("value");

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        Object actual = propertyBuilder.getPropertyDefaultValue("value", member, new TypeReference<String>() {}.getType());

        assertEquals("testValue", actual);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType type = new TypeReference<IntegerWrapper>() {}.getType();

        Object actual = propertyBuilder.getDefaultValue(type);

        assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultBeanNull() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder.getDefaultBean();
    }

    @Test
    public void testGetDefaultBeanInstance() throws Exception {
        BeanDescription beanDesc = BeanDescription.forDeserialization(String.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        Object actual = propertyBuilder.getDefaultBean();

        assertNotNull(actual);
        assertTrue(actual instanceof String);
    }

    @Test
    public void testThrowWrapped() throws Exception {
        Exception e = new Exception();

        String propName = "value";
        Object defaultBean = "defaultBean";

        Object actual = propertyBuilder._throwWrapped(e, propName, defaultBean);

        assertEquals("Failed to get property 'value' of default defaultBean instance", actual.toString());
    }
}