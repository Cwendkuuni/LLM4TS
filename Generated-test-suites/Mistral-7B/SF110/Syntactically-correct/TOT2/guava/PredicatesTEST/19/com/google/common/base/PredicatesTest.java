package com.example;

import com.google.common.base.Predicates;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(10));
        assertTrue(predicate.apply(20));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(10));
        assertFalse(predicate.apply(20));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertNull(isNullPredicate.apply(null));
        assertFalse(isNullPredicate.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();
        assertNotNull(notNullPredicate.apply(new Object()));
        assertFalse(notNullPredicate.apply(null));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();

        Predicate<Boolean> notTruePredicate = Predicates.not(truePredicate);
        Predicate<Boolean> notFalsePredicate = Predicates.not(falsePredicate);

        assertTrue(notTruePredicate.apply(false));
        assertFalse(notFalsePredicate.apply(true));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> evenPredicate = Predicates.alwaysTrue().and(new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        });

        assertTrue(evenPredicate.apply(2));
        assertFalse(evenPredicate.apply(1));
    }

    @Test
    public void testAndWithNulls() {
        Predicate<String> stringPredicate1 = Predicates.notNull();
        Predicate<Integer> integerPredicate1 = Predicates.alwaysTrue();
        Predicate<String> stringPredicate2 = Predicates.isNull();
        Predicate<Integer> integerPredicate2 = Predicates.alwaysFalse();

        Predicate<Object> andPredicate = Predicates.and(stringPredicate1, integerPredicate1, stringPredicate2, integerPredicate2);

        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(new Object()));
        assertFalse(andPredicate.apply(""));
        assertFalse(andPredicate.apply(1));

        String testString = "hello";
        Integer testInteger = 10;

        assertTrue(andPredicate.apply(testString));
        assertTrue(andPredicate.apply(testInteger));
    }

    @Test
    public void testOr() {
        Predicate<Integer> evenPredicate = Predicates.alwaysTrue().and(new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        });

        Predicate<Integer> oddPredicate = Predicates.not(evenPredicate);

        Predicate<Integer> orPredicate = Predicates.or(evenPredicate, oddPredicate);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
    }

    @Test
    public void testEquals() {
        Predicate<Object> equalsPredicate = Predicates.equalTo("testString");
        Predicate<Object> notEqualsPredicate = Predicates.not(Predicates.equalTo("testString"));

        Object testObject = "testString";
        Object nullObject = null;
        Object differentObject = new Object();

        assertTrue(equalsPredicate.apply(testObject));
        assertFalse(equalsPredicate.apply(nullObject));
        assertFalse(equalsPredicate.apply(differentObject));

        assertTrue(notEqualsPredicate.apply(testObject));
        assertFalse(notEqualsPredicate.apply(nullObject));
        assertTrue(notEqualsPredicate.apply(differentObject));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);

        assertTrue(instanceOfPredicate.apply("testString"));
        assertFalse(instanceOfPredicate.apply(1));
        assertFalse(instanceOfPredicate.apply(new Object()));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(String.class);

        assertTrue(assignableFromPredicate.apply(String[].class));
        assertFalse(assignableFromPredicate.apply(Integer.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        Predicate<Integer> inPredicate = Predicates.in(list);

        assertTrue(inPredicate.apply(2));
        assertFalse(inPredicate.apply(5));
    }

    @Test
    public void testComposition() {
        Predicate<Integer> evenPredicate = Predicates.alwaysTrue().and(new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        });

        Predicate<Integer> isZeroPredicate = Predicates.equalTo(0);

        Predicate<Integer> compositionPredicate = Predicates.compose(evenPredicate, new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * 2;
            }
        });

        assertTrue(compositionPredicate.apply(2));
        assertFalse(compositionPredicate.apply(1));
    }

    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern(".*test.*");

        assertTrue(containsPatternPredicate.apply("testString"));
        assertFalse(containsPatternPredicate.apply("helloWorld"));
    }

    @Test
    public void testEdgeCases() {
        // Test null arguments
        Predicate<String> nullPredicate = Predicates.alwaysTrue();
        assertNull(nullPredicate.apply(null));

        // Test empty collections
        List<Integer> emptyList = Collections.emptyList();
        Predicate<Integer> emptyListPredicate = Predicates.in(emptyList);
        assertFalse(emptyListPredicate.apply(10));

        // Test different types
        Predicate<String> stringPredicate = Predicates.equalTo("hello");
        assertFalse(stringPredicate.apply(10));

        // Test exceptions in functions and predicates
        try {
            Predicate<String> exceptionPredicate = new ThrowingPredicate<String>() {};
            assertThrows(RuntimeException.class, () -> exceptionPredicate.apply(null));
        } catch (RuntimeException e) {
            // Expected
        }
    }
}