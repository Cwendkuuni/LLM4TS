package com.example.predicates; // replace with your test package name

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import com.google.common.base.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertNull(predicate.apply(null));
        assertFalse(predicate.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> predicate = Predicates.notNull();
        assertNull(predicate.apply(null));
        assertTrue(predicate.apply(new Object()));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicateTrue = Predicates.alwaysTrue();
        Predicate<Boolean> predicateFalse = Predicates.alwaysFalse();

        Predicate<Boolean> notPredicateTrue = Predicates.not(predicateTrue);
        Predicate<Boolean> notPredicateFalse = Predicates.not(predicateFalse);

        assertFalse(notPredicateTrue.apply(true));
        assertTrue(notPredicateTrue.apply(false));

        assertFalse(notPredicateFalse.apply(false));
        assertTrue(notPredicateFalse.apply(true));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> predicatePositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(predicateEven, predicatePositive);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(-2));
        assertFalse(andPredicate.apply(1));
    }

    @Test
    public void testAndIterable() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> predicatePositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        List<Predicate<Integer>> predicates = Arrays.asList(predicateEven, predicatePositive);
        Predicate<Integer> andPredicate = Predicates.and(predicates);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(-2));
        assertFalse(andPredicate.apply(1));
    }

    @Test
    public void testAndArray() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> predicatePositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(predicateEven, predicatePositive);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(-2));
        assertFalse(andPredicate.apply(1));

        Predicate<Integer> andPredicate2 = Predicates.and(new Predicate<Integer>[] {predicateEven, predicatePositive});

        assertTrue(andPredicate2.apply(2));
        assertFalse(andPredicate2.apply(-2));
        assertFalse(andPredicate2.apply(1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> predicateOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(predicateEven, predicateOdd);

        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(2));
    }

    @Test
    public void testOrIterable() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> predicateOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        List<Predicate<Integer>> predicates = Arrays.asList(predicateEven, predicateOdd);
        Predicate<Integer> orPredicate = Predicates.or(predicates);

        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(2));
    }

    @Test
    public void testOrArray() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> predicateOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(new Predicate<Integer>[] {predicateEven, predicateOdd});

        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(2));
    }

    @Test
    public void testEqualsTo() {
        Object target = new Object();
        Predicate<Object> equalsToTargetPredicate = Predicates.equalTo(target);

        assertTrue(equalsToTargetPredicate.apply(target));
        assertFalse(equalsToTargetPredicate.apply(new Object()));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(String.class);

        assertTrue(instanceOfStringPredicate.apply("String"));
        assertFalse(instanceOfStringPredicate.apply(new Object()));
        assertFalse(instanceOfStringPredicate.apply(1));
    }

    @Test
    public void testAssignableFrom() {
        Class<String> stringClass = String.class;
        Class<Number> numberClass = Number.class;

        Predicate<Class<?>> assignableFromStringPredicate = Predicates.assignableFrom(stringClass);

        assertTrue(assignableFromStringPredicate.apply(String.class));
        assertTrue(assignableFromStringPredicate.apply(Number.class));
        assertFalse(assignableFromStringPredicate.apply(Integer.class));
    }

    @Test
    public void testIn() {
        List<Integer> targetList = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(targetList);

        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertFalse(inPredicate.apply(4));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> stringToIntegerFunction = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        };

        Predicate<String> stringIsEmptyPredicate = new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.isEmpty();
            }
        };

        Predicate<Integer> integerIsEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<String> composePredicate = Predicates.compose(stringIsEmptyPredicate, stringToIntegerFunction);

        assertFalse(composePredicate.apply(""));
        assertFalse(composePredicate.apply("1"));
        assertTrue(composePredicate.apply("2"));
        assertTrue(composePredicate.apply("10"));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab.*c");

        assertTrue(containsPatternPredicate.apply("abc"));
        assertTrue(containsPatternPredicate.apply("abxyzc"));
        assertFalse(containsPatternPredicate.apply("xyz"));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab.*c");

        assertTrue(containsPatternPredicate.apply("abc"));
        assertTrue(containsPatternPredicate.apply("abxyzc"));
        assertFalse(containsPatternPredicate.apply("xyz"));

        Predicate<String> containsPatternFromStringPredicate = Predicates.containsPatternFromString("ab.*c");

        assertTrue(containsPatternFromStringPredicate.apply("abc"));
        assertTrue(containsPatternFromStringPredicate.apply("abxyzc"));
        assertFalse(containsPatternFromStringPredicate.apply("xyz"));
    }

}