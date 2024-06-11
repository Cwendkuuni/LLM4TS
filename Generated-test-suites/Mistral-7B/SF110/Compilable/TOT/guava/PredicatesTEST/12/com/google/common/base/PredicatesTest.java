package com.google.common.base;
import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {

    

    @org.junit.Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(5));
        assertTrue(alwaysTrue.apply(null));
    }

    @org.junit.Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(5));
        assertFalse(alwaysFalse.apply(null));
    }

    @org.junit.Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(5));
        assertTrue(isNull.apply(null));
    }

    @org.junit.Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(5));
    }

    

}