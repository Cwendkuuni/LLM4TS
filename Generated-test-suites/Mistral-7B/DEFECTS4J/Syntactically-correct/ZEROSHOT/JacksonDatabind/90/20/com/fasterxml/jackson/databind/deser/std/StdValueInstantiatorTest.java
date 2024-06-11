package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for StdValueInstantiator class
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    private static final Object NULL_VALUE = null;
    private StdValueInstantiator instance;

    @Before
    public void setUp() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType valueType = ObjectTypeFactory.getDefaultInstance().constructType(String.class);
        instance = new StdValueInstantiator(config, valueType);
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(instance);
        assertEquals("java.lang.String", instance.getValueTypeDesc());
        assertNull(instance._defaultCreator);
        assertNull(instance._constructorArguments);
    }

    @Test
    public void testWithArgsConstructor() {
        DeserializationConfig config = new DeserializationConfig();
        AnnotatedWithParams withArgsCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[]{mock(SettableBeanProperty.class)};

        instance.configureFromObjectSettings(null, null, null, null, withArgsCreator, constructorArguments);

        assertNotNull(instance._withArgsCreator);
        assertEquals(withArgsCreator, instance._withArgsCreator);
        assertArrayEquals(constructorArguments, instance._constructorArguments);
    }

    @Test
    public void testDelegateConstructor() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType delegateType = ObjectTypeFactory.getDefaultInstance().constructType(Integer.class);
        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{mock(SettableBeanProperty.class)};

        instance.configureFromObjectSettings(null, delegateCreator, delegateType, delegateArguments, null, null);

        assertNotNull(instance._delegateType);
        assertEquals(delegateType, instance._delegateType);
        assertNotNull(instance._delegateCreator);
        assertEquals(delegateCreator, instance._delegateCreator);
        assertArrayEquals(delegateArguments, instance._delegateArguments);
    }

    @Test
    public void testGetValueClass() {
        assertEquals("java.lang.String", instance.getValueClass().getRawClass().getName());
    }

    @Test
    public void testCanCreateFromString() {
        assertFalse(instance.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        assertFalse(instance.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        assertFalse(instance.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        assertFalse(instance.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        assertFalse(instance.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        assertFalse(instance.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        assertFalse(instance.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        assertFalse(instance.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        assertNull(instance.createUsingDefault(null));
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        assertNull(instance.createFromObjectWith(null, new Object[0]));
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        Object delegate = new Object(){};
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{mock(SettableBeanProperty.class)};

        when(method.getParameter(0)).thenReturn(mock(AnnotatedParameter.class));

        instance._delegateCreator = mock(AnnotatedWithParams.class);
        instance._delegateArguments = delegateArguments;

        Object result = instance.createUsingDelegate(null, delegate, method);

        verify(instance._delegateCreator).call(delegateArguments);

        assertEquals(delegate, result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        Object delegate = new Object(){};
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[]{mock(SettableBeanProperty.class)};

        when(method.getParameter(0)).thenReturn(mock(AnnotatedParameter.class));

        instance._arrayDelegateCreator = mock(AnnotatedWithParams.class);
        instance._arrayDelegateArguments = arrayDelegateArguments;

        Object result = instance.createUsingArrayDelegate(null, delegate, method);

        verify(instance._arrayDelegateCreator).call(arrayDelegateArguments);

        assertEquals(delegate, result);
    }

    @Test
    public void testCreateFromString() throws IOException, JsonProcessingException {
        String value = "TestString";
        AnnotatedWithParams fromStringCreator = mock(AnnotatedWithParams.class);

        instance._fromStringCreator = fromStringCreator;

        Object result = instance.createFromString(null, value);

        verify(fromStringCreator).call1(value);

        assertEquals(value, result);
    }

    @Test
    public void testCreateFromInt() throws IOException, JsonProcessingException {
        int value = 42;
        AnnotatedWithParams fromIntCreator = mock(AnnotatedWithParams.class);

        instance._fromIntCreator = fromIntCreator;

        Object result = instance.createFromInt(null, value);

        verify(fromIntCreator).call1(Integer.valueOf(value));

        assertEquals(Integer.valueOf(value), result);
    }

    @Test
    public void testCreateFromLong() throws IOException, JsonProcessingException {
        long value = 42L;
        AnnotatedWithParams fromLongCreator = mock(AnnotatedWithParams.class);

        instance._fromLongCreator = fromLongCreator;

        Object result = instance.createFromLong(null, value);

        verify(fromLongCreator).call1(Long.valueOf(value));

        assertEquals(Long.valueOf(value), result);
    }

    @Test
    public void testCreateFromDouble() throws IOException, JsonProcessingException {
        double value = 42.0d;
        AnnotatedWithParams fromDoubleCreator = mock(AnnotatedWithParams.class);

        instance._fromDoubleCreator = fromDoubleCreator;

        Object result = instance.createFromDouble(null, value);

        verify(fromDoubleCreator).call1(Double.valueOf(value));

        assertEquals(Double.valueOf(value), result);
    }

    @Test
    public void testCreateFromBoolean() throws IOException, JsonProcessingException {
        boolean value = true;
        AnnotatedWithParams fromBooleanCreator = mock(AnnotatedWithParams.class);

        instance._fromBooleanCreator = fromBooleanCreator;

        Object result = instance.createFromBoolean(null, value);

        verify(fromBooleanCreator).call1(Boolean.valueOf(value));

        assertEquals(Boolean.valueOf(value), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromInvalidType() throws IOException {
        instance = new StdValueInstantiator(null, ObjectTypeFactory.getDefaultInstance().constructType(Integer.class));
        instance.createUsingDefault(null);
    }

    @Test
    public void testWrapException() {
        Throwable exception = new RuntimeException("TestException");
        JsonMappingException result = instance.unwrapAndWrapException(null, exception);

        assertEquals("TestException", result.getMessage());
        assertNotNull(result.getCause());
        assertEquals("TestException", result.getCause().getMessage());
    }

    @Test
    public void testWrapAsJsonMappingException() {
        Throwable exception = new RuntimeException("TestException");
        JsonMappingException result = instance.wrapAsJsonMappingException(null, exception);

        assertEquals("TestException", result.getMessage());
        assertNull(result.getCause());
    }

    @Test
    public void testCreateUsingDelegateWithInjectables() throws IOException {
        DeserializationConfig config = new DeserializationConfig();
        Object delegate = new Object(){};
        JavaType delegateType = ObjectTypeFactory.getDefaultInstance().constructType(Integer.class);
        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{mock(SettableBeanProperty.class), mock(SettableBeanProperty.class)};
        Object injectable1 = new Object(){};
        Object injectable2 = new Object(){};

        when(delegateCreator.call(delegateArguments)).thenReturn(delegate);

        instance.configureFromObjectSettings(null, null, null, null, null, null);
        instance.configureFromObjectSettings(delegateType, delegateCreator, null, null, null, null);

        Object result = instance.createUsingDelegate(config, delegate);

        assertEquals(delegate, result);

        verify(delegateCreator).call(delegateArguments);

        SettableBeanProperty injectableProperty1 = delegateArguments[0];
        SettableBeanProperty injectableProperty2 = delegateArguments[1];

        Object injectedValue1 = config.findInjectableValue(injectableProperty1.getInjectableValueId(), injectableProperty1, null);
        Object injectedValue2 = config.findInjectableValue(injectableProperty2.getInjectableValueId(), injectableProperty2, null);

        assertNotNull(injectedValue1);
        assertNotNull(injectedValue2);

        assertEquals(injectedValue1, result);
        assertEquals(injectedValue2, result);
    }
}