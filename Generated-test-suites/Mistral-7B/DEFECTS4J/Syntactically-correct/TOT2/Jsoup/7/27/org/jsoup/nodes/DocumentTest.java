import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 // Test for Document constructor
 @Test
 public void testDocumentConstruction() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 String baseUri = "http://example.com";
 Document doc = Document.createShell(baseUri);
 assertNotNull(doc);
assertTrue(doc.head() != null);
 assertTrue(doc.body() != null);
 assertEquals(baseUri, doc.baseUri());
 }

 // Test for head and body accessor methods
 @Test
 public void testHeadAndBodyAccessors() {
 Document doc = createShell("http://example.com");
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertEquals(doc.head().nodeName(), "head");
 assertEquals(doc.body().nodeName(), "body");
 }

 // Test for title method
 @Test
 public void testTitle() {
 Document doc = createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 // Test for title method with empty title
 @Test
 public void testTitleWithEmptyTitle() {
 Document doc = createShell("http://example.com");
 assertEquals("", doc.title());
 }

 // Test for createElement method
 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertEquals("p", p.nodeName());
 }

 // Test for normalise method
 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.text("Hello World");
 doc.appendChild(doc.createElement("p").text("Test"));
 doc.normalise();
 assertEquals("Hello WorldTest", doc.text().trim());
 }

 // Test for findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.appendElement("html");
 Element head = doc.createElement("head");
 Element body = doc.createElement("body");
 html.appendChild(head);
 html.appendChild(body);
 Element title = doc.createElement("title");
 title.setText("Test Title");
 head.appendChild(title);
 Element h1 = doc.createElement("h1");
 h1.setText("Test Heading");
 body.appendChild(h1);

 Element foundTitle = doc.findFirstElementByTagName("title", doc);
 Element foundH1 = doc.findFirstElementByTagName("h1", doc);

 assertEquals(title, foundTitle);
 assertEquals(h1, foundH1);
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 Element html = doc.appendElement("html");
 Element head = doc.createElement("head");
 Element body = doc.createElement("body");
 html.appendChild(head);
 html.appendChild(body);
 Element title = doc.createElement("title");
 title.setText("Test Title");
 head.appendChild(title);
 Element h1 = doc.createElement("h1");
 h1.setText("Test Heading");
 body.appendChild(h1);

 doc.title("Test Title 2");
 doc.text("Test Text");

 String expectedHtml = "<html><head><title>Test Title 2</title></head><body>" +
         "<h1>Test Heading</h1>" +
         "<p>Test Text</p></body></html>";

 assertEquals(expectedHtml, doc.outerHtml());
 }

 // Test for text method
 @Test
 public void testTextMethod() {
 Document doc = new Document("http://example.com");
 doc.text("Test Text");
 assertEquals("Test Text", doc.text().trim());
 }

 // Test for null baseUri in Document constructor
 @Test(expected = NullPointerException.class)
 public void testDocumentConstructionWithNullBaseUri() {
 new Document(null);
 }

 // Test for null tagName in createElement method
 @Test(expected = NullPointerException.class)
 public void testCreateElementWithNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }

 // Test for null tagName in findFirstElementByTagName method
 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameWithNullTagName() {
 Document doc = new Document("http://example.com");
 doc.findFirstElementByTagName(null, doc);
 }
}