package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    private static final int[] INTEGER_ARRAY_1 = new int[]{1, 2, 3};
    private static final int[] INTEGER_ARRAY_2 = new int[]{2, 3, 1};
    private static final int[] INTEGER_ARRAY_3 = new int[]{1, 2, 3, 4};
    private static final int[] INTEGER_ARRAY_4 = new int[]{};
    private static final int[] INTEGER_ARRAY_5 = null;

    private static final Comparable<Object> STRING_1 = new String("Hello");
    private static final Comparable<Object> STRING_2 = new String("World");
    private static final Comparable<Object> STRING_3 = new String("HelloWorld");
    private static final Comparable<Object> STRING_4 = null;

    private static final long[] LONG_ARRAY_1 = new long[]{1, 2, 3};
    private static final long[] LONG_ARRAY_2 = new long[]{2, 3, 1};
    private static final long[] LONG_ARRAY_3 = new long[]{1, 2, 3, 4};
    private static final long[] LONG_ARRAY_4 = new long[]{};
    private static final long[] LONG_ARRAY_5 = null;

    private static final double[] DOUBLE_ARRAY_1 = new double[]{1.0, 2.0, 3.0};
    private static final double[] DOUBLE_ARRAY_2 = new double[]{2.0, 3.0, 1.0};
    private static final double[] DOUBLE_ARRAY_3 = new double[]{1.0, 2.0, 3.0, 4.0};
    private static final double[] DOUBLE_ARRAY_4 = new double[]{};
    private static final double[] DOUBLE_ARRAY_5 = null;

    private static final float[] FLOAT_ARRAY_1 = new float[]{1.0f, 2.0f, 3.0f};
    private static final float[] FLOAT_ARRAY_2 = new float[]{2.0f, 3.0f, 1.0f};
    private static final float[] FLOAT_ARRAY_3 = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
    private static final float[] FLOAT_ARRAY_4 = new float[]{};
    private static final float[] FLOAT_ARRAY_5 = null;

    private static final boolean[] BOOLEAN_ARRAY_1 = new boolean[]{true, false, true};
    private static final boolean[] BOOLEAN_ARRAY_2 = new boolean[]{false, true, true};
    private static final boolean[] BOOLEAN_ARRAY_3 = new boolean[]{true, false, true, false};
    private static final boolean[] BOOLEAN_ARRAY_4 = new boolean[]{};
    private static final boolean[] BOOLEAN_ARRAY_5 = null;

    @Test
    public void testReflectionCompare_IntegerObjects() {
        int comparison = CompareToBuilder.reflectionCompare(INTEGER_ARRAY_1, INTEGER_ARRAY_1);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(INTEGER_ARRAY_1, INTEGER_ARRAY_2);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(INTEGER_ARRAY_1, INTEGER_ARRAY_3);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(INTEGER_ARRAY_1, INTEGER_ARRAY_4);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(INTEGER_ARRAY_1, null);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(INTEGER_ARRAY_1, INTEGER_ARRAY_5);
        Assert.assertEquals(Integer.MIN_VALUE, comparison);
    }

    @Test
    public void testReflectionCompare_StringObjects() {
        int comparison = CompareToBuilder.reflectionCompare(STRING_1, STRING_1);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(STRING_1, STRING_2);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(STRING_1, STRING_3);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(STRING_1, null);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(STRING_1, STRING_5);
        Assert.assertEquals(Integer.MIN_VALUE, comparison);
    }

    @Test
    public void testReflectionCompare_LongArrays() {
        int comparison = CompareToBuilder.reflectionCompare(LONG_ARRAY_1, LONG_ARRAY_1);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(LONG_ARRAY_1, LONG_ARRAY_2);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(LONG_ARRAY_1, LONG_ARRAY_3);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(LONG_ARRAY_1, LONG_ARRAY_4);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(LONG_ARRAY_1, null);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(LONG_ARRAY_1, LONG_ARRAY_5);
        Assert.assertEquals(Long.MIN_VALUE, comparison);
    }

    @Test
    public void testReflectionCompare_DoubleArrays() {
        int comparison = CompareToBuilder.reflectionCompare(DOUBLE_ARRAY_1, DOUBLE_ARRAY_1);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(DOUBLE_ARRAY_1, DOUBLE_ARRAY_2);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(DOUBLE_ARRAY_1, DOUBLE_ARRAY_3);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(DOUBLE_ARRAY_1, DOUBLE_ARRAY_4);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(DOUBLE_ARRAY_1, null);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(DOUBLE_ARRAY_1, DOUBLE_ARRAY_5);
        Assert.assertEquals(Double.MIN_VALUE, comparison);
    }

    @Test
    public void testReflectionCompare_FloatArrays() {
        int comparison = CompareToBuilder.reflectionCompare(FLOAT_ARRAY_1, FLOAT_ARRAY_1);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(FLOAT_ARRAY_1, FLOAT_ARRAY_2);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(FLOAT_ARRAY_1, FLOAT_ARRAY_3);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(FLOAT_ARRAY_1, FLOAT_ARRAY_4);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(FLOAT_ARRAY_1, null);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(FLOAT_ARRAY_1, FLOAT_ARRAY_5);
        Assert.assertEquals(Float.MIN_VALUE, comparison);
    }

    @Test
    public void testReflectionCompare_BooleanArrays() {
        int comparison = CompareToBuilder.reflectionCompare(BOOLEAN_ARRAY_1, BOOLEAN_ARRAY_1);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(BOOLEAN_ARRAY_1, BOOLEAN_ARRAY_2);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(BOOLEAN_ARRAY_1, BOOLEAN_ARRAY_3);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(BOOLEAN_ARRAY_1, BOOLEAN_ARRAY_4);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(BOOLEAN_ARRAY_1, null);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(BOOLEAN_ARRAY_1, BOOLEAN_ARRAY_5);
        Assert.assertEquals(Integer.MIN_VALUE, comparison);
    }

    @Test
    public void testReflectionCompare_WithExcludedFields() {
        String[] excludedFields = new String[]{"excludedField"};
        int comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, new String[0], excludedFields);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, new String[]{"excludedField"}, excludedFields);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, new String[]{"anotherField"}, excludedFields);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, new String[]{"allFields"}, excludedFields);
        Assert.assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompare_WithTransients() {
        String[] excludedFields = new String[0];
        int comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, false, null, excludedFields);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, _transientField}, new Object[]{1, 2, 3}, false, null, excludedFields);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, true, null, excludedFields);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, _transientField}, new Object[]{1, 2, 3}, true, null, excludedFields);
        Assert.assertEquals(0, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_NullObjects() {
        CompareToBuilder.reflectionCompare(null, null);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_DifferentTypes() {
        CompareToBuilder.reflectionCompare(new Integer(1), new String("Hello"));
    }

    @Test
    public void testReflectionCompare_WithExcludedSuperClass() {
        Class<?> reflectUpToClass = Object.class;
        int comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2}, false, reflectUpToClass.getSuperclass(), new String[0]);
        Assert.assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, false, reflectUpToClass.getSuperclass(), new String[0]);
        Assert.assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, false, null, new String[0]);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, false, null, new String[]{"fieldInSuperclass"});
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, false, Object.class, new String[0]);
        Assert.assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(new Object[]{1, 2}, new Object[]{1, 2, 3}, false, Object.class, new String[]{"fieldInSuperclass"});
        Assert.assertEquals(0, comparison);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionCompare_InvalidArguments() {
        CompareToBuilder.reflectionCompare(null, new Object[]{1, 2});
    }
}