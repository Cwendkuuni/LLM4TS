package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply("foo"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply("foo"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply("foo"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply("foo"));
    }

    @Test
    public void testNot() {
        Predicate<Object> isNull = Predicates.isNull();
        Predicate<Object> notNull = Predicates.notNull();
        Predicate<Object> notNullNotIsNull = Predicates.not(isNull);
        Predicate<Object> notNullNotIsNull = Predicates.not(notNull);

        assertFalse(notNullNotIsNull.apply(null));
        assertTrue(notNullNotIsNull.apply(1));
        assertTrue(notNullNotIsNull.apply("foo"));

        assertFalse(notNullNotIsNull.apply(null));
        assertTrue(notNullNotIsNull.apply(1));
        assertTrue(notNullNotIsNull.apply("foo"));

        Object obj = new Object();
        assertFalse(notNullNotIsNull.apply(obj));
        assertTrue(notNullNotIsNull.apply(obj));
        assertFalse(notNullNotIsNull.apply(obj));
        assertTrue(notNullNotIsNull.apply(obj));
    }

    @Test
    public void testAnd() {
        Predicate<Object> isNull = Predicates.isNull();
        Predicate<Object> notNull = Predicates.notNull();
        Predicate<Object> isString = Predicates.instanceOf(String.class);

        Predicate<Object> andIsNullAndIsString = Predicates.and(isNull, isString);
        Predicate<Object> andNotNullAndIsString = Predicates.and(notNull, isString);
        Predicate<Object> andIsNullAndNotNull = Predicates.and(isNull, notNull);

        assertFalse(andIsNullAndIsString.apply(1));
        assertFalse(andIsNullAndIsString.apply("foo"));
        assertFalse(andIsNullAndIsString.apply(new Object()));
        assertTrue(andIsNullAndIsString.apply(null));

        assertFalse(andNotNullAndIsString.apply(1));
        assertFalse(andNotNullAndIsString.apply("foo"));
        assertFalse(andNotNullAndIsString.apply(null));
        assertTrue(andNotNullAndIsString.apply(new String("foo")));

        assertFalse(andIsNullAndNotNull.apply(1));
        assertFalse(andIsNullAndNotNull.apply("foo"));
        assertTrue(andIsNullAndNotNull.apply(null));
        assertTrue(andIsNullAndNotNull.apply(new Object()));

        Predicate<String> startsWithF = Predicates.containsPattern(Pattern.compile("^F.*"));
        Predicate<String> startsWithB = Predicates.containsPattern(Pattern.compile("^B.*"));
        Predicate<String> andStartsWithFAndNotStartsWithB = Predicates.and(startsWithF, Predicates.not(startsWithB));

        assertFalse(andStartsWithFAndNotStartsWithB.apply("Baz"));
        assertFalse(andStartsWithFAndNotStartsWithB.apply("Foo"));
        assertTrue(andStartsWithFAndNotStartsWithB.apply("FooBar"));
        assertTrue(andStartsWithFAndNotStartsWithB.apply("FooBarBaz"));
    }

    @Test
    public void testOr() {
        Predicate<Object> isNull = Predicates.isNull();
        Predicate<Object> notNull = Predicates.notNull();
        Predicate<Object> isString = Predicates.instanceOf(String.class);

        Predicate<Object> orIsNullOrIsString = Predicates.or(isNull, isString);
        Predicate<Object> orNotNullAndIsString = Predicates.or(notNull, isString);

        assertTrue(orIsNullOrIsString.apply(null));
        assertTrue(orIsNullOrIsString.apply("foo"));
        assertTrue(orIsNullOrIsString.apply(new String("foo")));
        assertTrue(orIsNullOrIsString.apply(1));

        assertTrue(orNotNullAndIsString.apply("foo"));
        assertTrue(orNotNullAndIsString.apply(new String("foo")));
        assertFalse(orNotNullAndIsString.apply(null));
        assertFalse(orNotNullAndIsString.apply(1));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inList = Predicates.in(numbers);

        assertFalse(inList.apply(0));
        assertFalse(inList.apply(-1));
        assertTrue(inList.apply(1));
        assertTrue(inList.apply(2));
        assertTrue(inList.apply(3));
        assertTrue(inList.apply(4));
        assertTrue(inList.apply(5));
    }

    @Test
    public void testCompose() {
        Predicate<String> isEmpty = Predicates.alwaysFalse();
        Function<String, Integer> length = String::length;

        Predicate<String> isEmptyString = Predicates.compose(isEmpty, length);

        assertFalse(isEmptyString.apply("foo"));
        assertFalse(isEmptyString.apply(""));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern(Pattern.compile("foo.*"));

        assertFalse(containsPattern.apply("bar"));
        assertTrue(containsPattern.apply("foo"));
        assertTrue(containsPattern.apply("foobar"));
        assertTrue(containsPattern.apply("foo123"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isString = Predicates.instanceOf(String.class);
        Predicate<Object> isInteger = Predicates.instanceOf(Integer.class);

        assertTrue(isString.apply("foo"));
        assertFalse(isString.apply(1));
        assertFalse(isInteger.apply("foo"));
        assertTrue(isInteger.apply(1));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> assignableFromNumber = Predicates.assignableFrom(Number.class);

        assertTrue(assignableFromString.apply(String.class));
        assertFalse(assignableFromString.apply(Number.class));
        assertTrue(assignableFromNumber.apply(Number.class));
        assertFalse(assignableFromNumber.apply(String.class));
    }
}