import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        Assert.assertEquals("#root", doc.tagName());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        Assert.assertEquals("#html", html.tagName());
        Element head = html.getFirstElementChild();
        Assert.assertEquals("#head", head.tagName());
        Element body = head.getNextElementSibling();
        Assert.assertEquals("#body", body.tagName());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("baseUri");
        doc.appendElement("html").appendElement("head").appendElement("body");
        Element head = doc.head();
        Element body = doc.body();
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals(head, doc.head());
        Assert.assertEquals(body, doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.title("New Test Title");
        String title = doc.title();
        Assert.assertEquals("New Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element elem = doc.createElement("p");
        Assert.assertEquals("#p", elem.tagName());
        Assert.assertEquals(doc.baseUri(), elem.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.appendElement("html").appendElement("head").appendElement("body").appendText(" Test ");
        doc.appendText(" Normalise");
        doc.normalise();
        Element body = doc.body();
        TextNode titleTextNode = (TextNode) body.getFirstChild();
        Assert.assertEquals(" Test Normalise", titleTextNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleNull() {
        Document doc = new Document("baseUri");
        doc.title(null);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testCreateDocumentWithEmptyStringBaseUri() {
        Document doc = new Document("");
        Assert.assertEquals("#root", doc.tagName());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
    }

    @Test
    public void testCreateShellWithEmptyStringBaseUri() {
        Document doc = Document.createShell("");
        Element html = doc.head();
        Assert.assertEquals("#html", html.tagName());
        Element head = html.getFirstElementChild();
        Assert.assertEquals("#head", head.tagName());
        Element body = head.getNextElementSibling();
        Assert.assertEquals("#body", body.tagName());
    }

    @Test
    public void testGetHeadAndBodyWithEmptyDocument() {
        Document doc = new Document("baseUri");
        doc.appendElement("html").appendElement("head").appendElement("body");
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
    }

    @Test
    public void testGetTitleWithEmptyDocument() {
        Document doc = new Document("baseUri");
        String title = doc.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testSetTitleWithEmptyDocument() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElementWithEmptyStringTagName() {
        Document doc = new Document("baseUri");
        Element elem = doc.createElement("");
        Assert.assertNull(elem);
    }

    @Test
    public void testNormaliseTextNodesWithEmptyDocument() {
        Document doc = new Document("baseUri");
        doc.appendElement("html").appendElement("body");
        doc.normalise();
        List<Node> nodes = new ArrayList<>();
        for (Node node : doc.childNodes()) {
            nodes.add(node);
        }
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("#document", nodes.get(0).tagName());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");
        Element script = body.appendElement("script");
        Element script2 = body.appendElement("script");
        Element script3 = body.appendElement("script");
        Element script4 = body.appendElement("script");

        Element found = doc.findFirstElementByTagName("script", head);
        Assert.assertEquals(script, found);

        found = doc.findFirstElementByTagName("script", body);
        Assert.assertEquals(script, found);

        found = doc.findFirstElementByTagName("title", head);
        Assert.assertEquals(title, found);

        found = doc.findFirstElementByTagName("title", body);
        Assert.assertNull(found);

        found = doc.findFirstElementByTagName("title", null);
        Assert.assertNull(found);
    }
}