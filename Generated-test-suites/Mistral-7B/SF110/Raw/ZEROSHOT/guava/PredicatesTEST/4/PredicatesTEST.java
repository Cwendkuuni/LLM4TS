package com.google.common.base;

import static org.junit.Assert.*;

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
        assertTrue(alwaysTrue.apply(2));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = (Predicate<Integer>)Predicates.alwaysFalse().not();
        Predicate<Integer> isOdd = Predicates.not(Predicates.alwaysFalse());
        Predicate<Integer> isZero = Predicates.isNull();

        assertFalse(isEven.apply(1));
        assertFalse(isEven.apply(2));
        assertTrue(isEven.apply(0));

        assertTrue(isOdd.apply(1));
        assertFalse(isOdd.apply(2));
        assertFalse(isOdd.apply(0));

        assertFalse(isZero.apply(1));
        assertTrue(isZero.apply(null));
        assertFalse(isZero.apply(2));

        Predicate<Integer> isEvenOrZero = Predicates.or(isEven, isZero);
        assertTrue(isEvenOrZero.apply(0));
        assertTrue(isEvenOrZero.apply(2));
        assertTrue(isEvenOrZero.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = (Predicate<Integer>)Predicates.alwaysFalse().not();
        Predicate<Integer> isPositive = (p) -> p > 0;

        Predicate<Integer> isEvenAndPositive = Predicates.and(isEven, isPositive);
        assertFalse(isEvenAndPositive.apply(-1));
        assertFalse(isEvenAndPositive.apply(0));
        assertTrue(isEvenAndPositive.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysFalse());
        Predicate<Integer> andPredicate = Predicates.and(predicates);
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(null));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = (Predicate<Integer>)Predicates.alwaysFalse().not();
        Predicate<Integer> isPositive = (p) -> p > 0;

        Predicate<Integer> isEvenOrPositive = Predicates.or(isEven, isPositive);
        assertFalse(isEvenOrPositive.apply(-1));
        assertTrue(isEvenOrPositive.apply(0));
        assertTrue(isEvenOrPositive.apply(1));
        assertTrue(isEvenOrPositive.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysFalse());
        Predicate<Integer> orPredicate = Predicates.or(predicates);
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(null));
    }

    @Test
    public void testEquals() {
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);
        Predicate<Integer> isEqualToNull = Predicates.equalTo(null);

        assertFalse(isEqualToFive.apply(1));
        assertTrue(isEqualToFive.apply(5));
        assertFalse(isEqualToFive.apply(2));
        assertFalse(isEqualToFive.apply(null));

        assertFalse(isEqualToNull.apply(1));
        assertFalse(isEqualToNull.apply(5));
        assertFalse(isEqualToNull.apply(2));
        assertTrue(isEqualToNull.apply(null));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        Predicate<Object> isInstanceOfNumber = Predicates.instanceOf(Number.class);

        assertTrue(isInstanceOfString.apply("Hello"));
        assertFalse(isInstanceOfString.apply(1));
        assertTrue(isInstanceOfNumber.apply(1));
        assertFalse(isInstanceOfNumber.apply("Hello"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);

        assertTrue(isAssignableFromString.apply(String.class));
        assertFalse(isAssignableFromString.apply(Number.class));
        assertTrue(isAssignableFromNumber.apply(Number.class));
        assertFalse(isAssignableFromNumber.apply(String.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isInList = Predicates.in(numbers);

        assertFalse(isInList.apply(-1));
        assertFalse(isInList.apply(6));
        assertTrue(isInList.apply(1));
        assertTrue(isInList.apply(2));
        assertTrue(isInList.apply(3));
        assertTrue(isInList.apply(4));
        assertTrue(isInList.apply(5));
    }

    @Test
    public void testCompose() {
        Predicate<String> isEmptyString = Predicates.alwaysFalse();
        Function<String, Integer> lengthFunction = (s) -> s.length();

        Predicate<String> isEmptyStringOrNull = Predicates.compose(isEmptyString, lengthFunction);
        assertFalse(isEmptyStringOrNull.apply("Hello"));
        assertTrue(isEmptyStringOrNull.apply(null));
        assertFalse(isEmptyStringOrNull.apply(""));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab");
        Predicate<String> containsPatternNull = Predicates.containsPattern(null);

        assertFalse(containsPattern.apply("Hello"));
        assertTrue(containsPattern.apply("HelloWorld"));
        assertTrue(containsPattern.apply("HelloabWorld"));

        assertFalse(containsPatternNull.apply("Hello"));
        assertFalse(containsPatternNull.apply("HelloWorld"));
        assertFalse(containsPatternNull.apply("HelloabWorld"));
    }
}