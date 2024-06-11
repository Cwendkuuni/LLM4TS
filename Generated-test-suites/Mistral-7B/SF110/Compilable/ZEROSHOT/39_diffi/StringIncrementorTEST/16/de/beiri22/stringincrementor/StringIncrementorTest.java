package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiff()
    {
        // Test case 1: empty strings
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);

        // Test case 2: identical strings
        a = "abcdefghijklmnopqrstuvwxyz";
        b = a;
        result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);

        // Test case 3: different strings with a common substring
        a = "abcdefghijklmnopqrstuvwxyz";
        b = "xyzabcdefghijklmnopqrstuvw";
        result = StringIncrementor.diff(a, b);
        StringLink link = result.getLink(0);
        assertEquals(12, link.getLen());
        assertEquals(7, link.getPosNew());
        assertEquals(11, link.getPosOrig());
        String expected = "xyz";
        assertEquals(expected, result.getAbsolute().substring(link.getPosOrig(), link.getPosOrig() + link.getLen()));

        // Test case 4: different strings with no common substrings
        a = "abcdefghijklmnopqrstuvwxyz";
        b = "abxyzpqrstuvwxyzklmno";
        result = StringIncrementor.diff(a, b);
        assertEquals(20, result.linksCount());
        String expectedAbsolute = "abxyzpqrstuvwxyzklmno";
        assertEquals(expectedAbsolute, result.getAbsolute());

        // Test case 5: test verbose mode
        System.out.println("Verbose mode test:");
        a = "abcdefghijklmnopqrstuvwxyz";
        b = "xyzabcdefghijklmnopqrstuvw";
        result = StringIncrementor.diff(a, b, true);
        link = result.getLink(0);
        assertEquals(12, link.getLen());
        assertEquals(7, link.getPosNew());
        assertEquals(11, link.getPosOrig());
        expected = "xyz";
        assertEquals(expected, result.getAbsolute().substring(link.getPosOrig(), link.getPosOrig() + link.getLen()));
        System.out.println("Link @ 0%: " + link);

        a = "abcdefghijklmnopqrstuvwxyz";
        b = "abxyzpqrstuvwxyzklmno";
        result = StringIncrementor.diff(a, b, true);
        assertEquals(22, result.linksCount());
        assertEquals(21, result.getAbsolute().length());
        for (int i = 0; i < result.linksCount(); ++i) {
            link = result.getLink(i);
            System.out.println("Link " + (i + 1) + " @ " + Math.round(i / (result.linksCount() * 1.0) * 100.0) / 10.0 + "%: " + link);
        }
    }

    @Test
    public void testPatch()
    {
        // Test case 1: empty strings
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);

        // Test case 2: identical strings
        a = "abcdefghijklmnopqrstuvwxyz";
        r = new RelativeString();
        result = StringIncrementor.patch(a, r);
        assertEquals(a, result);

        // Test case 3: patch string with a common substring
        a = "abcdefghijklmnopqrstuvwxyz";
        r = new RelativeString();
        r.addLink(new StringLink(7, 7, 3));
        result = StringIncrementor.patch(a, r);
        assertEquals("xyzabcdefghijklmnopqrstuvwxyz", result);

        // Test case 4: patch string with multiple links
        a = "abcdefghijklmnopqrstuvwxyz";
        r = new RelativeString();
        r.addLink(new StringLink(7, 7, 3));
        r.addLink(new StringLink(13, 13, 5));
        result = StringIncrementor.patch(a, r);
        assertEquals("xyzxyzdefghijklmnopqrstuvwxyz", result);

        // Test case 5: test verbose mode
        System.out.println("Verbose mode test:");
        a = "abcdefghijklmnopqrstuvwxyz";
        r = new RelativeString();
        r.addLink(new StringLink(7, 7, 3));
        result = StringIncrementor.patch(a, r, true);
        System.out.println("Patched string: " + result);

        a = "abcdefghijklmnopqrstuvwxyz";
        r = new RelativeString();
        r.addLink(new StringLink(7, 7, 3));
        r.addLink(new StringLink(13, 13, 5));
        result = StringIncrementor.patch(a, r, true);
        String expected = "xyzxyzdefghijklmnopqrstuvwxyz";
        assertEquals(expected, result);
        System.out.println("Added 'xyz' from pos#7 to pos#10");
        System.out.println("Added 'xyzdefghijklmnopqrstuvw' from pos#13 to pos#28");
    }
}