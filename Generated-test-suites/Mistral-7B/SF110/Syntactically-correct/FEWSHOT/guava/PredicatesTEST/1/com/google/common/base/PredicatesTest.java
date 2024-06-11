package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(null));
        assertTrue(alwaysTruePredicate.apply(1));
        assertTrue(alwaysTruePredicate.apply(2.5f));
        assertTrue(alwaysTruePredicate.apply(""));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(null));
        assertFalse(alwaysFalsePredicate.apply(1));
        assertFalse(alwaysFalsePredicate.apply(2.5f));
        assertFalse(alwaysFalsePredicate.apply(""));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNullPredicate = Predicates.isNull();
        assertNull(isNullPredicate.apply(null));
        assertFalse(isNullPredicate.apply(0));
        assertFalse(isNullPredicate.apply(new Integer(0)));
        assertFalse(isNullPredicate.apply(1));
        assertFalse(isNullPredicate.apply(new Integer(1)));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNullPredicate = Predicates.notNull();
        assertFalse(notNullPredicate.apply(null));
        assertTrue(notNullPredicate.apply(0));
        assertTrue(notNullPredicate.apply(new Integer(0)));
        assertTrue(notNullPredicate.apply(1));
        assertTrue(notNullPredicate.apply(new Integer(1)));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> notEvenPredicate = Predicates.not(isEven);

        assertFalse(notEvenPredicate.apply(0));
        assertTrue(notEvenPredicate.apply(1));
        assertFalse(notEvenPredicate.apply(2));
        assertTrue(notEvenPredicate.apply(3));
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

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
    }

    @Test
    public void testAndIterable() {
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

        Predicate<Integer> andPredicate = Predicates.and(Arrays.asList(isEven, isPositive));

        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
    }

    @Test
    public void testAndArray() {
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

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(

2));

        Predicate<Integer> andPredicate2 = Predicates.and(new Predicate[] {isEven, isPositive});

        assertFalse(andPredicate2.apply(-1));
        assertFalse(andPredicate2.apply(0));
        assertTrue(andPredicate2.apply(1));
        assertTrue(andPredicate2.apply(2));
    }

    @Test
    public void testOr() {
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

        Predicate<Integer> orPredicate = Predicates.or(isEven, isPositive);

        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
    }

    @Test
    public void testOrIterable() {
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

        Predicate<Integer> orPredicate = Predicates.or(Arrays.asList(isEven, isPositive));

        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
    }

    @Test
    public void testOrArray() {
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

        Predicate<Integer> orPredicate = Predicates.or(new Predicate[] {isEven, isPositive});

        assertFalse(orPredicate.apply(-1));
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
    }

    @Test
    public void testEqualsTo() {
        Predicate<String> equalsToPredicate = Predicates.equalTo("Test");
        assertFalse(equalsToPredicate.apply("Hello"));
        assertFalse(equalsToPredicate.apply(null));
        assertFalse(equalsToPredicate.apply("Test1"));
        assertTrue(equalsToPredicate.apply("Test"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> instanceOfStringPredicate = Predicates.instanceOf(String.class);
        assertFalse(instanceOfStringPredicate.apply(1));
        assertFalse(instanceOfStringPredicate.apply(null));
        assertFalse(instanceOfStringPredicate.apply(new Object()));
        assertTrue(instanceOfStringPredicate.apply("Test"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assertFalse(assignableFromString.apply(Integer.class));
        assertFalse(assignableFromString.apply(null));
        assertTrue(assignableFromString.apply(StringBuffer.class));
        assertTrue(assignableFromString.apply(StringBuilder.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> inPredicate = Predicates.in(numbers);

        assertFalse(inPredicate.apply(-1));
        assertFalse(inPredicate.apply(0));
        assertFalse(inPredicate.apply(6));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertTrue(inPredicate.apply(4));
        assertTrue(inPredicate.apply(5));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * input;
            }
        };

        Predicate<Integer> composedPredicate = Predicates.compose(isEven, square);

        assertFalse(composedPredicate.apply(-1));
        assertFalse(composedPredicate.apply(0));
        assertTrue(composedPredicate.apply(1));
        assertTrue(composedPredicate.apply(2));
        assertTrue(composedPredicate.apply(4));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(".*Test.*");

        assertFalse(containsPatternPredicate.apply("Hello"));
        assertFalse(containsPatternPredicate.apply(null));
        assertFalse(containsPatternPredicate.apply("Test1"));
        assertFalse(containsPatternPredicate.apply("Test2"));
        assertTrue(containsPatternPredicate.apply("Test"));
        assertTrue(containsPatternPredicate.apply("TestHello"));
        assertTrue(containsPatternPredicate.apply("Test123"));
    }

    @Test
    public void testDefensiveCopy() {
        Predicate<String> isEmptyString = Predicates.alwaysFalse();
        Predicate<String> isNullString = Predicates.isNull();

        List<Predicate<String>> list1 = Arrays.asList(isEmptyString, isNullString);
        List<Predicate<String>> list2 = Collections.unmodifiableList(Lists.newArrayList(isEmptyString, isNullString));

        List<Predicate<String>> copiedList = Predicates.defensiveCopy(list1);
        List<Predicate<String>> copiedList2 = Predicates.defensiveCopy(list2);

        assertEquals(list1, copiedList);
        assertEquals(list1, copiedList2);

        isEmptyString.apply(null);
        isEmptyString = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && input.isEmpty();
            }
        };

        isNullString.apply("Test");
        isNullString = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input == null;
            }
        };

        assertFalse(copiedList.get(0).apply(null));
        assertTrue(copiedList.get(0).apply(""));
        assertFalse(copiedList.get(1).apply("Test"));
        assertTrue(copiedList.get(1).apply(null));

        assertFalse(copiedList2.get(0).apply(null));
        assertTrue(copiedList2.get(0).apply(""));
        assertFalse(copiedList2.get(1).apply("Test"));
        assertTrue(copiedList2.get(1).apply(null));
    }
}