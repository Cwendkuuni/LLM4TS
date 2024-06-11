package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new IdentityTranslator();
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty CharSequence.
     * @throws IOException
     */
    @Test
    public void testTranslateEmptyCharSequence() throws IOException {
        StringWriter stringWriter = new StringWriter();
        int consumed = translator.translate(CharSequenceUtils.EMPTY_CHAR_SEQUENCE, 0, stringWriter);
        assertEquals("", stringWriter.toString());
        assertEquals(0, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer.
     * @expected: IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequence cs = "test";
        translator.translate(cs, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with normal CharSequence.
     * @throws IOException
     */
    @Test
    public void testTranslateNormalCharSequence() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CharSequence cs = "test";
        int consumed = translator.translate(cs, 0, stringWriter);
        String expected = cs.toString();
        assertEquals(expected, stringWriter.toString());
        assertEquals(cs.length(), consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with CharSequence containing surrogate pairs.
     * @throws IOException
     */
    @Test
    public void testTranslateCharSequenceWithSurrogatePairs() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CharSequence cs = "\uD800\uDC00\uD800\uDFFF";
        int consumed = translator.translate(cs, 0, stringWriter);
        String expected = "\uD800\uDC00\uD800\uDFFF";
        assertEquals(expected, stringWriter.toString());
        assertEquals(cs.length() * 2, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with empty CharSequence.
     */
    @Test
    public void testTranslateEmptyCharSequenceToStr() {
        String expected = "";
        String result = translator.translate(CharSequenceUtils.EMPTY_CHAR_SEQUENCE);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null CharSequence.
     * @expected: NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequence() {
        translator.translate(null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with normal CharSequence.
     */
    @Test
    public void testTranslateNormalCharSequenceToStr() {
        String expected = "test";
        String result = translator.translate(new String("test"));
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null CharSequence.
     * @expected: NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequenceToWriter() {
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty Writer.
     * @expected: IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateEmptyWriter() {
        CharSequence cs = "test";
        translator.translate(cs, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with normal writer.
     * @throws IOException
     */
    @Test
    public void testTranslateNormalCharSequenceToWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CharSequence cs = "test";
        translator.translate(cs, stringWriter);
        String expected = cs.toString();
        assertEquals(expected, stringWriter.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with CharSequence containing surrogate pairs.
     * @throws IOException
     */
    @Test
    public void testTranslateCharSequenceWithSurrogatePairsToWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CharSequence cs = "\uD800\uDC00\uD800\uDFFF";
        translator.translate(cs, stringWriter);
        String expected = "\uD800\uDC00\uD800\uDFFF";
        assertEquals(expected, stringWriter.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with empty array.
     * @return new instance of IdentityTranslator
     */
    @Test
    public CharSequenceTranslator testWithEmptyArray() {
        CharSequenceTranslator[] array = new CharSequenceTranslator[0];
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequenceTranslator result = translator.with(array);
        assertEquals(translator, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with single translator.
     * @return new instance of ConcatenatingTranslator with IdentityTranslator
     */
    @Test
    public CharSequenceTranslator testWithSingleTranslator() {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator[] array = {translator1};
        CharSequenceTranslator translator2 = new ConcatenatingTranslator(array);
        CharSequenceTranslator result = translator1.with(translator1);
        assertEquals(translator2, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with single IdentityTranslator.
     * @throws IOException
     */
    @Test
    public void testTranslateCharSequenceWithIdentityTranslator() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CharSequence cs = "test";
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(cs, stringWriter);
        String expected = cs.toString();
        assertEquals(expected, stringWriter.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with single ConcatenatingTranslator.
     * @throws IOException
     */
    @Test
    public void testTranslateCharSequenceWithConcatenatingTranslator() throws IOException {
        StringWriter stringWriter1 = new StringWriter();
        StringWriter stringWriter2 = new StringWriter();
        CharSequence cs = "test";
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
        CharSequenceTranslator[] array = {translator1, translator2};
        CharSequenceTranslator translator3 = new ConcatenatingTranslator(array);
        translator3.translate(cs, stringWriter1);
        translator3.translate(cs, stringWriter2);
        String expected1 = cs.toString();
        String expected2 = cs.toString().toUpperCase(Locale.ENGLISH);
        assertEquals(expected1, stringWriter1.toString());
        assertEquals(expected2, stringWriter2.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with multiple translators.
     * @throws IOException
     */
    @Test
    public void testTranslateCharSequenceWithMultipleTranslators() throws IOException {
        StringWriter stringWriter1 = new StringWriter();
        StringWriter stringWriter2 = new StringWriter();
        StringWriter stringWriter3 = new StringWriter();
        CharSequence cs = "test";
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
        CharSequenceTranslator translator3 = new ToLowerLocaleTranslator(Locale.ENGLISH);
        CharSequenceTranslator[] array = {translator1, translator2, translator3};
        CharSequenceTranslator translator4 = new ConcatenatingTranslator(array);
        translator4.translate(cs, stringWriter1);
        translator4.translate(cs, stringWriter2);
        translator4.translate(cs, stringWriter3);
        String expected1 = cs.toString();
        String expected2 = cs.toString().toUpperCase(Locale.ENGLISH);
        String expected3 = cs.toString().toLowerCase(Locale.ENGLISH);
        assertEquals(expected1, stringWriter1.toString());
        assertEquals(expected2, stringWriter2.toString());
        assertEquals(expected3, stringWriter3.toString());
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
    }

    @Override
    public void translate(CharSequence input, Writer out) throws IOException {
        out.write(input, 0, input.length());
    }
}

class ToUpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
    }

    @Override
    public void translate(CharSequence input, Writer out) throws IOException {
        for (int i = 0; i < input.length(); i++) {
            char c = Character.toUpperCase(input.charAt(i));
            out.write(c);
        }
    }
}

class ToLowerLocaleTranslator extends CharSequenceTranslator {
    private final Locale locale;

    public ToLowerLocaleTranslator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
    }

    @Override
    public void translate(CharSequence input, Writer out) throws IOException {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            out.write(Character.toUpperCase(c));
            out.write(Character.toLowerCase(c, locale));
        }
    }
}

class ConcatenatingTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public ConcatenatingTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int totalConsumed = 0;
        for (CharSequenceTranslator translator : translators) {
            int consumed = translator.translate(input, index, out);
            index += consumed;
            totalConsumed += consumed;
        }
        return totalConsumed;
    }

    @Override
    public void translate(CharSequence input, Writer out) throws IOException {
        for (CharSequenceTranslator translator : translators) {
            translator.translate(input, out);
        }
    }
}