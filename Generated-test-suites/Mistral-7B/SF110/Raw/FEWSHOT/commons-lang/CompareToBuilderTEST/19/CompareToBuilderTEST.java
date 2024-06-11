package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testComparisonOfObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
        int comparison = builder.build();
        assertEquals(-1, comparison);

        obj2 = obj1;
        builder = new CompareToBuilder();
        builder.append(obj1, obj2);
        comparison = builder.build();
        assertEquals(0, comparison);

        obj2 = null;
        builder = new CompareToBuilder();
        builder.append(obj1, obj2);
        comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(arr1, arr2);
        int comparison = builder.build();
        assertEquals(0, comparison);

        arr2 = new int[]{1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(arr1, arr2);
        comparison = builder.build();
        assertEquals(-1, comparison);

        arr2 = null;
        builder = new CompareToBuilder();
        builder.append(arr1, arr2);
        comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfPrimitives() {
        int intValue1 = 1;
        int intValue2 = 2;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(intValue1, intValue2);
        int comparison = builder.build();
        assertEquals(-1, comparison);

        intValue2 = intValue1;
        builder = new CompareToBuilder();
        builder.append(intValue1, intValue2);
        comparison = builder.build();
        assertEquals(0, comparison);

        intValue2 = 3;
        builder = new CompareToBuilder();
        builder.append(intValue1, intValue2);
        comparison = builder.build();
        assertEquals(1, comparison);

        int intValue3 = 3;
        builder = new CompareToBuilder();
        builder.append(intValue1, intValue3);
        comparison = builder.build();
        assertEquals(1, comparison);

        intValue3 = 2;
        builder = new CompareToBuilder();
        builder.append(intValue1, intValue3);
        comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfCollections() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(list1, list2);
        int comparison = builder.build();
        assertEquals(0, comparison);

        list2 = Arrays.asList(1, 2, 4);
        builder = new CompareToBuilder();
        builder.append(list1, list2);
        comparison = builder.build();
        assertEquals(-1, comparison);

        list2 = null;
        builder = new CompareToBuilder();
        builder.append(list1, list2);
        comparison = builder.build();
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonWithExcludedFields() {
        class TestClass {
            private int excludedField = 1;

            @Override
            public String toString() {
                return "TestClass{" +
                        "excludedField=" + excludedField +
                        '}';
            }
        }

        TestClass obj1 = new TestClass();
        TestClass obj2 = new TestClass();

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2, "excludedField");
        int comparison = builder.build();

        assertEquals(0, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgument() {
        Object obj1 = null;
        Object obj2 = new Object();

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
    }

    @Test(expected = ClassCastException.class)
    public void testTypeMismatch() {
        String str1 = "a";
        int intValue1 = 1;

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(str1, intValue1);
    }
}