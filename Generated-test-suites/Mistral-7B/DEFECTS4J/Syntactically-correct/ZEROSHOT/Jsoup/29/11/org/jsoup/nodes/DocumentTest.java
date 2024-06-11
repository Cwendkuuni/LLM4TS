package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.StandardCharsets;
import org.jsoup.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java.
 */
public class DocumentTest {

 @Test
 public void testNewDocument() {
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
 public void testGetElementHead() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 }

 @Test
 public void testGetElementBody() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testGetStringTitle() {
 Document doc = new Document("http://example.com<title>Test Title</title>");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetStringTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("a");
 assertNotNull(element);
 assertEquals("a", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body></body></html>");
 doc.normalise();
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testNormaliseTextNodes() {
 Document doc = new Document("<html><head><title>Test Title</title></html><body></body><html><head><title>Test Title</title></html>");
 doc.normalise();
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());

 int textNodesCountBefore = body.childNodes().size();
 assertEquals(0, textNodesCountBefore);

 TextNode textNode = doc.createElement("textNode").text(" Test text ");
 body.appendChild(textNode);

 int textNodesCountAfter = body.childNodes().size();
 assertEquals(1, textNodesCountAfter);

 body.prependChild(textNode);
 int textNodesCountAfterAppended = body.childNodes().size();
 assertEquals(1, textNodesCountAfterAppended);
 }

 @Test
 public void testNormaliseStructure() {
 Document doc = new Document("<html><head><title>Test Title 1</title></html><html><head><title>Test Title 2</title></html>");
 doc.normalise();
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 assertEquals(1, head.childNodes().size());

 String title = head.childNodes().get(0).text();
 assertEquals("Test Title 1", title);
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><div id='test'><p>Test</p></div></body></html>");
 Element div = doc.findFirstElementByTagName("div", doc.body());
 assertNotNull(div);
 assertEquals("div", div.nodeName());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("<html><head><title>Test Title</title></html><body><p>Test</p></body>");
 String outerHtml = doc.outerHtml();
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", outerHtml);
 }

 @Test
 public void testText() {
 Document doc = new Document("<html><head><title>Test Title</title></html><body><p>Test</p></body>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.text().get());
 }

 @Test
 public void testClone() {
 Document originalDoc = new Document("<html><head><title>Test Title</title></html><body><p>Test</p></body></html>");
 Document clonedDoc = originalDoc.clone();
 assertNotNull(clonedDoc);
 assertNotSame(originalDoc, clonedDoc);
 }

 @Test
 public void testOutputSettings() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></html><body><p>Test</p></body></html>", "UTF-8");
 OutputSettings outputSettings = doc.outputSettings();
 assertNotNull(outputSettings);
 assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
 }

 @Test
 public void testOutputSettingsEscapeMode() {
 Document doc = Jsoup.parse("<html><head><title>Test Title &amp; < ></title></html><body><p>Test &amp; < ></p></body></html>", "UTF-8");
 OutputSettings outputSettings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 }

 @Test
 public void testOutputSettingsPrettyPrint() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></html><body><p>Test</p></body></html>", "UTF-8");
 doc.outputSettings(new OutputSettings().prettyPrint(false));
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testOutputSettingsIndentAmount() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></html><body><p>Test</p></body></html>", "UTF-8");
 doc.outputSettings(new OutputSettings().indentAmount(2));
 assertEquals("<html>\n<head>\n<title>Test Title</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>", doc.outerHtml());
 }
}