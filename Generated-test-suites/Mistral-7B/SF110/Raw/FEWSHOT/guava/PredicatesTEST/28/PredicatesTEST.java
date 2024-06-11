package com.google.common.base;

import static org.junit.Assert.*;
import com.google.common.base.Predicates.*;
import org.junit.Test;

import java.util.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = alwaysTrue();
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply("hello"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply("hello"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicate = isNull();
        Predicate<Boolean> negatedPredicate = not(predicate);
        assertFalse(negatedPredicate.apply(null));
        assertTrue(negatedPredicate.apply(false));
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

        Predicate<Integer> andPredicate = and(isEven, isPositive);

        List<Integer> testCases = Arrays.asList(-2, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        for (Integer number : testCases) {
            assertTrue(andPredicate.apply(number));
        }
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

        Predicate<Integer> orPredicate = or(isEven, isOdd);

        List<Integer> testCases = Arrays.asList(-2, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        for (Integer number : testCases) {
            assertTrue(orPredicate.apply(number));
        }

        assertFalse(orPredicate.apply(-1));
    }

    @Test
    public void testEqualTo() {
        Predicate<String> isEqualToString = equalTo("hello");

        String testCases[] = {"hello", "world", "hello world", "HELLO", "HELLO WORLD"};

        for (String str : testCases) {
            if (str.equals("hello")) {
                assertTrue(isEqualToString.apply(str));
            } else {
                assertFalse(isEqualToString.apply(str));
            }
        }
    }

    @Test
    public void testInstanceOf() {
        Predicate<Class<?>> isInstanceOfClass = assignableFrom(String.class);

        Class<?> testCases[] = {String.class, Integer.class, Double.class, List.class, Map.class};

        for (Class<?> clazz : testCases) {
            if (clazz == String.class) {
                assertTrue(isInstanceOfClass.apply(String.class));
            } else {
                assertFalse(isInstanceOfClass.apply(String.class));
            }
        }
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromClass = assignableFrom(List.class);

        Class<?> testCases[] = {String.class, Integer.class, Double.class, List.class, Map.class};

        for (Class<?> clazz : testCases) {
            if (List.class.isAssignableFrom(clazz)) {
                assertTrue(isAssignableFromClass.apply(List.class));
            } else {
                assertFalse(isAssignableFromClass.apply(List.class));
            }
        }
    }

    @Test
    public void testIn() {
        List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> inPredicate = in(testList);

        List<Integer> testCases = Arrays.asList(-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        for (Integer number : testCases) {
            if (testList.contains(number)) {
                assertTrue(inPredicate.apply(number));
            } else {
                assertFalse(inPredicate.apply(number));
            }
        }
    }
}