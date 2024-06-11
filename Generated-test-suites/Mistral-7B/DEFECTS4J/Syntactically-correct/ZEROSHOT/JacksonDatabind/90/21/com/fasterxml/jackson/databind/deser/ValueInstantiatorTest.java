package com.example.junit;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiator() throws IOException {
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Test getValueClass
        when(valueInstantiator.getValueClass()).thenReturn(String.class);
        assertEquals(String.class, valueInstantiator.getValueClass());

        // Test getValueTypeDesc
        when(valueInstantiator.getValueTypeDesc()).thenReturn("String");
        assertEquals("String", valueInstantiator.getValueTypeDesc());

        // Test canInstantiate
        when(valueInstantiator.canCreateFromString()).thenReturn(true);
        when(valueInstantiator.canCreateFromInt()).thenReturn(true);
        when(valueInstantiator.canCreateFromLong()).thenReturn(true);
        when(valueInstantiator.canCreateFromDouble()).thenReturn(true);
        when(valueInstantiator.canCreateFromBoolean()).thenReturn(true);
        when(valueInstantiator.canCreateUsingDefault()).thenReturn(true);
        when(valueInstantiator.canCreateUsingDelegate()).thenReturn(true);
        when(valueInstantiator.canCreateFromObjectWith()).thenReturn(true);

        assertTrue(valueInstantiator.canInstantiate());

        // Test canCreateFromString
        when(valueInstantiator.canCreateFromString()).thenReturn(true);
        Object result = valueInstantiator.createFromString(ctxt, "test");
        assertEquals("test", result);

        // Test canCreateFromInt
        when(valueInstantiator.canCreateFromInt()).thenReturn(true);
        result = valueInstantiator.createFromInt(ctxt, 1);
        assertEquals(1, result.hashCode());

        // Test canCreateFromLong
        when(valueInstantiator.canCreateFromLong()).thenReturn(true);
        result = valueInstantiator.createFromLong(ctxt, 1L);
        assertEquals(1L, result.hashCode());

        // Test canCreateFromDouble
        when(valueInstantiator.canCreateFromDouble()).thenReturn(true);
        result = valueInstantiator.createFromDouble(ctxt, 1.0);
        assertEquals(1.0, result.hashCode(), 0.001);

        // Test canCreateFromBoolean
        when(valueInstantiator.canCreateFromBoolean()).thenReturn(true);
        result = valueInstantiator.createFromBoolean(ctxt, true);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        // Test canCreateUsingDefault
        when(valueInstantiator.canCreateUsingDefault()).thenReturn(true);
        result = valueInstantiator.createUsingDefault(ctxt);
        assertEquals(String.class, result.getClass());

        // Test createFromObjectWith
        Object[] args = new Object[]{1, "test"};
        when(valueInstantiator.getFromObjectArguments(config)).thenReturn(new SettableBeanProperty[]{
                new SettableBeanProperty(int.class, "intProperty"),
                new SettableBeanProperty(String.class, "stringProperty")
        });
        when(valueInstantiator.createFromObjectWith(ctxt, args)).thenCallRealMethod();
        result = valueInstantiator.createFromObjectWith(ctxt, args);
        assertEquals(Arrays.asList(1, "test"), Arrays.asList(result.getClass().getDeclaredFields()[0].get(result),
                result.getClass().getDeclaredFields()[1].get(result)));

        // Test createUsingDelegate
        Object delegate = mock(String.class);
        when(valueInstantiator.getDelegateType(config)).thenReturn(JavaType.construct(String[].class));
        when(valueInstantiator.createUsingDelegate(ctxt, delegate)).thenCallRealMethod();
        result = valueInstantiator.createUsingDelegate(ctxt, delegate);
        assertEquals(String[].class, result.getClass());

        // Test createUsingArrayDelegate
        when(valueInstantiator.getArrayDelegateType(config)).thenReturn(JavaType.construct(String[].class));
        result = valueInstantiator.createUsingArrayDelegate(ctxt, delegate);
        assertEquals(String[].class, result.getClass());

        // Test createUsingDefault
        result = valueInstantiator.createUsingDefault(ctxt);
        assertEquals(String.class, result.getClass());

        // Test createFromObjectWith
        SettableBeanProperty[] props = new SettableBeanProperty[]{
                new SettableBeanProperty(int.class, "intProperty"),
                new SettableBeanProperty(String.class, "stringProperty")
        };
        when(valueInstantiator.getFromObjectArguments(config)).thenReturn(props);
        result = valueInstantiator.createFromObjectWith(ctxt, props, new PropertyValueBuffer());
        assertEquals(1, result.getClass().getDeclaredFields()[0].get(result).hashCode());
        assertEquals("test", result.getClass().getDeclaredFields()[1].get(result));

        // Test createUsingDelegate
        when(valueInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));
        result = valueInstantiator.createUsingDefault(ctxt);
        assertNotNull(result);

        // Test createUsingDelegate
        when(valueInstantiator.getDelegateCreator()).thenReturn(mock(AnnotatedWithParams.class));
        result = valueInstantiator.createUsingDelegate(ctxt, mock(String.class));
        assertNotNull(result);

        // Test createUsingArrayDelegate
        when(valueInstantiator.getArrayDelegateCreator()).thenReturn(mock(AnnotatedWithParams.class));
        result = valueInstantiator.createUsingArrayDelegate(ctxt, mock(String[].class));
        assertNotNull(result);

        // Test createUsingDelegate
        when(valueInstantiator.getWithArgsCreator()).thenReturn(mock(AnnotatedWithParams.class));
        result = valueInstantiator.createFromObjectWith(ctxt, mock(Object.class));
        assertNotNull(result);

        // Test incompleteParameter
        when(valueInstantiator.getIncompleteParameter()).thenReturn(mock(AnnotatedParameter.class));
        result = valueInstantiator.createFromObjectWith(ctxt, mock(Object[].class));
        assertNotNull(result);
    }

    @Test
    public void testEmptyStringAsNullObject() throws IOException {
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(valueInstantiator.getValueClass()).thenReturn(String.class);
        when(valueInstantiator.canCreateFromString()).thenReturn(true);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);

        Object result = valueInstantiator.createFromString(ctxt, "");
        assertEquals(null, result);
    }

    @Test
    public void testMissingInstantiator() throws IOException {
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(valueInstantiator.getValueClass()).thenReturn(String.class);
        when(valueInstantiator.canCreateFromString()).thenReturn(false);

        try {
            valueInstantiator.createFromString(ctxt, "test");
            fail();
        } catch (IOException e) {
            assertEquals("no String-argument constructor/factory method to deserialize from String value ('test')", e.getMessage());
        }

        try {
            valueInstantiator.createUsingDefault(ctxt);
            fail();
        } catch (IOException e) {
            assertEquals("no default no-arguments constructor found", e.getMessage());
        }

        try {
            valueInstantiator.createFromObjectWith(ctxt, mock(Object[].class));
            fail();
        } catch (IOException e) {
            assertEquals("no creator with arguments specified", e.getMessage());
        }

        try {
            valueInstantiator.createUsingDelegate(ctxt, mock(Object.class));
            fail();
        } catch (IOException e) {
            assertEquals("no delegate creator specified", e.getMessage());
        }

        try {
            valueInstantiator.createUsingArrayDelegate(ctxt, mock(Object.class));
            fail();
        } catch (IOException e) {
            assertEquals("no array delegate creator specified", e.getMessage());
        }
    }

    @Test
    public void testCreateFromStringFallbacks() throws IOException {
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(valueInstantiator.getValueClass()).thenReturn(Boolean.class);
        when(valueInstantiator.canCreateFromBoolean()).thenReturn(false);
        when(valueInstantiator.canCreateFromString()).thenReturn(true);

        String value = "true";
        Object result = valueInstantiator.createFromString(ctxt, value);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        value = "false";
        result = valueInstantiator.createFromString(ctxt, value);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        value = "";
        result = valueInstantiator.createFromString(ctxt, value);
        assertEquals(null, result);
    }

}