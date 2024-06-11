package org.jsoup.nodes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.nio.charset.StandardCharsets;

public class DocumentTest {

    private Document document;

    @BeforeEach
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        Assertions.assertEquals("http://example.com", doc.baseUri());
        Assertions.assertNotNull(doc.head());
        Assertions.assertNotNull(doc.body());
    }

    @Test
    public void testHeadAndBodyAccessors() {
        Element head = document.head();
        Element body = document.body();

        document.title("Test Title");
        Assertions.assertEquals("Test Title", document.title());
        document.title("");
        Assertions.assertEquals("", document.title());

        document.title("Test Title");
        Element titleElement = head.getElementsByTag("title").first();
        Assertions.assertEquals("Test Title", titleElement.text().trim());

        document.title("Test Title");
        Element newTitleElement = head.appendElement("title").text("New Title");
        Assertions.assertEquals("New Title", document.title());
        Assertions.assertEquals("New Title", titleElement.text().trim());

        Assertions.assertNotNull(body.text(null));
        Assertions.assertEquals("", body.text().trim());
        body.text("Test Body");
        Assertions.assertEquals("Test Body", body.text().trim());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        document.body().appendChild(element);
        Assertions.assertEquals("div", element.nodeName());
        Assertions.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.appendText("Test before normalisation");
        document.body().appendText("Test after normalisation");
        document.normalise();

        int counterBefore = 0;
        int counterAfter = 0;

        for (Node node : document.childNodes()) {
            if (node instanceof TextNode && node.text().trim().length() > 0) {
                if (node.previousSibling() instanceof TextNode) {
                    counterBefore++;
                }
                counterAfter++;
            }
        }

        Assertions.assertEquals(1, counterBefore);
        Assertions.assertEquals(1, counterAfter);
    }

    @Test
    public void testOutputSettings() {
        document.outputSettings().escapeMode(Entities.EscapeMode.extended);
        document.outputSettings().charset("UTF-16");
        document.outputSettings().prettyPrint(false);
        document.outputSettings().indentAmount(2);

        String html = document.outerHtml();
        Assertions.assertFalse(html.contains("&amp;"));
        Assertions.assertTrue(html.contains("%F0%AD"));
        Assertions.assertFalse(html.contains("\n"));
        Assertions.assertFalse(html.contains("\t"));

        document.outputSettings().escapeMode(Entities.EscapeMode.base);
        document.outputSettings().charset(StandardCharsets.UTF_8);
        document.outputSettings().prettyPrint(true);
        document.outputSettings().indentAmount(4);

        html = document.outerHtml();
        Assertions.assertTrue(html.contains("&amp;"));
        Assertions.assertFalse(html.contains("%F0%AD"));
        Assertions.assertTrue(html.contains("\n"));
        Assertions.assertTrue(html.contains("\t"));
    }
}