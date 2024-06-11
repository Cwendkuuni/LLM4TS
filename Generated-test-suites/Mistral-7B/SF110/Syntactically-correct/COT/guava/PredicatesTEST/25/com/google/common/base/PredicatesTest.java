package com.example.test;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;
import com.google.common.base.*;

/**
 * Tests for the Predicates class.
 */
public class PredicatesTest {

    

    @org.junit.Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2.0));
        assertTrue(alwaysTrue.apply(""));
    }

    @org.junit.Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2.0));
        assertFalse(alwaysFalse.apply(""));
    }

    @org.junit.Test
    public void testIsNull() {
        Predicate<String> isNull = Predicates.isNull();
        assertFalse(isNull.apply(""));
        assertTrue(isNull.apply(null));
    }

    @org.junit.Test
    public void testNotNull() {
        Predicate<String> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(""));
    }

    @org.junit.Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> notEven = Predicates.not(isEven);

        assertFalse(notEven.apply(0));
        assertTrue(notEven.apply(1));
        assertTrue(notEven.apply(2));
        assertFalse(notEven.apply(3));
    }

    @org.junit.Test
    public void testAnd() {
        Predicate<String> isEmpty = Predicates.isNull();
        Predicate<String> isEmptyOrLengthOne = Predicates.isNull()
                .or(Predicates.compose(Predicates.instanceOf(String.class), str -> str.length() == 1));

        assertTrue(isEmptyOrLengthOne.apply(null));
        assertTrue(isEmptyOrLengthOne.apply(""));
        assertTrue(isEmptyOrLengthOne.apply("a"));
        assertFalse(isEmptyOrLengthOne.apply("hello"));

        List<Predicate<String>> andPredicates = Arrays.asList(Predicates.notNull(), Predicates.instanceOf(String.class));
        Predicate<String> and = Predicates.and(andPredicates);

        assertFalse(and.apply(Integer.class));
        assertFalse(and.apply(null));
        assertTrue(and.apply(""));
        assertTrue(and.apply("abc"));
    }

    @org.junit.Test
    public void testOr() {
        Predicate<String> isEmpty = Predicates.isNull();
        Predicate<String> isEmptyOrLengthOne = Predicates.isNull()
                .or(Predicates.compose(Predicates.instanceOf(String.class), str -> str.length() == 1));

        assertTrue(isEmptyOrLengthOne.apply(null));
        assertTrue(isEmptyOrLengthOne.apply(""));
        assertTrue(isEmptyOrLengthOne.apply("a"));
        assertTrue(isEmptyOrLengthOne.apply("hello"));

        List<Predicate<String>> orPredicates = Arrays.asList(Predicates.notNull(), Predicates.instanceOf(String.class));
        Predicate<String> or = Predicates.or(orPredicates);

        assertTrue(or.apply(null));
        assertTrue(or.apply(""));
        assertTrue(or.apply("abc"));
        assertTrue(or.apply("hello"));
    }

    @org.junit.Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);
        assertTrue(isEqualToFive.apply(5));
        assertFalse(isEqualToFive.apply(4));
        assertFalse(isEqualToFive.apply(6));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEqualToNull() {
        Predicate<Integer> isEqualToNull = Predicates.equalTo(null);
    }

    @org.junit.Test
    public void testInstanceOf() {
        Predicate<String> isString = Predicates.instanceOf(String.class);
        assertTrue(isString.apply(""));
        assertFalse(isString.apply(1));
        assertFalse(isString.apply(new Object()));
    }

    @org.junit.Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
        assertTrue(isAssignableFromString.apply(StringBuilder.class));
        assertTrue(isAssignableFromString.apply(StringBuffer.class));
        assertFalse(isAssignableFromString.apply(Integer.class));
    }

    @org.junit.Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inNumbers = Predicates.in(numbers);

        assertFalse(inNumbers.apply(-1));
        assertFalse(inNumbers.apply(6));
        assertTrue(inNumbers.apply(1));
        assertTrue(inNumbers.apply(2));
        assertTrue(inNumbers.apply(3));
        assertTrue(inNumbers.apply(4));
        assertTrue(inNumbers.apply(5));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testInWithNullCollection() {
        Predicate<Object> inNullCollection = Predicates.in(null);
    }

    @org.junit.Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * input;
            }
        };
        Predicate<Integer> isEvenSquare = Predicates.compose(isEven, square);

        assertFalse(isEvenSquare.apply(1));
        assertTrue(isEvenSquare.apply(4));
        assertTrue(isEvenSquare.apply(9));
        assertFalse(isEvenSquare.apply(11));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testComposeWithNullPredicate() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * input;
            }
        };
        Predicate<Integer> composeWithNullPredicate = Predicates.compose(null, square);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> square = null;
        Predicate<Integer> composeWithNullFunction = Predicates.compose(isEven, square);
    }

    
}