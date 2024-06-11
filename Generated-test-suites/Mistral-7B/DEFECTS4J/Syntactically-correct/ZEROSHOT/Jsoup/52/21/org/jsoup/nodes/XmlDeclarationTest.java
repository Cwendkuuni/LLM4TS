package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for XmlDeclaration.java
 *
 * @author [Your Name]
 */
public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertNull(xmlDeclaration.name());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
 assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 assertNull(xmlProcessingInstruction.name());
 assertEquals("<?xml>", xmlProcessingInstruction.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test (expected = NullPointerException.class)
 public void testXmlDeclarationCreationWithNullName() {
 Validate.notNull(new XmlDeclaration(null, null, false));
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<?xml>", xmlDeclaration.toString());
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml>", xmlProcessingInstruction.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Document document = new Document();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Element xmlElement = document.createElement("root");
 xmlElement.appendChild(xmlDeclaration);
 StringBuilder stringBuilder = new StringBuilder();
 xmlDeclaration.outerHtmlHead(stringBuilder, 0, null);
 assertEquals("<?xml>", stringBuilder.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Document document = new Document();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Element xmlElement = document.createElement("root");
 xmlElement.appendChild(xmlDeclaration);
 StringBuilder stringBuilder = new StringBuilder();
 xmlDeclaration.outerHtmlTail(stringBuilder, 0, null);
 assertEquals("", stringBuilder.toString());
 }
}