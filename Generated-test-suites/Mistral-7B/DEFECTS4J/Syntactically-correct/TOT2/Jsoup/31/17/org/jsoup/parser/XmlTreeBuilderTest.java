import org.junit.jupiter.api.Test;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "<?xml version='1.0' encoding='UTF-8'?><root><element/></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        // Add assertions here if needed
    }
}