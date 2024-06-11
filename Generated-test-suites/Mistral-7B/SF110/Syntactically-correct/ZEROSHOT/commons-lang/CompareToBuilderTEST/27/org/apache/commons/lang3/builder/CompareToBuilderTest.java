package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
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
        assertEquals(-5, comparison);
    }

    @Test
    public void testReflectionCompare_compareTransients() throws Exception {
        Object lhs = new Object() {
            transient int value = 5;
        };
        Object rhs = new Object() {
            int value = 10;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(-5, comparison);
    }

    @Test
    public void testReflectionCompare_excludeFields() throws Exception {
        Object lhs = new Object() {
            int value1 = 5;
            int value2 = 10;
        };
        Object rhs = new Object() {
            int value1 = 5;
            int value3 = 10;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("value2"));
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_excludeFields_array() throws Exception {
        Object lhs = new Object() {
            int value1 = 5;
            int value2 = 10;
        };
        Object rhs = new Object() {
            int value1 = 5;
            int value3 = 10;
        };
        String[] excludeFields = {"value2"};
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, excludeFields);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_excludeFields_class() throws Exception {
        Object lhs = new Object() {
            int value1 = 5;
            int value2 = 10;
        };
        Object rhs = new Object() {
            int value1 = 5;
            int value3 = 10;
        };
        Class<?> reflectUpToClass = Object.class;
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, reflectUpToClass, excludeFields);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_superCompareTo() {
        Object parent = new Object() {};
        Object child1 = new Object() {
            @Override
            public int compareTo(Object obj) {
                return 5;
            }
        };
        Object child2 = new Object() {
            @Override
            public int compareTo(Object obj) {
                return 10;
            }
        };
        int comparison = CompareToBuilder.reflectionCompare(child1, child2, parent);
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend() throws Exception {
        Object lhs = new Object() {
            int value1 = 5;
            int value2 = 10;
        };
        Object rhs = new Object() {
            int value1 = 5;
            int value2 = 15;
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_Comparator() throws Exception {
        Object lhs = new Object() {
            int value1 = 5;
            int value2 = 10;
        };
        Object rhs = new Object() {
            int value1 = 5;
            int value2 = 15;
        };
        Comparator<Object> comparator = (o1, o2) -> ((Integer)o1.getClass().getDeclaredField("value2").get(o1)).intValue() - ((Integer)o2.getClass().getDeclaredField("value2").get(o2)).intValue();
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_long() throws Exception {
        long lhs = 5L;
        long rhs = 10L;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_int() throws Exception {
        int lhs = 5;
        int rhs = 10;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_short() throws Exception {
        short lhs = (short)5;
        short rhs = (short)10;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_char() throws Exception {
        char lhs = 'A';
        char rhs = 'E';
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_byte() throws Exception {
        byte lhs = (byte)5;
        byte rhs = (byte)10;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_double() throws Exception {
        double lhs = 5.0;
        double rhs = 10.0;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5.0, comparison);
    }

    @Test
    public void testAppend_float() throws Exception {
        float lhs = 5.0f;
        float rhs = 10.0f;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5.0f, comparison);
    }

    @Test
    public void testAppend_boolean() throws Exception {
        boolean lhs = false;
        boolean rhs = true;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_Object_Object() throws Exception {
        Object lhs = new Object() {
            int value = 5;
        };
        Object rhs = new Object() {
            int value = 10;
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_Object_Object_Comparator() throws Exception {
        Object lhs = new Object() {
            int value1 = 5;
            int value2 = 10;
        };
        Object rhs = new Object() {
            int value1 = 5;
            int value3 = 10;
        };
        Comparator<Object> comparator = (o1, o2) -> ((Integer)o1.getClass().getDeclaredField("value2").get(o1)).intValue() - ((Integer)o2.getClass().getDeclaredField("value2").get(o2)).intValue();
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);
    }

    @Test
    public void testAppend_ObjectArray() throws Exception {
        Object[] lhs = {1, 2, 3};
        Object[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_ObjectArray_Comparator() throws Exception {
        Object[] lhs = {1, 2, 3};
        Object[] rhs = {1, 2, 4};
        Comparator<Object[]> comparator = (o1, o2) -> Arrays.stream(o1).mapToInt(i -> ((Integer)i.getClass().getDeclaredField("value").get(i)).intValue()).min().compareTo(Arrays.stream(o2).mapToInt(i -> ((Integer)i.getClass().getDeclaredField("value").get(i)).intValue()).min().getAsInt());
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_longArray() throws Exception {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_intArray() throws Exception {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_shortArray() throws Exception {
        short[] lhs = {(short)1, (short)2, (short)3};
        short[] rhs = {(short)1, (short)2, (short)4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_charArray() throws Exception {
        char[] lhs = {'A', 'B', 'C'};
        char[] rhs = {'A', 'B', 'D'};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_byteArray() throws Exception {
        byte[] lhs = {(byte)1, (byte)2, (byte)3};
        byte[] rhs = {(byte)1, (byte)2, (byte)4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_doubleArray() throws Exception {
        double[] lhs = {1.0, 2.0, 3.0};
        double[] rhs = {1.0, 2.0, 4.0};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_floatArray() throws Exception {
        float[] lhs = {1.0f, 2.0f, 3.0f};
        float[] rhs = {1.0f, 2.0f, 4.0f};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_booleanArray() throws Exception {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {true, false, false};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }
}