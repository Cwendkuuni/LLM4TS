package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testComparison_Objects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        lhs = new Object();
        rhs = null;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertNotNull(comparison);
        assertEquals(-1, comparison);

        lhs = null;
        rhs = new Object();
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertNotNull(comparison);
        assertEquals(1, comparison);
    }

    @Test
    public void testComparison_PrimitiveTypes() {
        CompareToBuilder builder = new CompareToBuilder();

        builder.append((byte) 5, (byte) 10);
        int comparison = builder.toComparison();
        assertEquals(-5, comparison);

        builder.append((short) 5, (short) 10);
        comparison = builder.toComparison();
        assertEquals(-5, comparison);

        builder.append((int) 5, (int) 10);
        comparison = builder.toComparison();
        assertEquals(-5, comparison);

        builder.append((long) 5, (long) 10);
        comparison = builder.toComparison();
        assertEquals(-5, comparison);

        builder.append((float) 5.0f, (float) 10.0f);
        comparison = builder.toComparison();
        assertEquals(-5, comparison);

        builder.append((double) 5.0, (double) 10.0);
        comparison = builder.toComparison();
        assertEquals(-5.0, comparison, 0.001);
    }

    @Test
    public void testComparison_Arrays() {
        CompareToBuilder builder = new CompareToBuilder();
        Object[] lhs = {1, 2};
        Object[] rhs = {1, 2};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);

        lhs = new Object[]{1, 2};
        rhs = null;
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertNotNull(comparison);
        assertEquals(-1, comparison);

        lhs = null;
        rhs = new Object[]{1, 2};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertNotNull(comparison);
        assertEquals(1, comparison);

        lhs = new Object[]{1, 2};
        rhs = new Object[]{2, 1};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(0, comparison);

        lhs = new Object[]{1, 2, 3};
        rhs = new Object[]{1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testComparison_NullLhs() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        builder.append(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testComparison_LhsNotInstanceOfRhs() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new String("abc");
        Object rhs = new Integer(1);
        builder.append(lhs, rhs);
    }

    @Test
    public void testReflectionComparison() {
        Object lhs = new Person("John", 30);
        Object rhs = new Person("Jane", 25);

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-5, comparison);
    }

    @Test
    public void testReflectionComparison_ExcludeFields() {
        Object lhs = new Person("John", 30);
        Object rhs = new Person("Jane", 25);

        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("age"));
        assertEquals(0, comparison);
    }

    @Test
    public void testReflectionComparison_CompareTransients() {
        Object lhs = new Person("John", 30);
        Object rhs = new Person("Jane", 25);

        Person.setTransient(false); // set all fields transient to false for testing
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(-5, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionComparison_NullLhs() {
        Object lhs = null;
        Object rhs = new Person("Jane", 25);
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionComparison_NullRhs() {
        Object lhs = new Person("John", 30);
        Object rhs = null;
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionComparison_LhsNotInstanceOfRhs() {
        Object lhs = new String("abc");
        Object rhs = new Person("Jane", 25);
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    static class Person {
        private String name;
        private int age;

        @SuppressWarnings("WeakerAccess")
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public static void setTransient(boolean value) {
            AccessibleObject.setAccessible(Person.class.getDeclaredField("name"), true);
            AccessibleObject.setAccessible(Person.class.getDeclaredField("age"), true);
            Field[] fields = Person.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("name")) {
                    field.setAccessible(value);
                }
                if (field.getName().equals("age")) {
                    field.setAccessible(value);
                }
            }
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}