package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;

import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object() {
            int i = 1;
            int j = 2;
        };
        Object obj2 = new Object() {
            int i = 1;
            int j = 3;
        };

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj1);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(null, obj1);
        assertNull(obj1);

        comparison = CompareToBuilder.reflectionCompare(obj1, null);
        assertNotNull(obj1);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Object.class);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Object.class, Integer.class);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareExcludeFields() {
        Object obj1 = new Object() {
            int i = 1;
            int j = 2;
            String excludedField = "excluded";
        };
        Object obj2 = new Object() {
            int i = 1;
            int j = 3;
        };

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, new String[]{"excludedField"});
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();

        int[] intArray1 = {1, 2, 3};
        int[] intArray2 = {1, 2, 4};
        builder.append(intArray1, intArray2);
        assertEquals(-1, builder.toComparison());

        String[] stringArray1 = {"a", "b", "c"};
        String[] stringArray2 = {"a", "b", "d"};
        builder = new CompareToBuilder();
        builder.append(stringArray1, stringArray2);
        assertEquals(-1, builder.toComparison());

        Comparable<String> stringComparable1 = "a";
        Comparable<String> stringComparable2 = "b";
        builder = new CompareToBuilder();
        builder.append(stringComparable1, stringComparable2);
        assertEquals(-1, builder.toComparison());

        Comparator<String> stringComparator = Comparator.comparingInt(String::length);
        builder = new CompareToBuilder();
        builder.append(stringArray1, stringArray2, stringComparator);
        assertEquals(-1, builder.toComparison());

        long lhs = 10L;
        long rhs = 20L;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-10, builder.toComparison());

        int mhs = 10;
        int mhs = 20;
        builder = new CompareToBuilder();
        builder.append(mhs, mhs);
        assertEquals(0, builder.toComparison());

        short shlhs = 10;
        short shrhs = 20;
        builder = new CompareToBuilder();
        builder.append(shlhs, shrhs);
        assertEquals(-10, builder.toComparison());

        char chlhs = 'a';
        char chrhs = 'b';
        builder = new CompareToBuilder();
        builder.append(chlhs, chrhs);
        assertEquals(-1, builder.toComparison());

        byte bylhs = 10;
        byte byrhs = 20;
        builder = new CompareToBuilder();
        builder.append(bylhs, byrhs);
        assertEquals(-10, builder.toComparison());

        double dls = 10.0;
        double drs = 20.0;
        builder = new CompareToBuilder();
        builder.append(dls, drs);
        assertEquals(-10.0, builder.toComparison());

        float fls = 10.0f;
        float flrs = 20.0f;
        builder = new CompareToBuilder();
        builder.append(fls, flrs);
        assertEquals(-10.0f, builder.toComparison());

        boolean blhs = true;
        boolean blhs = false;
        builder = new CompareToBuilder();
        builder.append(blhs, blhs);
        assertEquals(0, builder.toComparison());

        boolean blhs1 = true;
        boolean blhs2 = false;
        builder = new CompareToBuilder();
        builder.append(blhs1, blhs2);
        assertEquals(1, builder.toComparison());

        Object[] objArray1 = {1, 2, 3};
        Object[] objArray2 = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(objArray1, objArray2);
        assertEquals(-1, builder.toComparison());

        long[] longArray1 = {1, 2, 3};
        long[] longArray2 = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(longArray1, longArray2);
        assertEquals(-1, builder.toComparison());

        int[] intArray3 = {1, 2, 3};
        int[] intArray4 = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(intArray3, intArray4);
        assertEquals(-1, builder.toComparison());

        short[] shortArray1 = {1, 2, 3};
        short[] shortArray2 = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(shortArray1, shortArray2);
        assertEquals(-1, builder.toComparison());

        char[] charArray1 = {'a', 'b', 'c'};
        char[] charArray2 = {'a', 'b', 'd'};
        builder = new CompareToBuilder();
        builder.append(charArray1, charArray2);
        assertEquals(-1, builder.toComparison());

        byte[] byteArray1 = {1, 2, 3};
        byte[] byteArray2 = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(byteArray1, byteArray2);
        assertEquals(-1, builder.toComparison());

        double[] doubleArray1 = {1.0, 2.0, 3.0};
        double[] doubleArray2 = {1.0, 2.0, 4.0};
        builder = new CompareToBuilder();
        builder.append(doubleArray1, doubleArray2);
        assertEquals(-1, builder.toComparison());

        float[] floatArray1 = {1.0f, 2.0f, 3.0f};
        float[] floatArray2 = {1.0f, 2.0f, 4.0f};
        builder = new CompareToBuilder();
        builder.append(floatArray1, floatArray2);
        assertEquals(-1, builder.toComparison());

        boolean[] booleanArray1 = {true, false, true};
        boolean[] booleanArray2 = {true, false, false};
        builder = new CompareToBuilder();
        builder.append(booleanArray1, booleanArray2);
        assertEquals(1, builder.toComparison());

        Object[] objArray3 = {"a", "b", "c"};
        Object[] objArray4 = {"a", "b", "d"};
        builder = new CompareToBuilder();
        builder.append(objArray3, objArray4);
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppendSuper() {
        Object obj1 = new Object() {
            int i = 1;
        };
        Object obj2 = new Object() {
            int i = 2;
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(CompareToBuilder.reflectionCompare(obj1, obj2));
        assertEquals(1, builder.toComparison());
    }
}