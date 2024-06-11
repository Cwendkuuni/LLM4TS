package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for XmlDeclaration
 */
public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.0");
 assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("encoding", "UTF-8");
 assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.0");
 xmlDeclaration.attributes().set("encoding", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<?xml?>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithAttributesToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.0");
 xmlDeclaration.attributes().set("encoding", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtml() throws Exception {
 Document document = new Document();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Element element = document.createElement("html");
 element.appendChild(xmlDeclaration);
 StringBuilder expected = new StringBuilder();
 expected.append("<html>");
 expected.append("<!--[if gt IE 7]>");
 expected.append("<xml version=\"1.0\" encoding=\"UTF-8\">");
 expected.append("<![endif]-->");
 xmlDeclaration.outerHtmlHead(expected, 0, null);
 expected.append("</xml>");
 expected.append("</html>");
 StringBuilder actual = new StringBuilder();
 xmlDeclaration.outerHtml(actual, 0, null);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws Exception {
 StringBuilder expected = new StringBuilder();
 expected.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.0");
 xmlDeclaration.attributes().set("encoding", "UTF-8");
 xmlDeclaration.outerHtmlHead(expected, 0, null);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", expected.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws Exception {
 StringBuilder expected = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlTail(expected, 0, null);
 assertEquals("", expected.toString());
 }
}