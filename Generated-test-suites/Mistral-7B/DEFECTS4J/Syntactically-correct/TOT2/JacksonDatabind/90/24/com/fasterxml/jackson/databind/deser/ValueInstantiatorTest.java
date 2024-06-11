package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.fail;

public class ValueInstantiatorTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() throws IOException {
        mapper = new ObjectMapper();
    }

    @Test
    public void testValueInstantiatorDefault() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        Object result = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertEquals("", result);
    }

    @Test
    public void testValueInstantiatorFromString() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        Object result = instantiator.createFromString(mapper.getDeserializationContext(), "Test");
        assertEquals("Test", result);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromInvalidString() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        instantiator.createFromString(mapper.getDeserializationContext(), "invalid");
        fail("Expected IOException to be thrown");
    }

    @Test
    public void testValueInstantiatorFromInt() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
        Object result = instantiator.createFromInt(mapper.getDeserializationContext(), 42);
        assertEquals(42, result);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromNegativeInt() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
        instantiator.createFromInt(mapper.getDeserializationContext(), -1);
        fail("Expected IOException to be thrown");
    }

    @Test
    public void testValueInstantiatorFromBoolean() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
        Object result = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertEquals(true, result);
    }

    @Test(expected = IOException.class)
    public void testValueInstantiatorFromInvalidBoolean() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
        instantiator.createFromBoolean(mapper.getDeserializationContext(), "invalid");
        fail("Expected IOException to be thrown");
    }

    @Test
    public void testValueInstantiatorCustom() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(CustomValue.class);
        Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), new CustomDelegate());
        CustomValue expected = new CustomValue(1, 2.0f, "Test");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testValueInstantiatorArrayDelegate() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(ArrayValue.class);
        Object[] arguments = {new Integer(1), new Double(2.0)};
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), arguments);
        ArrayValue expected = new ArrayValue(1, 2.0);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testValueInstantiatorNoCreator() throws IOException {
        Object result = mapper.findValueInstantiator(NoCreatorValue.class).createUsingDefault(mapper.getDeserializationContext());
        assertEquals(NoCreatorValue.class, result.getClass());
    }

    @Test(expected = JsonProcessingException.class)
    public void testValueInstantiatorMissingCreator() throws IOException {
        Object result = mapper.findValueInstantiator(MissingCreatorValue.class).createUsingDefault(mapper.getDeserializationContext());
        fail("Expected JsonProcessingException to be thrown");
    }

    @Test
    public void testValueInstantiatorAnnotatedCreator() throws IOException {
        ValueInstantiator instantiator = mapper.findValueInstantiator(AnnotatedValue.class);
        Object[] arguments = {new Integer(1), new Double(2.0)};
        AnnotatedMethod creator = findCreator(mapper, AnnotatedValue.class);
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), arguments, creator);
        AnnotatedValue expected = new AnnotatedValue(1, 2.0);
        assertEquals(expected.toString(), result.toString());
    }

    private AnnotatedMethod findCreator(ObjectMapper mapper, Class<?> valueClass) throws IOException {
        ObjectReader reader = mapper.readerForClass(valueClass);
        AnnotatedClass annotatedClass = reader.getAnnotationIntrospector().findAnnotated(reader.getRawValue(), valueClass);
        return annotatedClass.findAnnotatedMethod("createFromObjectWith", AnnotatedParameter.class, AnnotatedParameter.class);
    }

    static class CustomValue {
        private final int intValue;
        private final float floatValue;
        private final String stringValue;

        public CustomValue(int intValue, float floatValue, String stringValue) {
            this.intValue = intValue;
            this.floatValue = floatValue;
            this.stringValue = stringValue;
        }

        @JsonCreator
        public static CustomValue create(@JsonProperty("i") int i,
                                         @JsonProperty("f") float f,
                                         @JsonProperty("s") String s) {
            return new CustomValue(i, f, s);
        }
    }

    static class CustomDelegate {
        @JsonCreator
        public CustomValue create(@JsonProperty("i") int i,
                                 @JsonProperty("f") float f,
                                 @JsonProperty("s") String s) {
            return new CustomValue(i, f, s);
        }
    }

    static class ArrayValue {
        private final int intValue;
        private final double doubleValue;

        public ArrayValue(int intValue, double doubleValue) {
            this.intValue = intValue;
            this.doubleValue = doubleValue;
        }
    }

    static class NoCreatorValue {
    }

    static class MissingCreatorValue {
        @JsonCreator
        public static MissingCreatorValue create(@JsonProperty("i") int i) {
            throw new IllegalStateException("Missing creator");
        }
    }

    static class AnnotatedValue {
        private final int intValue;
        private final double doubleValue;

        public AnnotatedValue(int intValue, double doubleValue) {
            this.intValue = intValue;
            this.doubleValue = doubleValue;
        }

        @JsonCreator
        public static AnnotatedValue create(@JsonProperty("i") int i,
                                           @JsonProperty("d") double d) {
            return new AnnotatedValue(i, d);
        }
    }
}