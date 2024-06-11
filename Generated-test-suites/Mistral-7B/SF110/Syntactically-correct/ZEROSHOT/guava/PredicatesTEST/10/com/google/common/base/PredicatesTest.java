package com.example.test.predicates;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<String> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertNull(isNull.apply((String) null));
        assertFalse(isNull.apply(""));
        assertFalse(isNull.apply(""));
    }

    @Test
    public void testNotNull() {
        Predicate<String> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertNull(notNull.apply((String) null));
        assertNotNull(notNull.apply(""));
        assertNotNull(notNull.apply("hello"));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = Predicates.not(isEven);
        assertFalse(isOdd.apply(0));
        assertTrue(isOdd.apply(1));
        assertTrue(isOdd.apply(3));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> isEvenAndPositive = Predicates.and(isEven, isPositive);
        assertFalse(isEvenAndPositive.apply(-1));
        assertFalse(isEvenAndPositive.apply(0));
        assertTrue(isEvenAndPositive.apply(2));
        assertTrue(isEvenAndPositive.apply(3));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        Predicate<Integer> isEvenOrOdd = Predicates.or(isEven, isOdd);
        assertFalse(isEvenOrOdd.apply(-1));
        assertFalse(isEvenOrOdd.apply(0));
        assertTrue(isEvenOrOdd.apply(1));
        assertTrue(isEvenOrOdd.apply(2));
    }

    @Test
    public void testEquals() {
        Predicate<String> equalsToString = Predicates.equalTo("hello");
        assertTrue(equalsToString.apply("hello"));
        assertFalse(equalsToString.apply("world"));
        assertFalse(equalsToString.apply(null));
        assertFalse(equalsToString.apply(new Object()));

        Predicate<String> equalsToPattern = Predicates.containsPattern("hell.*");
        assertTrue(equalsToPattern.apply("hello"));
        assertTrue(equalsToPattern.apply("helloworld"));
        assertFalse(equalsToPattern.apply("worldhello"));
        assertFalse(equalsToPattern.apply("hello123"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isString = Predicates.instanceOf(String.class);
        assertTrue(isString.apply(""));
        assertTrue(isString.apply("hello"));
        assertFalse(isString.apply(1));
        assertFalse(isString.apply(new Object()));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
        assertTrue(isAssignableFromString.apply(String.class));
        assertTrue(isAssignableFromString.apply(StringBuilder.class));
        assertFalse(isAssignableFromString.apply(Integer.class));
        assertFalse(isAssignableFromString.apply(List.class));
    }

    @Test
    public void testIn() {
        Predicate<Integer> isInList = Predicates.in(Arrays.asList(1, 2, 3));
        assertFalse(isInList.apply(0));
        assertFalse(isInList.apply(4));
        assertTrue(isInList.apply(1));
        assertTrue(isInList.apply(2));
        assertTrue(isInList.apply(3));
    }

    @Test
    public void testCompose() {
        Predicate<String> isEmpty = new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.isEmpty();
            }
        };

        Function<String, Integer> lengthFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };

        Predicate<String> isEmptyOrLengthZero = Predicates.compose(isEmpty, lengthFunction);
        assertTrue(isEmptyOrLengthZero.apply(""));
        assertTrue(isEmptyOrLengthZero.apply(""));
        assertFalse(isEmptyOrLengthZero.apply("hello"));
    }
}