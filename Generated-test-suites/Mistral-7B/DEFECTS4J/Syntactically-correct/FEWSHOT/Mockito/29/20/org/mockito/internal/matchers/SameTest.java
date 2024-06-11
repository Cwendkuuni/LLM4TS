package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.serialVersionUID;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class SameTest {

    @Test
    public void testSameMatchesSameInstance() {
        Same sameInstance = new Same(new Object());
        Object sameInstanceObject = new Object();

        assertTrue(sameInstance.matches(sameInstanceObject));
    }

    @Test
    public void testSameMatchesDifferentInstancesOfSameClass() {
        Same sameInstance = new Same(new Object());
        Object differentInstance = new Object();

        assertTrue(sameInstance.matches(differentInstance));
    }

    @Test
    public void testSameMatchesNullAndNull() {
        Same nullMatcher = new Same(null);

        assertTrue(nullMatcher.matches(null));
    }

    @Test
    public void testSameMatchesDifferentClasses() {
        Same integerMatcher = new Same(new Integer(1));
        Object string = "one";

        assertFalse(integerMatcher.matches(string));
    }

    @Test
    public void testSameDescribesCorrectly() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Description description = new Description();

        sameMatcher.describeTo(description);

        String expectedDescription = String.format("same(%s)", wanted.toString());
        String actualDescription = description.toString();

        assertTrue(expectedDescription.equalsIgnoreCase(actualDescription));
    }

    @Test
    public void testSameSerializable() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Same clonedMatcher = (Same) sameMatcher.clone();

        assertTrue(sameMatcher.matches(wanted));
        assertTrue(clonedMatcher.matches(wanted));
    }

    @Test
    public void testSameMatchesStringWithQuotes() {
        String wanted = "foo";
        Same stringMatcher = new Same(wanted);
        Description description = new Description();

        stringMatcher.describeTo(description);

        String expectedDescription = String.format("same(\"%s\")", wanted);
        String actualDescription = description.toString();

        assertTrue(expectedDescription.equalsIgnoreCase(actualDescription));

        assertTrue(stringMatcher.matches("foo"));
    }

    @Test
    public void testSameMatchesCharacterWithQuotes() {
        char wanted = 'a';
        Same characterMatcher = new Same(wanted);
        Description description = new Description();

        characterMatcher.describeTo(description);

        String expectedDescription = String.format("same('%s')", wanted);
        String actualDescription = description.toString();

        assertTrue(expectedDescription.equalsIgnoreCase(actualDescription));

        assertTrue(characterMatcher.matches('a'));
    }

    @Test
    public void testSameMatchesNullWithQuotes() {
        Same nullMatcher = new Same(null);
        Description description = new Description();

        nullMatcher.describeTo(description);

        String expectedDescription = "same(null)";
        String actualDescription = description.toString();

        assertTrue(expectedDescription.equalsIgnoreCase(actualDescription));

        assertNull(nullMatcher.matches(null));
    }
}