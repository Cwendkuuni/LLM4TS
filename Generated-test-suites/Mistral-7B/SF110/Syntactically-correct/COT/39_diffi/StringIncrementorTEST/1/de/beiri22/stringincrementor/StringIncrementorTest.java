package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiffMethodWithTwoIdenticalStrings()
    {
        String a = "example";
        String b = "example";
        RelativeString expectedResult = new RelativeString();
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiffMethodWithTwoDifferentStrings()
    {
        String a = "example";
        String b = "exampledifferent";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(4, 4, 3));
        expectedResult.setAbsolute("different");
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiffMethodWithNegativeInput()
    {
        String a = "";
        String b = "example";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiffMethodWithNullInput()
    {
        String a = null;
        String b = "example";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffMethodWithEmptyBuffer()
    {
        String a = "example";
        String b = "";
        StringIncrementor.diff(a, b);
    }

    @Test
    public void testDiffMethodWithVerboseOption()
    {
        String a = "example";
        String b = "exampledifferent";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(4, 4, 3));
        expectedResult.setAbsolute("different");

        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                assertEquals(s, "@ 50.0%\nAdding Link: StringLink@37e55e5[posOrig=4, posNew=7, len=3]\n");
                super.println(s);
            }
        };

        RelativeString result = StringIncrementor.diff(a, b, true);
        assertTrue(expectedResult.equals(result));
    }

    @Test
    public void testPatchMethodWithEmptyStringInput()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String expectedResult = "";
        assertEquals(expectedResult, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatchMethodWithAbsoluteStringLongerThanInitialString()
    {
        String a = "example";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(4, 4, 3));
        r.setAbsolute("differentlonger");
        String expectedResult = "differentlonger";
        assertEquals(expectedResult, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatchMethodWithAbsoluteStringShorterThanInitialString()
    {
        String a = "example";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(4, 4, 3));
        r.setAbsolute("shorter");
        String expectedResult = "shorterexample";
        assertEquals(expectedResult, StringIncrementor.patch(a, r));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPatchMethodWithNegativeInput()
    {
        String a = "example";
        RelativeString r = new RelativeString();
        StringIncrementor.patch(a, r, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchMethodWithNullInput()
    {
        String a = "example";
        RelativeString r = null;
        StringIncrementor.patch(a, r);
    }
}