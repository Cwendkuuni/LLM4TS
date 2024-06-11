package org.jsoup.nodes.test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 assertTrue(doc.head() instanceof Element);
 assertTrue(doc.body() instanceof Element);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("<html><body><head><title>Test Title</title></head><div>Test</div></body></html>");
 doc.normalise();
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 assertEquals("Test", body.childNodes().get(0).text());
 }

 @Test
 public void testNormaliseWithTextNodes() {
 Document doc = new Document("<html><head></html><body><textarea>Test\nText</textarea></body></html>");
 doc.normalise();
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("Test\nText", textNode.text());
 }

 @Test
 public void testNormaliseWithEmptyTextNodes() {
 Document doc = new Document("<html><head></html><body><p></p></body></html>");
 doc.normalise();
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("", textNode.text());
 }

 @Test
 public void testNormaliseWithMultiTags() {
 Document doc = new Document("<html><head><title>Test Title</title><style>body { color: red; }</style></head><body><div>Test</div></body></html>");
 doc.normalise();
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 Element div = body.childNodes().get(0);
 assertEquals("Test", div.text());
 assertEquals("div", div.tagName());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document();
 OutputSettings settings = doc.outputSettings();
 assertEquals(OutputSettings.Entities.EscapeMode.base, settings.escapeMode());
 assertEquals("UTF-8", settings.charset().name());
 }

 @Test
 public void testOutputSettingsCustomEscapeMode() {
 Document doc = new Document();
 OutputSettings settings = doc.outputSettings().escapeMode(OutputSettings.Entities.EscapeMode.extended);
 assertEquals(OutputSettings.Entities.EscapeMode.extended, settings.escapeMode());
 }

 @Test
 public void testOutputSettingsCustomCharset() {
 Document doc = new Document();
 OutputSettings settings = doc.outputSettings().charset(Charset.forName("ISO-8859-1"));
 assertEquals("ISO-8859-1", settings.charset().name());
 }

 @Test
 public void testOutputSettingsCustomIndentAmount() {
 Document doc = new Document();
 OutputSettings settings = doc.outputSettings().indentAmount(5);
 assertEquals(5, settings.indentAmount());
 }

 }