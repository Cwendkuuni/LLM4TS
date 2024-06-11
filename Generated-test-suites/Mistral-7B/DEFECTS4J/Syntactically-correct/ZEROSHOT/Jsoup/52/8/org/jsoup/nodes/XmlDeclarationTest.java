package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertNotNull(xmlDeclaration);
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationCreationNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test
 public void testXmlDeclarationName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationNoAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.0");
 xmlDeclaration.attributes().set("encoding", "UTF-8");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml/>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationToStringWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.0");
 xmlDeclaration.attributes().set("encoding", "UTF-8");
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclarationPI = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml>", xmlDeclarationPI.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Document doc = new Document();
 Element root = doc.createElement("root");
 root.appendChild(xmlDeclaration);
 doc.appendBody(root);
 StringBuilder sb = new StringBuilder();
 xmlDeclaration.outerHtmlHead(sb, 0, null);
 xmlDeclaration.outerHtmlTail(sb, 0, null);
 assertEquals("<xml/>", xmlDeclaration.toString());
 assertEquals("<xml/>\n", sb.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHeadWithAttributes() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.0");
 xmlDeclaration.attributes().set("encoding", "UTF-8");
 Document doc = new Document();
 Element root = doc.createElement("root");
 root.appendChild(xmlDeclaration);
 doc.appendBody(root);
 StringBuilder sb = new StringBuilder();
 xmlDeclaration.outerHtmlHead(sb, 0, null);
 xmlDeclaration.outerHtmlTail(sb, 0, null);
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.toString());
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>\n", sb.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHeadProcessingInstruction() throws IOException {
 XmlDeclaration xmlDeclarationPI = new XmlDeclaration("xml", null, true);
 Document doc = new Document();
 Element root = doc.createElement("root");
 root.appendChild(xmlDeclarationPI);
 doc.appendBody(root);
 StringBuilder sb = new StringBuilder();
 xmlDeclarationPI.outerHtmlHead(sb, 0, null);
 xmlDeclarationPI.outerHtmlTail(sb, 0, null);
 assertEquals("<?xml>\n", sb.toString());
 }

 @Test
 public void testXmlDeclarationSetAttributeNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put(null, "value");
 assertNull(xmlDeclaration.attributes().get(null));
 }

 @Test
 public void testXmlDeclarationSetAttributeNullValue() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", null);
 assertNull(xmlDeclaration.attributes().get("version"));
 }
}