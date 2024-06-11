package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {};
        Object rhs = new Object() {};
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new Object() {
            int i = 1;
        };
        rhs = new Object() {
            int i = 2;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
        };
        rhs = new Object() {
            int i = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(0, comparison);

        lhs = new String[]{"a", "b"};
        rhs = new String[]{"a", "c"};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(-1, comparison);

        lhs = new String[]{"a", "b", "c"};
        rhs = new String[]{"a", "b", "c"};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(0, comparison);

        lhs = new String[]{"a", "b", "c"};
        rhs = new String[]{"a", "b", "c", "d"};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(-1, comparison);

        lhs = new String[]{"a", "b", "c", "d"};
        rhs = new String[]{"a", "b", "c", "d"};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(0, comparison);

        lhs = new String[]{"a", "b", "c", "d"};
        rhs = new String[]{"a", "b", "c", "d", "e"};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(-1, comparison);

        lhs = new String[]{"a", "b", "c", "d", "e"};
        rhs = new String[]{"a", "b", "c", "d", "e"};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testAppend() {
        ComparisonBuilder builder = new CompareToBuilder();
        builder.append(1, 2);
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(1, 1);
        Assert.assertEquals(0, builder.toComparison());

        builder.append(1, null);
        Assert.assertEquals(-1, builder.toComparison());

        builder.append(null, 1);
        Assert.assertEquals(1, builder.toComparison());

        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(arr1, arr2);
        Assert.assertEquals(0, builder.toComparison());

        arr1 = new int[]{1, 2, 3};
        arr2 = new int[]{1, 2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(arr1, arr2);
        Assert.assertEquals(-1, builder.toComparison());

        arr1 = new int[]{1, 2, 3};
        arr2 = new int[]{1, 2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(arr1, arr2, null);
        Assert.assertEquals(-1, builder.toComparison());

        arr1 = new int[]{1, 2, 3};
        arr2 = new int[]{1, 2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(arr1, arr2, (Comparator<int[]>) Arrays::compare);
        Assert.assertEquals(-1, builder.toComparison());

        long[] longArr1 = {1, 2, 3};
        long[] longArr2 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(longArr1, longArr2);
        Assert.assertEquals(0, builder.toComparison());

        longArr1 = new long[]{1, 2, 3};
        longArr2 = new long[]{1, 2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(longArr1, longArr2);
        Assert.assertEquals(-1, builder.toComparison());

        double[] doubleArr1 = {1.0, 2.0, 3.0};
        double[] doubleArr2 = {1.0, 2.0, 3.0};
        builder = new CompareToBuilder();
        builder.append(doubleArr1, doubleArr2);
        Assert.assertEquals(0, builder.toComparison());

        double[] doubleArr1 = {1.0, 2.0, 3.0};
        double[] doubleArr2 = {1.0, 2.0, 3.0, 4.0};
        builder = new CompareToBuilder();
        builder.append(doubleArr1, doubleArr2);
        Assert.assertEquals(-1, builder.toComparison());

        char[] charArr1 = {'a', 'b', 'c'};
        char[] charArr2 = {'a', 'b', 'c'};
        builder = new CompareToBuilder();
        builder.append(charArr1, charArr2);
        Assert.assertEquals(0, builder.toComparison());

        char[] charArr1 = {'a', 'b', 'c'};
        char[] charArr2 = {'a', 'b', 'c', 'd'};
        builder = new CompareToBuilder();
        builder.append(charArr1, charArr2);
        Assert.assertEquals(-1, builder.toComparison());

        boolean[] booleanArr1 = {true, false, true};
        boolean[] booleanArr2 = {true, false, true};
        builder = new CompareToBuilder();
        builder.append(booleanArr1, booleanArr2);
        Assert.assertEquals(0, builder.toComparison());

        boolean[] booleanArr1 = {true, false, true};
        boolean[] booleanArr2 = {true, false, true, false};
        builder = new CompareToBuilder();
        builder.append(booleanArr1, booleanArr2);
        Assert.assertEquals(-1, builder.toComparison());

        boolean[] booleanArr1 = {true, false, true};
        boolean[] booleanArr2 = {true, false, true, false};
        builder = new CompareToBuilder();
        builder.append(booleanArr1, booleanArr2, null);
        Assert.assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendSuper() {
        ComparisonBuilder builder = new CompareToBuilder();
        builder.appendSuper(1);
        Assert.assertEquals(1, builder.toComparison());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenComparingNullObjects() {
        ComparisonBuilder builder = new CompareToBuilder();
        builder.append(null, null);
    }

    @Test(expected = ClassCastException.class)
    public void testClassCastExceptionWhenComparingDifferentClasses() {
        ComparisonBuilder builder = new CompareToBuilder();
        builder.append(new Object() {}, new String(""));
    }
}