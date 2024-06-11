import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

public class DocumentTest {

 // Test for constructor with baseUri
 @Test
 public void testDocumentConstructorWithBaseUri() {
 Document doc = new Document("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertTrue(html != null);
 Element head = html.head();
 assertTrue(head != null);
 Element body = doc.body();
 assertTrue(body != null);
 }

 // Test for head method
 @Test
 public void testHeadMethod() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertTrue(head != null);
 }

 // Test for body method
 @Test
 public void testBodyMethod() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertTrue(body != null);
 }

 // Test for title method
 @Test
 public void testTitleMethod() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 // Test for title method with no title
 @Test
 public void testTitleMethodNoTitle() {
 Document doc = new Document("http://example.com");
 String title = doc.title();
 assertEquals("", title);
 }

 // Test for createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("p");
 assertEquals("p", element.tagName());
 }

 // Test for normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = new Document("http://example.com");
 doc.text("Test Text");
 doc.normalise();
 Element body = doc.body();
 TextNode textNode = (TextNode) body.child(0);
 assertEquals("Test Text", textNode.text());
 }

 // Test for findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagNameMethod() {
 Document doc = new Document("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertTrue(html != null);
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtmlMethod() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.text("Test Text");
 String html = doc.outerHtml();
 assertTrue(html.contains("Test Title"));
 assertTrue(html.contains("Test Text"));
 }

 // Test for text method
 @Test
 public void testTextMethod() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.body().text("Test Text");
 String text = doc.text();
 assertEquals("Test Text", text);
 }

 // Test for nodeName method
 @Test
 public void testNodeNameMethod() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 // Test for clone method
 @Test
 public void testCloneMethod() {
 Document doc1 = new Document("http://example.com");
 Document doc2 = doc1.clone();
 assertNotSame(doc1, doc2);
 }

 // Test for OutputSettings class
 @Test
 public void testOutputSettingsClass() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertEquals(Document.Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 }

 // Test for OutputSettings charset method
 @Test
 public void testOutputSettingsCharsetMethod() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.charset("ISO-8859-1");
 assertEquals("ISO-8859-1", outputSettings.charset().name());
 }
}