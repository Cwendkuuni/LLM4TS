package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 private Document createDocument(String html) {
 return new Document(Parser.parseInput(html, ""), "").head();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "W3C";
 String systemId = "//www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com/";
 DocumentType htmlDocType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", htmlDocType.nodeName());
 assertEquals(name, htmlDocType.attr(DocumentType.NAME));
 assertEquals(publicId, htmlDocType.attr(DocumentType.PUBLIC_ID));
 assertEquals(systemId, htmlDocType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, htmlDocType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 String name = "html";
 DocumentType htmlDocType = new DocumentType(name);

 assertEquals("#doctype", htmlDocType.nodeName());
 assertNull(htmlDocType.attr(DocumentType.PUBLIC_ID));
 assertNull(htmlDocType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithQuirksMode() {
 String name = "html";
 String publicId = "W3C";
 String systemId = "//www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com/";
 String publicIdQuirk = "quirk";
 String systemIdQuirk = "//quirks.w3.org/QUIRKS-mode/html4/strict.dtd";
 DocumentType htmlDocType = new DocumentType(name, publicIdQuirk, systemIdQuirk, baseUri);

 assertEquals("#doctype", htmlDocType.nodeName());
 assertFalse(htmlDocType.has(DocumentType.PUBLIC_ID));
 assertFalse(htmlDocType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws Exception {
 String name = "html";
 String publicId = "W3C";
 String systemId = "//www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com/";
 DocumentType htmlDocType = new DocumentType(name, publicId, systemId, baseUri);
 Document document = createDocument("<!DOCTYPE html PUBLIC \"W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");

 StringBuilder accum = new StringBuilder();
 htmlDocType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!doctype html>", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithoutPublicAndSystemIds() throws Exception {
 String name = "html5";
 DocumentType htmlDocType = new DocumentType(name);
 Document document = createDocument("<!DOCTYPE html>");

 StringBuilder accum = new StringBuilder();
 htmlDocType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!doctype html>", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType htmlDocType = new DocumentType("html");
 Document document = new Document("");

 Node node = document.appendChild(htmlDocType);
 node.outerHtmlTail(new StringBuilder(), 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("", node.outerHtml());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 String name = "html";
 String publicId = "W3C";
 String systemId = "//www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com/";
 DocumentType htmlDocType = new DocumentType(name, publicId, systemId, baseUri);

 assertTrue(htmlDocType.has(DocumentType.NAME));
 assertTrue(htmlDocType.has(DocumentType.PUBLIC_ID));
 assertTrue(htmlDocType.has(DocumentType.SYSTEM_ID));

 assertFalse(htmlDocType.has("nonExistentAttribute"));
 }
}