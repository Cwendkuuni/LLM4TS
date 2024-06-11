package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(2));
        assertFalse(predicate.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<String> predicate = Predicates.isNull();
        assertNull(predicate.apply(null));
        assertFalse(predicate.apply(""));
        assertFalse(predicate.apply("hello"));
    }

    @Test
    public void testNotNull() {
        Predicate<String> predicate = Predicates.notNull();
        assertNull(predicate.apply(null));
        assertTrue(predicate.apply(""));
        assertTrue(predicate.apply("hello"));
    }

    @Test
    public void testNot() {
        Predicate<Integer> predicate = Predicates.isNull();
        Predicate<Integer> notPredicate = Predicates.not(predicate);

        assertFalse(notPredicate.apply(null));
        assertTrue(notPredicate.apply(0));
        assertTrue(notPredicate.apply(1));

        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        predicate = notPredicate;
        notPredicate = Predicates.not(predicate);

        assertTrue(notPredicate.apply(null));
        assertFalse(notPredicate.apply(0));
        assertFalse(notPredicate.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.alwaysFalse();
        Predicate<Integer> andPredicate = Predicates.and(predicate1, predicate2);

        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));

        predicate1 = Predicates.isNull();
        predicate2 = Predicates.alwaysTrue();
        andPredicate = Predicates.and(predicate1, predicate2);

        assertFalse(andPredicate.apply(null));
        assertTrue(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysFalse());
        andPredicate = Predicates.and(predicates);

        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));

        predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysTrue());
        andPredicate = Predicates.and(predicates);

        assertFalse(andPredicate.apply(null));
        assertTrue(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));

        predicate1 = Predicates.isNull();
        predicate2 = Predicates.notNull();
        andPredicate = Predicates.and(predicate1, predicate2);

        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.alwaysTrue();
        Predicate<Integer> orPredicate = Predicates.or(predicate1, predicate2);

        assertFalse(orPredicate.apply(null));
        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));

        predicate1 = Predicates.isNull();
        predicate2 = Predicates.alwaysFalse();
        orPredicate = Predicates.or(predicate1, predicate2);

        assertFalse(orPredicate.apply(null));
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysTrue());
        orPredicate = Predicates.or(predicates);

        assertFalse(orPredicate.apply(null));
        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));

        predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysFalse());
        orPredicate = Predicates.or(predicates);

        assertFalse(orPredicate.apply(null));
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));

        predicate1 = Predicates.isNull();
        predicate2 = Predicates.notNull();
        orPredicate = Predicates.or(predicate1, predicate2);

        assertFalse(orPredicate.apply(null));
        assertFalse(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
    }

    @Test
    public void testEqualsTo() {
        Object target = new Object();
        Predicate<Object> predicate = Predicates.equalTo(target);

        assertTrue(predicate.apply(target));
        assertFalse(predicate.apply(new Object()));
        assertFalse(predicate.apply("hello"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> predicate = Predicates.instanceOf(String.class);

        assertTrue(predicate.apply("hello"));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(new Object()));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> predicate = Predicates.assignableFrom(String.class);

        assertTrue(predicate.apply(String[].class));
        assertTrue(predicate.apply(StringBuilder.class.getClass()));
        assertFalse(predicate.apply(Integer.class));
    }

    @Test
    public void testIn() {
        List<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> predicate = Predicates.in(collection);

        assertFalse(predicate.apply(-1));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
        assertFalse(predicate.apply(4));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Function<Integer, Boolean> function = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> composedPredicate = Predicates.compose(predicate1, function);

        assertFalse(composedPredicate.apply(null));
        assertFalse(composedPredicate.apply(-1));
        assertTrue(composedPredicate.apply(0));
        assertTrue(composedPredicate.apply(1));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> predicate = Predicates.containsPattern("ab.*");

        assertFalse(predicate.apply("abc"));
        assertFalse(predicate.apply("xyz"));
        assertTrue(predicate.apply("abcd"));
        assertTrue(predicate.apply("abxyz"));
    }
}