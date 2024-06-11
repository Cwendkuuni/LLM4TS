package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnscaper.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test translating numeric entities with decimal value.
     */
    @Test
    public void testTranslateDecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#10;Hello &#13;World!";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        String output = out.getBuffer().toString();
        assertEquals("Hello \nWorld!", output.substring(0, length));
    }

    /**
     * Test translating numeric entities with hexadecimal value.
     */
    @Test
    public void testTranslateHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#x2A;Hello &#x61;World!";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        String output = out.getBuffer().toString();
        assertEquals("Hello fWorld!", output.substring(0, length));
    }

    /**
     * Test translating an empty string.
     */
    @Test
    public void testTranslateEmptyString() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(0, length);
    }

    /**
     * Test translating an invalid entity.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&%#10;";
        CharSequence cs = new StringReader(input);
        unescaper.translate(cs, 0, new StringWriter());
    }

    /**
     * Test translating an entity with an out of bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateEntityWithOutOfBoundsIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#[x1F0000];";
        CharSequence cs = new StringReader(input);
        unescaper.translate(cs, 0, new StringWriter());
    }

    /**
     * Test translating an entity with an invalid format.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateEntityWithInvalidFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&&#x1F0000;";
        CharSequence cs = new StringReader(input);
        unescaper.translate(cs, 0, new StringWriter());
    }

    /**
     * Test translating an entity with an invalid character after the semicolon.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateEntityWithInvalidCharacterAfterSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#[x1F0000];X";
        CharSequence cs = new StringReader(input);
        unescaper.translate(cs, 0, new StringWriter());
    }

    /**
     * Test translating entities with a large number of characters.
     */
    @Test
    public void testTranslateEntitiesWithLargeNumberOfCharacters() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("&#x");
            if (i % 2 == 0) {
                sb.append("0");
            } else {
                sb.append("1");
            }
            sb.append("F0000;");
        }
        String input = sb.toString();
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        String output = out.getBuffer().toString();
        assertEquals(input.length(), length);
        // Assert that the output is empty since the entities are invalid
        assertEquals("", output);
    }

    /**
     * Test translating entities with a large number of entities.
     */
    @Test
    public void testTranslateEntitiesWithLargeNumberOfEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("&#[x");
            if (i % 2 == 0) {
                sb.append("0");
            } else {
                sb.append("1");
            }
            sb.append("F0000];");
        }
        String input = sb.toString();
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        String output = out.getBuffer().toString();
        assertEquals(input.length(), length);
        // Assert that the output is empty since the entities are invalid
        assertEquals("", output);
    }
}