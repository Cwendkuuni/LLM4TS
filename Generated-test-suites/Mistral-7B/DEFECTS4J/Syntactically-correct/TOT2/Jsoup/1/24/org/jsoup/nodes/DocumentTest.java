import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document(null);
        assertNull(doc);

        Document doc2 = new Document("");
        assertNotNull(doc2);
    }

    @Test
    public void testCreateShell() {
        try {
            Document.createShell(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        Document doc = Document.createShell("");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());

        try {
            Document.createShell("http://example.com/test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test methods head(), body(), title(), title(String title), createElement(String tagName), normalise(), text(String text), and nodeName() here

    @Test
    public void testOuterHtml() {
        Document doc = new Document("");
        doc.head().appendElement("test");
        doc.body().appendElement("test");

        try {
            doc.outerHtml(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        String html = doc.outerHtml();
        assertEquals("<html><head><test></test></head><body><test></test></body></html>", html);
    }
}