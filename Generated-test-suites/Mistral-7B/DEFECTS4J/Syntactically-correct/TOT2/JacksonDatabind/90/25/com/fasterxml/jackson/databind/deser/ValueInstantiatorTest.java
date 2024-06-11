package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserArgs;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorAbstractClass() throws IOException {
        ValueInstantiator instantiator = Mockito.spy(new Base(String.class));
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ObjectNode node = Mockito.mock(ObjectNode.class);

        // Test getValueClass and getValueTypeDesc methods
        assertEquals("java.lang.String", instantiator.getValueClass().getName());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());

        // Test canInstantiate method
        assertFalse(instantiator.canInstantiate());

        // Test createUsingDefault method with no arguments
        assertNull(instantiator.createUsingDefault(context));

        // Test createFromObjectWith method
        AnnotatedClass ac = Mockito.mock(AnnotatedClass.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[0];
        when(ac.findPropertiesWithAnnotation(AnnotatedMethod.class, ValueInstantiator.ValueInstantiatorFactory.class))
                .thenReturn(properties);
        when(context.findValueInstantiator(ac, null)).thenReturn(instantiator);
        when(context.findClassId(ac)).thenReturn("MyClass");

        Object result = instantiator.createFromObjectWith(context, null);
        assertNull(result);

        // Test createFromObjectWith method with arguments
        AnnotatedField argField = Mockito.mock(AnnotatedField.class);
        PropertiesProperties argProperties = Mockito.mock(PropertiesProperties.class);
        when(argField.getAnnotation(ValueInstantiator.ValueInstantiatorFactory.class)).thenReturn(argProperties);
        when(argProperties.creatorFromObject()).thenReturn(argField);
        AnnotatedMethod argMethod = Mockito.mock(AnnotatedMethod.class);
        when(argMethod.getParameterName()).thenReturn("argName");
        when(argMethod.getConstructor()).thenReturn(argMethod);
        when(argMethod.getParameters()).thenReturn(new AnnotatedParameter[0]);
        when(ac.findAbstractTypeAnnotations()).thenReturn(Collections.singletonList(argMethod.getAnnotations()));

        SettableBeanProperty argProp = new SettableBeanProperty("argName", String.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);
        when(buffer.getParameters(argProp)).thenReturn(new Object[]{argField});

        result = instantiator.createFromObjectWith(context, argProp, buffer);
        assertInstanceOf(String.class, result);

        // Test createUsingDelegate method
        Object delegate = Mockito.mock(Object.class);
        when(instantiator.createUsingDelegate(context, delegate)).thenCallRealMethod();
        result = instantiator.createUsingDelegate(context, delegate);
        assertNull(result);

        // Test createUsingArrayDelegate method
        when(instantiator.getArrayDelegateType(context)).thenReturn(JavaType.constructUnboundedArrayType(Integer.class));
        Object[] delegateArray = new Object[]{new Integer(1)};
        result = instantiator.createUsingArrayDelegate(context, delegateArray[0]);
        assertInstanceOf(Integer[].class, result.getClass());
        assertSame(new Integer(1)[0], result);
    }

    @Test
    public void testStdValueInstantiator() throws IOException {
        ValueInstantiator instantiator = new StdValueInstantiator(String.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        ObjectNode node = Mockito.mock(ObjectNode.class);

        // Test getValueClass and getValueTypeDesc methods
        assertEquals("java.lang.String", instantiator.getValueClass().getName());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());

        // Test canInstantiate method
        assertTrue(instantiator.canInstantiate());

        // Test createUsingDefault method with no arguments
        String result = instantiator.createUsingDefault(context).toString();
        assertEquals("", result);

        // Test createFromObjectWith method
        AnnotatedClass ac = Mockito.mock(AnnotatedClass.class);
        SettableBeanProperty argProp = new SettableBeanProperty("argName", String.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);
        when(buffer.getParameters(argProp)).thenReturn(new Object[]{argProp});
        when(context.findValueInstantiator(ac, null)).thenReturn(instantiator);
        when(context.findClassId(ac)).thenReturn("MyClass");

        result = instantiator.createFromObjectWith(context, argProp, buffer).toString();
        assertEquals("", result);

        // Test createUsingDelegate method
        Object delegate = Mockito.mock(Object.class);
        when(instantiator.createUsingDelegate(context, delegate)).thenCallRealMethod();
        result = instantiator.createUsingDelegate(context, delegate).toString();
        assertEquals("", result);

        // Test createUsingArrayDelegate method
        when(instantiator.getArrayDelegateType(context)).thenReturn(JavaType.constructUnboundedArrayType(Integer.class));
        Object[] delegateArray = new Object[]{new Integer(1)};
        result = instantiator.createUsingArrayDelegate(context, delegateArray[0]).toString();
        assertEquals("[", result);

        // Test createFromString method
        String value = "\"Hello World\"";
        result = instantiator.createFromString(context, value);
        assertEquals("Hello World", result);

        // Test createFromInt method
        int intValue = 42;
        result = instantiator.createFromInt(context, intValue);
        assertEquals(intValue, result.intValue());

        // Test createFromLong method
        long longValue = 42L;
        result = instantiator.createFromLong(context, longValue);
        assertEquals(longValue, result.longValue());

        // Test createFromDouble method
        double doubleValue = 42.1;
        result = instantiator.createFromDouble(context, doubleValue);
        assertEquals(doubleValue, result.doubleValue());

        // Test createFromBoolean method
        boolean boolValue = true;
        result = instantiator.createFromBoolean(context, boolValue);
        assertEquals(boolValue, result.booleanValue());
    }
}