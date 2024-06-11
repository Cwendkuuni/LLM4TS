package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String b = "The quick red fox jumps over the lazy dog.";

        RelativeString rs = StringIncrementor.diff(a, b);
        assertNotNull(rs);
        assertEquals(1, rs.linksCount());
        StringLink sl = rs.getLink(0);
        assertEquals(15, sl.getPosOrig());
        assertEquals(16, sl.getPosNew());
        assertEquals(2, sl.getLen());

        assertEquals("red", rs.getAbsolute());

        a = "The quick brown fox jumps over the lazy dog.";
        b = "The quick brown fox jumps over the quick green dog.";

        rs = StringIncrementor.diff(a, b, true);
        assertNotNull(rs);
        assertEquals(1, rs.linksCount());
        sl = rs.getLink(0);
        assertEquals(24, sl.getPosOrig());
        assertEquals(25, sl.getPosNew());
        assertEquals(1, sl.getLen());

        assertEquals("green", rs.getAbsolute());
        System.out.println("Passed testDiff().");
    }

    @Test
    public void testDiffWithVerboseOutput() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String b = "The quick red fox jumps over the lazy dog.";

        RelativeString rs = StringIncrementor.diff(a, b, true);
        assertNotNull(rs);
        assertEquals(1, rs.linksCount());
        StringLink sl = rs.getLink(0);
        assertEquals(15, sl.getPosOrig());
        assertEquals(16, sl.getPosNew());
        assertEquals(2, sl.getLen());

        assertEquals("red", rs.getAbsolute());

        System.out.println("Pos# 0: [15, 16]: Replace 2 bytes: 'brown' -> 'red'");
        System.out.println("Passed testDiffWithVerboseOutput().\n");
    }

    @Test
    public void testDiffWithLongString() {
        String a = "The quick brown fox jumps over the lazy dog." + new String(new char[10000]).replace("\0", "x");
        String b = "The quick brown fox jumps over the lazy dog." + new String(new char[10000]).replace("\0", "y");

        RelativeString rs = StringIncrementor.diff(a, b, true);
        assertNotNull(rs);
        assertEquals(0, rs.linksCount());
        System.out.println("Passed testDiffWithLongString().\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithNullStrings() {
        String a = null;
        String b = "The quick brown fox jumps over the lazy dog.";

        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithEmptyStrings() {
        String a = "";
        String b = "";

        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithShortStrings() {
        String a = "The";
        String b = "The quick brown fox jumps over the lazy dog.";

        StringIncrementor.diff(a, b);
    }

    @Test
    public void testPatch() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String b = "The quick red fox jumps over the lazy dog.";
        RelativeString rs = StringIncrementor.diff(a, b);

        String patched = StringIncrementor.patch(a, rs);
        assertEquals("The quick red brown fox jumps over the lazy dog.", patched);
        System.out.println("Passed testPatch().\n");
    }

    @Test
    public void testPatchWithVerboseOutput() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String b = "The quick red fox jumps over the lazy dog.";
        RelativeString rs = StringIncrementor.diff(a, b, true);

        String patched = StringIncrementor.patch(a, rs, true);
        assertEquals("The quick red brown fox jumps over the lazy dog.", patched);
        System.out.println("Passed testPatchWithVerboseOutput().\n");
    }

    @Test
    public void testPatchWithLongString() {
        String a = "The quick brown fox jumps over the lazy dog." + new String(new char[10000]).replace("\0", "x");
        String b = "The quick brown fox jumps over the quick green dog." + new String(new char[10000]).replace("\0", "y");
        RelativeString rs = StringIncrementor.diff(a, b, true);

        String patched = StringIncrementor.patch(a, rs, true);
        assertEquals(a, patched);
        System.out.println("Passed testPatchWithLongString().\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchWithNullStrings() {
        String a = null;
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 0));

        StringIncrementor.patch(a, rs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchWithEmptyStrings() {
        String a = "";
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 0));

        StringIncrementor.patch(a, rs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchWithShortStrings() {
        String a = "The";
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(0, 0, 0));

        StringIncrementor.patch(a, rs);
    }

    @Test
    public void testPatchWithRelativeString() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(15, 16, 2));

        String patched = StringIncrementor.patch(a, rs);
        assertEquals("The quick red brown fox jumps over the lazy dog.", patched);
        System.out.println("Passed testPatchWithRelativeString().\n");
    }

    @Test
    public void testPatchWithRelativeStringAndVerboseOutput() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(15, 16, 2));

        String patched = StringIncrementor.patch(a, rs, true);
        assertEquals("The quick red brown fox jumps over the lazy dog.", patched);
        System.out.println("Pos# 0: [15, 16]: Replace 2 bytes: 'brown' -> 'red'");
        System.out.println("Passed testPatchWithRelativeStringAndVerboseOutput().\n");
    }
}