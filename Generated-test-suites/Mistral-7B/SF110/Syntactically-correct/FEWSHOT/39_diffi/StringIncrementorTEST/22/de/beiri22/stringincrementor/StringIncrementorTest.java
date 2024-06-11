package de.beiri22.stringincrementor;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffIdenticalStrings() {
        String a = "foo";
        String b = "foo";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffDifferentStringsShort() {
        String a = "foo";
        String b = "bar";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(-4, 0, 3, "foo"));
        expected.addLink(new StringLink(-1, 3, 1, "b"));
        expected.setAbsolute("b");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffDifferentStringsLong() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String b = "The quick brown fox jumps over the red lazy cat.";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(-1, 0, 6, "The quick brown fox jumps over "));
        expected.addLink(new StringLink(-1, 12, 3, "the "));
        expected.addLink(new StringLink(-1, 16, 1, "l"));
        expected.addLink(new StringLink(-1, 17, 3, "azy "));
        expected.addLink(new StringLink(1, 43, 3, "cat."));
        expected.addLink(new StringLink(1, 46, 1, "."));
        expected.setAbsolute("the red cat.");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffDifferentStringsVerbose() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String b = "The quick brown fox jumps over the red lazy cat.";
        boolean verbose = true;
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(-1, 0, 6, "The quick brown fox jumps over "));
        expected.addLink(new StringLink(-1, 12, 3, "the "));
        expected.addLink(new StringLink(-1, 16, 1, "l"));
        expected.addLink(new StringLink(-1, 17, 3, "azy "));
        expected.addLink(new StringLink(1, 43, 3, "cat."));
        expected.addLink(new StringLink(1, 46, 1, "."));
        expected.setAbsolute("the red cat.");
        RelativeString result = StringIncrementor.diff(a, b, verbose);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchEmptyString() {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchIdentity() {
        String a = "foo";
        RelativeString r = new RelativeString();
        String expected = "foo";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchOneLink() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(-1, 0, 6, "The quick brown fox jumps over "));
        String expected = "The quick brown fox jumps over the lazy dog.";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchMultipleLinks() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(-1, 0, 6, "The quick brown fox jumps over "));
        r.addLink(new StringLink(-1, 12, 3, "the "));
        r.addLink(new StringLink(-1, 16, 1, "l"));
        r.addLink(new StringLink(-1, 17, 3, "azy "));
        r.addLink(new StringLink(1, 43, 3, "cat."));
        r.addLink(new StringLink(1, 46, 1, "."));
        String expected = "The quick brown fox jumps over the red cat.";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }
}