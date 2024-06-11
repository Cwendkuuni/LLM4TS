import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document document = new Document("baseUri");
 assertNotNull(document);
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("baseUri");
 Element html = document.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testGetHeadElement() {
 Document document = new Document("baseUri");
 Element head = document.head();
 assertNotNull(head);
 }

 @Test
 public void testGetBodyElement() {
 Document document = new Document("baseUri");
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.title("New Title");
 String title = document.title();
 assertEquals("New Title", title);
 }

 @Test
 public void testCreateElement() {
 Document document = new Document("baseUri");
 Element element = document.createElement("a");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().text("Test Body");
 document.appendElement("div").text("Test DIV");
 document.normalise();
 Element head = document.head();
 Element title = head.getElementsByTag("title").first();
 assertEquals("Test Title", title.text());
 Element body = document.body();
 assertEquals("Test Body", body.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document document = new Document("baseUri");
 Element html = document.findFirstElementByTagName("html", document);
 Element head = html.findFirstElementByTagName("head", html);
 Element body = html.findFirstElementByTagName("body", html);
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testOuterHtml() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().text("Test Body");
 String html = document.outerHtml();
 assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", html);
 }

 @Test
 public void testText() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().text("Test Body");
 document.text("Test Document");
 String text = document.text();
 assertEquals("Test DocumentTest Body", text);
 }

 @Test
 public void testNodeName() {
 Document document = new Document("baseUri");
 assertEquals("#document", document.nodeName());
 }
}