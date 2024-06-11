import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160; &#x20AC; &#123; &#x1F600; &#xFFFD; &#x2603; &#xFFFE;";
        StringWriter out = new StringWriter();
        int actual = unescaper.translate(new StringReader(input), 0, out);
        String expected = "\u00A0 \u00A2 \u007B \uD835\uDDE8 \uFFFF \u2603 \uFFFF";
        assertEquals(expected, out.toString());
        assertEquals(actual, input.length() + 41); // 41 is the expected length of the output string
    }
}