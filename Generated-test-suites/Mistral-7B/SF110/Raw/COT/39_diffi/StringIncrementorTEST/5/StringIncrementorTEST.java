package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.*;

public class StringIncrementorTest
{
    @Test
    public void testDiffMethod_whenStringsAreEqual_thenReturnEmptyRelativeString()
    {
        String a = "example";
        String b = "example";
        RelativeString result = StringIncrementor.diff(a, b);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDiffMethod_whenStringBIsSubStringOfStringA_thenReturnCorrectRelativeString()
    {
        String a = "exampleText";
        String b = "example";
        RelativeString result = StringIncrementor.diff(a, b);
        StringLink expectedLink = new StringLink(6, 0, 6);
        assertEquals(expectedLink, result.getLink(0));
    }

    @Test
    public void testDiffMethod_whenStringsAreDifferent_thenReturnCorrectRelativeString()
    {
        String a = "exampleText";
        String b = "differentText";
        RelativeString result = StringIncrementor.diff(a, b);
        StringLink expectedLink1 = new StringLink(3, 0, 4);
        StringLink expectedLink2 = new StringLink(10, 6, 3);
        assertEquals(expectedLink1, result.getLink(0));
        assertEquals(expectedLink2, result.getLink(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffMethod_whenFirstArgumentIsNull_thenThrowIllegalArgumentException()
    {
        StringIncrementor.diff(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffMethod_whenSecondArgumentIsNull_thenThrowIllegalArgumentException()
    {
        String a = "test";
        StringIncrementor.diff(a, null);
    }

    @Test
    public void testPatchMethod_whenRelativeStringIsEmpty_thenReturnOriginalString()
    {
        String original = "exampleText";
        RelativeString emptyRelativeString = new RelativeString();
        String result = StringIncrementor.patch(original, emptyRelativeString);
        assertEquals(original, result);
    }

    @Test
    public void testPatchMethod_whenPatchingStringWithOneLink_thenReturnCorrectPatchedString()
    {
        String original = "exampleText";
        RelativeString relativeString = new RelativeString();
        StringLink link = new StringLink(3, 0, 4);
        relativeString.addLink(link);
        String result = StringIncrementor.patch(original, relativeString);
        String expected = "exampleTextText";
        assertEquals(expected, result);
    }

    @Test
    public void testPatchMethod_whenPatchingStringWithMultipleLinks_thenReturnCorrectPatchedString()
    {
        String original = "exampleText1 exampleText2 exampleText3";
        RelativeString relativeString = new RelativeString();
        StringLink link1 = new StringLink(3, 0, 4);
        StringLink link2 = new StringLink(12, 11, 3);
        relativeString.addLink(link1);
        relativeString.addLink(link2);
        String result = StringIncrementor.patch(original, relativeString);
        String expected = "exampleText1 exampleTextText2 exampleText3";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchMethod_whenOriginalStringIsNull_thenThrowIllegalArgumentException()
    {
        String original = null;
        RelativeString relativeString = new RelativeString();
        String result = StringIncrementor.patch(original, relativeString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchMethod_whenRelativeStringIsNull_thenThrowIllegalArgumentException()
    {
        String original = "test";
        RelativeString relativeString = null;
        String result = StringIncrementor.patch(original, relativeString);
    }
}