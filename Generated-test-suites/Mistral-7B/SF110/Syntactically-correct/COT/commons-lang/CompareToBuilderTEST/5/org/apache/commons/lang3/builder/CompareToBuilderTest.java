package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testComparison_Object_Object() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparison_Object_Object_true() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        compareToBuilder.appendSuper(1);
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testComparison_Object_Object_false() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        compareToBuilder.appendSuper(-1);
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testComparison_Null_Object() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
    }

    @Test
    public void testComparison_ObjectArray_ObjectArray() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = new Object[1];
        Object[] rhs = new Object[1];
        compareToBuilder.append((Object[])lhs, (Object[])rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparison_ObjectArray_ObjectArray_Comparator() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = new Object[1];
        Object[] rhs = new Object[1];
        Comparator<?> comparator = (o1, o2) -> 1;
        compareToBuilder.append((Object[])lhs, (Object[])rhs, comparator);
        int comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testComparison_ObjectArray_Null() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = new Object[1];
        Object[] rhs = null;
        compareToBuilder.append((Object[])lhs, (Object[])rhs);
    }

    @Test(expected = ArrayStoreException.class)
    public void testComparison_ObjectArray_ObjectArray_DifferentLength() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = new Object[1];
        Object[] rhs = new Object[2];
        compareToBuilder.append((Object[])lhs, (Object[])rhs);
    }

    @Test
    public void testComparison_LongArray_LongArray() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        long[] lhs = new long[1];
        long[] rhs = new long[1];
        compareToBuilder.append((long[])lhs, (long[])rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparison_LongArray_LongArray_Comparator() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        long[] lhs = new long[1];
        long[] rhs = new long[1];
        Comparator<Long> comparator = (o1, o2) -> 1L;
        compareToBuilder.append((long[])lhs, (long[])rhs, comparator);
        int comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test(expected = ArrayStoreException.class)
    public void testComparison_LongArray_ObjectArray() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        long[] lhs = new long[1];
        Object[] rhs = new Object[1];
        compareToBuilder.append((long[])lhs, (Object[])rhs);
    }

    @Test(expected = ArrayStoreException.class)
    public void testComparison_LongArray_DoubleArray() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        long[] lhs = new long[1];
        double[] rhs = new double[1];
        compareToBuilder.append((long[])lhs, (double[])rhs);
    }

    @Test
    public void testComparison_DoubleArray_DoubleArray() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        double[] lhs = new double[1];
        double[] rhs = new double[1];
        compareToBuilder.append((double[])lhs, (double[])rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparison_DoubleArray_DoubleArray_Comparator() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        double[] lhs = new double[1];
        double[] rhs = new double[1];
        Comparator<Double> comparator = (o1, o2) -> 1D;
        compareToBuilder.append((double[])lhs, (double[])rhs, comparator);
        int comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test(expected = ArrayStoreException.class)
    public void testComparison_DoubleArray_LongArray() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        double[] lhs = new double[1];
        long[] rhs = new long[1];
        compareToBuilder.append((double[])lhs, (long[])rhs);
    }

    @Test(expected = InternalError.class)
    public void testReflectionCompare_Null_Null() {
        CompareToBuilder.reflectionCompare(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_Object_Null() {
        CompareToBuilder.reflectionCompare(new Object(), null);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_DifferentClass() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new String("Test");
        Object rhs = new Integer(1);
        compareToBuilder.appendSuper(0);
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        try {
            CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
            fail();
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testReflectionCompare_SameClass() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        String lhs = new String("Test");
        String rhs = new String("Test");
        compareToBuilder.appendSuper(0);
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
        int reflectionComparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        assertEquals(0, reflectionComparison);
    }

    @Test
    public void testReflectionCompare_ExcludeFields() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        String lhs = new String("Test");
        String rhs = new String("TestExcluded");
        compareToBuilder.appendSuper(0);
        compareToBuilder.append(lhs, rhs, new String[]{"fieldToExclude"});
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
        int reflectionComparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[]{"fieldToExclude"});
        assertEquals(0, reflectionComparison);
    }

    @Test
    public void testReflectionCompare_CompareTransients() {
        class TestClass {
            private int field1;
            private String field2;
            private String transientField;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != this.getClass()) return false;
                TestClass that = (TestClass) o;
                return field1 == that.field1 && Objects.equals(field2, that.field2);
            }

            @Override
            public String toString() {
                return "TestClass{" +
                        "field1=" + field1 +
                        ", field2='" + field2 + '\'' +
                        ", transientField='" + transientField + '\'' +
                        '}';
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        lhs.transientField = "TransientValue1";
        rhs.transientField = "TransientValue2";
        compareToBuilder.appendSuper(0);
        compareToBuilder.append(lhs, rhs, true);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
        int reflectionComparison = CompareToBuilder.reflectionCompare(lhs, rhs, true, null, new String[0]);
        assertEquals(0, reflectionComparison);
    }
}