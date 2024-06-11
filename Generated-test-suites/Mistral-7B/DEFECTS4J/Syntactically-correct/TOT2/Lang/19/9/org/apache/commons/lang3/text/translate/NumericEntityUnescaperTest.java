import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateValidHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        String input = "&#[xCA]FE;";
        int expectedLength = 2 + 4 + 1;
        int actualLength = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(expectedLength, actualLength);
        assertEquals("\u00CA\u00FE", out.toString());
    }
}