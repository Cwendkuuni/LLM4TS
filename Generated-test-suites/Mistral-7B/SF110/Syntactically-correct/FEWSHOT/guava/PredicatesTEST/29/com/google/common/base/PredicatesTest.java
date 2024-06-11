package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PredicatesTest {

    // Test alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(true));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply("foo"));
    }

    // Test alwaysFalse()
    @Test
    public void testAlwaysFalse() {
        Predicate<Object> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(true));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply("foo"));
    }

    // Test isNull()
    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertNull(predicate.apply(null));
        assertFalse(predicate.apply(0));
        assertFalse(predicate.apply(""));
        assertFalse(predicate.apply(new Object()));
    }

    // Test notNull()
    @Test
    public void testNotNull() {
        Predicate<Object> predicate = Predicates.notNull();
        assertNull(predicate.apply(null));
        assertTrue(predicate.apply(0));
        assertTrue(predicate.apply(""));
        assertTrue(predicate.apply(new Object()));
    }

    // Test not()
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

    // Test and()
    @Test
    public void testAnd() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> predicatePositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(predicateEven, predicatePositive);

        int[] numbers = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int number : numbers) {
            boolean result = andPredicate.apply(number);
            if (number >= 0 && number % 2 == 0) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    // Test or()
    @Test
    public void testOr() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> predicatePositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(predicateEven, predicatePositive);

        int[] numbers = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int number : numbers) {
            boolean result = orPredicate.apply(number);
            if ((number % 2 == 0 || number > 0)) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    // Test equalTo()
    @Test
    public void testEqualTo() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        Predicate<Object> predicateObj1 = Predicates.equalTo(obj1);

        assertTrue(predicateObj1.apply(obj1));
        assertFalse(predicateObj1.apply(obj2));
    }

    // Test instanceOf()
    @Test(expected = AssertionError.class)
    public void testInstanceOf() {
        Class<String> clazzString = String.class;
        Predicate<Object> predicateString = Predicates.instanceOf(clazzString);

        predicateString.apply(new Object());

        Predicate<String> predicateStringInstance = Predicates.instanceOf(String.class);
        assertNotNull(predicateStringInstance.apply(new String("foo")));
    }

    // Test assignableFrom()
    @Test
    public void testAssignableFrom() {
        Class<Integer> clazzInteger = Integer.class;
        Class<Number> clazzNumber = Number.class;

        Predicate<Class<?>> predicateNumber = Predicates.assignableFrom(clazzNumber);
        assertTrue(predicateNumber.apply(clazzInteger));
    }

    // Test in()
    @Test
    public void testIn() {
        Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> predicateNumber = Predicates.in(numbers);

        assertTrue(predicateNumber.apply(1));
        assertTrue(predicateNumber.apply(2));
        assertTrue(predicateNumber.apply(3));
        assertTrue(predicateNumber.apply(4));
        assertTrue(predicateNumber.apply(5));
        assertFalse(predicateNumber.apply(6));
    }

    // Test compose()
    @Test
    public void testCompose() {
        Predicate<String> predicateStringEvenLength = new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return s.length() % 2 == 0;
            }
        };

        Function<String, Integer> functionStringLength = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };

        Predicate<String> predicateStringEvenLengthSquare = Predicates.compose(predicateStringEvenLength, functionStringLength);

        String[] strings = {"", "foo", "bar", "baz", "qux"};

        for (String string : strings) {
            int result = predicateStringEvenLengthSquare.apply(string);
            if (string.length() % 2 == 0) {
               assertTrue(result > 0);
            } else {
                assertFalse(result > 0);
            }
        }
    }

    // Test containsPattern()
    @Test
    public void testContainsPattern() {
        Predicate<String> predicateStringPattern = Predicates.containsPattern(".*foo.*");

        assertTrue(predicateStringPattern.apply("foo"));
        assertTrue(predicateStringPattern.apply("barfooqux"));
        assertTrue(predicateStringPattern.apply("fooqux"));
        assertFalse(predicateStringPattern.apply("barqux"));
        assertFalse(predicateStringPattern.apply("fooquxbar"));
    }
}