package com.yourcompany.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class FieldPropertyTest {

    private ObjectNode node;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();

        node = mapper.createObjectNode();
    }

    @Test
    public void testConstructor() {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertEquals(propDef, fieldProperty.getBeanPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());
        assertEquals(field, fieldProperty._annotated);
        assertEquals(field.getAnnotated(), fieldProperty._field);
        assertFalse(fieldProperty._skipNulls);
    }

    @Test
    public void testWithName() {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        PropertyName newName = PropertyName.construct("newName");
        SettableBeanProperty newProperty = fieldProperty.withName(newName);

        assertEquals(propDef, newProperty.getBeanPropertyDefinition());
        assertEquals(type, newProperty.getType());
        assertNull(newProperty.getValueDeserializer());
        assertNull(newProperty.getNullProvider());
        assertEquals(field, newProperty._annotated);
        assertEquals(field.getAnnotated(), newProperty._field);
        assertFalse(newProperty._skipNulls);
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        JsonDeserializer<?> deserializer = mapper.findValueDeserializer(String.class, null);

        SettableBeanProperty newProperty = fieldProperty.withValueDeserializer(deserializer);

        assertEquals(propDef, newProperty.getBeanPropertyDefinition());
        assertEquals(type, newProperty.getType());
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertNull(newProperty.getNullProvider());
        assertEquals(field, newProperty._annotated);
        assertEquals(field.getAnnotated(), newProperty._field);
        assertFalse(newProperty._skipNulls);
    }

    @Test
    public void testWithNullProvider() {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        NullValueProvider nva = mapper.getFactory().getValueInjector().findValueInjector(Object.class);

        SettableBeanProperty newProperty = fieldProperty.withNullProvider(nva);

        assertEquals(propDef, newProperty.getBeanPropertyDefinition());
        assertEquals(type, newProperty.getType());
        assertNull(newProperty.getValueDeserializer());
        assertEquals(nva, newProperty.getNullProvider());
        assertEquals(field, newProperty._annotated);
        assertEquals(field.getAnnotated(), newProperty._field);
        assertFalse(newProperty._skipNulls);
    }

    @Test
    public void testFixAccess() throws IllegalAccessException {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, null, null, field);

        fieldProperty.fixAccess(mapper.getConfiguration());
        assertTrue(field.isAccessible());
    }

    @Test
    public void testGetAnnotation() {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, null, null, field);
        assertEquals(jsonProperty, fieldProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, null, null, field);

        assertEquals(field, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        Converter.StdConverter<String, Integer> converter = mapper.getFactory().findValueConverter(String.class, Integer.class);
        TypeDeserializer integerTypeDeser = converter.getNullValueDeserializer(mapper.getFactory());

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        String value = "123";
        node.put("fieldName", value);

        Object instance = mapper.readValue(node.toString(), type);

        fieldProperty.deserializeAndSet(mapper.getFactory().createParser(node), mapper.getConfiguration(), instance);

        assertEquals(Integer.valueOf(123), field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        Converter.StdConverter<String, Integer> converter = mapper.getFactory().findValueConverter(String.class, Integer.class);
        TypeDeserializer integerTypeDeser = converter.getNullValueDeserializer(mapper.getFactory());

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        String value = "123";
        node.put("fieldName", value);

        Object instance = mapper.readValue(node.toString(), type);

        Object result = fieldProperty.deserializeSetAndReturn(mapper.getFactory().createParser(node), mapper.getConfiguration(), instance);

        assertEquals(instance, result);
        assertEquals(Integer.valueOf(123), field.get(instance));
    }

    @Test
    public void testSet() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        Converter.StdConverter<String, Integer> converter = mapper.getFactory().findValueConverter(String.class, Integer.class);
        TypeDeserializer integerTypeDeser = converter.getNullValueDeserializer(mapper.getFactory());

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        String value = "123";
        Object instance = mapper.readValue(mapper.createObjectText("{\"fieldName\":\"\"}"), type);

        fieldProperty.set(instance, value);

        assertEquals(Integer.valueOf(123), field.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        JavaType type = mapper.getTypeFactory().constructType(YourClass.class);
        BeanPropertyDefinition propDef = mapper.findPropertyDefinition("fieldName", type);
        AnnotatedField field = mapper.findAnnotatedMember("fieldName", YourClass.class);
        TypeDeserializer typeDeser = mapper.getFactory().findValueDeserializer(String.class, null);
        Annotations contextAnnotations = mapper.getSerializationConfig().getDefaultAnnotationIntrospector().findAnnotation(field.getAnnotated());
        Converter.StdConverter<String, Integer> converter = mapper.getFactory().findValueConverter(String.class, Integer.class);
        TypeDeserializer integerTypeDeser = converter.getNullValueDeserializer(mapper.getFactory());

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        String value = "123";
        Object instance = mapper.readValue(mapper.createObjectText("{\"fieldName\":\"\"}"), type);

        Object result = fieldProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        assertEquals(Integer.valueOf(123), field.get(instance));
    }
}

class YourClass {
    @JsonProperty("fieldName")
    private int field;
}