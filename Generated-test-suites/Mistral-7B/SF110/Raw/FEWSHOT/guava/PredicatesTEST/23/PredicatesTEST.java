package com.google.common.base;

import static org.junit.Assert.*;
import com.google.common.base.Predicates.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = alwaysTrue();
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = alwaysFalse();
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(2));
        assertFalse(predicate.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = isNull();
        assertNull(predicate.apply(null));
        assertEquals(0, predicate.apply(new Object()).hashCode());
    }

    @Test
    public void testNotNull() {
        Predicate<Object> predicate = notNull();
        assertNotNull(predicate.apply(new Object()));
        assertEquals(1, predicate.apply(new Object()).hashCode());
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicateTrue = alwaysTrue();
        Predicate<Boolean> predicateFalse = alwaysFalse();

        Predicate<Boolean> notPredicateTrue = not(predicateTrue);
        Predicate<Boolean> notPredicateFalse = not(predicateFalse);

        assertFalse(notPredicateTrue.apply(true));
        assertTrue(notPredicateTrue.apply(false));

        assertFalse(notPredicateFalse.apply(true));
        assertFalse(notPredicateFalse.apply(false));

        assertTrue(notPredicateTrue.apply(!true));
        assertFalse(notPredicateTrue.apply(!false));

        assertTrue(notPredicateFalse.apply(!true));
        assertTrue(notPredicateFalse.apply(!false));
    }

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

        Predicate<Integer> andPredicate = and(predicateEven, predicatePositive);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        for (Integer number : numbers) {
            assertTrue(andPredicate.apply(number));
        }

        List<Integer> negativeNumbers = Arrays.asList(-1, -2, -3, -4, -5, -6);

        for (Integer number : negativeNumbers) {
                assertFalse(andPredicate.apply(number));
        }
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicateEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> predicateOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = or(predicateEven, predicateOdd);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        for (Integer number : numbers) {
            assertTrue(orPredicate.apply(number));
        }

        List<Integer> negativeNumbers = Arrays.asList(-1, -2, -3, -4, -5, -6);

        for (Integer number : negativeNumbers) {
            assertTrue(orPredicate.apply(number));
        }

        Predicate<Integer> predicateZero = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer == 0;
            }
        };

        Predicate<Integer> orPredicateZero = or(predicateEven, predicateZero);

        List<Integer> zeros = Arrays.asList(0, 0, 0);

        for (Integer number : zeros) {
            assertTrue(orPredicateZero.apply(number));
        }

        List<Integer> nonZeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        for (Integer number : nonZeros) {
            assertTrue(orPredicateZero.apply(number));
        }
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> predicateFive = equalTo(5);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        for (Integer number : numbers) {
            if (number.equals(5)) {
                assertTrue(predicateFive.apply(number));
            } else {
                assertFalse(predicateFive.apply(number));
            }
        }
    }

    @Test
    public void testInstanceOf() {
        @SuppressWarnings("unchecked")
        Predicate<String> predicateString = instanceOf(String.class);

        String testString = "Hello World!";

        assertTrue(predicateString.apply(testString));

        Object testObject = new Object();

        assertFalse(predicateString.apply(testObject));
    }

    @Test
    public void testAssignableFrom() {
        @SuppressWarnings("unchecked")
        Predicate<Class<?>> predicateStringClass = assignableFrom(String.class);

        Class<? > testClass = Integer.class;

        assertFalse(predicateStringClass.apply(testClass));

        Class<? > testStringClass = String.class;

        assertTrue(predicateStringClass.apply(testStringClass));
    }

    @Test
    public void testIn() {
        Collection<Integer> testCollection = Arrays.asList(1, 2, 3, 4, 5);

        Predicate<Integer> predicateInCollection = in(testCollection);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        for (Integer number : numbers) {
            if (testCollection.contains(number)) {
                assertTrue(predicateInCollection.apply(number));
            } else {
                assertFalse(predicateInCollection.apply(number));
            }
        }

        List<Integer> negativeNumbers = Arrays.asList(-1, -2, -3, -4, -5, -6);

        for (Integer number : negativeNumbers) {
            assertFalse(predicateInCollection.apply(number));
        }
    }

    @Test
    public void testCompose() {
        Function<String, Integer> functionLength = new Function<String, Integer>() {
            @Override
            public Integer apply(String string) {
                return string.length();
            }
        };

        Predicate<String> predicateLengthGreaterThanFive = new Predicate<String>() {
            @Override
            public boolean apply(String string) {
                return string.length() > 5;
            }
        };

        Predicate<String> predicateCompose = compose(predicateLengthGreaterThanFive, functionLength);

        String testString1 = "Test";
        String testString2 = "TestString";

        assertTrue(predicateCompose.apply(testString1));
        assertTrue(predicateCompose.apply(testString2));

        String testString3 = "TestString12345";

        assertFalse(predicateCompose.apply(testString3));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> predicatePattern = containsPattern("abc");

        List<String> strings = Arrays.asList("abcd", "efghijk", "lmnopqrstuvwxyz");

        for (String string : strings) {
            if (string.contains("abc")) {
                assertTrue(predicatePattern.apply(string));
            } else {
                assertFalse(predicatePattern.apply(string));
            }
        }

        List<String> stringsWithoutPattern = Arrays.asList("123", "Hello", "test");

        for (String string : stringsWithoutPattern) {
            assertFalse(predicatePattern.apply(string));
        }
    }
}