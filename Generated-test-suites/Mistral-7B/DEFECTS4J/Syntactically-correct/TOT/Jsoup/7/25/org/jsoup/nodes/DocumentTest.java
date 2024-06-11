package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertNotNull(html);
        Element head = html.getFirstElementSibling();
        assertNotNull(head);
        Element body = head.getNextElementSibling();
        assertNotNull(body);
        assertEquals(3, doc.numChildren());
    }

    @Test
    public void testTitleAccessors() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Document");
        assertEquals("Test Document", doc.title());
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Document");
        doc.body().text("This is a test\nThis is another test");
        doc.normalise();
        Element body = doc.body();
        List<Node> textNodes = body.childNodes();
        assertEquals(2, textNodes.size());
        assertEquals("This is a test", textNodes.get(0).toString());
        assertEquals("This is another test", textNodes.get(1).toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Document(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTagName() {
        new Document("").createElement("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonExistentTagName() {
        Document doc = new Document("http://example.com");
        doc.createElement("invalid-tag");
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), settings.charset());
        assertEquals(true, settings.prettyPrint());
        settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
        settings.charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), doc.outputSettings().charset());
        settings.prettyPrint(false);
        assertEquals(false, doc.outputSettings().prettyPrint());
    }
}