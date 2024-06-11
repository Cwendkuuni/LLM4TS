import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertNull(unescaper.getOptions());
        unescaper = new NumericEntityUnescaper();
        assertNotNull(unescaper.getOptions());
    }

    // ... (omitted for brevity)
}