import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        CharSequence input = "input";
        int index = 0;
        Writer out = new StringWriter();

        int result = translator.translate(input, index, out);

        assertEquals("", out.toString());
        assertEquals(0, result);
    }

    @Test
    public void testLookupTranslatorWithSingleEntryLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"key", "value"}});
        CharSequence input = "key";
        int index = 0;
        Writer out = new StringWriter();

        int result = translator.translate(input, index, out);

        assertEquals("value", out.toString());
        assertEquals(1, result);
    }

    @Test
    public void testLookupTranslatorWithMultipleEntriesLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"key1", "value1"}, {"key2", "value2"}});
        CharSequence input = "key1";
        int index = 0;
        Writer out = new StringWriter();

        int result = translator.translate(input, index, out);

        assertEquals("value1", out.toString());
        assertEquals(2, result.length()); // key length
    }

    @Test(expected = IOException.class)
    public void testLookupTranslatorWithNullWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"key", "value"}});
        CharSequence input = "key";
        int index = 0;
        Writer out = null;

        translator.translate(input, index, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLookupTranslatorWithInputBiggerThanLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"key", "value"}});
        CharSequence input = "keyabcd";
        int index = 0;
        Writer out = new StringWriter();

        translator.translate(input, index, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
    }
}