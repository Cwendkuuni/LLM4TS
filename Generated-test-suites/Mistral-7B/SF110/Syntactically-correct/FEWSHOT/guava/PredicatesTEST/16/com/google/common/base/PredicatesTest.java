package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(10));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(10));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertFalse(notNull.apply(0));
        assertTrue(notNull.apply(1));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = Predicates.not(isEven);

        assertFalse(isOdd.apply(2));
        assertTrue(isOdd.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> isEvenAndPositive = Predicates.and(isEven, isPositive);

        assertFalse(isEvenAndPositive.apply(-1));
        assertFalse(isEvenAndPositive.apply(0));
        assertTrue(isEvenAndPositive.apply(2));
        assertTrue(isEvenAndPositive.apply(3)); // odd, but positive

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> andPredicate = Predicates.and(predicates);
        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(3));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> isEvenOrPositive = Predicates.or(isEven, isPositive);

        assertFalse(isEvenOrPositive.apply(-1));
        assertTrue(isEvenOrPositive.apply(0));
        assertTrue(isEvenOrPositive.apply(1));
        assertTrue(isEvenOrPositive.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> orPredicate = Predicates.or(predicates);
        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);

        assertFalse(isEqualToFive.apply(null));
        assertFalse(isEqualToFive.apply(1));
        assertFalse(isEqualToFive.apply(4));
        assertTrue(isEqualToFive.apply(5));
    }

    @Test
    public void testInstanceOf() {
        Class<String> stringClass = String.class;
        Predicate<Object> isString = Predicates.instanceOf(stringClass);

        assertFalse(isString.apply(null));
        assertFalse(isString.apply(1));
        assertFalse(isString.apply(""));
        assertTrue(isString.apply("hello"));
    }

    @Test
    public void testAssignableFrom() {
        Class<String> stringClass = String.class;
        Class<Number> numberClass = Number.class;

        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(stringClass);
        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(numberClass);

        assertFalse(isAssignableFromString.apply(null));
        assertFalse(isAssignableFromString.apply(int.class));
        assertTrue(isAssignableFromString.apply(String.class));
        assertFalse(isAssignableFromString.apply(numberClass));

        assertFalse(isAssignableFromNumber.apply(null));
        assertFalse(isAssignableFromNumber.apply(int.class));
        assertTrue(isAssignableFromNumber.apply(Number.class));
        assertTrue(isAssignableFromNumber.apply(Integer.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> isInCollection = Predicates.in(collection);

        assertFalse(isInCollection.apply(null));
        assertFalse(isInCollection.apply(-1));
        assertFalse(isInCollection.apply(4));
        assertTrue(isInCollection.apply(1));
        assertTrue(isInCollection.apply(2));
        assertTrue(isInCollection.apply(3));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> stringToInt = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable final String input) {
                return Integer.parseInt(input);
            }
        };

        Predicate<String> isEmpty = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable final String input) {
                return input.isEmpty();
            }
        };

        Predicate<Integer> isNegative = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input < 0;
            }
        };

        Predicate<String> isNegativeString = Predicates.compose(isNegative, stringToInt, isEmpty);

        assertFalse(isNegativeString.apply(""));
        assertFalse(isNegativeString.apply("1"));
        assertTrue(isNegativeString.apply("-1"));
        assertTrue(isNegativeString.apply("-10"));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab*c");

        assertFalse(containsPattern.apply(null));
        assertFalse(containsPattern.apply(""));
        assertFalse(containsPattern.apply("abc"));
        assertTrue(containsPattern.apply("ababc"));
        assertTrue(containsPattern.apply("abbc"));
    }
}