package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.describeTo;

import org.junit.Test;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Object actual = wanted;
        assertTrue(same.matches(actual));
        describeTo(new Description()).describe(same);
        System.out.println();

        actual = new Object();
        assertFalse(same.matches(actual));
    }

    @Test
    public void testSameString() {
        String wanted = "foo";
        Same same = new Same(wanted);

        String actual = "foo";
        assertTrue(same.matches(actual));
        describeTo(new Description()).describe(same);
        System.out.println();

        actual = "bar";
        assertFalse(same.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        char wanted = 'a';
        Same same = new Same(wanted);

        char actual = 'a';
        assertTrue(same.matches(actual));
        describeTo(new Description()).describe(same);
        System.out.println();

        actual = 'b';
        assertFalse(same.matches(actual));
    }

    @Test
    public void testNullableSameObject() {
        Object wanted = null;
        Same same = new Same(wanted);

        Object actual = null;
        assertTrue(same.matches(actual));
        describeTo(new Description()).describe(same);
        System.out.println();

        actual = new Object();
        assertFalse(same.matches(actual));
    }

    @Test
    public void testNullableSameString() {
        String wanted = null;
        Same same = new Same(wanted);

        String actual = null;
        assertTrue(same.matches(actual));
        describeTo(new Description()).describe(same);
        System.out.println();

        actual = "";
        assertFalse(same.matches(actual));
    }

    @Test
    public void testNullableSameCharacter() {
        char wanted = 0;
        Same same = new Same(wanted);

        char actual = 0;
        assertTrue(same.matches(actual));
        describeTo(new Description()).describe(same);
        System.out.println();

        actual = '\u0001';
        assertFalse(same.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidType() {
        int wanted = 1;
        Same same = new Same(wanted);

        // This will throw an IllegalArgumentException
    }
}