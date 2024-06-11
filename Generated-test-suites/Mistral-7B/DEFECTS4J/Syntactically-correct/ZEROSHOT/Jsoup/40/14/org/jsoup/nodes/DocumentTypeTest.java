package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";

 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType docType = new DocumentType("html", "", "", "");

 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();

 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder();

 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();

 docType.outerHtmlTail(accum, 0, null);

 assertSame(accum, new StringBuilder());
 }

 @Test
 public void testParseDocumentType() {
 String html = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 Document doc = Parser.parseBodyFragment(html, null);
 Node node = doc.body();

 assertEquals(Node.DOCTYPE_NODE, node.nodeType());
 DocumentType docType = (DocumentType) node;

 assertEquals("html", docType.attr("name"));
 assertEquals("publicId", docType.attr("publicId"));
 assertEquals("systemId", docType.attr("systemId"));
 }

 @Test
 public void testParseDocumentTypeWithEmptyPublicIdAndSystemId() {
 String html = "<!DOCTYPE html>";
 Document doc = Parser.parseBodyFragment(html, null);
 Node node = doc.body();

 assertEquals(Node.DOCTYPE_NODE, node.nodeType());
 DocumentType docType = (DocumentType) node;

 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 }

}