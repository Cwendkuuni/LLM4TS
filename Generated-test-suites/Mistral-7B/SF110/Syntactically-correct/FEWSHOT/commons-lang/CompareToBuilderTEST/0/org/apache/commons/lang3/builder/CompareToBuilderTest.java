package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testCompareObjects() {
        Object obj1 = new Object() {
            int value = 1;
        };
        Object obj2 = new Object() {
            int value = 2;
        };

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        assertEquals(-1, compareToBuilder.toComparison());

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj2, obj1);
        assertEquals(1, compareToBuilder.toComparison());

        obj1 = new Object() {
            int value = 3;
        };
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysInt() {
        int[] arr1 = new int[]{1, 2, 3};
        int[] arr2 = new int[]{1, 2, 3};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new int[]{1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new int[]{1, 2, 3, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysLong() {
        long[] arr1 = new long[]{1, 2, 3};
        long[] arr2 = new long[]{1, 2, 3};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new long[]{1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new long[]{1, 2, 3, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysShort() {
        short[] arr1 = new short[]{1, 2, 3};
        short[] arr2 = new short[]{1, 2, 3};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new short[]{1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new short[]{1, 2, 3, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysChar() {
        char[] arr1 = new char[]{'a', 'b', 'c'};
        char[] arr2 = new char[]{'a', 'b', 'c'};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new char[]{'a', 'b', 'd'};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new char[]{'a', 'b', 'c', 'd'};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysByte() {
        byte[] arr1 = new byte[]{1, 2, 3};
        byte[] arr2 = new byte[]{1, 2, 3};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new byte[]{1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new byte[]{1, 2, 3, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysDouble() {
        double[] arr1 = new double[]{1.0, 2.0, 3.0};
        double[] arr2 = new double[]{1.0, 2.0, 3.0};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new double[]{1.0, 2.0, 4.0};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new double[]{1.0, 2.0, 3.0, 4.0};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysFloat() {
        float[] arr1 = new float[]{1.0f, 2.0f, 3.0f};
        float[] arr2 = new float[]{1.0f, 2.0f, 3.0f};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new float[]{1.0f, 2.0f, 4.0f};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareArraysBoolean() {
        boolean[] arr1 = new boolean[]{true, false, true};
        boolean[] arr2 = new boolean[]{true, false, true};

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(0, compareToBuilder.toComparison());

        arr2 = new boolean[]{true, false, false};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(-1, compareToBuilder.toComparison());

        arr1 = new boolean[]{true, false, true, true};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(arr1, arr2);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareWithExcludeFields() {
        class TestObject {
            private int field1 = 1;
            private int field2 = 2;

            private int excludedField = 3;

            @Override
            public String toString() {
                return "TestObject{" +
                       "field1=" + field1 +
                       ", field2=" + field2 +
                       '}';
            }
        }

        TestObject obj1 = new TestObject();
        TestObject obj2 = new TestObject();

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, new String[]{"excludedField"});
        assertEquals(0, compareToBuilder.toComparison());

        obj1.excludedField = 4;
        obj2.excludedField = 5;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, new String[]{"excludedField"});
        assertEquals(0, compareToBuilder.toComparison());

        obj1.field1 = 3;
        obj2.field1 = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, new String[]{"excludedField"});
        assertEquals(0, compareToBuilder.toComparison());

        obj1.field1 = 3;
        obj2.field1 = 4;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, new String[]{"excludedField"});
        assertEquals(-1, compareToBuilder.toComparison());

        obj1.field1 = 4;
        obj2.field1 = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, new String[]{"excludedField"});
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareWithTransients() {
        class TestObject {
            private int field1 = 1;
            private int field2 = 2;

            private transient int field3 = 3;

            @Override
            public String toString() {
                return "TestObject{" +
                       "field1=" + field1 +
                       ", field2=" + field2 +
                       ", field3=" + field3 +
                       '}';
            }
        }

        TestObject obj1 = new TestObject();
        TestObject obj2 = new TestObject();

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, false);
        assertEquals(0, compareToBuilder.toComparison());

        obj1.field1 = 3;
        obj2.field1 = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, false);
        assertEquals(0, compareToBuilder.toComparison());

        obj1.field1 = 3;
        obj2.field1 = 4;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, false);
        assertEquals(-1, compareToBuilder.toComparison());

        obj1.field1 = 4;
        obj2.field1 = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, false);
        assertEquals(1, compareToBuilder.toComparison());

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, true);
        assertEquals(0, compareToBuilder.toComparison());

        obj1.field3 = 4;
        obj2.field3 = 5;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, true);
        assertEquals(0, compareToBuilder.toComparison());

        obj1.field3 = 5;
        obj2.field3 = 4;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2, true);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testCompareWithSuperClass() {
        class BaseClass {
            private int baseField = 1;
        }

        class SubClass extends BaseClass {
            private int subField = 2;
        }

        SubClass obj1 = new SubClass();
        SubClass obj2 = new SubClass();

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(reflectionCompare(obj1, obj2, SubClass.class));
        assertEquals(0, compareToBuilder.toComparison());

        obj1.baseField = 3;
        obj2.baseField = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(reflectionCompare(obj1, obj2, SubClass.class));
        assertEquals(0, compareToBuilder.toComparison());

        obj1.baseField = 3;
        obj2.baseField = 4;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(reflectionCompare(obj1, obj2, SubClass.class));
        assertEquals(-1, compareToBuilder.toComparison());

        obj1.baseField = 4;
        obj2.baseField = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(reflectionCompare(obj1, obj2, SubClass.class));
        assertEquals(1, compareToBuilder.toComparison());

        obj1.subField = 3;
        obj2.subField = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        assertEquals(0, compareToBuilder.toComparison());

        obj1.subField = 3;
        obj2.subField = 4;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        assertEquals(-1, compareToBuilder.toComparison());

        obj1.subField = 4;
        obj2.subField = 3;
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(obj1, obj2);
        assertEquals(1, compareToBuilder.toComparison());
    }
}