package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

/**
 * Tests for {@link XmlDeclaration} class.
 */
public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationInstance() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertNotNull(xmlDeclaration);
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationInvalidName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test
 public void testXmlDeclarationWithNameAndBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertFalse(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("<?xml", "baseUri", true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("<?xml", xmlDeclaration.name());
 assertTrue(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testGetWholeDeclaration() throws Exception {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());

 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithVersion.attributes().put("version", "1.0");
 assertEquals("xml version=\"1.0\"", xmlDeclarationWithVersion.getWholeDeclaration());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("<?xml", null, true);
 assertEquals("<?xml", xmlProcessingInstruction.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Document document = Jsoup.parse("", Parser.xmlParser());
 Element rootElement = document.createElement("root");
 rootElement.appendChild(xmlDeclaration);
 String expected = "<declaration xml:space=\"preserve\">";
 rootElement.outerHtml(new StringBuilder(), 0, new Document.OutputSettings());
 String actual = xmlDeclaration.outerHtml("", 0, new Document.OutputSettings());
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlTail() throws Exception {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Document document = Jsoup.parse("", Parser.xmlParser());
 Element rootElement = document.createElement("root");
 rootElement.appendChild(xmlDeclaration);
 String expected = "";
 rootElement.outerHtmlTail(new StringBuilder(), 0, new Document.OutputSettings());
 String actual = xmlDeclaration.outerHtmlTail("", 0, new Document.OutputSettings());
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() throws Exception {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals(xmlDeclaration.toString(), xmlDeclaration.outerHtml());
 }
}