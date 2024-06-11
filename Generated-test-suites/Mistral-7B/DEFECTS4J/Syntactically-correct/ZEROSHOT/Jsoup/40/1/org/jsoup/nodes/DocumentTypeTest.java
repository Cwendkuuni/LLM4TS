package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "HTML";
 String systemId = "HTML 4.01 Transitional";
 String baseUri = "http://example.com/";

 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithEmptyName() {
 String name = "";
 String publicId = "HTML";
 String systemId = "HTML 4.01 Transitional";
 String baseUri = "http://example.com/";

 try {
 new DocumentType(name, publicId, systemId, baseUri);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 StringBuilder accum = new StringBuilder();
 DocumentType docType = new DocumentType("html", "HTML", "HTML 4.01 Transitional", null);
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML\" \"HTML 4.01 Transitional\" >", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 StringBuilder accum = new StringBuilder();
 DocumentType docType = new DocumentType("html", null, null, null);
 docType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeParsing() {
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 Parser parser = new Parser();
 Document doc = parser.parse(html);
 Node node = doc.selectNodes("head > #doctype")[0];

 assertNotNull(node);
 assertEquals(DocumentType.class, node.getClass());
 DocumentType docType = (DocumentType) node;

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML", docType.attr("publicId"));
 assertEquals("HTML 4.01 Transitional", docType.attr("systemId"));
 assertEquals(null, docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithEmptyAttributes() {
 String name = "html";
 DocumentType docType = new DocumentType(name, null, null, null);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithPublicIdAndSystemId() {
 String name = "html";
 String publicId = "HTML";
 String systemId = "HTML 4.01 Transitional";
 String baseUri = "http://example.com/";

 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithNullAttributes() {
 String name = "html";
 DocumentType docType = new DocumentType(name, null, null, null);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithEqualNodes() {
 String name1 = "html";
 String publicId1 = "HTML";
 String systemId1 = "HTML 4.01 Transitional";
 String baseUri1 = "http://example.com/";
 String name2 = "html";
 String publicId2 = "HTML";
 String systemId2 = "HTML 4.01 Transitional";
 String baseUri2 = "http://example.com/";

 DocumentType docType1 = new DocumentType(name1, publicId1, systemId1, baseUri1);
 DocumentType docType2 = new DocumentType(name2, publicId2, systemId2, baseUri2);

 assertEquals(docType1, docType2);
 }

 @Test
 public void testDocumentTypeWithNonEqualNodes() {
 String name1 = "html";
 String publicId1 = "HTML";
 String systemId1 = "HTML 4.01 Transitional";
 String baseUri1 = "http://example.com/";
 String name2 = "html5";
 String publicId2 = "HTML5";
 String systemId2 = "HTML5 DOCTYPE";
 String baseUri2 = "http://example.com/";

 DocumentType docType1 = new DocumentType(name1, publicId1, systemId1, baseUri1);
 DocumentType docType2 = new DocumentType(name2, publicId2, systemId2, baseUri2);

 assertNotSame(docType1, docType2);
 }

 @Test
 public void testDocumentTypeToString() {
 String name = "html";
 String publicId = "HTML";
 String systemId = "HTML 4.01 Transitional";
 String baseUri = "http://example.com/";

 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("<!DOCTYPE " + name + (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"" + publicId + "\" ")
         + (StringUtil.isBlank(systemId) ? "" : " \"" + systemId + '"')
         + ">", docType.toString());
 }
}