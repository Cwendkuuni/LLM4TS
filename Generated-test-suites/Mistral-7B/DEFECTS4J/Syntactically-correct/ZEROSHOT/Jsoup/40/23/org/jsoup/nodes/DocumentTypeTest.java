package org.jsoup.nodes.test;

import org.ajunicode.testutils.junit.JUnitUtils;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = JUnitUtils.newDocument();
 }

 @Test
 public void testCreateDocumentTypeWithValidParameters() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithEmptyName() {
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";

 try {
 new DocumentType("", publicId, systemId, baseUri);
 } catch (IllegalArgumentException e) {
 assertEquals("name cannot be empty", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testCreateDocumentTypeWithNullName() {
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";

 DocumentType documentType = new DocumentType(null, publicId, systemId, baseUri);
 assertNull(documentType.attr("name"));
 }

 @Test
 public void testCreateDocumentTypeWithValidPublicId() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertTrue(StringUtil.isBlank(documentType.attr("systemId")));
 }

 @Test
 public void testCreateDocumentTypeWithNullPublicId() {
 String name = "html";
 String publicId = null;
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertNull(documentType.attr("publicId"));
 }

 @Test
 public void testCreateDocumentTypeWithValidSystemId() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertNotNull(documentType.attr("systemId"));
 }

 @Test
 public void testCreateDocumentTypeWithNullSystemId() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = null;
 String baseUri = "http://example.com";

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testCreateDocumentTypeWithValidBaseUri() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testCreateDocumentTypeWithNullBaseUri() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = null;

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertNull(documentType.baseUri());
 }

 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlHead(accum, 0, out);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlTail(accum, 0, out);

 assertTrue(accum.toString().isEmpty());
 }

 @Test
 public void testCreateDocumentTypeWithQuirksMode() {
 String name = "html";
 String publicId = null;
 String systemId = null;
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 Validate.assertTrue(documentType.isQuirksMode());
 }

 @Test
 public void testCreateDocumentTypeWithoutQuirksMode() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertFalse(documentType.isQuirksMode());
 }

 @Test
 public void testCreateDocumentTypeWithValidQuirksModePublicId() {
 String name = "html";
 String publicId = "HTML_parsed_as_HTML3.2";
 String systemId = null;
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertTrue(documentType.isQuirksMode());
 }

 @Test
 public void testCreateDocumentTypeWithValidQuirksModeSystemId() {
 String name = "html";
 String publicId = null;
 String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertTrue(documentType.isQuirksMode());
 }

 @Test
 public void testCreateDocumentTypeWithInvalidQuirksModePublicId() {
 String name = "html";
 String publicId = "invalid_quirks_mode_public_id";
 String systemId = null;
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertFalse(documentType.isQuirksMode());
 }

 @Test
 public void testCreateDocumentTypeWithInvalidQuirksModeSystemId() {
 String name = "html";
 String publicId = null;
 String systemId = "invalid_quirks_mode_system_id";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertFalse(documentType.isQuirksMode());
 }

 @Test
 public void testCloneDocumentType() {
 DocumentType originalDocumentType = new DocumentType("html", null, null, null);
 DocumentType clonedDocumentType = (DocumentType) originalDocumentType.cloneNode(true);

 assertEquals(originalDocumentType.nodeName(), clonedDocumentType.nodeName());
 assertEquals(originalDocumentType.attr("name"), clonedDocumentType.attr("name"));
 assertEquals(originalDocumentType.attr("publicId"), clonedDocumentType.attr("publicId"));
 assertEquals(originalDocumentType.attr("systemId"), clonedDocumentType.attr("systemId"));
 assertEquals(originalDocumentType.baseUri(), clonedDocumentType.baseUri());
 }

 @Test
 public void testCloneDocumentTypeWithChildren() {
 Document document = JUnitUtils.newDocument();
 DocumentType originalDocumentType = new DocumentType("html", null, null, null);
 Element element = new Element("div");
 TextNode textNode = new TextNode("Test");
 element.appendChild(textNode);
 originalDocumentType.appendChild(element);
 DocumentType clonedDocumentType = (DocumentType) originalDocumentType.cloneNode(true);

 assertEquals(originalDocumentType.nodeName(), clonedDocumentType.nodeName());
 assertEquals(originalDocumentType.attr("name"), clonedDocumentType.attr("name"));
 assertEquals(originalDocumentType.attr("publicId"), clonedDocumentType.attr("publicId"));
 assertEquals(originalDocumentType.attr("systemId"), clonedDocumentType.attr("systemId"));
 assertEquals(originalDocumentType.baseUri(), clonedDocumentType.baseUri());
 assertEquals(originalDocumentType.childNodes().size(), clonedDocumentType.childNodes().size());

 Node node = clonedDocumentType.childNodes().get(0);
 assertSame(element, node);
 }
}