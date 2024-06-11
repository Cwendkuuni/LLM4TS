package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testComparisonOfTwoObjects() {
        Object lhs = new Object();
        Object rhs = new Object();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.append(lhs, rhs);
        assertEquals(comparison, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoObjectsWithExcludedFields() {
        Object lhs = new AnotherClass();
        Object rhs = new AnotherClass();
        String[] excludeFields = {"field1", "field2"};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, AnotherClass.class, excludeFields);
        compareToBuilder.appendSuper(comparison);
        compareToBuilder.append(lhs.getClass().getDeclaredField("field3").get(lhs), rhs.getClass().getDeclaredField("field3").get(rhs));
        assertEquals(comparison, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoObjectsWithTransients() {
        Object lhs = new TransientClass();
        Object rhs = new TransientClass();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        compareToBuilder.appendSuper(comparison);
        compareToBuilder.append(lhs.getClass().getDeclaredField("transientField").get(lhs), rhs.getClass().getDeclaredField("transientField").get(rhs));
        assertEquals(comparison, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 4};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoArraysWithCustomComparator() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {3, 2, 1};
        Comparator<int[]> comparator = new Comparator<int[]>() {
            @Override
            public int compare(int[] lhs, int[] rhs) {
                int result = 0;
                for (int i = 0; i < lhs.length && result == 0; ++i) {
                    result = Integer.compare(lhs[i], rhs[i]);
                }
                return result;
            }
        };
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, comparator);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoArraysWithDifferentLengths() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3, 4};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoArraysOfDifferentTypes() {
        long[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(Long.TYPE.getName().compareTo(Integer.TYPE.getName()), compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoBooleanArrays() {
        boolean[] lhs = {true, false, true};
        boolean[] rhs = {false, true, true};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoBooleanArraysWithDifferentLengths() {
        boolean[] lhs = {true};
        boolean[] rhs = {true, false};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoByteArrays() {
        byte[] lhs = {1, 2, 3};
        byte[] rhs = {1, 2, 4};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoDoubleArrays() {
        double[] lhs = {1.0, 2.0, 3.0};
        double[] rhs = {1.0, 2.0, 4.0};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoFloatArrays() {
        float[] lhs = {1.0f, 2.0f, 3.0f};
        float[] rhs = {1.0f, 2.0f, 4.0f};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoLongArrays() {
        long[] lhs = {1, 2, 3};
        long[] rhs = {1, 2, 4};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoShortArrays() {
        short[] lhs = {1, 2, 3};
        short[] rhs = {1, 2, 4};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoCharacterArrays() {
        char[] lhs = {'a', 'b', 'c'};
        char[] rhs = {'a', 'b', 'd'};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparisonOfTwoMixedArrays() {
        Object[] lhs = {1, "hello", 3.0f};
        Object[] rhs = {2, "world", 3.0f};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, null);
        assertEquals(-1, compareToBuilder.toComparison());
    }
}

class AnotherClass {
    String field1 = "foo";
    String field2 = "bar";
    int field3 = 1;
}

class TransientClass {
    @SuppressWarnings("transient")
    transient int transientField = 1;
}