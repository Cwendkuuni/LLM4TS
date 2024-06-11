import static org.junit.Assert.assertDoesNotThrow;
import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import java.io.StringReader;

public class ParseInputSourceTest {

    @org.junit.Test
    public void testParseInputSource() {
        String xmlString = "<root><tag>Test</tag></root>";
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
            @Override
            public void warning(SAXParseException exception) {
                // Ignore warnings
            }
        };
        assertDoesNotThrow(() -> abstractLoader.parseInputSource(inputSource));
    }
}