package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        // Test simple numeric entity
        CharSequence input = "&#123;";
        int length = unescaper.translate(input, 0, out);
        assertEquals("Expect length = 1", 1, length);
        assertEquals("Expect output = &#x7B", out.toString(), "#x7B");

        // Test hexadecimal numeric entity
        input = "&#x1F&";
        length = unescaper.translate(input, 0, out);
        assertEquals("Expect length = 2", 2, length);
        assertEquals("Expect output = ï»,", out.toString(), "\u00FE\u00BB");

        // Test error when invalid numeric entity
        input = "&#abc;";
        length = unescaper.translate(input, 0, out);
        assertEquals("Expect length = 0", 0, length);

        // Test error when out of bounds
        input = "&test&";
        length = unescaper.translate(input, 2, out);
        assertEquals("Expect length = 0", 0, length);
    }
}