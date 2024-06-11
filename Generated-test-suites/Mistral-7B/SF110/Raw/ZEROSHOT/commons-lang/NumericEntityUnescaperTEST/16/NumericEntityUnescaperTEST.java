package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    /**
     * Test constructor with default options
     */
    @Test
    public void testDefaultConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    /**
     * Test constructor with custom options
     */
    @Test
    public void testCustomConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    /**
     * Test isSet method
     */
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    }

    /**
     * Test translate method with valid numeric entities
     */
    @Test
    public void testTranslateValidEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160; &#x2013; &#10; &#xFF01; &#x1F600; &#x1F601; &#x1F602; &#xFFFD; &amp; #x1F6FB; &amp; #xFFFE;";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int bytesWritten = unescaper.translate(new String(input, index, length - index), index, output);
            index += bytesWritten;
        }
        assertEquals("  – 	 👨‍👩‍👧‍👦 🚧 ✅ 🙇‍♂️ 🙇‍♀️", output.toString());
    }

    /**
     * Test translate method with invalid entities
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#abc;";
        StringWriter output = new StringWriter();
        int index = 0;
        unescaper.translate(new String(input, index, input.length() - index), index, output);
    }

    /**
     * Test translate method with missing semi colon
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMissingSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&amp;#123;";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int bytesWritten = unescaper.translate(new String(input, index, length - index), index, output);
            index += bytesWritten;
        }
    }

    /**
     * Test translate method with missing semi colon and semiColonOptional option
     */
    @Test
    public void testTranslateMissingSemiColonOptional() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "&amp;#123";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int bytesWritten = unescaper.translate(new String(input, index, length - index), index, output);
            index += bytesWritten;
        }
        assertEquals("", output.toString());
    }

    /**
     * Test translate method with valid and invalid entities mixed
     */
    @Test
    public void testTranslateValidAndInvalidEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160; &amp;#x2013; &amp;#10; &amp;#xFF01; &amp;#x1F600; &amp;#x1F601; &amp;#x1F602; &amp;#xFFFD; &amp; #x1F6FB; &amp; #xFFFE; &amp;#abc;";
        StringWriter output = new StringWriter();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int bytesWritten = unescaper.translate(new String(input, index, length - index), index, output);
            index += bytesWritten;
        }
        assertEquals("  – 	 👨‍👩‍👧‍👦 🚧 ✅ 🙇‍♂️ 🙇‍♀️", output.toString());
    }
}