package org.mockito.internal.matchers.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.same;

import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testConstructor() {
        Object wanted = new Object();
        Same sameObject = new Same(wanted);
        assertTrue(sameObject != null);
    }

    @Test
    public void testMatches() {
        Object wanted = new Object();
        Same sameObject = new Same(wanted);
        Object actual = wanted;
        assertTrue(sameObject.matches(actual));
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullWanted() {
        Same sameObject = new Same(null);
        Object actual = new Object();
        assertFalse(sameObject.matches(actual));
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullActual() {
        Object wanted = new Object();
        Same sameObject = new Same(wanted);
        assertFalse(sameObject.matches(null));
    }

    @Test
    public void testDifferentTypes() {
        Object wanted = new Object();
        Same sameObject = new Same(wanted);
        String actual = "sample string";
        assertFalse(sameObject.matches(actual));
    }

    @Test
    public void testSameObjectDifferentReferences() {
        Object wanted = new Object();
        Object actual = new Object();
        Same sameObject = new Same(wanted);
        assertFalse(sameObject.matches(actual));

        wanted = actual;
        assertTrue(sameObject.matches(wanted));
    }

    @Test
    public void testDescribeTo() {
        Description description = new Description();
        Object wanted = new Object();
        Same sameObject = new Same(wanted);
        sameObject.describeTo(description);

        String expectedOutput = "same(\"[Ljava.lang.Object;@\")";
        String actualOutput = description.toString();

        assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    public void testMatchesSameArrays() {
        Object[] wanted = new Object[1];
        Object[] actual = new Object[1];
        Same sameObject = new Same(wanted);
        assertTrue(sameObject.matches(actual));
    }

    @Test
    public void testMatchesDifferentArrays() {
        Object[] wanted = new Object[1];
        Object[] actual = new Object[2];
        Same sameObject = new Same(wanted);
        assertFalse(sameObject.matches(actual));
    }

    @Test
    public void testMatchesEquals() {
        Object wanted = new Object();
        Object actual = new Object();
        Same sameObject = new Same(wanted);

        // Set both objects to have the same value
        actual.equals(wanted);

        assertTrue(sameObject.matches(actual));
    }
}