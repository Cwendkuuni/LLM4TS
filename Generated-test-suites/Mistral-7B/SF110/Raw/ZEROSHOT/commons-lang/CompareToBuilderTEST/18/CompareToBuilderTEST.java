package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.ArrayUtils;

import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() throws Exception {
        // Test with default settings
        Object lhs = new Foo(1, "hello");
        Object rhs = new Foo(1, "hello");
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        // Test with different classes
        lhs = new Bar(1, 2.0f);
        rhs = new Foo(1, "hello");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        // Test with excludeFields
        String[] excludeFields = {"field2"};
        lhs = new Foo(1, "hello");
        rhs = new Foo(1, "world");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList(excludeFields.clone()));
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompareWithComparator() throws Exception {
        // Test with default settings and null comparator
        Object lhs = new Foo(1, "hello");
        Object rhs = new Foo(1, "hello");
        Comparator<?> comparator = null;
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, comparator);
        assertEquals(0, comparison);

        // Test with Comparable objects
        lhs = new Foo(1, "hello");
        rhs = new Foo(2, "hello");
        comparator = new CompareToComparator();
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, comparator);
        assertEquals(-1, comparison);

        // Test with custom Comparator
        lhs = new Foo(1, "hello");
        rhs = new Foo(1, "world");
        comparator = new MyCustomComparator();
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, comparator);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompareWithTransients() throws Exception {
        // Test with default settings
        Object lhs = new FooTransient(1, "hello");
        Object rhs = new FooTransient(1, "hello");
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false);
        assertEquals(0, comparison);

        // Test with transients included
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithSuperCompareTo() throws Exception {
        Object lhs = new Foo(1, "hello");
        Object rhs = new Foo(2, "world");
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(1);
        Object result = compareToBuilder.build();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, compareToBuilder);
        assertEquals(2, comparison);
    }

    @Test
    public void testAppendSuper() throws Exception {
        // Test appendSuper with zero comparison
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(0);
        Object result = compareToBuilder.build();
        assertEquals(0, ((Number) result).intValue());

        // Test appendSuper with negative comparison
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(-1);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test appendSuper with positive comparison
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(1);
        result = compareToBuilder.build();
        assertEquals(1, ((Number) result).intValue());
    }

    @Test
    public void testAppend() throws Exception {
        // Test append with Objects
        Object lhs = new Foo(1, "hello");
        Object rhs = new Foo(1, "world");
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        Object result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with longs
        long lhsLong = 1L;
        long rhsLong = 2L;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsLong, rhsLong);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with ints
        int lhsInt = 1;
        int rhsInt = 2;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsInt, rhsInt);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with shorts
        short lhsShort = 1;
        short rhsShort = 2;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsShort, rhsShort);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with chars
        char lhsChar = 'h';
        char rhsChar = 'w';
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsChar, rhsChar);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with bytes
        byte lhsByte = 1;
        byte rhsByte = 2;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsByte, rhsByte);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with doubles
        double lhsDouble = 1.0;
        double rhsDouble = 2.0;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsDouble, rhsDouble);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with floats
        float lhsFloat = 1.0f;
        float rhsFloat = 2.0f;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsFloat, rhsFloat);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with booleans
        boolean lhsBoolean = true;
        boolean rhsBoolean = false;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsBoolean, rhsBoolean);
        result = compareToBuilder.build();
        assertEquals(1, ((Number) result).intValue());

        // Test append with arrays
        int[] lhsArray = {1, 2, 3};
        int[] rhsArray = {2, 3, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsArray, rhsArray);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with Comparators
        Object[] lhsObjectArray = {new Foo(1, "hello"), new Foo(1, "world")};
        Object[] rhsObjectArray = {new Foo(1, "hello"), new Foo(2, "world")};
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return new CompareToBuilder().append(o1, o2).toComparison();
            }
        };
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhsObjectArray, rhsObjectArray, comparator);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());
    }

    @Test
    public void testAppendWithExcludeFields() throws Exception {
        String[] excludeFields = {"field2"};
        Object lhs = new Foo(1, "hello");
        Object rhs = new Foo(1, "world");
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, excludeFields);
        Object result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());

        // Test append with empty excludeFields
        excludeFields = new String[0];
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, excludeFields);
        result = compareToBuilder.build();
        assertEquals(-1, ((Number) result).intValue());
    }

    private static class Foo {
        private int field1;
        private String field2;

        public Foo(int field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }

    private static class FooTransient {
        @SuppressWarnings("unused")
        private static final long serialVersionUID = 1L;
        private transient int field1;
        private String field2;

        public FooTransient(int field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ReflectionToStringBuilder.DEFAULT_TO_STRING_STYLE);
        }
    }

    private static class Bar {
        private int field1;
        private float field2;

        public Bar(int field1, float field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }

    private static class CompareToComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            return new CompareToBuilder().append(o1, o2).toComparison();
        }
    }

    private static class MyCustomComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            return new CompareToBuilder().appendSuper(0).append(o1, o2).toComparison();
        }
    }
}