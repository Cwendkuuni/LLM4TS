import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Document doc = xmlTreeBuilder.getDocument(); // assumed getDocument method exists
        assertTrue(doc != null);
        assertTrue(xmlTreeBuilder.stack.contains(doc));
    }
}