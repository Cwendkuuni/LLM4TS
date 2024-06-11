package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    /**
     * Test constructor Document.
     */
    @org.junit.Test
    public void testDocument() {
        Document doc = new Document("http://www.example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("http://www.example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());

        Document emptyShell = Document.createShell("http://www.example.com");
        assertNotNull(emptyShell);
        Element htmlEl = emptyShell.head();
        assertEquals("#html", htmlEl.nodeName());
        Element headEl = emptyShell.head();
        assertEquals("#head", headEl.nodeName());
        Element bodyEl = emptyShell.body();
        assertEquals("#body", bodyEl.nodeName());
        assertNull(emptyShell.title());
    }

    /**
     * Test method head.
     */
    @org.junit.Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertNull(head);

        Document emptyShell = Document.createShell("http://www.example.com");
        Element headEl = emptyShell.head();
        assertNotNull(headEl);
        assertEquals("#head", headEl.nodeName());

        Element newTitleEl = emptyShell.createElement("title");
        emptyShell.title("Test Title");
        assertEquals("Test Title", emptyShell.title());
        assertEquals("Test Title", headEl.child(0).text());
    }

    /**
     * Test method body.
     */
    @org.junit.Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertNull(body);

        Document emptyShell = Document.createShell("http://www.example.com");
        Element bodyEl = emptyShell.body();
        assertNotNull(bodyEl);
        assertEquals("#body", bodyEl.nodeName());

        Element newTextNode = emptyShell.createElement("text");
        bodyEl.appendText("Test Text");
        assertEquals("Test Text", bodyEl.text());
    }

    /**
     * Test method title.
     */
    @org.junit.Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        assertEquals("", doc.title());

        Document emptyShell = Document.createShell("http://www.example.com");
        emptyShell.title("Test Title");
        assertEquals("Test Title", emptyShell.title());
    }

    /**
     * Test method title.
     */
    @org.junit.Test
    public void testTitleSet() {
        Document doc = new Document("http://www.example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    /**
     * Test method createElement.
     */
    @org.junit.Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");
        Element newDivEl = doc.createElement("div");
        assertNotNull(newDivEl);
        assertEquals("#div", newDivEl.nodeName());
    }

    /**
     * Test method normalise.
     */
    @org.junit.Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        Element htmlEl = doc.createElement("html");
        Element headEl = doc.createElement("head");
        Element bodyEl = doc.createElement("body");
        Element titleEl = doc.createElement("title");
        titleEl.text("Test Title");
        Element textEl = doc.createElement("text");
        textEl.text("Test Text");

        htmlEl.appendChild(headEl);
        headEl.appendChild(titleEl);
        htmlEl.appendChild(bodyEl);
        bodyEl.appendChild(textEl);

        doc.appendChild(htmlEl);

        doc.normalise();

        assertEquals("#html", htmlEl.nodeName());
        assertEquals("#head", headEl.nodeName());
        assertEquals("#body", bodyEl.nodeName());
        assertEquals("#title", titleEl.nodeName());
        assertEquals("#text", textEl.nodeName());

        assertNull(headEl.parentNode());
        assertNull(bodyEl.parentNode());
        assertEquals(htmlEl, titleEl.parentNode());
        assertEquals(htmlEl, textEl.parentNode());

        Element body = doc.body();
        assertEquals("#body", body.nodeName());
        assertEquals("Test Text", body.text());
    }
}