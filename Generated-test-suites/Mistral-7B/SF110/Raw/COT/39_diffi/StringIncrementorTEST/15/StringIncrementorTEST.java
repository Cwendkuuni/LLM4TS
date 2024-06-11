package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff_sameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test
    public void testDiff_emptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test
    public void testDiff_identicalSubstring() {
        String a = "abababc";
        String b = "ababcab";
        RelativeString expected = new StringLink(3, 3, 2);
        expected.setAbsolute("bc");
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullStrings() {
        String a = null;
        String b = "hello";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullStringLink() {
        String a = "hello";
        String b = "hello";
        RelativeString actual = StringIncrementor.diff(a, null);
    }

    @Test
    public void testDiff_verbose_smallStrings() {
        String a = "abcdabcd";
        String b = "efghabcdabcd";
        StringIncrementor.diff(a, b, true);
    }

    @Test
    public void testDiff_verbose_largeStrings() {
        String a = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        String b = "1234567890123456789012345678901234567890123456789012345678901234567890";
        StringIncrementor.diff(a, b, true);
    }

    @Test
    public void testPatch_emptyString() {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }

    @Test
    public void testPatch_noLinks() {
        String a = "hello";
        RelativeString r = new RelativeString();
        String expected = "hello";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }

    @Test
    public void testPatch_singleLink() {
        String a = "abcdabcd";
        RelativeString r = new StringLink(3, 3, 2);
        r.setAbsolute("bc");
        String expected = "abcbcabcd";
        String actual = StringIncrementor.patch(a, r);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_nullString() {
        String a = "hello";
        RelativeString r = null;
        StringIncrementor.patch(a, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_nullPatchString() {
        String a = "hello";
        String patch = null;
        StringIncrementor.patch(a, patch);
    }
}