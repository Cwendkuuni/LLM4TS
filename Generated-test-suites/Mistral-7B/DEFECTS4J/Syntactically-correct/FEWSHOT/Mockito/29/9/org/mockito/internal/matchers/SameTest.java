package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.describeTo;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

    @Test
    public void testSameInstance() {
        Same sameInstance = new Same(new Object());

        Object actual = new Object();

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testStringSame() {
        Same sameString = new Same("mockito");

        String actualString = "mockito";

        assertTrue(sameString.matches(actualString));
    }

    @Test
    public void testCharSame() {
        Same sameChar = new Same('a');

        char actualChar = 'a';

        assertTrue(sameChar.matches(actualChar));
    }

    @Test
    public void testDescribeToString() {
        Same sameObject = new Same(new Object());

        String expectedDescription = "same(<org.mockito.internal.matchers.Same@7852ef62>[Ljava/lang/Object;@28c621f8)";
        String actualDescription = "";

        describeTo(new org.hamcrest.core.Description(), sameObject).toString();

        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testDescribeToStringWithString() {
        Same sameString = new Same("mockito");

        String expectedDescription = "same(mockito)";
        String actualDescription = "";

        describeTo(new org.hamcrest.core.Description(), sameString).toString();

        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testDescribeToStringWithChar() {
        Same sameChar = new Same('a');

        String expectedDescription = "same(a)";
        String actualDescription = "";

        describeTo(new org.hamcrest.core.Description(), sameChar).toString();

        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testNullArgument() {
        Same sameObject = new Same(new Object());

        assertNull(sameObject.matches(null));
    }
}