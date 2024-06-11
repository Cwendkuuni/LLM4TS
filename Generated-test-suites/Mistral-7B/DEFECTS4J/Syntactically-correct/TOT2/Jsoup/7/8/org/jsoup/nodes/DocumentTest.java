import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseUri");
 assertTrue(doc instanceof Document);
 assertEquals("#root", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertTrue(doc instanceof Document);
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("#html", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("#head", head.nodeName());
 Element body = html.body();
 assertNotNull(body);
 assertEquals("#body", body.nodeName());
 }

 @Test
 public void testHead() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("#head", head.nodeName());
 }

 @Test
 public void testBody() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("#body", body.nodeName());
 }

 @Test
 public void testTitle() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("div");
 assertTrue(element instanceof Element);
 assertEquals("div", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.text("Test");
 Element p = doc.createElement("p");
 p.text("Normalise Test");
 doc.body().appendChild(p);
 doc.normalise();
 TextNode textNode = (TextNode) doc.body().childNodes.get(0);
 assertEquals("Test", textNode.text());
 Element pElement = (Element) doc.body().childNodes.get(1);
 assertEquals("Normalise Test", pElement.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyStringBaseUri() {
 Document doc = new Document("");
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 Document doc = new Document("baseUri");
 doc.title(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyStringTitle() {
 Document doc = new Document("baseUri");
 doc.title("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedTagName() {
 Document doc = new Document("baseUri");
 doc.createElement("&tagName");
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 Element title = doc.createElement("title").text("Test Title");
 html.appendChild(head);
 html.appendChild(title);
 html.appendChild(body);

 Element headElement = doc.findFirstElementByTagName("head", doc);
 assertEquals(head, headElement);
 Element bodyElement = doc.findFirstElementByTagName("body", doc);
 assertEquals(body, bodyElement);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNode() {
 Document doc = new Document("baseUri");
 doc.findFirstElementByTagName("head", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("baseUri");
 doc.findFirstElementByTagName("invalidTag", this);
 }
}