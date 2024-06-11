import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.select("html").first();
 Element head = html.select("head").first();
 Element body = html.select("body").first();

 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testHeadElement() {
 Document doc = Jsoup.parse("<html><head><title>Test Document</title></head><body></body></html>", "http://example.com");
 Element head = doc.head();

 assertNotNull(head);
 assertEquals("Test Document", doc.title());
 }

 @Test
 public void testBodyElement() {
 Document doc = Jsoup.parse("<html><head><title>Test Document</title></head><body><p>Hello World</p></body></html>", "http://example.com");
 Element body = doc.body();

 assertNotNull(body);
 assertEquals(1, body.childrenSize());
 TextNode textNode = (TextNode) body.child(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testTitleElement() {
 Document doc = Jsoup.parse("<html><head><title>Test Document</title></head><body></body></html>", "http://example.com");

 assertEquals("Test Document", doc.title());
 }

 @Test
 public void testSetTitleElement() {
 Document doc = Jsoup.parse("<html><head><title></title></head><body></body></html>", "http://example.com");
 doc.title("New Title");

 assertEquals("New Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Jsoup.parse("<html><head><title>Test Document</title></head><body></body></html>", "http://example.com");
 Element p = doc.createElement("p");

 assertNotNull(p);
 assertEquals(Tag.P, p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = Jsoup.parse("<html><head><title>Test Document</title></head><body><p>Hello World</p><div>Text Outside Body</div></body></html>", "http://example.com");
 doc.normalise();

 List<Element> bodyElements = doc.body().children();
 assertEquals(1, bodyElements.size());
 TextNode textNode = (TextNode) bodyElements.get(0);
 assertEquals("Hello WorldText Outside Body", textNode.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }
}