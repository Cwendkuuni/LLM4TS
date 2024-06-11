import static org.junit.Assert.*;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import java.io.*;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "hello";
        String b = "hello world";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(5, 0, 5, " world"));
        String result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiffWithVerboseOutput() {
        String a = "hello";
        String b = "hello world";
        String expectedOutput = "@ 50.0%\nAdding Link: StringLink{posOrig=5, posNew=5, len=5, text= world}";
        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                assertEquals(expectedOutput, s);
            }
        };
        RelativeString result = StringIncrementor.diff(a, b, true);
        // Assertions for the result can be added here
    }

    @Test
    public void testPatch() {
        String a = "hello world this is a test string";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(11, 7, 4, " test"));
        String expectedResult = "hello world test is a test string";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPatchWithVerboseOutput() {
        String a = "hello world this is a test string";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(11, 7, 4, " test"));
        String expectedOutput = "Adding 4 absolute Bytes from pos#11\nAdding Link: StringLink{posOrig=11, posNew=11, len=4, text= test}";
        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                assertEquals(expectedOutput, s);
            }
        };
        String result = StringIncrementor.patch(a, r, true);
        // Assertions for the result can be added here
    }

    // Add test cases for edge cases and exception handling scenarios here

    @Test(expected = NullPointerException.class)
    public void testNullStringInput() {
        String a = null;
        String b = "hello world";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsException() {
        String a = "hello world";
        String b = "hello world hello world";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidVerboseFlag() {
        String a = "hello";
        String b = "hello world";
        StringIncrementor.diff(a, b, "invalid");
    }

    @Test
    public void testEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testIdenticalStrings() {
        String a = "hello";
        String b = "hello";
        String result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testStringsWithNoCommonSubstrings() {
        String a = "hello";
        String b = "world";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(new RelativeString(), result);
    }

    @Test
    public void testEmptySourceString() {
        String a = "";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 1, 1, "world"));
        String result = StringIncrementor.patch(a, r);
        assertEquals("world", result);
    }

    @Test
    public void testSourceStringShorterThanTargetString() {
        String a = "hello";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 0, 5, " world"));
        String result = StringIncrementor.patch(a, r);
        assertEquals("hello world", result);
    }

    @Test
    public void testSourceStringLongerThanTargetString() {
        String a = "hello world this is a test string";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(11, 7, 4, " test"));
        String result = StringIncrementor.patch(a, r);
        assertEquals("hello world test is a test string", result);
    }
}