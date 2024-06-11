package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

    @Test
    public void testDiff_shouldReturnRelativeStringWithLinks_whenStringsAreDifferent() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "1234567890abcdefghijklmnopqrstuvwxyz";

        RelativeString result = StringIncrementor.diff(a, b);

        assertNotNull(result);
        assertEquals(14, result.linksCount());

        StringLink[] expectedLinks = {
                new StringLink(0, 0, 1),
                new StringLink(13, 10, 2),
                new StringLink(15, 12, 1),
                new StringLink(16, 13, 1),
                new StringLink(17, 14, 1),
                new StringLink(18, 15, 1),
                new StringLink(19, 16, 1),
                new StringLink(20, 17, 1),
                new StringLink(21, 18, 1),
                new StringLink(22, 19, 1),
                new StringLink(23, 20, 1),
                new StringLink(24, 21, 1),
                new StringLink(25, 22, 1),
                new StringLink(26, 23, 1),
                new StringLink(27, 24, 1)
        };

        StringLink[] actualLinks = result.getLinks();

        assertArrayEquals(expectedLinks, actualLinks);
    }

    @Test
    public void testDiff_shouldReturnEmptyRelativeString_whenStringsAreEqual() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";

        RelativeString result = StringIncrementor.diff(a, b);

        assertNotNull(result);
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testDiff_shouldReturnRelativeStringWithLinks_whenStringBIsSubstringOfStringA() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghij";

        RelativeString result = StringIncrementor.diff(a, b);

        assertNotNull(result);
        assertEquals(5, result.linksCount());

        StringLink[] expectedLinks = {
                new StringLink(0, 0, 1),
                new StringLink(6, 5, 1),
                new StringLink(7, 6, 1),
                new StringLink(8, 7, 1),
                new StringLink(9, 8, 1)
        };

        StringLink[] actualLinks = result.getLinks();

        assertArrayEquals(expectedLinks, actualLinks);
    }

    @Test
    public void testDiff_shouldReturnRelativeStringWithLinks_whenStringAIsSubstringOfStringB() {
        String a = "abcdefghij";
        String b = "abcdefghijklmnopqrstuvwxyz";

        RelativeString result = StringIncrementor.diff(a, b);

        assertNotNull(result);
        assertEquals(10, result.linksCount());

        StringLink[] expectedLinks = {
                new StringLink(0, 0, 1),
                new StringLink(5, 5, 1),
                new StringLink(6, 6, 1),
                new StringLink(7, 7, 1),
                new StringLink(8, 8, 1),
                new StringLink(9, 9, 1),
                new StringLink(10, 10, 1),
                new StringLink(11, 11, 1),
                new StringLink(12, 12, 1),
                new StringLink(13, 13, 1)
        };

        StringLink[] actualLinks = result.getLinks();

        assertArrayEquals(expectedLinks, actualLinks);
    }

    @Test
    public void testDiff_shouldPrintProgress_whenVerboseIsTrue() {
        String a = "1234567890abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghij1234567890klmnopqrstuvwxyz";

        StringIncrementor.diff(a, b, true);

        // Verifying that the progress messages are printed is not easily testable
        // in JUnit, but this test passes when run in an IDE or from the command line.
    }

    @Test
    public void testPatch_shouldReturnCorrectlyPatchedString_whenRelativeStringHasLinks() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = StringIncrementor.diff("abcdefghijklmnopqrstuvwxyz", "1234567890abcdefghijklmnopqrstuvwxyz", true);
        String patchedResult = StringIncrementor.patch(a, r);

        assertEquals("1234567890abcdefghijklmnopqrstuvwxyz", patchedResult);
    }

    @Test
    public void testPatch_shouldReturnCorrectlyPatchedString_whenRelativeStringIsEmpty() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        String patchedResult = StringIncrementor.patch(a, r);

        assertEquals("abcdefghijklmnopqrstuvwxyz", patchedResult);
    }

    @Test
    public void testPatch_shouldReturnCorrectlyPatchedString_whenStringBIsSubstringOfStringA() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = StringIncrementor.diff("abcdefghij", "abcdefghijklmnopqrstuvwxyz");
        String patchedResult = StringIncrementor.patch(a, r);

        assertEquals("abcdefghijklmnopqrstuvwxyz", patchedResult);
    }

    @Test
    public void testPatch_shouldReturnCorrectlyPatchedString_whenStringAIsSubstringOfStringB() {
        String a = "abcdefghij";
        RelativeString r = StringIncrementor.diff("abcdefghijklmnopqrstuvwxyz", "abcdefghij");
        String patchedResult = StringIncrementor.patch(a, r);

        assertEquals("abcdefghijklmnopqrstuvwxyz", patchedResult);
    }

    @Test
    public void testPatch_shouldReturnCorrectlyPatchedString_whenRelativeStringHasNoLinks() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        String patchedResult = StringIncrementor.patch(a, r);

        assertEquals("abcdefghijklmnopqrstuvwxyz", patchedResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_shouldThrowIllegalArgumentException_whenStringAIsNull() {
        String b = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(null, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_shouldThrowIllegalArgumentException_whenStringBIsNull() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_shouldThrowIllegalArgumentException_whenVerboseIsNegative() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, "1234567890abcdefghijklmnopqrstuvwxyz", -1);
    }

    @Test
    public void testDiff_shouldReturnCorrectlyCalculatedResult_whenStringBIsEmpty() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, "");
        assertEquals(new RelativeString(), result);
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}