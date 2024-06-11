package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for Document.java
 */
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
        assertNotNull(doc);
        assertNotNull(doc.getElementsByTag("html"));
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadElement() {
        assertNotNull(document.head());
    }

    @Test
    public void testGetBodyElement() {
        assertNotNull(document.body());
    }

    @Test
    public void testGetTitle() {
        assertNull(document.title());
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
        document.title("Test Title");
        document.body().appendElement("Test Text");
        document.head().appendElement("Test Tag");
        document.appendElement("Test Tag 2");

        document.normalise();

        Element title = document.getElementsByTag("title").first();
        assertEquals("Test Title", title.text().trim());

        Element body = document.body();
        List<Node> bodyChildren = new ArrayList<Node>(body.childNodes());
        assertEquals(2, bodyChildren.size());
        assertEquals("Test Text", bodyChildren.get(0).toString());
        assertEquals("", bodyChildren.get(1).toString());

        List<Node> headChildren = new ArrayList<Node>(document.head().childNodes());
        assertEquals(1, headChildren.size());
        assertEquals("Test Tag", headChildren.get(0).toString());

        List<Node> documentChildren = new ArrayList<Node>(document.childNodes());
        assertEquals(3, documentChildren.size());
        assertEquals("#document", documentChildren.get(0).nodeName());
        assertEquals("html", documentChildren.get(1).nodeName());
        assertEquals("body", documentChildren.get(2).nodeName());
    }

    @Test
    public void testOuterHtml() {
        document.title("Test Title");
        document.body().appendElement("Test Text");
        document.appendElement("Test Tag");

        document.normalise();

        assertEquals("<html><head><title>Test Title</title></head>" +
                     "<body><p></p></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        document.title("Test Title");
        document.body().appendElement("Test Text");

        document.normalise();

        assertEquals("Test Text\n", document.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Validate.notNull(Document.createShell(null));
    }
}