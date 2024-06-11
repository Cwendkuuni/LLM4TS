package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiffMethodWithNullStrings() {
        try {
            StringIncrementor.diff(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            StringIncrementor.diff(null, "");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            StringIncrementor.diff("", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDiffMethodWithEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffMethodWithIdenticalStrings() {
        String a = "Hello World";
        String b = "Hello World";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffMethodWithOneCharacterDifference() {
        String a = "Hello World";
        String b = "Hello Worl";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 5, 1));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffMethodWithMultipleDifferences() {
        String a = "Hello World";
        String b = "Hello WOrld!";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 5, 1));
        expected.addLink(new StringLink(9, 9, 1));
        expected.addLink(new StringLink(10, 10, 1));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffMethodWithVerboseMode() {
        String a = "Hello World";
        String b = "Hello WOrld!";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 5, 1));
        expected.addLink(new StringLink(9, 9, 1));
        expected.addLink(new StringLink(10, 10, 1));
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(expected, result);
        
        // Check if verbose output is printed
        String out = System.out.getAbsolutePath();
        String expectedOut = "Creating index...\nSearching links...\n@ 50.0%\nAdding Link: StringLink{posOrig=5, len=1, posNew=6}\n@ 66.666666666666664%\nAdding Link: StringLink{posOrig=9, len=1, posNew=10}\n@ 100.0%\n";
        try (StringWriter sw = new StringWriter(new StringBuilder()); PrintWriter pw = new PrintWriter(sw)) {
            System.setOut(pw);
            StringIncrementor.diff(a, b, true);
            String actualOut = sw.toString();
            System.setOut(new PrintStream(out));
            assertEquals(expectedOut, actualOut);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiffMethodWithTargetStringTooShort() {
        String a = "Hello World";
        String b = "Hello";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDiffMethodWithSourceStringIndexOutOfBounds() {
        String a = "Hello World";
        String b = "Hello WOrld!";
        StringIncrementor.diff(a, b, true);
        StringIncrementor.diff(a, b, true);
    }

    @Test
    public void testPatchMethodWithNullArguments() {
        try {
            StringIncrementor.patch(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            StringIncrementor.patch(null, "");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            StringIncrementor.patch("", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPatchMethodWithEmptyStrings() {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchMethodWithNoLinks() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        String expected = "Hello World";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchMethodWithOneLink() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        String expected = "Hello Worl";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchMethodWithMultipleLinks() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        r.addLink(new StringLink(9, 9, 1));
        String expected = "Hello WOrld!";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchMethodWithVerboseMode() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        r.addLink(new StringLink(9, 9, 1));
        r.addLink(new StringLink(10, 10, 1));
        String expected = "Hello WOrld!";
        String result = StringIncrementor.patch(a, r, true);
        assertEquals(expected, result);

        // Check if verbose output is printed
        String out = System.out.getAbsolutePath();
        String expectedOut = "Adding 6 absolute Bytes from pos#0\nAdding Link: StringLink{posOrig=5, len=1, posNew=6}\nAdding 1 absolute Byte from pos#6\nAdding Link: StringLink{posOrig=9, len=1, posNew=10}\nAdding 1 absolute Byte from pos#10\n";
        try (StringWriter sw = new StringWriter(new StringBuilder()); PrintWriter pw = new PrintWriter(sw)) {
            System.setOut(pw);
            StringIncrementor.patch(a, r, true);
            String actualOut = sw.toString();
            System.setOut(new PrintStream(out));
            assertEquals(expectedOut, actualOut);
        }
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testPatchMethodWithSourceStringIndexOutOfBounds() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        r.addLink(new StringLink(9, 9, 1));
        r.addLink(new StringLink(10, 10, 1));
        StringIncrementor.patch(a, r);
        StringIncrementor.patch(a, r);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testPatchMethodWithTargetStringIndexOutOfBounds() {
        String a = "Hello World";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        r.addLink(new StringLink(9, 9, 1));
        r.addLink(new StringLink(10, 10, 1));
        StringIncrementor.patch(a, r);
        StringIncrementor.patch(a, r);
    }

}