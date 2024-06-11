package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest
{
    @Test
    public void testDiff_whenStringsAreEqual_thenReturnsEmptyRelativeString()
    {
        String a = "SampleString";
        String b = a;
        RelativeString expectedResult = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiff_whenFirstStringIsSubstringOfSecond_thenReturnsRelativeStringWithOneLink()
    {
        String a = "abc";
        String b = "abcdabcde";
        String expectedLink = "abc [3,6] 3";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> links = result.getLinks();
        assertEquals(1, links.size());
        assertEquals(expectedLink, links.get(0).toString());
    }

    @Test
    public void testDiff_whenBothStringsContainDifferentSubstrings_thenReturnsRelativeStringWithMultipleLinks()
    {
        String a = "abcdabcd";
        String b = "abcabcdef";
        String expectedLink1 = "abcd [2,5] 3";
        String expectedLink2 = "abc [1,3] 2";
        String expectedLink3 = "def [7,10] 4";
        RelativeString result = StringIncrementor.diff(a, b, false);
        List<StringLink> links = result.getLinks();
        assertEquals(3, links.size());
        assertEquals(expectedLink1, links.get(0).toString());
        assertEquals(expectedLink2, links.get(1).toString());
        assertEquals(expectedLink3, links.get(2).toString());
    }

    @Test
    public void testPatch_whenRelativeStringIsEmpty_thenReturnsOriginalString()
    {
        String a = "SampleString";
        RelativeString r = new RelativeString();
        String expectedResult = a;
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPatch_whenStringContainsOneLink_thenReturnsPatchedStringWithReplacedSubstring()
    {
        String a = "abcdabcd";
        String link = "abcd [2,5] 3";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 5, 3, "efg"));
        String expectedResult = "efgcdabcd";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPatch_whenStringContainsMultipleLinks_thenReturnsPatchedStringWithAllSubstringsReplaced()
    {
        String a = "abcdabcd";
        String expectedLink1 = "abcd [2,5] 3";
        String expectedLink2 = "abc [1,3] 2";
        String expectedLink3 = "def [7,10] 4";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 5, 3, "efg"));
        r.addLink(new StringLink(0, 3, 2, "hij"));
        r.addLink(new StringLink(6, 10, 4, "klm"));
        String expectedResult = "hijijklm";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expectedResult, result);
    }
}