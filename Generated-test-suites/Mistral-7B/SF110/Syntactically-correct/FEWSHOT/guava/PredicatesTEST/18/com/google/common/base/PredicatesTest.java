package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply("foo"));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply("foo"));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertNull(predicate.apply(null));
        assertFalse(predicate.apply("foo"));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> predicate = Predicates.notNull();
        assertNull(predicate.apply(null));
        assertTrue(predicate.apply("foo"));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(new Object()));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> p = Predicates.alwaysTrue();
        Predicate<Boolean> notP = Predicates.not(p);
        assertFalse(notP.apply(true));
        assertTrue(notP.apply(false));

        Predicate<Integer> p2 = Predicates.isNull();
        Predicate<Integer> notP2 = Predicates.not(p2);
        assertFalse(notP2.apply(null));
        assertTrue(notP2.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> p1 = Predicates.isNull();
        Predicate<Integer> p2 = Predicates.instanceOf(Integer.class);
        Predicate<Integer> and = Predicates.and(p1, p2);
        assertFalse(and.apply(1));
        assertFalse(and.apply(null));
        assertFalse(and.apply("foo"));

        Predicate<String> p3 = Predicates.containsPattern(Pattern.compile("foo"));
        Predicate<String> p4 = Predicates.notNull();
        Predicate<String> and2 = Predicates.and(p3, p4);
        assertTrue(and2.apply("foo"));
        assertFalse(and2.apply("bar"));
        assertFalse(and2.apply(null));
    }

    @Test
    public void testOr() {
        Predicate<Integer> p1 = Predicates.isNull();
        Predicate<Integer> p2 = Predicates.instanceOf(Integer.class);
        Predicate<Integer> or = Predicates.or(p1, p2);
        assertTrue(or.apply(null));
        assertTrue(or.apply(1));
        assertFalse(or.apply("foo"));

        Predicate<String> p3 = Predicates.containsPattern(Pattern.compile("foo"));
        Predicate<String> p4 = Predicates.notNull();
        Predicate<String> or2 = Predicates.or(p3, p4);
        assertTrue(or2.apply("foo"));
        assertFalse(or2.apply(null));
        assertTrue(or2.apply("bar"));
    }

    @Test
    public void testEqualTo() {
        Predicate<Object> p1 = Predicates.equalTo(null);
        Predicate<Object> p2 = Predicates.equalTo("foo");
        Predicate<Object> p3 = Predicates.equalTo(1);

        Predicate<Object> p4 = Predicates.notNull();
        Predicate<Object> p5 = Predicates.instanceOf(String.class);

        Predicate<Object> and = Predicates.and(p1, p5);
        assertFalse(and.apply("foo"));
        assertFalse(and.apply(1));
        assertFalse(and.apply(null));

        Predicate<Object> or = Predicates.or(p2, p3);
        assertFalse(or.apply(null));
        assertFalse(or.apply("foo"));
        assertTrue(or.apply(1));

        Predicate<Object> and2 = Predicates.and(p4, p2);
        assertFalse(and2.apply(null));
        assertTrue(and2.apply("foo"));
        assertFalse(and2.apply(1));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> p1 = Predicates.instanceOf(String.class);
        Predicate<Object> p2 = Predicates.instanceOf(Integer.class);

        Predicate<Object> and = Predicates.and(p1, p2);
        assertFalse(and.apply("foo"));
        assertFalse(and.apply(1));
        assertFalse(and.apply(new Object()));

        Predicate<Object> or = Predicates.or(p1, p2);
        assertFalse(or.apply(null));
        assertFalse(or.apply("foo"));
        assertTrue(or.apply(1));
        assertTrue(or.apply(new String()));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> p1 = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> p2 = Predicates.assignableFrom(Number.class);

        Predicate<Class<?>> and = Predicates.and(p1, p2);
        assertFalse(and.apply(Integer.class));
        assertFalse(and.apply(String.class));
        assertFalse(and.apply(Double.class));

        Predicate<Class<?>> or = Predicates.or(p1, p2);
        assertTrue(or.apply(String.class));
        assertTrue(or.apply(Integer.class));
        assertTrue(or.apply(Number.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> p = Predicates.in(collection);

        Predicate<Integer> and = Predicates.and(p, Predicates.notNull());
        assertFalse(and.apply(null));
        assertTrue(and.apply(1));
        assertFalse(and.apply(4));

        Predicate<Integer> or = Predicates.or(p, Predicates.isNull());
        assertTrue(or.apply(null));
        assertTrue(or.apply(1));
        assertTrue(or.apply(4));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> p1 = Predicates.isNull();
        Function<Integer, Boolean> f = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(@Nullable Integer input) {
                return input == null;
            }
        };

        Predicate<Integer> p2 = Predicates.alwaysTrue();
        Predicate<Integer> p3 = Predicates.compose(p1, f);
        Predicate<Integer> p4 = Predicates.compose(p2, f);

        assertTrue(p3.apply(null));
        assertFalse(p3.apply(1));
        assertFalse(p4.apply(null));
        assertTrue(p4.apply(1));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> p1 = Predicates.containsPattern(Pattern.compile("foo"));
        Predicate<String> p2 = Predicates.containsPattern(Pattern.compile("bar"));

        Predicate<String> and = Predicates.and(p1, p2);
        assertFalse(and.apply("foo"));
        assertFalse(and.apply("bar"));
        assertFalse(and.apply("foobar"));

        Predicate<String> or = Predicates.or(p1, p2);
        assertTrue(or.apply("foo"));
        assertTrue(or.apply("bar"));
        assertTrue(or.apply("foobar"));
    }
}