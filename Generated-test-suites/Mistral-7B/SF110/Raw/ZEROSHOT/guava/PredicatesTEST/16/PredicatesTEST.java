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
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = Predicates.not(isEven);

        assertFalse(isOdd.apply(2));
        assertTrue(isOdd.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> isEvenAndPositive = Predicates.and(isEven, isPositive);

        assertFalse(isEvenAndPositive.apply(-2));
        assertFalse(isEvenAndPositive.apply(0));
        assertTrue(isEvenAndPositive.apply(2));
        assertTrue(isEvenAndPositive.apply(3));
    }

    @Test
    public void testAndWithPredicatesArray() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> isEvenAndPositive = Predicates.and(isEven, isPositive);

        assertFalse(isEvenAndPositive.apply(-2));
        assertFalse(isEvenAndPositive.apply(0));
        assertTrue(isEvenAndPositive.apply(2));
        assertTrue(isEvenAndPositive.apply(3));

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
        assertEquals(isEvenAndPositive, andPredicate);
    }

    @Test
    public void testAndWithIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> isEvenAndPositive = Predicates.and(predicates);

        assertFalse(isEvenAndPositive.apply(-2));
        assertFalse(isEvenAndPositive.apply(0));
        assertTrue(isEvenAndPositive.apply(2));
        assertTrue(isEvenAndPositive.apply(3));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> isEvenOrOdd = Predicates.or(isEven, isOdd);

        assertFalse(isEvenOrOdd.apply(-2));
        assertTrue(isEvenOrOdd.apply(0));
        assertTrue(isEvenOrOdd.apply(1));
        assertTrue(isEvenOrOdd.apply(2));
    }

    @Test
    public void testOrWithPredicatesArray() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> isEvenOrOdd = Predicates.or(isEven, isOdd);

        assertFalse(isEvenOrOdd.apply(-2));
        assertTrue(isEvenOrOdd.apply(0));
        assertTrue(isEvenOrOdd.apply(1));
        assertTrue(isEvenOrOdd.apply(2));

        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);
        assertEquals(isEvenOrOdd, orPredicate);
    }

    @Test
    public void testOrWithIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isOdd);
        Predicate<Integer> isEvenOrOdd = Predicates.or(predicates);

        assertFalse(isEvenOrOdd.apply(-2));
        assertTrue(isEvenOrOdd.apply(0));
        assertTrue(isEvenOrOdd.apply(1));
        assertTrue(isEvenOrOdd.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isThree = Predicates.equalTo(3);
        assertTrue(isThree.apply(3));
        assertFalse(isThree.apply(1));
        assertFalse(isThree.apply(2));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isString = Predicates.instanceOf(String.class);
        assertTrue(isString.apply("test"));
        assertFalse(isString.apply(1));
        assertFalse(isString.apply(new Object()));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isNumber = Predicates.assignableFrom(Number.class);
        assertTrue(isNumber.apply(Integer.class));
        assertTrue(isNumber.apply(Double.class));
        assertFalse(isNumber.apply(String.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isInList = Predicates.in(numbers);

        assertFalse(isInList.apply(-1));
        assertTrue(isInList.apply(1));
        assertTrue(isInList.apply(2));
        assertTrue(isInList.apply(3));
        assertTrue(isInList.apply(4));
        assertTrue(isInList.apply(5));
        assertFalse(isInList.apply(6));
    }

    @Test(expected = NullPointerException.class)
    public void testCompose() {
        Function<Integer, String> stringConverter = new Function<Integer, String>() {
            @Override
            public String apply(@Nullable Integer input) {
                return String.valueOf(input);
            }
        };
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<String> isStringEven = Predicates.compose(isEven, stringConverter);

        assertFalse(isStringEven.apply(1));
        assertFalse(isStringEven.apply("1"));
        assertTrue(isStringEven.apply("2"));
        assertTrue(isStringEven.apply("4"));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab.*c");

        assertFalse(containsPattern.apply("abc"));
        assertFalse(containsPattern.apply("abagc"));
        assertTrue(containsPattern.apply("abcdc"));
        assertTrue(containsPattern.apply("ababac"));
    }

    @Test
    public void testContainsPatternWithString() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab.*c");

        assertFalse(containsPattern.apply("abc"));
        assertFalse(containsPattern.apply("abagc"));
        assertTrue(containsPattern.apply("abcdc"));
        assertTrue(containsPattern.apply("ababac"));

        Predicate<String> containsPatternFromString = Predicates.containsPattern("ab.*c");

        assertFalse(containsPatternFromString.apply("abc"));
        assertFalse(containsPatternFromString.apply("abagc"));
        assertTrue(containsPatternFromString.apply("abcdc"));
        assertTrue(containsPatternFromString.apply("ababac"));

        assertEquals(containsPattern, containsPatternFromString);
    }
}