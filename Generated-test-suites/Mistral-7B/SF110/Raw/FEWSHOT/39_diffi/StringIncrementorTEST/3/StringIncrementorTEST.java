package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

    @Test
    public void testDiff_EmptyStrings() {
        final String a = "";
        final String b = "";
        final RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiff_IdenticalStrings() {
        final String a = "foo";
        final String b = "foo";
        final RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiff_OneCharacterDifference() {
        final String a = "foo";
        final String b = "bar";
        final RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals(1, result.linksCount());
        final StringLink link = result.getLink(0);
        assertEquals(3, link.getPosOrig());
        assertEquals(3, link.getPosNew());
        assertEquals(1, link.getLen());
    }

    @Test
    public void testDiff_MultipleDifferences() {
        final String a = "foo bar baz";
        final String b = "qux quux quuz";
        final RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals(3, result.linksCount());
        final StringLink link1 = result.getLink(0);
        assertEquals(3, link1.getPosOrig());
        assertEquals(3, link1.getPosNew());
        assertEquals(3, link1.getLen());

        final StringLink link2 = result.getLink(1);
        assertEquals(10, link2.getPosOrig());
        assertEquals(9, link2.getPosNew());
        assertEquals(1, link2.getLen());

        final StringLink link3 = result.getLink(2);
        assertEquals(13, link3.getPosOrig());
        assertEquals(13, link3.getPosNew());
        assertEquals(0, link3.getLen());
    }

    @Test
    public void testDiff_Verbose() {
        final String a = "foo bar baz";
        final String b = "qux quux quuz";
        final boolean verbose = true;
        final RelativeString result = StringIncrementor.diff(a, b, verbose);
        assertNotNull(result);
        assertEquals(3, result.linksCount());
        for (int i = 0; i < result.linksCount(); ++i) {
            final StringLink link = result.getLink(i);
            System.out.println("Link #" + i + ": " + link);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDiff_NullArgument() {
        final String a = null;
        final String b = "bar";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = NullPointerException.class)
    public void testDiff_NullArgumentB() {
        final String a = "foo";
        final String b = null;
        StringIncrementor.diff(a, b);
    }

    @Test
    public void testPatch_EmptyString() {
        final String a = "";
        final RelativeString r = new RelativeString();
        final String patch = StringIncrementor.patch(a, r);
        assertEquals("", patch);
    }

    @Test
    public void testPatch_NoDifferences() {
        final String a = "foo";
        final RelativeString r = new RelativeString();
        final String patch = StringIncrementor.patch(a, r);
        assertEquals("foo", patch);
    }

    @Test
    public void testPatch_SingleDifference() {
        final String a = "foo";
        final String b = "bar";
        final RelativeString r = StringIncrementor.diff(a, b);
        final String patch = StringIncrementor.patch(a, r);
        assertEquals("bar", patch);
    }

    @Test
    public void testPatch_MultipleDifferences() {
        final String a = "foo bar baz";
        final String b = "qux quux quuz";
        final RelativeString r = StringIncrementor.diff(a, b);
        final String patch = StringIncrementor.patch(a, r);
        assertEquals("qux quux quuz", patch);
    }

    @Test
    public void testPatch_Verbose() {
        final String a = "foo bar baz";
        final String b = "qux quux quuz";
        final boolean verbose = true;
        final RelativeString r = StringIncrementor.diff(a, b);
        final String patch = StringIncrementor.patch(a, r, verbose);
        assertEquals("qux quux quuz", patch);
        for (int i = 0; i < r.linksCount(); ++i) {
            final StringLink link = r.getLink(i);
            System.out.println("Link #" + i + ": " + link);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testPatch_NullString() {
        final String a = null;
        final RelativeString r = new RelativeString();
        StringIncrementor.patch(a, r);
    }

    @Test(expected = NullPointerException.class)
    public void testPatch_NullRelativeString() {
        final String a = "foo";
        final RelativeString r = null;
        StringIncrementor.patch(a, r);
    }
}