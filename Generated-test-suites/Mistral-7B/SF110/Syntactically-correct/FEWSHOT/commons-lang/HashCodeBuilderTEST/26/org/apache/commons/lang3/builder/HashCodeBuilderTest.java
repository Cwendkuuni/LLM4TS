package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(17, hcb.hashCode());

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(1, 37);
        assertEquals(1 * 37 + 17, hcbWithInitialValue.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        assertEquals(32, hcb.hashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new boolean[]{true, false, true});
        assertEquals(-1441582971, hcb.hashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 10);
        assertEquals(-316115813, hcb.hashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte[]) new byte[]{1, 2, 3});
        assertEquals(132157277, hcb.hashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        assertEquals(-2111814807, hcb.hashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((char[]) new char[]{'a', 'b', 'c'});
        assertEquals(1389812647, hcb.hashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0);
        assertEquals(-1477751855, hcb.hashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((double[]) new double[]{1.0, 2.0, 3.0});
        assertEquals(-1097958612, hcb.hashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0f);
        assertEquals(1763573211, hcb.hashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float[]) new float[]{1.0f, 2.0f, 3.0f});
        assertEquals(-2047463237, hcb.hashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(10);
        assertEquals(-1420413609, hcb.hashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((int[]) new int[]{1, 2, 3});
        assertEquals(-1311140283, hcb.hashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(10L);
        assertEquals(459321158753L, hcb.hashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((long[]) new long[]{1, 2, 3});
        assertEquals(-15168552125714453453L, hcb.hashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append("Hello World");
        assertEquals(767617524, hcb.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new Object[]{"Hello World 1", "Hello World 2", "Hello World 3"});
        assertEquals(1775567947, hcb.hashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 10);
        assertEquals(-2145508165, hcb.hashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short[]) new short[]{1, 2, 3});
        assertEquals(-1292755968, hcb.hashCode());
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(10);
        assertEquals(11677542891, hcb.hashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Object object = new Object(){};
        assertEquals(2103244033, HashCodeBuilder.reflectionHashCode(object));
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object object = new Object(){
            int field1 = 1;
            int field2 = 2;
            private int privateField = 3;

            @Override
            public String toString() {
                return "Object{" +
                        "field1=" + field1 +
                        ", field2=" + field2 +
                        '}';
            }
        };
        assertEquals(-883798463, HashCodeBuilder.reflectionHashCode(object, new String[]{"privateField"}));
    }

    @Test
    public void testIsRegistered() {
        Object object = new Object(){};
        assertFalse(HashCodeBuilder.isRegistered(object));

        HashCodeBuilder.reflectionHashCode(object);
        assertTrue(HashCodeBuilder.isRegistered(object));

        HashCodeBuilder.unregister(object);
        assertFalse(HashCodeBuilder.isRegistered(object));
    }
}