package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() throws Exception {
        Object lhs = new Object() {
            int value = 5;
        };
        Object rhs = new Object() {
            int value = 10;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-5, comparison);

        lhs = new Object() {
            int value = 10;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(5, comparison);

        lhs = null;
        rhs = new Object() {
            int value = 10;
        };
        Assert.assertNull(CompareToBuilder.reflectionCompare(lhs, rhs));

        lhs = new Object() {
            int value = 10;
        };
        rhs = null;
        Assert.assertNull(CompareToBuilder.reflectionCompare(lhs, rhs));
    }

    @Test
    public void testReflectionCompare_compareTransients() throws Exception {
        class TestClass {
            @SuppressWarnings("unused")
            private transient int transientField;

            int nonTransientField = 5;
        }

        Object lhs = new TestClass();
        Object rhs = new TestClass();
        ((TestClass) lhs).nonTransientField = 10;
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true, TestClass.class, new String[0]);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true, TestClass.class, new String[]{"transientField"});
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_excludeFields() throws Exception {
        class TestClass {
            int field1 = 5;
            int field2 = 10;

            void setField3(int value) {
                this.field3 = value;
            }

            int field3;
        }

        Object lhs = new TestClass();
        Object rhs = new TestClass();
        ((TestClass) lhs).field1 = 1;
        ((TestClass) lhs).field2 = 10;
        ((TestClass) rhs).field1 = 1;
        ((TestClass) rhs).field2 = 5;

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, TestClass.class, new String[]{"field3"});
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, TestClass.class, new String[]{"field3", "field1"});
        Assert.assertEquals(-5, comparison);
    }

    @Test
    public void testCompareToBuilder_append() {
        CompareToBuilder builder = new CompareToBuilder();

        builder.append(5, 10);
        Assert.assertEquals(-5, builder.toComparison());

        builder.append(10, 5);
        Assert.assertEquals(5, builder.toComparison());

        builder.append((short) 5, (short) 10);
        Assert.assertEquals(-5, builder.toComparison());

        builder.append((short) 10, (short) 5);
        Assert.assertEquals(5, builder.toComparison());

        builder.append('a', 'z');
        Assert.assertEquals(-1, builder.toComparison());

        builder.append('z', 'a');
        Assert.assertEquals(1, builder.toComparison());

        builder.append(true, false);
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(false, true);
        Assert.assertEquals(1, builder.toComparison());

        Object[] array1 = {1, 2, 3};
        Object[] array2 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(array1, array2);
        Assert.assertEquals(0, builder.toComparison());

        array2 = new Object[3];
        array2[0] = 5;
        array2[1] = 10;
        array2[2] = 15;
        builder = new CompareToBuilder();
        builder.append(array1, array2);
        Assert.assertEquals(-2, builder.toComparison());

        Comparator<Object> comparator = Comparator.comparingInt(o -> ((Number) o).intValue());
        builder = new CompareToBuilder();
        builder.append(array1, array2, comparator);
        Assert.assertEquals(-2, builder.toComparison());

        long[] longArray1 = {1, 2, 3};
        long[] longArray2 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(longArray1, longArray2);
        Assert.assertEquals(0, builder.toComparison());

        longArray2 = new long[3];
        longArray2[0] = 5;
        longArray2[1] = 10;
        longArray2[2] = 15;
        builder = new CompareToBuilder();
        builder.append(longArray1, longArray2);
        Assert.assertEquals(-4, builder.toComparison());

        int[] intArray1 = {1, 2, 3};
        int[] intArray2 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(intArray1, intArray2);
        Assert.assertEquals(0, builder.toComparison());

        intArray2 = new int[3];
        intArray2[0] = 5;
        intArray2[1] = 10;
        intArray2[2] = 15;
        builder = new CompareToBuilder();
        builder.append(intArray1, intArray2);
        Assert.assertEquals(-3, builder.toComparison());

        short[] shortArray1 = {1, 2, 3};
        short[] shortArray2 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(shortArray1, shortArray2);
        Assert.assertEquals(0, builder.toComparison());

        shortArray2 = new short[3];
        shortArray2[0] = 5;
        shortArray2[1] = 10;
        shortArray2[2] = 15;
        builder = new CompareToBuilder();
        builder.append(shortArray1, shortArray2);
        Assert.assertEquals(-3, builder.toComparison());

        char[] charArray1 = {'a', 'b', 'c'};
        char[] charArray2 = {'a', 'b', 'c'};
        builder = new CompareToBuilder();
        builder.append(charArray1, charArray2);
        Assert.assertEquals(0, builder.toComparison());

        charArray2 = new char[3];
        charArray2[0] = 'z';
        charArray2[1] = 'e';
        charArray2[2] = 'c';
        builder = new CompareToBuilder();
        builder.append(charArray1, charArray2);
        Assert.assertEquals(-3, builder.toComparison());

        byte[] byteArray1 = {1, 2, 3};
        byte[] byteArray2 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(byteArray1, byteArray2);
        Assert.assertEquals(0, builder.toComparison());

        byteArray2 = new byte[3];
        byteArray2[0] = 5;
        byteArray2[1] = 10;
        byteArray2[2] = 15;
        builder = new CompareToBuilder();
        builder.append(byteArray1, byteArray2);
        Assert.assertEquals(-4, builder.toComparison());

        double[] doubleArray1 = {1.0, 2.0, 3.0};
        double[] doubleArray2 = {1.0, 2.0, 3.0};
        builder = new CompareToBuilder();
        builder.append(doubleArray1, doubleArray2);
        Assert.assertEquals(0, builder.toComparison());

        doubleArray2 = new double[3];
        doubleArray2[0] = 5.5;
        doubleArray2[1] = 10.5;
        doubleArray2[2] = 15.5;
        builder = new CompareToBuilder();
        builder.append(doubleArray1, doubleArray2);
        Assert.assertEquals(-3.5, builder.toComparison());

        float[] floatArray1 = {1.0f, 2.0f, 3.0f};
        float[] floatArray2 = {1.0f, 2.0f, 3.0f};
        builder = new CompareToBuilder();
        builder.append(floatArray1, floatArray2);
        Assert.assertEquals(0, builder.toComparison());

        floatArray2 = new float[3];
        floatArray2[0] = 5.5f;
        floatArray2[1] = 10.5f;
        floatArray2[2] = 15.5f;
        builder = new CompareToBuilder();
        builder.append(floatArray1, floatArray2);
        Assert.assertEquals(-3.5f, builder.toComparison());

        boolean[] booleanArray1 = {true, false, true};
        boolean[] booleanArray2 = {true, false, true};
        builder = new CompareToBuilder();
        builder.append(booleanArray1, booleanArray2);
        Assert.assertEquals(0, builder.toComparison());

        booleanArray2 = new boolean[3];
        booleanArray2[0] = true;
        booleanArray2[1] = false;
        booleanArray2[2] = true;
        builder = new CompareToBuilder();
        builder.append(booleanArray1, booleanArray2);
        Assert.assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testCompareToBuilder_appendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        Assert.assertEquals(5, builder.toComparison());
    }
}