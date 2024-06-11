package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_semiColonRequired_noSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "&amp;#123";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals("Error: Semi-colon required at end of numeric entity", -1, length);
    }

    @Test
    public void testTranslate_semiColonRequired_hasSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "&amp;#123;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(1, length);
        assertEquals("\u007B", out.toString());
    }

    @Test
    public void testTranslate_semiColonOptional_noSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(1, length);
        assertEquals("\u007B", out.toString());
    }

    @Test
    public void testTranslate_semiColonOptional_hasSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(1, length);
        assertEquals("\u007B", out.toString());
    }

    @Test
    public void testTranslate_errorIfNoSemiColon_noSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&amp;#123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, new StringWriter());
        });

        assertEquals("Semi-colon required at end of numeric entity", exception.getMessage());
    }

    @Test
    public void testTranslate_errorIfNoSemiColon_hasSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&amp;#123;";

        StringWriter out = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(1, length);
        assertEquals("\u007B", out.toString());
    }

    @Test
    public void testTranslate_hex_validEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x4141;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(3, length);
        assertEquals("AA", out.toString());
    }

    @Test
    public void testTranslate_hex_invalidLength() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x41;";
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(0, length);
    }

    @Test
    public void testTranslate_decimal_validEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#1234;";
        StringWriter out = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(4, length);
        assertEquals("\u007B\u0034", out.toString());
    }

    @Test
    public void testTranslate_decimal_entityOutOfRange() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#65536;";
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals(3, length);
        assertEquals("\uD800\uDC00", out.toString());
    }

}