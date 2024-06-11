package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testSameObject() {
        Same sameObject = new Same(new Object());

        Object testObject = new Object();

        assertTrue(sameObject.matches(testObject));
    }

    @Test
    public void testSameString() {
        Same sameString = new Same("hello");

        String testString = "hello";

        assertTrue(sameString.matches(testString));
    }

    @Test
    public void testSameCharacter() {
        Same sameCharacter = new Same('a');

        Character testCharacter = 'a';

        assertTrue(sameCharacter.matches(testCharacter));
    }

    @Test
    public void testDifferentTypes() {
        Same sameObject = new Same(new Object());

        Integer testInteger = 1;

        assertFalse(sameObject.matches(testInteger));
    }

    @Test
    public void testNullObject() {
        Same sameObject = new Same(null);

        assertNull(sameObject.matches(null));
    }

    @Test
    public void testDescribeTo() {
        Same sameObject = new Same(new Object());

        Description description = new Description();
        sameObject.describeTo(description);

        String expectedDescription = "same(object)";

        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToWithString() {
        Same sameString = new Same("hello");

        Description description = new Description();
        sameString.describeTo(description);

        String expectedDescription = "same(\"hello\")";

        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToWithCharacter() {
        Same sameCharacter = new Same('a');

        Description description = new Description();
        sameCharacter.describeTo(description);

        String expectedDescription = "same('a')";

        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }
}