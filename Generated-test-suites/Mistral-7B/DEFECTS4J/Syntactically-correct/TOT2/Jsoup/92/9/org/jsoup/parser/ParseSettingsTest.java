import static org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {
    // Test start
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TagName";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertTrue(normalizedTagName.equals(tagName));
    }

    // Test end
}