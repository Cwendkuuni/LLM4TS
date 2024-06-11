package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest {

    private String a;
    private String b;
    private RelativeString result;

    @Before
    public void setUp() {
        a = "The quick brown fox jumps over the lazy dog.";
        b = "The quick red fox jumps over the lazy cat.";
    }

    @Test
    public void testDiff_SimpleCase() {
        result = StringIncrementor.diff(a, b);
        assertEquals(7, result.linksCount());

        StringLink[] links = result.getLinks();
        assertEquals(2, links[0].getLen());
        assertEquals("red", links[0].getNew());
        assertEquals(6, links[1].getPosNew());
        assertEquals("jumps over the lazy ", links[1].getOld());
        assertEquals(17, links[1].getPosOrig());
        assertEquals("jumps over the lazy cat.", links[1].getNew());

        assertEquals(4, links[6].getLen());
        assertEquals("cat.", links[6].getNew());
        assertEquals(35, links[6].getPosNew());
        assertEquals("dog.", links[6].getOld());
        assertEquals(39, links[6].getPosOrig());
    }

    @Test
    public void testDiff_Verbose() {
        result = StringIncrementor.diff(a, b, true);
        assertEquals(7, result.linksCount());

        StringLink[] links = result.getLinks();

        // Skip the first link since it's not verbose
        for (int i = 1; i < links.length; ++i) {
            StringLink link = links[i];
            System.out.println("Link #" + i + ":");
            System.out.println("Old: " + link.getOld());
            System.out.println("New: " + link.getNew());
            System.out.println("Pos Orig: " + link.getPosOrig());
            System.out.println("Pos New: " + link.getPosNew());
            System.out.println("Len: " + link.getLen());
        }
    }

    @Test
    public void testPatch_SimpleCase() {
        String patched = StringIncrementor.patch(a, result);
        assertEquals(b, patched);
    }

    @Test
    public void testPatch_Verbose() {
        System.out.println("Original String A: " + a);
        System.out.println("Relative String:");
        result = StringIncrementor.diff(a, b, true);
        StringLink[] links = result.getLinks();
        for (int i = 0; i < links.length; ++i) {
            StringLink link = links[i];
            System.out.println("Link #" + i + ":");
            System.out.println("Old: " + link.getOld());
            System.out.println("New: " + link.getNew());
            System.out.println("Pos Orig: " + link.getPosOrig());
            System.out.println("Pos New: " + link.getPosNew());
            System.out.println("Len: " + link.getLen());
        }

        System.out.println("Patched String A:");
        String patched = StringIncrementor.patch(a, result, true);
        System.out.println(patched);
    }

    @Test(expected = NullPointerException.class)
    public void testDiff_NullString() {
        StringIncrementor.diff(null, b);
    }

    @Test(expected = NullPointerException.class)
    public void testDiff_NullString2() {
        StringIncrementor.diff(a, null);
    }

    @Test(expected = NullPointerException.class)
    public void testPatch_NullRelativeString() {
        StringIncrementor.patch(a, null);
    }
}