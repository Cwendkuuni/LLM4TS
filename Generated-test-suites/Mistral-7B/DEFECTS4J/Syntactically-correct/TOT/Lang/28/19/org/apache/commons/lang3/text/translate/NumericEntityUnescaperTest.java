package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    private static final String ENTITY_WITH_HEX_VALUE = "&#[xA1];";
    private static final String ENTITY_WITH_DEC_VALUE = "&#[41];";
    private static final String ENTITY_WITH_INVALID_FORMAT = "&#[xABC];";
    private static final String ENTITY_WITH_INVALID_CHAR_BEFORE = "&a#[41];";
    private static final String ENTITY_WITH_INVALID_CHAR_AFTER = "&#[41];a";
    private static final String ENTITY_WITH_EMPTY = "&#[];";
    private static final String ENTITY_WITH_MULTIPLE_ENTITIES = "&#[xABCDEF] &#[123];";

    private static final char CHAR_A1 = (char) 0xA1;

    @Test
    public void testUnescapeSingleEntityWithHexValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = String.format("%s%s", ENTITY_WITH_HEX_VALUE, StringUtils.LF);
        Writer output = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
        String outputString = output.toString();
        assertEquals(String.format("%c%s", CHAR_A1, StringUtils.LF), outputString.substring(0, length));
    }

    @Test
    public void testUnescapeSingleEntityWithDecValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = String.format("%s%s", ENTITY_WITH_DEC_VALUE, StringUtils.LF);
        Writer output = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
        String outputString = output.toString();
        assertEquals("A", outputString.substring(0, length));
    }

    @Test(expected = NumberFormatException.class)
    public void testUnescapeEntityWithInvalidFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = String.format("%s%s", ENTITY_WITH_INVALID_FORMAT, StringUtils.LF);
        Writer output = new StringWriter();
        unescaper.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUnescapeEntityWithInvalidCharBefore() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = String.format("%s%s", ENTITY_WITH_INVALID_CHAR_BEFORE, StringUtils.LF);
        Writer output = new StringWriter();
        unescaper.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUnescapeEntityWithInvalidCharAfter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = String.format("%s%s", ENTITY_WITH_INVALID_CHAR_AFTER, StringUtils.LF);
        Writer output = new StringWriter();
        unescaper.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    @Test
    public void testUnescapeEntityWithEmpty() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = String.format("%s%s", ENTITY_WITH_EMPTY, StringUtils.LF);
        Writer output = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
        String outputString = output.toString();
        assertEquals(StringUtils.EMPTY, outputString.substring(0, length));
    }

    @Test
    public void testUnescapeMultipleEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = String.format("%s%s", ENTITY_WITH_MULTIPLE_ENTITIES, StringUtils.LF);
        Writer output = new StringWriter();
        int length = unescaper.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
        String outputString = output.toString();
        assertEquals("A" + StringUtils.LF + "C", outputString.substring(0, length));
    }
}