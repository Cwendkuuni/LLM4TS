import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test public void testDocumentConstruction() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals(3, doc.childNodes().size());
    }

    @Test public void testHeadGetter() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test public void testBodyGetter() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test public void testTitleGetter() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);
    }

    @Test public void testTitleSetter() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("div");
        assertNotNull(newElement);
        assertEquals("div", newElement.tagName());
    }

    @Test public void testNormaliseDocument() {
        Document doc = new Document("<html><head></head><body></body></html>");
        doc.title("Test Title");
        doc.body().appendElement("Hello World!");
        doc.normalise();
        assertEquals("Hello World", doc.body().text());
    }

    @Test public void testOuterHtmlGetter() {
        Document doc = new Document("<html><head></head><body></body></html>");
        assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
    }

    @Test public void testTextSetter() {
        Document doc = new Document("<html><head></head><body></body></html>");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleSetterNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateElementNullTagName() {
        Document doc = new Document("http://example.com");
        doc.createElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateElementUnknownTagName() {
        Document doc = new Document("http://example.com");
        doc.createElement("unknownTag");
    }

    @Test public void testTextNodeEmptyString() {
        Document doc = new Document("http://example.com");
        Element textNode = doc.createElement("textNode");
        textNode.setText("");
        assertEquals("", textNode.text());
    }

    @Test public void testTextNodeBlank() {
        Document doc = new Document("http://example.com");
        Element textNode = doc.createElement("textNode");
        textNode.setText(" ");
        assertEquals("", textNode.text());
    }
}