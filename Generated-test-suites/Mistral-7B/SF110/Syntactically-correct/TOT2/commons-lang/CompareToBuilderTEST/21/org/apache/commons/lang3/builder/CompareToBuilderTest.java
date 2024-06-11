package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testComparisonOfObjects() {
        CompareToBuilder cb = new CompareToBuilder();
        Object obj1 = new Object();
        Object obj2 = new Object();
        cb.append(obj1, obj2);
        int comparison = cb.toComparison();
        Assert.assertEquals(-1, comparison);
    }

    @Test
    public void testComparisonOfEqualObjects() {
        CompareToBuilder cb = new CompareToBuilder();
        Object obj1 = new Object();
        Object obj2 = obj1;
        cb.append(obj1, obj2);
        int comparison = cb.toComparison();
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testComparisonOfNullObject() {
        CompareToBuilder cb = new CompareToBuilder();
        Object obj1 = null;
        Object obj2 = new Object();
        cb.append(obj1, obj2);
        int comparison = cb.toComparison();
        Assert.assertEquals(-1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameter() {
        CompareToBuilder cb = new CompareToBuilder();
        cb.append(null, null);
    }

    @Test(expected = ClassCastException.class)
    public void testDifferentClassTypes() {
        CompareToBuilder cb = new CompareToBuilder();
        int intValue = 1;
        String stringValue = "test";
        cb.append(intValue, stringValue);
    }

    @Test
    public void testReflectionCompare() {
        Object obj1 = new TestClass();
        Object obj2 = new TestClass();
        CompareToBuilder cb = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        Assert.assertEquals(0, comparison);

        obj2.setField1(1);
        obj2.setField2("test");
        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        Assert.assertNotEquals(0, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludedField() {
        Object obj1 = new TestClass();
        Object obj2 = new TestClass();
        obj2.setField1(1);
        obj2.setField2("test");
        obj2.setField3(2);
        CompareToBuilder cb = new CompareToBuilder();
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("field3"));
        Assert.assertEquals(0, comparison);
    }

    static class TestClass {
        int field1;
        String field2;
        transient int field3;

        public void setField1(int value) {
            this.field1 = value;
        }

        public void setField2(String value) {
            this.field2 = value;
        }
    }
}