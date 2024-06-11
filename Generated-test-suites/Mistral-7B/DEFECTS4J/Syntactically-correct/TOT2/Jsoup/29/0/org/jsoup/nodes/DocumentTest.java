import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
 assertEquals("#document", doc.nodeName());
 Element html = doc.getElementsByTag("html").first();
 assertNotNull(html);
 Element head = html.getElementsByTag("head").first();
 assertNotNull(head);
 Element body = html.getElementsByTag("body").first();
 assertNotNull(body);
 }

 @Test
 public void testTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("a");
 assertNotNull(element);
 assertEquals("a", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.appendText("Test Text");
 Element p = doc.createElement("p");
 p.text("Test Paragraph");
 doc.getElementsByTag("head").first().appendChild(p);
 doc.normalise();
 // Test for text nodes being moved to body
 assertEquals(2, doc.body().childNodes().size());
 Element titleEl = doc.getElementsByTag("title").first();
 assertEquals("Test Title", titleEl.text().trim());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.appendElement("html").appendElement("body").appendElement("div").appendText("Test Text");
 Element div = doc.findFirstElementByTagName("div", doc);
 assertEquals("div", div.nodeName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertEquals("UTF-8", settings.charset().name());
 }

 @Test
 public void testPrettyPrint() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = new OutputSettings();
 settings.prettyPrint(false);
 doc.outputSettings(settings);
 String html = doc.outerHtml();
 assertTrue(html.contains("<html>"));
 assertTrue(html.contains("<head>"));
 assertTrue(html.contains("<body>"));
 }

}