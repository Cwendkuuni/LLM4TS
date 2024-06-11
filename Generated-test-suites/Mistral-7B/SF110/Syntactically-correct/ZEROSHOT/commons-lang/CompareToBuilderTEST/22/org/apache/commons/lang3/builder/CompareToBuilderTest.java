package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.*;
import org.apache.commons.lang3.reflect.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        lhs = new Object() {
            int value = 1;
        };
        rhs = new Object() {
            int value = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new Object() {
            int value = null;
        };
        rhs = null;
        try {
            comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object();
        rhs = new Object();
        try {
            comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
            fail();
        } catch (ClassCastException e) {
            // Expected
        }

        lhs = new Object();
        Comparable<Object> comparableLhs = (Comparable<Object>)lhs;
        rhs = new Object();
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, comparableLhs::compareTo);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(1, comparison);

        // Test with different types
        long lhsLong = 1L;
        long rhsLong = 2L;
        builder = new CompareToBuilder();
        builder.append(lhsLong, rhsLong);
        comparison = builder.toComparison();
        assertEquals(1, comparison);

        char lhsChar = 'a';
        char rhsChar = 'b';
        builder = new CompareToBuilder();
        builder.append(lhsChar, rhsChar);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);

        short lhsShort = (short)1;
        short rhsShort = (short)2;
        builder = new CompareToBuilder();
        builder.append(lhsShort, rhsShort);
        comparison = builder.toComparison();
        assertEquals(1, comparison);

        int[] intArrayLhs = {1, 2, 3};
        int[] intArrayRhs = {2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(intArrayLhs, intArrayRhs);
        comparison = builder.toComparison();
        assertArrayEquals(new int[]{1, -1}, new int[]{comparison, builder.comparison});

        long[] longArrayLhs = {1, 2, 3};
        long[] longArrayRhs = {2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(longArrayLhs, longArrayRhs);
        comparison = builder.toComparison();
        assertArrayEquals(new int[]{1, -1}, new int[]{comparison, builder.comparison});

        String lhsString = "abc";
        String rhsString = "def";
        builder = new CompareToBuilder();
        builder.append(lhsString, rhsString);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);

        // Test with Comparator
        Comparator<Object> comparator = Comparator.comparingInt(o -> ((Number)o).intValue());
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);
        comparison = builder.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testAppendSuper() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };
        Object parent = Object.class;

        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(2);
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(3, comparison);
    }
}