package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testConstructorWithoutOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet_whenOptionIsSet_returnsTrue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet_whenOptionIsNotSet_returnsFalse() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate_whenInputIsEmpty_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_whenInputDoesNotContainEntity_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Hello World";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_whenInputContainsValidEntity_translatesCorrectly() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Hello &amp;#x41;ld Hello";
        int result = unescaper.translate(input, 0, out);
        assertEquals("Hello ÀLD Hello", out.toString());
        assertEquals(13, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_whenInputContainsEntityWithoutSemiColon_throwsExceptionWhenRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        CharSequence input = "Hello &amp;#x41;";
        int result = unescaper.translate(input, 0, out);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testTranslate_whenInputContainsEntityWithoutSemiColon_doesNotThrowExceptionWhenOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        CharSequence input = "Hello &amp;#x41;";
        int result = unescaper.translate(input, 0, out);
        assertEquals("Hello Á", out.toString());
        assertEquals(3, result);
    }

    @Test
    public void testTranslate_whenInputContainsInvalidEntity_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Hello &amp;#x5000;ld Hello";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_whenInputContainsEntityWithLargeValue_translatesCorrectly() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence input = "Hello &amp;#100000;ld Hello";
        int result = unescaper.translate(input, 0, out);
        assertEquals("Hello ♥ heart ♥ Hello", out.toString());
        assertEquals(14, result);
    }

    @Test
    public void testTranslate_whenInputContainsEntityWithLargeValueAndSemiColon_translatesCorrectly() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        CharSequence input = "Hello &amp;#100000;ld Hello";
        int result = unescaper.translate(input, 0, out);
        assertEquals("Hello ♥ heart ♥\n", out.toString());
        assertEquals(15, result);
    }
}