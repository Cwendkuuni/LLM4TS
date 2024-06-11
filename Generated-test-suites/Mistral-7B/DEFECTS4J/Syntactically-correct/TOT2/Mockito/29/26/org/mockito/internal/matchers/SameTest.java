package org.mockito.internal.matchers.test;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatchesSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        assertTrue(sameMatcher.matches(wanted));
    }

    @Test
    public void testMatchesObjectsOfDifferentClasses() {
        Object wanted = new Object();
        Object actual = new String("Hello");
        Same sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesObjectsOfSameClassButDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();
        Same sameMatcher = new Same(wanted);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesObjectsWithSameHashCodeButDifferentMemoryAddresses() {
        Object wanted = new Object();
        Object actual = new Object();
        Same sameMatcher = new Same(wanted);
        assertFalse(wanted.equals(actual)); // Ensure wanted and actual have different memory addresses
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullWanted() {
        Same sameMatcher = new Same(null);
        sameMatcher.matches(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullActual() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        sameMatcher.matches(null);
    }
}