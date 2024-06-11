package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testCompareToBuilderDefaultConstructor() {
        CompareToBuilder builder = new CompareToBuilder();
        assertEquals(0, builder.build());
    }

    @Test
    public void testReflectionCompare_Objects_NoExcludeFields() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompare_Objects_CompareTransientsFalse() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
            transient int c = 3;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
            transient int c = 1;
        };

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompare_Objects_ExcludeFieldsEmptyArray() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[0]);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompare_Objects_ExcludeFieldsStringArray() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
            String c = "test";
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
            String c = "test2";
        };

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"c"});
        assertEquals(0, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_NullLhs() {
        Object rhs = new Object();
        CompareToBuilder.reflectionCompare(null, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_NullRhs() {
        Object lhs = new Object();
        CompareToBuilder.reflectionCompare(lhs, null);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_LhsNotInstanceOfRhsClass() {
        Object lhs = new Integer(1);
        Object rhs = new String("test");
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test
    public void testReflectionCompare_Arrays_SameLength() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_Arrays_DifferentLengths() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2};
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompare_Arrays_DifferentValues() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {2, 3, 1};
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null);
        assertEquals(1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_NullArray() {
        int[] lhs = null;
        int[] rhs = new int[1];
        CompareToBuilder.reflectionCompare(lhs, rhs, null);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_NullComparator() {
        long[] lhs = {1L, 2L, 3L};
        long[] rhs = {1L, 2L, 3L};
        Comparator<Object> comparator = null;
        CompareToBuilder.reflectionCompare(lhs, rhs, comparator);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder appendedBuilder = builder.appendSuper(1);
        assertEquals(1, appendedBuilder.build());
    }

    @Test
    public void testAppendObjects() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendObjects_ComparatorNull() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendObjects_ComparatorComparator() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer)o1).compareTo((Integer)o2);
            }
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendLongs() {
        long lhs = 1L;
        long rhs = 2L;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendInts() {
        int lhs = 1;
        int rhs = 2;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendShorts() {
        short lhs = 1;
        short rhs = 2;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendChars() {
        char lhs = 'a';
        char rhs = 'b';
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendBytes() {
        byte lhs = 1;
        byte rhs = 2;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendDoubles() {
        double lhs = 1.0;
        double rhs = 2.0;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendFloats() {
        float lhs = 1.0f;
        float rhs = 2.0f;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendBooleans() {
        boolean lhs = true;
        boolean rhs = false;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.build();
        assertEquals(1, comparison);
    }

    @Test
    public void testAppendArrays_NullLhs() {
        Object[] lhs = null;
        Object[] rhs = new Object[1];
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        int comparison = builder.build();
        assertNull(comparison);
    }

    @Test
    public void testAppendArrays_NullRhs() {
        Object[] lhs = new Object[1];
        Object[] rhs = null;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        int comparison = builder.build();
        assertNull(comparison);
    }

    @Test
    public void testAppendArrays_ComparatorNull() {
        Object[] lhs = new Object[1];
        Object[] rhs = new Object[1];
        Object[] lhs2 = new Object[1];
        Object[] rhs2 = new Object[1];
        Comparator<Object> comparator = null;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int comparison = builder.build();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendArrays_ComparatorComparator() {
        Object[] lhs = new Object[]{new Integer(1), new Integer(2)};
        Object[] rhs = new Object[]{new Integer(2), new Integer(1)};
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer)o1).compareTo((Integer)o2);
            }
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArrays_ArraysSameLength() {
        Object[] lhs = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        Object[] rhs = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        int comparison = builder.build();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendArrays_ArraysDifferentLengths() {
        Object[] lhs = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        Object[] rhs = new Object[]{new Integer(1), new Integer(2)};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        int comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArrays_ArraysDifferentValues() {
        Object[] lhs = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        Object[] rhs = new Object[]{new Integer(1), new Integer(2), new Integer(4)};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, null);
        int comparison = builder.build();
        assertEquals(1, comparison);
    }
}