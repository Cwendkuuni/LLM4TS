import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = Document.createShell("http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateEmptyDocument() {
 Document doc = new Document("http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("#root", doc.tagName());
 }

 @Test
 public void testGetHeadElement() {
 Document doc = Document.createShell("http://example.com");
 Element head = doc.head();
 Assert.assertNotNull(head);
 Assert.assertEquals("head", head.tagName());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = Document.createShell("http://example.com");
 Element body = doc.body();
 Assert.assertNotNull(body);
 Assert.assertEquals("body", body.tagName());
 }

 @Test
 public void testGetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("");
 doc.title("Test Title");
 String title = doc.title();
 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element p = doc.createElement("p");
 Assert.assertNotNull(p);
 Assert.assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("Hello").appendElement("World");
 doc.head().appendElement("testing");
 doc.title("Test Title");
 doc.normalise();
 Element body = doc.body();
 List<TextNode> textNodes = body.textNodes();
 Assert.assertEquals(3, textNodes.size());
 Assert.assertEquals("HelloWorld", textNodes.get(0).text());
 Assert.assertEquals("Test Title", doc.title());
 Assert.assertEquals("testing", doc.select("head").first().text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("Hello").appendElement("World");
 doc.head().appendElement("testing");
 doc.title("Test Title");
 doc.normalise();
 String html = doc.outerHtml();
 Assert.assertEquals("<html><head><testing></testing></head><body>HelloWorld</body></html>", html);
 }

 @Test
 public void testText() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("Hello").appendElement("World");
 doc.text("Test");
 Element body = doc.body();
 List<Node> nodes = body.childNodes();
 List<TextNode> textNodes = new ArrayList<>();
 for (Node node : nodes) {
 if (node instanceof TextNode) {
 textNodes.add((TextNode) node);
 }
 }
 String text = doc.text().toString();
 Assert.assertEquals("Test", text);
 Assert.assertEquals(2, nodes.size());
 Assert.assertEquals("HelloWorld", textNodes.get(0).text());
 Assert.assertEquals("", textNodes.get(1).text());
 }
}