package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test class for LookupTranslator
 */
public class LookupTranslatorTest {

    private LookupTranslator translator;
    private Writer writer;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        writer = new StringWriter();
        stringWriter = new StringWriter();
        translator = new LookupTranslator(new CharSequence[][]{
                new CharSequence[]{"hello", "hi"},
                new CharSequence[]{"world", "greetings"},
                new CharSequence[]{"java", "programming language"},
                new CharSequence[]{"test", "example"},
                new CharSequence[]{"apple", "fruit"}
        });
    }

    @Test
    public void testTranslate() throws Exception {
        String testInput = "helloworldjava";

        translator.translate(CharSequence.class.cast(testInput), 0, writer);
        assertEquals("hi greetings programming language", writer.toString());

        stringWriter.getBuffer().setLength(0);
        translator.translate(CharSequence.class.cast(testInput), 0, stringWriter);
        assertEquals("hi greetings programming language", stringWriter.toString());

        testInput = "helloworldjavatestapple";
        translator.translate(CharSequence.class.cast(testInput), 0, writer);
        assertEquals("hi greetings programming language fruit", writer.toString());

        stringWriter.getBuffer().setLength(0);
        translator.translate(CharSequence.class.cast(testInput), 0, stringWriter);
        assertEquals("hi greetings programming language fruit", stringWriter.toString());

        testInput = "apple";
        translator.translate(CharSequence.class.cast(testInput), 0, writer);
        assertEquals("fruit", writer.toString());

        stringWriter.getBuffer().setLength(0);
        translator.translate(CharSequence.class.cast(testInput), 0, stringWriter);
        assertEquals("fruit", stringWriter.toString());

        testInput = "helloapple";
        translator.translate(CharSequence.class.cast(testInput), 0, writer);
        assertEquals("hi fruit", writer.toString());

        stringWriter.getBuffer().setLength(0);
        translator.translate(CharSequence.class.cast(testInput), 0, stringWriter);
        assertEquals("hi fruit", stringWriter.toString());

        testInput = "helloworldjavatest";
        translator.translate(CharSequence.class.cast(testInput), 0, writer);
        assertEquals("hi greetings programming language example", writer.toString());

        stringWriter.getBuffer().setLength(0);
        translator.translate(CharSequence.class.cast(testInput), 0, stringWriter);
        assertEquals("hi greetings programming language example", stringWriter.toString());

        testInput = "helloworldjava12345";
        translator.translate(CharSequence.class.cast(testInput), 0, writer);
        assertEquals("hi greetings programming language", writer.toString());

        stringWriter.getBuffer().setLength(0);
        translator.translate(CharSequence.class.cast(testInput), 0, stringWriter);
        assertEquals("hi greetings programming language", stringWriter.toString());

        testInput = "";
        translator.translate(CharSequence.class.cast(testInput), 0, writer);
        assertEquals("", writer.toString());

        stringWriter.getBuffer().setLength(0);
        translator.translate(CharSequence.class.cast(testInput), 0, stringWriter);
        assertEquals("", stringWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullLookupTable() {
        translator = new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithEmptyLookupTable() {
        translator = new LookupTranslator(new CharSequence[][]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() {
        translator.translate(null, 0, writer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithIndexOutOfBounds() {
        final String testInput = "helloworldjava";
        translator.translate(CharSequence.class.cast(testInput), testInput.length(), writer);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNullWriter() {
        translator.translate(CharSequence.class.cast("test"), 0, null);
    }

    @Test
    public void testGetShortestAndLongest() {
        Map<String, String> lookupMap = new HashMap<>();
        lookupMap.put("hello", "hi");
        lookupMap.put("world", "greetings");
        lookupMap.put("java", "programming language");
        lookupMap.put("test", "example");
        lookupMap.put("apple", "fruit");

        LookupTranslator translator = new LookupTranslator(transformToCharSequenceArray(lookupMap.keySet().toArray(new String[0])));
        int shortest = translator.getShortest();
        int longest = translator.getLongest();

        assertEquals(5, shortest);
        assertEquals(8, longest);
    }

    private CharSequence[] transformToCharSequenceArray(Object[] array) {
        int length = array.length;
        CharSequence[] result = new CharSequence[length];
        for (int i = 0; i < length; i++) {
            result[i] = (CharSequence) array[i];
        }
        return result;
    }
}