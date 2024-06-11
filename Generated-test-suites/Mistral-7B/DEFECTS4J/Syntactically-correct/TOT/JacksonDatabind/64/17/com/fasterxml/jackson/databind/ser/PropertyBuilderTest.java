package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.bean.BeanPropertyWriter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.Markers;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyBuilderTest {

    @Test
    public void testBuilderWithNoDefaults() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, null, null, null, null, null, null, false);
        assertNull(propertyWriter.getValueToSuppress());
        assertFalse(propertyWriter.suppressNulls());
    }

    @Test
    public void testBuilderWithDefaults() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JavaType intType = TypeFactory.defaultInstance().constructSimpleType(int.class);
        JavaType intArrayType = TypeFactory.defaultInstance().constructArrayType(int.class);

        Object defaultBean = new Object() {
            int intField = 10;
            int[] intArrayField = {1, 2, 3};
        };

        builder._defaultBean = defaultBean;
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, null, intType, null, null, null, null, false);
        assertEquals(propertyWriter.getValueToSuppress(), 0);
        assertFalse(propertyWriter.suppressNulls());

        propertyWriter = builder.buildWriter(null, null, intArrayType, null, null, null, null, false);
        assertSame(propertyWriter.getValueToSuppress(), Markers.DEFAULT_EMPTY_ARRAY_MARKER);
        assertFalse(propertyWriter.suppressNulls());
    }

    @Test
    public void testBuilderWithJsonIncludeAnnotation() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JsonInclude includeAnnotation = new JsonInclude(){{
            value = JsonInclude.Value.ALWAYS;
        }};

        BeanPropertyDefinition propertyDef = new BeanPropertyDefinition(null, "name", int.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        JavaType intType = TypeFactory.defaultInstance().constructType(int.class, JsonTypeInfo.Id.CLASS);

        Object defaultBean = new Object() {
            int intField = 10;
        };

        builder._defaultBean = defaultBean;
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        builder._beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        builder._beanDesc.setAnnotationValues(null, new AnnotatedMember(null, "name", intType, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        builder._beanDesc.setAnnotation(new JsonInclude(includeAnnotation));

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, propertyDef, intType, null, null, null, null, false);
        assertFalse(propertyWriter.suppressNulls());
        assertNull(propertyWriter.getValueToSuppress());
    }

    @Test
    public void testBuilderWithJsonSerializeAnnotation() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JsonSerialize serializationAnnotation = new JsonSerialize(){{
            useGetter = true;
            typing = Typing.DYNAMIC;
        }};

        BeanPropertyDefinition propertyDef = new BeanPropertyDefinition(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Object defaultBean = new Object() {
            String name = "defaultValue";
        };

        builder._defaultBean = defaultBean;
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        builder._beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        builder._beanDesc.setAnnotationValues(null, new AnnotatedMember(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        builder._beanDesc.setAnnotation(new JsonSerialize(serializationAnnotation));

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, propertyDef, String.class, null, null, null, null, false);
        assertFalse(propertyWriter.suppressNulls());
        assertNull(propertyWriter.getValueToSuppress());
    }

    @Test
    public void testBuilderWithJsonIncludeAndJsonSerializeAnnotations() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JsonInclude includeAnnotation = new JsonInclude(){{
            value = JsonInclude.Value.ALWAYS;
        }};

        JsonSerialize serializationAnnotation = new JsonSerialize(){{
            useGetter = true;
            typing = Typing.DYNAMIC;
        }};

        BeanPropertyDefinition propertyDef = new BeanPropertyDefinition(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Object defaultBean = new Object() {
            String name = "defaultValue";
        };

        builder._defaultBean = defaultBean;
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        builder._beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        builder._beanDesc.setAnnotationValues(null, new AnnotatedMember(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        builder._beanDesc.setAnnotation(new JsonInclude(includeAnnotation));
        builder._beanDesc.setAnnotation(new JsonSerialize(serializationAnnotation));

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, propertyDef, String.class, null, null, null, null, false);
        assertFalse(propertyWriter.suppressNulls());
        assertNull(propertyWriter.getValueToSuppress());
    }

    @Test
    public void testBuilderWithTypeSerializer() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JavaType intType = TypeFactory.defaultInstance().constructSimpleType(int.class);
        JavaType intSerializerType = TypeFactory.defaultInstance().constructType(TypeSerializer.class, intType);

        Object defaultBean = new Object() {{
            int intField = 10;
        }};

        builder._defaultBean = defaultBean;
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        builder._beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, null, intType, null, intSerializerType, null, null, false);
        assertFalse(propertyWriter.suppressNulls());
        assertNull(propertyWriter.getValueToSuppress());
    }

    @Test
    public void testBuilderWithNullSerializer() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JsonSerialize nullSerializerAnnotation = new JsonSerialize(){{
            nulls = JsonSerialize.NullSerializer.EMPTY_STRING;
        }};

        BeanPropertyDefinition propertyDef = new BeanPropertyDefinition(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Object defaultBean = new Object() {
            String name = null;
        };

        builder._defaultBean = defaultBean;
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        builder._beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        builder._beanDesc.setAnnotationValues(null, new AnnotatedMember(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        builder._beanDesc.setAnnotation(new JsonSerialize(nullSerializerAnnotation));

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, propertyDef, String.class, null, null, null, null, false);
        assertEquals("", propertyWriter.getValueToSuppress());
        assertFalse(propertyWriter.suppressNulls());
    }

    @Test
    public void testBuilderWithUnwrappingNameTransformer() throws Exception {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        NameTransformer nameTransformer = new NameTransformer() {{
            propertyName = "transformedName";
        }};

        BeanPropertyDefinition propertyDef = new BeanPropertyDefinition(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Object defaultBean = new Object() {
            String name = "defaultValue";
        };

        builder._defaultBean = defaultBean;
        builder._defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;

        builder._beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        builder._beanDesc.setAnnotationValues(null, new AnnotatedMember(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        builder._beanDesc.setAnnotation(new JsonInclude(null));
        builder._beanDesc.setAnnotation(new JsonSerialize(null));
        builder._beanDesc.setAnnotationValues(null, new AnnotatedMember(null, "name", String.class, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        builder._beanDesc.addNameTransformer(new NameTransformer[] {nameTransformer});

        BeanPropertyWriter propertyWriter = builder.buildWriter(null, propertyDef, String.class, null, null, null, null, false);
        assertEquals("transformedName", propertyWriter.getName());
        assertFalse(propertyWriter.suppressNulls());
        assertNull(propertyWriter.getValueToSuppress());
    }
}