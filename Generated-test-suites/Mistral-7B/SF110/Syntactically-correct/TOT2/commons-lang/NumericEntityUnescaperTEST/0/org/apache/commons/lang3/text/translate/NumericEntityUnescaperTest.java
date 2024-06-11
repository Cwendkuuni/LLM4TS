import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        new NumericEntityUnescaper(OPTION.semiColonRequired);
        new NumericEntityUnescaper();
        new NumericEntityUnescaper(new OPTION[0]); // Testing empty options array
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
        NumericEntityUnescaper unescaperWithoutOptions = new NumericEntityUnescaper();
        assertFalse(unescaperWithoutOptions.isSet(OPTION.semiColonRequired));
        NumericEntityUnescaper unescaperWithAllOptions = new NumericEntityUnescaper(OPTION.values());
        for (OPTION option : OPTION.values()) {
            assertTrue(unescaperWithAllOptions.isSet(option));
        }
    }

    @Test
    public void testTranslate() throws IOException {
        // Basic test case
        CharSequence input = "&amp;#160; &#x2013; &#10; &amp;#128; &amp;#x2145; &amp;#65535; &amp;#xFFFF;";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(input, 0, out);
        assertEquals(" ", out.toString());

        // Testing missing semicolon at the end
        CharSequence inputMissingSemiColon = "&amp;#160; &#x2013; &#10; &amp;#128; &amp;#x2145; &amp;#65535;";
        Writer outMissingSemiColon = new StringWriter();
        NumericEntityUnescaper unescaperMissingSemiColon = new NumericEntityUnescaper(OPTION.semiColonRequired);
        unescaperMissingSemiColon.translate(inputMissingSemiColon, 0, outMissingSemiColon);
        assertFalse(outMissingSemiColon.toString().isEmpty());
        assertTrue(outMissingSemiColon.toString().contains("Semi-colon required at end of numeric entity"));

        // Testing invalid numeric entities
        CharSequence inputInvalidNumericEntities = "&amp;#abcd; &amp;#1234abcd; &amp;#1234567890abcd;";
        Writer outInvalidNumericEntities = new StringWriter();
        NumericEntityUnescaper unescaperInvalidNumericEntities = new NumericEntityUnescaper();
        unescaperInvalidNumericEntities.translate(inputInvalidNumericEntities, 0, outInvalidNumericEntities);
        assertTrue(outInvalidNumericEntities.toString().contains("0"));
        assertTrue(outInvalidNumericEntities.toString().contains("1"));

        // Testing hexadecimal entities
        CharSequence inputHexadecimalEntities = "&amp;#x1234; &amp;#xABCD; &amp;#xFFFF;";
        Writer outHexadecimalEntities = new StringWriter();
        NumericEntityUnescaper unescaperHexadecimalEntities = new NumericEntityUnescaper();
        unescaperHexadecimalEntities.translate(inputHexadecimalEntities, 0, outHexadecimalEntities);
        assertEquals("\u1234\uABCD\uFFFF", outHexadecimalEntities.toString());
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateWithNumberFormatException() throws IOException {
        CharSequence inputWithNumberFormatException = "&amp;#abcdxyz;";
        Writer outWithNumberFormatException = new StringWriter();
        new NumericEntityUnescaper().translate(inputWithNumberFormatException, 0, outWithNumberFormatException);
    }
}