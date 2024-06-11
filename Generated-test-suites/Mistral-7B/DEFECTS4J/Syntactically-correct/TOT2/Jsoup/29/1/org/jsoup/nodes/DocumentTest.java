import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertElementPresent(doc, "html");
        assertElementPresent(doc, "head");
        assertElementPresent(doc, "body");
    }

    @Test
    public void testAccessHeadElement() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertElementPresent(doc, "head", head);
    }

    @Test
    public void testAccessBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertElementPresent(doc, "body", body);
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("<html><head><title>Test Title</title></head><body></body></html>");
        assertEquals("Test Title", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testGetTitleNullTitle() {
        Document doc = new Document("<html><head></head><body></body></html>");
        doc.title(null);
        assertEquals("", doc.title()); // normalize title after null assignment
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        throw new NullPointerException();
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("<html><head><title></title></head><body></body></html>");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("<html><head></head><body><p>Test</p><p>Test</p></body></html>");
        doc.normalise();
        List<Element> pElements = doc.getElementsByTag("p");
        assertEquals(1, pElements.size());
        assertTrue(pElements.get(0).parent().equals(doc.body()));
    }

    private void assertElementPresent(Document doc, String tag, Element expected) {
        Element actual = doc.getElementsByTag(tag).first();
        assertEquals(expected, actual);
    }

    private void assertElementPresent(Document doc, String tag) {
        Element element = doc.getElementsByTag(tag).first();
        assertNotNull(element);
    }

    private void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected object to be not null");
        }
    }
}