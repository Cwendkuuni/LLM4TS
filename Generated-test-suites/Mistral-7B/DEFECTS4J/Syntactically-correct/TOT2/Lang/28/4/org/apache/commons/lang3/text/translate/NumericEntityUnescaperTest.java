import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequence input = "&amp;#160; &#x201c; &#x201d; &#xA9; &#x20AC;";
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, out);
        String expected = "┬╗  „ „  © €";
        String actual = out.toString().substring(0, length);
        assertEquals(expected, actual);
    }
}