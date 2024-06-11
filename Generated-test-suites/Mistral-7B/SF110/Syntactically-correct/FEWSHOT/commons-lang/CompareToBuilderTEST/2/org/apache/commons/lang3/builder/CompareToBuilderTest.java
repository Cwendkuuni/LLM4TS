package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testComparisonOfTwoObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfNullAndNonNullObject() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfTwoArraysOfInts() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfTwoArraysOfDifferentLengths() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3, 4};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfTwoArraysOfLongs() {
        CompareToBuilder builder = new CompareToBuilder();
        long[] lhs = {1L, 2L, 3L};
        long[] rhs = {1L, 2L, 3L};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfTwoArraysOfDifferentTypes() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        long[] rhs = {1L, 2L, 3L};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfTwoObjectsWithCustomComparator() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        Comparator<Object> comparator = (l, r) -> Integer.compare((int) l.hashCode(), (int) r.hashCode());
        builder.append(lhs, rhs, comparator);
        int comparison = builder.toComparison();
        // Replace this with the expected comparison result based on the custom comparator logic
        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonWithExcludedFields() {
        CompareToBuilder builder = new CompareToBuilder();
        class MyClass {
            int field1 = 1;
            int field2 = 2;
            int field3 = 3;

            @Override
            public String toString() {
                return "MyClass{" +
                       "field1=" + field1 +
                       ", field2=" + field2 +
                       ", field3=" + field3 +
                       '}';
            }
        }

        MyClass lhs = new MyClass();
        MyClass rhs = new MyClass();
        builder.appendSuper(1);
        builder.append(lhs, rhs, "field1");
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }
}