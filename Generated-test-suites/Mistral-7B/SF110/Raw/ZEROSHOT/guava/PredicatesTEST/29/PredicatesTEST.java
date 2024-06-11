package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(null));
        assertTrue(alwaysTruePredicate.apply("test"));
        assertTrue(alwaysTruePredicate.apply(123));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(null));
        assertFalse(alwaysFalsePredicate.apply("test"));
        assertFalse(alwaysFalsePredicate.apply(123));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertFalse(isNullPredicate.apply(null));
        assertFalse(isNullPredicate.apply("test"));
        assertFalse(isNullPredicate.apply(123));

        Object obj = new Object();
        assertFalse(isNullPredicate.apply(obj));

        obj = null;
        assertTrue(isNullPredicate.apply(obj));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();
        assertFalse(notNullPredicate.apply(null));
        assertFalse(notNullPredicate.apply("test"));
        assertFalse(notNullPredicate.apply(123));

        Object obj = new Object();
        assertTrue(notNullPredicate.apply(obj));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> alwaysTruePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> alwaysFalsePredicate = Predicates.alwaysFalse();

        Predicate<Boolean> notAlwaysTruePredicate = Predicates.not(alwaysTruePredicate);
        Predicate<Boolean> notAlwaysFalsePredicate = Predicates.not(alwaysFalsePredicate);

        assertFalse(notAlwaysTruePredicate.apply(true));
        assertTrue(notAlwaysTruePredicate.apply(false));

        assertFalse(notAlwaysFalsePredicate.apply(true));
        assertTrue(notAlwaysFalsePredicate.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        List<Predicate<Integer>> components = Arrays.asList(isEven, isPositive);
        Predicate<Integer> andPredicate = Predicates.and(components);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
    }

    @Test
    public void testAndWithTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
    }

    @Test
    public void testAndWithIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        List<Predicate<Integer>> components = Arrays.asList(isEven, isPositive);
        Predicate<Integer> andPredicate = Predicates.and(components);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
    }

    @Test
    public void testOr() {
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

        List<Predicate<Integer>> components = Arrays.asList(isEven, isOdd);
        Predicate<Integer> orPredicate = Predicates.or(components);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
    }

    @Test
    public void testOrWithTwoPredicates() {
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

        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
    }

    @Test
    public void testOrWithIterable() {
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

        List<Predicate<Integer>> components = Arrays.asList(isEven, isOdd);
        Predicate<Integer> orPredicate = Predicates.or(components);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
    }

    @Test
    public void testEqualTo() {
        Predicate<String> stringPredicate = Predicates.equalTo("test");

        assertTrue(stringPredicate.apply("test"));
        assertFalse(stringPredicate.apply("test1"));
    }

    @Test
    public void testInstanceOf() {
        Class<String> stringClass = String.class;
        Predicate<Object> stringPredicate = Predicates.instanceOf(stringClass);

        assertTrue(stringPredicate.apply("test"));
        assertFalse(stringPredicate.apply(123));
    }

    @Test
    public void testAssignableFrom() {
        Class<String> stringClass = String.class;
        Class<Number> numberClass = Number.class;

        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(numberClass);
        assertTrue(assignableFromPredicate.apply(stringClass));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(numbers);

        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertFalse(inPredicate.apply(4));
    }

    @Test
    public void testCompose() {
        Predicate<String> isEmptyString = new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.isEmpty();
            }
        };

        Function<String, Integer> lengthFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };

        Predicate<String> isEmptyPredicate = Predicates.compose(isEmptyString, lengthFunction);

        assertTrue(isEmptyPredicate.apply(String.valueOf(0)));
        assertFalse(isEmptyPredicate.apply("test"));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(".*test.*");

        assertTrue(containsPatternPredicate.apply("test"));
        assertFalse(containsPatternPredicate.apply("test1"));
        assertFalse(containsPatternPredicate.apply("test2"));
    }

    @Test
    public void testContains() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(Pattern.compile("test"));

        String testString = "This is a test string.";
        assertTrue(containsPatternPredicate.apply(testString));

        String noTestString = "This is not a test string.";
        assertFalse(containsPatternPredicate.apply(noTestString));
    }
}