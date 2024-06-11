import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

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
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 doc.normalise();
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertEquals(1, doc.body().childNodes().size());
 }

 @Test(expected = NullPointerException.class)
 public void testNormaliseNullElement() {
 Document doc = new Document("http://example.com");
 doc.normalise(null);
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 Element head = doc.findFirstElementByTagName("head", doc);
 Element body = doc.findFirstElementByTagName("body", doc);
 Element title = doc.findFirstElementByTagName("title", doc);
 Element empty = doc.findFirstElementByTagName("div", doc);

 assertNotNull(head);
 assertNotNull(body);
 assertNotNull(title);
 assertNull(empty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindFirstElementByTagNameInvalidTagName() {
 Document doc = new Document("http://example.com");
 doc.findFirstElementByTagName("invalid", doc);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test
 public void testClone() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 Document clone = doc.clone();
 assertNotNull(clone);
 assertEquals(doc.outerHtml(), clone.outerHtml());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 OutputSettings outputSettings = doc.outputSettings();
 doc.outputSettings(new OutputSettings());
 assertNotSame(outputSettings, doc.outputSettings());
 }
}