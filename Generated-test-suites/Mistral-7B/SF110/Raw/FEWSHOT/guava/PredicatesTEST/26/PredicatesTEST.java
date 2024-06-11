package com.google.common.base;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

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
        assertNull(isNull.apply(null));
        assertNull(isNull.apply(new Object()));
        assertFalse(isNull.apply(new String("foo")));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNotNull(notNull.apply(null));
        assertNotNull(notNull.apply(new Object()));
        assertFalse(notNull.apply(new String("foo")));
        assertTrue(notNull.apply(new StringBuilder()));
    }

    @Test
    public void testNot() {
        Predicate<Object> predicate = Predicates.isNull();
        Predicate<Object> notPredicate = Predicates.not(predicate);
        assertFalse(notPredicate.apply(null));
        assertFalse(notPredicate.apply(new Object()));
        assertTrue(notPredicate.apply(new String("foo")));
    }

    @Test
    public void testAnd() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);
        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(new Object()));
        assertFalse(andPredicate.apply(new String("foo")));
        assertFalse(andPredicate.apply(new StringBuilder()));
    }

    @Test
    public void testAndWithMultiplePredicates() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.instanceOf(String.class);
        Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);
        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(new Object()));
        assertFalse(andPredicate.apply(new StringBuilder()));
        assertTrue(andPredicate.apply(new String("foo")));
    }

    @Test
    public void testOr() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);
        assertFalse(orPredicate.apply(null));
        assertFalse(orPredicate.apply(new Object()));
        assertTrue(orPredicate.apply(new String("foo")));
        assertTrue(orPredicate.apply(new StringBuilder()));
    }

    @Test
    public void testOrWithMultiplePredicates() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.instanceOf(String.class);
        Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);
        assertFalse(orPredicate.apply(null));
        assertFalse(orPredicate.apply(new Object()));
        assertFalse(orPredicate.apply(new String("foo")));
        assertTrue(orPredicate.apply(new StringBuilder()));
    }

    @Test
    public void testEqualTo() {
        Object target = new Object();
        Predicate<Object> equalToPredicate = Predicates.equalTo(target);
        assertTrue(equalToPredicate.apply(target));
        assertFalse(equalToPredicate.apply(new Object()));
    }

    @Test
    public void testInstanceOf() {
        Class<String> clazz = String.class;
        Predicate<Class<?>> instanceOfPredicate = Predicates.instanceOf(clazz);
        assertTrue(instanceOfPredicate.apply(clazz));
        assertFalse(instanceOfPredicate.apply(new Object()));
        assertFalse(instanceOfPredicate.apply(new StringBuilder()));
    }

    @Test
    public void testAssignableFrom() {
        Class<StringBuilder> assignableFromClazz = StringBuilder.class;
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(assignableFromClazz);
        assertTrue(assignableFromPredicate.apply(StringBuilder.class));
        assertTrue(assignableFromPredicate.apply(String.class));
        assertFalse(assignableFromPredicate.apply(Integer.class));
    }

    @Test
    public void testIn() {
        List<Object> collection = Arrays.asList(new Object(), new String("foo"), new StringBuilder());
        Predicate<Object> inPredicate = Predicates.in(collection);
        assertTrue(inPredicate.apply(new Object()));
        assertTrue(inPredicate.apply(new String("foo")));
        assertTrue(inPredicate.apply(new StringBuilder()));
        assertFalse(inPredicate.apply(null));
        assertFalse(inPredicate.apply(new Integer(1)));
    }

    @Test
    public void testCompose() {
        Function<Object, Boolean> function = obj -> obj instanceof String;
        Predicate<Object> predicate = Predicates.isNull();
        Predicate<Object> composedPredicate = Predicates.compose(predicate, function);
        assertFalse(composedPredicate.apply(null));
        assertFalse(composedPredicate.apply(new Object()));
        assertTrue(composedPredicate.apply(new String("foo")));
        assertFalse(composedPredicate.apply(new StringBuilder()));
    }

    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern("foo");
        assertTrue(containsPatternPredicate.apply("foo"));
        assertFalse(containsPatternPredicate.apply("bar"));
    }
}