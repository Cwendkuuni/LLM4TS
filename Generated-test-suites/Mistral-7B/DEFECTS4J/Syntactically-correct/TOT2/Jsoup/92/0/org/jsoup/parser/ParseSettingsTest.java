import org.junit.Test;
import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {
    // 

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TAG";
        String normalizedTag = settings.normalizeTag(tagName);
        assert (normalizedTag.equals(tagName)); // Test for preserving tag name case

        settings = ParseSettings.htmlDefault;
        String htmlNormalizedTag = settings.normalizeTag(tagName);
        assert (!tagName.equals(htmlNormalizedTag)); // Test for not preserving tag name case with default settings
    }

    // 
}