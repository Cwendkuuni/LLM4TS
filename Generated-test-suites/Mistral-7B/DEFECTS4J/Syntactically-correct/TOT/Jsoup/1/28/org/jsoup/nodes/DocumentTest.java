import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
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
 assertNotNull(newDoc);
 }

 @Test
 public void testCreateShell() {
 Document shellDoc = Document.createShell("http://example.com");
 Element html = shellDoc.head();
 Element head = html.getElementsByTag("head").first();
 Element body = shellDoc.body();

 assertEquals("#root", shellDoc.tagName());
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testAccessHeadElement() {
 doc.title("Test Title");
 Element head = doc.head();
 Element title = head.getElementsByTag("title").first();

 assertEquals("Test Title", title.text().trim());
 }

 @Test
 public void testAccessBodyElement() {
 doc.title("Test Title");
 Element body = doc.body();

 assertEquals("", body.text().trim());
 }

 @Test
 public void testGetTitle() {
 doc.title("Test Title");
 String title = doc.title();

 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 doc.title("Test Title");
 doc.title("New Title");
 String title = doc.title();

 assertEquals("New Title", title);
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
 doc.body().text("Test Text");
 Element p = doc.createElement("p");
 p.text("Test Paragraph");
 doc.body().appendChild(p);
 doc.normalise();

 assertEquals("", doc.head().text().trim());
 assertEquals("Test TextTest Paragraph", doc.body().text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBaseUri() {
 Document doc = new Document("");
 }
}