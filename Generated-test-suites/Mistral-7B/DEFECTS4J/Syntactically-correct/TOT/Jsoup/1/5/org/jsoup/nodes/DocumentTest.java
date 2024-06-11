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
 doc = Document.createShell("http://localhost");
 }

 @Test
 public void testCreateDocument() {
 Document expectedDocument = new Document("http://localhost");
 assertEquals(expectedDocument.nodeName(), doc.nodeName());
 assertEquals(Tag.valueOf("#root"), doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document expectedDocument = Document.createShell("http://localhost");
 Element html = expectedDocument.head();
 Element head = html.getElementsByTag("head").first();
 Element body = expectedDocument.body();

 assertEquals(3, expectedDocument.childNodes().size());
 assertEquals(html, doc.head());
 assertEquals(head, html.lastChild());
 assertEquals(body, doc.body());
 }

 @Test
 public void testTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitleWithNullParam() {
 assertThrows(NullPointerException.class, () -> doc.title(null));
 }

 @Test
 public void testTitleWithEmptyStringParam() {
 doc.title("");
 assertEquals("", doc.title());
 }

 @Test
 public void testCreateElement() {
 Element element = doc.createElement("div");
 assertEquals("div", element.tagName());
 assertEquals("#document", element.baseUri());
 }

 @Test
 public void testNormalise() {
 doc.title("Test Title");
 doc.text("Test Text");
 Element body = doc.body();
 TextNode textNode = (TextNode) body.childNodes().get(0);
 body.removeChild(textNode);

 doc.normalise();
 Element title = doc.head().getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 TextNode bodyText = (TextNode) doc.body().childNodes().get(0);
 assertEquals("Test Text", bodyText.text().trim());
 }

 @Test
 public void testNormaliseWithEmptyDocument() {
 doc.normalise();
 assertTrue(doc.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testNormaliseWithNullDocument() {
 Document nullDoc = null;
 nullDoc.normalise();
 }
}