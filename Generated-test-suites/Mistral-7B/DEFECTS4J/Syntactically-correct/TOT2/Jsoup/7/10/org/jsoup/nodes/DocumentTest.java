import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://localhost:8080");
        Assert.assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://localhost:8080");
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testGetTitle_Empty() {
        Document document = new Document("http://localhost:8080");
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testGetTitle_SetTitle() {
        Document document = new Document("http://localhost:8080");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitle_NullTitle() {
        Document document = new Document("http://localhost:8080");
        document.title(null);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://localhost:8080");
        Element element = document.createElement("p");
        Assert.assertNotNull(element);
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://localhost:8080");
        Element html = document.appendElement("html");
        Element body = document.appendElement("body");
        body.appendText("Test");
        html.appendChild(document.createElement("head"));

        document.normalise();

        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }

        Assert.assertEquals(1, textNodes.size());
        Assert.assertEquals("Test", textNodes.get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagName_NullNode() {
        Document document = new Document("http://localhost:8080");
        document.findFirstElementByTagName(null, document);
    }

    @Test
    public void testFindFirstElementByTagName_Exists() {
        Document document = new Document("http://localhost:8080");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element title = document.appendElement("title", document);

        Element headElement = document.findFirstElementByTagName("head", document);
        Assert.assertNotNull(headElement);
        Assert.assertEquals(head, headElement);
    }

    @Test
    public void testFindFirstElementByTagName_NotExists() {
        Document document = new Document("http://localhost:8080");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        Element notExistsElement = document.findFirstElementByTagName("footer", document);
        Assert.assertNull(notExistsElement);
    }

    @Test
    public void testOutputSettings_Getters() {
        Document document = new Document("http://localhost:8080");
        OutputSettings outputSettings = document.outputSettings();

        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Assert.assertNotNull(outputSettings.encoder());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettings_Setters() {
        Document document = new Document("http://localhost:8080");
        OutputSettings outputSettings = document.outputSettings();

        outputSettings
                .escapeMode(Entities.EscapeMode.extended)
                .charset(Charset.forName("UTF-16"))
                .prettyPrint(false)
                .indentAmount(5);

        Assert.assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        Assert.assertEquals(Charset.forName("UTF-16"), outputSettings.charset());
        Assert.assertFalse(outputSettings.prettyPrint());
        Assert.assertEquals(5, outputSettings.indentAmount());
    }
}