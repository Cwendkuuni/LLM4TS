package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int i = 1;
            long l = 2;
        };
        Object rhs = new Object() {
            int i = 1;
            long l = 3;
        };
        int comparison = CompareToBuilder. reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 2;
            long l = 2;
        };
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        lhs = new Object() {
            int i = 2;
            long l = 1;
        };
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareTransients() {
        class TestClass {
            boolean transientField = true;
            int nonTransientField = 1;
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.transientField = false;

        int comparison = CompareToBuilder. reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);

        lhs.nonTransientField = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, true);
        assertEquals(-1, comparison);

        rhs.nonTransientField = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, true);
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareExcludeFields() {
        class TestClass {
            int field1 = 1;
            int field2 = 2;
            int field3 = 3;

            @SuppressWarnings("unused")
            int excludedField = 4;
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.field2 = 3;

        int comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(0, comparison);

        lhs.field1 = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(-1, comparison);

        rhs.field1 = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareExcludeFieldsArray() {
        class TestClass {
            int field1 = 1;
            int field2 = 2;
            int field3 = 3;
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.field2 = 3;

        String[] excludeFields = {"field1"};

        int comparison = CompareToBuilder. reflectionCompare(lhs, rhs, excludeFields);
        assertEquals(0, comparison);

        lhs.field1 = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, excludeFields);
        assertEquals(0, comparison);

        lhs.field2 = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, excludeFields);
        assertEquals(0, comparison);

        lhs.field1 = 2;
        rhs.field1 = 3;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, excludeFields);
        assertEquals(-1, comparison);
    }

    @Test
    public void testReflectionCompareExcludeFieldsAndTransients() {
        class TestClass {
            boolean transientField = true;
            int nonTransientField = 1;
            int field1 = 1;
            int field2 = 2;
            int field3 = 3;
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        rhs.field2 = 3;

        int comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"transientField"}, true);
        assertEquals(0, comparison);

        lhs.nonTransientField = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"transientField"}, true);
        assertEquals(-1, comparison);

        rhs.nonTransientField = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"transientField"}, true);
        assertEquals(0, comparison);

        lhs.field1 = 2;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"transientField"}, true);
        assertEquals(0, comparison);

        lhs.field1 = 2;
        rhs.field1 = 3;
        comparison = CompareToBuilder. reflectionCompare(lhs, rhs, new String[]{"transientField"}, true);
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend() {
        Object lhs = new Object() {
            int i = 1;
        };
        Object rhs = new Object() {
            int i = 2;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(rhs, lhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendArrays() {
        Object[] lhs = new Object[]{1, 2, 3};
        Object[] rhs = new Object[]{1, 2, 4};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(rhs, lhs);
        assertEquals(1, builder.toComparison());

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendArraysWithComparator() {
        Object[] lhs = new Object[]{1, 2, 3};
        Object[] rhs = new Object[]{1, 2, 4};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        });
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(rhs, lhs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) o2).compareTo((Integer) o1);
            }
        });
        assertEquals(1, builder.toComparison());

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Object>(){});
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppendPrimitives() {
        int lhs = 1;
        int rhs = 2;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(rhs, lhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendPrimitivesWithComparator() {
        int lhs = 1;
        int rhs = 2;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return i1.compareTo(i2);
            }
        });
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(rhs, lhs, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return i2.compareTo(i1);
            }
        });
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendBoolean() {
        boolean lhs = true;
        boolean rhs = false;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(rhs, lhs);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendBooleanWithComparator() {
        boolean lhs = true;
        boolean rhs = false;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<Boolean>() {
            @Override
            public int compare(Boolean b1, Boolean b2) {
                return b1.booleanValue() ? -1 : (b2.booleanValue() ? 1 : 0);
            }
        });
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(rhs, lhs, new Comparator<Boolean>() {
            @Override
            public int compare(Boolean b1, Boolean b2) {
                return b2.booleanValue() ? -1 : (b1.booleanValue() ? 1 : 0);
            }
        });
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppendSuper() {
        Object lhs = new Object() {};
        Object rhs = new Object() {};
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(42);
        CompareToBuilder builderWithLhs = new CompareToBuilder();
        builderWithLhs.appendSuper(42);
        builderWithLhs.append(lhs, rhs);
        int comparison = builder.toComparison();
        int comparisonWithLhs = builderWithLhs.toComparison();

        assertEquals(comparisonWithLhs, comparison + 42);
    }
}