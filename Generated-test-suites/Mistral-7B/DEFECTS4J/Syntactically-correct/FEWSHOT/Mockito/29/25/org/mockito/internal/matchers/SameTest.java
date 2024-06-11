package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.describeTo;

import org.junit.Test;

public class SameTest {

    @Test
    public void testSameString() {
        Object wanted = "foo";
        Same sameString = new Same(wanted);

        String actual = "foo";
        describeTo(System.out).appendText("matches '");
        describeTo(System.out).appendText(wanted.toString());
        describeTo(System.out).appendText("' and '");
        describeTo(System.out).appendText(actual.toString());
        describeTo(System.out).appendText("'? ");
        assertTrue(sameString.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        char wanted = 'a';
        Same sameCharacter = new Same(wanted);

        char actual = 'a';
        describeTo(System.out).appendText("matches '");
        describeTo(System.out).appendText(Character.toString(wanted));
        describeTo(System.out).appendText("' and '");
        describeTo(System.out).appendText(Character.toString(actual));
        describeTo(System.out).appendText("'? ");
        assertTrue(sameCharacter.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        Object wanted = "foo";
        Same sameString = new Same(wanted);

        String actual = "bar";
        describeTo(System.out).appendText("does not match '");
        describeTo(System.out).appendText(wanted.toString());
        describeTo(System.out).appendText("' and '");
        describeTo(System.out).appendText(actual.toString());
        describeTo(System.out).appendText("' ");
        assertFalse(sameString.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        char wanted = 'a';
        Same sameCharacter = new Same(wanted);

        char actual = 'b';
        describeTo(System.out).appendText("does not match '");
        describeTo(System.out).appendText(Character.toString(wanted));
        describeTo(System.out).appendText("' and '");
        describeTo(System.out).appendText(Character.toString(actual));
        describeTo(System.out).appendText("' ");
        assertFalse(sameCharacter.matches(actual));
    }

    @Test
    public void testNullValue() {
        Object wanted = null;
        Same sameNull = new Same(wanted);

        Object actual = null;
        describeTo(System.out).appendText("matches null and null? ");
        assertTrue(sameNull.matches(actual));
    }

    @Test
    public void testDifferentNullAndObject() {
        Object wanted = "foo";
        Same sameString = new Same(wanted);

        Object actual = null;
        describeTo(System.out).appendText("does not match '");
        describeTo(System.out).appendText(wanted.toString());
        describeTo(System.out).appendText("' and null ");
        assertFalse(sameString.matches(actual));
    }

    @Test
    public void testDifferentObjectAndNull() {
        Object wanted = null;
        Same sameNull = new Same(wanted);

        Object actual = "foo";
        describeTo(System.out).appendText("does not match null and '");
        describeTo(System.out).appendText(actual.toString());
        describeTo(System.out).appendText("' ");
        assertFalse(sameNull.matches(actual));
    }
}