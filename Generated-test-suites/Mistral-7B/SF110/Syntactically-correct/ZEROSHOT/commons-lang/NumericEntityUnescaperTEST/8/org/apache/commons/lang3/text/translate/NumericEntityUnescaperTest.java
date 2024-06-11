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
    public void testDefaultConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional, OPTION.errorIfNoSemiColon);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSet_semiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));

        NumericEntityUnescaper unescaperNoSemiColon = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertFalse(unescaperNoSemiColon.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet_semiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));

        NumericEntityUnescaper unescaperWithSemiColonOptional = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertTrue(unescaperWithSemiColonOptional.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testIsSet_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

        NumericEntityUnescaper unescaperWithErrorIfNoSemiColon = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertTrue(unescaperWithErrorIfNoSemiColon.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslationWithMissingSemiColon_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&num;";
        StringWriter outWriter = new StringWriter();
        CharSequence sequence = new StringSequance(input);
        int length = unescaper.translate(sequence, 0, outWriter);
    }

    @Test
    public void testTranslationWithValidEntities_semiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#10; &lt; &gt; &#xa9; &#x27ef; &#x1f601; &#xFFFD; &#x1F600; &#x1F602;";
        StringWriter outWriter = new StringWriter();
        CharSequence sequence = new StringSequance(input);
        int length = unescaper.translate(sequence, 0, outWriter);
        assertEquals("&amp; &lt; &gt; &#xA9; &#x27EF; &#x1F601; &#xFFFD; &#x1F600; &#x1F602;", outWriter.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslationWithValidEntities_semiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "&amp;#10; &lt; &gt; &#xa9; &#x27ef; &#x1f601; &#xFFFD; &#x1F600; &#x1F602;";
        StringWriter outWriter = new StringWriter();
        CharSequence sequence = new StringSequance(input);
        int length = unescaper.translate(sequence, 0, outWriter);
        assertEquals("&amp; &lt; &gt; &#xA9; &#x27EF; &#x1F601; &#xFFFD; &#x1F600; &#x1F602;", outWriter.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslationWithInvalidEntities_noSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num; &amp;#10; &lt; &gt; &#xa9; &#x27ef; &#x1f601; &#xFFFD; &#x1F600; &#x1F602;";
        StringWriter outWriter = new StringWriter();
        CharSequence sequence = new StringSequance(input);
        int length = unescaper.translate(sequence, 0, outWriter);
        assertEquals("&amp; &lt; &gt; &#xA9; &#x27EF; &#x1F601; &#xFFFD; &#x1F600; &#x1F602;", outWriter.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslationWithHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x61; &amp;#x62;";
        StringWriter outWriter = new StringWriter();
        CharSequence sequence = new StringSequance(input);
        int length = unescaper.translate(sequence, 0, outWriter);
        assertEquals("&amp;ab", outWriter.toString());
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslationWithMixedEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp; #123; &#x456; &amp; #789; &#xABCD; &amp; #10;";
        StringWriter outWriter = new StringWriter();
        CharSequence sequence = new StringSequance(input);
        int length = unescaper.translate(sequence, 0, outWriter);
        assertEquals("&amp; & #123; &#x456; &amp; & #789; &#xABCD; &amp; ", outWriter.toString());
        assertEquals(input.length(), length);
    }
}

class StringSequance implements CharSequence {
    private final String string;

    public StringSequance(String string) {
        this.string = string;
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new StringSequance(string.substring(start, end));
    }
}