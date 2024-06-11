import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.child(0);
 assertNotNull(head);
 Element body = head.child(0);
 assertNotNull(body);
 }

 @Test
 public void testGetHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 assertNull(doc.title());
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element newElement = doc.createElement("a");
 assertNotNull(newElement);
 assertEquals("a", newElement.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.text("Test text in document.");
 Element p = doc.createElement("p");
 p.text("Test text in new paragraph.");
 doc.body().appendElement(p);
 doc.normalise();
 String documentText = doc.text();
 String paragraphText = p.text();
 assertEquals("Test text in document.\nTest text in new paragraph.", documentText);
 assertEquals("Test text in new paragraph.", paragraphText);
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertEquals("#document", html.tagName());
 Element head = doc.findFirstElementByTagName("head", doc);
 assertNull(head);
 Element body = doc.findFirstElementByTagName("body", doc);
 assertNull(body);
 Element body1 = doc.appendChild(doc.createElement("body"));
 head = doc.createElement("head");
 doc.insertBefore(head, doc.getFirstChild());
 Element html1 = doc.createElement("html");
 doc.appendChild(html1);
 doc.insertBefore(body1, html1.getFirstChild());
 head.appendChild(doc.createElement("title"));
 body1.appendChild(doc.createTextNode("Test text in body."));
 doc.normalise();
 head = doc.findFirstElementByTagName("head", doc);
 body = doc.findFirstElementByTagName("body", doc);
 title = head.querySelector("title");
 TextNode textNode = (TextNode) body.getElementsByTag("p").first().child(0);
 assertEquals("Test text in body.", textNode.text());
 assertEquals("Test text in head.", title.text());
 }

}