import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import java.io.StringWriter;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        // Test case provided by Expert 1
    }

    @Test
    public void testIsSet() {
        // Test case provided by Expert 1
    }

    @Test
    public void testTranslateValidNumericEntity() throws IOException {
        // Test case provided by Expert 1
    }

    @Test
    public void testTranslateValidHexNumericEntity() throws IOException {
        // Test case provided by Expert 1
    }

    @Test
    public void testTranslateMissingSemiColon() throws IOException {
        // Test case provided by Expert 1
    }

    @Test
    public void testTranslateInvalidHexValue() throws IOException {
        // Test case provided by Expert 1
    }

    @Test
    public void testTranslateMaxIntegerValue() throws IOException {
        // Test case provided by Expert 1
    }

    @Test
    public void testTranslateMaxIntegerValueWithSemiColon() throws IOException {
        // Test case provided by Expert 1
    }

    @Test
    public void testTranslateEmptyInput() throws IOException {
        Writer out = new StringWriter();
        CharSequence input = "";
        int result = new NumericEntityUnescaper().translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithMultipleEntities() throws IOException {
        Writer out = new StringWriter();
        CharSequence input = "&amp;#123; &amp;#45; &amp;#x61; &amp;#1024; &amp;#xFFFF; &amp;#x10FFFF; ";
        int result = new NumericEntityUnescaper().translate(input, 0, out);
        assertEquals("abcde\u0080\uffff", out.toString());
        assertEquals(7, result);
    }

    @Test
    public void testTranslateWithOptionalSemiColon() throws IOException {
        Writer out = new StringWriter();
        CharSequence input = "&amp;#123;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        int result = unescaper.translate(input, 0, out);
        assertEquals("abc", out.toString());
        assertEquals(3, result);
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColon() throws IOException {
        Writer out = new StringWriter();
        CharSequence input = "&amp;#123";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, out));
    }
}