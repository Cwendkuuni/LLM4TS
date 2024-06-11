package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &num;1234 test &amp; &nbsp; &amp;#160; &amp;#x304; &amp;#x00A9; &amp;#x20AC; &amp;#xFFFC; &amp;#xFFFE;";
        StringWriter output = new StringWriter();
        CharSequence sequence = CharSequenceUtils.subsequence(input, 0, input.length() - 1); // exclude the last ';'

        int length = unescaper.translate(sequence, 0, output);
        assertEquals(input.length(), length);
        assertEquals("This is a 1234 test &amp; &nbsp; &#160; &#342; &#169; &#8364; &#xFFFC; &#xFFFE;", output.toString());
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "This is a &num;1234 test &amp; &nbsp; &amp;#160; &amp;#x304; &amp;#x00A9; &amp;#x20AC; &amp;#xFFFC; &amp;#xFFFE;";
        StringWriter output = new StringWriter();
        CharSequence sequence = CharSequenceUtils.subsequence(input, 0, input.length() - 1); // exclude the last ';'

        int length = unescaper.translate(sequence, 0, output);
        assertEquals(input.length(), length);
        assertEquals("This is a 1234 test &amp; &nbsp; &#160; &#342; &#169; &#8364; &#226; &#169; &#172; &#168; &#165; &#164; &#163; &#162; &#161;", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOptionAndMissingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "This is a &num;1234 test &amp; &nbsp; &amp;#160; &amp;#x304; &amp;#x00A9; &amp;#x20AC; &amp;#xFFFC; &amp;#xFFFE;";
        StringWriter output = new StringWriter();
        CharSequence sequence = CharSequenceUtils.subsequence(input, 0, input.length() - 1); // exclude the last ';'

        int length = unescaper.translate(sequence, 0, output);
        fail("Expected IllegalArgumentException but it did not occur.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOptionAndValidEntityWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "This is a &num;1234 test &amp; &nbsp; &amp;#160; &amp;#x304; &amp;#x00A9; &amp;#x20AC; &amp;#xFFFC &amp; &amp;#xFFFE;";
        StringWriter output = new StringWriter();
        CharSequence sequence = CharSequenceUtils.subsequence(input, 0, input.length() - 1); // exclude the last ';'

        int length = unescaper.translate(sequence, 0, output);
        fail("Expected IllegalArgumentException but it did not occur.");
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "This is a &num;1234 test &amp; &nbsp; &amp;#160; &amp;#x304; &amp;#x00A9; &amp;#x20AC; &amp;#xFFFC; &amp;#xFFFE;";
        StringWriter output = new StringWriter();
        CharSequence sequence = CharSequenceUtils.subsequence(input, 0, input.length() - 1); // exclude the last ';'

        int length = unescaper.translate(sequence, 0, output);
        assertEquals(input.length(), length);
        assertEquals("This is a 1234 test &amp; &nbsp; &#160; &#342; &#169; &#8364; &#226; &#169; &#172; &#168; &#165; &#164; &#163; &#162; &#161;", output.toString());
    }

    @Test
    public void testTranslateWithMultipleEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &num;1234 test &amp; &nbsp; &amp;#160; &amp;#x304; &amp;#x00A9; &amp;#x20AC; &amp;#xFFFC; &amp;#xFFFE; &amp;#x0393; &amp;#x03B2; &amp;#x03B4; &amp;#x03C0; &amp;#x03C3;";
        StringWriter output = new StringWriter();
        CharSequence sequence = CharSequenceUtils.subsequence(input, 0, input.length() - 1); // exclude the last ';'

        int length = unescaper.translate(sequence, 0, output);
        assertEquals(input.length(), length);
        assertEquals("This is a 1234 test &amp; &nbsp; &#160; &#342; &#169; &#8364; &#351; &#382; &#384; &#360; &#363;", output.toString());
    }
}