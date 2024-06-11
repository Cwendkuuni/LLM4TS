package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Jsoup.parse("<html><head></head><body></body></html>", "base.html");
    }

    @Test
    public void testCreateDocument() {
        Document emptyDocument = new Document("base.html");
        Assert.assertNotNull(emptyDocument);

        Document shellDocument = Document.createShell("base.html");
        Assert.assertNotNull(shellDocument);
        Assert.assertEquals(3, shellDocument.childNodeSize());
        Assert.assertNotNull(shellDocument.head());
        Assert.assertNotNull(shellDocument.body());
    }

    @Test
    public void testGetHeadAndBody() {
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());

        Element head = document.head();
        Element body = document.body();

        Assert.assertEquals(Tag.HTML, head.tagName());
        Assert.assertEquals(Tag.BODY, body.tagName());

        Assert.assertEquals(0, head.childNodeSize());
        Assert.assertEquals(0, body.childNodeSize());
    }

    @Test
    public void testGetTitle() {
        document.title("Test Title");

        String title = document.title();
        Assert.assertEquals("Test Title", title);

        Element titleElement = document.getElementsByTag("title").first();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text().trim());
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");

        String title = document.title();
        Assert.assertEquals("Test Title", title);

        document.title("New Title");
        title = document.title();
        Assert.assertEquals("New Title", title);

        Element titleElement = document.getElementsByTag("title").first();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("New Title", titleElement.text().trim());
    }

    @Test
    public void testCreateElement() {
        Element newElement = document.createElement("div");
        Assert.assertNotNull(newElement);
        Assert.assertEquals(Tag.DIV, newElement.tagName());
        Assert.assertEquals(document.baseUri(), newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        document.head().appendText("Test Head Content");
        document.body().appendText("Test Body Content");
        document.appendText("Test Document Content");

        document.normalise();

        Element body = document.body();
        Assert.assertEquals("Test Body Content", body.text());
        Assert.assertEquals(1, body.childNodeSize());

        Element title = document.getElementsByTag("title").first();
        Assert.assertTrue(title != null);
        Assert.assertTrue(title.childNodeSize() == 0);
    }
}