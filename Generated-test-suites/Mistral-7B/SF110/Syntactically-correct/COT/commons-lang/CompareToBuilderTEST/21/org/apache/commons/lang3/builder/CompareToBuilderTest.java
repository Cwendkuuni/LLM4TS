package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() throws Exception {
        Object lhs = new Object() {
            int value = 10;
        };
        Object rhs = new Object() {
            int value = 15;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-5, comparison);
    }

    @Test
    public void testReflectionCompareWithNull() {
        try {
            CompareToBuilder.reflectionCompare(null, new Object());
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReflectionCompareWithDifferentClasses() {
        Object lhs = new Object() {
        };
        Object rhs = new String("hello");
        try {
            CompareToBuilder.reflectionCompare(lhs, rhs);
            fail();
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        assertEquals(5, builder.toComparison());
    }

    @Test
    public void testAppend() {
        Object lhs = new Object() {
            int value = 10;
        };
        Object rhs = new Object() {
            int value = 15;
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-5, builder.toComparison());
    }

    @Test
    public void testAppendWithComparator() {
        Object lhs = new Object() {
            int value = 10;
        };
        Object rhs = new Object() {
            int value = 15;
        };
        Comparator<Object> comparator = (l, r) -> ((Integer) l.getClass().getField("value").get(l)).compareTo((Integer) rhs.getClass().getField("value").get(rhs));
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        assertEquals(-5, builder.toComparison());
    }

    @Test
    public void testAppendWithArrays() {
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithArraysAndComparator() {
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {1, 2, 4};
        Comparator<Object> comparator = (l, r) -> ((Integer[]) l).compareTo((Integer[]) r);
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithLongArrays() {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithIntArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithShortArrays() {
        short[] lhs = {1, 2, 3};
        short[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithCharArrays() {
        char[] lhs = {'a', 'b', 'c'};
        char[] rhs = {'a', 'b', 'd'};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithByteArrays() {
        byte[] lhs = {1, 2, 3};
        byte[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithDoubleArrays() {
        double[] lhs = {1.0, 2.0, 3.0};
        double[] rhs = {1.0, 2.0, 4.0};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithFloatArrays() {
        float[] lhs = {1.0f, 2.0f, 3.0f};
        float[] rhs = {1.0f, 2.0f, 4.0f};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendWithBooleanArrays() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, false};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }
}