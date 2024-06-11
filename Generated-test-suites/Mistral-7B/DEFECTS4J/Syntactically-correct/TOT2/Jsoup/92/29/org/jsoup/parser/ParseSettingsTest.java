import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("TagName", settings.normalizeTag("TagName"));
    }
}