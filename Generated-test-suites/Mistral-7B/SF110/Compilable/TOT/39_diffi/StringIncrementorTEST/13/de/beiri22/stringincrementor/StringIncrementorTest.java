package de.beiri22.stringincrementor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_SameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expectedResult = new RelativeString();
        // Empty string as expected result since a and b are identical
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_EmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expectedResult = new RelativeString();
        // Empty string as expected result since a and b are identical
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_DifferentStringsWithDifferencesAtBeginning() {
        String a = "hello";
        String b = "world";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 5)); // 'hello' -> 'world'
        expectedResult.setAbsolute("world");
        assertEquals(expectedResult, StringIncrementor.diff(a, b, true));
    }

    @Test
    public void testDiff_DifferentStringsWithDifferencesAtEnd() {
        String a = "hello";
        String b = "hello world";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(5, b.length() - 5, 6)); // 'world' -> ''
        expectedResult.setAbsolute("world");
        assertEquals(expectedResult, StringIncrementor.diff(a, b, true));
    }

    @Test
    public void testDiff_DifferentStringsWithDifferencesInTheMiddle() {
        String a = "hello world";
        String b = "hello hello";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(6, 10, 5)); // 'hello' -> 'hello'
        expectedResult.setAbsolute("world");
        assertEquals(expectedResult, StringIncrementor.diff(a, b, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_NullStrings() {
        String a = null;
        String b = "hello";
        try {
            StringIncrementor.diff(a, b);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testPatch_SameStrings() {
        String a = "hello";
        RelativeString r = new RelativeString();
        String expectedResult = "hello";
        String actualResult = StringIncrementor.patch(a, r);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPatch_EmptyRelativeString() {
        String a = "hello world";
        RelativeString r = new RelativeString();
        String expectedResult = "hello world";
        String actualResult = StringIncrementor.patch(a, r);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPatch_IdentityRelativeString() {
        String a = "hello world";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, a.length()));
        String expectedResult = a;
        String actualResult = StringIncrementor.patch(a, r);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPatch_SimpleRelativeString() {
        String a = "hello world";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(6, 10, 5)); // 'world' -> ''
        String expectedResult = "hellow";
        String actualResult = StringIncrementor.patch(a, r);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPatch_ComplexRelativeString() {
        String a = "hello world hello hello";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(6, 14, 6)); // 'hello hello' -> ''
        r.addLink(new StringLink(3, 13, 6)); // 'hello' -> 'hello'
        String expectedResult = "hellowello";
        String actualResult = StringIncrementor.patch(a, r);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_NullStrings() {
        String a = null;
        RelativeString r = new RelativeString();
        try {
            StringIncrementor.patch(a, r);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}