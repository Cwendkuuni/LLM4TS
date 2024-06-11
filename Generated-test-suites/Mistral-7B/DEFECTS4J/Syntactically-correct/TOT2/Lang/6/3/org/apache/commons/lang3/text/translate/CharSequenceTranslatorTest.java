package org.junit.tests;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.*;
import static org.junit.Assert.*;;

public class CharSequenceTranslatorTest {

    CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new MyCharSequenceTranslator(); // Replace with the actual translator implementation
    }

    @Test
    public void testTranslate() {
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslate_EmptyInput() {
        String input = "";
        String expectedOutput = "";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullInput() {
        CharSequence input = null;
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
    }

    @Test(expected = IllegalStateException.class)
    public void testTranslate_NegativeIndex() {
        String input = "Hello, World!";
        Writer out = new StringWriter();
        translator.translate(input, -1, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() {
        String input = "Hello, World!";
        Writer out = null;
        try {
            translator.translate(input, out);
        } catch (IOException e) {
            fail("translate should not throw IOExceptions with StringWriter");
        }

        // The test should fail as we passed a null Writer
    }

    @Test(expected = IOException.class)
    public void testTranslate_IOException() throws IOException {
        String input = "Hello, World!";
        Writer out = new StringWriter() {
            @Override
            public void write(int c, IOException e) throws IOException {
                throw e;
            }
        };
        int consumed = translator.translate(input, 0, out);
        fail("translate should not consume any codepoints when an IOException is thrown");
    }

    @Test
    public void testTranslate_SurrogatePairs() {
        String input = "\uD800\uDC00";
        String expectedOutput = "\uD800\uDC00";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateWith() {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator1();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator2();
        CharSequenceTranslator translator = translator1.with(translator2);
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!"; // Replace with the expected output when merging multiple translators
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testHex() {
        for (int i = 0; i <= 0xFFFF; i++) {
            String hex = CharSequenceTranslator.hex(i);
            int dec = Integer.parseInt(hex, 16);
            assertEquals(i, dec);
        }
    }
}