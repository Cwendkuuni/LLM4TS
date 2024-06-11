package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testComparisonMethod() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Integer result = compareToBuilder.build();
        assertEquals(0, result.intValue());
    }

    @Test
    public void testReflectionCompareMethodWithNullArguments() {
        try {
            CompareToBuilder.reflectionCompare(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompareMethodWithDifferentClasses() {
        Object lhs = new Object();
        Object rhs = new String("test");
        try {
            CompareToBuilder.reflectionCompare(lhs, rhs);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompareMethodWithExcludeFields() {
        Object lhs = new Object() {
            private int a = 1;
            private String b = "test";
        };
        Object rhs = new Object() {
            private int a = 2;
            private String b = "test";
        };
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"b"});
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompareMethodWithCompareTransientsTrue() {
        Object lhs = new Object() {
            private int a = 1;
            private int b = 2;
            private volatile int c = 3;
        };
        Object rhs = new Object() {
            private int a = 1;
            private int b = 2;
            private volatile int c = 3;
        };
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(0, result);
    }

    @Test
    public void testAppendSuperMethodWithNegativeComparisonValue() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(-5);
        Integer result = compareToBuilder.build();
        assertEquals(-5, result.intValue());
    }

    @Test
    public void testAppendMethodWithNullArguments() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(null, null);
        Integer result = compareToBuilder.build();
        assertEquals(-1, result.intValue());
    }

    @Test
    public void testAppendMethodWithPrimitiveArrays() {
        byte[] lhs = new byte[]{-1, 0, 1};
        byte[] rhs = new byte[]{0, 1, 1};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        Integer result = compareToBuilder.build();
        assertEquals(-3, result.intValue());
    }

    @Test
    public void testAppendMethodWithObjectArrays() {
        Object[] lhs = new Object[]{"test1", "test2", "test3"};
        Object[] rhs = new Object[]{"test2", "test1", "test3"};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        Integer result = compareToBuilder.build();
        assertEquals(-1, result.intValue());
    }

    @Test
    public void testAppendMethodWithComparators() {
        Object lhs = new Object() {
            @Override
            public int compareTo(Object o) {
                String lhsName = (String) ((String) this.getClass().getName()).split("\\$")[1];
                String rhsName = (String) ((String) o.getClass().getName()).split("\\$")[1];
                return lhsName.compareTo(rhsName);
            }
        };
        Object rhs = new Object() {
            @Override
            public int compareTo(Object o) {
                String lhsName = (String) ((String) this.getClass().getName()).split("\\$")[1];
                String rhsName = (String) ((String) o.getClass().getName()).split("\\$")[1];
                return rhsName.compareTo(lhsName);
            }
        };
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((String) o1.getClass().getName()).compareTo(((String) o2.getClass().getName()));
            }
        });
        Integer result = compareToBuilder.build();
        assertEquals(1, result.intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendMethodWithNonComparableObject() {
        Object lhs = new Object();
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                throw new IllegalArgumentException();
            }
        };
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, lhs, comparator);
        compareToBuilder.build();
    }

    @Test
    public void testAppendMethodWithArraysOfDifferentLengths() {
        int[] lhs = new int[]{1, 2, 3};
        int[] rhs = new int[]{1, 2};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        Integer result = compareToBuilder.build();
        assertEquals(-1, result.intValue());
    }

    @Test
    public void testAppendMethodWithArraysOfSameLengthButDifferentValues() {
        int[] lhs = new int[]{1, 2, 3};
        int[] rhs = new int[]{2, 1, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        Integer result = compareToBuilder.build();
        assertEquals(-1, result.intValue());
    }

    @Test
    public void testAppendMethodWithArraysOfSameLengthButSameValues() {
        int[] lhs = new int[]{1, 2, 3};
        int[] rhs = new int[]{1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        Integer result = compareToBuilder.build();
        assertEquals(0, result.intValue());
    }

}