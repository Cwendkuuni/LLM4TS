package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.nio.charset.Charset;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        assertEquals("#document", html.nodeName());
        assertEquals(baseUri, html.baseUri());
        assertEquals("html", html.tagName());
        Element head = html.head();
        assertEquals("#head", head.nodeName());
        assertEquals(baseUri, head.baseUri());
        Element body = html.body();
        assertEquals("#body", body.nodeName());
        assertEquals(baseUri, body.baseUri());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element p = doc.createElement("p");
        assertEquals("#p", p.nodeName());
        assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.createElement("title").text("Test Title");
        head.appendElement(title);
        Element p = doc.createElement("p").text("Test Paragraph");
        body.appendElement(p);
        doc.appendChild(html);
        doc.normalise();
        assertEquals(1, html.childNodes().size());
        assertEquals(3, html.childNodes().get(0).childNodes().size());
        assertEquals("html", html.childNodes().get(0).tagName());
        assertEquals("#head", html.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("#title", html.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Test Title", html.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
        assertEquals("#body", html.childNodes().get(0).childNodes().get(1).tagName());
        assertEquals("#p", html.childNodes().get(0).childNodes().get(1).childNodes().get(0).tagName());
        assertEquals("Test Paragraph", html.childNodes().get(0).childNodes().get(1).childNodes().get(0).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        doc.title("Test Title");
        doc.text("Test Text");
        Element p = doc.createElement("p").text("Test Paragraph");
        body.appendElement(p);
        doc.appendChild(html);
        Element title = doc.findFirstElementByTagName("title", doc);
        assertEquals("#title", title.nodeName());
        assertEquals("Test Title", title.text().trim());
        Element bodyElement = doc.findFirstElementByTagName("body", doc);
        assertEquals("#body", bodyElement.nodeName());
        assertEquals("#p", bodyElement.childNodes().get(0).tagName());
        assertEquals("Test Paragraph", bodyElement.childNodes().get(0).text());
        Element headElement = doc.findFirstElementByTagName("head", doc);
        assertEquals("#head", headElement.nodeName());
        assertNull(doc.findFirstElementByTagName("script", doc));
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        doc.text("Test Text");
        Element p = doc.createElement("p").text("Test Paragraph");
        doc.body().appendElement(p);
        doc.normalise();
        String expected = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test Paragraph</p></body></html>";
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        doc.text("Test Text");
        Element p = doc.createElement("p").text("Test Paragraph");
        doc.body().appendElement(p);
        doc.normalise();
        String expected = "Test Text<p>Test Paragraph</p>";
        assertEquals(expected, doc.text().toString());
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testSetOutputSettings() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        OutputSettings outputSettings = doc.outputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        outputSettings.charset("ISO-8859-1");
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
        assertEquals(Charset.forName("ISO-8859-1"), doc.outputSettings().charset());
        assertFalse(doc.outputSettings().prettyPrint());
        assertEquals(2, doc.outputSettings().indentAmount());
    }
}