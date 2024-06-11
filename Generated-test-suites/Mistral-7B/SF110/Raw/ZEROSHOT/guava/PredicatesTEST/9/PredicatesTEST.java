package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Predicates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(null));
        assertTrue(alwaysTruePredicate.apply(1));
        assertTrue(alwaysTruePredicate.apply(2.0));
        assertTrue(alwaysTruePredicate.apply("abc"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(null));
        assertFalse(alwaysFalsePredicate.apply(1));
        assertFalse(alwaysFalsePredicate.apply(2.0));
        assertFalse(alwaysFalsePredicate.apply("abc"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertNull(isNullPredicate.apply(null));
        assertNull(isNullPredicate.apply(null));
        assertNull(isNullPredicate.apply((String)null));
        assertFalse(isNullPredicate.apply(1));
        assertFalse(isNullPredicate.apply(1));
        assertFalse(isNullPredicate.apply("abc"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();
        assertNull(notNullPredicate.apply(null));
        assertFalse(notNullPredicate.apply(null));
        assertFalse(notNullPredicate.apply((String)null));
        assertTrue(notNullPredicate.apply(1));
        assertTrue(notNullPredicate.apply(1));
        assertTrue(notNullPredicate.apply("abc"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> notTruePredicate = Predicates.not(truePredicate);

        assertFalse(notTruePredicate.apply(true));
        assertTrue(notTruePredicate.apply(false));

        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> notEvenPredicate = Predicates.not(isEvenPredicate);

        assertFalse(notEvenPredicate.apply(2));
        assertTrue(notEvenPredicate.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> positivePredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(evenPredicate, positivePredicate);

        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(evenPredicate, positivePredicate);
        Predicate<Integer> andPredicate2 = Predicates.and(predicates);
        assertTrue(andPredicate2.apply(1));
        assertTrue(andPredicate2.apply(2));
    }

    @Test
    public void testOr() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> positivePredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(evenPredicate, positivePredicate);

        assertFalse(orPredicate.apply(-1));
        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(evenPredicate, positivePredicate);
        Predicate<Integer> orPredicate2 = Predicates.or(predicates);
        assertTrue(orPredicate2.apply(0));
        assertTrue(orPredicate2.apply(1));
        assertTrue(orPredicate2.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFivePredicate = Predicates.equalTo(5);
        assertFalse(isEqualToFivePredicate.apply(1));
        assertFalse(isEqualToFivePredicate.apply(2));
        assertTrue(isEqualToFivePredicate.apply(5));
        assertFalse(isEqualToFivePredicate.apply("abc"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfStringPredicate = Predicates.instanceOf(String.class);
        assertFalse(isInstanceOfStringPredicate.apply(1));
        assertFalse(isInstanceOfStringPredicate.apply(2.0));
        assertTrue(isInstanceOfStringPredicate.apply("abc"));
        assertFalse(isInstanceOfStringPredicate.apply(new Object()));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromNumberPredicate = Predicates.assignableFrom(Number.class);
        assertFalse(isAssignableFromNumberPredicate.apply(String.class));
        assertTrue(isAssignableFromNumberPredicate.apply(Integer.class));
        assertTrue(isAssignableFromNumberPredicate.apply(Double.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Predicate<Integer> isInPredicate = Predicates.in(numbers);
        assertFalse(isInPredicate.apply(-1));
        assertFalse(isInPredicate.apply(4));
        assertTrue(isInPredicate.apply(1));
        assertTrue(isInPredicate.apply(2));
        assertTrue(isInPredicate.apply(3));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> doubleValueFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * 2;
            }
        };

        Predicate<Integer> isEvenAndDoublePredicate = Predicates.compose(isEvenPredicate, doubleValueFunction);

        assertFalse(isEvenAndDoublePredicate.apply(1));
        assertFalse(isEvenAndDoublePredicate.apply(2));
        assertTrue(isEvenAndDoublePredicate.apply(4));
        assertTrue(isEvenAndDoublePredicate.apply(6));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab.*c");
        assertFalse(containsPatternPredicate.apply("abc"));
        assertFalse(containsPatternPredicate.apply("def"));
        assertTrue(containsPatternPredicate.apply("abcd"));
        assertTrue(containsPatternPredicate.apply("abxvc"));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPatternPredicate = Predicates.containsPatternFromString("ab.*c");
        assertFalse(containsPatternPredicate.apply("abc"));
        assertFalse(containsPatternPredicate.apply("def"));
        assertTrue(containsPatternPredicate.apply("abcd"));
        assertTrue(containsPatternPredicate.apply("abxvc"));
    }
}