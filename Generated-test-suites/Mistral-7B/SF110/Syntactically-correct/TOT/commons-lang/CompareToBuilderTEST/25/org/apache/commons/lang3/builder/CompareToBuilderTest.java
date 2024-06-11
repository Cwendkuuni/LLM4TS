package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.ReflectionToStringBuilder;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_ObjectsAreEqual() {
        Object lhs = new Object();
        Object rhs = new Object();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_ObjectsAreNotEqual_SameClass() {
        String lhs = "abc";
        String rhs = "abc";
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_FirstObjectIsNull() {
        Object rhs = new Object();
        CompareToBuilder.reflectionCompare(null, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_SecondObjectIsNull() {
        Object lhs = new Object();
        CompareToBuilder.reflectionCompare(lhs, null);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_DifferentClasses() {
        String lhs = "abc";
        Number rhs = 123;
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test
    public void testReflectionCompare_Transients() {
        TestClass lhs = new TestClass(1, 2, "transientValue");
        TestClass rhs = new TestClass(1, 2, "transientValue");
        TestClass lhsWithTransientSet = new TestClass(1, 2, "nonTransientValue");
        Field transientField = TestClass.class.getDeclaredField("transientField");
        transientField.setAccessible(true);
        transientField.set(lhsWithTransientSet, "transientValue");

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, lhsWithTransientSet);
        assertNotEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_ExcludedFields() {
        TestClass lhs = new TestClass(1, 2, "field1");
        TestClass rhs = new TestClass(1, 2, "field2");
        String[] excludeFields = {"field1"};

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, null, excludeFields);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, null, new String[0]);
        assertNotEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_CustomCompareClass() {
        TestClass lhs = new TestClass(1, 2, "field1");
        TestClass rhs = new TestClass(1, 2, "field2");
        Comparator<TestClass> comparator = (l, r) -> l.getId() - r.getId();

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, TestClass.class, null);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, TestClass.class, comparator);
        assertNotEquals(0, comparison);
    }

    @Test
    public void testAppend_ObjectsAreEqual() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_ObjectsAreNotEqual_SameClass() {
        CompareToBuilder builder = new CompareToBuilder();
        String lhs = "abc";
        String rhs = "def";
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test(expected = NullPointerException.class)
    public void testAppend_FirstObjectIsNull() {
        CompareToBuilder builder = new CompareToBuilder();
        Object rhs = new Object();
        builder.append(null, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testAppend_SecondObjectIsNull() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        builder.append(lhs, null);
    }

    @Test
    public void testAppend_Arrays() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_ArraysDifferentLengths() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2};
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ArraysSameElementsInDifferentOrder() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = {2, 1, 3};
        int[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ArraysDifferentElements() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {4, 5, 6};
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_Primitives() {
        CompareToBuilder builder = new CompareToBuilder();
        long lhs = 1L;
        long rhs = 1L;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_PrimitivesDifferent() {
        CompareToBuilder builder = new CompareToBuilder();
        long lhs = 1L;
        long rhs = 2L;
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_PrimitivesNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        long lhs = -1L;
        long rhs = 1L;
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_PrimitivesZero() {
        CompareToBuilder builder = new CompareToBuilder();
        long lhs = 0L;
        long rhs = 0L;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_Doubles() {
        CompareToBuilder builder = new CompareToBuilder();
        double lhs = 1.0;
        double rhs = 1.0;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_DoublesDifferent() {
        CompareToBuilder builder = new CompareToBuilder();
        double lhs = 1.0;
        double rhs = 2.0;
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_DoublesNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        double lhs = -1.0;
        double rhs = 1.0;
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_DoublesZero() {
        CompareToBuilder builder = new CompareToBuilder();
        double lhs = 0.0;
        double rhs = 0.0;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_Floats() {
        CompareToBuilder builder = new CompareToBuilder();
        float lhs = 1.0f;
        float rhs = 1.0f;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_FloatsDifferent() {
        CompareToBuilder builder = new CompareToBuilder();
        float lhs = 1.0f;
        float rhs = 2.0f;
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_FloatsNegative() {
        CompareToBuilder builder = new CompareToBuilder();
        float lhs = -1.0f;
        float rhs = 1.0f;
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_FloatsZero() {
        CompareToBuilder builder = new CompareToBuilder();
        float lhs = 0.0f;
        float rhs = 0.0f;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_Booleans() {
        CompareToBuilder builder = new CompareToBuilder();
        boolean lhs = true;
        boolean rhs = true;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_BooleansDifferent() {
        CompareToBuilder builder = new CompareToBuilder();
        boolean lhs = true;
        boolean rhs = false;
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_BooleansFalse() {
        CompareToBuilder builder = new CompareToBuilder();
        boolean lhs = false;
        boolean rhs = true;
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_BooleansTrue() {
        CompareToBuilder builder = new CompareToBuilder();
        boolean lhs = true;
        boolean rhs = true;
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_ArraysWithComparators() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {3, 2, 1};
        Comparator<Integer[]> comparator = (l, r) -> Arrays.compare(l, r);

        builder.append(lhs, rhs, comparator);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ArraysWithComparatorsEquivalent() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {1, 2, 3};
        Comparator<Integer[]> comparator = (l, r) -> Arrays.compare(l, r);

        builder.append(lhs, rhs, comparator);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_ArraysWithCustomComparator() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {3, 2, 1};
        Comparator<Integer> customComparator = (l, r) -> l - r;

        builder.append(lhs, rhs, null, Integer[].class, customComparator);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ArraysWithCustomComparatorEquivalent() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {1, 2, 3};
        Comparator<Integer> customComparator = (l, r) -> l - r;

        builder.append(lhs, rhs, null, Integer[].class, customComparator);
        assertEquals(0, builder.toComparison());
    }

    public static class TestClass {
        private int id;
        private String field1;
        private String transientField;

        public TestClass(int id, String field1, String transientField) {
            this.id = id;
            this.field1 = field1;
            this.transientField = transientField;
        }

        public int getId() {
            return id;
        }

        public String getField1() {
            return field1;
        }

        public String getTransientField() {
            return transientField;
        }

        public static class ReflectedToStringBuilder {
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestClass that = (TestClass) obj;
            return id == that.id && Objects.equals(field1, that.field1) && Objects.equals(transientField, that.transientField);
        }
    }
}