package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String s1 = "The quick brown fox jumps over the lazy dog.";
        String s2 = "The quick brown fox jumps over the lazy dog. Lorem ipsum dolor sit amet.";

        RelativeString rs = StringIncrementor.diff(s1, s2);
        assertNotNull(rs);
        assertEquals(2, rs.linksCount());

        StringLink sl1 = rs.getLink(0);
        assertEquals(31, sl1.getPosOrig());
        assertEquals(32, sl1.getPosNew());
        assertEquals(3, sl1.getLen());

        StringLink sl2 = rs.getLink(1);
        assertEquals(s2.length(), sl2.getPosNew());
        assertEquals(" Lorem ipsum dolor sit amet.", sl2.getAbsolute());
    }

    @Test
    public void testDiffEmptyStrings() {
        String s1 = "";
        String s2 = "";

        RelativeString rs = StringIncrementor.diff(s1, s2);
        assertNotNull(rs);
        assertEquals(0, rs.linksCount());
    }

    @Test
    public void testDiffIdenticalStrings() {
        String s1 = "The quick brown fox jumps over the lazy dog.";
        String s2 = "The quick brown fox jumps over the lazy dog.";

        RelativeString rs = StringIncrementor.diff(s1, s2);
        assertNotNull(rs);
        assertEquals(0, rs.linksCount());
    }

    @Test
    public void testDiffEmptyStringWithNonEmptyString() {
        String s1 = "";
        String s2 = "The quick brown fox jumps over the lazy dog.";

        RelativeString rs = StringIncrementor.diff(s1, s2);
        assertNotNull(rs);
        assertEquals(1, rs.linksCount());

        StringLink sl = rs.getLink(0);
        assertEquals(s2.length(), sl.getPosNew());
        assertEquals(s2, sl.getAbsolute());
    }

    @Test
    public void testDiffWithVerboseOutput() {
        String s1 = "The quick brown fox jumps over the lazy dog.";
        String s2 = "The quick brown fox jumps over the lazy dog. Lorem ipsum dolor sit amet.";

        StringIncrementor.diff(s1, s2, true);

        RelativeString rs = StringIncrementor.diff(s1, s2);
        assertNotNull(rs);
        assertEquals(2, rs.linksCount());

        StringLink sl1 = rs.getLink(0);
        assertEquals(31, sl1.getPosOrig());
        assertEquals(32, sl1.getPosNew());
        assertEquals(3, sl1.getLen());

        StringLink sl2 = rs.getLink(1);
        assertEquals(s2.length(), sl2.getPosNew());
        assertEquals(" Lorem ipsum dolor sit amet.", sl2.getAbsolute());
    }

    @Test
    public void testPatch() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(14, 14, 6, "jumps "));
        r.addLink(new StringLink(35, 35, 11, "Lorem ipsum dolor sit amet."));

        String patched = StringIncrementor.patch(a, r);
        assertEquals("The quick brown fox jumps Lorem ipsum dolor sit amet.", patched);
    }

    @Test
    public void testPatchEmptyStrings() {
        String a = "";
        RelativeString r = new RelativeString();

        String patched = StringIncrementor.patch(a, r);
        assertEquals("", patched);
    }

    @Test
    public void testPatchIdenticalStrings() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString r = new RelativeString();

        String patched = StringIncrementor.patch(a, r);
        assertEquals(a, patched);
    }

    @Test
    public void testPatchWithVerboseOutput() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(14, 14, 6, "jumps "));
        r.addLink(new StringLink(35, 35, 11, "Lorem ipsum dolor sit amet."));

        StringBuilder output = new StringBuilder();
        PrintStream originalOut = System.out;
        try (PrintStream newOut = new PrintStream(new ByteArrayOutputStream())) {
            System.setOut(newOut);
            String patched = StringIncrementor.patch(a, r, true);
            System.setOut(originalOut);

            assertEquals("The quick brown fox jumps Lorem ipsum dolor sit amet.", patched);
            assertEquals(output.toString(), "Adding 6 absolute Bytes from pos#0\nAdding Link: [14, 20, 6]\nAdding 11 absolute Bytes from pos#21\nAdding Link: [35, 35, 11]\n");
        }
    }
}