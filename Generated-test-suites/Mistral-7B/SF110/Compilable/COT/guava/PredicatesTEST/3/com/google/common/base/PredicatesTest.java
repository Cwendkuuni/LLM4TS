package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(2));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertNull(predicate.apply(null));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply("hello"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> predicate = Predicates.notNull();
        assertNull(predicate.apply(null));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply("hello"));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullInput() {
        Predicate<Object> predicate = Predicates.notNull();
        predicate.apply(null);
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicateTrue = Predicates.alwaysTrue();
        Predicate<Boolean> predicateFalse = Predicates.alwaysFalse();
        Predicate<Boolean> notPredicateTrue = Predicates.not(predicateTrue);
        Predicate<Boolean> notPredicateFalse = Predicates.not(predicateFalse);

        assertFalse(notPredicateTrue.apply(true));
        assertTrue(notPredicateTrue.apply(false));

        assertFalse(notPredicateFalse.apply(true));
        assertFalse(notPredicateFalse.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicateOne = Predicates.alwaysTrue();
        Predicate<Integer> predicateTwo = Predicates.alwaysFalse();
        Predicate<Integer> andPredicate = Predicates.and(predicateOne, predicateTwo);

        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));

        Predicate<Integer> predicateThree = Predicates.equalTo(1);
        Predicate<Integer> andPredicateThree = Predicates.and(predicateThree, predicateOne);

        assertTrue(andPredicateThree.apply(1));
        assertFalse(andPredicateThree.apply(2));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicateOne = Predicates.alwaysTrue();
        Predicate<Integer> predicateTwo = Predicates.alwaysFalse();
        Predicate<Integer> orPredicate = Predicates.or(predicateOne, predicateTwo);

        assertTrue(orPredicate.apply(null));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));

        Predicate<Integer> predicateThree = Predicates.notNull();
        Predicate<Integer> orPredicateThree = Predicates.or(predicateThree, Predicates.alwaysFalse());

        assertTrue(orPredicateThree.apply(1));
        assertTrue(orPredicateThree.apply(2));
        assertFalse(orPredicateThree.apply(null));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> predicateOne = Predicates.equalTo(1);
        Predicate<Integer> predicateTwo = Predicates.equalTo(2);
        Predicate<Integer> predicateThree = Predicates.equalTo(1);

        assertFalse(predicateOne.apply(2));
        assertTrue(predicateOne.apply(1));

        assertFalse(predicateTwo.apply(1));
        assertTrue(predicateTwo.apply(2));

        assertTrue(predicateThree.apply(1));
        assertFalse(predicateThree.apply(2));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfIntPredicate = Predicates.instanceOf(Integer.class);
        Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(String.class);

        assertFalse(instanceOfIntPredicate.apply("hello"));
        assertTrue(instanceOfIntPredicate.apply(1));

        assertFalse(instanceOfStringPredicate.apply(1));
        assertTrue(instanceOfStringPredicate.apply("hello"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromIntegerPredicate = Predicates.assignableFrom(Integer.class);
        Predicate<Class<?>> assignableFromStringPredicate = Predicates.assignableFrom(String.class);

        assertFalse(assignableFromIntegerPredicate.apply(String.class));
        assertTrue(assignableFromIntegerPredicate.apply(Integer.class));

        assertFalse(assignableFromStringPredicate.apply(Integer.class));
        assertTrue(assignableFromStringPredicate.apply(String.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inPredicate = Predicates.in(list);

        assertFalse(inPredicate.apply(-1));
        assertTrue(inPredicate.apply(1));
        assertFalse(inPredicate.apply(6));

        Predicate<Integer> inPredicateEmpty = Predicates.in(Collections.emptyList());
        assertFalse(inPredicateEmpty.apply(1));
        assertFalse(inPredicateEmpty.apply(2));
    }

    @Test
    public void testCompose() {
        Predicate<Boolean> isTrue = Predicates.alwaysTrue();
        Function<Integer, Boolean> isPositive = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> composePredicate = Predicates.compose(isTrue, isPositive);

        assertFalse(composePredicate.apply(-1));
        assertTrue(composePredicate.apply(1));
        assertTrue(composePredicate.apply(2));
    }

    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern("ab");
        Predicate<CharSequence> containsPatternPredicateEmpty = Predicates.containsPattern("");

        assertFalse(containsPatternPredicate.apply("hello"));
        assertTrue(containsPatternPredicate.apply("hello world"));

        assertFalse(containsPatternPredicateEmpty.apply("hello"));
        assertFalse(containsPatternPredicateEmpty.apply("hello world"));
    }
}