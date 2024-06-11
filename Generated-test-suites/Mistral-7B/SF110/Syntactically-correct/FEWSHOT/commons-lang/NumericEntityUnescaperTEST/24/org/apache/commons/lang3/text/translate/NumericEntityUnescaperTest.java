package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NoOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#160; &amp;#32; &amp;#173; &amp;#x2019;".toCharArray();
        int index = 0;

        int length = unescaper.translate(new String(input), index, out);
        assertEquals("&amp; < > &nbsp; &nbsp; ’", out.toString());
        assertEquals(13, length);
    }

    @Test
    public void testTranslate_SemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#160; &amp;#32; &amp;#173; &amp;#x2019; &amp;#123;".toCharArray();
        int index = 0;

        int length = unescaper.translate(new String(input), index, out);
        assertEquals("&amp; < > &nbsp; &nbsp; ’ {", out.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_SemiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#160; &amp;#32; &amp;#173; &amp;#x2019; &amp;#123; & #125;".toCharArray();
        int index = 0;

        int length = unescaper.translate(new String(input), index, out);
        assertEquals("&amp; < > &nbsp; &nbsp; ’ {  }", out.toString());
        assertEquals(16, length);
    }

    @Test
    public void testTranslate_ErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#160; &amp;#32; &amp;#173; &amp;#x2019; &amp;#123;".toCharArray();
        int index = 0;

        try {
            unescaper.translate(new String(input), index, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTranslate_Hex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#x20AC;".toCharArray();
        int index = 0;

        int length = unescaper.translate(new String(input), index, out);
        assertEquals("&amp; < > €", out.toString());
        assertEquals(4, length);
    }

    @Test
    public void testTranslate_InvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#xyz;".toCharArray();
        int index = 0;

        int length = unescaper.translate(new String(input), index, out);
        assertEquals("&amp; < >", out.toString());
        assertEquals(6, length);
    }

    @Test
    public void testTranslate_LargeEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#65536;".toCharArray();
        int index = 0;

        int length = unescaper.translate(new String(input), index, out);
        assertEquals("&amp; < > 𠀀", out.toString());
        assertEquals(4, length);
    }

    @Test
    public void testTranslate_LargeEntityWithSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();

        char[] input = "&amp; &lt; &gt; &amp;#65536; & #65537;".toCharArray();
        int index = 0;

        int length = unescaper.translate(new String(input), index, out);
        assertEquals("&amp; < > 𠀀 𠀁", out.toString());
        assertEquals(8, length);
    }
}