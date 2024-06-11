import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
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
 assertNotNull(document);
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testCreateShell() {
 Element html = document.head();
 Element head = html.lastChild();
 Element body = html.lastChild();
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testSetTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.head().lastChild().text());
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("a");
 assertNotNull(element);
 assertEquals("a", element.tagName());
 }

 @Test
 public void testNormalise() {
 document.title("Test Title");
 document.body().text("Test Body");
 document.head().appendElement("p").text("Test Paragraph");
 document.normalise();
 assertNotNull(document.getElementsByTag("title").first());
 assertNotNull(document.getElementsByTag("head").first());
 assertNotNull(document.getElementsByTag("body").first());
 assertEquals("Test Title", document.title());
 assertEquals("Test Body", document.body().text());
 assertEquals(0, document.head().childNodeSize());
 }

 @Test
 public void testText() {
 document.text("Test Text");
 assertEquals("Test Text", document.outerHtml());
 }

 @Test
 public void testOutputSettings() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertNotNull(outputSettings.charset());
 assertEquals(4, outputSettings.encoder().encode("Test").length());
 assertTrue(outputSettings.prettyPrint());
 assertEquals(1, outputSettings.indentAmount());
 outputSettings.prettyPrint(false);
 assertEquals(false, outputSettings.prettyPrint());
 outputSettings.indentAmount(2);
 assertEquals(2, outputSettings.indentAmount());
 }

}