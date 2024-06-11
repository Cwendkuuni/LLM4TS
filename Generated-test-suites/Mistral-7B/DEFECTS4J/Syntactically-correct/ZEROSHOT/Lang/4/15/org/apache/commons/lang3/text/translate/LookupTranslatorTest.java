package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Unit tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        // Test empty constructor
        LookupTranslator translator = new LookupTranslator(null);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
        assertNull(translator.lookupMap);

        // Test constructor with empty lookup table
        CharSequence[][] lookup = new CharSequence[0][2];
        translator = new LookupTranslator(lookup);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
        assertNull(translator.lookupMap);

        // Test constructor with a non-empty lookup table
        lookup = new CharSequence[][] { new String[]{"hello", "hi"}, new String[]{"world", "planet"} };
        translator = new LookupTranslator(lookup);
        assertEquals(5, translator.shortest);
        assertEquals(6, translator.longest);
        assertEquals(2, translator.lookupMap.size());
        assertEquals("hi", translator.lookupMap.get(new String("hello")));
        assertEquals("planet", translator.lookupMap.get(new String("world")));
    }

    @Test
    public void testTranslate() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] { new String[]{"hello", "hi"}, new String[]{"world", "planet"} };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        String input = "helloworld";

        int index = 0;
        int actualLength = translator.translate(new String(input), index, out);
        String expected = "hiplanet";
        String actual = out.toString().substring(0, actualLength);
        assertEquals(expected, actual);
        index += actualLength;

        // Test the case when the input length is less than input length + lookup length
        actualLength = translator.translate(new String(input), index, out);
        assertEquals(0, actualLength);

        // Test the case when the substring length is less than the shortest lookup length
        String substring = input.substring(index);
        actualLength = translator.translate(new String(substring), 0, out);
        assertEquals(0, actualLength);

        // Test the case when the substring length is equal to the shortest lookup length
        substring = input.substring(index, index + translator.shortest);
        actualLength = translator.translate(new String(substring), 0, out);
        assertEquals(translator.shortest, actualLength);
    }
}