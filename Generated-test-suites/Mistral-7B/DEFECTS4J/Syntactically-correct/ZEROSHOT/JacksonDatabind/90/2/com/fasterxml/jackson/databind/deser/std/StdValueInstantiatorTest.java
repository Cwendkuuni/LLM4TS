package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test case for {@link StdValueInstantiator} class
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    private ObjectMapper mapper;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        valueInstantiator = mapper.getDeserializationConfig().findValueInstantiator(String.class);
    }

    @Test
    public void testDefaultCreation() throws Exception {
        assertEquals("UNKNOWN TYPE", valueInstantiator.getValueTypeDesc());
        assertNull(valueInstantiator.getDefaultCreator());
    }

    @Test
    public void testStringCreation() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), stringType);
        instantiator.configureFromStringCreator(mapper.getAnnotationIntrospector().findCreator(
                AnnotatedClass.construct(String.class), 0));
        assertEquals(String.class, instantiator.getValueClass());
        AnnotatedWithParams creator = instantiator.getFromStringCreator();
        assertNotNull(creator);
        assertEquals(String.class, creator.getDeclaringClass().getName());
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        String value = "test";
        ObjectNode node = mapper.createObjectNode();
        node.put("value", value);
        valueInstantiator.configureFromObjectSettings(
                mapper.getAnnotationIntrospector().findConstructor(String.class),
                null, null, null, null, null);

        Object result = valueInstantiator.createUsingDefault(mapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        JavaType delegateType = mapper.getTypeFactory().constructType(MyDelegate.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), delegateType);
        instantiator.configureFromDelegateSettings(
                mapper.getAnnotationIntrospector().findConstructor(MyDelegate.class),
                null, null, null);

        Object delegate = mapper.readValue(mapper.createObjectNode().toString(), MyDelegate.class);
        Object result = valueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
        assertEquals(delegate.getValue(), result);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        JavaType arrayType = mapper.getTypeFactory().constructType(int[].class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), arrayType);
        instantiator.configureFromArraySettings(
                mapper.getAnnotationIntrospector().findConstructor(int[].class),
                arrayType, null);

        int[] result = (int[]) valueInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new int[0]);
        assertArrayEquals(new int[0], result);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        valueInstantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Integer.TYPE);
        instantiator.configureFromIntCreator(mapper.getAnnotationIntrospector().findMethod(Integer.class, "valueOf", int.class));

        int value = 1;
        Object result = valueInstantiator.createFromInt(mapper.getDeserializationContext(), value);
        assertEquals(Integer.valueOf(value), result);
    }

    @Test
    public void testCreateFromLong() throws Exception {
        valueInstantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Long.TYPE);
        instantiator.configureFromLongCreator(mapper.getAnnotationIntrospector().findMethod(Long.class, "valueOf", long.class));

        long value = 1L;
        Object result = valueInstantiator.createFromLong(mapper.getDeserializationContext(), value);
        assertEquals(Long.valueOf(value), result);
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        valueInstantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Double.TYPE);
        instantiator.configureFromDoubleCreator(mapper.getAnnotationIntrospector().findMethod(Double.class, "valueOf", double.class));

        double value = 1.0;
        Object result = valueInstantiator.createFromDouble(mapper.getDeserializationContext(), value);
        assertEquals(Double.valueOf(value), result);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        valueInstantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Boolean.TYPE);
        instantiator.configureFromBooleanCreator(mapper.getAnnotationIntrospector().findMethod(Boolean.class, "valueOf", boolean.class));

        boolean value = true;
        Object result = valueInstantiator.createFromBoolean(mapper.getDeserializationContext(), value);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), stringType);
        instantiator.configureFromObjectSettings(
                mapper.getAnnotationIntrospector().findConstructor(String.class, AnnotatedParameter.std("value")),
                null, null, null, mapper.getAnnotationIntrospector().findParameter(String.class, "value", 0), null);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", "test");
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]);
        assertEquals("test", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidDefaultCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), String.class);
        instantiator.configureFromObjectSettings(mapper.getAnnotationIntrospector().findConstructor(Integer.class), null, null, null, null, null);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidWithArgsCreator() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), stringType);
        instantiator.configureFromObjectSettings(
                null, null, null, null, mapper.getAnnotationIntrospector().findConstructor(String.class), null);
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidDelegateCreator() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), stringType);
        instantiator.configureFromDelegateSettings(
                null, null, null, null);
        instantiator.createUsingDelegate(mapper.getDeserializationContext(), new Object[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidArrayDelegateCreator() throws Exception {
        JavaType intType = mapper.getTypeFactory().constructType(int[].class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), intType);
        instantiator.configureFromArraySettings(
                null, null, null);
        instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new Object[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidFromStringCreator() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), stringType);
        instantiator.configureFromStringCreator(null);
        instantiator.createFromString(mapper.getDeserializationContext(), "test");
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidFromIntCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Integer.TYPE);
        instantiator.configureFromIntCreator(null);
        instantiator.createFromInt(mapper.getDeserializationContext(), 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidFromLongCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Long.TYPE);
        instantiator.configureFromLongCreator(null);
        instantiator.createFromLong(mapper.getDeserializationContext(), 1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidFromDoubleCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Double.TYPE);
        instantiator.configureFromDoubleCreator(null);
        instantiator.createFromDouble(mapper.getDeserializationContext(), 1.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidFromBooleanCreator() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Boolean.TYPE);
        instantiator.configureFromBooleanCreator(null);
        instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithIncompleteParameter() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), stringType);
        instantiator.configureIncompleteParameter(mapper.getAnnotationIntrospector().findParameter(String.class, "value", 0));
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    static class MyDelegate {
        private final int value;

        @JsonCreator
        public MyDelegate(@JsonProperty("value") int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}