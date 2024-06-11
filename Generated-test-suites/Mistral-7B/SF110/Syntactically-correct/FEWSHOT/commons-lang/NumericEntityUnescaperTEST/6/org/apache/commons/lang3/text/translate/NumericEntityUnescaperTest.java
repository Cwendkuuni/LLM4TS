package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNewInstanceWithNoOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertNull(unescaper.options);
    }

    @Test
    public void testNewInstanceWithOptions() {
        OPTION[] options = { OPTION.semiColonRequired, OPTION.errorIfNoSemiColon };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        assertEquals(Arrays.asList(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon), Arrays.asList(unescaper.options.toArray(new OPTION[0])));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testNewInstanceWithSingleOption() {
        OPTION[] options = { OPTION.semiColonOptional };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        assertEquals(Arrays.asList(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon), Arrays.asList(unescaper.options.toArray(new OPTION[0])));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSetUnsetOptions() {
        OPTION[] options = { OPTION.semiColonRequired };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSetSetOptions() {
        OPTION[] options = { OPTION.semiColonRequired, OPTION.errorIfNoSemiColon };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslateNoEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello & World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello & World!", input.subSequence(0, input.length()));
        assertEquals("", output.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateSingleCharacterEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &amp; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello & World!", input.subSequence(0, input.length()));
        assertEquals("Hello & World!", output.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateSingleCharacterHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#x26; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello & World!", input.subSequence(0, input.length()));
        assertEquals("Hello & World!", output.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateSingleCharacterDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#x0041; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello & World!", input.subSequence(0, input.length()));
        assertEquals("Hello A World!", output.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateMultiCharacterEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#x0061;&#x0062;&#x0063; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello abc World!", input.subSequence(0, input.length()));
        assertEquals("Hello abc World!", output.toString());
        assertEquals(input.length(), length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMissingSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#x0061;";
        unescaper.translate(input, 0, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMissingSemiColonWithErrorOption() throws IOException {
        OPTION[] options = { OPTION.errorIfNoSemiColon };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#x0061;";
        unescaper.translate(input, 0, output);
    }

    @Test
    public void testTranslateMultiCharacterHexEntityWithSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#x0061;&#x0062;&#x0063;; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello abc; World!", input.subSequence(0, input.length()));
        assertEquals("Hello abc; World!", output.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateMultiCharacterDecimalEntityWithSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#0061;&#0062;&#0063;; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello abc; World!", input.subSequence(0, input.length()));
        assertEquals("Hello abc; World!", output.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateMultiCharacterEntityWithSemiColonAndOptions() throws IOException {
        OPTION[] options = { OPTION.semiColonRequired };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#0061;&#0062;&#0063; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello abc World!", input.subSequence(0, input.length()));
        assertEquals("Hello abc World!", output.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateMultiCharacterEntityWithMissingSemiColonAndOptions() throws IOException {
        OPTION[] options = { OPTION.errorIfNoSemiColon };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#0061;&#0062;&#0063";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello &#0061;&#0062;&#0063", input.subSequence(0, input.length()));
        assertEquals("", output.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslateMultiCharacterEntityWithSemiColonAndOptionsDifferentOrder() throws IOException {
        OPTION[] options = { OPTION.semiColonOptional, OPTION.errorIfNoSemiColon };
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.values());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharSequence input = "Hello &#0061;&#0062;&#0063; World!";
        int length = unescaper.translate(input, 0, output);
        assertEquals("Hello abc World!", input.subSequence(0, input.length()));
        assertEquals("Hello abc World!", output.toString());
        assertEquals(input.length(), length);
    }
}