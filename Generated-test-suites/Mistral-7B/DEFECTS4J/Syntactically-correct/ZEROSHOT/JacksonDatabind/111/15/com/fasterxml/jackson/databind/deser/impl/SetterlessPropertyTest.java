package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

/**
 * Test class for SetterlessProperty.
 */
public class SetterlessPropertyTest {

    private SettableBeanProperty setterlessProperty;
    private Object target;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setUp() {
        target = mock(Object.class);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);

        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        Method getter = mock(Method.class);

        when(annotatedMethod.getAnnotations()).thenReturn(contextAnnotations);
        when(annotatedMethod.getDeclaringClass()).thenReturn(Object.class);
        when(annotatedMethod.getName()).thenReturn("testMethod");
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[0]);
        when(annotatedMethod.getReturnType()).thenReturn(Object.class);
        when(annotatedMethod.getAnnotations().findValue(JsonProperty.class)).thenReturn(new JsonProperty());

        when(contextAnnotations.findValue(JsonProperty.class)).thenReturn(new JsonProperty());

        when(beanPropertyDefinition.getName()).thenReturn("testProperty");
        when(beanPropertyDefinition.getBeanClass()).thenReturn(Object.class);
        when(beanPropertyDefinition.getType()).thenReturn(Object.class);
        when(beanPropertyDefinition.getSetter()).thenReturn(null);
        when(beanPropertyDefinition.getGetter()).thenReturn(getter);

        setterlessProperty = new SetterlessProperty(beanPropertyDefinition, ObjectTypeFactory.constructType(Object.class), typeDeserializer, contextAnnotations, annotatedMethod);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = new Object();
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextFieldName()).thenReturn("testProperty");
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("Test Value");
        when(jsonParser.nextValue()).thenReturn(null);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, target);

        Object propertyValue = null;
        try {
            propertyValue = getter.invoke(target, (Object[]) null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException(e.getMessage());
        }

        assertEquals("Test Value", propertyValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = new Object();
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextFieldName()).thenReturn("testProperty");
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("Test Value");
        when(jsonParser.nextValue()).thenReturn(null);

        Object returnValue = setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, target);

        assertEquals(target, returnValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object value = new Object();

        setterlessProperty.set(target, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();

        setterlessProperty.setAndReturn(target, value);

        assertEquals(target, target);
    }

    @Test
    public void testNullValue() throws IOException {
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, target);

        Object propertyValue = null;
        try {
            propertyValue = getter.invoke(target, (Object[]) null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException(e.getMessage());
        }

        assertNull(propertyValue);
    }

    @Test
    public void testSetValueDeserializer() throws IOException {
        ObjectDeserializer valueDeserializer = mock(ObjectDeserializer.class);

        setterlessProperty = new SetterlessProperty(setterlessProperty, valueDeserializer, nullValueProvider);

        Object value = new Object();
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextFieldName()).thenReturn("testProperty");
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("Test Value");
        when(jsonParser.nextValue()).thenReturn(null);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, target);

        Object propertyValue = null;
        try {
            propertyValue = getter.invoke(target, (Object[]) null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException(e.getMessage());
        }

        assertEquals("Test Value", propertyValue);

        verify(valueDeserializer).deserialize(ArgumentMatchers.eq(jsonParser), ArgumentMatchers.eq(deserializationContext), ArgumentMatchers.eq(propertyValue));
    }

    @Test
    public void testNullValueProvider() throws IOException {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.findNullValue(Object.class)).thenReturn("nullValue");

        setterlessProperty = new SetterlessProperty(setterlessProperty, null, nullValueProvider);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, target);

        Object propertyValue = null;
        try {
            propertyValue = getter.invoke(target, (Object[]) null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException(e.getMessage());
        }

        assertEquals("nullValue", propertyValue);
    }

    @Test
    public void testSetterlessPropertyWithName() {
        SettableBeanProperty setterlessPropertyWithName = setterlessProperty.withName(new PropertyName("testPropertyWithName"));

        assertEquals("testPropertyWithName", setterlessPropertyWithName.getName());
    }

    @Test
    public void testSetterlessPropertyWithValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        SettableBeanProperty setterlessPropertyWithValueDeserializer = setterlessProperty.withValueDeserializer(valueDeserializer);

        assertEquals(valueDeserializer, setterlessPropertyWithValueDeserializer.getValueDeserializer());
    }

    @Test
    public void testSetterlessPropertyWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        SettableBeanProperty setterlessPropertyWithNullProvider = setterlessProperty.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, setterlessPropertyWithNullProvider.getNullProvider());
    }

    @Test
    public void testFixAccess() throws IOException {
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("Test Value");

        setterlessProperty.fixAccess(deserializationContext);

        verify(annotatedMethod).fixAccess(ArgumentMatchers.eq(deserializationContext.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)));
    }
}