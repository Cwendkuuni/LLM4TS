package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcdefghijklm";
        String b = "abcdexyzlmnopqrst";
        RelativeString result = StringIncrementor.diff(a, b);

        String expected = "xyzlmnopqrst";
        assertEquals(expected, result.getAbsolute());
        assertEquals(6, result.linksCount());

        StringLink[] links = new StringLink[6];
        links[0] = new StringLink(4, 6, 2, 'xy');
        links[1] = new StringLink(7, 7, 1, 'y');
        links[2] = new StringLink(8, 8, 1, 'z');
        links[3] = new StringLink(10, 10, 1, 'l');
        links[4] = new StringLink(11, 11, 1, 'm');
        links[5] = new StringLink(12, 12, 1, 'n');

        for (int i = 0; i < result.linksCount(); ++i) {
            assertEquals(links[i].getPosNew(), result.getLink(i).getPosNew());
            assertEquals(links[i].getPosOrig(), result.getLink(i).getPosOrig());
            assertEquals(links[i].getLen(), result.getLink(i).getLen());
            assertEquals(links[i].getText(), result.getLink(i).getText());
        }

        // Edge cases and exception handling
        String aNull = null;
        String bNull = null;
        try {
            StringIncrementor.diff(aNull, b);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            StringIncrementor.diff(a, bNull);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        String aEmpty = "";
        String bEmpty = "";
        RelativeString resultEmpty = StringIncrementor.diff(aEmpty, bEmpty);
        assertEquals(0, resultEmpty.linksCount());
        assertEquals("", resultEmpty.getAbsolute());

        String aShort = "ab";
        String bLong = "abcdefghijklm";
        RelativeString resultShortLong = StringIncrementor.diff(aShort, bLong);
        assertEquals(1, resultShortLong.linksCount());
        assertEquals(5, resultShortLong.getLink(0).getLen());
        assertEquals("cdefghijklm", resultShortLong.getAbsolute());

        String aLong = "abcdefghijklm";
        String bShort = "ab";
        RelativeString resultLongShort = StringIncrementor.diff(aLong, bShort);
        assertEquals(1, resultLongShort.linksCount());
        assertEquals(10, resultLongShort.getLink(0).getLen());
        assertEquals("ab", resultLongShort.getAbsolute());
    }

    @Test
    public void testPatch() {
        String a = "abcdefghijklm";
        String b = "xyzlmnopqrst";
        RelativeString relDiff = StringIncrementor.diff(a, b);
        String patched = StringIncrementor.patch(a, relDiff);

        assertEquals(b, patched);

        // Edge cases and exception handling
        String aNull = null;
        RelativeString relNull = new RelativeString();
        try {
            StringIncrementor.patch(aNull, relNull);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        String aEmpty = "";
        RelativeString relEmpty = new RelativeString();
        String patchedEmpty = StringIncrementor.patch(aEmpty, relEmpty);
        assertEquals("", patchedEmpty);

        String aShort = "ab";
        RelativeString relShort = new RelativeString();
        relShort.addLink(new StringLink(4, 4, 2, "cd"));
        String patchedShort = StringIncrementor.patch(aShort, relShort);
        assertEquals("cdab", patchedShort);

        String aLong = "abcdefghijklm";
        RelativeString relLong = new RelativeString();
        relLong.addLink(new StringLink(4, 4, 2, "cd"));
        relLong.addLink(new StringLink(7, 7, 1, "e"));
        relLong.addLink(new StringLink(10, 10, 1, "m"));
        String patchedLong = StringIncrementor.patch(aLong, relLong);
        assertEquals("abcdefghijkm", patchedLong);
    }
}