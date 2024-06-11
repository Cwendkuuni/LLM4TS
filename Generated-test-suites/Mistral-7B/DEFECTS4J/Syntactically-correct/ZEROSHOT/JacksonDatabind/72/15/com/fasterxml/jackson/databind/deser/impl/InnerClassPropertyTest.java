package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassProperty() throws IOException {
        // Given
        Class<?> innerClass = new Object() {}.getClass();
        Constructor<?> innerClassConstructor = innerClass.getDeclaredConstructor();
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, innerClassConstructor);

        // When
        InnerClassProperty innerClassProperty1 = new InnerClassProperty(innerClassProperty, annotatedConstructor);

        // Then
        Assert.assertNotSame(innerClassProperty, innerClassProperty1);
        Assert.assertEquals(innerClassProperty.getDelegate(), innerClassProperty1.getDelegate());
        Assert.assertNull(innerClassProperty1.getCreator());
        Assert.assertEquals(innerClassProperty.getAnnotated(), innerClassProperty1.getAnnotated());

        // When
        InnerClassProperty innerClassProperty2 = new InnerClassProperty(innerClassProperty, null);

        // Then
        Assert.assertNotSame(innerClassProperty, innerClassProperty2);
        Assert.assertEquals(innerClassProperty.getDelegate(), innerClassProperty2.getDelegate());
        Assert.assertNull(innerClassProperty2.getCreator());
        Assert.assertNull(innerClassProperty2.getAnnotated());

        // When
        InnerClassProperty innerClassProperty3 = new InnerClassProperty(innerClassProperty, mock(JsonDeserializer.class));

        // Then
        Assert.assertNotSame(innerClassProperty, innerClassProperty3);
        Assert.assertEquals(innerClassProperty.getDelegate(), innerClassProperty3.getDelegate());
        Assert.assertSame(innerClassProperty.getValueDeserializer(), innerClassProperty3.getValueDeserializer());
        Assert.assertNull(innerClassProperty3.getCreator());
        Assert.assertNull(innerClassProperty3.getAnnotated());

        // When
        InnerClassProperty innerClassProperty4 = new InnerClassProperty(innerClassProperty, new PropertyName("test"));

        // Then
        Assert.assertNotSame(innerClassProperty, innerClassProperty4);
        Assert.assertEquals(innerClassProperty.getDelegate(), innerClassProperty4.getDelegate());
        Assert.assertEquals(innerClassProperty.getName(), innerClassProperty4.getName());
        Assert.assertNull(innerClassProperty4.getCreator());
        Assert.assertNull(innerClassProperty4.getAnnotated());

        // When calling methods
        innerClassProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        innerClassProperty1.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        innerClassProperty2.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        innerClassProperty3.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));
        innerClassProperty4.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), mock(Object.class));

        innerClassProperty.set(mock(Object.class), mock(Object.class));
        innerClassProperty1.set(mock(Object.class), mock(Object.class));
        innerClassProperty2.set(mock(Object.class), mock(Object.class));
        innerClassProperty3.set(mock(Object.class), mock(Object.class));
        innerClassProperty4.set(mock(Object.class), mock(Object.class));

        innerClassProperty.setAndReturn(mock(Object.class), mock(Object.class));
        innerClassProperty1.setAndReturn(mock(Object.class), mock(Object.class));
        innerClassProperty2.setAndReturn(mock(Object.class), mock(Object.class));
        innerClassProperty3.setAndReturn(mock(Object.class), mock(Object.class));
        innerClassProperty4.setAndReturn(mock(Object.class), mock(Object.class));

        innerClassProperty.readResolve();
        innerClassProperty1.readResolve();
        innerClassProperty2.readResolve();
        innerClassProperty3.readResolve();
        innerClassProperty4.readResolve();

        innerClassProperty.writeReplace();
        innerClassProperty1.writeReplace();
        innerClassProperty2.writeReplace();
        innerClassProperty3.writeReplace();
        innerClassProperty4.writeReplace();
    }
}