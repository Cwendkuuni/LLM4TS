package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<String> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(""));
        assertTrue(alwaysTrue.apply("test"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<String> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(""));
        assertFalse(alwaysFalse.apply("test"));
    }

    @Test
    public void testIsNull() {
        Predicate<String> isNull = Predicates.isNull();
        assertFalse(isNull.apply(""));
        assertFalse(isNull.apply("test"));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<String> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(""));
        assertTrue(notNull.apply("test"));
    }

    @Test
    public void testNot() {
        Predicate<String> isEmpty = Predicates.alwaysFalse();
        Predicate<String> notEmpty = Predicates.not(isEmpty);
        assertFalse(notEmpty.apply(""));
        assertTrue(notEmpty.apply("test"));
        assertTrue(notEmpty.apply("hello world"));

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> notEven = Predicates.not(isEven);
        assertTrue(notEven.apply(1));
        assertFalse(notEven.apply(2));
        assertFalse(notEven.apply(3));
    }

    @Test
    public void testAnd() {
        Predicate<String> isEmpty = Predicates.alwaysFalse();
        Predicate<String> isEmptyOrNull = Predicates.or(isEmpty, Predicates.isNull());
        assertFalse(isEmptyOrNull.apply(""));
        assertTrue(isEmptyOrNull.apply(null));
        assertTrue(isEmptyOrNull.apply(null));

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isEvenAndGreaterThanZero = Predicates.and(isEven, Predicates.notNull());
        assertFalse(isEvenAndGreaterThanZero.apply(null));
        assertFalse(isEvenAndGreaterThanZero.apply(-1));
        assertTrue(isEvenAndGreaterThanZero.apply(0));
        assertTrue(isEvenAndGreaterThanZero.apply(2));

        List<Predicate<String>> listOfPredicates = Arrays.asList(Predicates.notNull(), Predicates.isNotEmpty());
        Predicate<String> andPredicate = Predicates.and(listOfPredicates);
        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(""));
        assertTrue(andPredicate.apply("test"));
    }

    @Test
    public void testOr() {
        Predicate<String> isEmpty = Predicates.alwaysFalse();
        Predicate<String> isEmptyOrNull = Predicates.or(isEmpty, Predicates.isNull());
        assertFalse(isEmptyOrNull.apply(""));
        assertTrue(isEmptyOrNull.apply(null));
        assertTrue(isEmptyOrNull.apply(null));

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> isEvenOrOdd = Predicates.or(isEven, isOdd);
        assertTrue(isEvenOrOdd.apply(0));
        assertTrue(isEvenOrOdd.apply(1));
        assertFalse(isEvenOrOdd.apply(2));

        List<Predicate<Integer>> listOfPredicates = Arrays.asList(Predicates.notNull(), Predicates.isEven(), Predicates.isGreaterThan(0));
        Predicate<Integer> orPredicate = Predicates.or(listOfPredicates);
        assertFalse(orPredicate.apply(null));
        assertFalse(orPredicate.apply(-1));
        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<String> isEmpty = Predicates.alwaysFalse();
        Predicate<String> isEmptyOrEqualToEmptyString = Predicates.or(isEmpty, Predicates.equalTo(""));
        assertFalse(isEmptyOrEqualToEmptyString.apply("test"));
        assertTrue(isEmptyOrEqualToEmptyString.apply(""));

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);
        Predicate<Integer> isEvenAndEqualToFive = Predicates.and(isEven, isEqualToFive);
        assertFalse(isEvenAndEqualToFive.apply(1));
        assertFalse(isEvenAndEqualToFive.apply(2));
        assertFalse(isEvenAndEqualToFive.apply(3));
        assertFalse(isEvenAndEqualToFive.apply(6));
        assertTrue(isEvenAndEqualToFive.apply(5));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        assertTrue(isInstanceOfString.apply(""));
        assertTrue(isInstanceOfString.apply("test"));
        assertFalse(isInstanceOfString.apply(1));
        assertFalse(isInstanceOfString.apply(new Integer(1)));

        Predicate<Object> isInstanceOfNumber = Predicates.instanceOf(Number.class);
        assertTrue(isInstanceOfNumber.apply(1));
        assertTrue(isInstanceOfNumber.apply(new Integer(1)));
        assertTrue(isInstanceOfNumber.apply(new Double(3.14)));
        assertFalse(isInstanceOfNumber.apply(""));
        assertFalse(isInstanceOfNumber.apply("test"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);
        assertTrue(isAssignableFromNumber.apply(Integer.class));
        assertTrue(isAssignableFromNumber.apply(Double.class));
        assertTrue(isAssignableFromNumber.apply(Float.class));
        assertFalse(isAssignableFromNumber.apply(String.class));
        assertFalse(isAssignableFromNumber.apply(List.class));

        Predicate<Class<?>> isAssignableFromList = Predicates.assignableFrom(List.class);
        assertTrue(isAssignableFromList.apply(ArrayList.class));
        assertTrue(isAssignableFromList.apply(LinkedList.class));
        assertTrue(isAssignableFromList.apply(Vector.class));
        assertFalse(isAssignableFromList.apply(Number.class));
        assertFalse(isAssignableFromList.apply(String.class));
    }

    @Test
    public void testIn() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Predicate<String> isInList = Predicates.in(list);
        assertFalse(isInList.apply("orange"));
        assertFalse(isInList.apply("apple"));
        assertTrue(isInList.apply("banana"));
        assertTrue(isInList.apply("cherry"));

        List<Integer> intList = Arrays.asList(1, 2, 3);
        Predicate<Integer> isInListInt = Predicates.in(intList);
        assertFalse(isInListInt.apply(0));
        assertFalse(isInListInt.apply(1));
        assertTrue(isInListInt.apply(2));
        assertTrue(isInListInt.apply(3));
    }

    @Test
    public void testCompose() {
        Predicate<String> isEmpty = Predicates.alwaysFalse();
        Function<String, Integer> lengthFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Predicate<String> isEmptyOrHasLengthZero = Predicates.compose(isEmpty, lengthFunction);
        assertFalse(isEmptyOrHasLengthZero.apply(""));
        assertTrue(isEmptyOrHasLengthZero.apply("test"));
        assertTrue(isEmptyOrHasLengthZero.apply(""));

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };
        Predicate<Integer> isEvenAndGreaterThanOne = Predicates.compose(isEven, addOne);
        assertFalse(isEvenAndGreaterThanOne.apply(0));
        assertFalse(isEvenAndGreaterThanOne.apply(1));
        assertFalse(isEvenAndGreaterThanOne.apply(2));
        assertTrue(isEvenAndGreaterThanOne.apply(3));
        assertTrue(isEvenAndGreaterThanOne.apply(4));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab.*");
        assertFalse(containsPattern.apply("123"));
        assertFalse(containsPattern.apply("test"));
        assertTrue(containsPattern.apply("the quick brown fox jumps over the lazy dog"));
        assertTrue(containsPattern.apply("applebaebanana"));

        Predicate<String> containsPatternFromString = Predicates.containsPatternFromString("ab.*");
        assertFalse(containsPatternFromString.apply("123"));
        assertFalse(containsPatternFromString.apply("test"));
        assertTrue(containsPatternFromString.apply("the quick brown fox jumps over the lazy dog"));
        assertTrue(containsPatternFromString.apply("applebaebanana"));
    }
}