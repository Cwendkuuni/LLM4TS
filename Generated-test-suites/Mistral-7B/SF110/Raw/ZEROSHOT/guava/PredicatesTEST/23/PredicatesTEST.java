package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(null));
        assertTrue(alwaysTruePredicate.apply(1));
        assertTrue(alwaysTruePredicate.apply(2.5));
        assertTrue(alwaysTruePredicate.apply("test"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(null));
        assertFalse(alwaysFalsePredicate.apply(1));
        assertFalse(alwaysFalsePredicate.apply(2.5));
        assertFalse(alwaysFalsePredicate.apply("test"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertNull(isNullPredicate.apply(null));
        assertFalse(isNullPredicate.apply(1));
        assertFalse(isNullPredicate.apply(1.0));
        assertFalse(isNullPredicate.apply("test"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();
        assertNull(notNullPredicate.apply(null));
        assertTrue(notNullPredicate.apply(1));
        assertTrue(notNullPredicate.apply(1.0));
        assertTrue(notNullPredicate.apply("test"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> alwaysTruePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> notAlwaysTruePredicate = Predicates.not(alwaysTruePredicate);
        assertFalse(notAlwaysTruePredicate.apply(true));
        assertTrue(notAlwaysTruePredicate.apply(false));

        Predicate<Boolean> alwaysFalsePredicate = Predicates.alwaysFalse();
        Predicate<Boolean> notAlwaysFalsePredicate = Predicates.not(alwaysFalsePredicate);
        assertTrue(notAlwaysFalsePredicate.apply(true));
        assertFalse(notAlwaysFalsePredicate.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isOnePredicate = Predicates.alwaysTrue();
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOneAndEvenPredicate = Predicates.and(isOnePredicate, isEvenPredicate);
        assertFalse(isOneAndEvenPredicate.apply(0));
        assertFalse(isOneAndEvenPredicate.apply(1));
        assertTrue(isOneAndEvenPredicate.apply(2));
        assertTrue(isOneAndEvenPredicate.apply(4));

        Predicate<Integer> isOnePredicate2 = Predicates.alwaysTrue();
        Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> isOneAndOddPredicate = Predicates.and(isOnePredicate2, isOddPredicate);
        assertFalse(isOneAndOddPredicate.apply(0));
        assertFalse(isOneAndOddPredicate.apply(1));
        assertFalse(isOneAndOddPredicate.apply(2));
        assertTrue(isOneAndOddPredicate.apply(3));

        Predicate<Integer> isOnePredicate3 = Predicates.alwaysTrue();
        Predicate<Integer> isEvenPredicate2 = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOneAndEvenPredicate2 = Predicates.and(isOnePredicate3, isEvenPredicate2);
        assertFalse(isOneAndEvenPredicate2.apply(0));
        assertFalse(isOneAndEvenPredicate2.apply(1));
        assertTrue(isOneAndEvenPredicate2.apply(2));
        assertTrue(isOneAndEvenPredicate2.apply(4));

        Predicate<Integer> isOnePredicate4 = Predicates.alwaysTrue();
        Predicate<Integer> isOddPredicate2 = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> isOneAndOddPredicate2 = Predicates.and(isOnePredicate4, isOddPredicate2);
        assertFalse(isOneAndOddPredicate2.apply(0));
        assertFalse(isOneAndOddPredicate2.apply(1));
        assertFalse(isOneAndOddPredicate2.apply(2));
        assertTrue(isOneAndOddPredicate2.apply(3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAndWithList() {
        Predicate<Integer> isOnePredicate = Predicates.alwaysTrue();
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOneAndEvenPredicate = Predicates.and(Arrays.asList(isOnePredicate, isEvenPredicate));
        assertFalse(isOneAndEvenPredicate.apply(0));
        assertFalse(isOneAndEvenPredicate.apply(1));
        assertTrue(isOneAndEvenPredicate.apply(2));
        assertTrue(isOneAndEvenPredicate.apply(4));
    }

    @Test
    public void testOr() {
        Predicate<Boolean> isTruePredicate = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == true;
            }
        };
        Predicate<Boolean> isFalsePredicate = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == false;
            }
        };
        Predicate<Boolean> isTrueOrFalsePredicate = Predicates.or(isTruePredicate, isFalsePredicate);
        assertTrue(isTrueOrFalsePredicate.apply(true));
        assertTrue(isTrueOrFalsePredicate.apply(false));

        Predicate<Boolean> isTruePredicate2 = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == true;
            }
        };
        Predicate<Boolean> isFalsePredicate2 = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == false;
            }
        };
        Predicate<Boolean> isTrueOrFalsePredicate2 = Predicates.or(isTruePredicate2, isFalsePredicate2);
        assertTrue(isTrueOrFalsePredicate2.apply(true));
        assertTrue(isTrueOrFalsePredicate2.apply(false));

        Predicate<Boolean> isFalsePredicate3 = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == false;
            }
        };
        Predicate<Boolean> isTruePredicate3 = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == true;
            }
        };
        Predicate<Boolean> isFalseOrTruePredicate = Predicates.or(isFalsePredicate3, isTruePredicate3);
        assertFalse(isFalseOrTruePredicate.apply(false));
        assertTrue(isFalseOrTruePredicate.apply(true));

        Predicate<Boolean> isFalsePredicate4 = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == false;
            }
        };
        Predicate<Boolean> isFalsePredicate5 = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == false;
            }
        };
        Predicate<Boolean> isFalseOrFalsePredicate = Predicates.or(isFalsePredicate4, isFalsePredicate5);
        assertFalse(isFalseOrFalsePredicate.apply(false));
        assertFalse(isFalseOrFalsePredicate.apply(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOrWithList() {
        Predicate<Boolean> isTruePredicate = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == true;
            }
        };
        Predicate<Boolean> isFalsePredicate = new Predicate<Boolean>() {
            @Override
            public boolean apply(@Nullable Boolean input) {
                return input == false;
            }
        };
        Predicate<Boolean> isTrueOrFalsePredicate = Predicates.or(Arrays.asList(isTruePredicate, isFalsePredicate));
        assertTrue(isTrueOrFalsePredicate.apply(true));
        assertTrue(isTrueOrFalsePredicate.apply(false));
    }

    @Test
    public void testIn() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isInPredicate = Predicates.in(collection);
        assertFalse(isInPredicate.apply(0));
        assertTrue(isInPredicate.apply(1));
        assertTrue(isInPredicate.apply(2));
        assertTrue(isInPredicate.apply(3));
        assertTrue(isInPredicate.apply(4));
        assertTrue(isInPredicate.apply(5));
        assertFalse(isInPredicate.apply(6));
    }

    @Test
    public void testCompose() {
        Function<Integer, Boolean> isEvenFunction = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isEvenPredicate = Predicates.alwaysTrue();
        Predicate<Integer> isEvenAndEvenPredicate = Predicates.compose(isEvenPredicate, isEvenFunction);
        assertFalse(isEvenAndEvenPredicate.apply(0));
        assertFalse(isEvenAndEvenPredicate.apply(1));
        assertTrue(isEvenAndEvenPredicate.apply(2));
        assertTrue(isEvenAndEvenPredicate.apply(4));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfStringPredicate = Predicates.instanceOf(String.class);
        assertFalse(isInstanceOfStringPredicate.apply(1));
        assertTrue(isInstanceOfStringPredicate.apply("test"));
        assertFalse(isInstanceOfStringPredicate.apply(new Object()));

        Predicate<Object> isInstanceOfNumberPredicate = Predicates.instanceOf(Number.class);
        assertFalse(isInstanceOfNumberPredicate.apply(1));
        assertTrue(isInstanceOfNumberPredicate.apply(1.0));
        assertTrue(isInstanceOfNumberPredicate.apply(new Integer(1)));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromStringPredicate = Predicates.assignableFrom(String.class);
        assertFalse(isAssignableFromStringPredicate.apply(Number.class));
        assertTrue(isAssignableFromStringPredicate.apply(StringBuilder.class));

        Predicate<Class<?>> isAssignableFromNumberPredicate = Predicates.assignableFrom(Number.class);
        assertFalse(isAssignableFromNumberPredicate.apply(String.class));
        assertTrue(isAssignableFromNumberPredicate.apply(Integer.class));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern("Pattern");
        assertFalse(containsPatternPredicate.apply("test"));
        assertTrue(containsPatternPredicate.apply("testPattern"));
        assertTrue(containsPatternPredicate.apply("PatternTest"));
    }

    @Test
    public void testContainsPatternWithRegex() {
        Pattern pattern = Pattern.compile("Pattern");
        Predicate<String> containsPatternPredicate = Predicates.contains(pattern);
        assertFalse(containsPatternPredicate.apply("test"));
        assertTrue(containsPatternPredicate.apply("testPattern"));
        assertTrue(containsPatternPredicate.apply("PatternTest"));
    }
}