package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.junit.Test;

public class CompareToBuilderTest {
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(1);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendObjectObjectNullComparator() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendObjectObjectNull() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendObjectObjectNotNull() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        // assume that equals() returns false for lhs and rhs
        // and that comparing lhs and rhs with a default Comparator returns a negative number
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfObjectNullComparator() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] lhs = new Object[0];
        Object[] rhs = new Object[0];
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfObjectNull() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] lhs = new Object[0];
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfObjectNotNull() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] lhs = new Object[] { new Object(), new Object() };
        Object[] rhs = new Object[] { new Object(), new Object() };
        builder.append(lhs, rhs);
        // assume that comparing the first elements of lhs and rhs with a default Comparator returns a negative number
        // and that comparing the second elements with the default Comparator returns 0
        // and that lhs and rhs have the same length
        assertEquals(-1, builder.toComparison());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendArrayOfLongNull() {
        CompareToBuilder builder = new CompareToBuilder();
        long[] lhs = null;
        builder.append(lhs, new long[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendArrayOfLongNullComparator() {
        CompareToBuilder builder = new CompareToBuilder();
        long[] lhs = new long[0];
        long[] rhs = null;
        builder.append(lhs, rhs);
    }

    @Test
    public void testAppendArrayOfLong() {
        CompareToBuilder builder = new CompareToBuilder();
        long[] lhs = { 1, 2, 3 };
        long[] rhs = { 1, 2, 3 };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfLongNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        long[] lhs = { 1, 2, 3 };
        long[] rhs = { 1, 3, 2 };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfInt() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = { 1, 2, 3 };
        int[] rhs = { 1, 2, 3 };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfIntNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = { 1, 2, 3 };
        int[] rhs = { 1, 3, 2 };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfShort() {
        CompareToBuilder builder = new CompareToBuilder();
        short[] lhs = { 1, 2, 3 };
        short[] rhs = { 1, 2, 3 };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfShortNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        short[] lhs = { 1, 2, 3 };
        short[] rhs = { 1, 3, 2 };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfChar() {
        CompareToBuilder builder = new CompareToBuilder();
        char[] lhs = { 'a', 'b', 'c' };
        char[] rhs = { 'a', 'b', 'c' };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfCharNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        char[] lhs = { 'a', 'b', 'c' };
        char[] rhs = { 'a', 'c', 'b' };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfByte() {
        CompareToBuilder builder = new CompareToBuilder();
        byte[] lhs = { 1, 2, 3 };
        byte[] rhs = { 1, 2, 3 };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfByteNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        byte[] lhs = { 1, 2, 3 };
        byte[] rhs = { 1, 3, 2 };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfDouble() {
        CompareToBuilder builder = new CompareToBuilder();
        double[] lhs = { 1.0, 2.0, 3.0 };
        double[] rhs = { 1.0, 2.0, 3.0 };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfDoubleNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        double[] lhs = { 1.0, 2.0, 3.0 };
        double[] rhs = { 1.0, 3.0, 2.0 };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfFloat() {
        CompareToBuilder builder = new CompareToBuilder();
        float[] lhs = { 1.0f, 2.0f, 3.0f };
        float[] rhs = { 1.0f, 2.0f, 3.0f };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfFloatNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        float[] lhs = { 1.0f, 2.0f, 3.0f };
        float[] rhs = { 1.0f, 3.0f, 2.0f };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfBoolean() {
        CompareToBuilder builder = new CompareToBuilder();
        boolean[] lhs = { true, false, true };
        boolean[] rhs = { true, false, true };
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArrayOfBooleanNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        boolean[] lhs = { true, false, true };
        boolean[] rhs = { true, true, false };
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendArrayOfBooleanWrongType() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = new int[0];
        builder.append(lhs, new boolean[0]);
    }
}