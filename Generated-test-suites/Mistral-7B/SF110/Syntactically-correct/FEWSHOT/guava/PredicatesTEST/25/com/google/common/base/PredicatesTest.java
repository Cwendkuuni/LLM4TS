package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

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
        assertNotNull(isNull.apply(1));
        assertNotNull(isNull.apply("foo"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNotNull(notNull.apply(null));
        assertNull(notNull.apply(1));
        assertNull(notNull.apply("foo"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicate = Predicates.alwaysTrue();
        Predicate<Boolean> notPredicate = Predicates.not(predicate);
        assertFalse(notPredicate.apply(true));
        assertTrue(notPredicate.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull())
                .and(Predicates.instanceOf(Integer.class));

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        for (Integer number : numbers) {
            assertTrue(andPredicate.apply(number));
        }

        assertFalse(andPredicate.apply(null));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull())
                .and(Predicates.instanceOf(Integer.class));

        Predicate<Integer> orPredicate = Predicates.or(isEven, isPositive);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        for (Integer number : numbers) {
            if (number == 2 || number > 0) {
                assertTrue(orPredicate.apply(number));
            } else {
                assertFalse(orPredicate.apply(number));
            }
        }

        assertFalse(orPredicate.apply(null));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Predicate<Object> isObj1 = Predicates.equalTo(obj1);
        Predicate<Object> isObj2 = Predicates.equalTo(obj2);

        assertFalse(isObj1.apply(obj2));
        assertFalse(isObj2.apply(obj1));
        assertTrue(isObj1.apply(obj1));
        assertTrue(isObj2.apply(obj2));
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> isString = Predicates.instanceOf(String.class);
        Predicate<Integer> isInteger = Predicates.instanceOf(Integer.class);

        assertFalse(isString.apply(1));
        assertTrue(isString.apply(""));
        assertFalse(isInteger.apply(""));
        assertTrue(isInteger.apply(1));
    }

    @Test
    public void testAssignableFrom() {
        Class<String> stringClass = String.class;
        Class<Number> numberClass = Number.class;

        Predicate<Class<?>> isString = Predicates.assignableFrom(stringClass);
        Predicate<Class<?>> isNumber = Predicates.assignableFrom(numberClass);

        assertTrue(isString.apply(String.class));
        assertTrue(isString.apply(StringBuilder.class));
        assertTrue(isNumber.apply(Integer.class));
        assertTrue(isNumber.apply(Number.class));
        assertFalse(isString.apply(Number.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> inPredicate = Predicates.in(list);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        for (Integer number : numbers) {
            if (list.contains(number)) {
                assertTrue(inPredicate.apply(number));
            } else {
                assertFalse(inPredicate.apply(number));
            }
        }

        assertFalse(inPredicate.apply(6));
    }

    @Test
    public void testCompose() {
        Predicate<String> isEmptyString = Predicates.alwaysFalse();
        Function<String, Integer> stringToLength = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };

        Predicate<String> isStringLengthZero = Predicates.compose(isEmptyString, stringToLength);

        assertFalse(isStringLengthZero.apply(""));
        assertTrue(isStringLengthZero.apply(null));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab");

        String s1 = "Hello World";
        String s2 = "abcdefg";
        String s3 = "Hello ab World";

        assertFalse(containsPattern.apply(s1));
        assertTrue(containsPattern.apply(s2));
        assertTrue(containsPattern.apply(s3));
    }

    @Test
    public void testContainsPatternWithEmptyPattern() {
        Predicate<String> containsEmptyPattern = Predicates.containsPattern("");

        String s1 = "Hello World";
        String s2 = "";
        String s3 = "Hello ab World";

        assertFalse(containsEmptyPattern.apply(s1));
        assertTrue(containsEmptyPattern.apply(s2));
        assertTrue(containsEmptyPattern.apply(s3));
    }
}