package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiffWithEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiffWithIdenticalStrings() {
        String a = "foo";
        String b = "foo";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiffWithSingleCharacterDifference() {
        String a = "foo";
        String b = "bar";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(1, link.getLen());
        assertEquals(1, link.getPosOrig());
        assertEquals(1, link.getPosNew());
    }

    @Test
    public void testDiffWithMultipleChanges() {
        String a = "foo bar baz qux quux quuz";
        String b = "quux quuz quuz bar foo baz";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(5, result.linksCount());
        StringLink link1 = result.getLink(0);
        assertEquals(3, link1.getLen());
        assertEquals(11, link1.getPosOrig());
        assertEquals(8, link1.getPosNew());
        StringLink link2 = result.getLink(1);
        assertEquals(3, link2.getLen());
        assertEquals(18, link2.getPosOrig());
        assertEquals(15, link2.getPosNew());
        StringLink link3 = result.getLink(2);
        assertEquals(1, link3.getLen());
        assertEquals(21, link3.getPosOrig());
        assertEquals(21, link3.getPosNew());
        StringLink link4 = result.getLink(3);
        assertEquals(3, link4.getLen());
        assertEquals(1, link4.getPosOrig());
        assertEquals(4, link4.getPosNew());
        StringLink link5 = result.getLink(4);
        assertEquals(4, link5.getLen());
        assertEquals(16, link5.getPosOrig());
        assertEquals(12, link5.getPosNew());
    }

    @Test
    public void testDiffWithVerboseLogging() {
        String a = "foo bar baz qux quux quuz";
        String b = "quux quuz quuz bar foo baz";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(5, result.linksCount());
        for (int i = 0; i < result.linksCount(); ++i) {
            StringLink link = result.getLink(i);
            String linkStr = link.toString();
            System.out.println(linkStr);
        }
    }

    @Test
    public void testPatchWithEmptyStrings() {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatchWithIdenticalStrings() {
        String a = "foo";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("foo", result);
    }

    @Test
    public void testPatchWithSingleCharacterDifference() {
        String a = "foo";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 0, 1));
        r.setAbsolute("bar");
        String result = StringIncrementor.patch(a, r);
        assertEquals("bar", result);
    }

    @Test
    public void testPatchWithMultipleChanges() {
        String a = "foo bar baz qux quux quuz";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(11, 8, 3));
        r.addLink(new StringLink(18, 15, 3));
        r.addLink(new StringLink(21, 21, 1));
        r.addLink(new StringLink(1, 4, 3));
        r.addLink(new StringLink(16, 12, 4));
        String result = StringIncrementor.patch(a, r);
        assertEquals("quux quuz quuz bar foo baz", result);
    }

    @Test
    public void testPatchWithVerboseLogging() {
        String a = "foo bar baz qux quux quuz";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(11, 8, 3));
        r.addLink(new StringLink(18, 15, 3));
        r.addLink(new StringLink(21, 21, 1));
        r.addLink(new StringLink(1, 4, 3));
        r.addLink(new StringLink(16, 12, 4));
        String result = StringIncrementor.patch(a, r, true);
        assertEquals("quux quuz quuz bar foo baz", result);
    }
}