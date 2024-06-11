package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        char[] input = {'&', '#', 'x', '3', 'F', ';'};
        int length = unescaper.translate(new String(input), 0, out);
        assertEquals("Ø", out.toString().substring(0, length));
    }

    @Test
    public void testTranslateWithSemiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        char[] input = {'&', '#', 'x', '3', 'F'};
        int length = unescaper.translate(new String(input), 0, out);
        assertEquals("Ø", out.toString().substring(0, length));
    }

    @Test
    public void testTranslateWithoutSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        char[] input = {'&', '#', 'x', '3', 'F'};
        int length = unescaper.translate(new String(input), 0, out);
        assertEquals("Ø", out.toString().substring(0, length));
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        char[] input = {'&', '#', 'x', '3', 'F'};
        assertThrows(IllegalArgumentException.class, () -> {
            unescaper.translate(new String(input), 0, out);
        });
    }

    @Test
    public void testTranslateWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        char[] input = {'&', '#', 'x', 'z'};
        int length = unescaper.translate(new String(input), 0, out);
        assertNull(out.toString());
    }

    @Test
    public void testTranslateWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        char[] input = {'&', '#', 'x', '2', 'A', '#', 'x', '4', '1', '7', ';'};
        int length = unescaper.translate(new String(input), 0, out);
        assertEquals("\u002A\u0041\u0047", out.toString().substring(0, length));
    }

    @Test
    public void testTranslateWithLargeEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        char[] input = {'&', '#', 'x', '10FFFF'};
        int length = unescaper.translate(new String(input), 0, out);
        assertEquals("\uD800\uDBFF\uDFFF", out.toString().substring(0, length));
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    }
}