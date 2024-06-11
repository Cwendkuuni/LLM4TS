package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeToString() throws IOException {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri());

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
 docType.outerHtmlHead(writer, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 writer.flush();
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testDocumentTypeWithEmptyName() {
 Document doc = new Document();
 DocumentType docType = new DocumentType(null, "", "", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithNullBaseUri() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", null);

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri());

 assertTrue(docType.has(DocumentType.NAME));
 assertTrue(docType.has(DocumentType.PUBLIC_ID));
 assertTrue(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithoutAttribute() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, doc.baseUri());

 assertFalse(docType.has(DocumentType.NAME));
 assertFalse(docType.has(DocumentType.PUBLIC_ID));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithQuirksMode() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 docType.attr(DocumentType.NAME, "html");
 docType.attr(DocumentType.PUBLIC_ID, "publicIdQuirks");
 docType.attr(DocumentType.SYSTEM_ID, "systemIdQuirks");
 docType.attr("quirksMode", "true");

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
 docType.outerHtmlHead(writer, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 writer.flush();
 String expected = "<!DOCTYPE html PUBLIC \"publicIdQuirks\" \"systemIdQuirks\" [<!ENTITY % quirks SYSTEM \"about:legacy-compat\">]\n<!ENTITY % HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\" [<!ENTITY % HTML_QUIRKS SYSTEM \"about:legacy-compat\">]>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testDocumentTypeWithParentElement() throws IOException {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 Element parent = doc.createElement("html");
 parent.appendChild(docType);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
 parent.outerHtml(writer, Document.OutputSettings.prettyPrint(true));
 String expected = "<html>\n<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">\n</html>";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testDocumentTypeWithTextNodeChild() throws IOException {
 Document doc = new Document();
 TextNode textNode = doc.createTextNode("This is a test");
 DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 docType.appendChild(textNode);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
 doc.outerHtml(writer, Document.OutputSettings.prettyPrint(true));
 String expected = "<html>\n<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">\n<textarea>\nThis is a test\n</textarea>\n</html>";
 assertEquals(expected, outputStream.toString());
 }
}