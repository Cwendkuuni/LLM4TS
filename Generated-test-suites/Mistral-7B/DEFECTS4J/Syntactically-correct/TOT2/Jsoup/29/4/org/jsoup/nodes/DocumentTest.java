import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document("http://example.com");
 }

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
 assertEquals("html", html.nodeName());
 Element head = html.appendElement("head");
 Element body = html.appendElement("body");
 assertTrue(head.parent().equals(html));
 assertTrue(body.parent().equals(html));
 }

 @Test
 public void testGetHeadElement() {
 Element head = document.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 }

 @Test
 public void testGetBodyElement() {
 Element body = document.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testGetTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testSetTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 document.title(null);
 assertEquals("", document.title());
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("div");
 assertEquals("div", element.nodeName());
 }

 @Test
 public void testNormalise() {
 document.title("Test Title 1");
 document.title("Test Title 2");
 document.body().text("Test Body Text");
 document.normalise();
 assertEquals("Test Title 1", document.title());
 assertEquals("Test Body Text", document.body().text());
 }

 @Test(expected = NullPointerException.class)
 public void testNormaliseNullElement() {
 Document doc = new Document("http://example.com");
 doc.normalise(null);
 }

 @Test
 public void testFindFirstElementByTagName() {
 Element element = document.findFirstElementByTagName("html");
 assertNotNull(element);
 assertEquals("html", element.nodeName());
 }

 @Test
 public void testFindFirstElementByTagNameNullNode() {
 Node node = null;
 Element element = document.findFirstElementByTagName("html", node);
 assertNull(element);
 }

 @Test
 public void testFindFirstElementByTagNameOtherTag() {
 Node node = document.createElement("div");
 Element element = document.findFirstElementByTagName("html", node);
 assertNull(element);
 }

 @Test
 public void testOuterHtml() {
 document.title("Test Title");
 document.body().text("Test Body Text");
 assertEquals("<html><head><title>Test Title</title></head><body>Test Body Text</body></html>", document.outerHtml());
 }

 @Test
 public void testText() {
 document.title("Test Title");
 document.body().text("Test Body Text");
 assertEquals("Test Body Text", document.text());
 }

 @Test
 public void testClone() {
 Document original = new Document("http://example.com");
 Document clone = original.clone();
 assertEquals(original.hashCode(), clone.hashCode());
 }
}