
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        Element html = doc.getElementsByTag("html").first();
        assertEquals("head", html.getElementsByTag("head").first().tagName());
        assertEquals("body", html.getElementsByTag("body").first().tagName());
    }

    @Test
    public void testHeadAccess() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyAccess() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleAccess() {
        Document doc = new Document("baseUri");
        assertEquals("", doc.title());

        Document docWithTitle = new Document("baseUri");
        docWithTitle.title("Test Title");
        assertEquals("Test Title", docWithTitle.title());
    }

    @Test(expected = IllegalStateException.class)
    public void testTitle_EmptyDoc() {
        Document doc = new Document("baseUri");
        assertEquals("", doc.title());

        Document emptyDoc = new Document("");
        try {
            assertEquals("", emptyDoc.title());
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    @Test(expected = NullPointerException.class)
    public void testTitle_NullTitle() {
        Document doc = new Document("baseUri");
        try {
            doc.title(null);
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("p");
        assertNotNull(element);
    }

    // Test normalise method here...
}
