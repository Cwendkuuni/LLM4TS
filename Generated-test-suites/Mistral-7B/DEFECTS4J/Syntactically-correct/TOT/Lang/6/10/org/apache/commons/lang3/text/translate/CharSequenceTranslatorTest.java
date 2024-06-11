package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.junit.Test;

/**
 * Test for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with an empty CharSequence.
     */
    @Test
    public void testTranslateEmptyCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = "";
        int expectedConsumed = 0;
        int actualConsumed = translator.translate(input, 0, writer);
        assertEquals(expectedConsumed, actualConsumed);
        assertEquals("", translator.translate(input));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a null CharSequence.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = null;
        translator.translate(input, 0, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a single character CharSequence.
     */
    @Test
    public void testTranslateSingleCharacterCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = "A";
        int expectedConsumed = 1;
        int actualConsumed = translator.translate(input, 0, writer);
        assertEquals(expectedConsumed, actualConsumed);
        assertEquals("A", translator.translate(input));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a multicharacter CharSequence and no exceptions.
     */
    @Test
    public void testTranslateMulticharacterCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello, World!";
        int expectedConsumed = input.length();
        int actualConsumed = translator.translate(input, 0, writer);
        assertEquals(expectedConsumed, actualConsumed);
        assertEquals("Hello, World!", translator.translate(input));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a multibyte character CharSequence and no exceptions.
     */
    @Test
    public void testTranslateMultibyteCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello, 世界!";
        int expectedConsumed = input.length();
        int actualConsumed = translator.translate(input, 0, writer);
        assertEquals(expectedConsumed, actualConsumed);
        assertEquals("Hello, 世界!", translator.translate(input));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a multibyte character CharSequence and an IOException.
     */
    @Test(expected = IOException.class)
    public void testTranslateMultibyteCharSequenceWithIOException() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello, 世界!";
        translator.translate(input, 0, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with an empty CharSequence.
     */
    @Test
    public void testTranslateEmptyCharSequenceToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String actual = translator.translate("");
        assertEquals("", actual);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a null CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequenceToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a single character CharSequence.
     */
    @Test
    public void testTranslateSingleCharacterCharSequenceToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String actual = translator.translate("A");
        assertEquals("A", actual);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a multicharacter CharSequence.
     */
    @Test
    public void testTranslateMulticharacterCharSequenceToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String actual = translator.translate("Hello, World!");
        assertEquals("Hello, World!", actual);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a multibyte character CharSequence.
     */
    @Test
    public void testTranslateMultibyteCharSequenceToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String actual = translator.translate("Hello, 世界!");
        assertEquals("Hello, 世界!", actual);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        translator.translate(input, writer -> {}); // write method with null Writer
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with an empty array.
     */
    @Test
    public void testWithEmptyArray() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequenceTranslator mergedTranslator = translator.with();
        assertEquals(translator, mergedTranslator);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with a single translator.
     */
    @Test
    public void testWithSingleTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator translator = new IdentityTranslator().with(identityTranslator);
        assertEquals(2, translator.getClass().getDeclaredMethods().length);
        assertEquals(identityTranslator, translator.getDelegate());
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with multiple translators.
     */
    @Test
    public void testWithMultipleTranslators() {
        CharSequenceTranslator identityTranslator1 = new IdentityTranslator();
        CharSequenceTranslator identityTranslator2 = new IdentityTranslator();
        CharSequenceTranslator translator = new IdentityTranslator().with(identityTranslator1, identityTranslator2);
        assertEquals(3, translator.getClass().getDeclaredMethods().length);
        assertEquals(identityTranslator1, translator.getDelegate(0));
        assertEquals(identityTranslator2, translator.getDelegate(1));
    }
}