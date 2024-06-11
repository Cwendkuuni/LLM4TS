import static org.junit.Assert.*;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {
    // Test start
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
        // Test end
    }
}