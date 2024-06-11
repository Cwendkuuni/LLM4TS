package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int i = 1;
            int j = 2;
        };

        Object rhs = new Object() {
            int i = 2;
            int j = 1;
        };

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
            int j = 1;
        };

        rhs = new Object() {
            int i = 2;
            int j = 1;
        };

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
            int j = 1;
        };

        rhs = new Object() {
            int i = 1;
            int j = 1;
        };

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = null;
        rhs = new Object() {
            int i = 1;
        };

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertNull(lhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
        };

        comparison = CompareToBuilder.reflectionCompare(lhs, null);
        assertEquals(1, comparison);
    }

    @Test
    public void testAppend() {
        final Object lhs = new Object() {
            int i = 1;
            int j = 2;
        };

        final Object rhs = new Object() {
            int i = 2;
            int j = 1;
        };

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.hashCode() - o2.hashCode();
            }
        });
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendArray() {
        final Object[] lhs = new Object[]{1, 2, 3};
        final Object[] rhs = new Object[]{3, 2, 1};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs, null);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs, null);
        assertEquals(1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.hashCode() - o2.hashCode();
            }
        });
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendLongArray() {
        final long[] lhs = new long[]{1, 2, 3};
        final long[] rhs = new long[]{3, 2, 1};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendIntArray() {
        final int[] lhs = new int[]{1, 2, 3};
        final int[] rhs = new int[]{3, 2, 1};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendShortArray() {
        final short[] lhs = new short[]{1, 2, 3};
        final short[] rhs = new short[]{3, 2, 1};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendCharArray() {
        final char[] lhs = new char[]{
                'a', 'b', 'c'
        };
        final char[] rhs = new char[]{
                'c', 'b', 'a'
        };

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendByteArray() {
        final byte[] lhs = new byte[]{1, 2, 3};
        final byte[] rhs = new byte[]{3, 2, 1};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendDoubleArray() {
        final double[] lhs = new double[]{1.0, 2.0, 3.0};
        final double[] rhs = new double[]{3.0, 2.0, 1.0};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendFloatArray() {
        final float[] lhs = new float[]{1.0f, 2.0f, 3.0f};
        final float[] rhs = new float[]{3.0f, 2.0f, 1.0f};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendBooleanArray() {
        final boolean[] lhs = new boolean[]{true, false, true};
        final boolean[] rhs = new boolean[]{true, false, false};

        final CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, lhs);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendSuper() {
        final Object lhs = new Object() {
            int i = 1;
        };

        final Object rhs = new Object() {
            int i = 2;
        };

        final CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(lhs, rhs));
        assertEquals(-1, builder.toComparison());
    }
}