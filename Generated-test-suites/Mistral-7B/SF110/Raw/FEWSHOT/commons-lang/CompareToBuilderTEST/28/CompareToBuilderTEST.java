package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object a = new Object() {
            int i = 1;
            int j = 2;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Object that = (Object) o;
                return this.i == that.i && this.j == that.j;
            }
        };

        Object b = new Object() {
            int i = 2;
            int j = 1;
        };

        int comparison = CompareToBuilder.reflectionCompare(a, b);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(b, a);
        assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(a, a);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(null, a);
        assertNull(comparison);

        comparison = CompareToBuilder.reflectionCompare(a, null);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        Object a = new Object() {
            int i = 1;
            int j = 2;
            String excludeField = "excluded";

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Object that = (Object) o;
                return this.i == that.i && this.j == that.j;
            }
        };

        Object b = new Object() {
            int i = 2;
            int j = 1;
        };

        int comparison = CompareToBuilder.reflectionCompare(a, b, "excludeField");
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(a, b);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(b, a);
        assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithSuperClass() {
        Object a = new SubClass1();
        Object b = new SubClass2();

        int comparison = CompareToBuilder.reflectionCompare(a, b, SubClass1.class);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(a, b);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(b, a);
        assertEquals(1, comparison);
    }

    static class SubClass1 extends ParentClass {
        int x = 1;
    }

    static class SubClass2 extends ParentClass {
        int x = 2;
    }

    static class ParentClass {
        int y = 0;
    }

    @Test
    public void testAppend() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();

        int i = 1;
        int j = 2;
        compareToBuilder.append(i, j);
        assertEquals(-1, compareToBuilder.comparison);

        j = 1;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(i, j);
        assertEquals(0, compareToBuilder.comparison);

        i = 2;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(i, j);
        assertEquals(1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(null, j);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(i, null);
        assertEquals(1, compareToBuilder.comparison);

        char c1 = 'a';
        char c2 = 'b';
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(c1, c2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(c2, c1);
        assertEquals(1, compareToBuilder.comparison);

        byte[] byteArray1 = {1, 2, 3};
        byte[] byteArray2 = {2, 3, 1};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(byteArray1, byteArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(byteArray2, byteArray1);
        assertEquals(1, compareToBuilder.comparison);

        int[] intArray1 = {1, 2, 3};
        int[] intArray2 = {2, 3, 1};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(intArray1, intArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(intArray2, intArray1);
        assertEquals(1, compareToBuilder.comparison);

        short[] shortArray1 = {1, 2, 3};
        short[] shortArray2 = {2, 3, 1};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(shortArray1, shortArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(shortArray2, shortArray1);
        assertEquals(1, compareToBuilder.comparison);

        char[] charArray1 = {'a', 'b', 'c'};
        char[] charArray2 = {'b', 'c', 'a'};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(charArray1, charArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(charArray2, charArray1);
        assertEquals(1, compareToBuilder.comparison);

        boolean[] booleanArray1 = {true, false, true};
        boolean[] booleanArray2 = {false, true, false};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(booleanArray1, booleanArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(booleanArray2, booleanArray1);
        assertEquals(1, compareToBuilder.comparison);

        double[] doubleArray1 = {1.0, 2.0, 3.0};
        double[] doubleArray2 = {2.0, 3.0, 1.0};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(doubleArray1, doubleArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(doubleArray2, doubleArray1);
        assertEquals(1, compareToBuilder.comparison);

        float[] floatArray1 = {1.0f, 2.0f, 3.0f};
        float[] floatArray2 = {2.0f, 3.0f, 1.0f};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(floatArray1, floatArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(floatArray2, floatArray1);
        assertEquals(1, compareToBuilder.comparison);

        long[] longArray1 = {1, 2, 3};
        long[] longArray2 = {2, 3, 1};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(longArray1, longArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(longArray2, longArray1);
        assertEquals(1, compareToBuilder.comparison);

        int[][] int2DArray1 = {{1, 2}, {3, 4}, {5, 6}};
        int[][] int2DArray2 = {{2, 3}, {4, 5}, {6, 5}};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(int2DArray1, int2DArray2);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(int2DArray2, int2DArray1);
        assertEquals(1, compareToBuilder.comparison);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int i = 1;
        int j = 2;
        CompareToBuilder appendSuper = compareToBuilder.appendSuper(1);
        appendSuper.append(i, j);
        assertEquals(1, appendSuper.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(2);
        compareToBuilder.append(i, j);
        assertEquals(1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(0);
        compareToBuilder.append(i, j);
        assertEquals(-1, compareToBuilder.comparison);
    }

    @Test
    public void testAppendWithComparator() {
        Comparator<Object> comparator = (lhs, rhs) -> ((Comparable<Object>) lhs).compareTo(rhs);
        Object a = new Object() {
            int i = 1;
            int j = 2;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Object that = (Object) o;
                return this.i == that.i && this.j == that.j;
            }

            @Override
            public int compareTo(Object o) {
                Object that = (Object) o;
                return this.i - that.i;
            }
        };

        Object b = new Object() {
            int i = 3;
            int j = 2;
        };

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, b, comparator);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(b, a, comparator);
        assertEquals(1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, a, comparator);
        assertEquals(0, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(null, b, comparator);
        assertNull(compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, null, comparator);
        assertEquals(-1, compareToBuilder.comparison);
    }

    @Test
    public void testAppendWithArrayAndComparator() {
        Comparator<Object> comparator = (lhs, rhs) -> ((Comparable<Object>) lhs[0]).compareTo(rhs[0]);
        Object[] a = new Object[]{new Object() {
            int i = 1;
            int j = 2;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Object that = (Object) o;
                return this.i == that.i && this.j == that.j;
            }

            @Override
            public int compareTo(Object o) {
                Object that = (Object) o;
                return this.i - that.i;
            }
        }, new Object() {
            int i = 3;
            int j = 2;
        }};

        Object[] b = new Object[]{new Object() {
            int i = 2;
            int j = 3;
        }, new Object() {
            int i = 4;
            int j = 3;
        }};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, b, comparator);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(b, a, comparator);
        assertEquals(1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, a, comparator);
        assertEquals(0, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(null, b, comparator);
        assertNull(compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, null, comparator);
        assertEquals(-1, compareToBuilder.comparison);
    }

    @Test
    public void testAppendWithArrayAndComparatorAndExcludeFields() {
        Comparator<Object> comparator = (lhs, rhs) -> ((Comparable<Object>) ((Object) lhs[0]).getClass().getDeclaredField("i").get(lhs[0])).compareTo(rhs[0]);
        Object[] a = new Object[]{new Object() {
            int i = 1;
            int j = 2;
            String excludeField = "j";

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Object that = (Object) o;
                return this.i == that.i;
            }

            @Override
            public int compareTo(Object o) {
                Object that = (Object) o;
                return this.i - that.i;
            }
        }, new Object() {
            int i = 3;
            int j = 2;
        }};

        Object[] b = new Object[]{new Object() {
            int i = 2;
            int j = 3;
        }, new Object() {
            int i = 4;
            int j = 3;
        }};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, b, comparator, new String[]{"j"});
        assertEquals(0, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, b, comparator);
        assertEquals(-1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(b, a, comparator);
        assertEquals(1, compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(null, b, comparator);
        assertNull(compareToBuilder.comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(a, null, comparator);
        assertEquals(-1, compareToBuilder.comparison);
    }
}