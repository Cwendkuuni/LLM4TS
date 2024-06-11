package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int value = 2;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        lhs = new Object() {
            int value = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, lhs);
        assertEquals(0, comparison);

        lhs = null;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertNull(lhs);
        assertEquals(-1, comparison);

        rhs = null;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertNull(rhs);
        assertEquals(1, comparison);

        lhs = new String("foo");
        rhs = new String("bar");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new String("bar");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        lhs = new String("foo");
        rhs = new String("foo");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        Object lhs = new ExcludableClass() {
            int value = 1;
        };
        Object rhs = new ExcludableClass() {
            int value = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "value");
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"value"});
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "value", "otherField");
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"value", "otherField"});
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "value", null);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null);
        assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithCompareTransients() {
        Object lhs = new TransientClass() {
            boolean transientField = true;
            int value = 1;
        };
        Object rhs = new TransientClass() {
            boolean transientField = false;
            int value = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(1);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();

        // Object comparison
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new String("foo");
        rhs = new String("bar");
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        // Primitive comparison
        builder = new CompareToBuilder();
        builder.append(1, 2);
        assertEquals(-1, builder.toComparison());

        builder.append(1, 1);
        assertEquals(0, builder.toComparison());

        builder.append(2, 1);
        assertEquals(1, builder.toComparison());

        // Array comparison with Comparator
        Object[] lhsArray = {1, 2, 3};
        Object[] rhsArray = {1, 3, 2};
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
        };
        builder.append(lhsArray, rhsArray, comparator);
        assertEquals(-1, builder.toComparison());

        // Array comparison without Comparator
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray);
        assertEquals(-1, builder.toComparison());

        // Array comparison with null
        lhsArray = new Object[]{null};
        rhsArray = new Object[]{1};
        builder.append(lhsArray, rhsArray);
        assertEquals(-1, builder.toComparison());

        lhsArray = new Object[]{1};
        rhsArray = new Object[]{null};
        builder.append(lhsArray, rhsArray);
        assertEquals(1, builder.toComparison());

        // Array comparison with equal length
        lhsArray = new Object[]{1, 2, 3};
        rhsArray = new Object[]{1, 2, 3};
        builder.append(lhsArray, rhsArray);
        assertEquals(0, builder.toComparison());

        // Array comparison with different length
        lhsArray = new Object[]{1, 2, 3};
        rhsArray = new Object[]{1, 2, 3, 4};
        builder.append(lhsArray, rhsArray);
        assertEquals(-1, builder.toComparison());

        // Primitive array comparison
        builder = new CompareToBuilder();
        builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        assertEquals(0, builder.toComparison());

        builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 4});
        assertEquals(-1, builder.toComparison());

        builder.append(new int[]{1, 2, 3}, new int[]{2, 2, 3});
        assertEquals(1, builder.toComparison());

        // Primitive array comparison with Comparator
        builder = new CompareToBuilder();
        builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 3}, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                for (int i = 0; i < Math.min(o1.length, o2.length); i++) {
                    int comparison = Integer.compare(o1[i], o2[i]);
                    if (comparison != 0) {
                        return comparison;
                    }
                }
                return Integer.compare(o1.length, o2.length);
            }
        });
        assertEquals(0, builder.toComparison());

        // Primitive array comparison with null
        int[] lhsPrimitiveArray = {1, 2, null};
        int[] rhsPrimitiveArray = {1, 2, 3};
        builder.append(lhsPrimitiveArray, rhsPrimitiveArray);
        assertEquals(-1, builder.toComparison());

        lhsPrimitiveArray = new int[]{1, 2, 3};
        rhsPrimitiveArray = new int[]{1, 2, null};
        builder.append(lhsPrimitiveArray, rhsPrimitiveArray);
        assertEquals(1, builder.toComparison());

        // Primitive array comparison with equal length
        lhsPrimitiveArray = new int[]{1, 2, 3};
        rhsPrimitiveArray = new int[]{1, 2, 3};
        builder.append(lhsPrimitiveArray, rhsPrimitiveArray);
        assertEquals(0, builder.toComparison());

        // Primitive array comparison with different length
        lhsPrimitiveArray = new int[]{1, 2, 3};
        rhsPrimitiveArray = new int[]{1, 2, 3, 4};
        builder.append(lhsPrimitiveArray, rhsPrimitiveArray);
        assertEquals(-1, builder.toComparison());

        // Primitive array comparison with different data types
        builder = new CompareToBuilder();
        builder.append(new int[]{1, 2, 3}, new long[]{1, 2, 3});
        assertEquals(-1, builder.toComparison());

        builder.append(new int[]{1, 2, 3}, new float[]{1, 2, 3});
        assertEquals(-1, builder.toComparison());

        builder.append(new int[]{1, 2, 3}, new double[]{1, 2, 3});
        assertEquals(-1, builder.toComparison());

        builder.append(new int[]{1, 2, 3}, new boolean[]{true, false, true});
        assertEquals(-1, builder.toComparison());

        // Primitive array comparison with different data types and null
        int[] ints = {1, 2, null};
        long[] longs = {1, 2, 3};
        builder.append(ints, longs);
        assertEquals(-1, builder.toComparison());

        ints = new int[]{1, 2, 3};
        longs = new long[]{1, 2, null};
        builder.append(ints, longs);
        assertEquals(1, builder.toComparison());

        // Custom class comparison
        Object customLhs = new CustomClass();
        Object customRhs = new CustomClass();
        builder.append(customLhs, customRhs);
        assertEquals(0, builder.toComparison());

        customLhs = new CustomClass("foo");
        customRhs = new CustomClass("bar");
        builder.append(customLhs, customRhs);
        assertEquals(-1, builder.toComparison());
    }
}

class CustomClass implements Comparable<CustomClass> {
    String name;

    CustomClass() {
    }

    CustomClass(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(CustomClass o) {
        return this.name.compareTo(o.name);
    }
}

class ExcludableClass {
    String value;
    String otherField;

    ExcludableClass() {
    }

    ExcludableClass(String value) {
        this.value = value;
    }

    ExcludableClass(String value, String otherField) {
        this.value = value;
        this.otherField = otherField;
    }
}

class TransientClass {
    @SuppressWarnings("unused")
    transient boolean transientField;
    int value;

    TransientClass() {
    }

    TransientClass(boolean transientField, int value) {
        this.transientField = transientField;
        this.value = value;
    }
}