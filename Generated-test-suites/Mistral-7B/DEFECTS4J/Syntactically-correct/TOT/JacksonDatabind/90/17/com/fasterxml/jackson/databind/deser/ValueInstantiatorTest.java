package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.testng.annotations.*;

import java.io.IOException;
import static org.testng.Assert.*;

public class ValueInstantiatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefaultInstantiator() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        Object result = instantiator.createUsingDefault(MAPPER.getDeserializationContext().getDefaultContext());
        assertEquals(result, "");
    }

    @Test
    public void testStringInstantiator() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return "CustomString";
            }
        };
        Object result = instantiator.createFromString(MAPPER.getDeserializationContext().getDefaultContext(), "Test");
        assertEquals(result, "CustomString");
    }

    @Test
    public void testIntInstantiator() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class) {
            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value * 2;
            }
        };
        Object result = instantiator.createFromInt(MAPPER.getDeserializationContext().getDefaultContext(), 5);
        assertEquals(result, 10);
    }

    @Test
    public void testInvalidInstantiator() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public boolean canCreateUsingDefault() {
                return true;
            }

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                throw new RuntimeException("Test error");
            }
        };
        try {
            instantiator.createUsingDefault(MAPPER.getDeserializationContext().getDefaultContext());
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testPropertyBasedInstantiator() throws IOException {
        class MyValue {
            private String prop1;

            public MyValue(String prop1) {
                this.prop1 = prop1;
            }

            public String getProp1() {
                return prop1;
            }
        }

        class MyInstantiator extends ValueInstantiator {
            @Override
            public boolean canCreateFromObjectWith() {
                return true;
            }

            @Override
            public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
                MyValue result = new MyValue((String) args[0]);
                MapperConfig config = ctxt.getConfig();
                ObjectReader reader = config.getFactory().getObjectReaderForValueType(MyValue.class);
                return reader.readValue(ctxt.getParser(), new TypeReference<MyValue>() {});
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop1\": \"Test\"}";
        Object result = new MyInstantiator().createFromObjectWith(mapper.getDeserializationContext(), new Object[]{"Test"});
        assertEquals(result.getClass(), MyValue.class);
        assertEquals(((MyValue) result).getProp1(), "Test");
    }

    @Test
    public void testArrayDelegateInstantiator() throws IOException {
        class MyArrayDelegate {
            public Object[] createArray(int size) {
                return new Object[size];
            }
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(Object[].class) {
            @Override
            public boolean canCreateUsingArrayDelegate() {
                return true;
            }

            @Override
            public JavaType getArrayDelegateType(DeserializationConfig config) {
                return new ArrayType(config.getTypeFactory().constructType(MyArrayDelegate.class), 1);
            }

            @Override
            public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                MyArrayDelegate arrayDelegate = (MyArrayDelegate) delegate;
                Object[] args = arrayDelegate.createArray(5);
                for (int i = 0; i < args.length; i++) {
                    args[i] = "Test" + i;
                }
                return args;
            }
        };

        Object result = instantiator.createUsingArrayDelegate(MAPPER.getDeserializationContext(), new MyArrayDelegate());
        assertArrayEquals(new String[]{"Test0", "Test1", "Test2", "Test3", "Test4"}, (Object[]) result);
    }

    @Test
    public void testDelegateInstantiator() throws IOException {
        class MyDelegate {
            public Object createValue(DeserializationContext ctxt) throws IOException {
                return ctxt.handleMissingInstantiator(String.class, ctxt.getParser(), "Missing Delegate");
            }
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }

            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new AnnotatedWithParams(MyDelegate.class, new AnnotatedParameter[]{});
            }
        };

        Object result = instantiator.createUsingDelegate(MAPPER.getDeserializationContext(), new MyDelegate());
        assertEquals(result, "Missing Delegate");
    }

    @Test
    public void testInstantiateArray() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Object[].class) {
            @Override
            public boolean canCreateUsingDefault() {
                return true;
            }

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                Object[] result = new Object[3];
                result[0] = new Integer(1);
                result[1] = new String("Two");
                result[2] = new Boolean(true);
                return result;
            }
        };

        Object result = instantiator.createUsingDefault(MAPPER.getDeserializationContext());
        assertArrayEquals(new Object[]{new Integer(1), new String("Two"), new Boolean(true)}, result);
    }

    @Test
    public void testInstantiateCustomValue() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(CustomValue.class) {
            @Override
            public boolean canCreateUsingDefault() {
                return false;
            }

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                throw new RuntimeException("Should not call this method");
            }
        };

        Object result = instantiator.createUsingDefault(MAPPER.getDeserializationContext());
        fail("Should not reach this point");
    }

    static class CustomValue {
        private final String prop1;
        private final int prop2;

        public CustomValue(String prop1, int prop2) {
            this.prop1 = prop1;
            this.prop2 = prop2;
        }

        public String getProp1() {
            return prop1;
        }

        public int getProp2() {
            return prop2;
        }
    }
}