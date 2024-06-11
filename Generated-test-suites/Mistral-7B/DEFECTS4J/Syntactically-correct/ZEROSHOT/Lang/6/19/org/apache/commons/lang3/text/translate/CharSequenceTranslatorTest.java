package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    private static final CharSequenceTranslator TRANSLATOR = new IdentityTranslator();

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testTranslate() throws Exception {
        String input = "Hello, 世界!";
        String expected = "Hello, %7F%99%90%8E%87%82%88%84%8B%9D%BF%B0%B5%B9%B2!";
        Writer out = new StringWriter();
        int index = 0;
        int count = TRANSLATOR.translate(StringUtils.substring(input, index), index, out);
        assertEquals(count, 6);
        index += 6;
        count = TRANSLATOR.translate(StringUtils.substring(input, index), index, out);
        assertEquals(count, 11);
        index += 11;
        String result = out.toString();
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateToString() {
        String input = "Hello, 世界!";
        String expected = "Hello, %7F%99%90%8E%87%82%88%84%8B%9D%BF%B0%B5%B9%B2!";
        String result = TRANSLATOR.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testTranslateToWriter() throws Exception {
        String input = "Hello, 世界!";
        Writer out = new StringWriter();
        int index = 0;
        TRANSLATOR.translate(input, out);
        String result = out.toString();
        String expected = "Hello, %7F%99%90%8E%87%82%88%84%8B%9D%BF%B0%B5%B9%B2!";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator[])}.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new UpperCaseTranslator();
        CharSequenceTranslator translator2 = new LowerCaseTranslator();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                int count = translator1.translate(input, index, out);
                return translator2.translate(input, index + count, out);
            }
        }.with(translator1, translator2);
        String input = "Hello, World!";
        String expected = "HELLO, world!";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

}