
package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import java.io.*;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "abcdefg";
        String b = "xyzabcdefg";
        RelativeString result = diff(a, b);
        assertEquals("Link(3, 5, 3) @ 50.0%", result.toString());
    }

    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        try {
            RelativeString result = diff(a, b, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDiffNullStrings() {
        String a = null;
        String b = "abcdefg";
        try {
            RelativeString result = diff(a, b, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDiffSpecialCharacters() {
        String a = "abcdefg@#$%";
        String b = "xyzabcdefg";
        RelativeString result = diff(a, b, false);
        assertEquals("", result.toString());
    }

    @Test
    public void testDiffLargeStrings() {
        String a = new String(new char[100000]);
        String b = new String(new char[100000 + 1000]);
        RelativeString result = diff(a, b, false);
        // Check result as needed
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiffIndexNotFound() {
        String a = "abcdefg";
        String b = "xyzabcdefgx";
        try {
            RelativeString result = diff(a, b, false);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffSubstringLengthGreaterThanMainString() {
        String a = "abcdefg";
        String b = "xyzabcdefgx";
        try {
            RelativeString result = diff(a, b, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDiffConsoleOutput() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        String a = "abcdefg";
        String b = "xyzabcdefg";
        try {
            RelativeString result = diff(a, b, true);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    public void testPatch() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 5, 3));
        String result = patch(a, r);
        assertEquals("abcdefgabcdefg", result);
    }

    @Test
    public void testPatchEmptyStrings() {
        String a = "";
        RelativeString r = new RelativeString();
        try {
            String result = patch(a, r, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPatchNullStrings() {
        String a = null;
        RelativeString r = new RelativeString();
        try {
            String result = patch(a, r, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPatchSubstringLengthGreaterThanMainString() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 5, 6));
        try {
            String result = patch(a, r, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPatchConsoleOutput() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        try {
            String result = patch(a, r, true);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        } finally {
            System.setOut(System.out);
        }
    }
}
