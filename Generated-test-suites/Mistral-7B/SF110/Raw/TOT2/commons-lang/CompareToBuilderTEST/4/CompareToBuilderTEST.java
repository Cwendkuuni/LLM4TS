package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testComparisonOfSameObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object obj1 = new Object();
        Object obj2 = obj1;

        builder.append(obj1, obj2);
        int comparison = builder.toComparison();

        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfDifferentObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object obj1 = new Object();
        Object obj2 = new Object();

        builder.append(obj1, obj2);
        int comparison = builder.toComparison();

        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfNullAndNonNullObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object obj1 = new Object();
        Object obj2 = null;

        builder.append(obj1, obj2);
        int comparison = builder.toComparison();

        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonOfArrays() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};

        builder.append(array1, array2);
        int comparison = builder.toComparison();

        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfDifferentArrays() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 4};

        builder.append(array1, array2);
        int comparison = builder.toComparison();

        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfNullAndNonNullArrays() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = null;

        builder.append(array1, array2);
        int comparison = builder.toComparison();

        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonOfArraysWithSameLengthButDifferentValues() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{2, 3, 1};

        builder.append(array1, array2);
        int comparison = builder.toComparison();

        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonOfArraysWithDifferentLength() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2};

        builder.append(array1, array2);
        int comparison = builder.toComparison();

        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfCustomClassObjects() throws NoSuchFieldException, IllegalAccessException {
        class CustomClass {
            private int value;

            public CustomClass(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        CompareToBuilder builder = new CompareToBuilder();
        CustomClass obj1 = new CustomClass(1);
        CustomClass obj2 = new CustomClass(2);

        Field valueField = CustomClass.class.getDeclaredField("value");
        valueField.setAccessible(true);

        builder.append(obj1, obj2);
        int comparison = builder.toComparison();

        assertEquals(-1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testComparisonWithNullClass() {
        CompareToBuilder builder = new CompareToBuilder();
        Object obj1 = new Object();
        Object obj2 = new Object();

        builder.append(obj1, obj2, null);
    }

    @Test
    public void testComparisonWithExcludedFields() {
        CompareToBuilder builder = new CompareToBuilder();
        Object obj1 = new TestClass();
        Object obj2 = new TestClass();

        builder.append(obj1, obj2, "field1");
        int comparison = builder.toComparison();

        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonWithTransientFields() {
        CompareToBuilder builder = new CompareToBuilder();
        Object obj1 = new TestClass();
        Object obj2 = new TestClass();

        builder.append(obj1, obj2, true);
        int comparison = builder.toComparison();

        assertEquals(0, comparison);
    }

    static class TestClass {
        private int field1;
        private transient int field2;

        public TestClass() {
            this.field1 = 1;
            this.field2 = 2;
        }

        public int getField1() {
            return field1;
        }

        public int getField2() {
            return field2;
        }
    }
}