package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    private static final String NUMERIC_ENTITY = "&amp;#x1F612;&#x1F633;"; // Unicode characters: 💃️💺
    private static final String NUMERIC_ENTITY_DECIMAL = "&amp;#128514;&#128515;";
    private static final String NUMERIC_ENTITY_HEX = "&amp;#x1F612;&#x1F633;";
    private static final String NUMERIC_ENTITY_DECIMAL_SEMICOLON = "&amp;#128514;";
    private static final String NUMERIC_ENTITY_HEX_SEMICOLON = "&amp;#x1F612;";
    private static final String NUMERIC_ENTITY_DECIMAL_NO_SEMICOLON = "&amp;#128514";
    private static final String NUMERIC_ENTITY_HEX_NO_SEMICOLON = "&amp;#x1F612";

    @Test
    public void testEmpty() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String result = unescaper.translate(new StringReader(""), 0, out);
        assertEquals("", result.toString());
    }

    @Test
    public void testNoEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = "foo";
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(input, result.toString());
    }

    @Test
    public void testValidEntityDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_DECIMAL;
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\uD83D\uDC61\uD83D\uDC69", result.toString());
    }

    @Test
    public void testValidEntityHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_HEX;
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\uD83D\uDC61\uD83D\uDC69", result.toString());
    }

    @Test
    public void testValidEntityDecimalWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_DECIMAL_SEMICOLON;
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\uD83D\uDC61\uD83D\uDC69", result.toString());
    }

    @Test
    public void testValidEntityHexWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_HEX_SEMICOLON;
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\uD83D\uDC61\uD83D\uDC69", result.toString());
    }

    @Test
    public void testValidEntityDecimalNoSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_DECIMAL_NO_SEMICOLON;
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\uD83D\uDC61\uD83D\uDC69", result.toString());
    }

    @Test
    public void testValidEntityHexNoSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_HEX_NO_SEMICOLON;
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("\uD83D\uDC61\uD83D\uDC69", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEntityNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_DECIMAL_NO_SEMICOLON;
        unescaper.translate(new StringReader(input), 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEntityHexNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_HEX_NO_SEMICOLON;
        unescaper.translate(new StringReader(input), 0, out);
    }

    @Test
    public void testInvalidEntityDecimalWithSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_DECIMAL;
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", result.toString());
    }

    @Test
    public void testInvalidEntityHexWithSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = NUMERIC_ENTITY_HEX;
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        String result = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", result.toString());
    }

    @Test
    public void testSetSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertEquals(true, unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testSetSemiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertEquals(false, unescaper.isSet(OPTION.semiColonRequired));
        assertEquals(true, unescaper.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testSetErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertEquals(false, unescaper.isSet(OPTION.semiColonRequired));
        assertEquals(false, unescaper.isSet(OPTION.semiColonOptional));
        assertEquals(true, unescaper.isSet(OPTION.errorIfNoSemiColon));
    }
}