package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() throws Exception {
        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        lhs = new Object() {
            int value = 2;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        lhs = new Object() {
            int value = 1;
        };
        rhs = null;
        try {
            comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        } catch (NullPointerException e) {
            comparison = -1;
        }
        assertEquals(-1, comparison);

        lhs = null;
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        lhs = lhs.getClass().getSuperclass().getDeclaredConstructor().newInstance();
        FieldUtils.writeFieldValue(lhs, "value", 3, true);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, lhs.getClass());
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend() throws Exception {
        CompareToBuilder builder = new CompareToBuilder();

        Object lhs = new Object() {
            int value = 1;
        };
        Object rhs = new Object() {
            int value = 2;
        };
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);

        lhs = new Object() {
            int value = 2;
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(1, comparison);

        lhs = new Object() {
            int value = 1;
        };
        rhs = null;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);

        lhs = null;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(1, comparison);

        lhs = lhs.getClass().getSuperclass().getDeclaredConstructor().newInstance();
        FieldUtils.writeFieldValue(lhs, "value", 3, true);
        builder = new CompareToBuilder();
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        lhs = new int[]{1, 2, 3};
        Object[] rhsInt = new Object[]{1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhs, rhsInt);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        Object[] rhsLong = new Object[]{1L, 2L, 3L};
        builder = new CompareToBuilder();
        builder.append(lhs, rhsLong);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        Object[] rhsDouble = new Object[]{1.0, 2.0, 3.0};
        builder = new CompareToBuilder();
        builder.append(lhs, rhsDouble);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        Object[] rhsFloat = new Object[]{1.0f, 2.0f, 3.0f};
        builder = new CompareToBuilder();
        builder.append(lhs, rhsFloat);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        Object[] rhsBoolean = new Object[]{true, false, true};
        builder = new CompareToBuilder();
        builder.append(lhs, rhsBoolean);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        Object[] rhsString = new Object[]{"abc", "def", "ghi"};
        builder = new CompareToBuilder();
        builder.append(lhs, rhsString);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        Object[] rhsCustom = new Object[]{new CustomClass(1), new CustomClass(2), new CustomClass(3)};
        builder = new CompareToBuilder();
        builder.append(lhs, rhsCustom, new CustomComparator());
        comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    private static class CustomClass {
        int value;

        public CustomClass(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Object o) {
            CustomClass that = (CustomClass)o;
            return Integer.compare(this.value, that.value);
        }
    }

    private static class CustomComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            CustomClass lhs = (CustomClass)o1;
            CustomClass rhs = (CustomClass)o2;
            return Integer.compare(lhs.value, rhs.value);
        }
    }
}