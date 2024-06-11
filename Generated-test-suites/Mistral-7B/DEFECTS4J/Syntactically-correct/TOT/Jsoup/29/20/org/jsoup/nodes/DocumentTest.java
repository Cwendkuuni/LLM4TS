package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        Element html = document.head();
        Element head = document.head();
        Element body = document.body();

        assertEquals(Tag.HTML, html.tagName());
        assertEquals(Tag.HEAD, head.tagName());
        assertEquals(Tag.BODY, body.tagName());
        assertNull(document.title());
        assertNull(html.text());
        assertNull(head.text());
        assertNull(body.text());
    }

    @Test
    public void testAccessHeadAndBody() {
        Element title = document.title();
        document.title("Test Title");
        assertEquals("Test Title", title.text().trim());

        Element newElement = document.createElement("div");
        document.body().appendElement(newElement);
        Element appendElement = document.body().lastChild();
        assertSame(newElement, appendElement);
        assertEquals(Tag.DIV, appendElement.tagName());
    }

    @Test
    public void testGetAndSetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.title(null);
        assertNull(document.title());
    }

    @Test
    public void testNormalise() {
        document.body().appendElement("Test Text");
        document.body().appendElement("Test Text 2");
        document.text("Test Text 3");

        document.normalise();

        Element body = document.body();
        Element testText1 = body.getElementsByText("Test Text").first();
        Element testText2 = body.getElementsByText("Test Text 2").first();
        TextNode testText3 = (TextNode) body.childNodes().get(0);

        assertNotNull(testText1);
        assertNotNull(testText2);
        assertNotNull(testText3);
        assertEquals("Test Text", testText1.text());
        assertEquals("Test Text 2", testText2.text());
        assertEquals(" Test Text 3\n", testText3.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        document.body().appendElement("html");
        document.body().appendElement("head");
        document.body().appendElement("body");

        Element html = document.findFirstElementByTagName("html", this.document);
        Element head = document.findFirstElementByTagName("head", this.document);
        Element body = document.findFirstElementByTagName("body", this.document);

        assertSame(document.getElementsByTagName("html").get(0), html);
        assertSame(document.getElementsByTagName("head").get(0), head);
        assertSame(document.getElementsByTagName("body").get(0), body);
    }

    @Test
    public void testOuterHtml() {
        document.title("Test Title");
        document.body().appendElement("Test Text");

        String expected = "<html><head><title>Test Title</title></head><body>Test Text</body></html>";
        String actual = document.outerHtml();

        assertEquals(expected, actual);
    }

    @Test
    public void testText() {
        document.title("Test Title");
        document.body().appendElement("Test Text");

        document.text("Test Text 3");

        String expected = "<html><head><title>Test Title</title></head><body>Test Text 3</body></html>";
        String actual = document.html();

        assertEquals(expected, document.outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCloneWithNullOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = null;
        Document clonedDocument = document.clone();
        clonedDocument.outputSettings(outputSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentWithNullBaseUri() {
        Document document = new Document(null);
    }
}