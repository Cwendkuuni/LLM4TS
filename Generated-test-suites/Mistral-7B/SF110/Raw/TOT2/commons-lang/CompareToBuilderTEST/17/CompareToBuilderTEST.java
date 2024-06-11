package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.ReflectionToStringBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_nullLhs_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(null, new Object(), false, null, new String[0]));
    }

    @Test
    public void testReflectionCompare_nullRhs_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(new Object(), null, false, null, new String[0]));
    }

    @Test
    public void testReflectionCompare_classesNotEqual_throwsClassCastException() {
        Assert.assertThrows(ClassCastException.class, () -> CompareToBuilder.reflectionCompare(new Object(), new String(), false, null, new String[0]));
    }

    @Test
    public void testReflectionCompare_basicScenario() {
        Integer lhs = 5;
        Integer rhs = 10;
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-5, comparison);
    }

    @Test
    public void testReflectionCompare_transients_true() {
        class TestClass {
            private int transientField;
            private int nonTransientField;

            public int getTransientField() {
                return transientField;
            }

            public void setTransientField(int transientField) {
                this.transientField = transientField;
            }

            public int getNonTransientField() {
                return nonTransientField;
            }

            public void setNonTransientField(int nonTransientField) {
                this.nonTransientField = nonTransientField;
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        lhs.setTransientField(1);
        lhs.setNonTransientField(2);
        rhs.setTransientField(3);
        rhs.setNonTransientField(2);

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true, null, new String[0]);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompare_excludeFields_basicScenario() {
        class TestClass {
            private int field1;
            private int field2;

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }

            public int getField2() {
                return field2;
            }

            public void setField2(int field2) {
                this.field2 = field2;
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        lhs.setField1(1);
        lhs.setField2(2);
        rhs.setField1(1);
        rhs.setField2(3);

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, null, new String[]{"field2"});
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend_nullLhs_comparisonIsMinusOne() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(null, new Object());
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_nullRhs_comparisonIsOne() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(new Object(), null);
        int comparison = builder.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testAppend_longs_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10L, 5L);
        int comparison = builder.toComparison();
        assertEquals(5, comparison);
    }

    @Test
    public void testAppend_ints_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(5, 10
);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_shorts_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((short) 10, (short) 5);
        int comparison = builder.toComparison();
        assertEquals(5, comparison);
    }

    @Test
    public void testAppend_chars_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((char) 'z', (char) 'a');
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_bytes_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((byte) 10, (byte) 5);
        int comparison = builder.toComparison();
        assertEquals(5, comparison);
    }

    @Test
    public void testAppend_doubles_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10.5, 5.5);
        int comparison = builder.toComparison();
        assertEquals(-0.5, comparison);
    }

    @Test
    public void testAppend_floats_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10.5f, 5.5f);
        int comparison = builder.toComparison();
        assertEquals(-0.5f, comparison);
    }

    @Test
    public void testAppend_booleans_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(true, false);
        int comparison = builder.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testAppend_arrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        int[] lhs2 = {1, 2, 3};
        int[] rhs2 = {1, 3, 3};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_longArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        long[] lhs2 = {1, 2, 3};
        long[] rhs2 = {1, 3, 3};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_intArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        int[] lhs2 = {1, 2, 3};
        int[] rhs2 = {1, 3, 3};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_shortArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        short[] lhs = {1, 2, 3};
        short[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        short[] lhs2 = {1, 2, 3};
        short[] rhs2 = {1, 3, 3};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_charArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        char[] lhs = {'a', 'b', 'c'};
        char[] rhs = {'a', 'b', 'c'};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        char[] lhs2 = {'a', 'b', 'c'};
        char[] rhs2 = {'a', 'c', 'c'};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_byteArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        byte[] lhs = {1, 2, 3};
        byte[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        byte[] lhs2 = {1, 2, 3};
        byte[] rhs2 = {1, 3, 3};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_doubleArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        double[] lhs = {1.1, 2.2, 3.3};
        double[] rhs = {1.1, 2.2, 3.3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        double[] lhs2 = {1.1, 2.2, 3.3};
        double[] rhs2 = {1.1, 2.3, 3.3};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_floatArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        float[] lhs = {1.1f, 2.2f, 3.3f};
        float[] rhs = {1.1f, 2.2f, 3.3f};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        float[] lhs2 = {1.1f, 2.2f, 3.3f};
        float[] rhs2 = {1.1f, 2.3f, 3.3f};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_booleanArrays_comparisonIsCorrect() {
        CompareToBuilder builder = new CompareToBuilder();

        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, true};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        boolean[] lhs2 = {true, false, true};
        boolean[] rhs2 = {true, false, false};
        builder.append(lhs2, rhs2);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }
}