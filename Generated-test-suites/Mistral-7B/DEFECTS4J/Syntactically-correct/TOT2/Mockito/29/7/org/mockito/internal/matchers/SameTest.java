package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testSame() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentObjectTypes() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        String actual = "Hello World";
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesNull() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        sameMatcher.matches(null);
    }

    @Test
    public void testMatchesSameClassObject() {
        Object wanted = new String("Test");
        Same sameMatcher = new Same(wanted);
        String actual = new String("Test");
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesDifferentClassObjects() {
        Object wanted = new String("Test");
        Same sameMatcher = new Same(wanted);
        Integer actual = new Integer(1);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(Object)";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }
}