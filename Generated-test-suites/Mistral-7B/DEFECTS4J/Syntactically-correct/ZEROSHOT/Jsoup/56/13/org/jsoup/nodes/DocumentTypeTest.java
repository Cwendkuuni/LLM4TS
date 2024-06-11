package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 private String baseUri = "http://test.com";

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", "systemId", baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertTrue(documentType.has("publicId"));
 assertTrue(documentType.has("systemId"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "", "", baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertFalse(documentType.has("publicId"));
 assertFalse(documentType.has("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithOnlyName() {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", null, null, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertFalse(documentType.has("publicId"));
 assertFalse(documentType.has("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithOnlyPublicId() {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", null, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertTrue(documentType.has("publicId"));
 assertFalse(documentType.has("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithOnlySystemId() {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", null, "systemId", baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertFalse(documentType.has("publicId"));
 assertTrue(documentType.has("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithAllAttributes() {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", "systemId", baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertTrue(documentType.has("publicId"));
 assertTrue(documentType.has("systemId"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeParsing() throws Exception {
 Document document = new Document(baseUri);
 Parser parser = new Parser();
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 Element root = parser.parse(html).getRootElement();
 DocumentType documentType = (DocumentType) root.child(0);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertTrue(documentType.has("publicId"));
 assertTrue(documentType.has("systemId"));
 assertEquals("html", documentType.attr("name"));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeParsingWithEmptyAttributes() throws Exception {
 Document document = new Document(baseUri);
 Parser parser = new Parser();
 String html = "<!DOCTYPE html>";
 Element root = parser.parse(html).getRootElement();
 DocumentType documentType = (DocumentType) root.child(0);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertFalse(documentType.has("publicId"));
 assertFalse(documentType.has("systemId"));
 }

 @Test
 public void testDocumentTypeParsingWithOnlyName() throws Exception {
 Document document = new Document(baseUri);
 Parser parser = new Parser();
 String html = "<!DOCTYPE html SYSTEM>";
 Element root = parser.parse(html).getRootElement();
 DocumentType documentType = (DocumentType) root.child(0);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertFalse(documentType.has("publicId"));
 assertFalse(documentType.has("systemId"));
 }

 @Test
 public void testDocumentTypeParsingWithOnlyPublicId() throws Exception {
 Document document = new Document(baseUri);
 Parser parser = new Parser();
 String html = "<!DOCTYPE html PUBLIC \"test\" \"test.dtd\">";
 Element root = parser.parse(html).getRootElement();
 DocumentType documentType = (DocumentType) root.child(0);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertTrue(documentType.has("publicId"));
 assertFalse(documentType.has("systemId"));
 assertEquals("test", documentType.attr("publicId"));
 }

 @Test
 public void testDocumentTypeParsingWithOnlySystemId() throws Exception {
 Document document = new Document(baseUri);
 Parser parser = new Parser();
 String html = "<!DOCTYPE html SYSTEM \"test.dtd\">";
 Element root = parser.parse(html).getRootElement();
 DocumentType documentType = (DocumentType) root.child(0);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertFalse(documentType.has("publicId"));
 assertTrue(documentType.has("systemId"));
 assertEquals("test.dtd", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeParsingWithAllAttributes() throws Exception {
 Document document = new Document(baseUri);
 Parser parser = new Parser();
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 Element root = parser.parse(html).getRootElement();
 DocumentType documentType = (DocumentType) root.child(0);

 assertEquals("#doctype", documentType.nodeName());
 assertTrue(documentType.has("name"));
 assertTrue(documentType.has("publicId"));
 assertTrue(documentType.has("systemId"));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtml() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", "systemId", baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithEmptyAttributes() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", null, null, baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html>", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithOnlyName() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", null, null, baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html>", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithOnlyPublicId() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", null, baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" >", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithOnlySystemId() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", null, "systemId", baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html SYSTEM \"systemId\">", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithAllAttributes() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", "systemId", baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithQuirkMode() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", "systemId", baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.QuirksMode.YES));
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\" [<!ENTITY % quirks \"\"\">]\n<!ENTITY % html SYSTEM \"\"\">\n<!ENTITY % body SYSTEM \"\"\">\n<!ENTITY % isindex SYSTEM \"\"\">\n<!ENTITY % frameset SYSTEM \"\"\">\n<!ENTITY % base SYSTEM \"\"\">", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithQuirkModeAndOnlyName() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", null, null, baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.QuirksMode.YES));
 assertEquals("<!DOCTYPE html>", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithQuirkModeAndOnlyPublicId() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", null, baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.QuirksMode.YES));
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" >", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithQuirkModeAndOnlySystemId() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", null, "systemId", baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.QuirksMode.YES));
 assertEquals("<!DOCTYPE html SYSTEM \"systemId\">", sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithQuirkModeAndAllAttributes() throws Exception {
 Document document = new Document(baseUri);
 DocumentType documentType = new DocumentType(document, "#doctypeName", "publicId", "systemId", baseUri);
 StringBuilder sb = new StringBuilder();
 documentType.outerHtml(sb, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.QuirksMode.YES));
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\" [<!ENTITY % quirks \"\"\">]\n<!ENTITY % html SYSTEM \"\"\">\n<!ENTITY % body SYSTEM \"\"\">\n<!ENTITY % isindex SYSTEM \"\"\">\n<!ENTITY % frameset SYSTEM \"\"\">\n<!ENTITY % base SYSTEM \"\"\">", sb.toString());
 }

}