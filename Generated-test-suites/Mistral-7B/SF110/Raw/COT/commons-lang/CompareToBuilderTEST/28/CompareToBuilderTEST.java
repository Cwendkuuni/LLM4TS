package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object();
        Object rhs = new Object();
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);

        lhs = new Integer(1);
        rhs = new Integer(1);
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);

        lhs = new Integer(1);
        rhs = new Integer(2);
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, result);

        lhs = new Integer(2);
        rhs = new Integer(1);
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, result);

        lhs = new String("abc");
        rhs = new String("abc");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);

        lhs = new String("abc");
        rhs = new String("def");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, result);

        lhs = new String("def");
        rhs = new String("abc");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompareWithNull() {
        Object lhs = null;
        Object rhs = new Object();
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompareWithDifferentClasses() {
        Object lhs = new Integer(1);
        Object rhs = new String("abc");
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        assertEquals(5, builder.toComparison());
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();

        // Integer
        builder.append(new Integer(1), new Integer(1));
        assertEquals(0, builder.toComparison());

        builder.append(new Integer(1), new Integer(2));
        assertEquals(-1, builder.toComparison());

        builder.append(new Integer(2), new Integer(1));
        assertEquals(1, builder.toComparison());

        // Long
        builder = new CompareToBuilder();
        builder.append((long) 1, (long) 1);
        assertEquals(0, builder.toComparison());

        builder.append((long) 1, (long) 2);
        assertEquals(-1, builder.toComparison());

        builder.append((long) 2, (long) 1);
        assertEquals(1, builder.toComparison());

        // Double
        builder = new CompareToBuilder();
        builder.append(1.0d, 1.0d);
        assertEquals(0, builder.toComparison());

        builder.append(1.0d, 2.0d);
        assertEquals(-1, builder.toComparison());

        builder.append(2.0d, 1.0d);
        assertEquals(1, builder.toComparison());

        // String
        builder = new CompareToBuilder();
        builder.append("abc", "abc");
        assertEquals(0, builder.toComparison());

        builder.append("abc", "def");
        assertEquals(-1, builder.toComparison());

        builder.append("def", "abc");
        assertEquals(1, builder.toComparison());

        // Array
        Object[] lhs = {1, 2, 3};
        Object[] rhs = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 2};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(1, builder.toComparison());

        // Custom Comparator
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Integer && o2 instanceof Integer) {
                    Integer i1 = (Integer) o1;
                    Integer i2 = (Integer) o2;
                    return i1.compareTo(i2);
                }
                throw new UnsupportedOperationException();
            }
        };
        Object[] lhs2 = {new Integer(1), new Integer(2), new Integer(3)};
        Object[] rhs2 = {new Integer(1), new Integer(2), new Integer(3)};
        builder = new CompareToBuilder();
        builder.append(lhs2, rhs2, comparator);
        assertEquals(0, builder.toComparison());

        Object[] lhs3 = {new Integer(1), new Integer(2), new Integer(3)};
        Object[] rhs3 = {new Integer(1), new Integer(2), new Integer(4)};
        builder = new CompareToBuilder();
        builder.append(lhs3, rhs3, comparator);
        assertEquals(-1, builder.toComparison());

        Object[] lhs4 = {new Integer(1), new Integer(2), new Integer(3)};
        Object[] rhs4 = {new Integer(1), new Integer(2), new Integer(2)};
        builder = new CompareToBuilder();
        builder.append(lhs4, rhs4, comparator);
        assertEquals(1, builder.toComparison());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendWithNull() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(null, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendWithNull2() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(new Object(), null);
    }
}