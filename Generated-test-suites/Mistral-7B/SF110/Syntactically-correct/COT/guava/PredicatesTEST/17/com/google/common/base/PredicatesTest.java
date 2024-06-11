package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.assignableFrom;
import static com.google.common.base.Predicates.contains;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.isNull;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.base.Predicates.or;
import static com.google.common.base.Predicates.or;
import static com.google.common.base.Predicates.or;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(null));
        assertTrue(alwaysTruePredicate.apply(1));
        assertTrue(alwaysTruePredicate.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(null));
        assertFalse(alwaysFalsePredicate.apply(1));
        assertFalse(alwaysFalsePredicate.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNullPredicate = Predicates.isNull();
        assertFalse(isNullPredicate.apply(1));
        assertFalse(isNullPredicate.apply(new Object()));
        assertTrue(isNullPredicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNullPredicate = Predicates.notNull();
        assertFalse(notNullPredicate.apply(null));
        assertTrue(notNullPredicate.apply(1));
        assertTrue(notNullPredicate.apply(new Object()));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isNullPredicate = Predicates.isNull();
        Predicate<Integer> notIsNullPredicate = Predicates.not(isNullPredicate);

        assertFalse(notIsNullPredicate.apply(null));
        assertTrue(notIsNullPredicate.apply(1));
        assertTrue(notIsNullPredicate.apply(new Object()));

        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        Predicate<Integer> notAlwaysTruePredicate = Predicates.not(alwaysTruePredicate);

        assertTrue(notAlwaysTruePredicate.apply(null));
        assertFalse(notAlwaysTruePredicate.apply(1));
        assertFalse(notAlwaysTruePredicate.apply(new Object()));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> evenNumberPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> greaterThanZeroPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(evenNumberPredicate, greaterThanZeroPredicate);

        assertFalse(andPredicate.apply(-2));
        assertFalse(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));

        List<Predicate<Integer>> predicatesList = Arrays.asList(evenNumberPredicate, greaterThanZeroPredicate);
        Predicate<Integer> andPredicateList = Predicates.and(predicatesList);

        assertFalse(andPredicateList.apply(-2));
        assertFalse(andPredicateList.apply(1));
        assertTrue(andPredicateList.apply(2));
        assertTrue(andPredicateList.apply(4));
    }

    @Test
    public void testOr() {
        Predicate<Integer> evenNumberPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> greaterThanZeroPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(evenNumberPredicate, greaterThanZeroPredicate);

        assertFalse(orPredicate.apply(-2));
        assertTrue(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(4));

        List<Predicate<Integer>> predicatesList = Arrays.asList(evenNumberPredicate, greaterThanZeroPredicate);
        Predicate<Integer> orPredicateList = Predicates.or(predicatesList);

        assertFalse(orPredicateList.apply(-2));
        assertTrue(orPredicateList.apply(0));
        assertFalse(orPredicateList.apply(1));
        assertTrue(orPredicateList.apply(2));
        assertTrue(orPredicateList.apply(4));
    }

    @Test
    public void testEqualsTo() {
        Predicate<Integer> equalsToFive = Predicates.equalTo(5);
        assertFalse(equalsToFive.apply(1));
        assertFalse(equalsToFive.apply(4));
        assertTrue(equalsToFive.apply(5));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isString = Predicates.instanceOf(String.class);
        assertFalse(isString.apply(1));
        assertFalse(isString.apply(new Object()));
        assertTrue(isString.apply("Hello World"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isStringOrNumber = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> isNumber = Predicates.assignableFrom(Number.class);

        assertFalse(isStringOrNumber.apply(Integer.class));
        assertTrue(isStringOrNumber.apply(String.class));
        assertTrue(isStringOrNumber.apply(Number.class));

        assertFalse(isNumber.apply(String.class));
        assertTrue(isNumber.apply(Integer.class));
        assertTrue(isNumber.apply(Number.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isInListPredicate = Predicates.in(numbers);

        assertFalse(isInListPredicate.apply(0));
        assertFalse(isInListPredicate.apply(6));
        assertTrue(isInListPredicate.apply(1));
        assertTrue(isInListPredicate.apply(2));
        assertTrue(isInListPredicate.apply(3));
        assertTrue(isInListPredicate.apply(4));
        assertTrue(isInListPredicate.apply(5));
    }

    @Test
    public void testCompose() {
        Predicate<String> isEmpty = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable final String string) {
                return string == null || string.isEmpty();
            }
        };

        Function<Integer, String> toStringFunction = new Function<Integer, String>() {
            @Override
            public String apply(@Nullable final Integer integer) {
                return String.valueOf(integer);
            }
        };

        Predicate<Integer> isEmptyStringPredicate = Predicates.compose(isEmpty, toStringFunction);

        assertFalse(isEmptyStringPredicate.apply(1));
        assertTrue(isEmptyStringPredicate.apply(0));
        assertFalse(isEmptyStringPredicate.apply(2));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(Pattern.compile("\\d+"));

        assertFalse(containsPatternPredicate.apply("hello"));
        assertTrue(containsPatternPredicate.apply("123hello"));
        assertTrue(containsPatternPredicate.apply("hello123"));
        assertTrue(containsPatternPredicate.apply("123hello123"));
    }

    @Test(expected = AssertionError.class)
    public void testContainsPatternNullInput() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(Pattern.compile("\\d+"));
        containsPatternPredicate.apply(null);
    }

    @Test
    public void testContainsPatternNullPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern((Pattern)null);
        containsPatternPredicate.apply("hello");
    }
}