package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String b = "The quick brown fox jumps over the lazy cat.";
        RelativeString result = StringIncrementor.diff(a, b);

        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(7, link.getPosOrig());
        assertEquals(12, link.getLen());
        assertEquals("cat", result.getAbsolute());

        a = "The quick brown fox jumps over the lazy dog.";
        b = "The quick brown fox jumps over the quick cat.";
        result = StringIncrementor.diff(a, b);

        assertEquals(2, result.linksCount());
        link = result.getLink(0);
        assertEquals(7, link.getPosOrig());
        assertEquals(6, link.getLen());
        assertEquals("quick ", result.getAbsolute().substring(0, link.getPosNew()));
        link = result.getLink(1);
        assertEquals(23, link.getPosOrig());
        assertEquals(4, link.getLen());
        assertEquals("cat", result.getAbsolute().substring(link.getPosNew()));

        a = "The quick brown fox jumps over the quick brown dog.";
        b = "The quick brown fox jumps over the quick brown cat.";
        result = StringIncrementor.diff(a, b);

        assertEquals(0, result.linksCount());

        a = "The quick brown fox jumps over the quick brown dog.";
        b = "The quick brown fox jumps over the quick red cat.";
        result = StringIncrementor.diff(a, b, true);

        assertEquals(1, result.linksCount());
        link = result.getLink(0);
        assertEquals(18, link.getPosOrig());
        assertEquals(3, link.getLen());
        assertEquals("red", result.getAbsolute().substring(link.getPosNew()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffNullStrings() {
        StringIncrementor.diff(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffNullStringB() {
        String a = "The quick brown fox jumps over the lazy dog.";
        StringIncrementor.diff(a, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiffEmptyStringA() {
        String b = "The quick brown fox jumps over the lazy dog.";
        StringIncrementor.diff("", b);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiffEmptyStringB() {
        String a = "The quick brown fox jumps over the lazy dog.";
        StringIncrementor.diff(a, "");
    }

    @Test
    public void testPatch() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString rs = StringIncrementor.diff("The quick brown fox jumps over the lazy dog.", "The quick brown fox jumps over the quick brown cat.");
        String patched = StringIncrementor.patch(a, rs);

        assertEquals("The quick brown fox jumps over the quick brown cat.", patched);

        a = "The quick brown fox jumps over the quick brown dog.";
        rs = StringIncrementor.diff("The quick brown fox jumps over the quick brown dog.", "The quick brown fox jumps over the quick red cat.");
        patched = StringIncrementor.patch(a, rs);

        assertEquals("The quick brown fox jumps over the quick red cat.", patched);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchNullString() {
        String a = "The quick brown fox jumps over the lazy dog.";
        RelativeString rs = new RelativeString();
        StringIncrementor.patch(a, rs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchNullRelativeString() {
        String a = "The quick brown fox jumps over the lazy dog.";
        String rs = "The quick brown fox jumps over the quick brown cat.";
        StringIncrementor.patch(a, rs);
    }
}