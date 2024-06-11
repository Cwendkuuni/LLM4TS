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
 doc = new Document("http://example.com");
 }

 @Test
 public void testDocumentCreation() {
 Document newDoc = Document.createShell("http://example.com");
 assertNotNull(newDoc);
 assertNotNull(newDoc.head());
 assertNotNull(newDoc.body());
 }

 @Test
 public void testElementAccessors() {
 Element head = doc.head();
 Element body = doc.body();

 assertNotNull(head);
 assertNotNull(body);

 assertEquals("#head", head.nodeName());
 assertEquals("#body", body.nodeName());
 }

 @Test
 public void testTitleAccessors() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Element newElement = doc.createElement("p");
 assertNotNull(newElement);
 }

 @Test
 public void testNormalise() {
 doc.appendText("Test text before normalise");
 doc.body().appendText("Test text after normalise");
 doc.normalise();

 assertEquals("Test text after normaliseTest text before normalise\n", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 doc.createElement(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullText() {
 doc.title(null);
 }
}