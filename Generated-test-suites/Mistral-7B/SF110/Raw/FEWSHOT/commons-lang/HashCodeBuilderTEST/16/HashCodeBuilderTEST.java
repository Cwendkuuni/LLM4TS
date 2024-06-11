package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(0, hcb.hashCode());

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(1, 3);
        assertEquals(1, hcbWithInitialValue.hashCode());

        HashCodeBuilder hcbWithInitialValueAndMultiplier = new HashCodeBuilder(11, 31);
        assertEquals(11, hcbWithInitialValueAndMultiplier.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);
        assertEquals(65537, hcb.hashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new boolean[]{true, false, true});
        assertEquals(1474539871, hcb.hashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        hcb.append((byte) 255);
        assertEquals(-1610612735, hcb.hashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new byte[]{1, 255});
        assertEquals(1835220295, hcb.hashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('z');
        assertEquals(-1610612735, hcb.hashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new char[]{'a', 'z'});
        assertEquals(-1999424323, hcb.hashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double value = 3.14;
        hcb.append(value);
        assertEquals(17221518819635789691L, hcb.hashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] array = {1.2, 2.718, 3.14};
        hcb.append(array);
        assertEquals(-7376624509272266721L, hcb.hashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float value = 3.14f;
        hcb.append(value);
        assertEquals(-1592175043, hcb.hashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] array = {1.2f, 2.718f, 3.14f};
        hcb.append(array);
        assertEquals(1657125366, hcb.hashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.append(Integer.MAX_VALUE);
        assertEquals(-2108235355, hcb.hashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] array = {1, Integer.MAX_VALUE};
        hcb.append(array);
        assertEquals(2108235351, hcb.hashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        hcb.append(Long.MAX_VALUE);
        assertEquals(-9223372036854775807L, hcb.hashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] array = {1L, Long.MAX_VALUE};
        hcb.append(array);
        assertEquals(-9223372036854775805L, hcb.hashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object object = new Object();
        hcb.append(object);
        assertEquals(System.identityHashCode(object), hcb.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] array = {new Object(), new Object()};
        hcb.append(array);
        int hashCode = 0;
        for (Object obj : array) {
            hashCode += System.identityHashCode(obj);
        }
        assertEquals(hashCode, hcb.hashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 1);
        hcb.append((short) 32767);
        assertEquals(-1461234583, hcb.hashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] array = {1, 32767};
        hcb.append(array);
        assertEquals(-1932179901, hcb.hashCode());
    }

    @Test
    public void testAppendSuperInt() {
        class Test {
            int superHashCode = 123;
        }

        Test test = new Test();
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(test.superHashCode);
        assertEquals(123, hcb.hashCode());
    }

    @Test
    public void testReflectionHashCode() {
        class Test {
            int a = 1;
            int b = 2;
            int c = 3;
        }

        Test test = new Test();
        int hashCode = HashCodeBuilder.reflectionHashCode(test, true);
        assertEquals(598264932, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(test, "a", "b");
        assertEquals(16777214, hashCode);
    }
}