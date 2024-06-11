import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateMethod() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160;"; // Check for ampersand and space entity
        Writer out = new StringWriter();

        int actual = unescaper.translate(new StringReader(input), 0, out);
        String expected = " \u00A0";
        assertEquals(expected, out.toString());
        assertEquals(actual, 3); // Check the number of characters written
    }
}