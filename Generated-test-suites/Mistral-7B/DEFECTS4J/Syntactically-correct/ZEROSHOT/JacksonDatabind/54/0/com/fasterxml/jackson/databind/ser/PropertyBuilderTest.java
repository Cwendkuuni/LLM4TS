package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setup() {
        ObjectNode node = ObjectMapperFactory.getJsonMapper().createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);
        node.put("cars", new int[]{1, 2, 3});
        beanDesc = Introspector.findBeanDescription(node.getClass(), ObjectMapperFactory.getObjectMapper());
        config = new SerializationConfig(new AnnotationIntrospector(), new StandardSerializerFactory(true));
        config.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Test
    public void testPropertyBuilder() throws JsonMappingException, JsonProcessingException {
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, pb.getDefaultInclusion().getValueInclusion());
    }

    @Test
    public void testBuildWriterWithDefaults() throws JsonMappingException, JsonProcessingException {
        SerializerProvider prov = new StandardSerializerProvider();
        BeanPropertyWriter bpw = pb.buildWriter(prov, null, null, null, null, null, null, false);
        Assert.assertNull(bpw.getValueToSuppress());
        Assert.assertFalse(bpw.suppressNulls());
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, bpw.getInclusion());
    }

    @Test
    public void testBuildWriterWithNonDefault() throws JsonMappingException, JsonProcessingException {
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("name");
        JavaType declaredType = ObjectMapperFactory.getObjectMapper().getTypeFactory().constructType(String.class);
        BeanPropertyWriter bpw = pb.buildWriter(prov, propDef, declaredType, null, null, null, null, false);
        Object defaultBean = pb.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertEquals("John Doe", bpw.getValueToSuppress());
        Assert.assertFalse(bpw.suppressNulls());
        Assert.assertEquals(JsonInclude.Include.NON_DEFAULT, bpw.getInclusion());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = ObjectMapperFactory.getObjectMapper().getTypeFactory().constructType(String[].class);
        JavaType serializationType = pb.findSerializationType(am, false, declaredType);
        Assert.assertNull(serializationType);
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() throws JsonMappingException {
        Annotated am = Mockito.mock(Annotated.class);
        JsonSerialize jsonSerialize = Mockito.mock(JsonSerialize.class);
        when(am.getValue()).thenReturn(jsonSerialize);
        when(jsonSerialize.type()).thenReturn(JsonSerialize.Typing.STATIC);
        JavaType declaredType = ObjectMapperFactory.getObjectMapper().getTypeFactory().constructType(String.class);
        JavaType serializationType = pb.findSerializationType(am, true, declaredType);
        Assert.assertEquals(declaredType, serializationType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithInvalidType() throws JsonMappingException {
        Annotated am = Mockito.mock(Annotated.class);
        JavaType declaredType = ObjectMapperFactory.getObjectMapper().getTypeFactory().constructType(String.class);
        pb.findSerializationType(am, true, declaredType);
        // Should throw IllegalArgumentException here
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException, JsonProcessingException {
        String propName = "name";
        Object defaultBean = pb.getDefaultBean();
        Object propertyDefaultValue = pb.getPropertyDefaultValue(propName, null, ObjectMapperFactory.getObjectMapper().getTypeFactory().constructType(String.class));
        Assert.assertEquals("John Doe", propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithNullBean() throws JsonMappingException, JsonProcessingException {
        String propName = "name";
        Object defaultBean = null;
        Object propertyDefaultValue = pb.getPropertyDefaultValue(propName, null, ObjectMapperFactory.getObjectMapper().getTypeFactory().constructType(String.class));
        Assert.assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() throws JsonMappingException {
        JavaType type = ObjectMapperFactory.getObjectMapper().getTypeFactory().constructType(String[].class);
        Object defaultValue = pb.getDefaultValue(type);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultBean() throws JsonMappingException, JsonProcessingException {
        Object defaultBean = pb.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertEquals(beanDesc.getBeanClass(), defaultBean.getClass());
    }
}