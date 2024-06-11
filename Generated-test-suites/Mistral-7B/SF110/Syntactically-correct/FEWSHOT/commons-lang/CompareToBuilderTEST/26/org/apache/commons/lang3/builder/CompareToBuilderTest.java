package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Foo(1, "one");
        Object rhs = new Foo(1, "one");
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new Foo(1, "one");
        rhs = new Foo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Foo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        Object lhs = new Foo(1, "one");
        Object rhs = new Foo(1, "one");
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field2");
        assertEquals(0, comparison);

        lhs = new Foo(1, "one");
        rhs = new Foo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field2");
        assertEquals(-1, comparison);

        lhs = new Foo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field2");
        assertEquals(1, comparison);

        lhs = new Foo(1, "one");
        rhs = new Foo(1, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field1", "field2");
        assertEquals(0, comparison);

        lhs = new Foo(1, "one");
        rhs = new Foo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field1", "field2");
        assertEquals(-1, comparison);

        lhs = new Foo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field1", "field2");
        assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithSuperClass() {
        Object lhs = new SubFoo(1, "one");
        Object rhs = new SubFoo(1, "one");
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Foo.class, false, new String[0]);
        assertEquals(0, comparison);

        lhs = new SubFoo(1, "one");
        rhs = new SubFoo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Foo.class, false, new String[0]);
        assertEquals(-1, comparison);

        lhs = new SubFoo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Foo.class, false, new String[0]);
        assertEquals(1, comparison);

        lhs = new SubFoo(1, "one");
        rhs = new SubFoo(1, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Foo.class, true, new String[0]);
        assertEquals(1, comparison);

        lhs = new SubFoo(1, "one");
        rhs = new SubFoo(2, "two");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Foo.class, true, new String[0]);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend() {
        Object lhs = new Foo(1, "one");
        Object rhs = new Foo(1, "one");
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(0, compareToBuilder.toComparison());

        lhs = new Foo(1, "one");
        rhs = new Foo(2, "two");
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(-1, compareToBuilder.toComparison());

        lhs = new Foo(2, "two");
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppendWithArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(0, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(-1, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppendWithComparators() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        Comparator<int[]> comparator = Comparator.comparingInt(a -> a[0]);
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, comparator);
        assertEquals(0, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, comparator);
        assertEquals(-1, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, comparator);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppendWithCustomComparisons() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new CustomComparator());
        assertEquals(0, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new CustomComparator());
        assertEquals(-1, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new CustomComparator());
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppendWithCustomBuilder() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        CompareToBuilder compareToBuilder2 = new CompareToBuilder();
        compareToBuilder2.append(lhs[0], rhs[0]);
        compareToBuilder2.append(lhs[1], rhs[1]);
        compareToBuilder2.append(lhs[2], rhs[2]);
        compareToBuilder.append(lhs, rhs, compareToBuilder2);
        assertEquals(0, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder2.append(lhs[0], rhs[0]);
        compareToBuilder2.append(lhs[1], rhs[1]);
        compareToBuilder2.append(lhs[2], rhs[2]);
        compareToBuilder.append(lhs, rhs, compareToBuilder2);
        assertEquals(-1, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder2.append(lhs[0], rhs[0]);
        compareToBuilder2.append(lhs[1], rhs[1]);
        compareToBuilder2.append(lhs[2], rhs[2]);
        compareToBuilder.append(lhs, rhs, compareToBuilder2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppendWithCustomBuilderWithComparators() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        Comparator<Integer> comparator = Comparator.comparingInt(i -> i);
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        CompareToBuilder compareToBuilder2 = new CompareToBuilder();
        compareToBuilder2.append(lhs[0], rhs[0], comparator);
        compareToBuilder2.append(lhs[1], rhs[1], comparator);
        compareToBuilder2.append(lhs[2], rhs[2], comparator);
        compareToBuilder.append(lhs, rhs, compareToBuilder2);
        assertEquals(0, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder2.append(lhs[0], rhs[0], comparator);
        compareToBuilder2.append(lhs[1], rhs[1], comparator);
        compareToBuilder2.append(lhs[2], rhs[2], comparator);
        compareToBuilder.append(lhs, rhs, compareToBuilder2);
        assertEquals(-1, compareToBuilder.toComparison());

        lhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder2.append(lhs[0], rhs[0], comparator);
        compareToBuilder2.append(lhs[1], rhs[1], comparator);
        compareToBuilder2.append(lhs[2], rhs[2], comparator);
        compareToBuilder.append(lhs, rhs, compareToBuilder2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    private static class Foo {
        private final int number;
        private final String name;

        public Foo(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "number=" + number +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private static class SubFoo extends Foo {
        public SubFoo(int number, String name) {
            super(number, name);
        }
    }

    private static class CustomComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            return 0;
        }
    }
}