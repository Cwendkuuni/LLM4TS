package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyCreation() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
    }

    @Test
    public void testDefaultConstructor() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(settableAnyProperty._property);
        assertNull(settableAnyProperty._setter);
    }

    @Test
    public void testSetValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty oldSettableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, null, valueTypeDeserializer);
        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);

        SettableAnyProperty newSettableAnyProperty = oldSettableAnyProperty.withValueDeserializer(newValueDeserializer);
        assertEquals(newValueDeserializer, newSettableAnyProperty._valueDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPropertyName() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class), "");
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullValue() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        Object instance = mock(Object.class);
        String propName = "test";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance, propName);

        verify(valueDeserializer, times(1)).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetInvalidType() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        Object instance = mock(Object.class);
        String propName = "test";

        when(valueDeserializer.deserializeWithType(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class))).thenReturn(new Integer(1));
        when(valueTypeDeserializer.findDeserializerForValueType(type)).thenReturn(valueDeserializer);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance, propName);
    }

    @Test
    public void testSetValue() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        Object instance = mock(Object.class);
        Object value = new Integer(1);
        String propName = "test";

        when(setter.getDeclaringClass()).thenReturn(Object.class);
        when(setter.callOnWith(instance, propName, anyObject())).thenAnswer(invocationOnMock -> {
            Object field = invocationOnMock.getArgument(0);
            Map<Object, Object> map = (Map<Object, Object>) field;
            map.put(propName, invocationOnMock.getArgument(2));
        });

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance, propName);

        verify(setter, times(1)).callOnWith(instance, propName, value);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetUnresolvedForwardReference() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);

        Object instance = mock(Object.class);
        String propName = "test";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance, propName);

        verify(valueDeserializer, times(1)).getObjectIdReader();
        verify(reference, times(1)).getRoid();
    }
}