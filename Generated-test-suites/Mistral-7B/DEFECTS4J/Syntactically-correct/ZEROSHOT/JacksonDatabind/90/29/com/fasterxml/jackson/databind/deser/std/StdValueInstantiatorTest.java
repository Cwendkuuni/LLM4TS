package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdValueInstantiatorTest {

    private StdValueInstantiator stdValueInstantiator;
    private DeserializationContext context;
    private BeanDescription beanDescription;

    @Before
    public void setUp() throws Exception {
        context = mock(DeserializationContext.class);
        beanDescription = mock(BeanDescription.class);
        when(beanDescription.getBeanClass()).thenReturn(String.class);
        when(beanDescription.getParameterNames()).thenReturn(new String[]{"param1"});
        stdValueInstantiator = new StdValueInstantiator(null, null) {
            @Override
            public void configureFromObjectSettings(AnnotatedWithParams defaultCreator,
                    AnnotatedWithParams delegateCreator, JavaType delegateType, SettableBeanProperty[] delegateArgs,
                    AnnotatedWithParams withArgsCreator, SettableBeanProperty[] constructorArgs) {
                this._defaultCreator = defaultCreator;
                this._delegateCreator = delegateCreator;
                this._delegateType = delegateType;
                this._delegateArguments = delegateArgs;
                this._withArgsCreator = withArgsCreator;
                this._constructorArguments = constructorArgs;
            }
        };
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        when(stdValueInstantiator._fromStringCreator).thenReturn(mock(AnnotatedWithParams.class));
        assertEquals(true, stdValueInstantiator.canCreateFromString());
    }

    @Test
    public void testCannotCreateFromString() throws Exception {
        when(stdValueInstantiator._fromStringCreator).thenReturn(null);
        assertEquals(false, stdValueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        when(stdValueInstantiator._defaultCreator).thenReturn(mock(AnnotatedWithParams.class));
        assertEquals(true, stdValueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCannotCreateUsingDefault() throws Exception {
        when(stdValueInstantiator._defaultCreator).thenReturn(null);
        assertEquals(false, stdValueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        Object[] args = new Object[]{"arg1"};
        when(stdValueInstantiator._withArgsCreator).thenReturn(mock(AnnotatedWithParams.class));
        when(stdValueInstantiator._withArgsCreator.call(args)).thenReturn("result");

        assertEquals("result", stdValueInstantiator.createFromObjectWith(context, args));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromObjectWithThrowsException() throws Exception {
        when(stdValueInstantiator._withArgsCreator).thenReturn(mock(AnnotatedWithParams.class));
        when(stdValueInstantiator._withArgsCreator.call(Mockito.anyObject())).thenThrow(IOException.class);

        stdValueInstantiator.createFromObjectWith(context, new Object[0]);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        Object delegate = mock(Object.class);
        when(stdValueInstantiator._delegateCreator).thenReturn(mock(AnnotatedWithParams.class));

        assertEquals(stdValueInstantiator._delegateCreator.getDeclaringClass().getName(),
                stdValueInstantiator.getClass().getName());
        assertEquals(stdValueInstantiator._delegateCreator.getDeclaringClass().getName(),
               stdValueInstantiator.getClass().getName());

        assertEquals(stdValueInstantiator.createUsingDelegate(context, delegate),
                stdValueInstantiator._delegateCreator.call(delegate));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateUsingDelegateThrowsException() throws Exception {
        Object delegate = mock(Object.class);
        when(stdValueInstantiator._delegateCreator).thenReturn(mock(AnnotatedWithParams.class));
        when(stdValueInstantiator._delegateCreator.call(delegate)).thenThrow(IOException.class);

        stdValueInstantiator.createUsingDelegate(context, delegate);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        Object delegate = mock(Object[].class);
        when(stdValueInstantiator._arrayDelegateCreator).thenReturn(mock(AnnotatedWithParams.class));

        assertEquals(stdValueInstantiator._arrayDelegateCreator.getDeclaringClass().getName(),
                stdValueInstantiator.getClass().getName());
        assertEquals(stdValueInstantiator._arrayDelegateCreator.getDeclaringClass().getName(),
                stdValueInstantiator.getClass().getName());

        Object[] args = new Object[]{delegate};
        assertEquals(stdValueInstantiator._arrayDelegateCreator.call(args)[0],
                stdValueInstantiator.createUsingArrayDelegate(context, delegate));
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateUsingArrayDelegateThrowsException() throws Exception {
        Object delegate = mock(Object[].class);
        when(stdValueInstantiator._arrayDelegateCreator).thenReturn(mock(AnnotatedWithParams.class));
        when(stdValueInstantiator._arrayDelegateCreator.call(Mockito.anyObject())).thenThrow(IOException.class);

        stdValueInstantiator.createUsingArrayDelegate(context, delegate);
    }
}