import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with empty lookup table.
     */
    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
        assertEmpty(translator.lookupMap);
    }

    /**
     * Test constructor with lookup table containing single entry.
     */
    @Test
    public void testSingleEntryLookupTable() {
        CharSequence key = "test";
        CharSequence value = "replace";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key, value}});
        assertEquals(key.length(), translator.shortest);
        assertEquals(key.length(), translator.longest);
        assertEquals(1, translator.lookupMap.size());
        assertEquals(value, translator.lookupMap.get(key));
    }

    /**
     * Test constructor with lookup table containing multiple entries.
     */
    @Test
    public void testMultipleEntriesLookupTable() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replace1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replace2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key1, value1}, {key2, value2}});
        assertEquals(key1.length(), translator.shortest);
        assertEquals(Math.max(key1.length(), key2.length()), translator.longest);
        assertEquals(2, translator.lookupMap.size());
        assertEquals(value1, translator.lookupMap.get(key1));
        assertEquals(value2, translator.lookupMap.get(key2));
    }

    /**
     * Test method translate with input matching a key in the lookup table.
     * @throws IOException
     */
    @Test
    public void testTranslateWithMatchingInput() throws IOException {
        CharSequence key = "test";
        CharSequence value = "replace";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key, value}});
        String expectedOutput = value.toString();
        String actualOutput = "";
        String input = key;
        int index = 0;
        Writer writer = new StringWriter();
        int translatedLength = translator.translate(input, index, writer);
        actualOutput = writer.toString();
        assertEquals(expectedOutput, actualOutput);
        assertEquals(key.length(), translatedLength);
    }

    /**
     * Test method translate with input not matching any key in the lookup table.
     * @throws IOException
     */
    @Test
    public void testTranslateWithNoMatchingInput() throws IOException {
        CharSequence key1 = "test1";
        CharSequence value1 = "replace1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replace2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key1, value1}, {key2, value2}});
        String input = "test3";
        Writer writer = new StringWriter();
        int actualLength = translator.translate(input, 0, writer);
        assertEquals(0, actualLength);
        String actualOutput = writer.toString();
        assertEquals("", actualOutput);
    }

    /**
     * Test method translate with input partially matching a key in the lookup table.
     * @throws IOException
     */
    @Test
    public void testTranslateWithPartiallyMatchingInput() throws IOException {
        CharSequence key1 = "test1";
        CharSequence value1 = "replace1";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key1, value1}});
        String input = "tester1";
        Writer writer = new StringWriter();
        int actualLength = translator.translate(input, 0, writer);
        String actualOutput = writer.toString();
        String expectedOutput = value1.subSequence(0, key1.length()).toString();
        assertEquals(expectedOutput, actualOutput);
        assertEquals(key1.length(), actualLength);
    }

    /**
     * Test method translate with input matching multiple keys in the lookup table.
     * @throws IOException
     */
    @Test
    public void testTranslateWithMultipleMatchingInputs() throws IOException {
        CharSequence key1 = "test1";
        CharSequence value1 = "replace1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replace2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key1, value1}, {key2, value2}});
        String input = "test1test2";
        Writer writer = new StringWriter();
        int actualLength = translator.translate(input, 0, writer);
        String actualOutput = writer.toString();
        String expectedOutput = value1.toString() + value2.toString();
        assertEquals(expectedOutput, actualOutput);
        assertEquals(key1.length() + key2.length(), actualLength);
    }

    /**
     * Helper method to check if a Map is empty.
     * @param map the Map to check
     */
    private void assertEmpty(HashMap<CharSequence, CharSequence> map) {
        assertTrue(map.isEmpty());
    }

    /**
     * Helper class to write a String to a Writer.
     */
    private static class StringWriter extends Writer {

        private StringBuilder sb = new StringBuilder();

        @Override
        public void write(int c) throws IOException {
            sb.append((char) c);
        }

        @Override
        public void write(String s, int start, int length) throws IOException {
            sb.append(s.substring(start, start + length));
        }

        @Override
        public void write(CharSequence cq) throws IOException {
            sb.append(cq);
        }

        @Override
        public void flush() throws IOException {
            // no-op
        }

        @Override
        public void close() throws IOException {
            // no-op
        }

        @Override
        public String getBuffer() {
            return sb.toString();
        }
    }
}