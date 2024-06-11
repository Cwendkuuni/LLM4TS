package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };
        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new Object() {
            int a = 1;
            int b = 1;
        };
        rhs = new Object() {
            int a = 1;
            int b = 1;
        };

        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new Object() {
            int a = null;
            int b = 1;
        };
        rhs = new Object() {
            int a = 1;
            int b = null;
        };

        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
        } catch (NullPointerException e) {
            result = -1; // NullPointerException is expected
        }
        Assert.assertEquals(-1, result);

        lhs = new Object() {
            int a = new Object();
            int b = new Object();
        };
        rhs = new Object() {
            int a = new Object();
            int b = new Object();
        };

        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
        } catch (ClassCastException e) {
            result = -1; // ClassCastException is expected
        }
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
            private int c = 3;
        };
        Object rhs = new Object() {
            int a = 2;
            int b = 1;
            private int c = 3;
        };

        int result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("c"));
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReflectionCompareWithTransients() {
        Object lhs = new Object() {
            transient int a = 1;
            int b = 2;
        };
        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        Assert.assertEquals(-1, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, true, Object.class, new String[0]);
        Assert.assertEquals(-1, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, true, Object.class, "a");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAppendMethods() {
        CompareToBuilder builder = new CompareToBuilder();

        int[] intArray = {1, 2, 3};
        Object[] objectArray = {1, 2, 3};
        long[] longArray = {1L, 2L, 3L};
        float[] floatArray = {1.0f, 2.0f, 3.0f};
        double[] doubleArray = {1.0, 2.0, 3.0};
        boolean[] booleanArray = {true, false, true};

        builder.append(1, 2);
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(1, 1);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(1, null);
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(null, 1);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(new int[] {1, 2}, new int[] {2, 1});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new int[] {1, 2}, new int[] {1, 2});
        Assert.assertEquals(0, builder.toComparison());

        builder.append(new int[] {1, null}, new int[] {1, 2});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new int[] {1, 2}, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(new long[] {1L, 2L}, new long[] {2L, 1L});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new long[] {1L, 2L}, new long[] {1L, 2L});
        Assert.assertEquals(0, builder.toComparison());

        builder.append(new long[] {1L, null}, new long[] {1L, 2L});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new long[] {1L, 2L}, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(new float[] {1.0f, 2.0f}, new float[] {2.0f,
                1.0f});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new float[] {1.0f, 2.0f}, new float[] {1.0f, 2.0f});
        Assert.assertEquals(0, builder.toComparison());

        builder.append(new float[] {1.0f, null}, new float[] {1.0f, 2.0f});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new float[] {1.0f, 2.0f}, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(new double[] {1.0, 2.0}, new double[] {2.0, 1.0});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new double[] {1.0, 2.0}, new double[] {1.0, 2.0});
        Assert.assertEquals(0, builder.toComparison());

        builder.append(new double[] {1.0, null}, new double[] {1.0, 2.0});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new double[] {1.0, 2.0}, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(new boolean[] {true, false}, new boolean[] {false,
                true});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new boolean[] {true, false}, new boolean[] {true,
                false});
        Assert.assertEquals(0, builder.toComparison());

        builder.append(new boolean[] {true, null}, new boolean[] {true,
                false});
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(new boolean[] {true, false}, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(intArray, objectArray);
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(intArray, intArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(intArray, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(longArray, longArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(longArray, longArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(longArray, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(floatArray, floatArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(floatArray, floatArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(floatArray, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(doubleArray, doubleArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(doubleArray, doubleArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(doubleArray, null);
        Assert.assertEquals(1, builder.toComparison());

        builder.append(booleanArray, booleanArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(booleanArray, booleanArray);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(booleanArray, null);
        Assert.assertEquals(1, builder.toComparison());

        // Test appendSuper
        CompareToBuilder subClassBuilder = new CompareToBuilder();
        int subClassCompareTo = subClassBuilder.appendSuper(10).toComparison();
        CompareToBuilder superClassBuilder = new CompareToBuilder();
        int superClassCompareTo = superClassBuilder.appendSuper(5).toComparison();
        CompareToBuilder builderWithSuper = new CompareToBuilder();
        builderWithSuper.appendSuper(subClassCompareTo).appendSuper(superClassCompareTo);
        Assert.assertEquals(5 + 10, builderWithSuper.toComparison());
    }

    @Test
    public void testAppendSuperWithNegativeValues() {
        CompareToBuilder subClassBuilder = new CompareToBuilder();
        int subClassCompareTo = subClassBuilder.appendSuper(-10).toComparison();
        CompareToBuilder superClassBuilder = new CompareToBuilder();
        int superClassCompareTo = superClassBuilder.appendSuper(-5).toComparison();
        CompareToBuilder builderWithSuper = new CompareToBuilder();
        builderWithSuper.appendSuper(subClassCompareTo).appendSuper(superClassCompareTo);
        Assert.assertEquals(-5 - 10, builderWithSuper.toComparison());
    }

    @Test
    public void testAppendSuperWithNull() {
        CompareToBuilder subClassBuilder = new CompareToBuilder();
        int subClassCompareTo = subClassBuilder.appendSuper(null).toComparison();
        CompareToBuilder builderWithSuper = new CompareToBuilder();
        builderWithSuper.appendSuper(subClassCompareTo);
        Assert.assertEquals(Integer.MIN_VALUE, builderWithSuper.toComparison());
    }

    @Test
    public void testAppendSuperWithNonIntValues() {
        CompareToBuilder subClassBuilder = new CompareToBuilder();
        Integer subClassCompareTo = subClassBuilder.appendSuper(new Integer(10)).toComparison();
        CompareToBuilder superClassBuilder = new CompareToBuilder();
        Integer superClassCompareTo = superClassBuilder.appendSuper(new Integer(5)).toComparison();
        CompareToBuilder builderWithSuper = new CompareToBuilder();
        builderWithSuper.appendSuper(subClassCompareTo).appendSuper(superClassCompareTo);
        Assert.assertEquals(new Integer(5).intValue() + new Integer(10).intValue(), builderWithSuper.toComparison());
    }
}