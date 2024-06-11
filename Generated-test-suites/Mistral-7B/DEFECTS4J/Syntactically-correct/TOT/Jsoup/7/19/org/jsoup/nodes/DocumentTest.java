package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document createdDocument = new Document("http://example.com");
        Assert.assertEquals("http://example.com", createdDocument.baseUri());
        Assert.assertNotNull(createdDocument.head());
        Assert.assertNotNull(createdDocument.body());
    }

    @Test
    public void testAccessorMethodsForHeadAndBody() {
        Element head = document.head();
        Element body = document.body();

        Assert.assertNotNull(head);
        Assert.assertNotNull(body);

        Assert.assertEquals(document, head.parentNode());
        Assert.assertEquals(document, body.parentNode());
    }

    @Test
    public void testSettingAndGettingTitle() {
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);

        document.title(null); // test null title
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testCreatingElementWithBaseUri() {
        Element newElement = document.createElement("a");
        Assert.assertEquals("http://example.com", newElement.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        document.appendText("Test text outside body");
        Element newElement = document.createElement("p");
        newElement.text("Test text inside new element");
        document.insertBefore(newElement, document.body());

        document.normalise();

        List<Node> bodyNodes = document.body().childNodes();
        Assert.assertEquals(3, bodyNodes.size());
        Assert.assertEquals("Test text outside body", bodyNodes.get(0).text());
        Assert.assertEquals("Test text inside new element", bodyNodes.get(1).text());
        Assert.assertEquals("", bodyNodes.get(2).text()); // blank TextNode created for whitespace
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameter() {
        document.title(null);
    }

    @Test
    public void testSettingAndGettingOutputSettings() {
        OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals(StandardCharsets.UTF_8, outputSettings.charset());

        document.outputSettings().prettyPrint(false);
        String html = document.html();
        Assert.assertFalse(html.contains("\n")); // no line breaks when pretty print is disabled

        document.outputSettings().charset("ISO-8859-1");
        String encodedHtml = document.html();
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset().name());
        Assert.assertTrue(encodedHtml.contains("%E1%88%AB")); // special character "A with grave"
    }
}