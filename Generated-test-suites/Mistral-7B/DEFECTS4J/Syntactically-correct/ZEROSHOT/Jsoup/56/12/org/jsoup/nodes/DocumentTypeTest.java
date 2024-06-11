package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup. nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attribute;
import org.jsoup.nodes.attributes.Attributes;
import org.jsoup.nodes.Attributes.Named;
import org.jsoup.nodes.output.OutputSettings;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", "HTML", "http://www.w3.org/TR/html4/strict.dtd");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("HTML", documentType.attr(Named.NAME.toString()));
 assertTrue(documentType.has(Named.NAME.toString()));
 assertTrue(documentType.has(DocumentType.PUBLIC_KEY));
 assertTrue(documentType.has(DocumentType.SYSTEM_KEY));
 assertEquals("HTML", documentType.attr(DocumentType.NAME));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr(DocumentType.SYSTEM_ID));

 documentType = new DocumentType(document, "html", null, "http://www.w3.org/TR/html4/strict.dtd");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("HTML", documentType.attr(Named.NAME.toString()));
 assertTrue(documentType.has(Named.NAME.toString()));
 assertTrue(documentType.has(DocumentType.SYSTEM_KEY));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr(DocumentType.SYSTEM_ID));

 documentType = new DocumentType(document, "html", "HTML", null);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("HTML", documentType.attr(Named.NAME.toString()));
 assertTrue(documentType.has(Named.NAME.toString()));
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOuterHtml() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", "HTML", "http://www.w3.org/TR/html4/strict.dtd");

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlHead(sb, 0, out);
 documentType.outerHtmlTail(sb, 0, out);

 String expected = "<!doctype html PUBLIC \"HTML\" \"http://www.w3.org/TR/html4/strict.dtd\">";
 assertEquals(expected, sb.toString());

 documentType = new DocumentType(document, "html", "HTML", null);
 out.syntax(OutputSettings.Syntax.html);

 sb.setLength(0);
 documentType.outerHtmlHead(sb, 0, out);
 documentType.outerHtmlTail(sb, 0, out);

 expected = "<!doctype html>";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithoutPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", null, null);

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlHead(sb, 0, out);
 documentType.outerHtmlTail(sb, 0, out);

 String expected = "<!doctype html>";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithEmptyPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", "\"\", \"\"", null);

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlHead(sb, 0, out);
 documentType.outerHtmlTail(sb, 0, out);

 String expected = "<!doctype html>";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithPublicIdOnly() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", "PUBLIC", null);

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlHead(sb, 0, out);
 documentType.outerHtmlTail(sb, 0, out);

 String expected = "<!doctype html PUBLIC>";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithSystemIdOnly() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", null, "http://www.w3.org/TR/html4/strict.dtd");

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlHead(sb, 0, out);
 documentType.outerHtmlTail(sb, 0, out);

 String expected = "<!doctype html SYSTEM http://www.w3.org/TR/html4/strict.dtd>";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithBothPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", "PUBLIC \"HTML\" \"http://www.w3.org/TR/html4/strict.dtd\"", null);

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlHead(sb, 0, out);
 documentType.outerHtmlTail(sb, 0, out);

 String expected = "<!doctype html PUBLIC \"HTML\" \"http://www.w3.org/TR/html4/strict.dtd\">";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(document, "html", "HTML", "http://www.w3.org/TR/html4/strict.dtd");

 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 assertFalse(documentType.has("foo"));
 }
}