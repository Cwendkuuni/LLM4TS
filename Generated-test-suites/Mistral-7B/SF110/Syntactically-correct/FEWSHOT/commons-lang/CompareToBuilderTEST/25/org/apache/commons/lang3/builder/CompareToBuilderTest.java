package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testComparison() {
        Object obj1 = new Object() {
            int value = 1;
        };
        Object obj2 = new Object() {
            int value = 2;
        };

        CompareToBuilder comparator = new CompareToBuilder();
        comparator.append(obj1, obj2);
        int comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(obj2, obj1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(obj1, obj1);
        comparison = comparator.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfArrays() {
        int[] intArray1 = {1, 2, 3};
        int[] intArray2 = {1, 2, 3};
        int[] intArray3 = {1, 2, 4};

        CompareToBuilder comparator = new CompareToBuilder();
        comparator.append(intArray1, intArray2);
        int comparison = comparator.toComparison();
        assertEquals(0, comparison);

        comparator = new CompareToBuilder();
        comparator.append(intArray2, intArray1);
        comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(intArray1, intArray3);
        comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(intArray3, intArray1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonOfPrimitives() {
        int intValue1 = 1;
        int intValue2 = 2;
        long longValue1 = 1L;
        long longValue2 = 2L;
        float floatValue1 = 1.0f;
        float floatValue2 = 2.0f;
        double doubleValue1 = 1.0;
        double doubleValue2 = 2.0;
        boolean booleanValue1 = true;
        boolean booleanValue2 = false;

        CompareToBuilder comparator = new CompareToBuilder();
        comparator.append(intValue1, intValue2);
        int comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(intValue2, intValue1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(longValue1, longValue2);
        comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(longValue2, longValue1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(floatValue1, floatValue2);
        comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(floatValue2, floatValue1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(doubleValue1, doubleValue2);
        comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(doubleValue2, doubleValue1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(booleanValue1, booleanValue2);
        comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(booleanValue2, booleanValue1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonOfObjects() {
        String stringObject1 = "foo";
        String stringObject2 = "bar";

        CompareToBuilder comparator = new CompareToBuilder();
        comparator.append(stringObject1, stringObject2);
        int comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(stringObject2, stringObject1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonWithCustomComparator() {
        String stringObject1 = "foo";
        String stringObject2 = "bar";

        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        };

        CompareToBuilder comparatorBuilder = new CompareToBuilder();
        comparatorBuilder.append(stringObject1, stringObject2, comparator);
        int comparison = comparatorBuilder.toComparison();
        assertEquals(-1, comparison);

        comparatorBuilder = new CompareToBuilder();
        comparatorBuilder.append(stringObject2, stringObject1, comparator);
        comparison = comparatorBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonWithNullValues() {
        Object obj1 = new Object();
        Object obj2 = null;

        CompareToBuilder comparator = new CompareToBuilder();
        comparator.append(obj1, obj2);
        int comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(obj2, obj1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(obj1, null);
        comparison = comparator.toComparison();
        assertEquals(-1, comparison);

        comparator = new CompareToBuilder();
        comparator.append(null, obj1);
        comparison = comparator.toComparison();
        assertEquals(1, comparison);
    }

    @Test
    public void testComparisonWithSuperClasses() {
        Object obj1 = new Object() {
        };
        Object obj2 = new Object() {
        };
        Class<? extends Object> superClass = obj1.getClass().getSuperclass();

        CompareToBuilder comparator = new CompareToBuilder();
        comparator.append(obj1, obj2);
        comparator.appendSuper(comparison(obj1, obj2, superClass));

        int comparison = comparator.toComparison();
        assertEquals(0, comparison);
    }

    private int comparison(Object lhs, Object rhs, Class<?> reflectUpToClass) {
        return CompareToBuilder.reflectionCompare(lhs, rhs, reflectUpToClass, null, new String[0]);
    }
}