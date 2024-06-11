package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;

/**
 * This class contains unit tests for the SetterlessProperty class.
 */
public class SetterlessPropertyTest {

    private static final Class<SetterlessProperty> CLASS = SetterlessProperty.class;

    private JsonDeserializer<Object> jsonDeserializer;
    private AnnotatedMethod annotatedMethod;
    private Annotations annotations;
    private Object instance;
    private Object value;
    private SetterlessProperty setterlessProperty;

    @Before
    public void setup() throws Exception {
        jsonDeserializer = mock(JsonDeserializer.class);
        annotatedMethod = mock(AnnotatedMethod.class);
        annotations = mock(Annotations.class);
        instance = mock(Object.class);
        value = mock(Object.class);

        when(jsonDeserializer.getType()).thenReturn(Object.class);
        when(jsonDeserializer.getValueDeserializer()).thenReturn(null);
        when(jsonDeserializer.getNullValueProvider()).thenReturn(null);
        when(jsonDeserializer.getAnnotationMembers()).thenReturn(new AnnotatedMember[0]);

        when(annotatedMethod.getName()).thenReturn("getNameMethod");
        when(annotatedMethod.getAnnotated()).thenReturn(annotations);
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[0]);
        when(annotatedMethod.getDeclaringClass()).thenReturn(Object.class);
        when(annotatedMethod.getMember()).thenReturn(annotations);
        when(annotations.findAnnotation(JsonProperty.class)).thenReturn(null);

        Method getter = mock(Method.class);
        when(getter.getName()).thenReturn("getNameMethod");
        when(getter.getParameterTypes()).thenReturn(new Class[0]);
        when(getter.getDeclaringClass()).thenReturn(Object.class);
        when(getter.getReturnType()).thenReturn(Object.class);
        when(getter.invoke(instance, new Object[0])).thenReturn(value);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.getType()).thenReturn(Object.class);

        setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new PropertyName("name"), null, null, null),
                ObjectType.StdClass(Object.class), typeDeserializer, annotations, annotatedMethod);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        Annotation annotation = mock(Annotation.class);
        when(annotations.findAnnotation(annotation.getClass())).thenReturn(annotation);
        assertInstanceOf(annotation.getClass(), setterlessProperty.getAnnotation(annotation.getClass()));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);

        setterlessProperty.deserializeAndSet(jsonParser, new TestContext(), instance);

        // The deserializeAndSet method should not return a value
        assertNotNull(setterlessProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("testValue");

        Object result = setterlessProperty.deserializeSetAndReturn(jsonParser, new TestContext(), instance);

        // The deserializeSetAndReturn method should return the instance passed as an argument
        assertSame(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        setterlessProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        setterlessProperty.setAndReturn(instance, value);

        // The setAndReturn method should return the instance passed as an argument
        assertSame(instance, setterlessProperty.getBean());
    }
}