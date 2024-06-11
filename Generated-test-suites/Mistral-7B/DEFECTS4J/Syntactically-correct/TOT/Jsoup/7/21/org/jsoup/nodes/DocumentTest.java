package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.getFirstElementChild();
        Assert.assertNotNull(head);
        Element body = head.getNextElementSibling();
        Assert.assertNotNull(body);
        Assert.assertEquals("head", head.tagName());
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testAccessHeadAndBodyElements() {
        Document document = Document.createShell("http://example.com");
        Element head = document.head();
        Element body = document.body();

        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());

        TextNode textNode = document.createElement("p").text("Test Text");
        body.appendChild(textNode);
        Assert.assertEquals("Test Text", textNode.text());
        Assert.assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = Document.createShell("http://example.com");
        document.appendChild(new Element("p").text("Text 1 "));
        document.head().appendChild(new Element("title").text("Title 1"));
        document.body().appendChild(new Element("p").text("Text 2 "));
        document.head().appendChild(new Element("p").text("Text 3 "));

        document.normalise();

        Element head = document.head();
        Element title = head.getFirstElementChild();
        Assert.assertEquals("title", title.tagName());
        Assert.assertEquals("Title 1", title.text().trim());
        Assert.assertEquals(2, head.childNodeSize());

        Element body = document.body();
        Assert.assertEquals(3, body.childNodeSize());
        TextNode text1 = (TextNode) body.get(0);
        Assert.assertEquals("Text 1 ", text1.text().trim());
        TextNode text2 = (TextNode) body.get(1);
        Assert.assertEquals("Text 2 ", text2.text().trim());
        TextNode text3 = (TextNode) body.get(2);
        Assert.assertEquals("", text3.text().trim());
    }

    @Test
    public void testTitleSettingAndGetting() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("", document.title());

        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());

        document.title(null);
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testCreateElementWithBaseUri() {
        Document document = Document.createShell("http://example.com");
        Element link = document.createElement("a");
        Assert.assertEquals("a", link.tagName());
        Assert.assertEquals("http://example.com", link.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseWithNullElement() {
        Document document = Document.createShell("http://example.com");
        document.normalise(null);
    }
}