package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;

public class DocumentTest {

    @org.junit.Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertTrue(document.head() == null);
        assertTrue(document.body() == null);
    }

    @org.junit.Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @org.junit.Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @org.junit.Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @org.junit.Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element p1 = document.createElement("p").text("Test");
        Element p2 = document.createElement("p").text("Test 2");
        document.appendChild(p1);
        document.appendChild(p2);
        document.appendChild(document.createElementTextNode(" Test 3 "));
        document.normalise();
        Element body = document.body();
        List<Element> pElements = body.getElementsByTag("p");
        assertEquals(3, pElements.size());
        Element p = pElements.get(0);
        assertEquals("Test", p.text());
        p = pElements.get(1);
        assertEquals("Test 2", p.text());
        p = body.getElementsByTag("p").last();
        assertEquals(" Test 3 ", p.text().trim());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.createElement("html");
        Element head = document.createElement("head");
        Element body = document.createElement("body");
        document.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element headElement = document.findFirstElementByTagName("head", document);
        assertEquals(head, headElement);

        Element bodyElement = document.findFirstElementByTagName("body", document);
        assertEquals(body, bodyElement);

        Element nonExistentElement = document.findFirstElementByTagName("div", document);
        assertNull(nonExistentElement);
    }

    @org.junit.Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element p = document.createElement("p").text("Test");
        document.body().appendChild(p);
        assertEquals("<p>Test</p>", document.outerHtml());
    }

    @org.junit.Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.body().text("Test");
        assertEquals("Test", document.text());
    }

    @org.junit.Test
    public void testNodeName() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Document clonedDocument = document.clone();
        assertEquals(document.outputSettings().charset().name(), clonedDocument.outputSettings().charset().name());
        // You may add more assertions here to test other fields
    }
}