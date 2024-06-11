package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
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
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;

/**
 * Test class for SetterlessProperty.
 */
public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Object instance;
    private JsonDeserializer<Object> jsonDeserializer;
    private TypeDeserializer typeDeserializer;
    private NullValueProvider nullValueProvider;
    private AnnotatedMethod annotatedMethod;

    @Before
    public void setUp() throws Exception {
        annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(mock(Object.class));
        when(annotatedMethod.getName()).thenReturn("testMethod");
        when(annotatedMethod.getMember()).thenReturn(annotatedMethod);

        Method getter = mock(Method.class);
        when(getter.getName()).thenReturn("getTestProperty");
        when(getter.invoke(instance, new Object[0])).thenReturn(new Object());

        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        when(beanPropertyDefinition.getName()).thenReturn("testProperty");
        when(beanPropertyDefinition.getBeanClass()).thenReturn(Object.class);
        when(beanPropertyDefinition.getType()).thenReturn(Object.class);

        jsonDeserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);

        setterlessProperty = new SetterlessProperty(beanPropertyDefinition, Object.class.getType(),
                typeDeserializer, Annotations.empty(), annotatedMethod, getter);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        instance = mock(Object.class);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("test Value");

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        Object testProperty = BeanUtil.findPropertyValue(instance, "testProperty");
        assertEquals("test Value", testProperty);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        Object testProperty = BeanUtil.findPropertyValue(instance, "testProperty");
        assertNotNull(testProperty);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(instance, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        setterlessProperty.setAndReturn(instance, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithName() throws IOException {
        setterlessProperty.withName(new PropertyName("testProperty2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithValueDeserializer() throws IOException {
        setterlessProperty.withValueDeserializer(jsonDeserializer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithNullProvider() throws IOException {
        setterlessProperty.withNullProvider(nullValueProvider);
    }
}