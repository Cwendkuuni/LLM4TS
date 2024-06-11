package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());

        html = doc.body();
        assertNotNull(html);
        assertEquals("body", html.nodeName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.nodeName());
    }

    @Test
    public void testNormalise() {
        document.title("Test Title 1");
        document.text("Test Text 1");
        document.body().appendText("Test Text 2");

        document.normalise();

        String title = document.title();
        assertEquals("Test Title 1", title);

        String bodyText = document.body().text();
        assertEquals("Test Text 1 Test Text 2", bodyText);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element p = document.createElement("p");
        body.appendElement(p);

        Element found = document.findFirstElementByTagName("body", document);
        assertEquals(body, found);

        found = document.findFirstElementByTagName("head", document);
        assertEquals(head, found);

        found = document.findFirstElementByTagName("p", document);
        assertEquals(p, found);

        found = document.findFirstElementByTagName("p", body);
        assertEquals(p, found);

        found = document.findFirstElementByTagName("head", body);
        assertNull(found);

        found = document.findFirstElementByTagName("body", head);
        assertNull(found);
    }

    @Test
    public void testOuterHtml() {
        document.title("Test Title");
        document.text("Test Text");
        document.body().appendText("Test Text 2");

        String html = document.outerHtml();
        String expected = "<html><head><title>Test Title</title></head><body>" +
                "Test Text Test Text 2</body></html>";
        assertEquals(expected, html);
    }

    @Test
    public void testText() {
        document.title("Test Title");
        document.text("Test Text 1");
        document.body().appendText("Test Text 2");

        document.text("Test Text 3");

        String text = document.text();
        String expected = "Test Text 3";
        assertEquals(expected, text);

        text = document.body().text();
        expected = "Test Text 1 Test Text 2";
        assertEquals(expected, text);
    }

    @Test
    public void testNormaliseTextNodes() {
        Element head = document.appendElement("head");
        document.appendText("Test Text 1");
        document.appendText("Test Text 2");
        body().appendText("Test Text 3");

        document.normalise();

        Node firstNode = head.childNodes().get(0);
        assertTrue(firstNode instanceof TextNode);
        TextNode textNode = (TextNode) firstNode;
        assertEquals("Test Text 1 Test Text 2", textNode.text());

        textNode = (TextNode) body().childNodes().get(0);
        assertEquals("Test Text 3", textNode.text());
    }
}