package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder appendedBuilder = builder.appendSuper(5);
        assertEquals(5, appendedBuilder.toComparison());
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
        assertEquals(-1, builder.toComparison());
    }

    @Test
    public void testAppend_Comparator() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) o1.hashCode()) - ((Integer) o2.hashCode());
            }
        });
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_longArray() {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_longArray_differentLength() {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());
    }

    @Test(expected = NullPointerException.class)
    public void testAppend_NullLhs() {
        Object lhs = null;
        Object rhs = new Object() {
            int value = 1;
        };
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testAppend_NullRhs() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = null;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
    }

    @Test
    public void testReflectionCompare_Objects() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 1;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_ObjectsDifferentTypes() {
        Object lhs = new Object() {
            int value = 1;
        };
        String rhs = "test";
        try {
            CompareToBuilder.reflectionCompare(lhs, rhs);
            fail("expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_NullLhs() {
        Object lhs = null;
        Object rhs = new Object() {
            int value = 1;
        };
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_NullRhs() {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = null;
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }
}