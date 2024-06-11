package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DocumentTypeTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyNameThrowsException() {
 String name = "";
 String publicId = "publicId";
 String systemId = "systemId";
 String baseUri = "baseUri";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test
 public void testOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicId() {
 DocumentType documentType = new DocumentType("html", "", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptySystemId() {
 DocumentType documentType = new DocumentType("html", "publicId", "", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" >", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNullPublicId() {
 DocumentType documentType = new DocumentType("html", null, "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNullSystemId() {
 DocumentType documentType = new DocumentType("html", "publicId", null, "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" >", accum.toString());
 }

 @Test
 public void testAddAttributes() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 documentType.attr("data", "test");
 Node node = documentType.clone();

 assertSame(documentType, node);
 Attribute attribute = (Attribute) node.child(0);
 assertEquals("data", attribute.attrName());
 assertEquals("test", attribute.attrValue());
 }

 @Test
 public void testCloneWithoutAttributes() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 Node clonedNode = documentType.clone();

 assertNotNull(clonedNode);
 assertSame(documentType.getClass(), clonedNode.getClass());
 assertNull(clonedNode.child(0));
 }

 @Test
 public void testCloneWithAttributes() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 documentType.attr("attr1", "value1");
 documentType.attr("attr2", "value2");
 Node clonedNode = documentType.clone();

 assertNotNull(clonedNode);
 assertSame(documentType.getClass(), clonedNode.getClass());
 assertEquals(2, clonedNode.childNodes().size());
 Node attribute1Node = clonedNode.child(0);
 Node attribute2Node = clonedNode.child(1);
 assertSame(documentType.attr("attr1"), attribute1Node.attr("value1"));
 assertSame(documentType.attr("attr2"), attribute2Node.attr("value2"));
 }

 @Test
 public void testNodeNameIsDOCTYPE() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testTextNodeAsChild() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 TextNode textNode = new TextNode("text");
 documentType.appendChild(textNode);
 Node child = documentType.child(0);

 assertSame(textNode, child);
 }

 @Test
 public void testElementAsChild() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 Element element = new Element("div");
 documentType.appendChild(element);
 Node child = documentType.child(0);

 assertSame(element, child);
 }
}