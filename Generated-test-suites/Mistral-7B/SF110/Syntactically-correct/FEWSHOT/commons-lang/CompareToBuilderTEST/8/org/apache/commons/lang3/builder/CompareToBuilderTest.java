package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompareToBuilderTest {
    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int i = 1;
            int j = 2;
        };
        Object rhs = new Object() {
            int i = 2;
            int j = 1;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
        };
        rhs = new Object() {
            int i = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new String("hello");
        rhs = new String("world");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new String("world");
        rhs = new String("hello");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        Object lhs = new ObjectWithExcludedFields() {
            int i = 1;
            int j = 2;
        };
        Object rhs = new ObjectWithExcludedFields() {
            int i = 2;
            int j = 1;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"i"});
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithCustomComparator() {
        Object lhs = new CustomComparableObject() {
            int i = 1;
        };
        Object rhs = new CustomComparableObject() {
            int i = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                CustomComparableObject c1 = (CustomComparableObject) o1;
                CustomComparableObject c2 = (CustomComparableObject) o2;
                return c1.i - c2.i;
            }
        });
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithTransients() {
        class TestClass {
            private int i;
            private transient int j;

            public TestClass(int i, int j) {
                this.i = i;
                this.j = j;
            }

            public int getI() {
                return i;
            }

            public int getJ() {
                return j;
            }
        }

        TestClass lhs = new TestClass(1, 1);
        TestClass rhs = new TestClass(1, 2);
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithSuperClass() {
        class SubClass extends ParentClass {
            int i;
        }

        class ParentClass {
            int j;
        }

        SubClass lhs = new SubClass() {
            int i = 1;
        };
        SubClass rhs = new SubClass() {
            int i = 2;
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(new ParentClass(), new ParentClass(), false, null, new String[0]));
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithArrays() {
        Object[] lhs = {1, 2, 3};
        Object[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithCustomComparatorForArrays() {
        Object[] lhs = {1, 2, 3};
        Object[] rhs = {1, 2, 3};
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                int[] a1 = (int[]) o1;
                int[] a2 = (int[]) o2;
                for (int i = 0; i < a1.length && i < a2.length; i++) {
                    if (a1[i] < a2[i]) {
                        return -1;
                    } else if (a1[i] > a2[i]) {
                        return 1;
                    }
                }
                if (a1.length < a2.length) {
                    return -1;
                } else if (a1.length > a2.length) {
                    return 1;
                }
                return 0;
            }
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs, comparator);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithLongArrays() {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithLongArraysWithDifferentLengths() {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 3, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithIntArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithIntArraysWithDifferentLengths() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithShortArrays() {
        short[] lhs = {1, 2, 3};
        short[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithShortArraysWithDifferentLengths() {
        short[] lhs = {1, 2, 3};
        short[] rhs = {1, 2, 3, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithCharArrays() {
        char[] lhs = {'h', 'e', 'l', 'l', 'o'};
        char[] rhs = {'h', 'e', 'l', 'l', 'o'};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithCharArraysWithDifferentLengths() {
        char[] lhs = {'h', 'e', 'l', 'l', 'o'};
        char[] rhs = {'h', 'e', 'l', 'l', 'o', 'w'};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithByteArrays() {
        byte[] lhs = {1, 2, 3};
        byte[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithByteArraysWithDifferentLengths() {
        byte[] lhs = {1, 2, 3};
        byte[] rhs = {1, 2, 3, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithDoubleArrays() {
        double[] lhs = {1.0, 2.0, 3.0};
        double[] rhs = {1.0, 2.0, 3.0};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithDoubleArraysWithDifferentLengths() {
        double[] lhs = {1.0, 2.0, 3.0};
        double[] rhs = {1.0, 2.0, 3.0, 4.0};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithFloatArrays() {
        float[] lhs = {1.0f, 2.0f, 3.0f};
        float[] rhs = {1.0f, 2.0f, 3.0f};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithFloatArraysWithDifferentLengths() {
        float[] lhs = {1.0f, 2.0f, 3.0f};
        float[] rhs = {1.0f, 2.0f, 3.0f, 4.0f};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithBooleanArrays() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, true};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithBooleanArraysWithDifferentLengths() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, true, false};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((Object[]) lhs, (Object[]) rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }
}

class ObjectWithExcludedFields {
    int i;
    int j;

    public ObjectWithExcludedFields(int i, int j) {
        this.i = i;
        this.j = j;
    }
}

class CustomComparableObject implements Comparable<CustomComparableObject> {
    int i;

    public CustomComparableObject(int i) {
        this.i = i;
    }

    @Override
    public int compareTo(CustomComparableObject o) {
        return this.i - o.i;
    }
}