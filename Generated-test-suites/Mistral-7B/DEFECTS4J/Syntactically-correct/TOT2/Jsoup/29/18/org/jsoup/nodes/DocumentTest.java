import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("#document", document.nodeName());
        Assert.assertNull(document.title());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        Element head = html.getFirstChild();
        Element body = head.nextSibling();
        Assert.assertEquals("html", html.nodeName());
        Assert.assertEquals("head", head.nodeName());
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testSetAndGetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        Document document = new Document("http://example.com");
        document.title(null);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element newElement = document.createElement("a");
        Assert.assertEquals("a", newElement.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = new Document("http://example.com");
        document.head().appendElement("title").text("Test Title");
        document.body().appendElement("p").text("Test Paragraph");
        Element title = document.head().getFirstChild();
        Element body = document.body();
        document.normalise();
        Assert.assertEquals("html", document.head().parent().nodeName());
        Assert.assertEquals("head", title.nodeName());
        Assert.assertEquals("body", body.nodeName());
        Assert.assertEquals("Test Title", document.title());
        Assert.assertEquals("Test Paragraph", body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html").appendElement("head").appendElement("body");
        Element head = document.findFirstElementByTagName("head", html);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testCloneDocument() {
        Document originalDocument = new Document("http://example.com");
        Document clonedDocument = originalDocument.clone();
        Assert.assertNotSame(originalDocument, clonedDocument);
        Assert.assertEquals("#document", clonedDocument.nodeName());
        Assert.assertEquals(originalDocument.outputSettings(), clonedDocument.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testSetOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        document.outputSettings(outputSettings);
        Assert.assertEquals(outputSettings, document.outputSettings());
        Assert.assertEquals(outputSettings.charset(), document.outputSettings().charset());
        Assert.assertEquals(outputSettings.escapeMode(), document.outputSettings().escapeMode());
        Assert.assertEquals(outputSettings.prettyPrint(), document.outputSettings().prettyPrint());
        Assert.assertEquals(outputSettings.indentAmount(), document.outputSettings().indentAmount());
    }
}