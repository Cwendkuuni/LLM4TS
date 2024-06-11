import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class DocumentTest {

 // Test constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 }

 // Test createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 Element body = html.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 // Test head and body accessors
 @Test
 public void testHeadAndBodyAccessors() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 Element body = doc.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 // Test title method
 @Test
 public void testTitleMethod() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 // Test createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("div");
 assertNotNull(element);
 }

 // Test normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = new Document("baseUri");
 doc.appendText("Text before");
 Element div = doc.createElement("div");
 doc.appendChild(div);
 div.appendText("Text inside div");
 doc.normalise();
 assertEquals("Text beforeText inside div", doc.body().text());
 }

 // Test findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagNameMethod() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 Element title = doc.appendElement("title");
 doc.title("Test Title");
 assertEquals(title, doc.findFirstElementByTagName("title", doc));
 }

 // Test outerHtml method
 @Test
 public void testOuterHtmlMethod() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 doc.title("Test Title");
 assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
 }

 // Test text method
 @Test
 public void testTextMethod() {
 Document doc = new Document("baseUri");
 doc.body().text("Text before");
 Element div = doc.createElement("div");
 doc.body().appendChild(div);
 div.text("Text inside div");
 doc.normalise();
 doc.body().text("Text beforeText inside div");
 assertEquals("Text beforeText inside div", doc.body().text());
 }

 // Test nodeName method
 @Test
 public void testNodeNameMethod() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 }

 // Test clone method
 @Test
 public void testCloneMethod() {
 Document doc = new Document("baseUri");
 Document clone = doc.clone();
 assertNotSame(doc, clone);
 }

 // Test OutputSettings getters
 @Test
 public void testOutputSettingsGetters() {
 Document doc = new Document("baseUri");
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertEquals("UTF-8", settings.charset().name());
 }

 // Test OutputSettings setters
 @Test
 public void testOutputSettingsSetters() {
 Document doc = new Document("baseUri");
 OutputSettings settings = doc.outputSettings();
 settings.escapeMode(Entities.EscapeMode.extended);
 settings.charset(Charset.forName("ISO-8859-1"));
 doc.outputSettings(settings);
 assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
 assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
 }

 // Test QuirksMode getters
 @Test
 public void testQuirksModeGetters() {
 Document doc = new Document("baseUri");
 assertEquals(QuirksMode.noQuirks, doc.quirksMode());
 }

 // Test QuirksMode setters
 @Test
 public void testQuirksModeSetters() {
 Document doc = new Document("baseUri");
 doc.quirksMode(QuirksMode.quirks);
 assertEquals(QuirksMode.quirks, doc.quirksMode());
 }

 // Edge cases and exception handling
 @Test(expected = NullPointerException.class)
 public void testNullBaseUriConstructor() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndentAmount() {
 Document doc = new Document("baseUri");
 OutputSettings settings = doc.outputSettings();
 document.outputSettings(settings.indentAmount(-1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownCharset() {
 Document doc = new Document("baseUri");
 OutputSettings settings = doc.outputSettings();
 document.outputSettings(settings.charset("XYZ"));
 }
}