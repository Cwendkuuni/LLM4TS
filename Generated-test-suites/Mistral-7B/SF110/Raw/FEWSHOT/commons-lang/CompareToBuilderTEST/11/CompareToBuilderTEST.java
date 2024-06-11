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
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoObjectsWithExcludedFields() {
        Object lhs = new ObjectWithExcludedFields("foo", "bar");
        Object rhs = new ObjectWithExcludedFields("bar", "foo");
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"field1"});
        compareToBuilder.append(lhs, rhs, new String[]{"field1"});
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoObjectsWithDifferentClasses() {
        Object lhs = new ObjectWithLongField();
        Object rhs = new ObjectWithIntField();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, ObjectWithLongField.class, new String[0]);
        compareToBuilder.appendSuper(comparison);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, ObjectWithIntField.class, new String[0]);
        compareToBuilder.append(lhs, rhs);
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, IntComparator.INSTANCE);
        compareToBuilder.append(lhs, rhs, IntComparator.INSTANCE);
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoArraysWithDifferentLengths() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3, 4};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, IntComparator.INSTANCE);
        compareToBuilder.append(lhs, rhs);
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoArraysWithCustomComparator() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {3, 2, 1};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, IntArrayComparator.INSTANCE);
        compareToBuilder.append(lhs, rhs, IntArrayComparator.INSTANCE);
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoPrimitiveTypes() {
        int lhs = 1;
        int rhs = 2;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = Comparator.comparingInt(i -> i).compare(lhs, rhs);
        compareToBuilder.append(lhs, rhs);
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoPrimitiveTypesWithDifferentSigns() {
        int lhs = 1;
        int rhs = -1;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = Comparator.comparingInt(i -> i).compare(lhs, rhs);
        compareToBuilder.append(lhs, rhs);
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test
    public void testComparisonOfTwoNullObjects() {
        Object lhs = null;
        Object rhs = null;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.appendSuper(comparison);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.append(lhs, rhs);
        int comparisonBuilt = compareToBuilder.toComparison();
        assertEquals(comparison, comparisonBuilt);
    }

    @Test(expected = NullPointerException.class)
    public void testComparisonOfDifferentTypes() {
        Object lhs = new Object();
        int rhs = 1;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.appendSuper(comparison);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.append(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testComparisonOfDifferentClasses() {
        Object lhs = new ObjectWithLongField();
        Object rhs = new ObjectWithIntField();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, ObjectWithLongField.class, new String[0]);
        compareToBuilder.appendSuper(comparison);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, ObjectWithIntField.class, new String[0]);
        compareToBuilder.append(lhs, rhs);
    }
}

class ObjectWithExcludedFields {
    String field1;
    String field2;

    ObjectWithExcludedFields(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
}

class ObjectWithLongField {
    long field;
}

class ObjectWithIntField {
    int field;
}

class IntComparator implements Comparator<int[]> {
    public static final IntComparator INSTANCE = new IntComparator();

    @Override
    public int compare(int[] o1, int[] o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        int comparison = Arrays.compare(o1, o2);
        return comparison;
    }
}

class IntArrayComparator implements Comparator<int[]> {
    @Override
    public int compare(int[] o1, int[] o2) {
        int length1 = o1.length;
        int length2 = o2.length;
        int comparison = Integer.compare(length1, length2);

        if (comparison != 0) {
            return comparison;
        }

        for (int i = 0; i < length1 && comparison == 0; ++i) {
            comparison = Arrays.compare(o1, o2);
            if (comparison != 0) {
                break;
            }
        }

        return comparison;
    }
}