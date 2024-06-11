package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "This text contains &amp;#x1F60A; and &amp;#123; entities.";
        CharSequence cs = CharSequence.class.cast(input);
        int index = 0;

        while (index < cs.length()) {
            int length = unescaper.translate(cs, index, out);
            index += length;
        }

        assertEquals("This text contains &#12452;&#65020; and &amp;#123; entities.", out.toString());
    }

    @Test
    public void testNumericEntityUnescaperWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        Writer out = new StringWriter();

        String input = "This text contains &amp;#x1F60A; and &amp;#123; entities.";
        CharSequence cs = CharSequence.class.cast(input);
        int index = 0;

        while (index < cs.length()) {
            int length = unescaper.translate(cs, index, out);
            index += length;
        }

        assertThrows(IllegalArgumentException.class, () -> {
            StringWriter out2 = new StringWriter();
            NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(OPTION.semiColonRequired);
            CharSequence cs2 = CharSequence.class.cast("This text contains &amp;#x1F60A; entities.");
            int index2 = 0;

            while (index2 < cs2.length()) {
                int length2 = unescaper2.translate(cs2, index2, out2);
                index2 += length2;
            }
        });
    }

    @Test
    public void testNumericEntityUnescaperWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        Writer out = new StringWriter();

        String input = "This text contains &amp;#x1F60A; and &amp;#123; entities.";
        CharSequence cs = CharSequence.class.cast(input);
        int index = 0;

        while (index < cs.length()) {
            int length = unescaper.translate(cs, index, out);
            index += length;
        }

        assertEquals("This text contains &#12452;&#65020; and &amp;#123; entities.", out.toString());
    }

    @Test
    public void testNumericEntityUnescaperWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        Writer out = new StringWriter();

        String input = "This text contains &amp;#x1F60A; entities.";
        CharSequence cs = CharSequence.class.cast(input);
        int index = 0;

        while (index < cs.length()) {
            int length = unescaper.translate(cs, index, out);
            index += length;
        }

        assertThrows(IllegalArgumentException.class, () -> {
            StringWriter out2 = new StringWriter();
            NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
            CharSequence cs2 = CharSequence.class.cast("This text contains &amp;#x1F60A; entities.");
            int index2 = 0;

            while (index2 < cs2.length()) {
                int length2 = unescaper2.translate(cs2, index2, out2);
                index2 += length2;
            }
        });
    }

    @Test
    public void testEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "";
        CharSequence cs = CharSequence.class.cast(input);
        int index = 0;

        while (index < cs.length()) {
            int length = unescaper.translate(cs, index, out);
            index += length;
        }

        assertEquals("", out.toString());
    }
}