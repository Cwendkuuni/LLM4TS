package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("publicId", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemId", documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.children());
 }

 @Test
 public void testDocumentTypeCreationWithoutAttributes() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypetoString() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("<!DOCTYPE html>", documentType.toString());
 }

 @Test
 public void testDocumentTypeOutputHtml() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html>", out.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlWithPublicId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", null, null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", out.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlWithSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, "systemId", null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html SYSTEM \"systemId\">", out.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlWithPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\">", out.toString());
 }

 @Test
 public void testDocumentTypeOutputXml() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html>", out.toString());
 }

 @Test
 public void testDocumentTypeOutputXmlWithPublicId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", null, null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", out.toString());
 }

 @Test
 public void testDocumentTypeOutputXmlWithSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, "systemId", null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html SYSTEM \"systemId\">", out.toString());
 }

 @Test
 public void testDocumentTypeOutputXmlWithPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
 document.body().appendChild(documentType);
 document.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 StringBuilder out = new StringBuilder();
 document.output(out);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\">", out.toString());
 }

 @Test
 public void testDocumentTypeWithNullAttributes() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithEmptyAttributes() {
 DocumentType documentType = new DocumentType("html", "", "", null);

 assertEquals("#doctype", documentType.nodeName());
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithInvalidAttributes() {
 DocumentType documentType = new DocumentType("html", "invalid", "invalid", null);

 assertEquals("#doctype", documentType.nodeName());
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeParsing() throws Exception {
 Document document = new Document();
 Parser parser = new Parser(document);
 document = parser.parse("<!doctype html>");

 assertEquals("#doctype", document.childNodes().get(0).nodeName());
 assertNull(document.childNodes().get(0).children());
 }

 @Test
 public void testDocumentTypeParsingWithPublicId() throws Exception {
 Document document = new Document();
 Parser parser = new Parser(document);
 document = parser.parse("<!DOCTYPE html PUBLIC \"publicId\">");

 assertEquals("#doctype", document.childNodes().get(0).nodeName());
 assertFalse(document.childNodes().get(0).has(DocumentType.PUBLIC_ID));
 assertFalse(document.childNodes().get(0).has(DocumentType.SYSTEM_ID));
 assertEquals("publicId", document.childNodes().get(0).attr(DocumentType.PUBLIC_ID));
 }

 @Test
 public void testDocumentTypeParsingWithSystemId() throws Exception {
 Document document = new Document();
 Parser parser = new Parser(document);
 document = parser.parse("<!DOCTYPE html SYSTEM \"systemId\">");

 assertEquals("#doctype", document.childNodes().get(0).nodeName());
 assertFalse(document.childNodes().get(0).has(DocumentType.PUBLIC_ID));
 assertFalse(document.childNodes().get(0).has(DocumentType.SYSTEM_ID));
 assertEquals("systemId", document.childNodes().get(0).attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeParsingWithPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 Parser parser = new Parser(document);
 document = parser.parse("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\">");

 assertEquals("#doctype", document.childNodes().get(0).nodeName());
 assertTrue(document.childNodes().get(0).has(DocumentType.PUBLIC_ID));
 assertEquals("publicId", document.childNodes().get(0).attr(DocumentType.PUBLIC_ID));
 assertTrue(document.childNodes().get(0).has(DocumentType.SYSTEM_ID));
 assertEquals("systemId", document.childNodes().get(0).attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeParsingWithQuirksMode() throws Exception {
 Document document = new Document();
 Parser parser = new Parser(document);
 document = parser.parse("<!DOCTYPE html [<!ENTITY % quirks SYSTEM \"quirks.dtd\"> %]>");

 assertEquals("#doctype", document.childNodes().get(0).nodeName());
 assertFalse(document.childNodes().get(0).has(DocumentType.PUBLIC_ID));
 assertFalse(document.childNodes().get(0).has(DocumentType.SYSTEM_ID));
 Element quirks = (Element) document.childNodes().get(1);
 assertEquals("ENTITY", quirks.nodeName());
 assertEquals("quirks", quirks.nodeName());
 assertEquals("quirks.dtd", quirks.attr("systemId"));
 }
}