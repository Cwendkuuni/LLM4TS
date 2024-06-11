import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 assertEquals("html", html.tagName());
 Element head = html.head();
 assertEquals("head", head.tagName());
 Element body = html.body();
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetHeadElement() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertEquals("head", head.tagName());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("baseUri");
 doc.title("");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElementWithValidTagName() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("p");
 assertEquals("p", element.tagName());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testCreateElementWithInvalidTagName() {
 Document doc = new Document("baseUri");
 doc.createElement("InvalidTagName");
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("baseUri");
 doc.appendText("Test text before normalise");
 doc.appendElement("p").text("Test text inside p");
 doc.appendElement("p").text("Test text inside second p");
 doc.normalise();
 Element body = doc.body();
 TextNode textNode1 = (TextNode) body.childNodes().get(0);
 TextNode textNode2 = (TextNode) body.childNodes().get(1);
 TextNode textNode3 = (TextNode) body.childNodes().get(2);
 assertEquals("Test text before normalise", textNode1.text());
 assertEquals("Test text inside p", textNode2.text());
 assertEquals("Test text inside second p", textNode3.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 Element title = doc.createElement("title").text("Test Title");
 html.appendChild(head);
 head.appendChild(title);
 html.appendChild(body);
 Element found = doc.findFirstElementByTagName("html", doc);
 assertEquals(html, found);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element title = doc.createElement("title").text("Test Title");
 Element body = doc.createElement("body").appendText("Test text");
 html.appendChild(head);
 html.appendChild(body);
 assertEquals("<html><head></head><body>Test text</body></html>", doc.outerHtml());
 }

 @Test
 public void testTextMethod() {
 Document doc = new Document("baseUri");
 doc.body().text("Test text");
 assertEquals("Test text", doc.text());
 }
}