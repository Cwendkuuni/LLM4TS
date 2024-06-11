package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        // Test translating a simple numeric entity
        String input = "&#123;";
        int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals("123", output.toString());
        assertEquals(1, length);

        // Test translating a hexadecimal numeric entity
        input = "&#x7F";
        output.setLength(0);
        length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals("\ufffd", output.toString());
        assertEquals(1, length);

        // Test translating an entity with no following semicolon
        input = "&#123";
        output.setLength(0);
        length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals("", output.toString());
        assertEquals(0, length);

        // Test translating an entity with invalid syntax
        input = "&invalidEntity;";
        output.setLength(0);
        length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals("", output.toString());
        assertEquals(0, length);

        // Test translating an empty string
        input = "";
        output.setLength(0);
        length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals("", output.toString());
        assertEquals(0, length);
    }
}