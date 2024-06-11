package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html");

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, depth, out);
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());

 docType.outerHtmlTail(accum, depth, out);
 assertNull(accum.toString());
 }

 @Test
 public void testDocumentTypeParse() {
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";

 Parser parser = new Parser();
 Element element = parser.parseOutOfContext(html);

 DocumentType docType = (DocumentType) element.child(0);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
 assertNull(docType.baseUri());
 }
}