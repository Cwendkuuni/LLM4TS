package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test cases for {@link NumericEntityUnescaper} class.
 *
 * @author John Doe
 */
public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslateSimpleNumericEntities() {
        // Test simple numeric entities
        String input = "&amp; &lt; &gt; &apos; &quot; &#34; &#39; &#60; &#62; &#123; &#125;";
        String expectedOutput = "&amp; &lt; &gt; &#34; &#39; &#60; &#62; {@123};{125}";
        testTranslate(input, expectedOutput);
    }

    @Test
    public void testTranslateHexNumericEntities() {
        // Test hex numeric entities
        String input = "&#x41; &#x61; &#x62; &#x26;";
        String expectedOutput = "Aa bc &amp;";
        testTranslate(input, expectedOutput);
    }

    @Test(expected = RuntimeException.class)
    public void testTranslateInvalidNumericEntity() {
        // Test invalid numeric entity
        String input = "&#Invalid;";
        String output = "";
        unescaper.translate(new StringBuffer(input), 0, new StringWriter(output));
    }

    @Test
    public void testTranslateLargeNumericEntity() {
        // Test large numeric entity
        String input = "&#[1114111];";
        String expectedOutput = "☺";
        testTranslate(input, expectedOutput);
    }

    @Test
    public void testTranslateNumericEntitiesWithSemicolonMissing() {
        // Test numeric entities with semicolon missing
        String input = "&amp; #65 #x41";
        String expectedOutput = "&amp; A";
        testTranslate(input, expectedOutput);
    }

    @Test
    public void testTranslateNumericEntitiesWithSemicolon() {
        // Test numeric entities with semicolon
        String input = "&amp; #65; #x41;";
        String expectedOutput = "&amp; A; A";
        testTranslate(input, expectedOutput);
    }

    private void testTranslate(String input, String expectedOutput) {
        StringWriter output = new StringWriter();
        unescaper.translate(new StringBuffer(input), 0, output);
        assertEquals(expectedOutput, output.toString());
    }

}