import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        String input = "&amp;#160;&#x2013;&#163;";

        int length = unescaper.translate(new StringReader(input), 0, out);
        String expected = "\u00A0\u2013\u00A3";
        String result = out.toString().substring(0, length);

        assertEquals(expected, result);
    }
}