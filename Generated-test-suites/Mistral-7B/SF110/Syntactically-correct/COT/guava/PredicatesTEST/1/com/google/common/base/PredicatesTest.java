package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply(new Object()));

        Object obj = new Object();
        assertFalse(isNull.apply(obj));

        obj = null;
        assertTrue(isNull.apply(obj));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertTrue(notNull.apply(new Object()));

        Object obj = null;
        assertFalse(notNull.apply(obj));

        obj = new Object();
        assertTrue(notNull.apply(obj));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();

        Predicate<Boolean> notTruePredicate = Predicates.not(truePredicate);
        assertFalse(notTruePredicate.apply(true));
        assertTrue(notTruePredicate.apply(false));

        Predicate<Boolean> notFalsePredicate = Predicates.not(falsePredicate);
        assertFalse(notFalsePredicate.apply(false));
        assertTrue(notFalsePredicate.apply(true));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> positivePredicate = Predicates.not(Predicates.isNull());

        Predicate<Integer> andPredicate = Predicates.and(evenPredicate, positivePredicate);

        Integer[] numbers = { null, 1, 2, 3, 4, 5, 6 };
        for (Integer number : numbers) {
            if (number == null) {
                assertFalse(andPredicate.apply(number));
            } else {
                assertTrue(andPredicate.apply(number));
            }
        }
    }

    @Test
    public void testOr() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> oddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(evenPredicate, oddPredicate);

        Integer[] numbers = { null, 1, 2, 3, 4, 5, 6 };
        for (Integer number : numbers) {
            if (number == null) {
                assertFalse(orPredicate.apply(number));
            } else {
                if (number % 2 == 0) {
                    assertTrue(orPredicate.apply(number));
                } else {
                    assertTrue(orPredicate.apply(number));
                }
            }
        }
    }

    @Test
    public void testEqualsTo() {
        Predicate<Object> equalToNullPredicate = Predicates.equalTo(null);
        Predicate<Object> notNullPredicate = Predicates.notNull();

        Predicate<Object> andPredicate = Predicates.and(equalToNullPredicate, notNullPredicate);

        Object[] objects = { null, new Object(), new Object(), new Object() };
        for (Object object : objects) {
            assertFalse(andPredicate.apply(object));
        }

        Object target = new Object();
        Predicate<Object> equalToTargetPredicate = Predicates.equalTo(target);

        for (Object object : objects) {
            if (object == target) {
                assertTrue(equalToTargetPredicate.apply(object));
            } else {
                assertFalse(equalToTargetPredicate.apply(object));
            }
        }
    }

    @Test
    public void testInstanceOf() {
        Class<?> StringClass = String.class;
        Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(StringClass);

        Object[] objects = { null, new Object(), "hello", new String("hello") };
        for (Object object : objects) {
            if (object == null) {
                assertFalse(instanceOfStringPredicate.apply(object));
            } else if (object.getClass() == StringClass) {
                assertTrue(instanceOfStringPredicate.apply(object));
            } else {
                assertFalse(instanceOfStringPredicate.apply(object));
            }
        }
    }

    @Test
    public void testAssignableFrom() {
        Class<?> NumberClass = Number.class;
        Class<?> IntegerClass = Integer.class;
        Predicate<Class<?>> assignableFromNumberPredicate = Predicates.assignableFrom(NumberClass);
        Predicate<Class<?>> assignableFromIntegerPredicate = Predicates.assignableFrom(IntegerClass);

        Class<?>[] classes = { null, NumberClass, Integer.class, String.class };
        for (Class<?> clazz : classes) {
            if (clazz == null) {
                assertFalse(assignableFromNumberPredicate.apply(clazz));
                assertFalse(assignableFromIntegerPredicate.apply(clazz));
            } else if (clazz.isAssignableFrom(NumberClass)) {
                assertTrue(assignableFromNumberPredicate.apply(clazz));
                assertTrue(assignableFromIntegerPredicate.apply(clazz));
            } else if (clazz == String.class) {
                assertFalse(assignableFromNumberPredicate.apply(clazz));
                assertFalse(assignableFromIntegerPredicate.apply(clazz));
            } else {
                assertFalse(assignableFromNumberPredicate.apply(clazz));
                assertTrue(assignableFromIntegerPredicate.apply(clazz));
            }
        }
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inListPredicate = Predicates.in(list);

        Integer[] numbers = { null, 0, 1, 2, 3, 4, 5, 6 };
        for (Integer number : numbers) {
            if (number == null) {
                assertFalse(inListPredicate.apply(number));
            } else {
                if (list.contains(number)) {
                    assertTrue(inListPredicate.apply(number));
                } else {
                    assertFalse(inListPredicate.apply(number));
                }
            }
        }
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Function<Integer, Boolean> isPositiveFunction = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer integer) {
                return integer > 0;
            }
        };

        Predicate<Integer> isEvenAndPositivePredicate = Predicates.compose(isEvenPredicate, isPositiveFunction);

        Integer[] numbers = { null, -2, -1, 0, 1, 2, 3, 4, 5, 6 };
        for (Integer number : numbers) {
            if (number == null) {
                assertFalse(isEvenAndPositivePredicate.apply(number));
            } else {
                if (isEvenPredicate.apply(number) && isPositiveFunction.apply(number)) {
                    assertTrue(isEvenAndPositivePredicate.apply(number));
                } else {
                    assertFalse(isEvenAndPositivePredicate.apply(number));
                }
            }
        }
    }

    @Test(expected = AssertionError.class)
    public void testContainsPattern() {
        String regex = "ab";
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern(regex);

        CharSequence[] charSequences = { null, "", "abc", "ab", "abcd", "abcde" };
        for (CharSequence charSequence : charSequences) {
            if (charSequence == null) {
                assertFalse(containsPatternPredicate.apply(charSequence));
            } else if (charSequence.toString().matches(regex)) {
                assertTrue(containsPatternPredicate.apply(charSequence));
            } else {
                assertFalse(containsPatternPredicate.apply(charSequence));
            }
        }

        // This test case should throw an AssertionError because the pattern does not match the input
        assertFalse(containsPatternPredicate.apply("xyz"));
    }

    @Test(expected = AssertionError.class)
    public void testContainsPatternFromString() {
        String patternString = "ab";
        Predicate<CharSequence> containsPatternFromStringPredicate = Predicates.containsPatternFromString(patternString);

        CharSequence[] charSequences = { null, "", "abc", "ab", "abcd", "abcde" };
        for (CharSequence charSequence : charSequences) {
            if (charSequence == null) {
                assertFalse(containsPatternFromStringPredicate.apply(charSequence));
            } else if (charSequence.toString().matches(Pattern.compile(patternString).pattern())) {
                assertTrue(containsPatternFromStringPredicate.apply(charSequence));
            } else {
                assertFalse(containsPatternFromStringPredicate.apply(charSequence));
            }
        }

        // This test case should throw an AssertionError because the pattern does not match the input
        assertFalse(containsPatternFromStringPredicate.apply("xyz"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputInPredicates() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        Function<Object, Object> identityFunction = new Function<Object, Object>() {
            @Override
            public Object apply(Object object) {
                return object;
            }
        };
        Predicate<Object> compositionPredicate = Predicates.compose(isNullPredicate, identityFunction);

        Object[] objects = { null, new Object(), new Object(), new Object() };
        for (Object object : objects) {
            if (object == null) {
                assertTrue(compositionPredicate.apply(object));
            } else {
                assertFalse(compositionPredicate.apply(object));
            }
        }

        // This test case should throw a NullPointerException because the input is null
        assertFalse(compositionPredicate.apply(null));
    }

    
}