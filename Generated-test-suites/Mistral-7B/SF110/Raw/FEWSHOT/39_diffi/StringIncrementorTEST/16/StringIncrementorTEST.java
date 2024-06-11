package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiffEmptyString() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testDiffIdenticalStrings() {
        String a = "foo";
        String b = "foo";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testDiffSingleCharacterDifference() {
        String a = "foo";
        String b = "bar";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new StringLink(3, 0, 1), result.getLink(0));
        assertEquals("1", result.getAbsolute());
    }

    @Test
    public void testDiffMultipleCharacterDifference() {
        String a = "foo";
        String b = "barb";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new StringLink(3, 0, 1), result.getLink(0));
        assertEquals(new StringLink(5, 2, 1), result.getLink(1));
        assertEquals("1", result.getAbsolute());
    }

    @Test
    public void testDiffLongStringWithSingleCharacterDifference() {
        String a = "foo";
        String b = "bar";
        String longStringA = "foobarfoofoobarfoofoobarfoo";
        String longStringB = "foobarfoofoobarfoofoobarfoofoobarfoofoobar";
        RelativeString result = StringIncrementor.diff(longStringA, longStringB);
        assertEquals(new StringLink(14, 13, 1), result.getLink(0));
        assertEquals("1", result.getAbsolute());
    }

    @Test
    public void testDiffLongStringWithMultipleCharacterDifference() {
        String a = "foo";
        String b = "barb";
        String longStringA = "foobarfoofoobarfoofoobarfoo";
        String longStringB = "foobarfoofoobarfoofoobarfoofoobarfoofoobar";
        RelativeString result = StringIncrementor.diff(longStringA, longStringB);
        assertEquals(new StringLink(14, 13, 1), result.getLink(0));
        assertEquals(new StringLink(16, 26, 1), result.getLink(1));
        assertEquals("2", result.getAbsolute());
    }

    @Test
    public void testDiffLongStringWithMultipleCharacterDifferences() {
        String a = "foo";
        String b = "barbazqux";
        String longStringA = "foobarfoofoobarfoofoobarfoo";
        String longStringB = "foobarfoofoobarfoofoobarfoofoobarbazqux";
        RelativeString result = StringIncrementor.diff(longStringA, longStringB, true);
        assertEquals(new StringLink(14, 13, 1), result.getLink(0));
        assertEquals(new StringLink(17, 27, 1), result.getLink(1));
        assertEquals(new StringLink(11, 34, 1), result.getLink(2));
        assertEquals("3", result.getAbsolute());
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
        RelativeString r = new StringLink(3, 0, 1);
        String result = StringIncrementor.patch(a, r);
        assertEquals("bar", result);
    }

    @Test
    public void testPatchMultipleCharacterDifference() {
        String a = "foo";
        String b = "barb";
        RelativeString r = new StringLink(3, 0, 1)
                .addLink(new StringLink(5, 2, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("barb", result);
    }

    @Test
    public void testPatchLongStringWithSingleCharacterDifference() {
        String a = "foobarfoofoobarfoofoobarfoo";
        String b = "foobarfoofoobarfoofoobarfoofoobar";
        RelativeString r = new StringLink(14, 13, 1);
        String result = StringIncrementor.patch(a, r);
        assertEquals("foobarfoofoobarfoofoobarfoofoobar", result);
    }

    @Test
    public void testPatchLongStringWithMultipleCharacterDifferences() {
        String a = "foobarfoofoobarfoofoobarfoo";
        String b = "foobarfoofoobarfoofoobarfoofoobarfoofoobar";
        RelativeString r = new StringLink(14, 13, 1)
                .addLink(new StringLink(16, 26, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("foobarfoofoobarfoofoobarfoofoobarbazqux", result);
    }

}