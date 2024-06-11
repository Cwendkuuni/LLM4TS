import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetHeadAndBodyElements() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body></body></html>");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("<html><body><p>Test</p><head><title>Title</title></head></body></html>");
 doc.normalise();
 Element p = doc.body().firstChild();
 assertEquals("Test", p.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 Element head = doc.findFirstElementByTagName("head", doc);
 assertNotNull(head);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("<html><body><p>Test</p></body></html>");
 assertEquals("<html><body><p>Test</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testTextMethod() {
 Document doc = new Document("<html><body></body></html>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.html());
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
 public void testInvalidTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement("invalidTag");
 }

 @Test
 public void testFindFirstElementByTagNameWithNullNode() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 Element head = doc.findFirstElementByTagName("head", null);
 assertNull(head);
 }

 @Test
 public void testNormaliseTextNodesWithEmptyDocument() {
 Document doc = new Document("");
 doc.normalise();
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testNormaliseTextNodesWithTextNodesOnly() {
 Document doc = new Document("<html></html>");
 doc.appendChild(new TextNode("Test"));
 doc.normalise();
 assertEquals(1, doc.childNodes().size());
 assertEquals("Test", doc.body().getText());
 }

 @Test
 public void testNormaliseTextNodesWithNonTextNodesOnly() { // tag name does not matter
 Document doc = new Document("<html></html>");
 doc.appendChild(new Element("p"));
 doc.normalise();
 assertEquals(1, doc.childNodes().size());
 assertEquals("", doc.body().getText());
 }

 @Test
 public void testNormaliseTextNodesWithMixedNodes() {
 Document doc = new Document("<html><p>Test</p><head></head></html>");
 doc.normalise();
 assertEquals(1, doc.childNodes().size());
 assertEquals("Test", doc.body().getText());
 }

 @Test
 public void testNormaliseTextNodesWithTagsContainingText() {
 Document doc = new Document("<html><p>Test <b>Inside</b></p></html>");
 doc.normalise();
 assertEquals(1, doc.childNodes().size());
 assertEquals("Test Inside", doc.body().getText());
 }

 @Test
 public void testNormaliseTextNodesWithTextAndElementChildren() {
 Document doc = new Document("<html><p>Test <a href=\"#\">Link</a></p></html>");
 doc.normalise();
 assertEquals(2, doc.childNodes().size());
 assertEquals("Test", doc.body().getText());
 Element link = doc.body().getElementsByTag("a").first();
 assertEquals("#", link.attr("href"));
 }

 @Test
 public void testNormaliseTextNodesWithEmptyTextNodes() {
 Document doc = new Document("<html><p></p></html>");
 doc.normalise();
 assertEquals(1, doc.childNodes().size());
 assertEquals("", doc.body().getText());
 }
}