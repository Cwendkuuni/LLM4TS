package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

@JacksonStdImpl
public class CollectionDeserializerTest extends BaseTest {

    private CollectionDeserializer deserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;
    private JsonParser mockParser;
    private DeserializationContext mockContext;

    @BeforeMethod
    public void setUp() throws Exception {
        valueDeserializer = mock(JsonDeserializer.class);
        valueTypeDeserializer = mock(TypeDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeserializer = mock(JsonDeserializer.class);
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        when(mockContext.findContextualValueDeserializer(any(Class.class), any(PropertyName.class)))
                .thenReturn(valueDeserializer);
        when(mockContext.handleSecondaryContextualization(any(JsonDeserializer.class), any(BeanProperty.class), any(Class.class)))
                .thenReturn(valueDeserializer);
        deserializer = new CollectionDeserializer(createCollectionType(), valueDeserializer, valueTypeDeserializer, valueInstantiator,
                delegateDeserializer, null);
    }

    @Test
    public void testCreateContextualWithNullDelegateDeserializer() {
        CollectionDeserializer contextualDeserializer = deserializer.createContextual(mockContext, nullBeanProperty());
        assertNotNull(contextualDeserializer);
        assertEquals(contextualDeserializer.delegateDeserializer, null);
    }

    @Test
    public void testCreateContextualWithDelegateDeserializer() {
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        CollectionDeserializer contextualDeserializer = deserializer.createContextual(mockContext, nullBeanProperty())
                .withResolved(delegateDeserializer, valueDeserializer, valueTypeDeserializer, null);
        assertEquals(contextualDeserializer.delegateDeserializer, delegateDeserializer);
    }

    @Test
    public void testIsCachable() throws Exception {
        assertTrue(deserializer.isCachable());
        ValueDeserializerWithType valueDeserializerWithType = mock(ValueDeserializerWithType.class);
        deserializer = new CollectionDeserializer(createCollectionType(), valueDeserializerWithType, valueTypeDeserializer, valueInstantiator,
                delegateDeserializer, null);
        assertFalse(deserializer.isCachable());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIsCachableWithNullValueDeserializer() throws Exception {
        deserializer = new CollectionDeserializer(createCollectionType(), null, valueTypeDeserializer, valueInstantiator,
                delegateDeserializer, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIsCachableWithNullValueTypeDeserializer() throws Exception {
        deserializer = new CollectionDeserializer(createCollectionType(), valueDeserializer, null, valueInstantiator,
                delegateDeserializer, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIsCachableWithNullDelegateDeserializer() throws Exception {
        deserializer = new CollectionDeserializer(createCollectionType(), valueDeserializer, valueTypeDeserializer, valueInstantiator,
                null, null);
    }

    @Test
    public void testCreateContextualWithValueInstantiator() throws Exception {
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        when(valueInstantiator.createUsingDelegate()).thenReturn(createValueStub());
        ValueDeserializerWithType valueDeserializerWithType = mock(ValueDeserializerWithType.class);
        CollectionDeserializer contextualDeserializer = deserializer.createContextual(mockContext, nullBeanProperty())
                .withResolved(valueDeserializerWithType, valueDeserializer, valueTypeDeserializer, null)
                .withResolved(null, valueInstantiator, null);
        Collection<Object> result = contextualDeserializer.deserialize(mockParser, mockContext);
        verify(valueInstantiator).createUsingDelegate(mockContext, argThat(isA(Object.class)));
        assertEquals(result.size(), 1);
    }

    @Test
    public void testCreateContextualWithValueDeserializerAndValueTypeDeserializer() throws Exception {
        when(valueDeserializer.getNullValue()).thenReturn(null);
        JsonDeserializer<Object> valueDeserializerWithType = mock(ValueDeserializerWithType.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        CollectionDeserializer contextualDeserializer = deserializer.createContextual(mockContext, nullBeanProperty())
                .withResolved(valueDeserializerWithType, valueDeserializer, valueTypeDeserializer, null);
        Object value = createValueStub();
        Object valueWithType = createValueWithTypeStub();
        when(valueDeserializerWithType.deserializeWithType(mockParser, mockContext, valueTypeDeserializer)).thenReturn(valueWithType);
        when(valueDeserializer.deserialize(mockParser, mockContext)).thenReturn(value);

        Collection<Object> result = contextualDeserializer.deserialize(mockParser, mockContext);
        assertEquals(result.size(), 1);
        verify(valueDeserializerWithType).deserializeWithType(mockParser, mockContext, valueTypeDeserializer);
        assertEquals(result.get(0), valueWithType);
    }

    @Test
    public void testDeserializeJsonParserWithNullContext() throws Exception {
        Collection<Object> result = deserializer.deserialize(mockParser, null);
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    @Test
    public void testDeserializeJsonParserWithContext() throws Exception {
        Object value = createValueStub();
        Object valueWithType = createValueWithTypeStub();
        when(valueDeserializer.deserializeWithType(mockParser, mockContext, valueTypeDeserializer)).thenReturn(valueWithType);
        when(valueDeserializer.deserialize(mockParser, mockContext)).thenReturn(value);

        Collection<Object> result = deserializer.deserialize(mockParser, mockContext);
        assertEquals(result.size(), 1);
        verify(valueDeserializer).deserializeWithType(mockParser, mockContext, valueTypeDeserializer);
        verify(valueDeserializer).deserialize(mockParser, mockContext);
        assertEquals(result.get(0), valueWithType);
    }

    @Test
    public void testDeserializeJsonParserWithTypeDeserializer() throws Exception {
        Object valueWithType = createValueWithTypeStub();
        when(valueDeserializer.deserializeWithType(mockParser, mockContext, valueTypeDeserializer)).thenReturn(valueWithType);

        Collection<Object> result = deserializer.deserializeWithType(mockParser, mockContext, valueTypeDeserializer);
        assertEquals(result.size(), 1);
        verify(valueDeserializer).deserializeWithType(mockParser, mockContext, valueTypeDeserializer);
        assertEquals(result.get(0), valueWithType);
    }

    private JavaType createCollectionType() {
        JavaType collectionType = mock(JavaType.class);
        when(collectionType.getContentType()).thenReturn(createCollectionContentType());
        return collectionType;
    }

    private JavaType createCollectionContentType() {
        JavaType javaType = mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(Object.class);
        return javaType;
    }

    private Object createValueStub() {
        return new Object() {
        };
    }

    private Object createValueWithTypeStub() {
        Object value = createValueStub();
        Type type = mock(Type.class);
        when(valueDeserializer.getType(mockParser, mockContext)).thenReturn(type);
        when(valueTypeDeserializer.deserializeTypedFromArray(mockParser, mockContext)).thenReturn(value);
        return new ObjectWithType(value, type);
    }

    private static class ObjectWithType {
        private final Object value;
        private final Type type;

        public ObjectWithType(Object value, Type type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            return "ObjectWithType{" + "value=" + value + '}';
        }
    }
}