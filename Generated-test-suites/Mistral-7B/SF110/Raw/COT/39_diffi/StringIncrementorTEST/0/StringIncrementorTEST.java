package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff_SameStrings() {
        String a = "abcdefghijklmnop";
        String b = "abcdefghijklmnop";
        RelativeString expectedResult = new RelativeString();
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_EmptyStringB() {
        String a = "abcdefghijklmnop";
        String b = "";
        try {
            StringIncrementor.diff(a, b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_EmptyStringA() {
        String a = "";
        String b = "abcdefghijklmnop";
        try {
            StringIncrementor.diff(a, b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_EmptyStringB_Verbose() {
        String a = "abcdefghijklmnop";
        String b = "";
        try {
            StringIncrementor.diff(a, b, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_EmptyStringA_Verbose() {
        String a = "";
        String b = "abcdefghijklmnop";
        try {
            StringIncrementor.diff(a, b, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_NullStringA() {
        String a = null;
        String b = "abcdefghijklmnop";
        try {
            StringIncrementor.diff(a, b);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_NullStringB() {
        String a = "abcdefghijklmnop";
        String b = null;
        try {
            StringIncrementor.diff(a, b);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_NullStringB_Verbose() {
        String a = "abcdefghijklmnop";
        String b = null;
        try {
            StringIncrementor.diff(a, b, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_NullStringA_Verbose() {
        String a = null;
        String b = "abcdefghijklmnop";
        try {
            StringIncrementor.diff(a, b, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDiff_DifferentStrings() {
        String a = "abcdefghijklmnop";
        String b = "abcdefghijklmnqop";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(5, 5, 3, "def"));
        expectedResult.setAbsolute("qop");
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_DifferentStrings_Verbose() {
        String a = "abcdefghijklmnop";
        String b = "abcdefghijklmnqop";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(5, 5, 3, "def"));
        expectedResult.setAbsolute("qop");
        StringIncrementor.diff(a, b, true);
        String result = new String(StringIncrementor.diff(a, b).getAbsolute().toCharArray());
        assertEquals(expectedResult.getAbsolute(), result);
    }

    @Test
    public void testPatch_SameStrings() {
        String a = "abcdefghijklmnop";
        RelativeString r = new RelativeString();
        String expectedResult = "abcdefghijklmnop";
        assertEquals(expectedResult, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_EmptyStringResult() {
        String a = "abcdefghijklmnop";
        RelativeString r = new RelativeString();
        try {
            StringIncrementor.patch(a, r);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPatch_EmptyStringInput() {
        String a = "";
        RelativeString r = new RelativeString();
        try {
            StringIncrementor.patch(a, r);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPatch_NullStringInput() {
        String a = null;
        RelativeString r = new RelativeString();
        try {
            StringIncrementor.patch(a, r);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPatch_NullStringResult() {
        String a = "abcdefghijklmnop";
        RelativeString r = null;
        try {
            StringIncrementor.patch(a, r);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPatch_EmptyStringResult_Verbose() {
        String a = "abcdefghijklmnop";
        RelativeString r = new RelativeString();
        try {
            StringIncrementor.patch(a, r, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPatch_EmptyStringInput_Verbose() {
        String a = "";
        RelativeString r = new RelativeString();
        try {
            StringIncrementor.patch(a, r, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPatch_NullStringInput_Verbose() {
        String a = "abcdefghijklmnop";
        RelativeString r = null;
        try {
            StringIncrementor.patch(a, r, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPatch_RelativeString_SameStrings() {
        String a = "abcdefghijklmnop";
        RelativeString r = new RelativeString();
        RelativeString expectedResult = new RelativeString();
        assertEquals(expectedResult, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_StringInput_RelativeString() {
        String a = "abcdefghijklmnop";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 3, "def"));
        r.setAbsolute("qop");
        String expectedResult = "abcdefghijklmnopqop";
        assertEquals(expectedResult, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_StringInput_RelativeString_Verbose() {
        String a = "abcdefghijklmnop";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 3, "def"));
        r.setAbsolute("qop");
        String expectedResult = "abcdefghijklmnopqop";
        StringIncrementor.patch(a, r, true);
        String result = new String(StringIncrementor.patch(a, r).getAbsolute().toCharArray());
        assertEquals(expectedResult, result);
    }
}