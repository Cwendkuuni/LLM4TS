package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link FieldProperty}.
 */
public class FieldPropertyTest {

    private FieldProperty fieldProperty;
    private AnnotatedField annotatedField;
    private Field field;
    private Object instance;

    @Before
    public void setup() throws Exception {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        annotatedField = mock(AnnotatedField.class);
        field = new Field(getClass().getDeclaredField("fieldPropertyTest"), "testField");
        when(annotatedField.getAnnotated()).thenReturn(field);

        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotatedField);
        instance = new Object();
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = fieldProperty.withName(newName);

        assertSame(fieldProperty, result);
        assertSame(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty result = fieldProperty.withValueDeserializer(deserializer);

        assertSame(fieldProperty, result);
        assertSame(deserializer, result.getValueDeserializer());
        assertSame(nva, result.getNullProvider());

        SettableBeanProperty result2 = fieldProperty.withNullProvider(nva);

        assertSame(fieldProperty, result2);
        assertSame(deserializer, result2.getValueDeserializer());
        assertSame(nva, result2.getNullProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        DeserializationConfig config = mock(DeserializationConfig.class);
        fieldProperty.fixAccess(config);
        ClassUtil.checkAccess(field, getClass());
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(annotatedField.getAnnotation(Annotation.class)).thenReturn(annotation);

        assertSame(annotation, fieldProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertSame(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Object value = "testValue";
        when(parser.getValueAsString()).thenReturn(value);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        fieldProperty.deserializeAndSet(parser, ctxt, instance);

        Object fieldValue = field.get(instance);
        assertSame(value, fieldValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Object value = "testValue";
        when(parser.getValueAsString()).thenReturn(value);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        Object result = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);

        assertSame(instance, result);
        assertSame(value, field.get(instance));
    }

    @Test
    public void testSet() throws IOException {
        Object value = "testValue";

        fieldProperty.set(instance, value);

        assertSame(value, field.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = "testValue";

        Object result = fieldProperty.setAndReturn(instance, value);

        assertSame(instance, result);
        assertSame(value, field.get(instance));
    }

    @Test
    public void testReadResolve() throws Exception {
        Object result = fieldProperty.readResolve();

        assertSame(fieldProperty, result);
    }

    @Test
    public void testSkipNulls() {
        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(any())).thenReturn(null);

        fieldProperty = new FieldProperty(fieldProperty, null, nva);

        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testSkipNullsWhenWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(any())).thenReturn(null);

        fieldProperty = new FieldProperty(fieldProperty, null, nva);

        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testSkipNullsWhenWithValueDeserializerAndNullProvider() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(any())).thenReturn(null);

        fieldProperty = new FieldProperty(fieldProperty, deserializer, nva);

        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testSkipNullsWhenWithNullProviderAndValueDeserializer() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(any())).thenReturn(null);

        fieldProperty = new FieldProperty(fieldProperty, nva, deserializer);

        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testSkipNullsWhenWithNullProviderAndValueDeserializerAndTypeDeserializer() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        fieldProperty = new FieldProperty(fieldProperty, deserializer, nva, typeDeserializer);

        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testSkipNullsWhenWithValueDeserializerAndTypeDeserializer() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        fieldProperty = new FieldProperty(fieldProperty, deserializer, typeDeserializer);

        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testSkipNullsWhenWithTypeDeserializer() {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        fieldProperty = new FieldProperty(fieldProperty, typeDeserializer);

        assertFalse(fieldProperty.isSkipNulls());
    }
}