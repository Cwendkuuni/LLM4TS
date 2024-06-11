package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
        assertNull(isNull.apply(new Object()));
        isNull = Predicates.isNull().<String>withNarrowedType();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(""));
        assertFalse(isNull.apply("foo"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNotNull(notNull.apply(null));
        assertNotNull(notNull.apply(new Object()));
        notNull = Predicates.notNull().<String>withNarrowedType();
        assertNotNull(notNull.apply(null));
        assertNotNull(notNull.apply(""));
        assertTrue(notNull.apply("foo"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();
        Predicate<Boolean> notTrue = Predicates.not(truePredicate);
        Predicate<Boolean> notFalse = Predicates.not(falsePredicate);
        assertTrue(notTrue.apply(false));
        assertFalse(notTrue.apply(true));
        assertTrue(notFalse.apply(true));
        assertFalse(notFalse.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = (Predicate<Integer>)Predicates.alwaysFalse();
        Predicate<Integer> isPositive = (Predicate<Integer>)Predicates.alwaysTrue();
        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
        assertTrue(andPredicate.apply(3));
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(-2));
        assertTrue(andPredicate.apply(-3));

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        andPredicate = Predicates.and(predicates);
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
        assertTrue(andPredicate.apply(3));
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(-2));
        assertTrue(andPredicate.apply(-3));

        isEven = (Predicate<Integer>)Predicates.equalTo(2);
        andPredicate = Predicates.and(isEven, isPositive);
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(-2));
        assertTrue(andPredicate.apply(-3));

        isPositive = Predicates.not(Predicates.isNull());
        andPredicate = Predicates.and(isEven, isPositive);
        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
        assertFalse(andPredicate.apply(-1));
        assertTrue(andPredicate.apply(-2));
        assertTrue(andPredicate.apply(-3));

        List<Predicate<Integer>> predicates2 = Arrays.asList(isEven, Predicates.not(Predicates.isNull()));
        andPredicate = Predicates.and(predicates2);
        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
        assertTrue(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(-2));
        assertTrue(andPredicate.apply(-3));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = (Predicate<Integer>)Predicates.alwaysFalse();
        Predicate<Integer> isOdd = (Predicate<Integer>)Predicates.alwaysTrue();
        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(-2));
        assertTrue(orPredicate.apply(-3));

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isOdd);
        orPredicate = Predicates.or(predicates);
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(-2));
        assertTrue(orPredicate.apply(-3));

        isEven = (Predicate<Integer>)Predicates.equalTo(2);
        orPredicate = Predicates.or(isEven, isOdd);
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertFalse(orPredicate.apply(3));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(-2));
        assertTrue(orPredicate.apply(-3));

        isOdd = Predicates.not(isEven);
        orPredicate = Predicates.or(isEven, isOdd);
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertFalse(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(-2));
        assertTrue(orPredicate.apply(-3));

        List<Predicate<Integer>> predicates2 = Arrays.asList(isEven, isOdd);
        orPredicate = Predicates.or(predicates2);
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertFalse(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(-2));
        assertTrue(orPredicate.apply(-3));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToOne = Predicates.equalTo(1);
        Predicate<Integer> isEqualToTwo = Predicates.equalTo(2);
        Predicate<Integer> isEqualToThree = Predicates.equalTo(3);

        assertFalse(isEqualToOne.apply(0));
        assertFalse(isEqualToOne.apply(1));
        assertFalse(isEqualToOne.apply(2));
        assertTrue(isEqualToOne.apply(1));
        assertFalse(isEqualToTwo.apply(0));
        assertFalse(isEqualToTwo.apply(1));
        assertTrue(isEqualToTwo.apply(2));
        assertFalse(isEqualToThree.apply(0));
        assertFalse(isEqualToThree.apply(1));
        assertFalse(isEqualToThree.apply(2));
        assertFalse(isEqualToThree.apply(3));
    }

    @Test
    public void testInstanceOf() {
        @SuppressWarnings("unchecked")
        Predicate<String> isInstanceOfString = (Predicate<String>)Predicates.instanceOf(String.class);
        Predicate<Number> isInstanceOfNumber = Predicates.instanceOf(Number.class);

        assertFalse(isInstanceOfString.apply(null));
        assertTrue(isInstanceOfString.apply(""));
        assertTrue(isInstanceOfString.apply("foo"));
        assertTrue(isInstanceOfString.apply("bar"));

        assertFalse(isInstanceOfNumber.apply("foo"));
        assertFalse(isInstanceOfNumber.apply("bar"));
        assertTrue(isInstanceOfNumber.apply(1));
        assertTrue(isInstanceOfNumber.apply(1.0));
        assertTrue(isInstanceOfNumber.apply(new Integer(1)));
        assertTrue(isInstanceOfNumber.apply(new Double(1.0)));
    }

    @Test
    public void testAssignableFrom() {
        @SuppressWarnings("unchecked")
        Predicate<Class<Number>> assignableFromNumber = (Predicate<Class<Number>>)Predicates.assignableFrom(Number.class);
        Predicate<Class<String>> assignableFromString = Predicates.assignableFrom(String.class);

        assertFalse(assignableFromNumber.apply(String.class));
        assertFalse(assignableFromString.apply(Number.class));
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Double.class));
        assertTrue(assignableFromNumber.apply(Number[].class));
        assertTrue(assignableFromString.apply(StringBuilder.class));
        assertTrue(assignableFromString.apply(StringBuffer.class));
        assertTrue(assignableFromString.apply(StringBuilder[].class));
        assertTrue(assignableFromString.apply(StringBuffer[].class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(list);

        assertFalse(inPredicate.apply(0));
        assertFalse(inPredicate.apply(4));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));

        List<String> stringList = Arrays.asList("foo", "bar", "baz");
        Predicate<String> inPredicateString = Predicates.in(stringList);

        assertFalse(inPredicateString.apply(null));
        assertFalse(inPredicateString.apply("qux"));
        assertTrue(inPredicateString.apply("foo"));
        assertTrue(inPredicateString.apply("bar"));
        assertTrue(inPredicateString.apply("baz"));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> stringToInt = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable String input) {
                return Integer.parseInt(input);
            }
        };
        Predicate<String> isEvenString = Predicates.alwaysFalse();
        Predicate<Integer> isEvenInteger = (Predicate<Integer>)Predicates.equalTo(0);

        Predicate<String> composePredicate = Predicates.compose(isEvenInteger, stringToInt);
        assertFalse(composePredicate.apply("0"));
        assertFalse(composePredicate.apply("1"));
        assertFalse(composePredicate.apply("2"));
        assertFalse(composePredicate.apply("3"));
        assertFalse(composePredicate.apply("4"));
        assertFalse(composePredicate.apply("5"));
        assertTrue(composePredicate.apply("0"));
        assertTrue(composePredicate.apply("2"));
        assertTrue(composePredicate.apply("4"));
        assertTrue(composePredicate.apply("6"));
        assertTrue(composePredicate.apply("8"));
    }

    @Test
    public void testContainsPattern() {
        Pattern pattern = Pattern.compile("foo");
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);

        assertFalse(containsPatternPredicate.apply(null));
        assertFalse(containsPatternPredicate.apply(""));
        assertFalse(containsPatternPredicate.apply("bar"));
        assertTrue(containsPatternPredicate.apply("foo"));
        assertTrue(containsPatternPredicate.apply("fooBar"));
        assertTrue(containsPatternPredicate.apply("fooBaz"));
        assertTrue(containsPatternPredicate.apply("foobar"));
        assertTrue(containsPatternPredicate.apply("fooBazBar"));
    }
}