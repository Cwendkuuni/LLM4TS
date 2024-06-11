package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    private static final String NUMERIC_ENTITY = "&amp;#x1F601;&#xA9;"; // Unicode character U+1F601 (grinning face)
    private static final String NUMERIC_ENTITY_DECIMAL = "&amp;#128515;";
    private static final String NUMERIC_ENTITY_HEX = "&amp;#x1F601;";
    private static final String NUMERIC_ENTITY_WITH_SEMICOLON = "&amp;#x1F601;";
    private static final String NUMERIC_ENTITY_NO_SEMICOLON = "&amp;#x1F601";
    private static final String EMPTY_STRING = "";

    @Test
    public void testTranslate_EmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(0, unescaper.translate(EMPTY_STRING, 0, out));
        assertEquals(EMPTY_STRING, out.toString());
    }

    @Test
    public void testTranslate_NoEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(0, unescaper.translate("foo", 0, out));
        assertEquals("foo", out.toString());
    }

    @Test
    public void testTranslate_InvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(0, unescaper.translate("&a", 0, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_DecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(3, unescaper.translate(NUMERIC_ENTITY_DECIMAL, 0, out));
        assertEquals("\u0001\u0002", out.toString());
    }

    @Test
    public void testTranslate_HexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(4, unescaper.translate(NUMERIC_ENTITY_HEX, 0, out));
        assertEquals("\uD83D\uDE01", out.toString());
    }

    @Test
    public void testTranslate_EntityWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(4, unescaper.translate(NUMERIC_ENTITY_WITH_SEMICOLON, 0, out));
        assertEquals("\uD83D\uDE01", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_EntityWithoutSemicolon_ErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        assertEquals(4, unescaper.translate(NUMERIC_ENTITY_NO_SEMICOLON, 0, out));
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testTranslate_EntityWithoutSemicolon_SemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        assertEquals(4, unescaper.translate(NUMERIC_ENTITY_NO_SEMICOLON, 0, out));
        assertEquals("\uD83D\uDE01", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_EntityWithoutSemicolon_ErrorIfNoSemiColonOption_Set() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        assertEquals(4, unescaper.translate(NUMERIC_ENTITY_NO_SEMICOLON, 0, out));
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testIsSet_ValidOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testIsSet_EmptyOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }
}