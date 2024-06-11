package org.apache.commons.lang3;

import static org.junit.Assert.*;

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
            int value = 1;
        };
        rhs = lhs;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new Object();
        rhs = new Object();
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertNull(comparison);

        lhs = new String("foo");
        rhs = new String("bar");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new String("foo");
        rhs = new String("foo");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        class TestClass {
            int field1 = 1;
            int field2 = 2;
            int field3 = 3;
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.field1 = 4;

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field2");
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithCompareTransients() {
        class TestClass {
            int field1 = 1;
            int transientField = 2;

            public int getTransientField() {
                return transientField;
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.transientField = 4;

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);

        lhs = new Object() {
            int value = 1;
        };
        rhs = lhs;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        rhs = new Object();
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertNull(comparison);

        String strLhs = "foo";
        String strRhs = "bar";
        builder = new CompareToBuilder();
        builder.append(strLhs, strRhs);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);

        strLhs = "foo";
        strRhs = "foo";
        builder = new CompareToBuilder();
        builder.append(strLhs, strRhs);
        comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendWithArrays() {
        int[] intArrayLhs = {1, 2, 3};
        int[] intArrayRhs = {1, 2, 4};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(intArrayLhs, intArrayRhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);

        intArrayLhs = new int[]{1, 2, 3};
        intArrayRhs = new int[]{1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(intArrayLhs, intArrayRhs);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        intArrayLhs = new int[]{1, 2, 3};
        intArrayRhs = null;
        builder = new CompareToBuilder();
        builder.append(intArrayLhs, intArrayRhs);
        assertNull(comparison);

        long[] longArrayLhs = {1, 2, 3};
        long[] longArrayRhs = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(longArrayLhs, longArrayRhs);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        long[] longArrayLhs2 = {1, 2, 3};
        long[] longArrayRhs2 = {1, 2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(longArrayLhs2, longArrayRhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendWithObjects() {
        class TestClass {
            int field1 = 1;
            int field2 = 2;

            @Override
            public int compareTo(Object o) {
                TestClass other = (TestClass) o;
                return Integer.compare(field1, other.field1);
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.field1 = 4;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);

        lhs = new TestClass();
        rhs = lhs;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        rhs = new TestClass();
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertNull(comparison);
    }

    @Test
    public void testAppendWithCustomComparator() {
        class TestClass implements Comparable<TestClass> {
            int field1 = 1;
            int field2 = 2;

            @Override
            public int compareTo(TestClass other) {
                return Integer.compare(other.field2, this.field2);
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.field2 = 4;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new TestComparator());
        int comparison = builder.toComparison();
        assertEquals(1, comparison);

        lhs = new TestClass();
        rhs = lhs;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, new TestComparator());
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        rhs = new TestClass();
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, new TestComparator());
        assertNull(comparison);
    }

    @Test
    public void testAppendSuper() {
        class Child extends Parent {
            int childField = 1;
        }

        class Parent {
            int parentField = 2;
        }

        Parent lhs = new Parent();
        Parent rhs = new Child();

        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(lhs, rhs));
        int comparison = builder.toComparison();
        assertEquals(2, comparison);

        lhs = new Parent();
        rhs = new Child();
        builder = new CompareToBuilder();
        builder.appendSuper(-2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);

        lhs = new Child();
        rhs = new Parent();
        builder = new CompareToBuilder();
        builder.appendSuper(1);
        comparison = builder.toComparison();
        assertEquals(1, comparison);
    }
}

class TestComparator implements Comparator<TestClass> {
    @Override
    public int compare(TestClass o1, TestClass o2) {
        return Integer.compare(o2.field2, o1.field2);
    }
}