package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testReflectionCompare() throws Exception {
        Object lhs = new Object() {};
        Object rhs = new Object() {};

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        Class<?> lhsClass = lhs.getClass();
        lhsClass = lhsClass.getSuperclass();

        Field field1 = lhsClass.getDeclaredField("field1");
        field1.setAccessible(true);
        field1.set(lhs, 1);

        Field field2 = lhsClass.getDeclaredField("field2");
        field2.setAccessible(true);
        field2.set(lhs, 2);

        ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(lhs, new HashSet<>(Arrays.asList("field2")));
        String lhsString = reflectionToStringBuilder.toString();

        Field field3 = rhs.getClass().getDeclaredField("field1");
        field3.setAccessible(true);
        field3.set(rhs, 1);

        Field field4 = rhs.getClass().getDeclaredField("field2");
        field4.setAccessible(true);
        field4.set(rhs, 3);

        ReflectionToStringBuilder reflectionToStringBuilderRhs = new ReflectionToStringBuilder(rhs, new HashSet<>(Arrays.asList("field1")));
        String rhsString = reflectionToStringBuilderRhs.toString();

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true, null, new String[0]);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, lhsClass, new String[]{"field1"});
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, lhsClass, new String[]{"field1"});
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true, lhsClass, new String[]{"field1"});
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, lhsClass, new String[]{lhsString, "field2"});
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, lhsClass, new String[]{rhsString, "field1"});
        assertEquals(1, comparison);
    }

    @org.junit.Test
    public void testAppendSuper() throws Exception {
        Object lhs = new Object() {};
        Object rhs = new Object() {
            int superCompareTo = 2;
        };

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(2);

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(2, comparison);
    }

    @org.junit.Test
    public void testAppend() throws Exception {
        Object lhs = new Object() {};
        Object rhs = new Object() {};

        CompareToBuilder compareToBuilder = new CompareToBuilder();

        // Test with null values
        compareToBuilder.append(null, null);
        assertEquals(0, compareToBuilder.toComparison());

        compareToBuilder.append(null, rhs);
        assertEquals(-1, compareToBuilder.toComparison());

        compareToBuilder.append(lhs, null);
        assertEquals(1, compareToBuilder.toComparison());

        // Test with primitive data types
        compareToBuilder.append(1, 2);
        assertEquals(-1, compareToBuilder.toComparison());

        compareToBuilder.append(1, 1);
        assertEquals(0, compareToBuilder.toComparison());

        compareToBuilder.append(1, 3);
        assertEquals(1, compareToBuilder.toComparison());

        // Test with arrays
        int[] intArrayLhs = {1, 2, 3};
        int[] intArrayRhs = {1, 2, 3};

        compareToBuilder.append(intArrayLhs, intArrayRhs);
        assertEquals(0, compareToBuilder.toComparison());

        int[] intArrayLhsNegative = {1, 2, 3};
        int[] intArrayRhsPositive = {1, 2, 4};

        compareToBuilder.append(intArrayLhsNegative, intArrayRhsPositive);
        assertEquals(-1, compareToBuilder.toComparison());

        int[] intArrayLhsPositive = {1, 2, 3};
        int[] intArrayRhsNegative = {1, 2, 4};

        compareToBuilder.append(intArrayLhsPositive, intArrayRhsNegative);
        assertEquals(1, compareToBuilder.toComparison());

        long[] longArrayLhs = {1, 2, 3};
        long[] longArrayRhs = {1, 2, 3};

        compareToBuilder.append(longArrayLhs, longArrayRhs);
        assertEquals(0, compareToBuilder.toComparison());

        long[] longArrayLhsNegative = {1, 2, 3};
        long[] longArrayRhsPositive = {1, 2, 4};

        compareToBuilder.append(longArrayLhsNegative, longArrayRhsPositive);
        assertEquals(-1, compareToBuilder.toComparison());

        long[] longArrayLhsPositive = {1, 2, 3};
        long[] longArrayRhsNegative = {1, 2, 4};

        compareToBuilder.append(longArrayLhsPositive, longArrayRhsNegative);
        assertEquals(1, compareToBuilder.toComparison());

        float[] floatArrayLhs = {1.0f, 2.0f, 3.0f};
        float[] floatArrayRhs = {1.0f, 2.0f, 3.0f};

        compareToBuilder.append(floatArrayLhs, floatArrayRhs);
        assertEquals(0, compareToBuilder.toComparison());

        float[] floatArrayLhsNegative = {1.0f, 2.0f, 3.0f};
        float[] floatArrayRhsPositive = {1.0f, 2.0f, 4.0f};

        compareToBuilder.append(floatArrayLhsNegative, floatArrayRhsPositive);
        assertEquals(-1, compareToBuilder.toComparison());

        float[] floatArrayLhsPositive = {1.0f, 2.0f, 3.0f};
        float[] floatArrayRhsNegative = {1.0f, 2.0f, 4.0f};

        compareToBuilder.append(floatArrayLhsPositive, floatArrayRhsNegative);
        assertEquals(1, compareToBuilder.toComparison());

        double[] doubleArrayLhs = {1.0, 2.0, 3.0};
        double[] doubleArrayRhs = {1.0, 2.0, 3.0};

        compareToBuilder.append(doubleArrayLhs, doubleArrayRhs);
        assertEquals(0, compareToBuilder.toComparison());

        double[] doubleArrayLhsNegative = {1.0, 2.0, 3.0};
        double[] doubleArrayRhsPositive = {1.0, 2.0, 4.0};

        compareToBuilder.append(doubleArrayLhsNegative, doubleArrayRhsPositive);
        assertEquals(-1, compareToBuilder.toComparison());

        double[] doubleArrayLhsPositive = {1.0, 2.0, 3.0};
        double[] doubleArrayRhsNegative = {1.0, 2.0, 4.0};

        compareToBuilder.append(doubleArrayLhsPositive, doubleArrayRhsNegative);
        assertEquals(1, compareToBuilder.toComparison());

        // Test with Comparable objects
        Comparable<Integer> comparableLhs = 1;
        Comparable<Integer> comparableRhs = 2;

        compareToBuilder.append(comparableLhs, comparableRhs);
        assertEquals(-1, compareToBuilder.toComparison());

        // Test with Comparator
        Comparator<Object> comparator = Comparator.comparingInt(o -> ((Integer) o).intValue());

        compareToBuilder.append(lhs, rhs, comparator);
        assertEquals(-1, compareToBuilder.toComparison());

        compareToBuilder.append(rhs, lhs, comparator.reversed());
        assertEquals(1, compareToBuilder.toComparison());
    }

    @org.junit.Test
    public void testAppendSuperWithCustomComparator() throws Exception {
        Object lhs = new Object() {};
        Object rhs = new Object() {
            int superCompareTo = 2;
        };

        Comparator<Object> comparator = Comparator.comparingInt(o -> ((Integer) ((CompareToBuilder) o).comparison).intValue());

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(3);

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0], comparator);
        assertEquals(1, comparison);
    }
}