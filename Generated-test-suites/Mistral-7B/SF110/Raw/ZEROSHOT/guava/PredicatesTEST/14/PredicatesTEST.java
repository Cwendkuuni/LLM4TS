package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

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
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply("abc"));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply("abc"));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> notEven = Predicates.not(isEven);

        assertFalse(notEven.apply(2));
        assertTrue(notEven.apply(1));
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
        Predicate<Integer> and = Predicates.and(isEven, isPositive);

        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertTrue(and.apply(2));
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
        Predicate<Integer> or = Predicates.or(isEven, isOdd);

        assertFalse(or.apply(-1));
        assertFalse(or.apply(0));
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);
        assertFalse(isEqualToFive.apply(4));
        assertFalse(isEqualToFive.apply(6));
        assertTrue(isEqualToFive.apply(5));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab.*c");
        assertFalse(containsPattern.apply("abcdefg"));
        assertTrue(containsPattern.apply("abcdabcdabcdabcd"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        assertFalse(isInstanceOfString.apply(42));
        assertTrue(isInstanceOfString.apply("hello"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);
        assertFalse(isAssignableFromNumber.apply(String.class));
        assertTrue(isAssignableFromNumber.apply(Integer.class));
        assertTrue(isAssignableFromNumber.apply(Number[].class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isInNumbers = Predicates.in(numbers);

        assertFalse(isInNumbers.apply(-1));
        assertFalse(isInNumbers.apply(6));
        assertTrue(isInNumbers.apply(1));
        assertTrue(isInNumbers.apply(5));
    }

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOneMoreEven = Predicates.compose(isEven, addOne);

        assertFalse(isOneMoreEven.apply(1));
        assertTrue(isOneMoreEven.apply(2));
        assertTrue(isOneMoreEven.apply(4));
    }
}