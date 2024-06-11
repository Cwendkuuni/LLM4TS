package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&#160;";

        int lengthBefore = input.length();
        int translatedLength = unescaper.translate(input, 0, output);
        StringBuilder expectedOutput = new StringBuilder((char) 160);

        assertEquals("Unexpected output when translating &#160;", expectedOutput.toString(), output.toString());
        assertEquals("Unexpected translated length when translating &#160;", lengthBefore + 1,
                lengthBefore + translatedLength);
    }

    @Test
    public void testTranslateHexNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&#x201C;";

        int lengthBefore = input.length();
        int translatedLength = unescaper.translate(input, 0, output);
        StringBuilder expectedOutput = new StringBuilder((char) 8222);

        assertEquals("Unexpected output when translating &#x201C;", expectedOutput.toString(), output.toString());
        assertEquals("Unexpected translated length when translating &#x201C;", lengthBefore + 1,
                lengthBefore + translatedLength);
    }

    @Test
    public void testTranslateMixedNumericEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&#{x20AC}123; &#{123}; &#x201D;";

        int lengthBefore = input.length();
        int translatedLength = unescaper.translate(input, 0, output);
        StringBuilder expectedOutput = new StringBuilder((char) 172, (char) 1, (char) 173, (char) 125);

        assertEquals("Unexpected output when translating &#{x20AC}123; &#{123}; &#x201D;",
                expectedOutput.toString(), output.toString());
        assertEquals("Unexpected translated length when translating &#{x20AC}123; &#{123}; &#x201D;",
                lengthBefore + 1 + 2 + 1 + 1, lengthBefore + translatedLength);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&xyz;";

        unescaper.translate(input, 0, output);
    }

    @Test(expected = IOException.class)
    public void testTranslateNullWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&xyz;";

        unescaper.translate(input, 0, null);
    }
}