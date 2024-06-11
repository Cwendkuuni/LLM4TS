import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 Element body = html.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testAccessHeadAndBody() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNotNull(head);
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testAccessTitle() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("baseUri");
 doc.title(null);
 assertEquals("", doc.title());
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("p");
 assertNotNull(element);
 assertEquals("p", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.appendText("Test ");
 doc.appendElement("p").text("Normalise");
 doc.appendText(" test.");
 doc.normalise();
 assertEquals("Test Normalise test.", doc.body().text());
 }

 @Test
 public void testNormaliseTextNodes() {
 Document doc = new Document("baseUri");
 Element p = doc.createElement("p");
 TextNode textNode1 = doc.createTextNode("Test 1");
 TextNode textNode2 = doc.createTextNode(" Test 2");
 p.appendChild(textNode1);
 p.appendChild(textNode2);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test 1 Test 2", doc.body().text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.createElement("head");
 Element body = doc.createElement("body");
 html.appendChild(head);
 html.appendChild(body);
 doc.body().appendChild(doc.createElement("p"));
 Element found = doc.findFirstElementByTagName("body", doc);
 assertEquals("body", found.nodeName());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("baseUri");
 doc.body().text("Test");
 assertEquals("<body></body>", doc.outerHtml());
 doc.title("Test Title");
 assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("baseUri");
 doc.body().appendText("Test");
 assertEquals("Test", doc.text());
 }

 @Test
 public void testClone() {
 Document original = new Document("baseUri");
 Document cloned = original.clone();
 assertNotSame(original, cloned);
 }
}