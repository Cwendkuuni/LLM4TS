package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocumentWithBaseUri() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
        assertEquals("baseUri", document.baseUri());
    }

    @Test
    public void testCreateShellWithBaseUri() {
        Document document = Document.createShell("baseUri");
        Element html = document.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals("baseUri", document.baseUri());
    }

    @Test
    public void testHeadMethod() {
        Document document = Document.createShell("baseUri");
        Element head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBodyMethod() {
        Document document = Document.createShell("baseUri");
        Element body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitleMethod() {
        Document document = Document.createShell("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitleMethod() {
        Document document = Document.createShell("baseUri");
        document.title(null);
        String title = document.title();
        assertEquals("", title);
        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = Document.createShell("baseUri");
        Element newElement = document.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.tagName());
        assertEquals("baseUri", newElement.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = Document.createShell("baseUri");
        Element html = document.select("html").first();
        Element title = html.appendElement("title").text("Test Title");
        Element body = html.appendElement("body").text("Test Body");
        Element textNode = html.appendText("Test Text");

        document.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals(3, bodyChildren.size());
        Node titleNode = bodyChildren.get(0);
        assertEquals(Element.class, titleNode.getClass());
        Element titleElement = (Element) titleNode;
        assertEquals("Test Title", titleElement.text());

        Node textNodeInBody = bodyChildren.get(1);
        assertEquals(TextNode.class, textNodeInBody.getClass());
        TextNode bodyTextNode = (TextNode) textNodeInBody;
        assertEquals(" Test Text", bodyTextNode.text());

        Node textNodeInHead = bodyChildren.get(2);
        assertEquals(TextNode.class, textNodeInHead.getClass());
        TextNode headTextNode = (TextNode) textNodeInHead;
        assertEquals("", headTextNode.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseWithNullDocument() {
        normalise(null);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = Document.createShell("baseUri");
        Element html = document.select("html").first();
        html.appendElement("title").text("Test Title");
        html.appendElement("body").text("Test Body");

        String outerHtml = document.outerHtml();
        String expected = "<html><head></head><body>Test Body</body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testTextMethod() {
        Document document = Document.createShell("baseUri");
        document.body().text("Test Body");
        String text = document.text();
        assertEquals("Test Body", text);
    }
}