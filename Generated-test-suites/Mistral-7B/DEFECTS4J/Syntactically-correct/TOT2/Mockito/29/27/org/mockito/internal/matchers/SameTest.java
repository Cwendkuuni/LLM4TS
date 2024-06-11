package org.example.junit.tests;

import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatchesSameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Same sameMatcher = new Same(obj1);
        assertTrue(sameMatcher.matches(obj2));
    }

    @Test
    public void testMatchesNullWanted() {
        Same sameMatcher = new Same(null);
        assertFalse(sameMatcher.matches(new Object()));
    }

    @Test
    public void testMatchesDifferentClasses() {
        Object stringObj = "test";
        Object intObj = 123;
        Same sameMatcher = new Same(stringObj);
        assertFalse(sameMatcher.matches(intObj));
    }

    @Test
    public void testMatchesPrimitiveTypes() {
        int wanted = 123;
        int actual = 123;
        Same sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(actual));
    }

}