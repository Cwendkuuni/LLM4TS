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
 assertEquals("#document", newDoc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document shellDoc = Document.createShell("http://example.com");
 Element html = shellDoc.select("html").first();
 Element head = html.getElementsByTag("head").first();
 Element body = html.getElementsByTag("body").first();
 assertEquals(3, shellDoc.childNodeSize());
 assertEquals("#root", html.nodeName());
 assertEquals("http://example.com", shellDoc.baseUri());
 }

 @Test
 public void testGetHeadElement() {
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test
 public void testGetBodyElement() {
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitleElementAndTitle() {
 doc.title("Test Title");
 Element titleEl = doc.getElementsByTag("title").first();
 assertEquals("Test Title", titleEl.text().trim());
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitleElementAndTitle() {
 doc.title("Test Title");
 doc.title("New Test Title");
 Element titleEl = doc.getElementsByTag("title").first();
 assertEquals("New Test Title", titleEl.text().trim());
 assertEquals("New Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Element newElement = doc.createElement("a");
 assertEquals("a", newElement.nodeName());
 assertEquals("http://example.com", newElement.baseUri());
 }

 @Test
 public void testNormaliseDocument() {
 doc.appendElement("html").appendElement("body").text("Test");
 doc.normalise();
 Element body = doc.getElementsByTag("body").first();
 assertEquals("Test", body.text().trim());
 }

 @Test
 public void testNormaliseDocumentWithPreExistingTextAndElements() {
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 doc.appendElement("html").appendElement("body").appendElement("p").text("Test Paragraph");
 doc.normalise();
 Element body = doc.getElementsByTag("body").first();
 assertEquals("Test Paragraph", body.text().trim());
 }

 @Test
 public void testNormaliseDocumentWithEmptyDocument() {
 doc.normalise();
 assertEquals(3, doc.childNodeSize());
 }

 @Test
 public void testNormaliseDocumentWithEmptyDocumentAndNewElement() {
 doc.normalise();
 doc.createElement("div").text("New Element");
 assertEquals(4, doc.childNodeSize());
 }
}