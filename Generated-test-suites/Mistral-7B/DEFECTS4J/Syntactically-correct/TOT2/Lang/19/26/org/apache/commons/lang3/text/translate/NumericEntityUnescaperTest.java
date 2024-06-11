package org.apache.commons.lang3.text.translate;

import org.apache.commons.io.StringReader;
import org.apache.commons.io.StringWriter;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testDecodeDecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;#128;&#160;&#161;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("Åäå", writer.toString());
        assertEquals(11, length);
    }

    @Test
    public void testDecodeHexadecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;#x41;&#x42;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("AB", writer.toString());
        assertEquals(3, length);
    }

    @Test
    public void testDecodeEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, length);
    }

    @Test
    public void testDecodeInputWithLeadingAndTrailingWhitespaces() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = " &amp;#128;&#160;&#161; ";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("Åäå", writer.toString());
        assertEquals(11, length);
    }

    @Test
    public void testDecodeMultipleEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;#128;&#160;&#161;&amp;#171;&#172;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        assertEquals("ÅäåRT", writer.toString());
        assertEquals(13, length);
    }

    @Test(expected = NumberFormatException.class)
    public void testDecodeInvalidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;#abc;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        fail();
    }

    @Test(expected = NumberFormatException.class)
    public void testDecodeInvalidHexadecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;#xz;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        fail();
    }

    @Test(expected = NumberFormatException.class)
    public void testDecodeInvalidEntityWithSemicolonMissing() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;#x41";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        fail();
    }

    @Test(expected = NumberFormatException.class)
    public void testDecodeInvalidEntityWithLeadingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = ";&amp;#x41";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        fail();
    }

    @Test(expected = IOException.class)
    public void testDecodeIOException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };

        String input = "&amp;#128;&#160;&#161;";
        int length = unescaper.translate(new StringReader(input), 0, writer);

        fail();
    }
}