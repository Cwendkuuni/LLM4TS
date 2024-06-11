package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));

        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaperWithOptions.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslateWithNoEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "Hello World!";
        int translatedLength = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(input.length(), translatedLength);
        assertEquals(input, out.toString());
    }

    @Test
    public void testTranslateWithSingleEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;";
        int translatedLength = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(1, translatedLength);
        assertEquals("&", out.toString());
    }

    @Test
    public void testTranslateWithSingleEntityHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&#x41;";
        int translatedLength = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(1, translatedLength);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateWithMultipleEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "Hello &amp;world! This is &lt;strong&gt;some &#x41;&#x42;&#x43; text&lt;/strong&gt;.";
        int translatedLength = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(input.length(), translatedLength);
        assertEquals(input, out.toString());
    }

    @Test
    public void testTranslateWithMissingSemiColon() throws IOException {
        NumericEntityUnescaper unescaperWithErrorIfNoSemiColon = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        String input = "Hello &amp;world! This is &lt;strong&gt;some &#x41;&#x42; text";
        assertThrows(IllegalArgumentException.class, () -> unescaperWithErrorIfNoSemiColon.translate(new StringReader(input), 0, out));
    }

    @Test
    public void testTranslateWithMissingSemiColonOptional() throws IOException {
        NumericEntityUnescaper unescaperWithSemiColonOptional = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        String input = "Hello &amp;world! This is &lt;strong&gt;some &#x41;&#x42; text";
        int translatedLength = unescaperWithSemiColonOptional.translate(new StringReader(input), 0, out);
        assertEquals(input.length(), translatedLength);
        assertEquals(input, out.toString());
    }

    @Test
    public void testTranslateWithMissingSemiColonRequired() throws IOException {
        NumericEntityUnescaper unescaperWithSemiColonRequired = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        String input = "Hello &amp;world! This is &lt;strong&gt;some &#x41;&#x42; text";
        assertThrows(IllegalArgumentException.class, () -> unescaperWithSemiColonRequired.translate(new StringReader(input), 0, out));
    }
}