package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        Writer out = new StringWriter();
        String input = "&amp;#x1F602;&#xA9;"; // Unicode characters: smiling face & copyright sign

        int index = 0;
        int charsTranslated = unescaper.translate(new StringReader(input), index, out);
        assertEquals(input.length(), index + charsTranslated);
        assertEquals("😊©", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        Writer out = new StringWriter();
        String input = "&amp;#x1F602;"; // Unicode character: smiling face with semi-colon

        int index = 0;
        int charsTranslated = unescaper.translate(new StringReader(input), index, out);
        assertEquals(input.length(), index + charsTranslated);
        assertEquals("😊", out.toString());
    }

    @Test
    public void testTranslateWithoutSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(); // Defaults to semiColonRequired=false, errorIfNoSemiColon=false
        Writer out = new StringWriter();
        String input = "&amp;#x1F602"; // Unicode character: smiling face

        int index = 0;
        int charsTranslated = unescaper.translate(new StringReader(input), index, out);
        assertEquals(input.length(), index + charsTranslated);
        assertEquals("😊", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        Writer out = new StringWriter();
        String input = "&amp;#x1F602"; // Unicode character: smiling face

        int index = 0;
        int charsTranslated = unescaper.translate(new StringReader(input), index, out);
        fail("Expected IllegalArgumentException: Semi-colon required at end of numeric entity");
    }

    @Test
    public void testTranslateWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        String input = "&amp;#invalid";

        int index = 0;
        int charsTranslated = unescaper.translate(new StringReader(input), index, out);
        assertEquals(input.length(), index + charsTranslated);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithHexAndDecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        String input = "&amp;#x1F602;&#1048576;"; // Unicode character: smiling face & number 1073741824

        int index = 0;
        int charsTranslated = unescaper.translate(new StringReader(input), index, out);
        assertEquals(input.length(), index + charsTranslated);
        assertEquals("😊𝌆", out.toString());
    }
}