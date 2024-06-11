import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNull(document.title());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("#html", html.nodeName());
        Assert.assertNotNull(html.head());
        Assert.assertNotNull(html.body());
    }

    @Test
    public void testHeadAccessors() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        Element head = document.head();

        String title = "Test Title";
        document.title(title);
        Assert.assertEquals(title, document.title());

        Element newHead = document.createElement("head");
        document.head(newHead);
        Assert.assertEquals(newHead, document.head());

        Assert.assertNull(document.title());
        Assert.assertEquals(newHead, document.head());
    }

    @Test
    public void testBodyAccessors() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();

        String text = "Test Body Text";
        document.text(text);
        Assert.assertEquals(text, document.body().text());

        Element newBody = document.createElement("body");
        document.body(newBody);
        Assert.assertEquals(newBody, document.body());

        Assert.assertNull(document.body().text());
        Assert.assertEquals(newBody, document.body());
    }

    @Test
    public void testTitleSettingNull() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        Assert.assertThrows(NullPointerException.class, () -> document.title(null));
    }

    @Test
    public void testTitleSettingEmptyString() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        document.title("");
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testNormalizeDocument() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        document.appendElement("html").appendElement("body").appendElement("title").text("Other Title");
        document.appendText("Test Text 1 ");
        document.appendElement("p").text("Test Text 2");

        document.normalise();

        Assert.assertEquals("Test Title", document.title());
        Element body = document.body();
        Assert.assertEquals("Test Text 1 Test Text 2", body.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeNullElement() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        document.normaliseTextNodes(null);
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("");
        OutputSettings outputSettings = document.outputSettings();

        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Assert.assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());

        outputSettings.charset("ISO-8859-1");
        Assert.assertEquals("ISO-8859-1", outputSettings.charset().name());

        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(5);
        Assert.assertEquals(5, outputSettings.indentAmount());
    }
}