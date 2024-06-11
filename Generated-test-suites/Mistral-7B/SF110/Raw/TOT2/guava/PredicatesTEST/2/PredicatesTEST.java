package com.example.junit4.test.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.base.Predicates.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(alwaysTrue().apply(null));
        assertTrue(alwaysTrue().apply(1));
        assertTrue(alwaysTrue().apply("hello"));
    }

    @Test
    public void testIsNull() {
        assertFalse(isNull().apply(null));
        assertFalse(isNull().apply(1));
        assertFalse(isNull().apply("hello"));
        assertTrue(isNull().apply(null));
        assertTrue(isNull().apply(new Object() { }));
    }

    @Test
    public void testAnd() {
        assertTrue(and(alwaysTrue(), alwaysTrue()).apply(null));
        assertFalse(and(alwaysTrue(), alwaysFalse()).apply(null));
        assertFalse(and(alwaysFalse(), alwaysTrue()).apply(null));
        assertFalse(and(alwaysFalse(), alwaysFalse()).apply(null));
    }

    @Test
    public void testAndWithNullPredicates() {
        List<Predicate<?>> predicates = Arrays.asList(alwaysTrue(), null);
        assertFalse(and(predicates).apply(null));
    }

    @Test
    public void testAndWithEmptyPredicates() {
        List<Predicate<?>> predicates = Collections.emptyList();
        assertFalse(and(predicates).apply(null));
    }

    @Test
    public void testAndWithNullIterable() {
        assertFalse(and((Iterable<Predicate<?>>)null).apply(null));
    }

    // Test cases for other methods: or(), equalTo(), instanceOf(), assignableFrom(), in(), compose(), containsPattern(), containsPatternFromString()

}