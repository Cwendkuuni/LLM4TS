
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSetNotSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSetEmptyOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslateValidDecimal() throws IOException {
        String input = "This text contains the entity &#10;.";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int index = input.indexOf("&#10;");
        int translatedLength = unescaper.translate(new StringReader(input), index, output);
        String expectedOutput = "This text contains the entity \u000A.";
        assertEquals(expectedOutput.length(), output.toString().length());
        assertEquals(expectedOutput, output.toString());
        assertEquals(3 + 1 + 1, translatedLength);
    }

    @Test
    public void testTranslateValidHex() throws IOException {
        String input = "This text contains the entity &#x61;.";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int index = input.indexOf("&#x61;");
        int translatedLength = unescaper.translate(new StringReader(input), index, output);
        String expectedOutput = "This text contains the entity a.";
        assertEquals(expectedOutput.length(), output.toString().length());
        assertEquals(expectedOutput, output.toString());
        assertEquals(3 + 1 + 1, translatedLength);
    }

    @Test
    public void testTranslateInvalidEntity() throws IOException {
        String input = "This text contains the invalid entity &%$#@!.";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        int index = input.indexOf("&%$#@!");
        int translatedLength = unescaper.translate(new StringReader(input), index, output);
        assertEquals(0, translatedLength);
        assertEquals("", output.toString());
    }
}