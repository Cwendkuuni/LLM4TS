import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertEquals("html", html.nodeName());
 assertEquals("head", html.child(0).nodeName());
 assertEquals("body", html.child(1).nodeName());
 }

 @Test
 public void testGetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title(null);
 assertNull(doc.title());
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element element = doc.createElement("a");
 assertEquals("a", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 doc.body().text("Test test");
 assertEquals("Test Title\nTest test", doc.html());
 doc.normalise();
 assertEquals("Test Title\n<body>Test test</body>", doc.outerHtml());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 Element title = doc.createElement("title");
 title.text("Test Title");
 head.appendChild(title);

 assertEquals("title", doc.findFirstElementByTagName("title", doc).nodeName());
 assertEquals("html", doc.findFirstElementByTagName("html", doc).nodeName());
 assertEquals(null, doc.findFirstElementByTagName("footer", doc));
 }

 @Test
 public void testText() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 doc.body().text("Test test");
 assertEquals("Test Title\nTest test", doc.text());
 assertEquals("Test Title\nTest test", doc.outerHtml());
 }

 @Test
 public void testNormaliseTextNodes() {
 Document doc = Document.createShell("http://example.com");
 doc.body().text("Test test\nTest 2");
 doc.normalise();
 Element body = doc.body();
 List<Node> children = body.childNodes();
 assertEquals(3, children.size());
 assertEquals("Test test", children.get(0).toString().trim());
 assertEquals("Test 2", children.get(2).toString().trim());
 }
}