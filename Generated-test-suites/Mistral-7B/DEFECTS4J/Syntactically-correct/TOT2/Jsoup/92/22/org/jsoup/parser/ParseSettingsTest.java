import org.junit.Test;
import org.junit.Assert;

public class ParseSettingsTest {
    // Test for preserveTagCase() method
    @Test
    public void testPreserveTagCase() {
        ParseSettings settingsHtmlDefault = new ParseSettings(false, false);
        Assert.assertFalse(settingsHtmlDefault.preserveTagCase());

        ParseSettings settingsPreserveCase = new ParseSettings(true, true);
        Assert.assertTrue(settingsPreserveCase.preserveTagCase());
    }
}