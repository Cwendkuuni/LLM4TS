package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testCompareToBuilderBasicUsage() {
        Comparable<Integer> lhs = 5;
        Comparable<Integer> rhs = 10;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testCompareToBuilderWithArray() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);

        lhs = new int[]{1, 2, 3};
        rhs = new int[]{2, 2, 3};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);

        lhs = new int[]{1, 2, 3};
        rhs = new int[]{1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testCompareToBuilderWithLongArray() {
        long[] lhs = {1L, 2L, 3L};
        long[] rhs = {1L, 2L, 3L};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);

        lhs = new long[]{1L, 2L, 3L};
        rhs = new long[]{2L, 2L, 3L};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);

        lhs = new long[]{1L, 2L, 3L};
        rhs = new long[]{1L, 2L, 4L};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testCompareToBuilderWithString() {
        String lhs = "abc";
        String rhs = "abc";
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);

        lhs = "abc";
        rhs = "abcd";
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);

        lhs = "abc";
        rhs = "acb";
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToBuilderNullArguments() {
        Comparable<Integer> lhs = null;
        Comparable<Integer> rhs = 10;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToBuilderClassCastException() {
        Object lhs = new Object() {};
        Comparable<Integer> rhs = 10;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCompareToBuilderIllegalAccessException() throws IllegalAccessException {
        Object lhs = new Object() {};
        Object rhs = new Object() {};
        Field field1 = Object.class.getDeclaredField("field1");
        Field field2 = Object.class.getDeclaredField("field2");
        Object field1Value = FieldUtils.readField(lhs, field1, true);
        Object field2Value = FieldUtils.readField(rhs, field2, true);
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        compareToBuilder.append(field1Value, field2Value);
        int comparison = compareToBuilder.toComparison();
    }
}