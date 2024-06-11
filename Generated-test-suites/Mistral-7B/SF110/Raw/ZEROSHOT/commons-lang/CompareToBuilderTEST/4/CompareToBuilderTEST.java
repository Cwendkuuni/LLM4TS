package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.ReflectionToStringBuilder;
import org.apache.commons.lang3.reflect.ToStringBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object() {
            private int a = 1;
            private int b = 2;

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this);
            }
        };

        Object obj2 = new Object() {
            private int a = 1;
            private int b = 3;

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this);
            }
        };

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj2, obj1);
        assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj1);
        assertEquals(0, comparison);

        // Test with excludeFields
        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("a"));
        assertEquals(0, comparison);

        // Test with transients
        class TestClassWithTransient {
            private int a;
            private transient int b;

            @Override
            public String toString() {
                return ToStringBuilder.reflectionToString(this);
            }
        }

        TestClassWithTransient obj3 = new TestClassWithTransient();
        obj3.a = 1;

        TestClassWithTransient obj4 = new TestClassWithTransient();
        obj4.a = 1;

        comparison = CompareToBuilder.reflectionCompare(obj3, obj4, true);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj3, obj4, ReflectionToStringBuilder.class);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj3, obj4, true, ReflectionToStringBuilder.class);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj3, obj4, true, Object.class);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj3, obj4, true, TestClassWithTransient.class);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj3, obj4, true, TestClassWithTransient.class, "b");
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj3, obj4, true, TestClassWithTransient.class, "b", "a");
        assertEquals(0, comparison);

        // Test with super compareTo
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int superCompareTo = 5;
        CompareToBuilder appendedBuilder = compareToBuilder.appendSuper(superCompareTo);
        int comparisonWithSuper = CompareToBuilder.reflectionCompare(obj1, obj2, null, TestClassWithTransient.class, "b");
        assertEquals(superCompareTo + comparisonWithSuper, appendedBuilder.toComparison());
    }
}