package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CompareToBuilderTest {
    @Test
    public void testAppend() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNullLhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNullRhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = null;
        compareToBuilder.append(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testAppendIncompatibleTypes() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new String("hello");
        Object rhs = new Integer(10);
        compareToBuilder.append(lhs, rhs);
    }

    @Test
    public void testReflectionCompare() {
        // Add a test case for reflectionCompare method here
    }

    @Test
    public void testAppendSuper() {
        // Add a test case for appendSuper method here
    }

    @Test
    public void testAppendArray() {
        // Add a test case for appendArray method here
    }

    @Test
    public void testAppendComparable() {
        // Add a test case for appendComparable method here
    }

    @Test
    public void testAppendNumericTypes() {
        // Add a test case for appendNumericTypes method here
    }

    @Test
    public void testAppendBoolean() {
        // Add a test case for appendBoolean method here
    }

    @Test
    public void testToComparison() {
        // Add a test case for toComparison method here
    }
}