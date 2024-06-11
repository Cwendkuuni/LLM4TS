package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        testTranslate("&amp;#123;", unescaper, "c1");
        testTranslate("&amp;#x7f;", unescaper, "\u007f");
        testTranslate("&amp;#xFFFF;", unescaper, "\uFFFF");
        testTranslate("&amp;#x1234;", unescaper, "\u1234");
        testTranslate("&amp;#123ab;", unescaper, "c1 c3 b1 a3 b1");
        testTranslate("&amp;#x1F00AB;", unescaper, "\uD800\uDCA1");
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        testTranslate("&amp;#123;", unescaper, "c1");
        testTranslate("&amp;#123", unescaper, "c1");
        testTranslate("&amp;#x7f;", unescaper, "\u007f");
        testTranslate("&amp;#xFFFF;", unescaper, "\uFFFF");
        testTranslate("&amp;#x1234;", unescaper, "\u1234");
        testTranslate("&amp;#123ab;", unescaper, "c1 c3 b1 a3 b1");
        testTranslate("&amp;#x1F00AB;", unescaper, "\uD800\uDCA1");
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        testTranslate("&amp;#123;", unescaper, "c1");
        testTranslate("&amp;#123", unescaper, null);
    }

    @Test
    public void testTranslateWithNoOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        testTranslate("&amp;#123;", unescaper, "c1");
        testTranslate("&amp;#123", unescaper, "c1");
    }

    private void testTranslate(String input, NumericEntityUnescaper unescaper, String expectedOutput) {
        StringWriter writer = new StringWriter();
        char[] chars = input.toCharArray();
        int index = 0;
        int length = unescaper.translate(new String(chars), index, writer);
        assertEquals("Expected correct output", expectedOutput, writer.toString());
        assertEquals("Expected correct length", chars.length - index + length, chars.length);
    }

    @Test
    public void testIsSetWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue("Expected semiColonRequired to be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse("Expected other options to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse("Expected other options to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSetWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertFalse("Expected semiColonRequired to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue("Expected semiColonOptional to be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse("Expected errorIfNoSemiColon to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSetWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertFalse("Expected semiColonRequired to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse("Expected semiColonOptional to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue("Expected errorIfNoSemiColon to be set", unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSetWithEmptyOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse("Expected semiColonRequired to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse("Expected semiColonOptional to not be set", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue("Expected semiColonRequired to be set as a default", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        char[] chars = "&amp;#invalid".toCharArray();
        int index = 0;
        unescaper.translate(new String(chars), index, writer);
    }
}

class TestHelper {
    public static <T> EnumSet<T> copyOf(T[] array) {
        return EnumSet.copyOf(Arrays.asList(array));
    }
}