package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.Comparator;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompareToBuilderTest {

    @Test
    public void testRefectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result); // objects of different classes with no fields are considered equal

        obj1 = new Object() {
            int value = 5;
        };
        obj2 = new Object() {
            int value = 10;
        };
        result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-5, result); // comparison of non-transient, non-static fields

        obj1 = new ComparableObject();
        obj2 = new ComparableObject();
        result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(obj1.compareTo(obj2), result); // comparison of Comparable objects using their compareTo method

        obj1 = null;
        obj2 = new Object();
        try {
            result = CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        obj1 = new Object();
        obj2 = null;
        try {
            result = CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        obj1 = new Object();
        obj2 = obj1;
        result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result); // comparison of identical objects
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        int result = builder.toComparison();
        assertEquals(5, result);

        builder = new CompareToBuilder();
        builder.appendSuper(-1);
        result = builder.toComparison();
        assertEquals(-1, result);

        builder = new CompareToBuilder();
        builder.appendSuper(0);
        result = builder.toComparison();
        assertEquals(0, result);
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();

        // test append for primitive types
        builder.append(1, 2);
        int result = builder.toComparison();
        assertEquals(-1, result);

        builder.append(5, 3);
        result = builder.toComparison();
        assertEquals(2, result);

        builder.append(5.0, 3.0);
        result = builder.toComparison();
        assertEquals(2.0, result);

        builder.append(true, false);
        result = builder.toComparison();
        assertEquals(1, result);

        // test append for arrays
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {1, 2, 3, 4};
        builder.append(arr1, arr2);
        result = builder.toComparison();
        assertEquals(0, result);

        int[] arr3 = {1, 2, 3, 4};
        int[] arr4 = {1, 2, 3, 5};
        builder.append(arr3, arr4);
        result = builder.toComparison();
        assertEquals(-1, result);

        // test append for classes with custom comparison logic
        ClassWithCustomCompare class1 = new ClassWithCustomCompare(1);
        ClassWithCustomCompare class2 = new ClassWithCustomCompare(2);
        builder.append(class1, class2);
        result = builder.toComparison();
        assertEquals(-1, result);

        // test append for collections
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
        builder.append(list1, list2);
        result = builder.toComparison();
        assertEquals(0, result);

        List<Integer> list3 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list4 = Arrays.asList(1, 2, 3, 5);
        builder.append(list3, list4);
        result = builder.toComparison();
        assertEquals(-1, result);

        // test append with comparator
        Comparator<Object> comparator = Comparator.comparingInt(o -> ((Number)o).intValue());
        builder.append(new Integer(1), new Integer(2), comparator);
        result = builder.toComparison();
        assertEquals(-1, result);
    }

    private static class ClassWithCustomCompare {
        private final int value;

        ClassWithCustomCompare(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Object o) {
            ClassWithCustomCompare other = (ClassWithCustomCompare)o;
            return this.value - other.value;
        }
    }
}

//