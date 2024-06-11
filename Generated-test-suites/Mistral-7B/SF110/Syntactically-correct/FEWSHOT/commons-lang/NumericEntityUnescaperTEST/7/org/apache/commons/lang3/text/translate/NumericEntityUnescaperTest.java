package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithNoOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp; &lt; &gt; &apos; &quot; &euro; &pound; &cent; &yen; &copy; &reg;";
        String expectedOutput = "&amp; &lt; &gt; &apos; &quot; &#8364; &#163; &#162; &#165; &#169;";
        Writer out = new StringWriter();
        CharSequence inputSequence = new StringReader(input).chars().collect(Collectors.toList()).asSubList(0, input.length());

        int translatedChars = unescaper.translate(inputSequence, 0, out);

        assertEquals(expectedOutput, out.toString());
        assertEquals(expectedOutput.length(), translatedChars);
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "&amp; &lt; &gt; &apos; &quot; &euro; &pound; &cent; &yen; &copy; &reg;";
        String expectedOutput = "&amp; &lt;&gt; &apos;&apos; &#8364; &#163; &#162; &#165; &#169;";
        Writer out = new StringWriter();
        CharSequence inputSequence = new StringReader(input).chars().collect(Collectors.toList()).asSubList(0, input.length());

        int translatedChars = unescaper.translate(inputSequence, 0, out);

        assertEquals(expectedOutput, out.toString());
        assertEquals(expectedOutput.length(), translatedChars);
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "&amp; &lt; &gt; &apos; &quot; &euro; &pound; &cent; &yen; &copy; &reg;";
        String expectedOutput = "&amp; &lt; &gt; &apos;&apos; &#8364; &#163; &#162; &#165; &#169;";
        Writer out = new StringWriter();
        CharSequence inputSequence = new StringReader(input).chars().collect(Collectors.toList()).asSubList(0, input.length());

        int translatedChars = unescaper.translate(inputSequence, 0, out);

        assertEquals(expectedOutput, out.toString());
        assertEquals(expectedOutput.length(), translatedChars);
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&amp; &lt; &gt; &apos; &quot; &euro; &pound; &cent; &yen;";
        Writer out = new StringWriter();

        assertThrows(IllegalArgumentException.class, () -> {
            CharSequence inputSequence = new StringReader(input).chars().collect(Collectors.toList()).asSubList(0, input.length());
            unescaper.translate(inputSequence, 0, out);
        });
    }

    @Test
    public void testTranslateWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp; #x41 #x42 &apos; &quot; &euro; &pound; &cent; &yen; &copy; &reg; &#xFFF0;";
        String expectedOutput = "&amp; A B &apos;&quot; &#8364; &#163; &#162; &#165; &#169; &#xFFF0;";
        Writer out = new StringWriter();
        CharSequence inputSequence = new StringReader(input).chars().collect(Collectors.toList()).asSubList(0, input.length());

        int translatedChars = unescaper.translate(inputSequence, 0, out);

        assertEquals(expectedOutput, out.toString());
        assertEquals(expectedOutput.length(), translatedChars);
    }

    @Test
    public void testTranslateWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp; &lt; &gt; &apos; &quot; &euro; &pound; &cent; &yen; &copy; &reg; &#xABC;";
        Writer out = new StringWriter();

        int translatedChars = unescaper.translate(new StringReader(input).chars().collect(Collectors.toList()).asSubList(0, input.length()), 0, out);

        assertEquals(0, translatedChars);
        assertEquals("", out.toString());
    }
}