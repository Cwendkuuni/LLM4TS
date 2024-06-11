import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testDocumentConstruction() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Assert.assertEquals("#root", doc.tagName());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        Assert.assertEquals("#html", html.tagName());
        Assert.assertEquals("#head", head.tagName());
        Assert.assertEquals("#body", body.tagName());
    }

    // Test edge cases and exception handling scenarios here...

    @Test
    public void testTitleAccessors() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("", doc.title());

        Element title = doc.createElement("title");
        title.text("Test Title");
        doc.head().appendChild(title);

        Assert.assertEquals("Test Title", doc.title());
        Assert.assertEquals("Test Title", doc.head().getElementsByTag("title").first().text());

        doc.title("New Title");
        Assert.assertEquals("New Title", doc.title());
        Assert.assertEquals("New Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        Assert.assertEquals("#div", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        TextNode textNode = doc.createTextNode("Test Text");

        p.appendChild(textNode);
        doc.body().appendChild(p);

        doc.normalise();

        List<Node> bodyChildren = doc.body().childNodes();
        Assert.assertEquals(2, bodyChildren.size());
        Assert.assertEquals(textNode.text(), bodyChildren.get(0).text());
        Assert.assertEquals("#text", bodyChildren.get(0).tagName());
        Assert.assertEquals("#p", bodyChildren.get(1).tagName());
    }

    // Add more test cases for remaining methods here...
}