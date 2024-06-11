package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

        NumericEntityUnescaper unescaperNoOptions = new NumericEntityUnescaper();
        assertTrue(unescaperNoOptions.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate_SimpleNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp; &lt; &gt; &amp;#128; &amp;#x7f; &amp;#34; &amp;#x26; &amp;#xFFFF;";
        StringWriter output = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int translatedLength = unescaper.translate(new StringReader(input), index, output);
            index += translatedLength;
        }

        String expectedOutput = "& & < > &#128; &#x7f; \" & &";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslate_ComplexNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        String input = "Hello &amp;#x41;&#x41;&#x41; World! &amp;#xFFFF; &amp;#x00A9; &amp;#x2192;";
        StringWriter output = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int translatedLength = unescaper.translate(new StringReader(input), index, output);
            index += translatedLength;
        }

        String expectedOutput = "Hello AAWorld!  ";
        assertEquals(expectedOutput, output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NumericEntityWithoutSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "Hello &amp;#123; World!";
        StringWriter output = new StringWriter();

        int index = 0;
        int translatedLength = unescaper.translate(new StringReader(input), index, output);

        index += translatedLength;

        // This line should throw an exception
        translatedLength = unescaper.translate(new StringReader(input), index, output);
    }

    @Test
    public void testTranslate_NumericEntityWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);

        String input = "Hello &amp;#123; World!";
        StringWriter output = new StringWriter();

        int index = 0;
        String result = "";

        try {
            while (index < input.length()) {
                int translatedLength = unescaper.translate(new StringReader(input), index, output);
                index += translatedLength;
                result += output.get(0);
                output.get(0) = (char) 0; // Clear the buffer
                output.flush();
            }
        } catch (IllegalArgumentException e) {
            result = e.getMessage();
        }

        assertEquals("Semi-colon required at end of numeric entity", result);
    }
}

class StringReader extends Reader {

    private final String data;

    public StringReader(String data) {
        this.data = data;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (len < 0) {
            throw new IllegalArgumentException("Length: " + len);
        }

        if (off < 0) {
            throw new IllegalArgumentException("Offset: " + off);
        }

        if (cbuf == null) {
            throw new NullPointerException();
        }

        int read = 0;
        if (off + len > data.length()) {
            len = data.length() - off;
        }

        if (len > 0) {
            System.arraycopy(data.substring(off).toCharArray(), 0, cbuf, 0, len);
            read = len;
        }

        return read;
    }

    @Override
    public void close() throws IOException {
        // Do nothing
    }
}