package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    private ObjectMapper mapper;
    private StdValueInstantiator stdValueInstantiator;
    private DeserializationContext context;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        context = Mockito.mock(DeserializationContext.class);
        MockitoAnnotations.initMocks(this);
        stdValueInstantiator = new StdValueInstantiator(mapper.getConfig(), null);
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals("UNKNOWN TYPE", stdValueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        assertEquals(Object.class, stdValueInstantiator.getValueClass());
    }

    @Test
    public void testCanCreateFromString() {
        assertFalse(stdValueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        assertFalse(stdValueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        assertFalse(stdValueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        assertFalse(stdValueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        assertFalse(stdValueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateUsingDefault() {
        assertFalse(stdValueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        assertFalse(stdValueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        assertFalse(stdValueInstantiator.canCreateUsingArrayDelegate());
    }

    @Test
    public void testCanCreateFromObjectWith() {
        assertFalse(stdValueInstantiator.canCreateFromObjectWith());
    }

    @Test
    public void testConfigureFromObjectSettings() {
        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams delegateCreator = Mockito.mock(AnnotatedWithParams.class);
        JavaType delegateType = Mockito.mock(JavaType.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[0];
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[0];
        stdValueInstantiator.configureFromObjectSettings(defaultCreator, delegateCreator, delegateType, delegateArguments, constructorArguments);
        assertEquals(defaultCreator, stdValueInstantiator.getDefaultCreator());
        assertEquals(delegateCreator, stdValueInstantiator.getDelegateCreator());
        assertEquals(delegateType, stdValueInstantiator.getDelegateType());
        assertArrayEquals(delegateArguments, stdValueInstantiator.getDelegateArguments());
        assertEquals(constructorArguments, stdValueInstantiator.getConstructorArguments());
    }

    @Test
    public void testConfigureFromArraySettings() {
        AnnotatedWithParams arrayDelegateCreator = Mockito.mock(AnnotatedWithParams.class);
        JavaType arrayDelegateType = Mockito.mock(JavaType.class);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[0];
        stdValueInstantiator.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);
        assertEquals(arrayDelegateCreator, stdValueInstantiator.getArrayDelegateCreator());
        assertEquals(arrayDelegateType, stdValueInstantiator.getArrayDelegateType());
        assertArrayEquals(arrayDelegateArguments, stdValueInstantiator.getArrayDelegateArguments());
    }

    @Test
    public void testConfigureFromStringCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        stdValueInstantiator.configureFromStringCreator(creator);
        assertEquals(creator, stdValueInstantiator.getFromStringCreator());
    }

    @Test
    public void testConfigureFromIntCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        stdValueInstantiator.configureFromIntCreator(creator);
        assertEquals(creator, stdValueInstantiator.getFromIntCreator());
    }

    @Test
    public void testConfigureFromLongCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        stdValueInstantiator.configureFromLongCreator(creator);
        assertEquals(creator, stdValueInstantiator.getFromLongCreator());
    }

    @Test
    public void testConfigureFromDoubleCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        stdValueInstantiator.configureFromDoubleCreator(creator);
        assertEquals(creator, stdValueInstantiator.getFromDoubleCreator());
    }

    @Test
    public void testConfigureFromBooleanCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        stdValueInstantiator.configureFromBooleanCreator(creator);
        assertEquals(creator, stdValueInstantiator.getFromBooleanCreator());
    }

    @Test
    public void testConfigureIncompleteParameter() {
        AnnotatedParameter parameter = Mockito.mock(AnnotatedParameter.class);
        stdValueInstantiator.configureIncompleteParameter(parameter);
        assertEquals(parameter, stdValueInstantiator.getIncompleteParameter());
    }

    @Test
    public void testCreateUsingDefault() throws JsonProcessingException {
        when(stdValueInstantiator.getDefaultCreator().call()).thenCallRealMethod();
        stdValueInstantiator.configureFromObjectSettings(null, null, null, null, null);
        Object result = stdValueInstantiator.createUsingDefault(context);
        Mockito.verify(stdValueInstantiator.getDefaultCreator()).call();
        assertNotNull(result);
    }

    @Test
    public void testCreateFromObjectWith() throws JsonProcessingException {
        Object[] args = new Object[0];
        when(stdValueInstantiator.getWithArgsCreator().call(args)).thenCallRealMethod();
        stdValueInstantiator.configureFromObjectSettings(null, null, null, null, stdValueInstantiator.getWithArgsCreator(), args);
        Object result = stdValueInstantiator.createFromObjectWith(context, args);
        Mockito.verify(stdValueInstantiator.getWithArgsCreator()).call(args);
        assertNotNull(result);
    }

    @Test
    public void testCreateUsingDelegate() throws JsonProcessingException {
        Object delegate = Mockito.mock(Object.class);
        AnnotatedWithParams delegateCreator = Mockito.mock(AnnotatedWithParams.class);
        JavaType delegateType = Mockito.mock(JavaType.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[0];
        when(delegateCreator.call(delegate)).thenCallRealMethod();
        stdValueInstantiator.configureFromDelegateSettings(delegateCreator, delegateType, delegateArguments);
        Object result = stdValueInstantiator.createUsingDelegate(context, delegate);
        Mockito.verify(delegateCreator).call(delegate);
        assertNotNull(result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws JsonProcessingException {
        Object delegate = Mockito.mock(Object[].class);
        AnnotatedWithParams arrayDelegateCreator = Mockito.mock(AnnotatedWithParams.class);
        JavaType arrayDelegateType = Mockito.mock(JavaType.class);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[0];
        when(arrayDelegateCreator.call(delegate)).thenCallRealMethod();
        stdValueInstantiator.configureFromArrayDelegateSettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);
        Object result = stdValueInstantiator.createUsingArrayDelegate(context, delegate);
        Mockito.verify(arrayDelegateCreator).call(delegate);
        assertNotNull(result);
    }

    @Test
    public void testCreateFromString() throws JsonProcessingException {
        String value = "test";
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        AnnotatedClass cls = Mockito.mock(AnnotatedClass.class);
        when(creator.getDeclaringClass()).thenReturn(cls);
        when(creator.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(creator.call1(value)).thenCallRealMethod();
        stdValueInstantiator.configureFromStringCreator(creator);
        Object result = stdValueInstantiator.createFromString(context, value);
        Mockito.verify(creator).call1(value);
        assertNotNull(result);
    }

    @Test
    public void testCreateFromInt() throws JsonProcessingException {
        int value = 1;
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        when(creator.call1(Integer.valueOf(value))).thenCallRealMethod();
        stdValueInstantiator.configureFromIntCreator(creator);
        Object result = stdValueInstantiator.createFromInt(context, value);
        Mockito.verify(creator).call1(Integer.valueOf(value));
        assertNotNull(result);
    }

    @Test
    public void testCreateFromLong() throws JsonProcessingException {
        long value = 1;
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        when(creator.call1(Long.valueOf(value))).thenCallRealMethod();
        stdValueInstantiator.configureFromLongCreator(creator);
        Object result = stdValueInstantiator.createFromLong(context, value);
        Mockito.verify(creator).call1(Long.valueOf(value));
        assertNotNull(result);
    }

    @Test
    public void testCreateFromDouble() throws JsonProcessingException {
        double value = 1.0;
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        when(creator.call1(Double.valueOf(value))).thenCallRealMethod();
        stdValueInstantiator.configureFromDoubleCreator(creator);
        Object result = stdValueInstantiator.createFromDouble(context, value);
        Mockito.verify(creator).call1(Double.valueOf(value));
        assertNotNull(result);
    }

    @Test
    public void testCreateFromBoolean() throws JsonProcessingException {
        boolean value = true;
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        when(creator.call1(Boolean.valueOf(value))).thenCallRealMethod();
        stdValueInstantiator.configureFromBooleanCreator(creator);
        Object result = stdValueInstantiator.createFromBoolean(context, value);
        Mockito.verify(creator).call1(Boolean.valueOf(value));
        assertNotNull(result);
    }
}