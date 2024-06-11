import org.junit.Test;
import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings1 = new ParseSettings(false, false);
        assertFalse(parseSettings1.preserveTagCase());

        ParseSettings parseSettings2 = new ParseSettings(true, false);
        assertTrue(parseSettings2.preserveTagCase());
    }

    @Test
    public void testPreserveTagCaseEdgeCase() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertTrue(parseSettings.preserveTagCase());

        ParseSettings parseSettings2 = new ParseSettings(false, true);
        assertFalse(parseSettings2.preserveTagCase());
    }
}