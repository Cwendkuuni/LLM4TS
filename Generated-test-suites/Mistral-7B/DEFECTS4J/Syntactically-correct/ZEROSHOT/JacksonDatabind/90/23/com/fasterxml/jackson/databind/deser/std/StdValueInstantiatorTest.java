package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

/**
 * Test class for {@link StdValueInstantiator} class.
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    private static final Object[] DEFAULT_CONSTRUCTOR_ARGS = new Object[0];
    private static final Class<?> STRING_CLASS = String.class;
    private static final Class<?> INTEGER_CLASS = Integer.class;
    private static final Class<?> LONG_CLASS = Long.class;
    private static final Class<?> DOUBLE_CLASS = Double.class;
    private static final Class<?> BOOLEAN_CLASS = Boolean.class;

    private StdValueInstantiator createStdValueInstantiator(JavaType valueType) {
        DeserializationConfig config = new DeserializationConfig();
        return new StdValueInstantiator(config, valueType);
    }

    private void testCanCreateFromType(Class<?> valueType) throws Exception {
        StdValueInstantiator instantiator = createStdValueInstantiator(new ValueTypeReference(valueType));
        assertTrue(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromInt());
        assertTrue(instantiator.canCreateFromLong());
        assertTrue(instantiator.canCreateFromDouble());
        assertTrue(instantiator.canCreateFromBoolean());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateUsingArrayDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertEquals(valueType.getName(), instantiator.getValueTypeDesc());
        assertEquals(valueType.getRawClass(), instantiator.getValueClass());
    }

    private void testCreateFromString(StdValueInstantiator instantiator, String value) throws Exception {
        Object result = instantiator.createFromString(null, value);
        assertNotNull(result);
    }

    private void testCreateFromInt(StdValueInstantiator instantiator, int value) throws Exception {
        Object result = instantiator.createFromInt(null, value);
        assertNotNull(result);
    }

    private void testCreateFromLong(StdValueInstantiator instantiator, long value) throws Exception {
        Object result = instantiator.createFromLong(null, value);
        assertNotNull(result);
    }

    private void testCreateFromDouble(StdValueInstantiator instantiator, double value) throws Exception {
        Object result = instantiator.createFromDouble(null, value);
        assertNotNull(result);
    }

    private void testCreateFromBoolean(StdValueInstantiator instantiator, boolean value) throws Exception {
        Object result = instantiator.createFromBoolean(null, value);
        assertNotNull(result);
    }

    private void testCreateUsingDefault(StdValueInstantiator instantiator) throws Exception {
        Object result = instantiator.createUsingDefault(null);
        assertNotNull(result);
    }

    @Test
    public void testStringValueInstantiator() throws Exception {
        testCanCreateFromType(STRING_CLASS);
        StdValueInstantiator instantiator = createStdValueInstantiator(new ValueTypeReference(STRING_CLASS));
        Object result;

        result = instantiator.createFromString(null, "test");
        assertEquals("test", result);

        testCreateFromInt(instantiator, 1);
        testCreateFromLong(instantiator, 1L);
        testCreateFromDouble(instantiator, 1.0);
        testCreateFromBoolean(instantiator, true);
        testCreateUsingDefault(instantiator);

        // Test creation using default constructor
        instantiator = createStdValueInstantiator(null);
        result = instantiator.createUsingDefault(null);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testIntegerValueInstantiator() throws Exception {
        testCanCreateFromType(INTEGER_CLASS);
        StdValueInstantiator instantiator = createStdValueInstantiator(new ValueTypeReference(INTEGER_CLASS));
        Object result;

        testCreateFromString(instantiator, "1");
        testCreateFromInt(instantiator, 1);
        testCreateFromLong(instantiator, 1L);
        testCreateFromDouble(instantiator, 1.0);
        testCreateFromBoolean(instantiator, true);
        testCreateUsingDefault(instantiator);

        result = instantiator.createFromString(null, "123");
        assertEquals(123, result.intValue());

        result = instantiator.createFromInt(null, 456);
        assertEquals(456, result.intValue());

        result = instantiator.createFromLong(null, 789L);
        assertEquals(789, result.intValue());

        result = instantiator.createFromDouble(null, 123.456);
        assertEquals(123, result.intValue());

        result = instantiator.createFromBoolean(null, true);
        assertTrue(result instanceof Boolean);
        assertEquals(true, ((Boolean) result).booleanValue());

        result = instantiator.createUsingDefault(null);
        assertEquals(0, result.intValue());
    }

    @Test
    public void testLongValueInstantiator() throws Exception {
        testCanCreateFromType(LONG_CLASS);
        StdValueInstantiator instantiator = createStdValueInstantiator(new ValueTypeReference(LONG_CLASS));
        Object result;

        testCreateFromString(instantiator, "1");
        testCreateFromInt(instantiator, 1);
        testCreateFromLong(instantiator, 1L);
        testCreateFromDouble(instantiator, 1.0);
        testCreateFromBoolean(instantiator, true);
        testCreateUsingDefault(instantiator);

        result = instantiator.createFromString(null, "12345678901234567890");
        assertEquals(12345678901234567890L, result.longValue());

        result = instantiator.createFromInt(null, 45678901234567890L);
        assertEquals(45678901234567890L, result.longValue());

        result = instantiator.createFromLong(null, 78901234567890123456L);
        assertEquals(78901234567890123456L, result.longValue());

        result = instantiator.createFromDouble(null, 12345678901234567890.1234567890);
        assertEquals(12345678901234567890L, result.longValue());

        result = instantiator.createFromBoolean(null, true);
        assertTrue(result instanceof Boolean);
        assertEquals(true, ((Boolean) result).booleanValue());

        result = instantiator.createUsingDefault(null);
        assertEquals(0, result.longValue());
    }

    @Test
    public void testDoubleValueInstantiator() throws Exception {
        testCanCreateFromType(DOUBLE_CLASS);
        StdValueInstantiator instantiator = createStdValueInstantiator(new ValueTypeReference(DOUBLE_CLASS));
        Object result;

        testCreateFromString(instantiator, "1");
        testCreateFromInt(instantiator, 1);
        testCreateFromLong(instantiator, 1L);
        testCreateFromDouble(instantiator, 1.0);
        testCreateFromBoolean(instantiator, true);
        testCreateUsingDefault(instantiator);

        result = instantiator.createFromString(null, "1.23");
        assertEquals(1.23, result, 0.0);

        result = instantiator.createFromInt(null, 456);
        assertEquals(456.0, result, 0.0);

        result = instantiator.createFromLong(null, 789L);
        assertEquals(789.0, result, 0.0);

        result = instantiator.createFromDouble(null, 123.456);
        assertEquals(123.456, result, 0.0);

        result = instantiator.createFromBoolean(null, true);
        assertTrue(result instanceof Boolean);
        assertEquals(true, ((Boolean) result).booleanValue());

        result = instantiator.createUsingDefault(null);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testBooleanValueInstantiator() throws Exception {
        testCanCreateFromType(BOOLEAN_CLASS);
        StdValueInstantiator instantiator = createStdValueInstantiator(new ValueTypeReference(BOOLEAN_CLASS));
        Object result;

        testCreateFromString(instantiator, "true");
        testCreateFromInt(instantiator, 1);
        testCreateFromLong(instantiator, 1L);
        testCreateFromDouble(instantiator, 1.0);
        testCreateFromBoolean(instantiator, true);
        testCreateUsingDefault(instantiator);

        result = instantiator.createFromString(null, "false");
        assertEquals(false, result);

        result = instantiator.createFromInt(null, 0);
        assertEquals(false, result);

        result = instantiator.createFromLong(null, 0L);
        assertEquals(false, result);

        result = instantiator.createFromDouble(null, 0.0);
        assertEquals(false, result);

        result = instantiator.createFromBoolean(null, false);
        assertEquals(false, result);

        result = instantiator.createUsingDefault(null);
        assertEquals(false, result);
    }

    @Test
    public void testCustomValueInstantiator() throws Exception {
        class CustomValue {
            @JsonCreator
            public CustomValue(@JsonProperty("value") int i) {
                this.value = i;
            }

            private int value;

            public int getValue() {
                return value;
            }
        }

        JavaType customType = new ValueTypeReference(CustomValue.class);
        StdValueInstantiator instantiator = createStdValueInstantiator(customType);

        testCanCreateFromType(CustomValue.class);
        Object result = instantiator.createFromString(null, "123");
        assertEquals(123, result.getValue());

        result = instantiator.createFromInt(null, 456);
        assertEquals(456, result.getValue());

        result = instantiator.createFromLong(null, 789L);
        assertEquals(789, result.getValue());

        result = instantiator.createFromDouble(null, 123.456);
        assertEquals(123, result.getValue());

        result = instantiator.createFromBoolean(null, true);
        assertTrue(result instanceof Boolean);
        assertEquals(true, ((Boolean) result).booleanValue());

        result = instantiator.createUsingDefault(null);
        assertEquals(0, result.getValue());
    }
}