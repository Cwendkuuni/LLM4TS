package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Tag;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        assertNotNull(html);
        assertEquals("head", html.tagName());

        html = document.body();
        assertNotNull(html);
        assertEquals("body", html.tagName());

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testGetHeadAndBody() {
        Document document = Document.createShell("http://example.com");

        Element head = document.head();
        assertNotNull(head);

        Element body = document.body();
        assertNotNull(body);

        assertEquals(head, document.head());
        assertEquals(body, document.body());
    }

    @Test
    public void testGetTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");

        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title 1");
        document.title("Test Title 2");

        String title = document.title();
        assertEquals("Test Title 2", title);
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element element = document.createElement("div");

        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        Element title = document.createElement("title");
        title.text("Test Title");
        document.head().appendChild(title);

        Element body = document.createElement("body");
        TextNode textNode1 = document.createElementTextNode("Test Text 1");
        TextNode textNode2 = document.createElementTextNode("Test Text 2");
        body.appendChild(textNode1);
        body.appendChild(textNode2);
        document.appendChild(title);
        document.appendChild(body);

        document.normalise();

        Element head = document.head();
        Element titleElement = head.querySelector("title");
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title", titleElement.text());

        Element bodyElement = document.body();
        List<Node> childNodes = bodyElement.childNodes();
        assertEquals(2, childNodes.size());
        TextNode firstTextNode = (TextNode) childNodes.get(0);
        TextNode secondTextNode = (TextNode) childNodes.get(1);
        assertEquals("Test Text 1", firstTextNode.text());
        assertEquals("Test Text 2", secondTextNode.text());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = Document.createShell("http://example.com");
        Element title = document.createElement("title");
        title.text("Test Title 1");
        document.head().appendChild(title);

        Element body = document.createElement("body");
        TextNode textNode1 = document.createElementTextNode("Test Text 1 1");
        TextNode textNode1_1 = document.createElementTextNode(" Test Text 1 2");
        TextNode textNode2 = document.createElementTextNode("Test Text 2 1");
        TextNode textNode2_1 = document.createElementTextNode(" Test Text 2 2");
        TextNode textNode3 = document.createElementTextNode("Test Text 3");
        body.appendChild(textNode1);
        body.appendChild(textNode1_1);
        body.appendChild(textNode2);
        body.appendChild(textNode2_1);
        body.appendChild(textNode3);
        document.appendChild(title);
        document.appendChild(body);

        document.normalise();

        Element head = document.head();
        Element titleElement = head.querySelector("title");
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title 1Test Text 1 1 Test Text 1 2", titleElement.text());

        Element bodyElement = document.body();
        List<Node> childNodes = bodyElement.childNodes();
        assertEquals(3, childNodes.size());
        TextNode mergedTextNode = (TextNode) childNodes.get(0);
        assertEquals("Test Title 1Test Text 1 1 Test Text 1 2", mergedTextNode.text());
        TextNode secondTextNode = (TextNode) childNodes.get(1);
        assertEquals("Test Text 2 1 Test Text 2 2", secondTextNode.text());
        TextNode thirdTextNode = (TextNode) childNodes.get(2);
        assertEquals("Test Text 3", thirdTextNode.text());
    }

    @Test
    public void testNormaliseStructure() {
        Document document = Document.createShell("http://example.com");
        Element title1 = document.createElement("title");
        title1.text("Test Title 1");
        document.head().appendChild(title1);

        Element title2 = document.createElement("title");
        title2.text("Test Title 2");
        document.head().appendChild(title2);

        Element body = document.createElement("body");
        TextNode textNode1 = document.createElementTextNode("Test Text 1");
        body.appendChild(textNode1);
        document.appendChild(title1);
        document.appendChild(title2);
        document.appendChild(body);

        document.normalise();

        Element headElement = document.head();
        Element titleElement = headElement.querySelector("title");
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title 1Test Text 1", titleElement.text());

        Element bodyElement = document.body();
        TextNode textNode = bodyElement.querySelectorText("Test Text 1");
        assertEquals("Test Text 1", textNode.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        Element title = document.createElement("title");
        title.text("Test Title");
        document.head().appendChild(title);

        Element body = document.createElement("body");
        TextNode textNode1 = document.createElementTextNode("Test Text 1");
        TextNode textNode2 = document.createElementTextNode("Test Text 2");
        body.appendChild(textNode1);
        body.appendChild(textNode2);
        document.appendChild(title);
        document.appendChild(body);

        document.normalise();

        String outerHtml = document.outerHtml();
        String expected = "<html><head><title>Test TitleTest Text 1 Test Text 2</title></head><body>Test Text 1 Test Text 2</body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        Element body = document.createElement("body");
        TextNode textNode = document.createElementTextNode("Test Text");
        body.appendChild(textNode);
        document.appendChild(body);

        String text = document.text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testClone() {
        Document document = Document.createShell("http://example.com");
        Element title = document.createElement("title");
        title.text("Test Title");
        document.head().appendChild(title);

        Element body = document.createElement("body");
        TextNode textNode1 = document.createElementTextNode("Test Text 1");
        TextNode textNode2 = document.createElementTextNode("Test Text 2");
        body.appendChild(textNode1);
        body.appendChild(textNode2);
        document.appendChild(title);
        document.appendChild(body);

        document.normalise();

        Document clonedDocument = document.clone();

        Element headElement = document.head();
        Element titleElement = headElement.querySelector("title");
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Title", titleElement.text());

        Element bodyElement = document.body();
        List<Node> childNodes = bodyElement.childNodes();
        assertEquals(2, childNodes.size());
        TextNode firstTextNode = (TextNode) childNodes.get(0);
        TextNode secondTextNode = (TextNode) childNodes.get(1);
        assertEquals("Test Text 1", firstTextNode.text());
        assertEquals("Test Text 2", secondTextNode.text());

        Element headElementCloned = clonedDocument.head();
        Element titleElementCloned = headElementCloned.querySelector("title");
        assertEquals("title", titleElementCloned.tagName());
        assertEquals("Test Title", titleElementCloned.text());

        Element bodyElementCloned = clonedDocument.body();
        List<Node> childNodesCloned = bodyElementCloned.childNodes();
        assertEquals(2, childNodesCloned.size());
        TextNode firstTextNodeCloned = (TextNode) childNodesCloned.get(0);
        TextNode secondTextNodeCloned = (TextNode) childNodesCloned.get(1);
        assertEquals("Test Text 1", firstTextNodeCloned.text());
        assertEquals("Test Text 2", secondTextNodeCloned.text());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        Document documentWithOutputSettings = document.outputSettings(outputSettings);

        Charset charset = Charset.forName("UTF-8");
        Document documentWithCharset = document.charset(charset);

        assertEquals(outputSettings, documentWithOutputSettings.outputSettings());
        assertEquals(charset, documentWithCharset.charset());
    }

    @Test
    public void testGetQuirksMode() {
        Document document = Document.createShell("http://example.com");
        assertEquals(QuirksMode.noQuirks, document.quirksMode());
    }

    @Test
    public void testSetQuirksMode() {
        Document document = Document.createShell("http://example.com");
        document.quirksMode(QuirksMode.quirks);
        assertEquals(QuirksMode.quirks, document.quirksMode());
    }
}