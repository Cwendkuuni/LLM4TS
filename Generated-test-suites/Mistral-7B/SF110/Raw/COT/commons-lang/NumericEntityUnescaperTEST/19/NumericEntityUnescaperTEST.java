package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

        NumericEntityUnescaper unescaperDefault = new NumericEntityUnescaper();
        assertFalse(unescaperDefault.isSet(OPTION.semiColonRequired));
        assertFalse(unescaperDefault.isSet(OPTION.semiColonOptional));
        assertTrue(unescaperDefault.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate_NumericEntityWithoutSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;#123;";
        StringWriter out = new StringWriter();

        int translated = unescaper.translate(input, 0, out);
        assertEquals(input.length(), translated);
        assertEquals("", out.toString());

        //Test for option: errorIfNoSemiColon
        unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        input = "&amp;#123;";
        out = new StringWriter();
        try {
            unescaper.translate(input, 0, out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }

    @Test
    public void testTranslate_SimpleNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;#1234;";
        StringWriter out = new StringWriter();

        int translated = unescaper.translate(input, 0, out);
        assertEquals(5, translated);
        assertEquals("\u0001234", out.toString());

        //Test for option: semiColonRequired
        unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        input = "&amp;#1234;";
        out = new StringWriter();
        translated = unescaper.translate(input, 0, out);
        assertEquals(6, translated);
        assertEquals("\u0001234;", out.toString());
    }

    @Test
    public void testTranslate_NumericEntityWithHexCode() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;#xABCD";
        StringWriter out = new StringWriter();

        int translated = unescaper.translate(input, 0, out);
        assertEquals(5, translated);
        assertEquals("\u00ABCD", out.toString());

        //Test for option: semiColonRequired
        unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        input = "&amp;#xABCD";
        out = new StringWriter();
        translated = unescaper.translate(input, 0, out);
        assertEquals(6, translated);
        assertEquals("\u00ABCD;", out.toString());
    }

    @Test
    public void testTranslate_NumericEntityWithLongCode() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;#65535";
        StringWriter out = new StringWriter();

        int translated = unescaper.translate(input, 0, out);
        assertEquals(3, translated);
        assertEquals("\uFFFF", out.toString());

        //Test for option: semiColonRequired
        unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        input = "&amp;#65535";
        out = new StringWriter();
        translated = unescaper.translate(input, 0, out);
        assertEquals(4, translated);
        assertEquals("\uFFFF;", out.toString());
    }

    @Test
    public void testTranslate_NumericEntityWithSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;#1234;";
        StringWriter out = new StringWriter();

        int translated = unescaper.translate(input, 0, out);
        assertEquals(6, translated);
        assertEquals("\u0001234;", out.toString());

        //Test for option: semiColonOptional
        unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        input = "&amp;#1234";
        out = new StringWriter();
        translated = unescaper.translate(input, 0, out);
        assertEquals(5, translated);
        assertEquals("\u0001234", out.toString());
    }
}