import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Assert.assertEquals(Tag.valueOf("#root"), document.tagName());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertEquals(Tag.valueOf("#root"), document.tagName());
        Assert.assertEquals(baseUri, document.baseUri());
        Element html = document.head();
        Assert.assertEquals(Tag.valueOf("html"), html.tagName());
        Element head = html.firstChild();
        Assert.assertEquals(Tag.valueOf("head"), head.tagName());
        Element body = head.nextSibling();
        Assert.assertEquals(Tag.valueOf("body"), body.tagName());
    }

    @Test
    public void testHeadAccess() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element head = document.head();
        Assert.assertEquals(Tag.valueOf("head"), head.tagName());
        Assert.assertNotNull(head.parent());
        Assert.assertEquals(document, head.parent());
    }

    @Test
    public void testBodyAccess() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        Assert.assertEquals(Tag.valueOf("body"), body.tagName());
        Assert.assertNotNull(body.parent());
        Assert.assertEquals(document, body.parent());
    }

    @Test
    public void testTitleAccess() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
        Element titleElement = document.getElementsByTag("title").first();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testTitleSetNull() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title(null);
        String title = document.title();
        Assert.assertEquals("", title);
        Element titleElement = document.getElementsByTag("title").first();
        Assert.assertNull(titleElement);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element element = document.createElement("div");
        Assert.assertEquals(Tag.valueOf("div"), element.tagName());
        Assert.assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.body().text("Hello <world>");
        document.head().appendElement("title").text("Test Title");
        document.appendElement("html").appendElement("head").appendElement("body").text(" Goodbye <world>");
        document.normalise();
        Element body = document.body();
        Assert.assertEquals("Hello <world>\nGoodbye <world>", body.text());
        Element title = document.head().getElementsByTag("title").first();
        Assert.assertEquals("Test Title", title.text());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNull() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        findFirstElementByTagName(null, document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        findFirstElementByTagName("invalid", document);
    }

    private Element findFirstElementByTagName(String tagName, Node node) {
        if (node == null) {
            throw new NullPointerException();
        }
        if (node.nodeName().equals(tagName)) {
            return (Element) node;
        }
        for (Node child : node.childNodes) {
            Element found = findFirstElementByTagName(tagName, child);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertFalse(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Assert.assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset("UTF-16");
        Assert.assertEquals("UTF-16", outputSettings.charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        outputSettings.indentAmount(4);
        Assert.assertEquals(4, outputSettings.indentAmount());
    }
}