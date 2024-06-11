package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequencePair;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link LookupTranslator} class
 *
 * @author John Doe
 */
public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setup() {
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku", " a"}};
        translator = new LookupTranslator(lookup);
    }

    @Test
    public void testTranslateWithValidInput() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final String input = "kakoru";

        translator.translate(new StringCharSequence(input), index, out);
        final String expectedOutput = "the a an a";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslateWithShortInput() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 1;
        final String input = "ko";

        translator.translate(new StringCharSequence(input), index, out);
        final String expectedOutput = "a";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslateWithLongInput() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final String input = "kakorukat";

        translator.translate(new StringCharSequence(input), index, out);
        final String expectedOutput = "the a an a";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 10;
        final String input = "kakoru";

        translator.translate(new StringCharSequence(input), index, out);
        assertNull(out.toString());
    }

    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final String input = "";

        translator.translate(new StringCharSequence(input), index, out);
        assertNull(out.toString());
    }

    @Test
    public void testTranslateWithNullInput() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final String input = null;

        translator.translate(new StringCharSequence(input), index, out);
        assertNull(out.toString());
    }

    @Test
    public void testTranslateWithNullOutput() throws Exception {
        final StringWriter out = null;
        final int index = 0;
        final String input = "kakoru";

        translator.translate(new StringCharSequence(input), index, out);
        assertNull(out);
    }

    @Test
    public void testTranslateWithNullLookupTable() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final CharSequence[][] lookup = null;

        assertThrows(IllegalArgumentException.class, () -> new LookupTranslator(lookup));
    }

    @Test
    public void testTranslateWithEmptyLookupTable() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final CharSequence[][] lookup = new CharSequence[0][];

        assertThrows(IllegalArgumentException.class, () -> new LookupTranslator(lookup));
    }

    @Test
    public void testTranslateWithInvalidLookupTable() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku"}, {" ku"}};

        assertThrows(IllegalArgumentException.class, () -> new LookupTranslator(lookup));
    }

    @Test
    public void testTranslateWithNullKeyInLookupTable() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final CharSequence[][] lookup = {{"  ", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku", " a"}};

        assertThrows(NullPointerException.class, () -> new LookupTranslator(lookup));
    }

    @Test
    public void testTranslateWithNullValueInLookupTable() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", null}, {" kr", " an"}, {" ku", " a"}};

        assertThrows(NullPointerException.class, () -> new LookupTranslator(lookup));
    }

    @Test
    public void testTranslateWithEmptyValueInLookupTable() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", ""}, {" kr", " an"}, {" ku", " a"}};

        assertThrows(IllegalArgumentException.class, () -> new LookupTranslator(lookup));
    }

    @Test
    public void testTranslateWithDuplicateKeysInLookupTable() throws Exception {
        final StringWriter out = new StringWriter();
        final int index = 0;
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku", " a"}, {" ka", " the"}};

        assertThrows(IllegalArgumentException.class, () -> new LookupTranslator(lookup));
    }

    @Test
    public void testGetShortestLookupKeyLength() {
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku", " a"}};
        final LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals(2, translator.getShortest());
    }

    @Test
    public void testGetLongestLookupKeyLength() {
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku", " a"}};
        final LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals(3, translator.getLongest());
    }

    @Test
    public void testCreateLookupMap() {
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku", " a"}};
        final LookupTranslator translator = new LookupTranslator(lookup);

        final Map<CharSequence, CharSequence> expectedLookupMap = new HashMap<>();
        expectedLookupMap.put(new StringCharSequence("ka"), new StringCharSequence("the"));
        expectedLookupMap.put(new StringCharSequence("ko"), new StringCharSequence("a"));
        expectedLookupMap.put(new StringCharSequence("kr"), new StringCharSequence("an"));
        expectedLookupMap.put(new StringCharSequence("ku"), new StringCharSequence("a"));

        assertEquals(expectedLookupMap, translator.lookupMap);
    }

    @Test
    public void testToString() {
        final CharSequence[][] lookup = {{" ka", " the"}, {" ko", " a"}, {" kr", " an"}, {" ku", " a"}};
        final LookupTranslator translator = new LookupTranslator(lookup);

        final String expectedToString = "LookupTranslator{lookupMap={ka=the, ko=a, kr=an, ku=a}}";
        assertEquals(expectedToString, translator.toString());
    }
}