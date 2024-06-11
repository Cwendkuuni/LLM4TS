package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        String input = "&amp;#123;&#123;abcd;&#x61;efgh;&#x41;&#x65;";
        int length = unescaper.translate(new StringSource(input), 0, new StringWriter(output));
        assertEquals(input.length(), length);
        assertEquals("abcdefghij", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        String input = "&amp;#xz"; // z is not a valid hex digit
        int length = unescaper.translate(new StringSource(input), 0, new StringWriter(output));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidNumericEntityWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        String input = "&amp;#123";
        int length = unescaper.translate(new StringSource(input), 0, new StringWriter(output));
    }

    @Test
    public void testTranslateMultiNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        String input = "&amp;#123;&#x61;&#125;&#x62;&#x63;";
        int length = unescaper.translate(new StringSource(input), 0, new StringWriter(output));
        assertEquals(input.length(), length);
        assertEquals("abc", output.toString());
    }

    @Test
    public void testTranslateLargeNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        String input = "&amp;#65533;"; // Character.MAX_VALUE
        int length = unescaper.translate(new StringSource(input), 0, new StringWriter(output));
        assertEquals(input.length(), length);
        assertEquals("\ufffd", output.toString());
    }

    @Test
    public void testTranslateSurrogatePair() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        String input = "&amp;#x10FF;&#x1080;"; // U+10FF U+1080: Dingbat
        int length = unescaper.translate(new StringSource(input), 0, new StringWriter(output));
        assertEquals(input.length(), length);
        assertEquals("\ud83d\ude00", output.toString()); // Dingbat
    }
}