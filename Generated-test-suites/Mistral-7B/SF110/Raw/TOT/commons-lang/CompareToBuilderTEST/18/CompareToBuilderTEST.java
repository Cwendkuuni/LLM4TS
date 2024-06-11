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
    public void testReflectionCompare_Objects() {
        Object lhs = new Object();
        Object rhs = new Object();
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new Object();
        rhs = null;
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        lhs = null;
        rhs = new Object();
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        lhs = new String("hello");
        rhs = new String("hello");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new String("hello");
        rhs = new String("world");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new String("world");
        rhs = new String("hello");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testReflectionCompare_Collections() {
        Object[] lhs = new Object[]{1, 2, 3};
        Object[] rhs = new Object[]{1, 2, 3};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 4};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 3, 4};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new Object[]{1, 2, null};
        rhs = new Object[]{1, 2, null};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new Object[]{1, 2, null};
        rhs = new Object[]{1, 2, 3};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new Object[]{1, 2, null};
        rhs = new Object[]{1, 2, null};
        result = CompareToBuilder.reflectionCompare(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReflectionCompare_Arrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new int[]{1, 2, 3};
        rhs = new int[]{1, 2, 4};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new int[]{1, 2, 3};
        rhs = new int[]{1, 2, 3, 4};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new int[]{1, 2, null};
        rhs = new int[]{1, 2, null};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new int[]{1, 2, null};
        rhs = new int[]{1, 2, 3};
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new int[]{1, 2, null};
        rhs = new int[]{1, 2, null};
        result = CompareToBuilder.reflectionCompare(lhs, rhs, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int length1 = o1.length;
                int length2 = o2.length;
                int minLength = Math.min(length1, length2);
                for (int i = 0; i < minLength; i++) {
                    int comparison = Integer.compare(o1[i], o2[i]);
                    if (comparison != 0) {
                        return comparison;
                    }
                }
                return Integer.compare(length1, length2);
            }
        });
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReflectionCompare_NestedClasses() throws Exception {
        class NestedClass {
            private int value;

            public NestedClass(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        NestedClass lhs = new NestedClass(1);
        NestedClass rhs = new NestedClass(1);
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new NestedClass(1);
        rhs = new NestedClass(2);
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new NestedClass(1);
        rhs = new NestedClass(1);
        result = CompareToBuilder.reflectionCompare(lhs, rhs, new Comparator<NestedClass>() {
            @Override
            public int compare(NestedClass o1, NestedClass o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });
        Assert.assertEquals(0, result);

        lhs = new NestedClass(1);
        rhs = new NestedClass(2);
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        Class<?> clazz = NestedClass.class;
        Field valueField = FieldUtils.getField(clazz, "value", true);
        AccessibleObject.setAccessible(valueField, true);

        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(lhs, rhs, Comparator.comparing(valueField::get)));
        int superCompareTo = builder.toComparison();
        Assert.assertEquals(-1, superCompareTo);
    }
}