import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @After
 public void tearDown() {
 document = null;
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testHead() {
 Element head = document.head();
 assertNotNull(head);
 }

 @Test
 public void testBody() {
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testTitle() {
 String title = document.title();
 assertEquals("", title);
 document.title("Test Title");
 title = document.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("a");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 document.appendElement("p").text("Test");
 document.normalise();
 Element body = document.body();
 TextNode textNode = (TextNode) body.child(0);
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testOutputSettings() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 assertTrue(outputSettings.prettyPrint());
 assertEquals(1, outputSettings.indentAmount());
 }

 @Test
 public void testSetOutputSettings() {
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.escapeMode(Entities.EscapeMode.extended);
 outputSettings.charset("ISO-8859-1");
 outputSettings.prettyPrint(false);
 outputSettings.indentAmount(3);
 document.outputSettings(outputSettings);
 OutputSettings outputSettingsNew = document.outputSettings();
 assertEquals(Entities.EscapeMode.extended, outputSettingsNew.escapeMode());
 assertEquals("ISO-8859-1", outputSettingsNew.charset().name());
 assertFalse(outputSettingsNew.prettyPrint());
 assertEquals(3, outputSettingsNew.indentAmount());
 }
}