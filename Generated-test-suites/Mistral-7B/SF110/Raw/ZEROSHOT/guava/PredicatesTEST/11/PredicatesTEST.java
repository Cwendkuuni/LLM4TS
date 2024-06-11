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
        assertTrue(alwaysTrue.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertFalse(notNull.apply(null));
        assertNotNull(notNull.apply(new Object()));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> isTrue = Predicates.alwaysTrue();
        Predicate<Boolean> isFalse = Predicates.alwaysFalse();
        Predicate<Boolean> notTrue = Predicates.not(isTrue);
        Predicate<Boolean> notFalse = Predicates.not(isFalse);

        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));

        assertFalse(notFalse.apply(false));
        assertTrue(notFalse.apply(true));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isGreaterThanZero = Predicates.notNull();

        Predicate<Integer> and = Predicates.and(isEven, isGreaterThanZero);

        assertTrue(and.apply(2));
        assertTrue(and.apply(4));
        assertFalse(and.apply(1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 != 0;
            }
        };

        Predicate<Integer> or = Predicates.or(isEven, isOdd);

        assertTrue(or.apply(2));
        assertTrue(or.apply(3));
        assertFalse(or.apply(4));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);
        Predicate<Integer> isEqualToSeven = Predicates.equalTo(7);

        assertFalse(isEqualToFive.apply(3));
        assertFalse(isEqualToFive.apply(5));
        assertFalse(isEqualToSeven.apply(5));
        assertFalse(isEqualToSeven.apply(7));
        assertTrue(isEqualToFive.apply(5));
        assertTrue(isEqualToSeven.apply(7));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        Predicate<Object> isInstanceOfInteger = Predicates.instanceOf(Integer.class);

        assertFalse(isInstanceOfString.apply(new Object()));
        assertFalse(isInstanceOfString.apply("hello"));
        assertFalse(isInstanceOfString.apply(42));
        assertTrue(isInstanceOfString.apply("world"));
        assertFalse(isInstanceOfInteger.apply("hello"));
        assertFalse(isInstanceOfInteger.apply(new Object()));
        assertTrue(isInstanceOfInteger.apply(42));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);
        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);

        assertFalse(isAssignableFromNumber.apply(String.class));
        assertFalse(isAssignableFromString.apply(Number.class));
        assertTrue(isAssignableFromNumber.apply(Integer.class));
        assertTrue(isAssignableFromString.apply(StringBuilder.class));
    }

    @Test
    public void testIn() {
        Predicate<Integer> inListOneThreeFive = Predicates.in(Arrays.asList(1, 3, 5));
        Predicate<Integer> inListOneTwoThreeFive = Predicates.in(Arrays.asList(1, 2, 3, 5));

        assertFalse(inListOneThreeFive.apply(0));
        assertFalse(inListOneThreeFive.apply(4));
        assertTrue(inListOneThreeFive.apply(1));
        assertTrue(inListOneThreeFive.apply(3));
        assertTrue(inListOneThreeFive.apply(5));

        assertFalse(inListOneTwoThreeFive.apply(0));
        assertFalse(inListOneTwoThreeFive.apply(2));
        assertFalse(inListOneTwoThreeFive.apply(4));
        assertTrue(inListOneTwoThreeFive.apply(1));
        assertTrue(inListOneTwoThreeFive.apply(3));
        assertTrue(inListOneTwoThreeFive.apply(5));
    }

    @Test
    public void testNotIn() {
        Predicate<Integer> notInListOneThreeFive = Predicates.not(Predicates.in(Arrays.asList(1, 3, 5)));
        Predicate<Integer> notInListOneTwoThreeFive = Predicates.not(Predicates.in(Arrays.asList(1, 2, 3, 5)));

        assertTrue(notInListOneThreeFive.apply(0));
        assertTrue(notInListOneThreeFive.apply(4));
        assertFalse(notInListOneThreeFive.apply(1));
        assertFalse(notInListOneThreeFive.apply(3));
        assertFalse(notInListOneThreeFive.apply(5));

        assertTrue(notInListOneTwoThreeFive.apply(0));
        assertTrue(notInListOneTwoThreeFive.apply(2));
        assertTrue(notInListOneTwoThreeFive.apply(4));
        assertFalse(notInListOneTwoThreeFive.apply(1));
        assertFalse(notInListOneTwoThreeFive.apply(3));
        assertFalse(notInListOneTwoThreeFive.apply(5));
    }

    @Test
    public void testCompose() {
        Predicate<String> startsWithA = new Predicate<String>() {
            @Override
            public boolean apply(String string) {
                return string.length() > 0 && string.charAt(0) == 'A';
            }
        };

        Function<String, Integer> length = new Function<String, Integer>() {
            @Override
            public Integer apply(String string) {
                return string.length();
            }
        };

        Predicate<String> lengthGreaterThanSeven = Predicates.compose(Predicates.alwaysFalse(), length);
        Predicate<String> startsWithAA = Predicates.compose(startsWithA, startsWithA);

        assertFalse(lengthGreaterThanSeven.apply("hello"));
        assertFalse(lengthGreaterThanSeven.apply("world"));
        assertTrue(lengthGreaterThanSeven.apply("foo"));

        assertTrue(startsWithAA.apply("AA"));
        assertFalse(startsWithAA.apply("A"));
        assertFalse(startsWithAA.apply("hello"));
        assertFalse(startsWithAA.apply("world"));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternString = Predicates.containsPattern("String");
        Predicate<String> containsPatternPattern = Predicates.containsPattern(Pattern.compile("String"));

        assertFalse(containsPatternString.apply("hello"));
        assertFalse(containsPatternString.apply("world"));
        assertTrue(containsPatternString.apply("Stringhello"));
        assertTrue(containsPatternString.apply("Stringworld"));

        assertFalse(containsPatternPattern.apply("hello"));
        assertFalse(containsPatternPattern.apply("world"));
        assertTrue(containsPatternPattern.apply("Stringhello"));
        assertTrue(containsPatternPattern.apply("Stringworld"));
    }
}