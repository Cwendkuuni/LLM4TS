import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
}