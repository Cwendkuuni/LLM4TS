package com.google.common.base;

import static org.junit.Assert.*;
import com.google.common.base.Predicates.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(new String("foo")));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(new String("foo")));
    }

    @Test
    public void testIsNull() {
        Predicate<String> isNull = isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(""));
        assertFalse(isNull.apply("foo"));
    }

    @Test
    public void testNotNull() {
        Predicate<String> notNull = notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(""));
        assertTrue(notNull.apply("foo"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> isTrue = alwaysTrue();
        Predicate<Boolean> isFalse = alwaysFalse();

        Predicate<Boolean> notTrue = not(isTrue);
        Predicate<Boolean> notFalse = not(isFalse);

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

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> andPredicate = and(isEven, isPositive);

        // Test with null
        assertNull(andPredicate.apply(null));

        // Test with negative numbers
        assertFalse(andPredicate.apply(-1));

        // Test with even positive numbers
        assertTrue(andPredicate.apply(0));
        assertTrue(andPredicate.apply(2));

        // Test with odd positive numbers
        assertFalse(andPredicate.apply(1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> orPredicate = or(isEven, isPositive);

        // Test with null
        assertNull(orPredicate.apply(null));

        // Test with negative numbers
        assertTrue(orPredicate.apply(-1));
        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
    }

    @Test
    public void testEqualsTo() {
        Predicate<Integer> equalsToFive = equalTo(5);
        Predicate<String> equalsToString = equalTo("foo");

        assertFalse(equalsToFive.apply(null));
        assertFalse(equalsToFive.apply(1));
        assertTrue(equalsToFive.apply(5));
        assertFalse(equalsToFive.apply(6));

        assertFalse(equalsToString.apply(null));
        assertFalse(equalsToString.apply(""));
        assertFalse(equalsToString.apply("bar"));
        assertTrue(equalsToString.apply("foo"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> isString = instanceOf(String.class);
        Predicate<Number> isNumber = instanceOf(Number.class);

        assertFalse(isNumber.apply(""));
        assertFalse(isNumber.apply(new String("123")));
        assertFalse(isString.apply(123));
        assertFalse(isString.apply(1.2));
        assertTrue(isString.apply(""));
        assertTrue(isString.apply("foo"));
        assertTrue(isNumber.apply(123));
        assertTrue(isNumber.apply(1.2));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<Number>> isAssignableFromNumber = assignableFrom(Number.class);
        Predicate<Class<String>> isAssignableFromString = assignableFrom(String.class);

        assertFalse(isAssignableFromNumber.apply(String.class));
        assertTrue(isAssignableFromString.apply(String.class));
        assertTrue(isAssignableFromNumber.apply(Number[].class));
        assertTrue(isAssignableFromString.apply(StringBuffer.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inPredicate = in(numbers);

        assertFalse(inPredicate.apply(-1));
        assertFalse(inPredicate.apply(6));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertTrue(inPredicate.apply(4));
        assertTrue(inPredicate.apply(5));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> lengthFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String string) {
                return string.length();
            }
        };

        Predicate<String> isEmpty = alwaysFalse();
        Predicate<String> isStringLongerThanFive = compose(isStringLongerThanFive, lengthFunction);

        assertFalse(isStringLongerThanFive.apply(new String("")));
        assertFalse(isStringLongerThanFive.apply(new String("foo")));
        assertTrue(isStringLongerThanFive.apply(new String("barfoo")));
        assertTrue(isStringLongerThanFive.apply(new String("longString")));
    }

    @Test
    public void testContainsPattern() {
        String pattern = "[a-z]+foobar[a-z]+";
        Predicate<String> containsPatternPredicate = containsPattern(pattern);

        assertFalse(containsPatternPredicate.apply(null));
        assertFalse(containsPatternPredicate.apply(new String("")));
        assertFalse(containsPatternPredicate.apply(new String("abcd")));
        assertFalse(containsPatternPredicate.apply(new String("foobarabcd")));
        assertTrue(containsPatternPredicate.apply(new String("foobarabcdfoobar")));
    }
}