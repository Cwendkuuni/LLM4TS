package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdValueInstantiatorTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, String.class);
        assertNotNull(instantiator.getDefaultCreator());
        assertEquals(String.class, instantiator.getValueClass());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());

        String value = instantiator.createUsingDefault(objectMapper.getDeserializationContext());
        assertNotNull(value);
        assertEquals("", value);
    }

    @Test
    public void testWithArgsConstructor() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(ComplexType.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, javaType);
        assertNotNull(instantiator.getWithArgsCreator());
        assertEquals(ComplexType.class, instantiator.getValueClass());
        assertEquals("com.example.ComplexType", instantiator.getValueTypeDesc());

        ComplexType value = instantiator.createFromObjectWith(objectMapper.getDeserializationContext(), new Object[]{1, "Hello"});
        assertNotNull(value);
        assertEquals(1, value.getNumber());
        assertEquals("Hello", value.getString());
    }

    @Test
    public void testDelegateConstructor() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(ComplexType2.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, javaType);
        assertNotNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getDelegateType());
        assertEquals(ComplexType2.class, instantiator.getValueClass());
        assertEquals("com.example.ComplexType2", instantiator.getValueTypeDesc());

        ComplexType2 value = instantiator.createUsingDelegate(objectMapper.getDeserializationContext(), new ComplexType2());
        assertNotNull(value);
        assertEquals(1, value.getNumber());
        assertEquals("World", value.getString());
    }

    @Test
    public void testDelegateTypeConstructor() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(ComplexType2.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, javaType);
        instantiator.configureFromArraySettings(null, null, null, null);
        assertNull(instantiator.getDelegateCreator());
        assertNotNull(instantiator.getDelegateType());
        assertEquals(ComplexType2.class, instantiator.getValueClass());
        assertEquals("com.example.ComplexType2", instantiator.getValueTypeDesc());

        ComplexType2 value = instantiator.createUsingArrayDelegate(objectMapper.getDeserializationContext(), new ComplexType2());
        assertNotNull(value);
        assertEquals(2, value.getNumber());
        assertEquals("Hello", value.getString());
    }

    @Test
    public void testCreateFromString() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, String.class);
        assertNotNull(instantiator.getFromStringCreator());
        assertEquals(String.class, instantiator.getValueClass());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());

        String value = instantiator.createFromString(objectMapper.getDeserializationContext(), "Test");
        assertNotNull(value);
        assertEquals("Test", value);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, Integer.TYPE);
        assertNull(instantiator.getFromIntCreator());
        assertEquals(Integer.class, instantiator.getValueClass());
        assertEquals("int", instantiator.getValueTypeDesc());

        Integer value = instantiator.createFromInt(objectMapper.getDeserializationContext(), 42);
        assertNotNull(value);
        assertEquals(42, value.intValue());
    }

    @Test
    public void testCreateFromLong() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, Long.TYPE);
        assertNull(instantiator.getFromLongCreator());
        assertEquals(Long.class, instantiator.getValueClass());
        assertEquals("long", instantiator.getValueTypeDesc());

        Long value = instantiator.createFromLong(objectMapper.getDeserializationContext(), 42L);
        assertNotNull(value);
        assertEquals(42, value.longValue());
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, Double.TYPE);
        assertNull(instantiator.getFromDoubleCreator());
        assertEquals(Double.class, instantiator.getValueClass());
        assertEquals("double", instantiator.getValueTypeDesc());

        Double value = instantiator.createFromDouble(objectMapper.getDeserializationContext(), 42.0);
        assertNotNull(value);
        assertEquals(42.0, value.doubleValue(), 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, Boolean.TYPE);
        assertNull(instantiator.getFromBooleanCreator());
        assertEquals(Boolean.class, instantiator.getValueClass());
        assertEquals("boolean", instantiator.getValueTypeDesc());

        Boolean value = instantiator.createFromBoolean(objectMapper.getDeserializationContext(), true);
        assertNotNull(value);
        assertEquals(true, value.booleanValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidConstructor() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, ComplexType.class);
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);

        Object value = instantiator.createUsingDefault(objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingCreator() throws IOException {
        StdValueInstantiator instantiator = new StdValueInstantiator(objectMapper, ComplexType2.class);
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        instantiator.configureFromStringCreator(null);

        Object value = instantiator.createFromString(objectMapper.getDeserializationContext(), "Test");
    }

    private static class ComplexType {
        private int number;
        private String string;

        public ComplexType() {}

        public ComplexType(int number, String string) {
            this.number = number;
            this.string = string;
        }

        @JsonDeserialize(using = StdValueInstantiator.class)
        public static ComplexType fromValues(int number, String string) {
            return new ComplexType(number, string);
        }
    }

    private static class ComplexType2 {
        private int number;
        private String string;

        public ComplexType2() {}

        public ComplexType2(int number, String string) {
            this.number = number;
            this.string = string;
        }

        @JsonDeserialize(using = StdValueInstantiator.class)
        public static ComplexType2 fromValues(int number, String string) {
            return new ComplexType2(number, string);
        }
    }
}