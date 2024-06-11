package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(1));
        assertTrue(alwaysTruePredicate.apply(2));
        assertTrue(alwaysTruePredicate.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(1));
        assertFalse(alwaysFalsePredicate.apply(2));
        assertFalse(alwaysFalsePredicate.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNullPredicate = Predicates.isNull();
        assertNull(isNullPredicate.apply(null));
        assertFalse(isNullPredicate.apply(1));
        assertFalse(isNullPredicate.apply(2));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNullPredicate = Predicates.notNull();
        assertNull(notNullPredicate.apply(null));
        assertTrue(notNullPredicate.apply(1));
        assertTrue(notNullPredicate.apply(2));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> notEvenPredicate = Predicates.not(isEvenPredicate);

        assertFalse(notEvenPredicate.apply(2));
        assertTrue(notEvenPredicate.apply(1));
        assertTrue(notEvenPredicate.apply(3));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isPositivePredicate);

        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));
    }

    @Test
    public void testAndWithIterable() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };

        List<Predicate<Integer>> predicates = Arrays.asList(isEvenPredicate, isPositivePredicate);

        Predicate<Integer> andPredicate = Predicates.and(predicates);

        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));
    }

    @Test
    public void testAndWithSingleArgument() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isPositivePredicate);

        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));

        Predicate<Integer> andSingleArgumentPredicate = Predicates.and(isEvenPredicate);

        assertFalse(andSingleArgumentPredicate.apply(-1));
        assertFalse(andSingleArgumentPredicate.apply(0));
        assertTrue(andSingleArgumentPredicate.apply(2));
        assertTrue(andSingleArgumentPredicate.apply(4));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(isEvenPredicate, isPositivePredicate);

        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertTrue(orPredicate.apply(4));
    }

    @Test
    public void testOrWithIterable() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };

        List<Predicate<Integer>> predicates = Arrays.asList(isEvenPredicate, isPositivePredicate);

        Predicate<Integer> orPredicate = Predicates.or(predicates);

        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertTrue(orPredicate.apply(4));
    }

    @Test
    public void testOrWithSingleArgument() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> orSingleArgumentPredicate = Predicates.or(isEvenPredicate);

        assertFalse(orSingleArgumentPredicate.apply(-1));
        assertFalse(orSingleArgumentPredicate.apply(0));
        assertTrue(orSingleArgumentPredicate.apply(1));
        assertTrue(orSingleArgumentPredicate.apply(2));
        assertTrue(orSingleArgumentPredicate.apply(3));
        assertTrue(orSingleArgumentPredicate.apply(4));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFivePredicate = Predicates.equalTo(5);

        assertFalse(isEqualToFivePredicate.apply(1));
        assertFalse(isEqualToFivePredicate.apply(2));
        assertFalse(isEqualToFivePredicate.apply(3));
        assertFalse(isEqualToFivePredicate.apply(4));
        assertTrue(isEqualToFivePredicate.apply(5));
        assertFalse(isEqualToFivePredicate.apply(6));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isStringPredicate = Predicates.instanceOf(String.class);

        assertFalse(isStringPredicate.apply(1));
        assertFalse(isStringPredicate.apply(2));
        assertFalse(isStringPredicate.apply(3));
        assertTrue(isStringPredicate.apply(""));
        assertTrue(isStringPredicate.apply("hello"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isBooleanPredicate = Predicates.assignableFrom(Boolean.class);

        assertFalse(isBooleanPredicate.apply(String.class));
        assertFalse(isBooleanPredicate.apply(Integer.class));
        assertTrue(isBooleanPredicate.apply(Boolean.TYPE));
        assertTrue(isBooleanPredicate.apply(Boolean.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> inPredicate = Predicates.in(numbers);

        assertFalse(inPredicate.apply(-1));
        assertFalse(inPredicate.apply(0));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertTrue(inPredicate.apply(4));
        assertTrue(inPredicate.apply(5));
        assertFalse(inPredicate.apply(6));
    }

    @Test
    public void testCompose() {
        Function<Integer, Boolean> isEvenFunction = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isEvenPredicate = Predicates.alwaysTrue();

        Predicate<Integer> composedPredicate = Predicates.compose(isEvenPredicate, isEvenFunction);

        assertFalse(composedPredicate.apply(-1));
        assertFalse(composedPredicate.apply(0));
        assertTrue(composedPredicate.apply(1));
        assertTrue(composedPredicate.apply(2));
        assertTrue(composedPredicate.apply(3));
        assertTrue(composedPredicate.apply(4));
        assertTrue(composedPredicate.apply(5));
        assertTrue(composedPredicate.apply(6));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern("a.*b");

        assertFalse(containsPatternPredicate.apply("hello"));
        assertFalse(containsPatternPredicate.apply("world"));
        assertTrue(containsPatternPredicate.apply("hello world"));
        assertTrue(containsPatternPredicate.apply("hello aaaabbbb"));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPatternFromStringPredicate = Predicates.containsPattern("a.*b");

        assertFalse(containsPatternFromStringPredicate.apply("hello"));
        assertFalse(containsPatternFromStringPredicate.apply("world"));
        assertTrue(containsPatternFromStringPredicate.apply("hello world"));
        assertTrue(containsPatternFromStringPredicate.apply("hello aaaabbbb"));
    }
}