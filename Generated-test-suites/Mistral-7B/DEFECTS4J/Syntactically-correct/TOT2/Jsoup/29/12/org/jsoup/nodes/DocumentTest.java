import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testEmptyTitle() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("p");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendText("Test text");
 doc.normalise();
 assertEquals("Test text", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndentAmount() {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.indentAmount(-1);
 }

 @Test
 public void testGetOutputSettings() {
 Document doc = new Document("http://example.com");
 Document.OutputSettings settings = doc.outputSettings();
 assertEquals(Document.OutputSettings.Entities.EscapeMode.base, settings.escapeMode());
 }

 @Test
 public void testSetOutputSettings() {
 Document doc = new Document("http://example.com");
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.escapeMode(Document.OutputSettings.Entities.EscapeMode.extended);
 doc.outputSettings(settings);
 assertEquals(Document.OutputSettings.Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
 }

 @Test
 public void testDuplicateHeadElements() {
 Document doc = new Document("http://example.com");
 Element head1 = doc.appendElement("head");
 Element head2 = doc.appendElement("head");
 doc.normalise();
 assertEquals(1, doc.getElementsByTag("head").size());
 }

 @Test
 public void testDuplicateBodyElements() {
 Document doc = new Document("http://example.com");
 Element body1 = doc.appendElement("body");
 Element body2 = doc.appendElement("body");
 doc.normalise();
 assertEquals(1, doc.getElementsByTag("body").size());
 }

 @Test
 public void testNullBaseUri() {
 Document doc = new Document(null);
 assertNotNull(doc);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }
}