package com.google.common.base;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    

    // Testing alwaysTrue() method
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));
    }

    // Testing alwaysFalse() method
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));
    }

    // Testing isNull() method
    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2));
        assertTrue(isNull.apply(null));
    }

    // Testing notNull() method
    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2));
    }

    // Testing not() method
    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();

        Predicate<Boolean> notTruePredicate = Predicates.not(truePredicate);
        assertFalse(notTruePredicate.apply(true));
        assertTrue(notTruePredicate.apply(false));

        Predicate<Boolean> notFalsePredicate = Predicates.not(falsePredicate);
        assertTrue(notFalsePredicate.apply(true));
        assertFalse(notFalsePredicate.apply(false));
    }

    // Testing and() method with Iterable
    @Test
    public void testAndIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.notNull();

        List<Predicate<Integer>> components = Arrays.asList(isEven, isPositive);
        Predicate<Integer> andPredicate = Predicates.and(components);

        int[] testData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : testData) {
            assertTrue(andPredicate.apply(i));
        }
    }

    // Testing and() method with Predicate array
    @Test
    public void testAndPredicateArray() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.notNull();

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        int[] testData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : testData) {
            if (i <= 0) {
                assertFalse(andPredicate.apply(i));
            } else {
                assertTrue(andPredicate.apply(i));
            }
        }
    }

    // Testing and() method with two Predicates
    @Test
    public void testAndTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.notNull();

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        int[] testData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : testData) {
            if (i <= 0) {
                assertFalse(andPredicate.apply(i));
            } else {
                assertTrue(andPredicate.apply(i));
            }
        }
    }

    // Testing or() method with Iterable
    @Test
    public void testOrIterable() {
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

        int[] testData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : testData) {
            if (i % 2 == 0) {
                assertTrue(orPredicate.apply(i));
            } else {
                assertTrue(orPredicate.apply(i));
            }
        }
    }

    // Testing or() method with Predicate array
    @Test
    public void testOrPredicateArray() {
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

        int[] testData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : testData) {
            if (i % 2 == 0) {
                assertTrue(orPredicate.apply(i));
            } else {
                assertTrue(orPredicate.apply(i));
            }
        }
    }

    // Testing or() method with two Predicates
    @Test
    public void testOrTwoPredicates() {
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

        int[] testData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : testData) {
            if (i % 2 == 0) {
                assertTrue(orPredicate.apply(i));
            } else {
                assertTrue(orPredicate.apply(i));
            }
        }
    }

    // Testing equalTo() method
    @Test
    public void testEqualTo() {
        Predicate<String> eqPredicate = Predicates.equalTo("Test String");
        assertTrue(eqPredicate.apply("Test String"));
        assertFalse(eqPredicate.apply("Different String"));
    }

    // Testing instanceOf() method
    @Test(expected = AssertionError.class)
    public void testInstanceOfError() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);
        instanceOfPredicate.apply(new Object());
    }

    // Testing instanceOf() method with valid input
    @Test
    public void testInstanceOfValid() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);
        assertTrue(instanceOfPredicate.apply("Test String"));
    }

    // Testing assignableFrom() method
    @Test(expected = AssertionError.class)
    public void testAssignableFromError() {
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(int[].class);
        assignableFromPredicate.apply(String.class);
    }

    // Testing assignableFrom() method with valid input
    @Test
    public void testAssignableFromValid() {
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(String[].class);
        assertTrue(assignableFromPredicate.apply(String[].class));
        assertTrue(assignableFromPredicate.apply(String[][].class));
    }

    // Testing in() method with null collection
    @Test(expected = NullPointerException.class)
    public void testInNullCollection() {
        Predicate<Integer> inPredicate = Predicates.in(null);
        inPredicate.apply(1);
    }

    // Testing in() method with empty collection
    @Test
    public void testInEmptyCollection() {
        Predicate<Integer> inPredicate = Predicates.in(Collections.emptyList());
        assertFalse(inPredicate.apply(1));
    }

    // Testing in() method with valid input
    @Test
    public void testInValidInput() {
        List<Integer> testData = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inPredicate = Predicates.in(testData);

        for (Integer i : testData) {
            assertTrue(inPredicate.apply(i));
        }

        assertFalse(inPredicate.apply(6));
    }

    // Testing compose() method with null input
    @Test(expected = NullPointerException.class)
    public void testComposeNullInput() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        };

        Predicate<Integer> predicate = null;
        Predicate<Integer> composedPredicate = Predicates.compose(predicate, function);
    }

    // Testing compose() method with valid input
    @Test
    public void testComposeValidInput() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        };

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> composedPredicate = Predicates.compose(isEven, function);

        int[] testData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int i : testData) {
            if (i % 2 == 0) {
                assertTrue(composedPredicate.apply(i));
            } else {
                assertFalse(composedPredicate.apply(i));
            }
        }
    }

    // Testing containsPattern() method with null input
    @Test(expected = NullPointerException.class)
    public void testContainsPatternNullInput() {
        Predicate<String> patternPredicate = Predicates.containsPattern((String)null);
        patternPredicate.apply("Test String");
    }

    // Testing containsPattern() method with invalid pattern
    @Test(expected = IllegalArgumentException.class)
    public void testContainsPatternInvalidPattern() {
        Predicate<String> patternPredicate = Predicates.containsPattern(".*\\{.*\\}");
        patternPredicate.apply("Test String");
    }

    // Testing containsPattern() method with valid pattern
    @Test
    public void testContainsPatternValidPattern() {
        Predicate<String> patternPredicate = Predicates.containsPattern("Test.*String");
        assertTrue(patternPredicate.apply("Test String"));
        assertFalse(patternPredicate.apply("Different String"));
    }

    // Testing containsPatternFromString() method with null input
    @Test(expected = NullPointerException.class)
    public void testContainsPatternFromStringNullInput() {
        Predicate<String> patternPredicate = Predicates.containsPatternFromString((String)null);
        patternPredicate.apply("Test String");
    }

    // Testing containsPatternFromString() method with invalid pattern
    @Test(expected = IllegalArgumentException.class)
    public void testContainsPatternFromStringInvalidPattern() {
        Predicate<String> patternPredicate = Predicates.containsPatternFromString(".*\\{.*\\}");
        patternPredicate.apply("Test String");
    }

    // Testing containsPatternFromString() method with valid pattern
    @Test
    public void testContainsPatternFromStringValidPattern() {
        Predicate<String> patternPredicate = Predicates.containsPatternFromString("Test.*String");
        assertTrue(patternPredicate.apply("Test String"));
        assertFalse(patternPredicate.apply("Different String"));
    }

    
}