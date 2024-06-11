package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_simpleObjects() {
        Object lhs = new Object();
        Object rhs = new Object();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new String("test");
        rhs = new String("test");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new Integer(1);
        rhs = new Integer(1);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_differentClasses() {
        Object lhs = new String("test");
        Object rhs = new Integer(1);
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertNotEquals(0, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_nullArguments() {
        Object lhs = null;
        Object rhs = new Object();
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_incompatibleTypes() {
        Object lhs = new Integer(1);
        Object rhs = new String("test");
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test
    public void testReflectionCompare_excludeFields() {
        Object lhs = new TestClass("test1", "test2");
        Object rhs = new TestClass("test1", "test3");
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field2"));
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testReflectionCompare_transients() {
        Object lhs = new TestClassTransient("test1", true);
        Object rhs = new TestClassTransient("test2", false);
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testAppend_simpleTypes() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(1, 1);
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(0, comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(1.0, 1.0);
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(0, comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append('a', 'a');
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(0, comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(true, true);
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testAppend_differentValues() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(1, 2);
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(-1, comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(1.0, 2.0);
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(-1, comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append('a', 'b');
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(-1, comparison);

        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(true, false);
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_arrays() {
        Object[] lhs = {1, 2, 3};
        Object[] rhs = {1, 2, 3};
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(0, comparison);

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        comparison = compareToBuilder.toComparison();
        Assert.assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_customComparator() {
        Integer lhs = 1;
        Integer rhs = 2;
        Comparator<Integer> comparator = (l, r) -> l.compareTo(r) * -1;
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs, comparator);
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(1, comparison);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(2);
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(2, comparison);
    }

    static class TestClass {
        String field1;
        String field2;

        TestClass(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }

    static class TestClassTransient {
        String field1;
        boolean transientField;

        TestClassTransient(String field1, boolean transientField) {
            this.field1 = field1;
            this.transientField = transientField;
        }
    }
}