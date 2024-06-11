package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructorWithOptions() {
        final Option[] options = new Option[] { Option.semiColonRequired, Option.errorIfNoSemiColon };
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(options);
        final EnumSet<NumericEntityUnescaper.OPTION> expectedOptions = EnumSet.copyOf(Arrays.asList(options));
        assertTrue(unescaper.isSet(Option.semiColonRequired));
        assertTrue(unescaper.isSet(Option.errorIfNoSemiColon));
        assertEquals(expectedOptions, unescaper.options);
    }

    @Test
    public void testConstructorWithoutOptions() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final EnumSet<NumericEntityUnescaper.OPTION> expectedOptions = EnumSet.copyOf(Arrays.asList(Option.semiColonRequired));
        assertTrue(unescaper.isSet(Option.semiColonRequired));
        assertFalse(unescaper.isSet(Option.errorIfNoSemiColon));
        assertEquals(expectedOptions, unescaper.options);
    }

    @Test
    public void testIsSetWithValidOption() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(Option.semiColonRequired));
    }

    @Test
    public void testIsSetWithInvalidOption() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(Option.semiColonOptional));
    }

    @Test
    public void testTranslateWithValidNumericEntity() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(output);
        final String input = "This text contains a &num;1234 entity.";
        final int index = input.indexOf("&num;");
        final char[] chars = new char[input.length()];
        System.arraycopy(input.toCharArray(), 0, chars, 0, input.length());
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final int length = unescaper.translate(new String(chars, 0, input.length()), index, writer);
        writer.close();
        final byte[] bytes = output.toByteArray();
        final String expectedOutput = "This text contains a 1234 entity.";
        final String actualOutput = new String(bytes, 0, length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithValidHexNumericEntity() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(output);
        final String input = "This text contains a &#x1F449; entity.";
        final int index = input.indexOf("&#x");
        final char[] chars = new char[input.length()];
        System.arraycopy(input.toCharArray(), 0, chars, 0, input.length());
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final int length = unescaper.translate(new String(chars, 0, input.length()), index, writer);
        writer.close();
        final byte[] bytes = output.toByteArray();
        final String expectedOutput = "This text contains a 🤪 entity.";
        final String actualOutput = new String(bytes, 0, length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithMissingSemiColon() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(output);
        final String input = "This text contains a &num;1234";
        final int index = input.indexOf("&num;");
        final char[] chars = new char[input.length()];
        System.arraycopy(input.toCharArray(), 0, chars, 0, input.length());
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(Option.errorIfNoSemiColon);
        unescaper.translate(new String(chars, 0, input.length()), index, writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithInvalidNumericEntity() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(output);
        final String input = "This text contains a &num#1234";
        final int index = input.indexOf("&num#");
        final char[] chars = new char[input.length()];
        System.arraycopy(input.toCharArray(), 0, chars, 0, input.length());
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(new String(chars, 0, input.length()), index, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateWithInvalidHexValue() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(output);
        final String input = "This text contains a &#xABC; entity.";
        final int index = input.indexOf("&#x");
        final char[] chars = new char[input.length()];
        System.arraycopy(input.toCharArray(), 0, chars, 0, input.length());
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(new String(chars, 0, input.length()), index, writer);
    }

    @Test
    public void testTranslateWithLongNumericEntity() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(output);
        final String input = "This text contains a &num;1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890 entity.";
        final int index = input.indexOf("&num;");
        final char[] chars = new char[input.length()];
        System.arraycopy(input.toCharArray(), 0, chars, 0, input.length());
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(new String(chars, 0, input.length()), index, writer);
        writer.close();
        final byte[] bytes = output.toByteArray();
        final String expectedOutput = "This text contains a 123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890 entity.";
        final String actualOutput = new String(bytes, 0, bytes.length);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithSemiColonOptional() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(output);
        final String input = "This text contains a &num;1234; entity.";
        final int index = input.indexOf("&num;");
        final char[] chars = new char[input.length()];
        System.arraycopy(input.toCharArray(), 0, chars, 0, input.length());
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(Option.semiColonOptional);
        final int length = unescaper.translate(new String(chars, 0, input.length()), index, writer);
        writer.close();
        final byte[] bytes = output.toByteArray();
        final String expectedOutput = "This text contains a 1234 entity.";
        final String actualOutput = new String(bytes, 0, length);
        assertEquals(expectedOutput, actualOutput);
    }
}