package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java.
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Set up for the tests
    }

    @org.junit.After
    public void tearDown() {
        // Clean up after the tests
    }

    /**
     * Test constructor Document.
     */
    @org.junit.Test
    public void testConstructor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertEquals(Tag.valueOf("#root"), document.tagName());
        assertNull(document.head());
        assertNull(document.body());
        assertEquals("", document.title());
    }

    /**
     * Test method createShell.
     */
    @org.junit.Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        Element head = html.child(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());
        Element body = html.child(1);
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals("", document.title());
    }

    /**
     * Test method head.
     */
    @org.junit.Test
    public void testHead() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element head = document.head();
        Element title = head.appendElement("title");
        title.text("Test Title");
        assertEquals("Test Title", document.title());
    }

    /**
     * Test method body.
     */
    @org.junit.Test
    public void testBody() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element body = document.body();
        body.appendText("Test Body");
        assertEquals("Test Body", document.body().text());
    }

    /**
     * Test method title.
     */
    @org.junit.Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element title = document.head().appendElement("title");
        title.text("Test Title");
        assertEquals("Test Title", document.title());
    }

    /**
     * Test method title.
     */
    @org.junit.Test
    public void testTitleSet() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    /**
     * Test method createElement.
     */
    @org.junit.Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    /**
     * Test method normalise.
     */
    @org.junit.Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title1 = head.appendElement("title");
        title1.text("Title 1");
        Element title2 = head.appendElement("title");
        title2.text("Title 2");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("Test 1 ");
        TextNode textNode2 = body.appendText("Test 2");

        document.normalise();

        Element headElement = document.head();
        assertEquals(1, headElement.childSize());
        assertEquals("Title 1", headElement.child(0).text());

        Element bodyElement = document.body();
        assertEquals("Test 1 Test 2", bodyElement.text());
    }

    /**
     * Test method findFirstElementByTagName.
     */
    @org.junit.Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = findFirstElementByTagName(document, "title");
        assertNull(title);

        title = head.appendElement("title");
        title.text("Test Title");

        Element title1 = document.findFirstElementByTagName("title");
        assertEquals("title", title1.tagName());
        assertEquals("Test Title", title1.text());

        Element title2 = findFirstElementByTagName(document, "title", head);
        assertEquals("title", title2.tagName());
        assertEquals("Test Title", title2.text());

        Element body1 = document.findFirstElementByTagName("body");
        assertEquals("body", body1.tagName());
        assertEquals(document, body1.parent());

        Element body2 = findFirstElementByTagName(head, "body");
        assertNull(body2);
    }

    private Element findFirstElementByTagName(Document document, String tagName) {
        return document.findFirstElementByTagName(tagName, null);
    }

    private Element findFirstElementByTagName(Node node, String tagName) {
        return findFirstElementByTagName(node, null);
    }

    private Element findFirstElementByTagName(Node node, Element parent) {
        if (node.nodeName().equals(tagName)) {
            return (Element) node;
        }

        for (Node child : node.childNodes()) {
            Element found = findFirstElementByTagName(child, parent);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Test method outerHtml.
     */
    @org.junit.Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Title");
        Element body = html.appendElement("body");
        body.appendText("Test Body");

        String htmlString = document.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", htmlString);
    }

    /**
     * Test method text.
     */
    @org.junit.Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element body = document.body();
        body.appendText("Test Text");

        String text = document.text();
        assertEquals("Test Text", text);
    }

    /**
     * Test method nodeName.
     */
    @org.junit.Test
    public void testNodeName() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertEquals("#document", document.nodeName());
    }

    /**
     * Test method clone.
     */
    @org.junit.Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Document clonedDocument = document.clone();
        assertEquals("#document", clonedDocument.nodeName());
        assertEquals(baseUri, clonedDocument.baseUri());

        Element html = clonedDocument.head();
        Element title = html.appendElement("title");
        title.text("Cloned Title");

        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertEquals(0, document.head().childSize());
        assertEquals("Test Title", document.title());

        assertEquals("#document", clonedDocument.nodeName());
        assertEquals(baseUri, clonedDocument.baseUri());
        assertEquals(1, clonedDocument.head().childSize());
        assertEquals("Cloned Title", clonedDocument.title());
    }

    /**
     * Test method outputSettings.
     */
    @org.junit.Test
    public void testOutputSettings() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);

        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = new OutputSettings();
        newOutputSettings.escapeMode(Entities.EscapeMode.extended);
        newOutputSettings.charset("US-ASCII");
        newOutputSettings.prettyPrint(false);
        newOutputSettings.indentAmount(2);

        document.outputSettings(newOutputSettings);

        OutputSettings outputSettings1 = document.outputSettings();
        assertEquals(Entities.EscapeMode.extended, outputSettings1.escapeMode());
        assertEquals(Charset.forName("US-ASCII"), outputSettings1.charset());
        assertFalse(outputSettings1.prettyPrint());
        assertEquals(2, outputSettings1.indentAmount());
    }

    /**
     * Test method quirksMode.
     */
    @org.junit.Test
    public void testQuirksMode() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);

        QuirksMode quirksMode = QuirksMode.noQuirks;
        document.quirksMode(quirksMode);

        assertEquals(QuirksMode.noQuirks, document.quirksMode());

        QuirksMode quirksMode1 = QuirksMode.quirks;
        document.quirksMode(quirksMode1);

        assertEquals(QuirksMode.quirks, document.quirksMode());

        QuirksMode quirksMode2 = QuirksMode.limitedQuirks;
        document.quirksMode(quirksMode2);

        assertEquals(QuirksMode.limitedQuirks, document.quirksMode());
    }
}