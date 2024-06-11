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
 public void testCreateEmptyDocument() {
 Document emptyDoc = new Document("http://example.com");
 assertEquals("#document", emptyDoc.nodeName());
 assertNull(emptyDoc.title());
 assertNull(emptyDoc.head());
 assertNull(emptyDoc.body());
 }

 @Test
 public void testCreateDocumentWithTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 Element titleEl = doc.getElementsByTag("title").first();
 assertEquals("Test Title", titleEl != null ? titleEl.text().trim() : "");
 }

 @Test
 public void testCreateDocumentWithHeadAndBody() {
 doc.head().appendElement("title").text("Test Title");
 doc.body().appendElement("p").text("Test Content");
 assertEquals("Test Title", doc.title());
 assertEquals("Test Content", doc.body().text().trim());
 }

 @Test
 public void testCreateElement() {
 Element newElement = doc.createElement("a");
 assertEquals("a", newElement.tagName());
 assertEquals("", newElement.attr("href"));
 }

 @Test
 public void testNormalise() {
 doc.head().appendElement("p").text("Test");
 doc.appendElement("p").text("Test");
 doc.normalise();
 List<Element> bodyElements = doc.body().getElementsByTag("p");
 assertEquals(2, bodyElements.size());
 assertEquals("Test", bodyElements.get(0).text().trim());
 assertEquals("Test", bodyElements.get(1).text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBaseUri() {
 try {
 new Document("");
 fail("Should not allow empty base URI");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 try {
 doc.createElement(null);
 fail("Should not allow null tag name");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}