package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    private static final String TEST_INPUT = "&amp;#x41;&#x42;&#x61;&#x64;&#x73;&#x61;&#x72;&#x65;&#x74; &#x4E;&#x65;&#x6E;&#x63;&#x6F;&#x6D;&#x65;&#x6E;&amp; #&x5C;&#x61;&#x62;&#x65;&#x63;&#x74;&#x26;#x26;#x27; &#xA9;";
    private static final String TEST_OUTPUT = "ABacde & C\"& | ©";

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        CharSequence input = CharSequence.class.cast(TEST_INPUT);

        int translatedLength = unescaper.translate(input, 0, writer);
        String translatedOutput = writer.toString();

        assertEquals(TEST_OUTPUT, translatedOutput);
        assertEquals(TEST_OUTPUT.length(), translatedLength);
    }

    @Test
    public void testTranslateWithOptions() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        Writer writer = new StringWriter();
        CharSequence input = CharSequence.class.cast(TEST_INPUT);

        int translatedLength = unescaper.translate(input, 0, writer);
        String translatedOutput = writer.toString();

        assertEquals(TEST_OUTPUT + ";", translatedOutput);
        assertEquals(TEST_OUTPUT.length() + 1, translatedLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        Writer writer = new StringWriter();
        CharSequence input = CharSequence.class.cast("&amp;#x41;&#x42;&#x61;&#x64;&#x73;&#x61;&#x72;&#x65;&#x74;");

        int translatedLength = unescaper.translate(input, 0, writer);
        String translatedOutput = writer.toString();

        fail("An IllegalArgumentException was expected but not thrown");
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));

        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaperWithOptions.isSet(OPTION.semiColonRequired));
    }
}