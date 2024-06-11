package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

    @Test
    public void testDiff_EmptyStrings() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiff_SameStrings() {
        String a = "foo";
        String b = "foo";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiff_SingleCharDifference() {
        String a = "foo";
        String b = "bar";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(3, link.getPosNew());
        assertEquals(0, link.getPosOrig());
        assertEquals(1, link.getLen());
    }

    @Test
    public void testDiff_MultipleDifferences() {
        String a = "foobar";
        String b = "barfoo";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(2, result.linksCount());
        StringLink firstLink = result.getLink(0);
        StringLink secondLink = result.getLink(1);
        assertEquals(1, firstLink.getPosNew());
        assertEquals(4, firstLink.getPosOrig());
        assertEquals(1, firstLink.getLen());
        assertEquals(6, secondLink.getPosNew());
        assertEquals(3, secondLink.getPosOrig());
        assertEquals(4, secondLink.getLen());
    }

    @Test
    public void testDiff_VerboseMode() {
        String a = "foobar";
        String b = "barfoo";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(2, result.linksCount());
        StringLink firstLink = result.getLink(0);
        StringLink secondLink = result.getLink(1);
        assertEquals(1, firstLink.getPosNew());
        assertEquals(4, firstLink.getPosOrig());
        assertEquals(1, firstLink.getLen());
        assertEquals(6, secondLink.getPosNew());
        assertEquals(3, secondLink.getPosOrig());
        assertEquals(4, secondLink.getLen());
        assertEquals("Link 1: @ 25%\nLink 1: Adding 3 absolute Bytes from pos#0\nLink 1: Adding Link: [posOrig=4, posNew=7, len=3]\nLink 2: @ 75%\nLink 2: Adding 4 absolute Bytes from pos#6", System.console().readLine());
    }

    @Test
    public void testPatch_EmptyPatches() {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatch_NoPatches() {
        String a = "foo";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("foo", result);
    }

    @Test
    public void testPatch_SingleCharPatch() {
        String a = "foobar";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("barfoo", result);
    }

    @Test
    public void testPatch_MultiplePatches() {
        String a = "foobar";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 1));
        r.addLink(new StringLink(6, 6, 4));
        String result = StringIncrementor.patch(a, r);
        assertEquals("barfoo", result);
    }

}