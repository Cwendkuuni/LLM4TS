import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Assert.*;

public class DocumentTest {

 private Document doc;

 @Before
 public void setup() {
 doc = Document.createShell("https://example.com");
 }

 @Test
 public void testCreateDocumentEmpty() {
 Document emptyDoc = new Document("https://example.com");
 assertTrue(emptyDoc.head() == null);
 assertTrue(emptyDoc.body() == null);
 }

 @Test
 public void testCreateShellWithHeadAndBody() {
 Document shellDoc = Document.createShell("https://example.com");
 Element html = shellDoc.select("html").first();
 Element head = shellDoc.head();
 Element body = shellDoc.body();

 assertEquals(Tag.HTML, html.tagName());
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testGetHeadElement() {
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test(expected = NullPointerException.class)
 public void testGetHeadElementWhenNull() {
 doc.head(null);
 doc.head();
 }

 @Test
 public void testSetAndGetTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testSetTitleWithNull() {
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Element link = doc.createElement("a");
 assertEquals(Tag.A, link.tagName());
 }

 @Test
 public void testNormalise() {
 doc.appendText("Test text in head");
 Element head = doc.head();
 TextNode textNode = (TextNode) head.childNodes().get(0);
 doc.normalise();
 TextNode bodyTextNode = (TextNode) doc.body().childNodes().get(0);
 assertEquals(textNode.text(), bodyTextNode.text());
 }

 @Test
 public void testOuterHtml() {
 doc.body().text("Test text in body");
 doc.title("Test Title");
 doc.normalise();
 String html = doc.outerHtml();
 String expected = "<html><head><title>Test Title</title></head><body>Test text in body</body></html>";
 assertEquals(expected, html);
 }

 @Test
 public void testText() {
 doc.body().appendElement(new TextNode("Test text in body"));
 doc.text("Test text to replace");
 assertEquals("Test text to replace", doc.body().text());
 }
}