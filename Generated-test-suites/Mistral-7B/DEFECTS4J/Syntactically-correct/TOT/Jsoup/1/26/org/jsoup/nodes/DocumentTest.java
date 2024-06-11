import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document newDoc = new Document("http://example.com");
 assertEquals("#root", newDoc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.elementSibling();
 Element body = head.nextElementSibling();
 assertEquals("html", html.tagName());
 assertEquals("head", head.tagName());
 assertEquals("body", body.tagName());
 }

 @Test
 public void testHeadAccessors() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 Element head = doc.head();
 assertEquals("Test Title", head.getElementsByTag("title").first().text());
 }

 @Test
 public void testBodyAccessors() {
 doc.title("Test Title");
 doc.body().appendElement("p").text("Test Body");
 assertEquals("Test Title", doc.title());
 assertEquals("Test Body", doc.body().text());
 }

 @Test
 public void testTitleSetters() {
 doc.title("Test Title");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTitle() {
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 doc.title("Test Title");
 Element link = doc.createElement("a");
 assertEquals("a", link.tagName());
 }

 @Test
 public void testNormalise() {
 doc.title("Test Title");
 doc.body().appendElement("p").text("Test Body");
 doc.appendElement("div").text("Test Div");
 doc.normalise();
 assertEquals("html<head><title>Test Title</title></head><body>Test Body</body>", doc.outerHtml());
 }

 @Test
 public void testTextSetter() {
 doc.title("Test Title");
 doc.text("Test Text");
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test
 public void testTextEmpty() {
 doc.title("Test Title");
 doc.text("");
 assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
 }

 @Test
 public void testNodeName() {
 doc.title("Test Title");
 doc.text("Test Text");
 assertEquals("#document", doc.nodeName());
 }
}