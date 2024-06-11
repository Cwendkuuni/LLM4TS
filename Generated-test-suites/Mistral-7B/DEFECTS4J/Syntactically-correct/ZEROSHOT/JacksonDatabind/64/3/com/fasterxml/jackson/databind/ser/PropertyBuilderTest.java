package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        serializationConfig = objectMapper.getSerializationConfig();
        AnnotatedClass ac = new AnnotatedClass(MyClass.class, null, null);
        beanDescription = objectMapper.introspect(ac);
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testBuildWriter() throws Exception {
        BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("property1");
        JavaType declaredType = propDef.getType();
        JsonSerializer<?> ser = new ObjectSerializer(null, null, null, null, null);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember(propDef.getName(), propDef.getBeanProperty(), null, null);
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(beanPropertyWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithException() throws Exception {
        BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("property1");
        JavaType declaredType = propDef.getType();
        JsonSerializer<?> ser = new ObjectSerializer(null, null, null, null, null);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = new AnnotatedMember("nonExistentProperty", null, null, null);
        boolean defaultUseStaticTyping = false;

        propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        JavaType jsonType = propertyBuilder.findSerializationType(null, false, declaredTypeOf("MyClass"));
        assertNotNull(jsonType);

        JsonSerializer<?> ser = new ObjectSerializer(null, null, null, null, null);
        TypeSerializer typeSer = null;
        AnnotatedMember am = new AnnotatedMember("property1", null, null, null);

        JavaType type = propertyBuilder.findSerializationType(am, false, declTypeOf("MyClass"));
        assertEquals(type, jsonType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithException() throws Exception {
        AnnotatedMember am = new AnnotatedMember("nonExistentProperty", null, null, null);

        propertyBuilder.findSerializationType(am, false, declTypeOf("MyClass"));
    }

    @Test
    public void testGetClassAnnotations() {
        Annotation[] classAnnotations = propertyBuilder.getClassAnnotations();

        assertNotNull(classAnnotations);
        assertTrue(classAnnotations.length > 0);
    }

    @Test
    public void testGetDefaultBean() {
        assertNull(propertyBuilder.getDefaultBean());

        propertyBuilder.getDefaultBean();

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        assertEquals(0, propertyBuilder.getDefaultValue(intType).intValue());
        assertEquals("", propertyBuilder.getDefaultValue(stringType).toString());

        assertNull(propertyBuilder.getDefaultValue(declTypeOf(MyClass.class)));

        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(listType));
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        BeanPropertyDefinition propDef = beanDescription.findPropertyDefinition("property1");
        AnnotatedMember am = new AnnotatedMember("property1", null, null, null);
        JavaType javaType = propDef.getType();

        assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue("property1", am, javaType));

        if (defaultBean != null) {
            assertEquals(defaultBean.getClass().getDeclaredField("property1").get(defaultBean), propertyBuilder.getPropertyDefaultValue("property1", am, javaType));
        } else {
            assertEquals(propertyBuilder.getDefaultValue(javaType), propertyBuilder.getPropertyDefaultValue("property1", am, javaType));
        }
    }

    private JavaType declTypeOf(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}