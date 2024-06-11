import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "&amp;#123; &#x7f; &#127; &amp;#1234; &amp;#x1F600; &amp;#16r; &amp;#16R; &amp;#x1F6FF;";
        StringWriter out = new StringWriter();
        CharSequence cs = new StringReader(input);

        int charsTranslated = unescaper.translate(cs, 0, out);
        assertEquals("&#123;&#123;&#127;&#1234;&#1F600;&#16;&#16;&#1F6FF;", out.toString());
        assertEquals(12, charsTranslated);
    }
}