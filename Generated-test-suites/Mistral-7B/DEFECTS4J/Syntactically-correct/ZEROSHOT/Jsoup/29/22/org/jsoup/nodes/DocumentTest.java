package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author [Your Name]
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.title());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.head();
        Element body = doc.body();

        assertEquals("#head", head.nodeName());
        assertEquals("#body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        String title = doc.title();

        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title(null);
        doc.title("Test Title");
        String title = doc.title();

        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element element = doc.createElement("a");

        assertEquals("a", element.nodeName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Document.createShell("baseUri");
        doc.head().appendElement("title").text("Test Title");
        doc.body().appendElement("p").text("Test Paragraph");
        doc.appendElement("div").text("Test Div");

        doc.normalise();

        Element head = doc.head();
        Element title = head.getElementsByTag("title").first();
        Element body = doc.body();
        List<Element> divs = doc.getElementsByTag("div");

        assertEquals(1, head.childNodes().size());
        assertEquals("Test Title", title.text());
        assertEquals(1, body.childNodes().size());
        assertEquals("Test Paragraph", body.childNodes().get(0).text());
        assertEquals(0, divs.size());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.head();
        Element title = doc.createElement("title");
        title.text("Test Title");
        head.appendChild(title);
        Element body = doc.body();

        doc.normalise();

        Element foundTitle = doc.getElementsByTag("title").first();
        assertEquals(title, foundTitle);
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = Document.createShell("baseUri");
        Element head1 = doc.createElement("head");
        Element title1 = doc.createElement("title").text("Test Title 1");
        head1.appendChild(title1);
        doc.appendChild(head1);

        Element head2 = doc.createElement("head");
        Element title2 = doc.createElement("title").text("Test Title 2");
        head2.appendChild(title2);
        doc.appendChild(head2);

        doc.normalise();

        Element foundTitle = doc.getElementsByTag("title").first();
        assertEquals(title1, foundTitle);
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element p = doc.createElement("p").text("Test Paragraph");
        doc.body().appendChild(p);

        String html = doc.outerHtml();

        String expectedHtml = "<html><head></head><body><p>Test Paragraph</p></body></html>";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("baseUri");
        Element p = doc.createElement("p").text("Test Paragraph");
        doc.body().appendChild(p);

        String text = doc.text();

        assertEquals("Test Paragraph", text);
    }

    @Test
    public void testClone() {
        Document doc1 = Document.createShell("baseUri");
        Document doc2 = doc1.clone();

        Element p1 = doc1.createElement("p").text("Test Paragraph 1");
        Element p2 = doc1.createElement("p").text("Test Paragraph 2");
        doc1.body().appendChild(p1);
        doc1.body().appendChild(p2);

        doc2.title("Test Title");
        Element p3 = doc2.createElement("p").text("Test Paragraph 3");
        doc2.body().appendChild(p3);

        assertEquals(doc1.title(), doc2.title());
        assertEquals(2, doc1.body().childNodes().size());
        assertEquals(1, doc2.body().childNodes().size());
        assertNotEquals(p1, p3);
    }
}