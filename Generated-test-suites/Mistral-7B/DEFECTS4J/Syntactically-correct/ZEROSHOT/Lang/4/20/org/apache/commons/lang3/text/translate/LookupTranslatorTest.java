import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link LookupTranslator}
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        // Test empty constructor
        LookupTranslator emptyTranslator = new LookupTranslator(null);
        Assert.assertNull(emptyTranslator.getLookupMap());
        Assert.assertEquals(0, emptyTranslator.getShortest());
        Assert.assertEquals(0, emptyTranslator.getLongest());

        // Test constructor with empty lookup table
        CharSequence[][] emptyLookup = new CharSequence[0][];
        LookupTranslator lookupTranslator = new LookupTranslator(emptyLookup);
        Assert.assertNotNull(lookupTranslator.getLookupMap());
        Assert.assertEquals(0, lookupTranslator.getShortest());
        Assert.assertEquals(0, lookupTranslator.getLongest());

        // Test constructor with lookup table
        CharSequence[][] lookup = new CharSequence[][]{
                new CharSequence[]{"abc", "def"},
                new CharSequence[]{"123", "xyz"},
                new CharSequence[]{"ABC", "XYZ"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        Assert.assertNotNull(translator.getLookupMap());
        Assert.assertEquals(3, translator.getShortest());
        Assert.assertEquals(3, translator.getLongest());
        Assert.assertEquals(3, translator.translate(new String("abc"), 0, new StringWriter()));
        Assert.assertEquals("def", translator.getLookupMap().get("abc"));
        Assert.assertEquals(3, translator.translate(new String("ABC"), 0, new StringWriter()));
        Assert.assertEquals("XYZ", translator.getLookupMap().get("ABC"));
        Assert.assertEquals(0, translator.translate(new String("123"), 0, new StringWriter()));
        Assert.assertEquals(0, translator.translate(new String("1234"), 0, new StringWriter()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{
                new CharSequence[]{"abc", null},
                new CharSequence[]{"123", "xyz"},
                new CharSequence[]{"ABC", "XYZ"}
        };
        new LookupTranslator(lookup);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithInvalidIndex() {
        CharSequence[][] lookup = new CharSequence[][]{
                new CharSequence[]{"abc", "def"},
                new CharSequence[]{"123", "xyz"},
                new CharSequence[]{"ABC", "XYZ"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "12345";
        StringWriter out = new StringWriter();
        translator.translate(new String(input), 5, out);
    }

    @Test
    public void testTranslateWithPartialMatch() {
        CharSequence[][] lookup = new CharSequence[][]{
                new CharSequence[]{"abc", "def"},
                new CharSequence[]{"123", "xyz"},
                new CharSequence[]{"ABC", "XYZ"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "abcd123ABC";
        StringWriter out = new StringWriter();
        translator.translate(new String(input), 0, out);
        Assert.assertEquals("def123XYZ", out.toString());
    }
}