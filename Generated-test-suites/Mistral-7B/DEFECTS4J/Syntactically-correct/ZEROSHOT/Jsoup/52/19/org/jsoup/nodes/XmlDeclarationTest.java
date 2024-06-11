package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

import org.junit.Test;

/**
 * Test class for XmlDeclaration.java
 */
public class XmlDeclarationTest {

 private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 private static final String XML_DECLARATION_PI = "<?xml-processing-instruction PI?>";
 private static final String XML_DECLARATION_NO_NAME = "<?>";

 @Test
 public void testXmlDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals(XML_DECLARATION, xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test
 public void testXmlDeclarationWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.1");
 assertEquals("xml version=\"1.1\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("encoding", "UTF-16");
 assertEquals("xml encoding=\"UTF-16\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().set("version", "1.1");
 xmlDeclaration.attributes().set("encoding", "UTF-16");
 assertEquals("xml version=\"1.1\" encoding=\"UTF-16\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-processing-instruction", null, true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml-processing-instruction", xmlDeclaration.name());
 assertEquals(XML_DECLARATION_PI, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationNoName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("", xmlDeclaration.name());
 assertEquals(XML_DECLARATION_NO_NAME, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testParseXmlDeclaration() throws IOException {
 Parser parser = new Parser();
 Document doc = parser.parse("<html>\r\n" + XML_DECLARATION + "\r\n<body>\r\n</body>\r\n</html>");
 Element body = doc.body();
 assertEquals(XmlDeclaration.class, body.child(0).getClass());
 XmlDeclaration xmlDeclaration = (XmlDeclaration) body.child(0);
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("1.0", xmlDeclaration.attributes().get("version"));
 assertEquals("UTF-8", xmlDeclaration.attributes().get("encoding"));
 }

}