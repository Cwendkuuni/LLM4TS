import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Element html = document.head();
 assertNotNull(html);
 assertEquals("head", html.tagName());

 Element head = html.child(0);
 assertNotNull(head);
 assertEquals("html", head.parent().tagName());

 Element body = document.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testSetTitle() {
 document.title("");
 assertNull(document.title());

 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.tagName());
 }

 @Test
 public void testNormalise() {
 document.text("Test text before normalise");
 document.normalise();
 TextNode textNode = (TextNode) document.body().child(0).get(0);
 assertEquals("Test text before normalise", textNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUriInDocumentConstruction() {
 Document doc = new Document(null);
 // no assertions here, just check for exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagNameInCreateElementConstruction() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement(null);
 // no assertions here, just check for exception
 }
}