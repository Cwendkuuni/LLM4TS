package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        // Test valid entities
        String input = "&amp; &lt; &gt; &apos; &quot; &eacute; &nbsp; &cent; &pound; &copy; &yen; &reg; &trade; &egrave; &agrave; &Atilde; &Acirc; &Otilde; &Oelig; &Scaron; &Scaredslash; &Xi; &sigmaf; &fnof; &circ; &tilde; &Ouml; &Uuml; &times; &divide; &micro; &para; &middot; &ensp; &emsp; &quot; &quot; &amp; &quot;";
        char[] expectedOutput = new char[]{'&', '&', '>', '>', '\'', '\'', 'e', '\u00A0', '\u00A2', 'C', '\u00A9', '(C)', 'Y', '\u00AE', '(R)', '(C)', '\u00E0', '\u00C3', '\u00C4', '\u00DC', '\u00D8', '\u00DF', 'x', 'i', 's', 'g', 'm', '\u03C3', '\u03F7', 'x'};
        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; ++i) {
            int translated = unescaper.translate(new StringCharacterIterator(input, index), 0, out);
            index += translated;
            assertEquals(expectedOutput[i], out.toString().charAt(i));
        }

        assertEquals("", out.toString().substring(input.length()));
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        Writer out = new StringWriter();

        String input = "&amp; &lt; &gt; &apos; &quot; &eacute; &nbsp; &cent; &pound; &copy; &yen; &reg; &trade; &egrave; &agrave; &Atilde; &Acirc; &Otilde; &Oelig; &Scaron; &Scaredslash; &Xi; &sigmaf; &fnof; &circ; &tilde; &Ouml; &Uuml; &times; &divide; &micro; &para; &middot; &ensp; &emsp; &quot; &quot; &amp;";
        char[] expectedOutput = new char[]{'&', '&', '>', '>', '\'', '\'', 'e', '\u00A0', '\u00A2', 'C', '\u00A9', '(C)', 'Y', '\u00AE', '(R)', '(C)', '\u00E0', '\u00C3', '\u00C4', '\u00DC', '\u00D8', '\u00DF', 'x', 'i', 's', 'g', 'm', '\u003C', '\u003E', 'x'};

        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; ++i) {
            int translated = unescaper.translate(new StringCharacterIterator(input, index), 0, out);
            index += translated;
            assertEquals(expectedOutput[i], out.toString().charAt(i));
        }

        assertEquals("", out.toString().substring(input.length()));
        assertEquals(";", out.toString().substring(input.length() + 1));
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        Writer out = new StringWriter();

        String input = "&amp; &lt; &gt; &apos; &quot; &eacute; &nbsp; &cent; &pound; &copy; &yen; &reg; &trade; &egrave; &agrave; &Atilde; &Acirc; &Otilde; &Oelig; &Scaron; &Scaredslash; &Xi; &sigmaf; &fnof; &circ; &tilde; &Ouml; &Uuml; &times; &divide; &micro; &para; &middot; &ensp; &emsp; &quot; &quot; &amp;";
        char[] expectedOutput = new char[]{'&', '&', '>', '>', '\'', '\'', 'e', '\u00A0', '\u00A2', 'C', '\u00A9', '(C)', 'Y', '\u00AE', '(R)', '(C)', '\u00E0', '\u00C3', '\u00C4', '\u00DC', '\u00D8', '\u00DF', 'x', 'i', 's', 'g', 'm', '\u003C', '\u003E', 'x'};

        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; ++i) {
            int translated = unescaper.translate(new StringCharacterIterator(input, index), 0, out);
            index += translated;
            assertEquals(expectedOutput[i], out.toString().charAt(i));
        }

        assertTrue(out.toString().substring(input.length()).isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        Writer out = new StringWriter();

        String input = "&amp; &lt; &gt; &apos; &quot; &eacute; &nbsp; &cent; &pound; &copy; &yen; &reg; &trade; &egrave; &agrave; &Atilde; &Acirc; &Otilde; &Oelig; &Scaron; &Scaredslash; &Xi; &sigmaf; &fnof; &circ; &tilde; &Ouml; &Uuml; &times; &divide; &micro; &para; &middot; &ensp; &emsp; &quot; &quot; &amp;";

        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; ++i) {
            unescaper.translate(new StringCharacterIterator(input, index), 0, out);
            index += 2;
        }

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithMalformedEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&amp;#xinvalid &quot;";

        int index = 0;
        int length = input.length();

        try {
            unescaper.translate(new StringCharacterIterator(input, index), 0, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected
        }

        assertEquals("", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNegativeIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&amp;";

        try {
            unescaper.translate(new StringCharacterIterator(input, -1), 0, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected
        }

        assertEquals("", out.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNullWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;";

        try {
            unescaper.translate(new StringCharacterIterator(input, 0), 0, null);
            fail("Expected IOException");
        } catch (IOException ex) {
            // Expected
        }
    }
}