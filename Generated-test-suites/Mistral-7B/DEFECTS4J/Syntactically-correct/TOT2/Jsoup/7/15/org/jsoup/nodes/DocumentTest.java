import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = head.body();
 assertNotNull(body);
 }

 @Test
 public void testGetHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());

 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("<html><head></head><body><p>Hello</p></body></html>");
 assertEquals("Hello", doc.body().text());
 doc.normalise();
 assertEquals("", doc.head().text());
 assertEquals("Hello", doc.body().text());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameWithNullNode() {
 Document doc = new Document("http://example.com");
 try {
 doc.findFirstElementByTagName("html", null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><head><title>Test Title</title></head></html>");
 Element title = doc.findFirstElementByTagName("title", doc);
 assertNotNull(title);
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Hello</p></body></html>");
 assertEquals("<html><head><title>Test Title</title></head><body><p>Hello</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("<html><head></head><body><p>Hello</p></body></html>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test
 public void testCreateDocumentWithNullBaseUri() {
 try {
 new Document(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCreateShellWithNullBaseUri() {
 try {
 Document.createShell(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNormaliseWithNullElement() {
 Document doc = new Document("<html><head></head></html>");
 try {
 doc.normalise(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }
}