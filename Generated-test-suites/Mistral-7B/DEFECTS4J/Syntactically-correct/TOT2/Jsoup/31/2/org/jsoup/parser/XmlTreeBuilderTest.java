import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @BeforeEach
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml>\n<tag attr='value'/>\n</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertNotNull(xmlTreeBuilder.getDocument());
    }
}