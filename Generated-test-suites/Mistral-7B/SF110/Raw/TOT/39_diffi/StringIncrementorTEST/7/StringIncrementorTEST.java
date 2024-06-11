package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringLink;
import de.beiri22.stringincrementor.relativestring.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest
{
    @Test
    public void testDiffEmptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testDiffIdenticalStrings()
    {
        String a = "testtesttest";
        String b = "testtesttest";
        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testDiffSingleCharacterDifference()
    {
        String a = "testtesttest";
        String b = "testtesttes";
        RelativeString result = StringIncrementor.diff(a, b, false);
        StringLink expectedLink = new StringLink(3, 6, 1);
        assertEquals(expectedLink, result.getLink(0));
    }

    @Test
    public void testDiffMultipleCharacterDifferences()
    {
        String a = "testtesttest";
        String b = "testtesteresttest";
        RelativeString result = StringIncrementor.diff(a, b, false);
        StringLink link1 = new StringLink(3, 6, 1);
        StringLink link2 = new StringLink(10, 12, 3);
        StringLink link3 = new StringLink(14, 17, 3);
        StringLink[] expectedLinks = { link1, link2, link3 };
        assertEquals(3, result.linksCount());
        for (int i = 0; i < result.linksCount(); ++i) {
            assertEquals(expectedLinks[i], result.getLink(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffNullString()
    {
        String a = null;
        String b = "testtesttest";
        StringIncrementor.diff(a, b, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffNullRelativeString()
    {
        String a = "testtesttest";
        RelativeString r = null;
        StringIncrementor.patch(a, r, false);
    }

    @Test
    public void testPatchEmptyString()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r, false);
        assertEquals("", result);
    }

    @Test
    public void testPatchSingleLink()
    {
        String a = "testtesttest";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 6, 1));
        String result = StringIncrementor.patch(a, r, false);
        assertEquals("testtesttest", result);
    }

    @Test
    public void testPatchMultipleLinks()
    {
        String a = "testtesttest";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 6, 1));
        r.addLink(new StringLink(10, 12, 3));
        r.addLink(new StringLink(14, 17, 3));
        String result = StringIncrementor.patch(a, r, false);
        assertEquals("testtesteresttest", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchNullString()
    {
        String a = null;
        RelativeString r = new RelativeString();
        StringIncrementor.patch(a, r, false);
    }

    @Test
    public void testPatchEmptyRelativeString()
    {
        String a = "testtesttest";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r, false);
        assertEquals("testtesttest", result);
    }
}