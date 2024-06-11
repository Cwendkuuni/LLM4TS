import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        assertEquals("#document", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Element html = document.createShell("http://example.com").head();
        assertEquals("#html", html.nodeName());
        assertNotNull(html.getElementsByTag("head").first());
        assertNotNull(html.getElementsByTag("body").first());
    }

    @Test
    public void testGetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        assertEquals("", document.title()); // check default value
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element title = doc.createElement("title").text("Test Title");
        doc.head().appendChild(title);
        doc.appendElement("p").text("Test text");

        doc.normalise();

        Element head = doc.head();
        Element body = doc.body();

        assertEquals(1, head.childNodes().size());
        assertEquals("Test Title", head.childNodes().get(0).text());
        assertEquals(1, body.childNodes().size());
        assertEquals("Test text", body.childNodes().get(0).text());
    }
}