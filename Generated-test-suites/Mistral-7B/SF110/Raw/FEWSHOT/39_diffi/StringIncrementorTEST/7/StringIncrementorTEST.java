package de.beiri22.stringincrementor.test;

import de.beiri22.stringincrementor.*;
import org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertTrue(result.linksCount() == 0);
    }

    @Test
    public void testDiffIdenticalStrings() {
        String a = "foo";
        String b = "foo";
        RelativeString result = StringIncrementor.diff(a, b);
        assertTrue(result.linksCount() == 0);
    }

    @Test
    public void testDiffSingleCharacterDifference() {
        String a = "foo";
        String b = "bar";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(4, link.getPosNew());
        assertEquals(3, link.getPosOrig());
        assertEquals(1, link.getLen());
    }

    @Test
    public void testDiffMultipleCharacterDifferences() {
        String a = "foo";
        String b = "barbaz";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(3, result.linksCount());
        StringLink link1 = result.getLink(0);
        assertEquals(4, link1.getPosNew());
        assertEquals(3, link1.getPosOrig());
        assertEquals(1, link1.getLen());
        StringLink link2 = result.getLink(1);
        assertEquals(7, link2.getPosNew());
        assertEquals(3, link2.getPosOrig());
        assertEquals(4, link2.getLen());
        StringLink link3 = result.getLink(2);
        assertEquals(11, link3.getPosNew());
        assertEquals(7, link3.getPosOrig());
        assertEquals(4, link3.getLen());
    }

    @Test
    public void testDiffWithVerboseFlag() {
        String a = "foo";
        String b = "barbaz";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(3, result.linksCount());
        StringLink link1 = result.getLink(0);
        assertEquals(4, link1.getPosNew());
        assertEquals(3, link1.getPosOrig());
        assertEquals(1, link1.getLen());
        StringLink link2 = result.getLink(1);
        assertEquals(7, link2.getPosNew());
        assertEquals(3, link2.getPosOrig());
        assertEquals(4, link2.getLen());
        StringLink link3 = result.getLink(2);
        assertEquals(11, link3.getPosNew());
        assertEquals(7, link3.getPosOrig());
        assertEquals(4, link3.getLen());
        String absolute = result.getAbsolute();
        System.out.println("Absolute difference: " + absolute);
    }

    @Test
    public void testPatchEmptyString() {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatchSingleCharacterDifference() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 4, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("bar", result);
    }

    @Test
    public void testPatchMultipleCharacterDifferences() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 4, 1));
        r.addLink(new StringLink(7, 3, 4));
        r.addLink(new StringLink(11, 7, 4));
        String result = StringIncrementor.patch(a, r);
        assertEquals("barbaz", result);
    }

    @Test
    public void testPatchWithVerboseFlag() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 4, 1));
        r.addLink(new StringLink(7, 3, 4));
        r.addLink(new StringLink(11, 7, 4));
        String result = StringIncrementor.patch(a, r, true);
        assertEquals("barbaz", result);
    }
}