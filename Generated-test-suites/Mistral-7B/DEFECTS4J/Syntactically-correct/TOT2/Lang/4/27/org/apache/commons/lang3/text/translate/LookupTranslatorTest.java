package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        // Testing with empty lookup table
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{});
        assertNull(lookupTranslator.lookupMap);

        // Testing with a non-empty lookup table
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple pie"}, {"banana", "banana bread"}};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup);
        assertEquals(lookup.length, lookupTranslator.lookupMap.size());
    }

    @Test
    public void testTranslate() throws IOException {
        // Testing with an empty lookup table
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{});
        String input = "";
        Writer out = new StringWriter();
        lookupTranslator.translate(input, 0, out);
        assertEquals("", out.toString());

        // Testing with a non-empty lookup table
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple pie"}, {"banana", "banana bread"}};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup);
        String input1 = "apple";
        Writer out = new StringWriter();
        lookupTranslator.translate(input1, 0, out);
        assertEquals("apple pie", out.toString());

        String input2 = "banana";
        lookupTranslator.translate(input2, 0, out);
        assertEquals("banana bread", out.toString());

        String input3 = "app";
        lookupTranslator.translate(input3, 0, out);
        assertEquals("", out.toString());

        String input4 = "appler";
        lookupTranslator.translate(input4, 0, out);
        assertEquals("apple pie", out.toString());

        String input5 = "bananab";
        lookupTranslator.translate(input5, 0, out);
        assertEquals("banana bread", out.toString());

        String input6 = "bananaapp";
        lookupTranslator.translate(input6, 0, out);
        assertEquals("banana breadapple pie", out.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullInput() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{{"apple", "apple pie"}, {"banana", "banana bread"}});
        String input = null;
        Writer out = new StringWriter();
        lookupTranslator.translate(input, 0, out);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{{"apple", "apple pie"}, {"banana", "banana bread"}});
        String input = "";
        Writer out = new StringWriter();
        lookupTranslator.translate(input, 0, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithInputStringShorterThanMinimumLength() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[][]{{"apple", "apple pie"}, {"banana", "banana bread"}});
        String input = "ap";
        Writer out = new StringWriter();
        lookupTranslator.translate(input, 0, out);
        assertEquals("", out.toString());
    }
}