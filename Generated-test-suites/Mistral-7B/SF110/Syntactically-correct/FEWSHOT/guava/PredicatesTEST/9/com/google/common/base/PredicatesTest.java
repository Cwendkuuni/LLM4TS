package com.google.common.base;

import static org.junit.Assert.*;
import java.util.*;
import java.util.regex.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply("hello"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply("hello"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply("hello"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply("hello"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicate = Predicates.alwaysTrue();
        Predicate<Boolean> notPredicate = Predicates.not(predicate);
        assertFalse(notPredicate.apply(true));
        assertTrue(notPredicate.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.alwaysTrue();

        Predicate<Integer> andPredicate = Predicates.and(predicate1, predicate2);
        assertFalse(andPredicate.apply(null));
        assertTrue(andPredicate.apply(1));
        assertFalse(andPredicate.apply(null));

        predicate1 = Predicates.isNull();
        predicate2 = Predicates.isNull();
        andPredicate = Predicates.and(predicate1, predicate2);
        assertFalse(andPredicate.apply(1));
        assertTrue(andPredicate.apply(null));

        predicate1 = Predicates.isNotNull();
        predicate2 = Predicates.isNotNull();
        andPredicate = Predicates.and(predicate1, predicate2);
        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.isNotNull();

        Predicate<Integer> orPredicate = Predicates.or(predicate1, predicate2);
        assertFalse(orPredicate.apply(null));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply("hello"));

        predicate1 = Predicates.isNull();
        predicate2 = predicate1;

        orPredicate = Predicates.or(predicate1, predicate2);
        assertFalse(orPredicate.apply(1));
        assertFalse(orPredicate.apply(null));

        predicate1 = Predicates.isNotNull();
        predicate2 = Predicates.isNotNull();
        orPredicate = Predicates.or(predicate1, predicate2);
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(1));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> predicate = Predicates.equalTo(1);
        assertTrue(predicate.apply(1));
        assertFalse(predicate.apply(2));
        assertFalse(predicate.apply(null));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfInteger = Predicates.instanceOf(Integer.class);
        assertTrue(instanceOfInteger.apply(1));
        assertFalse(instanceOfInteger.apply(2.0));
        assertFalse(instanceOfInteger.apply("hello"));
        assertFalse(instanceOfInteger.apply(null));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromNumber = Predicates.assignableFrom(Number.class);
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Double.class));
        assertFalse(assignableFromNumber.apply(String.class));
        assertFalse(assignableFromNumber.apply(Object.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> inList = Predicates.in(list);
        assertTrue(inList.apply(1));
        assertTrue(inList.apply(2));
        assertTrue(inList.apply(3));
        assertFalse(inList.apply(4));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> function = s -> Integer.parseInt(s);
        Predicate<String> predicate = Predicates.instanceOf("123".getClass());
        Predicate<Integer> composedPredicate = Predicates.compose(predicate, function);
        assertTrue(composedPredicate.apply("123"));
        assertFalse(composedPredicate.apply("456"));
        assertFalse(composedPredicate.apply("hello"));
        assertFalse(composedPredicate.apply(null));
    }

    @Test
    public void testContainsPattern() {
        Pattern pattern = Pattern.compile("\\d+");
        Predicate<String> containsPattern = Predicates.containsPattern(pattern);
        assertTrue(containsPattern.apply("123"));
        assertTrue(containsPattern.apply("hello123"));
        assertFalse(containsPattern.apply("hello"));
        assertFalse(containsPattern.apply(null));

        Predicate<String> containsPatternFromString = Predicates.containsPatternFromString("\\d+");
        assertTrue(containsPatternFromString.apply("123"));
        assertTrue(containsPatternFromString.apply("hello123"));
        assertFalse(containsPatternFromString.apply("hello"));
        assertFalse(containsPatternFromString.apply(null));
    }
}