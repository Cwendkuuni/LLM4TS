package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        assertEquals("html", html.nodeName());
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitleElement() {
        Document doc = new Document("baseUri");
        doc.title("title");
        assertEquals("title", doc.title().nodeName());
        assertEquals("title", doc.title().text().trim());
    }

    @Test
    public void testSetTitleElement() {
        Document doc = new Document("baseUri");
        doc.title("title1");
        doc.title("title2");
        assertEquals("title2", doc.title().text().trim());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.createElement("title").text("title");
        head.appendChild(title);
        TextNode textNode = doc.createElement("TextNode").text("text");
        body.appendChild(textNode);

        doc.normalise();

        assertEquals(5, doc.childNodes().size());
        assertEquals("#document", doc.nodeName());
        Element htmlElement = doc.getElementsByTag("html").first();
        assertEquals("html", htmlElement.nodeName());
        assertEquals(4, htmlElement.childNodes().size());
        Element headElement = htmlElement.getElementsByTag("head").first();
        assertEquals("head", headElement.nodeName());
        assertEquals(1, headElement.childNodes().size());
        Element titleElement = headElement.getElementsByTag("title").first();
        assertEquals("title", titleElement.nodeName());
        assertEquals("title", titleElement.text().trim());
        Element bodyElement = htmlElement.getElementsByTag("body").first();
        assertEquals("body", bodyElement.nodeName());
        assertEquals(1, bodyElement.childNodes().size());
        TextNode textNodeInBody = bodyElement.getElementsByTag("TextNode").first();
        assertEquals("TextNode", textNodeInBody.nodeName());
        assertEquals("text", textNodeInBody.text().trim());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element foundHtml = Document.findFirstElementByTagName(Tag.HTML, doc);
        Element foundHead = Document.findFirstElementByTagName(Tag.HEAD, doc);
        Element foundBody = Document.findFirstElementByTagName(Tag.BODY, doc);

        assertEquals("html", foundHtml.nodeName());
        assertEquals("head", foundHead.nodeName());
        assertEquals("body", foundBody.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.createElement("title").text("title");
        head.appendChild(title);
        TextNode textNode = doc.createElement("TextNode").text("text");
        body.appendChild(textNode);

        doc.normalise();

        String outerHtml = doc.outerHtml();

        String expected = "<html><head><title>title</title></head><body>"
                + "<TextNode></TextNode></body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        TextNode textNode = doc.text("text");
        assertEquals("text", textNode.text().trim());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        Charset charset = Charset.forName("UTF-8");
        OutputSettings charsetOutputSettings = doc.outputSettings().charset(charset);

        assertEquals(Charset.forName("UTF-8"), charsetOutputSettings.charset());
    }
}