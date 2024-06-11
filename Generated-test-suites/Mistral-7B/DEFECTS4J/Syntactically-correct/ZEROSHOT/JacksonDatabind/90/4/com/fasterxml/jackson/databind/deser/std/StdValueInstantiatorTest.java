package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Test class for {@link StdValueInstantiator}.
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    private static final String VALUE_TYPE_DESC = "java.lang.String";
    private static final Class<?> VALUE_CLASS = String.class;

    private static final StdValueInstantiator createStdValueInstantiator() {
        return new StdValueInstantiator(null, VALUE_TYPE_DESC, VALUE_CLASS);
    }

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.Test
    public void testGetValueTypeDesc() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertEquals(VALUE_TYPE_DESC, instantiator.getValueTypeDesc());
    }

    @org.junit.Test
    public void testGetValueClass() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertEquals(VALUE_CLASS, instantiator.getValueClass());
    }

    @org.junit.Test
    public void testCanCreateFromString() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromString());
    }

    @org.junit.Test
    public void testCanCreateFromInt() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromInt());
    }

    @org.junit.Test
    public void testCanCreateFromLong() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromLong());
    }

    @org.junit.Test
    public void testCanCreateFromDouble() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromDouble());
    }

    @org.junit.Test
    public void testCanCreateFromBoolean() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromBoolean());
    }

    @org.junit.Test
    public void testCanCreateUsingDefault() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateUsingDefault());
    }

    @org.junit.Test
    public void testCanCreateUsingDelegate() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateUsingDelegate());
    }

    @org.junit.Test
    public void testCanCreateUsingArrayDelegate() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateUsingArrayDelegate());
    }

    @org.junit.Test
    public void testCanCreateFromObjectWith() {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertFalse(instantiator.canCreateFromObjectWith());
    }

    @org.junit.Test
    public void testCreateUsingDefault() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertNull(instantiator.createUsingDefault(null));
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateFromObjectWithNullArgs() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        instantiator.createFromObjectWith(null, new Object[0]);
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateFromObjectWithNullCreator() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        instantiator.createFromObjectWith(null, new Object[0]);
    }

    @org.junit.Test
    public void testCreateFromObjectWith() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        Object[] args = new Object[]{new Object()};
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedClass(Object.class), new AnnotatedField[]{new AnnotatedField()}, null);
        instantiator.configureFromObjectSettings(creator, null, null, null, null, args);
        Object result = instantiator.createFromObjectWith(null, args);
        assertNotNull(result);
    }

    @org.junit.Test
    public void testCreateUsingDelegate() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertNull(instantiator.createUsingDelegate(null, null));
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateUsingArrayDelegateNullDelegate() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        instantiator.configureFromArraySettings(null, null, null);
        instantiator.createUsingArrayDelegate(null, null);
    }

    @org.junit.Test
    public void testCreateUsingArrayDelegate() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        JavaType arrayDelegateType = new SimpleType("int[]").withoutPropertyBinding();
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(new AnnotatedClass(int[].class), null, null);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[]{new SettableBeanProperty()};
        instantiator.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);
        Object[] delegateArgs = new Object[]{new int[0]};
        Object result = instantiator.createUsingArrayDelegate(null, new Object[]{new Object()});
        assertNotNull(result);
    }

    @org.junit.Test
    public void testCreateFromString() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertNull(instantiator.createFromString(null, null));
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateFromStringNullValues() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        instantiator.configureFromStringCreator(null);
        instantiator.createFromString(null, null);
    }

    @org.junit.Test
    public void testCreateFromString() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedClass(String.class), null, null);
        instantiator.configureFromStringCreator(creator);
        String value = "test";
        Object result = instantiator.createFromString(null, value);
        assertNotNull(result);
        assertEquals(value, result);
    }

    @org.junit.Test
    public void testCreateFromInt() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertNull(instantiator.createFromInt(null, 0));
    }

    @org.junit.Test
    public void testCreateFromInt() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedClass(String.class), null, null);
        instantiator.configureFromIntCreator(creator);
        int value = 42;
        Object result = instantiator.createFromInt(null, value);
        assertNotNull(result);
        assertEquals(Integer.valueOf(value), result);
    }

    @org.junit.Test
    public void testCreateFromLong() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertNull(instantiator.createFromLong(null, 0L));
    }

    @org.junit.Test
    public void testCreateFromLong() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedClass(String.class), null, null);
        instantiator.configureFromLongCreator(creator);
        long value = 42L;
        Object result = instantiator.createFromLong(null, value);
        assertNotNull(result);
        assertEquals(Long.valueOf(value), result);
    }

    @org.junit.Test
    public void testCreateFromDouble() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertNull(instantiator.createFromDouble(null, 0.0));
    }

    @org.junit.Test
    public void testCreateFromDouble() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedClass(String.class), null, null);
        instantiator.configureFromDoubleCreator(creator);
        double value = 42.0;
        Object result = instantiator.createFromDouble(null, value);
        assertNotNull(result);
        assertEquals(Double.valueOf(value), result);
    }

    @org.junit.Test
    public void testCreateFromBoolean() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        assertNull(instantiator.createFromBoolean(null, false));
    }

    @org.junit.Test
    public void testCreateFromBoolean() throws IOException {
        StdValueInstantiator instantiator = createStdValueInstantiator();
        AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedClass(String.class), null, null);
        instantiator.configureFromBooleanCreator(creator);
        boolean value = true;
        Object result = instantiator.createFromBoolean(null, value);
        assertNotNull(result);
        assertEquals(Boolean.valueOf(value), result);
    }
}