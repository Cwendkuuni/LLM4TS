import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(input, baseUri, errors);
        Document doc = builder.getDocument();
        assertNotNull(doc);
    }
}