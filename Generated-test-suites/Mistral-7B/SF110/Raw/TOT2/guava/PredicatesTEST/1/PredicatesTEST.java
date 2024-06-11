
package com.example.test;

import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;
import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(new Object()));
        assertTrue(predicate.apply("test string"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> predicate = notNull();
        assertFalse(predicate.apply(null));
        assertTrue(predicate.apply(new Object()));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> p1 = isEqual(Integer.valueOf(1));
        Predicate<Integer> p2 = isEqual(Integer.valueOf(2));

        Predicate<Integer> andPredicate = and(p1, p2);

        assertTrue(andPredicate.apply(Integer.valueOf(1)));
        assertTrue(andPredicate.apply(Integer.valueOf(2)));
        assertFalse(andPredicate.apply(Integer.valueOf(1)));
        assertFalse(andPredicate.apply(Integer.valueOf(3)));
    }

    @Test
    public void testOr() {
        Predicate<Integer> p1 = isEqual(Integer.valueOf(1));
        Predicate<Integer> p2 = isEqual(Integer.valueOf(2));

        Predicate<Integer> orPredicate = or(p1, p2);

        assertTrue(orPredicate.apply(Integer.valueOf(1)));
        assertTrue(orPredicate.apply(Integer.valueOf(2)));
        assertTrue(orPredicate.apply(Integer.valueOf(3)));
        assertFalse(orPredicate.apply(Integer.valueOf(4)));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> predicate = instanceOf(String.class);

        assertTrue(predicate.apply("test string"));
        assertFalse(predicate.apply(new Object()));
    }

    @Test(expected = NullPointerException.class)
    public void testAndThrowsException() {
        Predicate<Integer> p1 = isNull();
        Predicate<Integer> p2 = isEqual(Integer.valueOf(1));

        Predicate<Integer> andPredicate = and(p1, p2);

        andPredicate.apply(null);
    }
}