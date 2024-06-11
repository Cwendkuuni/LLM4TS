package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
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
 DocumentType docType = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://www.w3.org/TR/html4/strict.dtd");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML PUBLIC -//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
 assertEquals("DTD_HTML_4_STRICT.dtd", docType.attr("systemId"));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithNullPublicId() {
 DocumentType docType = new DocumentType("html", null, "DTD_HTML_4_STRICT.dtd", "http://www.w3.org/TR/html4/strict.dtd");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertEquals("DTD_HTML_4_STRICT.dtd", docType.attr("systemId"));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithNullSystemId() {
 DocumentType docType = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01//EN", null, "http://www.w3.org/TR/html4/strict.dtd");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML PUBLIC -//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyBaseUri() {
 DocumentType docType = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML PUBLIC -//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
 assertEquals("DTD_HTML_4_STRICT.dtd", docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://www.w3.org/TR/html4/strict.dtd");
 Element html = new Element("html");
 html.append(docType);

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html");
 expected.append(" PUBLIC \"").append("HTML PUBLIC -//W3C//DTD HTML 4.01//EN").append("\"");
 expected.append('>');

 StringBuilder actual = new StringBuilder();
 docType.outerHtmlHead(actual, 0, null);

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML PUBLIC -//W3C//DTD HTML 4.01//EN", "DTD_HTML_4_STRICT.dtd", "http://www.w3.org/TR/html4/strict.dtd");

 StringBuilder expected = new StringBuilder();
 StringBuilder actual = new StringBuilder();
 docType.outerHtmlTail(actual, 0, null);

 assertTrue(actual.toString().isEmpty());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeParse() throws Exception {
 Parser parser = new Parser();
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"DTD_HTML_4_STRICT.dtd\">";
 Document doc = parser.parse(html);

 Node node = doc.selectFirst("#doctype");
 DocumentType docType = (DocumentType) node;

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML PUBLIC -//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
 assertEquals("DTD_HTML_4_STRICT.dtd", docType.attr("systemId"));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.baseUri());
 }

 @Test
 public void testDocumentTypeParseWithNullPublicId() throws Exception {
 Parser parser = new Parser();
 String html = "<!DOCTYPE html SYSTEM \"DTD_HTML_4_STRICT.dtd\">";
 Document doc = parser.parse(html);

 Node node = doc.selectFirst("#doctype");
 DocumentType docType = (DocumentType) node;

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertEquals(null, docType.attr("systemId"));
 assertNull(docType.baseUri());
 }
}