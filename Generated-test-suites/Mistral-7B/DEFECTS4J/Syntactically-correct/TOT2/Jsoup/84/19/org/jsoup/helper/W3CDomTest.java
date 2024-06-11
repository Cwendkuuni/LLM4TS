import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {
    @Test
    public void testFromJsoupWithNullDocument() {
        // Test case for null jsoup document
        assertNull(W3CDom.fromJsoup(null));
    }

    @Test
    public void testFromJsoupWithEmptyDocument() {
        // Test case for empty jsoup document
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.factory.DefaultDocument();
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        assertTrue(w3cDocument.getDocumentElement() != null);
        assertTrue(w3cDocument.getDocumentElement().getNodeName().equals("#document"));
    }

    @Test
    public void testFromJsoupWithJsoupDocumentContainingRootElement() {
        // Test case for jsoup document with a root element
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.helper.Jsoup.parse("<html><body></body></html>");
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
        assertEquals(w3cDocument.getDocumentElement().getNodeName(), "html");
    }
}