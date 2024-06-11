import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    @Test
    public void testParseXML() {
        String xmlData = "<root><child1>Test</child1><child2><subChild>Test Again</subChild></child2></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlData), "baseUrl");
        assertNotNull(document);
        // add assertions to check the structure and content of the document
    }
}