package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import com.google.common.base.Predicates;
import java.util.*;

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
        Predicate<String> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(""));
        assertFalse(isNull.apply("hello"));
    }

    @Test
    public void testNotNull() {
        Predicate<String> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(""));
        assertTrue(notNull.apply("hello"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> isTrue = Predicates.alwaysTrue();
        Predicate<Boolean> isFalse = Predicates.alwaysFalse();
        Predicate<Boolean> notTrue = Predicates.not(isTrue);
        Predicate<Boolean> notFalse = Predicates.not(isFalse);

        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));
        assertTrue(notFalse.apply(false));
        assertFalse(notFalse.apply(true));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> andEvenAndPositive = Predicates.and(isEven, isPositive);

        assertTrue(andEvenAndPositive.apply(2));
        assertFalse(andEvenAndPositive.apply(1));
        assertFalse(andEvenAndPositive.apply(-1));
    }

    @Test
    public void testAndWithMultiplePredicates() {
        Predicate<String> startsWithA = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String s) {
                return s.startsWith("A");
            }
        };
        Predicate<String> endsWithE = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String s) {
                return s.endsWith("E");
            }
        };

        Predicate<String> andStartsWithAAndEndsWithE = Predicates.and(startsWithA, endsWithE);

        assertTrue(andStartsWithAAndEndsWithE.apply("Apple"));
        assertFalse(andStartsWithAAndEndsWithE.apply("Banana"));
        assertFalse(andStartsWithAAndEndsWithE.apply("Orange"));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 != 0;
            }
        };

        Predicate<Integer> orEvenOrOdd = Predicates.or(isEven, isOdd);

        assertTrue(orEvenOrOdd.apply(2));
        assertTrue(orEvenOrOdd.apply(1));
        assertFalse(orEvenOrOdd.apply(0));
    }

    @Test
    public void testOrWithMultiplePredicates() {
        Predicate<String> startsWithA = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String s) {
                return s.startsWith("A");
            }
        };
        Predicate<String> endsWithE = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String s) {
                return s.endsWith("E");
            }
        };
        Predicate<String> startsWithB = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String s) {
                return s.startsWith("B");
            }
        };

        Predicate<String> orStartsWithAOrEndsWithEOrStartsWithB = Predicates.or(startsWithA, endsWithE, startsWithB);

        assertTrue(orStartsWithAOrEndsWithEOrStartsWithB.apply("Apple"));
        assertTrue(orStartsWithAOrEndsWithEOrStartsWithB.apply("Banana"));
        assertFalse(orStartsWithAOrEndsWithEOrStartsWithB.apply("Orange"));
    }

    @Test
    public void testEqualsTo() {
        Predicate<String> isEqualToHello = Predicates.equalTo("hello");
        Predicate<String> isEqualToWorld = Predicates.equalTo("world");

        assertFalse(isEqualToHello.apply("hello"));
        assertFalse(isEqualToHello.apply("hello world"));
        assertTrue(isEqualToHello.apply("hello"));
        assertFalse(isEqualToWorld.apply("hello"));
        assertTrue(isEqualToWorld.apply("world"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        Predicate<Object> isInstanceOfInteger = Predicates.instanceOf(Integer.class);

        assertFalse(isInstanceOfString.apply(1));
        assertTrue(isInstanceOfString.apply("hello"));
        assertFalse(isInstanceOfInteger.apply("hello"));
        assertTrue(isInstanceOfInteger.apply(1));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);

        assertFalse(isAssignableFromString.apply(Integer.class));
        assertTrue(isAssignableFromString.apply(StringBuilder.class));
        assertTrue(isAssignableFromNumber.apply(Integer.class));
        assertTrue(isAssignableFromNumber.apply(Double.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isInNumbers = Predicates.in(numbers);

        assertFalse(isInNumbers.apply(0));
        assertFalse(isInNumbers.apply(6));
        assertTrue(isInNumbers.apply(1));
        assertTrue(isInNumbers.apply(2));
        assertTrue(isInNumbers.apply(3));
        assertTrue(isInNumbers.apply(4));
        assertTrue(isInNumbers.apply(5));
    }

    @Test
    public void testCompose() {
        Function<Integer, Boolean> isEven = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull());

        Predicate<Integer> isEvenAndPositive = Predicates.compose(isEven, isPositive);

        assertFalse(isEvenAndPositive.apply(null));
        assertFalse(isEvenAndPositive.apply(-1));
        assertTrue(isEvenAndPositive.apply(0));
        assertTrue(isEvenAndPositive.apply(2));
        assertFalse(isEvenAndPositive.apply(3));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternHello = Predicates.containsPattern("hello");
        Predicate<String> containsPatternWorld = Predicates.containsPattern("world");

        assertFalse(containsPatternHello.apply("Apple"));
        assertFalse(containsPatternHello.apply("hello"));
        assertFalse(containsPatternHello.apply("hello world"));
        assertTrue(containsPatternHello.apply("hello Apple"));
        assertFalse(containsPatternWorld.apply("Apple"));
        assertFalse(containsPatternWorld.apply("hello"));
        assertTrue(containsPatternWorld.apply("hello world"));
        assertFalse(containsPatternWorld.apply("world Apple"));
    }
}