package org.junit.tests.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testConstructorWithoutOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet_semiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet_semiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testIsSet_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate_validNumericEntityWithSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#x1F600;";
        int index = 0;
        unescaper.translate(new StringReader(input), index, out);
        assertEquals("\uD83C\uDF00", out.toString());
        assertEquals(7, out.getBuffer().length());
    }

    @Test
    public void testTranslate_validNumericEntityWithoutSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        String input = "&amp;#x1F600";
        int index = 0;
        unescaper.translate(new StringReader(input), index, out);
        assertEquals("\uD83C\uDF00", out.toString());
        assertEquals(6, out.getBuffer().length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_invalidEntityWithoutSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        String input = "&amp;#x1F600";
        int index = 0;
        unescaper.translate(new StringReader(input), index, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_invalidEntityWithInvalidHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#xz1F600";
        int index = 0;
        unescaper.translate(new StringReader(input), index, out);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_invalidEntityWithInvalidNumber() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#abc123";
        int index = 0;
        unescaper.translate(new StringReader(input), index, out);
    }

}