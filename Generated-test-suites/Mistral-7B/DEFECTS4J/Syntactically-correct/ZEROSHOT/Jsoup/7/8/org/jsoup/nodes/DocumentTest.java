import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
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
 assertEquals("#html", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("#head", head.nodeName());
 Element body = html.body();
 assertNotNull(body);
 assertEquals("#body", body.nodeName());
 }

 @Test
 public void testAccessingHeadAndBody() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testGettingAndSettingTitle() {
 Document doc = new Document("http://example.com");
 String initialTitle = doc.title();
 assertEquals("", initialTitle);
 doc.title("Test Title");
 String newTitle = doc.title();
 assertEquals("Test Title", newTitle);
 }

 @Test
 public void testCreatingElement() {
 Document doc = new Document("http://example.com");
 Element newElement = doc.createElement("a");
 assertNotNull(newElement);
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendElement("div").appendText("Test");
 Element div = doc.getElementsByTag("div").first();
 assertEquals("Test", div.text());
 doc.normalise();
 Element body = doc.body();
 assertEquals("Test", body.getElementsByTag("text").first().text());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertNotNull(outputSettings.charset());
 }
}