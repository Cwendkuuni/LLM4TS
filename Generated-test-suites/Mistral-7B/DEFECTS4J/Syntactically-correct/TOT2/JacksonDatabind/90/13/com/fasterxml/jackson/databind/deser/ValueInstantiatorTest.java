package com.example.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ValueInstantiatorTest {

    @Test
    public void testDefaultCreator() throws IOException {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
        Object instance = valueInstantiator.createUsingDefault(new TestDeserializationContext());
        assertNotNull(instance);
        assertEquals("", instance.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingCreator() throws IOException {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(int.class);
        Object instance = valueInstantiator.createUsingDefault(new TestDeserializationContext());
    }

    @Test
    public void testCanCreateFromString() throws IOException {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(StringTrimmerValueInstantiator.class);
        Object instance = valueInstantiator.createFromString(new TestDeserializationContext(), "Hello");
        assertEquals("Hello", instance.toString());
    }

    @Test
    public void testCanCreateFromInt() throws IOException {
        ValueInstantiator valueInstantiator = new ValueIntWrapperValueInstantiator.IntCreatorValueInstantiator();
        Object instance = valueInstantiator.createFromInt(new TestDeserializationContext(), 42);
        assertEquals(42, instance.intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotCreateFromInt() throws IOException {
        ValueInstantiator valueInstantiator = new ValueIntWrapperValueInstantiator.IntCreatorValueInstantiator();
        valueInstantiator.createFromInt(new TestDeserializationContext(), "not a number");
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ValueInstantiator valueInstantiator = new NestedObjectValueInstantiator();
        Object arg1 = new Argument1();
        Object arg2 = new Argument2();
        Object instance = valueInstantiator.createFromObjectWith(new TestDeserializationContext(), new Object[] { arg1, arg2 });
        assertEquals(arg1, instance.getArg1());
        assertEquals(arg2, instance.getArg2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingObjectCreatorArguments() throws IOException {
        ValueInstantiator valueInstantiator = new NestedObjectValueInstantiator();
        Object instance = valueInstantiator.createFromObjectWith(new TestDeserializationContext(), new Object[0]);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        ValueInstantiator valueInstantiator = new DelegatingValueInstantiator();
        Object delegate = new DelegateClass();
        Object instance = valueInstantiator.createUsingDelegate(new TestDeserializationContext(), delegate);
        assertNotNull(instance);
        assertEquals(delegate, instance);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        ValueInstantiator valueInstantiator = new ArrayDelegateValueInstantiator();
        Object[] args = new Object[] { new Argument1(), new Argument2() };
        Object instance = valueInstantiator.createUsingArrayDelegate(new TestDeserializationContext(), args);
        assertNotNull(instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingArrayCreatorArguments() throws IOException {
        ValueInstantiator valueInstantiator = new ArrayDelegateValueInstantiator();
        Object instance = valueInstantiator.createUsingArrayDelegate(new TestDeserializationContext(), new Object[0]);
    }

    private static class TestDeserializationContext extends DeserializationContext {
    }

    private static class Argument1 {}

    private static class Argument2 {}

    private static class DelegateClass {}

    private static class NestedObjectValueInstantiator extends ValueInstantiator {
        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return new NestedObject(args[0], args[1]);
        }

        private static class NestedObject {
            private final Argument1 arg1;
            private final Argument2 arg2;

            public NestedObject(Argument1 arg1, Argument2 arg2) {
                this.arg1 = arg1;
                this.arg2 = arg2;
            }

            public Argument1 getArg1() {
                return arg1;
            }

            public Argument2 getArg2() {
                return arg2;
            }
        }
    }

    private static class DelegatingValueInstantiator extends ValueInstantiator {
        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return new AnnotatedWithParams(DelegateCreator.class);
        }

        private static class DelegateCreator {
            @javax.annotation.Generated("ValueInstantiator")
            public Object create(Object delegate) {
                return delegate;
            }
        }
    }

    private static class StringTrimmerValueInstantiator extends ValueInstantiator {
        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return new StringTrimmer(value.trim());
        }

        private static class StringTrimmer {
            private final String value;

            public StringTrimmer(String value) {
                this.value = value;
            }

            public String toString() {
                return value;
            }
        }
    }

    private static class ValueIntWrapperValueInstantiator extends ValueInstantiator {
        public static class IntCreatorValueInstantiator extends ValueInstantiator {
            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return new IntWrapper(value);
            }

            private static class IntWrapper {
                private final int value;

                public IntWrapper(int value) {
                    this.value = value;
                }

                public int intValue() {
                    return value;
                }
            }
        }

        @Override
        public AnnotatedWithParams getWithArgsCreator() {
            return new AnnotatedWithParams(IntCreatorValueInstantiator.class);
        }
    }

    private static class ArrayDelegateValueInstantiator extends ValueInstantiator {
        @Override
        public AnnotatedWithParams getArrayDelegateCreator() {
            return new AnnotatedWithParams(ArrayDelegateCreator.class);
        }

        private static class ArrayDelegateCreator {
            @javax.annotation.Generated("ValueInstantiator")
            public Object create(Object delegate) {
                return delegate;
            }
        }
    }
}