import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class DocumentTest {

    @Test
    public void testDocumentConstruction() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("baseUri", doc.baseUri());
    }

    @Test
    public void testCreateShellConstruction() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertEquals("baseUri", doc.baseUri());
        assertNotNull(doc.getElementsByTag("html"));
        assertNotNull(doc.getElementsByTag("head"));
        assertNotNull(doc.getElementsByTag("body"));
    }
}