package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    // Test alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(Integer.MAX_VALUE));
        assertTrue(alwaysTrue.apply("test"));
    }

    // Test alwaysFalse()
    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(Integer.MAX_VALUE));
        assertFalse(alwaysFalse.apply("test"));
    }

    // Test isNull()
    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(Integer.MAX_VALUE));
        assertFalse(isNull.apply("test"));
    }

    // Test notNull()
    @Test
    public void testNotnull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(Integer.MAX_VALUE));
        assertTrue(notNull.apply("test"));
    }

    // Test not()
    @Test
    public void testNot() {
        Predicate<Boolean> isTrue = Predicates.alwaysTrue();
        Predicate<Boolean> isFalse = Predicates.alwaysFalse();

        Predicate<Boolean> notTrue = Predicates.not(isTrue);
        Predicate<Boolean> notFalse = Predicates.not(isFalse);

        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));

        assertFalse(notFalse.apply(true));
        assertTrue(notFalse.apply(false));
    }

    // Test and()
    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.notNull();

        Predicate<Integer> andEvenAndPositive = Predicates.and(isEven, isPositive);

        assertTrue(andEvenAndPositive.apply(2));
        assertTrue(andEvenAndPositive.apply(3)); // Should be false
        assertNull(andEvenAndPositive.apply(null)); // Should be null

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> andPredicate = Predicates.and(predicates);
        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(3)); // Should be false
        assertNull(andPredicate.apply(null)); // Should be null
    }

    // Test or()
    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.notNull();

        Predicate<Integer> orEvenOrPositive = Predicates.or(isEven, isPositive);

        assertTrue(orEvenOrPositive.apply(2));
        assertTrue(orEvenOrPositive.apply(1));
        assertNull(orEvenOrPositive.apply(null)); // Should be null

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> orPredicate = Predicates.or(predicates);
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertNull(orPredicate.apply(null)); // Should be null
    }

    // Test equalTo()
    @Test
    public void testEqualTo() {
        Predicate<String> isEqualToNull = Predicates.isNull();
        Predicate<String> isEqualToEmptyString = Predicates.equalTo((String)null);
        Predicate<String> isEqualToTest = Predicates.equalTo("test");

        assertFalse(isEqualToNull.apply("test"));
        assertTrue(isEqualToNull.apply(null));
        assertFalse(isEqualToEmptyString.apply("test"));
        assertFalse(isEqualToEmptyString.apply(""));
        assertTrue(isEqualToTest.apply("test"));
        assertFalse(isEqualToTest.apply("other"));
    }

    // Test instanceOf()
    @Test
    public void testInstanceOf() {
        Predicate<Class<?>> isInstanceOfString = Predicates.instanceOf(String.class);
        Predicate<Class<?>> isInstanceOfNumber = Predicates.instanceOf(Number.class);

        assertFalse(isInstanceOfString.apply(new Object()));
        assertTrue(isInstanceOfString.apply(new String()));
        assertFalse(isInstanceOfNumber.apply(new Object()));
        assertTrue(isInstanceOfNumber.apply(new Integer(1)));
    }

    // Test assignableFrom()
    @Test
    public void testAssignableFrom() {
        Predicate<Class<
 java>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);
        Predicate<Class<String>> isAssignableFromString = Predicates.assignableFrom(String.class);

        assertFalse(isAssignableFromNumber.apply(String.class));
        assertTrue(isAssignableFromNumber.apply(Number.class));
        assertTrue(isAssignableFromNumber.apply(Integer.class));
        assertFalse(isAssignableFromNumber.apply(StringBuilder.class));

        assertFalse(isAssignableFromString.apply(Number.class));
        assertTrue(isAssignableFromString.apply(String.class));
        assertFalse(isAssignableFromString.apply(Integer.class));
        assertTrue(isAssignableFromString.apply(StringBuilder.class));
    }

    // Test in()
    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Predicate<Integer> inNumbers = Predicates.in(numbers);

        assertFalse(inNumbers.apply(0));
        assertTrue(inNumbers.apply(1));
        assertTrue(inNumbers.apply(2));
        assertTrue(inNumbers.apply(3));
        assertFalse(inNumbers.apply(4));
    }

    // Test compose()
    @Test
    public void testCompose() {
        Predicate<String> isEvenLength = new Predicate<String>() {
            @Override
            public boolean apply(String string) {
                return string.length() % 2 == 0;
            }
        };

        Function<Integer, String> stringConverter = new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return Integer.toString(integer);
            }
        };

        Predicate<Integer> isEvenLengthString = Predicates.compose(isEvenLength, stringConverter);

        assertFalse(isEvenLengthString.apply(1));
        assertTrue(isEvenLengthString.apply(2));
        assertTrue(isEvenLengthString.apply(4));
        assertFalse(isEvenLengthString.apply(3));
    }

    // Test containsPattern()
    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("test");

        assertTrue(containsPattern.apply("test"));
        assertFalse(containsPattern.apply("hello"));
        assertFalse(containsPattern.apply("test hello"));
        assertTrue(containsPattern.apply("test test"));
    }

    // Test containsPatternFromString()
    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPattern = Predicates.containsPattern("test");

        Predicate<String> containsPatternFromString = Predicates.containsPatternFromString("test");

        assertTrue(containsPattern.apply("test"));
        assertFalse(containsPattern.apply("hello"));
        assertFalse(containsPattern.apply("test hello"));
        assertTrue(containsPattern.apply("test test"));

        assertEquals(containsPattern.toString(), containsPatternFromString.toString());
    }
}