package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> notEven = Predicates.not(isEven);
        assertFalse(notEven.apply(2));
        assertTrue(notEven.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositiveAndEven = Predicates.and(isPositive, isEven);
        assertFalse(isPositiveAndEven.apply(-1));
        assertFalse(isPositiveAndEven.apply(1));
        assertTrue(isPositiveAndEven.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(isPositive, isEven);
        Predicate<Integer> andMultiple = Predicates.and(predicates);
        assertFalse(andMultiple.apply(-1));
        assertFalse(andMultiple.apply(1));
        assertTrue(andMultiple.apply(2));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositiveOrEven = Predicates.or(isPositive, isEven);
        assertFalse(isPositiveOrEven.apply(-1));
        assertTrue(isPositiveOrEven.apply(0));
        assertTrue(isPositiveOrEven.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(isPositive, isEven);
        Predicate<Integer> orMultiple = Predicates.or(predicates);
        assertFalse(orMultiple.apply(-1));
        assertTrue(orMultiple.apply(0));
        assertTrue(orMultiple.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);
        assertFalse(isEqualToFive.apply(1));
        assertFalse(isEqualToFive.apply(2));
        assertTrue(isEqualToFive.apply(5));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        assertFalse(isInstanceOfString.apply(1));
        assertFalse(isInstanceOfString.apply(new Object()));
        assertTrue(isInstanceOfString.apply("hello world"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);
        assertFalse(isAssignableFromNumber.apply(String.class));
        assertFalse(isAssignableFromNumber.apply(Integer.class));
        assertTrue(isAssignableFromNumber.apply(Number.class));
        assertTrue(isAssignableFromNumber.apply(Double.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isInNumbers = Predicates.in(numbers);
        assertFalse(isInNumbers.apply(-1));
        assertFalse(isInNumbers.apply(6));
        assertTrue(isInNumbers.apply(1));
        assertTrue(isInNumbers.apply(2));
        assertTrue(isInNumbers.apply(3));
        assertTrue(isInNumbers.apply(4));
        assertTrue(isInNumbers.apply(5));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Function<Integer, Integer> doubleValue = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * 2;
            }
        };
        Predicate<Integer> isEvenAndDouble = Predicates.compose(isEven, doubleValue);
        assertFalse(isEvenAndDouble.apply(1));
        assertTrue(isEvenAndDouble.apply(2));
        assertTrue(isEvenAndDouble.apply(4));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("foo");
        assertFalse(containsPattern.apply("bar"));
        assertTrue(containsPattern.apply("foo"));
        assertTrue(containsPattern.apply("foo bar"));
    }
}