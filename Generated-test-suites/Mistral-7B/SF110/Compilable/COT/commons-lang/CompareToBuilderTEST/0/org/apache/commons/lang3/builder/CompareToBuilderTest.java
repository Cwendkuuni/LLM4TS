package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {};
        Object rhs = new Object() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompareNullLhs() {
        Object lhs = null;
        Object rhs = new Object() {};
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompareNullRhs() {
        Object lhs = new Object() {};
        Object rhs = null;
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompareDifferentTypes() {
        Object lhs = new Object() {};
        Object rhs = new Integer(1);
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        Object lhs = new ClassWithFields() {};
        Object rhs = new ClassWithFields() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, "field1", "field2");
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompareWithCompareTransientsFalse() {
        Object lhs = new ClassWithTransientField() {};
        Object rhs = new ClassWithTransientField() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, false);
        assertNotEquals(0, result);
    }

    @Test
    public void testReflectionCompareWithCompareTransientsTrue() {
        Object lhs = new ClassWithTransientField() {};
        Object rhs = new ClassWithTransientField() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        int superCompareTo = 5;
        CompareToBuilder appendedBuilder = builder.appendSuper(superCompareTo);
        assertEquals(superCompareTo, appendedBuilder.toComparison());
    }

    @Test
    public void testAppendObjects() {
        Object lhs = new Object() {};
        Object rhs = new Object() {};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertNotEquals(0, result);
    }

    @Test
    public void testAppendObjectsNullLhs() {
        Object lhs = null;
        Object rhs = new Object() {};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test
    public void testAppendObjectsNullRhs() {
        Object lhs = new Object() {};
        Object rhs = null;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(1, result);
    }

    @Test
    public void testAppendArraysInts() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(0, result);
    }

    @Test
    public void testAppendArraysIntsDifferentLengths() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test
    public void testAppendArraysIntsWithComparator() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {2, 1, 3};
        Comparator<int[]> comparator = (l, r) -> Integer.compare(l.length, r.length);
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test
    public void testAppendArraysDoubles() {
        double[] lhs = {1.1, 2.1, 3.1};
        double[] rhs = {1.1, 2.1, 3.1};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(0, result);
    }

    @Test
    public void testAppendArraysDoublesDifferentLengths() {
        double[] lhs = {1.1, 2.1, 3.1};
        double[] rhs = {1.1, 2.1, 3.1, 4.1};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test
    public void testAppendArraysDoublesWithComparator() {
        double[] lhs = {1.1, 2.1, 3.1};
        double[] rhs = {2.1, 1.1, 3.1};
        Comparator<double[]> comparator = (l, r) -> Double.compare(l[0], r[0]);
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test
    public void testAppendArraysBooleans() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, true};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(0, result);
    }

    @Test
    public void testAppendArraysBooleansDifferentLengths() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, true, false};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test
    public void testAppendArraysBooleansWithComparator() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {false, true, true};
        Comparator<boolean[]> comparator = (l, r) -> Boolean.compare(l[0], r[0]);
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    private static class ClassWithFields {
        int field1;
        String field2;
        transient int field3;
    }

    private static class ClassWithTransientField {
        int field1;
        transient int field2;
    }

}