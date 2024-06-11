package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testDefaultInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueInstantiator(String.class);
        assertNotNull(instantiator);
        assertNull(instantiator.getValueClass());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertFalse(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromDouble());
        assertFalse(instantiator.canCreateFromBoolean());
        try {
            instantiator.createUsingDefault(null);
            fail("Expected InstantiationException");
        } catch (Exception e) {
            // expected
        }
        try {
            instantiator.createFromObjectWith(null, new Object[0]);
            fail("Expected InstantiationException");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testStdValueInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueInstantiator(Integer.class);
        assertTrue(instantiator instanceof StdValueInstantiator.Std);
        assertEquals(Integer.class, instantiator.getValueClass());
        assertEquals("java.lang.Integer", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromDouble());
        assertFalse(instantiator.canCreateFromBoolean());
        Object obj = instantiator.createUsingDefault(null);
        assertNotNull(obj);
        assertEquals(0, obj.hashCode());
        assertEquals(Integer.class, obj.getClass());
        try {
            instantiator.createFromObjectWith(null, new Object[0]);
            fail("Expected InstantiationException");
        } catch (Exception e) {
            // expected
        }
        instantiator = mapper.getTypeFactory().constructValueInstantiator(String[].class);
        assertTrue(instantiator instanceof StdValueInstantiator.Array);
        assertEquals(String[].class, instantiator.getValueClass());
        assertEquals("java.lang.String[]", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromDouble());
        assertFalse(instantiator.canCreateFromBoolean());
        Object arr = instantiator.createUsingDefault(null);
        assertNotNull(arr);
        assertEquals(0, arr.length);
        assertEquals(String[].class, arr.getClass());
    }

    @Test
    public void testCustomInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass ac = mapper.getTypeFactory().findCreatorAnnotation(MyCustomValueInstantiator.class);
        ValueInstantiator instantiator = mapper.getTypeFactory().constructValueInstantiator(MyCustomValue.class);
        assertTrue(instantiator instanceof MyCustomValueInstantiator);
        assertEquals(MyCustomValue.class, instantiator.getValueClass());
        assertEquals("com.example.MyCustomValue", instantiator.getValueTypeDesc());
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertTrue(instantiator.canCreateUsingDelegate());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertTrue(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromInt());
        assertTrue(instantiator.canCreateFromLong());
        assertTrue(instantiator.canCreateFromDouble());
        assertTrue(instantiator.canCreateFromBoolean());
        Object obj = instantiator.createUsingDefault(null);
        assertNotNull(obj);
        assertEquals("default", obj.s);
        AnnotatedMethod meth = ac.findMethod("fromString");
        Object strObj = instantiator.createFromString(null, "test");
        assertNotNull(strObj);
        assertEquals("test", strObj.s);
        Object intObj = instantiator.createFromInt(null, 42);
        assertNotNull(intObj);
        assertEquals(42, intObj.i);
        Object longObj = instantiator.createFromLong(null, 42L);
        assertNotNull(longObj);
        assertEquals(42, longObj.i);
        Object doubleObj = instantiator.createFromDouble(null, 42.0);
        assertNotNull(doubleObj);
        assertEquals(42, doubleObj.i);
        Object boolObj = instantiator.createFromBoolean(null, true);
        assertNotNull(boolObj);
        assertEquals(true, boolObj.b);
        Object[] args = new Object[]{"arg1", "arg2"};
        Object objFromArgs = instantiator.createFromObjectWith(null, args);
        assertNotNull(objFromArgs);
        assertEquals("arg1", objFromArgs.s1);
        assertEquals("arg2", objFromArgs.s2);
    }

    static class MyCustomValue {
        String s;
        int i;
        boolean b;

        @SuppressWarnings("unused")
        MyCustomValue() {
            // default constructor
        }

        MyCustomValue(String s) {
            this.s = s;
        }

        MyCustomValue(int i) {
            this.i = i;
        }

        MyCustomValue(boolean b) {
            this.b = b;
        }

        MyCustomValue(String s1, String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        static MyCustomValue fromString(String s) {
            return new MyCustomValue(s);
        }

        static MyCustomValue fromInt(int i) {
            return new MyCustomValue(i);
        }

        static MyCustomValue fromLong(long l) {
            return new MyCustomValue((int)l);
        }

        static MyCustomValue fromDouble(double d) {
            return new MyCustomValue((int)d);
        }

        static MyCustomValue fromBoolean(boolean b) {
            return new MyCustomValue(b);
        }

    }

    static class MyCustomValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return fromString(value);
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return fromInt(value);
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return fromLong((int)value);
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return fromDouble((int)value);
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return fromBoolean(value);
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[0];
        }

        @Override
        public JavaType getDelegateType(DeserializationConfig config) {
            return null;
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return null;
        }
    }

}