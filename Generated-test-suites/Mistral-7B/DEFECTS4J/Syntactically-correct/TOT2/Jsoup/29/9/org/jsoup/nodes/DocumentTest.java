import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class DocumentTest {

    @Test
    public void testCreateNewDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Assert.assertEquals(Tag.valueOf("#root"), document.tagName());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNull(document.title());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertEquals(Tag.valueOf("#document"), document.tagName());
        Assert.assertEquals(Tag.valueOf("html"), document.head().tagName());
        Assert.assertEquals(Tag.valueOf("head"), document.head().child(0).tagName());
        Assert.assertEquals(Tag.valueOf("body"), document.child(0).tagName());
    }

    @Test
    public void testAccessHeadElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals(Tag.valueOf("head"), head.tagName());
    }

    @Test
    public void testAccessBodyElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals(Tag.valueOf("body"), body.tagName());
    }

    @Test
    public void testGetDocumentTitle() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
        Assert.assertEquals("", document.title()); // reset title
    }

    @Test
    public void testSetDocumentTitle() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.createElement("a");
        Assert.assertEquals(Tag.valueOf("a"), element.tagName());
        Assert.assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        document.appendText("Test Text ");
        document.body().appendElement("p").appendText("Test Paragraph");
        document.head().appendElement("title").text("Test Title");
        document.head().appendElement("title").text("Another Test Title");
        document.head().appendElement("title").text("Yet Another Test Title");
        document.normalise();
        Element titleElement = document.head().getElementsByTag("title").first();
        Assert.assertEquals("Test Title", titleElement.text());

        List<Node> textNodes = new ArrayList<>();
        for (Node node : document.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }
        Assert.assertEquals(3, textNodes.size());
        Assert.assertEquals(" Test Text ", document.body().text().trim());
        Assert.assertEquals("Test Paragraph", document.getElementsByTag("p").first().text().trim());
    }
}