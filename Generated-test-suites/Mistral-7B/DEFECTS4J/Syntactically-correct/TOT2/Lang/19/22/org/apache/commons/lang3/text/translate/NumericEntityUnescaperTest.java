import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        CharSequence input = "&amp; #65; A &#x41; &#x0066; &#0067;";
        int length = unescaper.translate(input, 0, out);

        assertEquals("AP A a b", out.toString());
        assertEquals(11, length);
    }
}