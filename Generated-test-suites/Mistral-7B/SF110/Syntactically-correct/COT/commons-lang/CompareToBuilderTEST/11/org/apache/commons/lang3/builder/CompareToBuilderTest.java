package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_Objects() {
        Object lhs = new Object();
        Object rhs = new Object();
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompare_Null() {
        Object lhs = null;
        Object rhs = new Object();
        assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(lhs, rhs));
    }

    @Test
    public void testReflectionCompare_DifferentClasses() {
        Object lhs = new ObjectA();
        Object rhs = new ObjectB();
        assertThrows(ClassCastException.class, () -> CompareToBuilder.reflectionCompare(lhs, rhs));
    }

    @Test
    public void testReflectionCompare_ExcludeFields() {
        Object lhs = new ObjectWithFields("field1", "field2");
        Object rhs = new ObjectWithFields("field2", "field3");
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field1"));
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompare_ExcludeFields_Null() {
        Object lhs = new ObjectWithFields("field1", "field2");
        Object rhs = new ObjectWithFields("field2", "field3");
        assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(lhs, rhs, null));
    }

    @Test
    public void testReflectionCompare_ExcludeFields_EmptyList() {
        Object lhs = new ObjectWithFields("field1", "field2");
        Object rhs = new ObjectWithFields("field2", "field3");
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, Collections.emptyList());
        assertEquals(CompareToBuilder.COMPARE_FIELDS_ORDER, result);
    }

    @Test
    public void testReflectionCompare_ExcludeFields_NonExistentField() {
        Object lhs = new ObjectWithFields("field1", "field2");
        Object rhs = new ObjectWithFields("field2", "field3");
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field4"));
        assertEquals(0, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder left = new CompareToBuilder();
        CompareToBuilder right = new CompareToBuilder();
        left.appendSuper(1);
        assertEquals(1, left.toComparison());
        right.appendSuper(2);
        right.appendSuper(left.toComparison());
        assertEquals(-1, right.toComparison());
    }

    @Test
    public void testAppend_Objects() {
        Object lhs = new ObjectA();
        Object rhs = new ObjectB();
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_NullLhs() {
        Object rhs = new Object();
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(null, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_NullRhs() {
        Object lhs = new Object();
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, null);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_PrimitiveArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_PrimitiveArrays_DifferentLengths() {
        int[] lhs = {1, 2};
        int[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_PrimitiveArrays_NegativeValues() {
        int[] lhs = {-1, -2, -3};
        int[] rhs = {-1, -2};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ObjectArrays() {
        Object[] lhs = {"foo", "bar"};
        Object[] rhs = {"foo", "baz"};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ObjectArrays_Null() {
        Object[] lhs = {"foo", "bar"};
        Object[] rhs = null;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ObjectArrays_DifferentLengths() {
        Object[] lhs = {"foo", "bar"};
        Object[] rhs = {"foo", "bar", "baz"};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ObjectArrays_Comparator() {
        Object[] lhs = {"foo", "bar"};
        Object[] rhs = {"bar", "foo"};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((String) o1).compareTo((String) o2);
            }
        });
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_LongArrays() {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_LongArrays_DifferentLengths() {
        long[] lhs = {1, 2};
        long[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_LongArrays_NegativeValues() {
        long[] lhs = {-1, -2, -3};
        long[] rhs = {-1, -2};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_IntArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_IntArrays_DifferentLengths() {
        int[] lhs = {1, 2};
        int[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_IntArrays_NegativeValues() {
        int[] lhs = {-1, -2, -3};
        int[] rhs = {-1, -2};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ShortArrays() {
        short[] lhs = {1, 2, 3};
        short[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_ShortArrays_DifferentLengths() {
        short[] lhs = {1, 2};
        short[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ShortArrays_NegativeValues() {
        short[] lhs = {-1, -2, -3};
        short[] rhs = {-1, -2};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_CharArrays() {
        char[] lhs = {'a', 'b', 'c'};
        char[] rhs = {'a', 'b', 'c'};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_CharArrays_DifferentLengths() {
        char[] lhs = {'a', 'b'};
        char[] rhs = {'a', 'b', 'c'};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_CharArrays_NegativeValues() {
        char[] lhs = {'-', '-', '-'};
        char[] rhs = {'-', '-'};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ByteArrays() {
        byte[] lhs = {1, 2, 3};
        byte[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_ByteArrays_DifferentLengths() {
        byte[] lhs = {1, 2};
        byte[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_ByteArrays_NegativeValues() {
        byte[] lhs = {-1, -2, -3};
        byte[] rhs = {-1, -2};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_DoubleArrays() {
        double[] lhs = {1.0, 2.0, 3.0};
        double[] rhs = {1.0, 2.0, 3.0};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_DoubleArrays_DifferentLengths() {
        double[] lhs = {1.0, 2.0};
        double[] rhs = {1.0, 2.0, 3.0};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_DoubleArrays_NegativeValues() {
        double[] lhs = {-1.0, -2.0, -3.0};
        double[] rhs = {-1.0, -2.0};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_FloatArrays() {
        float[] lhs = {1.0f, 2.0f, 3.0f};
        float[] rhs = {1.0f, 2.0f, 3.0f};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_FloatArrays_DifferentLengths() {
        float[] lhs = {1.0f, 2.0f};
        float[] rhs = {1.0f, 2.0f, 3.0f};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_FloatArrays_NegativeValues() {
        float[] lhs = {-1.0f, -2.0f, -3.0f};
        float[] rhs = {-1.0f, -2.0f};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_BooleanArrays() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, true};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_BooleanArrays_DifferentLengths() {
        boolean[] lhs = {true, false};
        boolean[] rhs = {true, false, true};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_BooleanArrays_NegativeValues() {
        boolean[] lhs = {false, false, false};
        boolean[] rhs = {false, false};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testToComparison() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(1, 2);
        builder.append(3, 3);
        builder.appendSuper(5);
        assertEquals(-2, builder.toComparison());
    }
}