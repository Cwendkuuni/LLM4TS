package org.junit.tests;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class HashCodeBuilderTest {

    private Object object;
    private boolean testTransients;
    private int expected;

    public HashCodeBuilderTest(Object object, boolean testTransients, int expected) {
        this.object = object;
        this.testTransients = testTransients;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Person("John", 30), true, 12345},
                {new Person("Jane", 25), false, 54321},
                {new Address("123 Main St", "Anytown", "CA", 12345), true, 123456},
                {new Address("456 Elm St", "Othertown", "NY", 54321), false, 654321},
                {new Boolean(false), false, 0},
                {new Byte((byte) 0), false, 0},
                {new Character('A'), false, 65},
                {new Double(1.0), false, 1},
                {new Float(1.0f), false, 1},
                {new Integer(1), false, 1},
                {new Long(1L), false, 1},
                {new Short((short) 1), false, 1}
        });
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;
        builder.append(value);
        int expected = 0; // (1 * 37) for true
        int actual = builder.toHashCode();
        assert(expected == actual);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(object);
        int actual = builder.toHashCode();
        assert(expected == actual);
    }

    @Test
    public void testReflectionHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.reflectionHashCode(object, testTransients);
        int actual = builder.toHashCode();
        assert(expected == actual);
    }

    private static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    private static class Address {
        private final String street;
        private final String city;
        private final String state;
        private final int zipCode;

        public Address(String street, String city, String state, int zipCode) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", zipCode=" + zipCode +
                    '}';
        }
    }
}