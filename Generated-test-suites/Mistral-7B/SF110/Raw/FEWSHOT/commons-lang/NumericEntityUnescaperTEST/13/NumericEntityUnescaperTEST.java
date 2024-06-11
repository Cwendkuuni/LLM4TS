package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder out = new StringBuilder();
        CharSequence input = "This text contains &amp;#x1F603;&#xA9; &nbsp;&#x2013; This is a test.";

        int translatedChars = unescaper.translate(input, 0, out);

        assertEquals("This text contains &#128515; &#169; &nbsp;&#x2013; This is a test.", out.toString());
        assertEquals(input.length(), translatedChars);
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        StringBuilder out = new StringBuilder();
        CharSequence input = "This text contains &amp;#x1F603; &nbsp;&#x2013; This is a test.";

        int translatedChars = unescaper.translate(input, 0, out);

        assertEquals("This text contains &#128515; &nbsp;&#x2013; This is a test.", out.toString());
        assertEquals(input.length(), translatedChars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);

        StringBuilder out = new StringBuilder();
        CharSequence input = "This text contains &amp;#x1F603; This is a test.";

        int translatedChars = unescaper.translate(input, 0, out);

        fail("IllegalArgumentException should have been thrown.");
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);

        StringBuilder out = new StringBuilder();
        CharSequence input = "This text contains &amp;#x1F603; This is a test.";

        int translatedChars = unescaper.translate(input, 0, out);

        assertEquals("This text contains &#128515; This is a test.", out.toString());
        assertEquals(input.length(), translatedChars);
    }

    @Test
    public void testTranslateWithHexNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder out = new StringBuilder();
        CharSequence input = "This text contains &amp;#x1F603;";

        int translatedChars = unescaper.translate(input, 0, out);

        assertEquals("This text contains &#128515;", out.toString());
        assertEquals(input.length(), translatedChars);
    }

    @Test
    public void testTranslateWithDecNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder out = new StringBuilder();
        CharSequence input = "This text contains &amp;#2632;";

        int translatedChars = unescaper.translate(input, 0, out);

        assertEquals("This text contains &#10098;", out.toString());
        assertEquals(input.length(), translatedChars);
    }
}