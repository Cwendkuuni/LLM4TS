import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("head", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testSetTitleTwice() {
 document.title("Test Title 1");
 document.title("Test Title 2");
 assertEquals("Test Title 2", document.title());
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.nodeName());
 }

 @Test
 public void testNormalise() {
 document.body().text("Hello World\nTest\n");
 Document normalisedDocument = document.normalise();
 assertNotNull(normalisedDocument);
 assertEquals("body", normalisedDocument.body().nodeName());
 assertEquals("Hello World Test", normalisedDocument.body().text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testNullParameter() {
 document.title(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTitle() {
 document.title("");
 }

 @Test
 public void testOutputSettings() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 }
}