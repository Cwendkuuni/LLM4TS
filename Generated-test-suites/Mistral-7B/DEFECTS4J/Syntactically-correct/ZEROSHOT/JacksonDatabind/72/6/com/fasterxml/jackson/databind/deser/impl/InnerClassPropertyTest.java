package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

/**
 * This class tests the {@link InnerClassProperty} class.
 */
public class InnerClassPropertyTest extends TestBase {

    private InnerClassProperty innerClassProperty;
    private SettableBeanProperty delegate;
    private Constructor<Object> constructor;
    private AnnotatedConstructor annotatedConstructor;
    private JsonDeserializer<Object> deserializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Class<Object> innerClass = mock(Class.class);
        Object instance = new Object();
        delegate = mock(SettableBeanProperty.class);
        when(delegate.getMember()).thenReturn(new AnnotatedMethod(null, null, null, null));
        when(delegate.getName()).thenReturn("name");
        when(delegate.getAnnotations()).thenReturn(new Annotation[] {});
        when(delegate.getBeanPropertyDefinition()).thenReturn(new BeanPropertyDefinition(), null);
        when(delegate.getCreator()).thenReturn(null);

        constructor = mock(Constructor.class);
        when(constructor.getParameterTypes()).thenReturn(new Class[] { Object.class });
        when(constructor.newInstance(any())).then(invocation -> new Object());

        annotatedConstructor = mock(AnnotatedConstructor.class);
        when(annotatedConstructor.getAnnotated()).thenReturn(constructor);
        when(annotatedConstructor.getName()).thenReturn("name");

        innerClassProperty = new InnerClassProperty(delegate, constructor);
        deserializer = mock(JsonDeserializer.class);
        innerClassProperty = new InnerClassProperty(innerClassProperty, deserializer);
    }

    @Test
    public void testConstructor() {
        assertSame(delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
    }

    @Test
    public void testConstructorWithAnnotated() {
        innerClassProperty = new InnerClassProperty(innerClassProperty, annotatedConstructor);
        assertSame(delegate, innerClassProperty._delegate);
        assertSame(annotatedConstructor, innerClassProperty._annotated);
        assertNull(innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithDeserializer() {
        innerClassProperty = new InnerClassProperty(innerClassProperty, deserializer);
        assertSame(delegate, innerClassProperty._delegate);
        assertSame(deserializer, innerClassProperty._valueDeserializer);
        assertNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
    }

    @Test
    public void testConstructorWithName() {
        PropertyName newName = new PropertyName("newName");
        innerClassProperty = new InnerClassProperty(innerClassProperty, newName);
        assertSame(delegate, innerClassProperty._delegate);
        assertSame(newName, innerClassProperty.getName());
        assertSame(deserializer, innerClassProperty._valueDeserializer);
        assertNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = new Object();
        JsonParser parser = createParser(new ObjectMapper(), "{}");
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextTextValue()).thenReturn("value");

        Object instance = new Object();
        innerClassProperty.deserializeAndSet(parser, new DeserializationContext(), instance);

        verify(delegate, times(1)).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = new Object();
        JsonParser parser = createParser(new ObjectMapper(), "{}");
        when(parser.getText()).thenReturn("value");

        Object instance = new Object();
        Object result = innerClassProperty.deserializeSetAndReturn(parser, new DeserializationContext(), instance);
        assertSame(value, result);

        verify(delegate, times(1)).set(instance, value);
        verify(parser, times(1)).nextToken();
    }

    @Test
    public void testSet() throws IOException {
        Object newValue = new Object();
        innerClassProperty.set(instance, newValue);
        verify(delegate, times(1)).set(instance, newValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object newValue = new Object();
        Object result = innerClassProperty.setAndReturn(instance, newValue);
        assertSame(newValue, result);
        verify(delegate, times(1)).set(instance, newValue);
        verify(parser, times(1)).nextToken();
    }

    @Test
    public void testReadResolve() throws Exception {
        Object result = innerClassProperty.readResolve();
        assertSame(innerClassProperty, result);
    }

    @Test
    public void testWriteReplace() throws Exception {
        Object result = innerClassProperty.writeReplace();
        assertSame(innerClassProperty, result);
    }

    @Test
    public void testCreatorNewInstance() throws Exception {
        Object result = innerClassProperty._creator.newInstance(instance);
        assertSame(instance, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorNull() throws Exception {
        innerClassProperty._creator = null;
        innerClassProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(delegate.getAnnotation(Annotation.class)).thenReturn(annotation);

        assertEquals(annotation, innerClassProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(delegate.getMember(), innerClassProperty.getMember());
    }

    @Test
    public void testGetValueDeserializer() {
        assertEquals(deserializer, innerClassProperty.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        innerClassProperty = innerClassProperty.withName(newName);
        assertEquals(newName, innerClassProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> deserializer2 = mock(JsonDeserializer.class);
        innerClassProperty = innerClassProperty.withValueDeserializer(deserializer2);
        assertEquals(deserializer2, innerClassProperty._valueDeserializer);
    }

    @Test
    public void testAssignIndex() throws Exception {
        int index = 42;
        innerClassProperty.assignIndex(index);
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        int index = 42;
        innerClassProperty.getPropertyIndex();
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetTwice() throws Exception {
        Object newValue = new Object();
        innerClassProperty.set(instance, newValue);
        innerClassProperty.set(instance, newValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetAndReturnTwice() throws Exception {
        Object newValue = new Object();
        Object result = innerClassProperty.setAndReturn(instance, newValue);
        innerClassProperty.setAndReturn(instance, newValue);
    }

    @Test
    public void testSerializable() throws Exception {
        ObjectWriter writer = new ObjectMapper().writer();
        JsonNode jsonNode = writeValueAsTree(writer, innerClassProperty);
        assertNotNull(jsonNode);
        verify(innerClassProperty).writeReplace();

        ObjectMapper mapper = new ObjectMapper();
        Object node = mapper.readValue(jsonNode, InnerClassProperty.class);
        assertSame(innerClassProperty, node);
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.parser();
        parser.setValueCodec(new SimpleBeanPropertyFilter.SerializeAll());

        parser.nextValue();
        parser.nextToken();

        Object deserialized = innerClassProperty.deserialize(parser, new DeserializationContext(), null);
        assertNull(deserialized);

        parser.setValue(new Object());
        Object deserializedInstance = innerClassProperty.deserialize(parser, new DeserializationContext(), instance);
        assertSame(instance, deserializedInstance);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.parser();
        parser.setValueCodec(new SimpleBeanPropertyFilter.SerializeAll());

        parser.nextValue();
        parser.nextToken();

        parser.setValue("class com.example.MyClass");

        parser.nextValue();
        Object deserialized = innerClassProperty.deserializeWithType(parser, new DeserializationContext(), Object.class);
        assertInstanceOf(Object.class, deserialized);
    }
}