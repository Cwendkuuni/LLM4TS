import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testEntityTranslation() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringBuilder builder = new StringBuilder();
        String testString = "&amp;&#x41;ABC&amp;&#13;";
        translator.translate(new StringReader(testString), 0, new StringWriter(builder));
        assertEquals("AB&", builder.toString());
    }
}