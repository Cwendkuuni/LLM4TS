package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CompareToBuilderTest {

    @Test
    public void testComparisonOfObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        CompareToBuilder comparator = new CompareToBuilder();
        int comparison = comparator.reflectionCompare(obj1, obj2);
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new String("test");
        CompareToBuilder comparator = new CompareToBuilder();
        int comparison = comparator.reflectionCompare(obj1, obj2);
        assertNotEquals(0, comparison);
    }

    @Test
    public void testComparisonOfObjectsWithExcludedFields() {
        class TestObject {
            private int id;
            private String name;

            public TestObject(int id, String name) {
                this.id = id;
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }

        TestObject obj1 = new TestObject(1, "test1");
        TestObject obj2 = new TestObject(1, "test2");
        CompareToBuilder comparator = new CompareToBuilder();
        int comparison = comparator.reflectionCompare(obj1, obj2, new String[]{"name"});
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfObjectsWithTransients() {
        class TestObject {
            private int id;
            private transient String name;

            public TestObject(int id, String name) {
                this.id = id;
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }

        TestObject obj1 = new TestObject(1, "test1");
        TestObject obj2 = new TestObject(1, "test2");
        CompareToBuilder comparator = new CompareToBuilder();
        int comparison = comparator.reflectionCompare(obj1, obj2, true);
        assertNotEquals(0, comparison);
    }

    @Test
    public void testComparisonOfObjectsWithSuperClass() {
        class SubClass extends ParentClass {
            private int subField;

            public SubClass(int subField) {
                this.subField = subField;
            }

            public int getSubField() {
                return subField;
            }
        }

        ParentClass parentObj1 = new ParentClass();
        SubClass subObj1 = new SubClass(1);
        SubClass subObj2 = new SubClass(2);
        ParentClass parentObj2 = new ParentClass();
        CompareToBuilder comparator = new CompareToBuilder();
        int comparison = comparator.reflectionCompare(subObj1, subObj2, ParentClass.class);
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        CompareToBuilder comparator = new CompareToBuilder();
        int comparison = comparator.reflectionCompare(arr1, arr2);
        assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfDifferentArrays() {
        int[] arr1 = {1, 2, 3};
        long[] arr2 = {1, 2, 3};
        CompareToBuilder comparator = new CompareToBuilder();
        int comparison = comparator.reflectionCompare(arr1, arr2);
        assertNotEquals(0, comparison);
    }

    @Test
    public void testComparisonOfArraysWithComparator() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 1};
        Comparator<int[]> comparator = (a, b) -> IntStream.range(0, a.length).mapToInt(i -> a[i] - b[i]).sum();
        CompareToBuilder builder = new CompareToBuilder();
        int comparison = builder.append(arr1, arr2, comparator).toComparison();
        assertNotEquals(0, comparison);
    }

}