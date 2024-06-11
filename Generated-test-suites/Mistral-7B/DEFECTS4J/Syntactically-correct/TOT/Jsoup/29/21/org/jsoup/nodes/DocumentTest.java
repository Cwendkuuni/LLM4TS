package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(Tag.HTML, doc.tagName());
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals(Tag.HEAD, html.tagName());
        Element head = html.body();
        Assert.assertNull(head);
        head = doc.appendElement("head");
        Assert.assertNotNull(head);
        Assert.assertEquals(Tag.HEAD, head.tagName());
        Element body = doc.appendElement("body");
        Assert.assertNotNull(body);
        Assert.assertEquals(Tag.BODY, body.tagName());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(Tag.HTML, doc.tagName());
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals(Tag.HEAD, html.tagName());
        Element head = html.body();
        Assert.assertNotNull(head);
        Assert.assertEquals(Tag.BODY, head.tagName());
    }

    @Test
    public void testAccessHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
        Assert.assertNotSame(head, body);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        String title = "Test Title";
        doc.title(title);
        Assert.assertEquals(title, doc.title());
    }

    @Test
    public void testGetTitleWhenNoneSet() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals(Tag.DIV, element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalizeDocument() {
        Document doc = new Document("http://example.com");
        Element title = doc.appendElement("title").text("Test Title");
        Element body = doc.appendElement("body");
        TextNode textNode1 = body.appendText("Test Text 1");
        TextNode textNode2 = body.appendText("Test Text 2");
        Element pTag = body.appendElement("p");
        pTag.appendText("Test Paragraph");

        doc.normalise();

        Element head = doc.head();
        Element titleElement = head.getElementsByTag("title").first();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text().trim());

        Element bodyElement = doc.body();
        List<TextNode> textNodes = new ArrayList<>();
        for (Node node : bodyElement.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add((TextNode) node);
            }
        }
        Assert.assertEquals(3, textNodes.size());
        Assert.assertEquals("Test Text 1", textNodes.get(0).text().trim());
        Assert.assertEquals("Test Text 2", textNodes.get(1).text().trim());
        Assert.assertEquals("Test Paragraph", textNodes.get(2).text().trim());
    }
}