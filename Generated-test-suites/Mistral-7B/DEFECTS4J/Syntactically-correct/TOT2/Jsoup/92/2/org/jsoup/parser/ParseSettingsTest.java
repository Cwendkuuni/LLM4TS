import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TagName";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertTrue(normalizedTagName.equals(tagName));

        settings = new ParseSettings(false, false);
        String lowerCaseTagName = settings.normalizeTag(tagName);
        assertFalse(normalizedTagName.equals(tagName));
        assertTrue(lowerCaseTagName.equals(tagName.toLowerCase()));
    }
}