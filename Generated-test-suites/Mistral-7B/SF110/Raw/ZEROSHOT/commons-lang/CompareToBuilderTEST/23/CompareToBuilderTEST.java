package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.Arrays;

public class CompareToBuilderTest {

    private CompareToBuilder lhsBuilder, rhsBuilder;
    private Integer lhs, rhs;

    @Before
    public void setUp() {
        lhsBuilder = new CompareToBuilder();
        rhsBuilder = new CompareToBuilder();
        
        lhs = 5;
        rhs = 10;

        FieldUtils.writeFieldValue(lhsBuilder, "comparison", lhs, true);
        FieldUtils.writeFieldValue(rhsBuilder, "comparison", rhs, true);
    }

    @Test
    public void testReflectionCompare() {
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-5, comparison);
    }

    @Test
    public void testReflectionCompareWithTransients() {
        lhsBuilder.appendSuper(0);
        rhsBuilder.appendSuper(5);

        comparison = CompareToBuilder.reflectionCompare(lhsBuilder, rhsBuilder, true, null, new String[0]);
        assertEquals(5, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludedFields() {
        lhsBuilder.appendSuper(0);
        rhsBuilder.appendSuper(5);

        comparison = CompareToBuilder.reflectionCompare(lhsBuilder, rhsBuilder, false, null, Arrays.asList("comparison").toArray(new String[0]));
        assertEquals(5, comparison);
    }

    @Test
    public void testAppendSuper() {
        lhsBuilder.appendSuper(1);
        rhsBuilder.appendSuper(2);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(1, comparison);
    }

    @Test
    public void testAppendObjectObject() {
        lhsBuilder.append(lhs, rhs);
        rhsBuilder.append(lhs, rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendObjectObjectComparator() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable<Object>)o1).compareTo(o2);
        lhsBuilder.append(lhs, rhs, comparator);
        rhsBuilder.append(lhs, rhs, comparator);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendLongLong() {
        lhsBuilder.append((long)lhs, (long)rhs);
        rhsBuilder.append((long)lhs, (long)rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppendIntInt() {
        lhsBuilder.append(lhs, rhs);
        rhsBuilder.append(lhs, rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppendShortShort() {
        lhsBuilder.append((short)lhs, (short)rhs);
        rhsBuilder.append((short)lhs, (short)rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppendCharChar() {
        lhsBuilder.append((char)lhs, (char)rhs);
        rhsBuilder.append((char)lhs, (char)rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppendByteByte() {
        lhsBuilder.append((byte)lhs, (byte)rhs);
        rhsBuilder.append((byte)lhs, (byte)rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppendDoubleDouble() {
        lhsBuilder.append(lhs, lhs);
        rhsBuilder.append(rhs, rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendFloatFloat() {
        lhsBuilder.append(lhs, lhs);
        rhsBuilder.append(rhs, rhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendBooleanBoolean() {
        lhsBuilder.append(lhs, lhs);
        rhsBuilder.append(lhs, lhs);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendObjectArrayObjectArray() {
        Object[] lhsArray = {1, 2, 3};
        Object[] rhsArray = {1, 2, 4};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test(expected = ClassCastException.class)
    public void testAppendObjectArrayDifferentTypes() {
        Object[] lhsArray = {1, 2, 3};
        String[] rhsArray = {"1", "2", "3"};

        lhsBuilder.append(lhsArray, rhsArray);
    }

    @Test
    public void testAppendObjectArrayComparator() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable<Object>)o1).compareTo(o2);
        Object[] lhsArray = {1, 2, 3};
        Object[] rhsArray = {1, 2, 4};

        lhsBuilder.append(lhsArray, rhsArray, comparator);
        rhsBuilder.append(lhsArray, rhsArray, comparator);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendLongArrayLongArray() {
        long[] lhsArray = {1, 2, 3};
        long[] rhsArray = {1, 2, 4};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendIntArrayIntArray() {
        int[] lhsArray = {1, 2, 3};
        int[] rhsArray = {1, 2, 4};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendShortArrayShortArray() {
        short[] lhsArray = {1, 2, 3};
        short[] rhsArray = {1, 2, 4};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendCharArrayCharArray() {
        char[] lhsArray = {'a', 'b', 'c'};
        char[] rhsArray = {'a', 'b', 'd'};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendByteArrayByteArray() {
        byte[] lhsArray = {1, 2, 3};
        byte[] rhsArray = {1, 2, 4};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendDoubleArrayDoubleArray() {
        double[] lhsArray = {1.0, 2.0, 3.0};
        double[] rhsArray = {1.0, 2.0, 4.0};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendFloatArrayFloatArray() {
        float[] lhsArray = {1.0f, 2.0f, 3.0f};
        float[] rhsArray = {1.0f, 2.0f, 4.0f};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendBooleanArrayBooleanArray() {
        boolean[] lhsArray = {true, false, true};
        boolean[] rhsArray = {true, false, false};

        lhsBuilder.append(lhsArray, rhsArray);
        rhsBuilder.append(lhsArray, rhsArray);

        comparison = lhsBuilder.compareTo(rhsBuilder);
        assertEquals(-1, comparison);
    }
}