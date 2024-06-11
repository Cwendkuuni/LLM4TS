package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

/**
 * NumericEntityUnescaperTest.java - JUnit 4 test file for NumericEntityUnescaper class
 */

public class NumericEntityUnescaperTest {

    @Test
    public void testDefaultConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(SEMI_COLON_REQUIRED));
    }

    @Test
    public void testCustomConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(SEMI_COLON_OPTIONAL, ERROR_IF_NO_SEMI_COLON);
        assertFalse(unescaper.isSet(SEMI_COLON_REQUIRED));
        assertTrue(unescaper.isSet(SEMI_COLON_OPTIONAL));
        assertTrue(unescaper.isSet(ERROR_IF_NO_SEMI_COLON));
    }

    @Test
    public void testIsSet_semiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(SEMI_COLON_REQUIRED));

        unescaper = new NumericEntityUnescaper(SEMI_COLON_OPTIONAL);
        assertFalse(unescaper.isSet(SEMI_COLON_REQUIRED));

        unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
        assertTrue(unescaper.isSet(SEMI_COLON_REQUIRED));
    }

    @Test
    public void testIsSet_semiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(SEMI_COLON_OPTIONAL));

        unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
        assertFalse(unescaper.isSet(SEMI_COLON_OPTIONAL));

        unescaper = new NumericEntityUnescaper(SEMI_COLON_OPTIONAL);
        assertTrue(unescaper.isSet(SEMI_COLON_OPTIONAL));
    }

    @Test
    public void testIsSet_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(ERROR_IF_NO_SEMI_COLON));

        unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
        assertFalse(unescaper.isSet(ERROR_IF_NO_SEMI_COLON));

        unescaper = new NumericEntityUnescaper(ERROR_IF_NO_SEMI_COLON);
        assertTrue(unescaper.isSet(ERROR_IF_NO_SEMI_COLON));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_invalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "&%#";

        unescaper.translate(input, 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_noSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123";

        unescaper.translate(input, 0, out);

        unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
        input = "&amp;#123";

        unescaper.translate(input, 0, out);
        throw new AssertionError("Expected IllegalArgumentException not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_noSemiColonError() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(ERROR_IF_NO_SEMI_COLON);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123";

        unescaper.translate(input, 0, out);
    }

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123;&#125;";

        unescaper.translate(input, 0, out);
        assertEquals("abc\r\n", out.toString());
    }

    @Test
    public void testTranslateHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#x41;&#x42;";

        unescaper.translate(input, 0, out);
        assertEquals("AB\r\n", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123;\r\n&amp;#125;";

        unescaper.translate(input, 0, out);
        assertEquals("abc\r\n", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(SEMI_COLON_OPTIONAL);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123\r\n&amp;#125";

        unescaper.translate(input, 0, out);
        assertEquals("abc", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(ERROR_IF_NO_SEMI_COLON);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123";

        unescaper.translate(input, 0, out);
    }
}