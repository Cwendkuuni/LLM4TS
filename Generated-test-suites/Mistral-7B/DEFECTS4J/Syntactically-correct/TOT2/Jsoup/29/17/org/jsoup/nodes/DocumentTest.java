import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("head", html.tagName());
 Element head = doc.body();
 assertNotNull(head);
 assertEquals("body", head.tagName());
 }

 @Test
 public void testAccessHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testAccessBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 assertEquals("", doc.title());
 doc.title(null);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.tagName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 doc.appendElement("body").text("Test Body");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title 2");
 doc.appendElement("body").text("Test Body 2");
 doc.normalise();
 assertEquals(1, doc.getElementsByTag("html").size());
 assertEquals(1, doc.getElementsByTag("head").size());
 assertEquals(1, doc.getElementsByTag("title").size());
 assertEquals(1, doc.getElementsByTag("body").size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement("!");
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 doc.appendElement("body").text("Test Body");
 assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("http://example.com");
 doc.appendElement("html").appendElement("body").text("Test Body");
 assertEquals("Test Body", doc.text());
 assertEquals("", doc.title());
 }

 @Test
 public void testClone() {
 Document doc = new Document("http://example.com");
 doc.appendElement("html").appendElement("body").text("Test Body");
 Document clone = doc.clone();
 assertEquals("Test Body", clone.text());
 }

}