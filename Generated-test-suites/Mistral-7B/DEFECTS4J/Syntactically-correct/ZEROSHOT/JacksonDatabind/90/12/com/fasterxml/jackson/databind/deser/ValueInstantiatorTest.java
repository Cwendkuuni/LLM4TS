package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.CreatorProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SettableBeanProperty;

/**
 * Test file for {@link ValueInstantiator} class.
 */
public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator {

        @Override
        public boolean canCreateUsingDefault() {
            return true;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[0];
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return new Object();
        }

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            return mock(AnnotatedWithParams.class);
        }
    }

    private static class TestConstructor {
        @JsonCreator
        public TestConstructor(@JsonProperty("a") int a, @JsonProperty("b") String b) {
            this.a = a;
            this.b = b;
        }

        int a;
        String b;
    }

    @org.junit.Test
    public void testValueInstantiator() throws IOException {
        // Test getValueTypeDesc()
        ValueInstantiator instantiator = new TestValueInstantiator(Object.class);
        assertEquals("java.lang.Object", instantiator.getValueTypeDesc());

        // Test canInstantiate()
        instantiator = new TestValueInstantiator(Object.class);
        assertTrue("Instance should be able to instantiate Object", instantiator.canInstantiate());

        // Test getDelegateType()
        instantiator = spy(new TestValueInstantiator(String.class));
        when(instantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));
        JavaType delegateType = instantiator.getDelegateType(mock(DeserializationConfig.class));
        assertNull("Delegate type should be null for Object instantiator", delegateType);

        // Test createUsingDefault()
        instantiator = new TestValueInstantiator(Object.class);
        Object result = instantiator.createUsingDefault(mock(DeserializationContext.class));
        assertEquals("Default constructor should create new instance", result, new Object());

        // Test createFromObjectWith()
        TestValueInstantiator testValueInstantiator = new TestValueInstantiator(TestConstructor.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getDeserializationConfig();
        Object[] args = new Object[]{1, "test"};
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = mock(SettableBeanProperty.class);
        properties[1] = mock(SettableBeanProperty.class);
        when(properties[0].getName()).thenReturn("a");
        when(properties[1].getName()).thenReturn("b");
        when(properties[0].getParameter().getRawType()).thenReturn(int.class);
        when(properties[1].getParameter().getRawType()).thenReturn(String.class);
        when(config.findBeanProperty( TestConstructor.class, "a")).thenReturn(properties[0]);
        when(config.findBeanProperty( TestConstructor.class, "b")).thenReturn(properties[1]);

        when(testValueInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));
        when(testValueInstantiator.getFromObjectArguments(config)).thenReturn(properties);

        result = testValueInstantiator.createFromObjectWith(mock(DeserializationContext.class), args);
        assertEquals("Instance created from JSON Object should match constructor arguments",
                new TestConstructor(1, "test"), result);

        // Test createFromObjectWith() when no CreatorProperty found
        when(testValueInstantiator.getFromObjectArguments(config)).thenReturn(new ArrayList<>());
        result = testValueInstantiator.createFromObjectWith(mock(DeserializationContext.class), args);
        assertNull("Instance created from JSON Object when no CreatorProperty found should be null", result);

        // Test createFromObjectWith() when no default constructor
        when(testValueInstantiator.getDefaultCreator()).thenReturn(null);
        when(testValueInstantiator.canCreateUsingDefault()).thenReturn(false);
        when(testValueInstantiator.canCreateUsingDelegate()).thenReturn(false);
        when(testValueInstantiator.canCreateFromObjectWith()).thenReturn(false);
        when(testValueInstantiator.canCreateFromString()).thenReturn(false);
        when(testValueInstantiator.canCreateFromInt()).thenReturn(false);
        when(testValueInstantiator.canCreateFromLong()).thenReturn(false);
        when(testValueInstantiator.canCreateFromDouble()).thenReturn(false);
        when(testValueInstantiator.canCreateFromBoolean()).thenReturn(false);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(testValueInstantiator.getDelegateType(config)).thenReturn(mock(JavaType.class));
        when(testValueInstantiator.getArrayDelegateType(config)).thenReturn(mock(JavaType.class));
        when(testValueInstantiator.createFromObjectWith(mock(DeserializationContext.class), mock(Object[].class))).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createUsingDefault(mock(DeserializationContext.class))).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createFromString(mock(DeserializationContext.class), anyString())).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createFromInt(mock(DeserializationContext.class), anyInt())).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createFromLong(mock(DeserializationContext.class), anyLong())).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createFromDouble(mock(DeserializationContext.class), anyDouble())).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createFromBoolean(mock(DeserializationContext.class), anyBoolean())).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createUsingDelegate(mock(DeserializationContext.class), any())).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.createUsingArrayDelegate(mock(DeserializationContext.class), any())).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.getDefaultCreator()).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.getDelegateCreator()).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.getArrayDelegateCreator()).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.getWithArgsCreator()).thenThrow(new JsonMappingException(""));
        when(testValueInstantiator.getIncompleteParameter()).thenReturn(null);

        result = testValueInstantiator.createValueInstance(mock(DeserializationContext.class), mock(CreatorProvider.class));
        assertNull("Instance creation should fail when no applicable method found", result);
    }

    @org.junit.Test
    public void testCreateValueInstance() throws IOException {
        TestValueInstantiator testValueInstantiator = new TestValueInstantiator(Object.class);
        DeserializationContext context = mock(DeserializationContext.class);
        CreatorProvider creatorProvider = mock(CreatorProvider.class);

        when(testValueInstantiator.canCreateUsingDefault()).thenReturn(false);
        when(testValueInstantiator.canCreateUsingDelegate()).thenReturn(false);
        when(testValueInstantiator.canCreateFromObjectWith()).thenReturn(false);
        when(testValueInstantiator.canCreateFromString()).thenReturn(false);
        when(testValueInstantiator.canCreateFromInt()).thenReturn(false);
        when(testValueInstantiator.canCreateFromLong()).thenReturn(false);
        when(testValueInstantiator.canCreateFromDouble()).thenReturn(false);
        when(testValueInstantiator.canCreateFromBoolean()).thenReturn(false);

        testValueInstantiator.createValueInstance(context, creatorProvider);
        verify(context).handleMissingInstantiator(testValueInstantiator.getValueClass(), eq(context.getParser()), anyString());
    }
}