package com.google.common.base;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(new Object()));
        assertTrue(alwaysTrue.apply(new Integer(1)));
        assertTrue(alwaysTrue.apply(new String("Hello")));
    }
}