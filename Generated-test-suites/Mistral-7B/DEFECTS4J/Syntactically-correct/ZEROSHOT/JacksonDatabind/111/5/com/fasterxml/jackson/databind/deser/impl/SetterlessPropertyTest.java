package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link SetterlessProperty} class.
 */
public class SetterlessPropertyTest {

    private static final Object DUMMY_INSTANCE = new Object() {};
    private static final Object DUMMY_VALUE = new Object() {};
    private static final BeanPropertyDefinition DUMMY_PROPERTY_DEFINITION = Mockito.mock(BeanPropertyDefinition.class);
    private static final JavaType DUMMY_TYPE = Mockito.mock(JavaType.class);
    private static final TypeDeserializer DUMMY_TYPE_DESERIALIZER = Mockito.mock(TypeDeserializer.class);
    private static final Annotations DUMMY_ANNOTATIONS = Mockito.mock(Annotations.class);
    private static final AnnotatedMethod DUMMY_ANNOTATED_METHOD = Mockito.mock(AnnotatedMethod.class);
    private static final Method DUMMY_GETTER = Mockito.mock(Method.class);

    private SettableBeanProperty setterlessProperty;
    private Object testInstance;

    @Before
    public void setUp() throws Exception {
        when(DUMMY_ANNOTATED_METHOD.getAnnotated()).thenReturn(DUMMY_ANNOTATIONS);
        when(DUMMY_ANNOTATIONS.hasAnnotation(JsonSetter.class)).thenReturn(true);
        when(DUMMY_ANNOTATED_METHOD.getName()).thenReturn("testProperty");
        when(DUMMY_ANNOTATED_METHOD.getMethod()).thenReturn(DUMMY_GETTER);

        setterlessProperty = new SetterlessProperty(DUMMY_PROPERTY_DEFINITION, DUMMY_TYPE, DUMMY_TYPE_DESERIALIZER, DUMMY_ANNOTATIONS, DUMMY_ANNOTATED_METHOD);
        testInstance = new Object() {};
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        ObjectMocker jsonValue = new ObjectMocker("testValue");
        when(mockParser.getValueAsTokem()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getValueAsString()).thenReturn(jsonValue.getValue());
        when(mockParser.getText()).thenReturn(jsonValue.getValue());
        when(mockParser.getCurrentName()).thenReturn("testProperty");

        SetterlessProperty setterlessProperty = new SetterlessProperty(setterlessProperty, Mockito.mock(JsonDeserializer.class.getDeclaredConstructor(JsonDeserializer.class)).useConstructorHandler(), Mockito.mock(NullValueProvider.class));

        setterlessProperty.deserializeAndSet(mockParser, Mockito.mock(DeserializationContext.class), testInstance);

        Object testProperty = BeanUtil.getFieldValue(testInstance, "testProperty");
        assertEquals(jsonValue.getValue(), testProperty);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        setterlessProperty.set(testInstance, DUMMY_VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        setterlessProperty.setAndReturn(testInstance, DUMMY_VALUE);
    }
}

class ObjectMocker {
    private final String value;

    public ObjectMocker(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}