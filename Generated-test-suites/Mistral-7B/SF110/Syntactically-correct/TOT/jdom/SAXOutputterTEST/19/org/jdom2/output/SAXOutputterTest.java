package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Collections;

public class SAXOutputterTest {

 @Test
 public void testEmptyDocumentOutput() {
 SAXOutputter outputter = new SAXOutputter();
 Document document = new Document();
 StringWriter writer = new StringWriter();
 outputter.setFormat(Format.getPrettyFormat());
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 try {
 outputter.output(document);
 } catch (JDOMException e) {
 // Should not throw an exception for empty document output
 fail("Unexpected exception: " + e.getMessage());
 }
 String expectedXml = "<document/>";
 String actualXml = writer.getBuffer().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testValidDocumentOutput() {
 SAXOutputter outputter = new SAXOutputter();
 Document document = new Document(new Element("root"));
 document.getRootElement().addContent(new Element("child1").setText("Child 1"));
 document.getRootElement().addContent(new Element("child2").setText("Child 2"));
 StringWriter writer = new StringWriter();
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.setFormat(Format.getPrettyFormat());
 try {
 outputter.output(document);
 } catch (JDOMException e) {
 // Should not throw an exception for valid document output
 fail("Unexpected exception: " + e.getMessage());
 }
 String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";
 String actualXml = writer.getBuffer().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testDTDDocumentOutput() {
 SAXOutputter outputter = new SAXOutputter();
 Document document = new Document(new Element("root"));
 document.getRootElement().setName("myroot");
 document.getRootElement().addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance", "xsi");
 document.getRootElement().addAttribute("xsi:noNamespaceSchemaLocation", "test.dtd");
 document.getRootElement().addContent(new Element("child1").setText("Child 1"));
 document.getRootElement().addContent(new Element("child2").setText("Child 2"));
 StringWriter writer = new StringWriter();
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.setDTDHandler(new DTDHandler() {
 @Override
 public void startDTD(String name, String publicId, String systemId) {
 assertEquals("test.dtd", systemId);
 }

 @Override
 public void endDTD() {
 // Empty
 }
 });
 outputter.setFormat(Format.getPrettyFormat());
 try {
 outputter.output(document);
 } catch (JDOMException e) {
 // Should not throw an exception for valid document output with DTD
 fail("Unexpected exception: " + e.getMessage());
 }
 String expectedXml = "<myroot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</myroot>";
 String actualXml = writer.getBuffer().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testEntityOutput() {
 SAXOutputter outputter = new SAXOutputter();
 Document document = new Document();
 document.setName("test");
 document.addContent(new Entity("amp;", "&amp;"));
 document.addContent(new Entity("apos;", "&apos;"));
 document.addContent(new Entity("quot;", "&quot;"));
 StringWriter writer = new StringWriter();
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.setFormat(Format.getPrettyFormat());
 try {
 outputter.output(document);
 } catch (JDOMException e) {
 // Should not throw an exception for document with entities
 fail("Unexpected exception: " + e.getMessage());
 }
 String expectedXml = "<test>\n<!ENTITY amp \"&amp;\"><!ENTITYapos\" \"&apos;\"><!ENTITY quot \"&quot;\">\n</test>";
 String actualXml = writer.getBuffer().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testErrorHandling() {
 SAXOutputter outputter = new SAXOutputter();
 Document document = new Document();
 StringWriter writer = new StringWriter();
 outputter.setErrorHandler(new ErrorHandler() {
 @Override
 public void error(SAXParseException exception) {
 assertEquals("Test error message", exception.getMessage());
 }
 });
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.setFormat(Format.getPrettyFormat());
 try {
 outputter.output(document);
 failure("Expected error to be thrown but was not");
 } catch (JDOMException e) {
 // Expected exception
 }
 document.getRootElement().setName("root");
 document.getRootElement().addContent(new Element("child1").setText("Child 1"));
 try {
 outputter.output(document);
 } catch (JDOMException e) {
 // Should not throw an exception for valid document output
 fail("Unexpected exception: " + e.getMessage());
 }
 document.getRootElement().addContent(new Element("child2").setText("Child 2"));
 try {
 outputter.output(document);
 failure("Expected error to be thrown but was not");
 } catch (JDOMException e) {
 // Expected exception with error message "Element 'child2' is already a child of element 'root'"
 String expectedExceptionMessage = "cvc-complex-type.2.4.a: The element type 'child2' must be terminated by the end of the content model.";
 assertEquals(expectedExceptionMessage, e.getMessage());
 }
 }

 @Test
 public void testDeclHandler() {
 SAXOutputter outputter = new SAXOutputter();
 Document document = new Document();
 document.getRootElement().setName("root");
 document.getRootElement().addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance", "xsi");
 document.getRootElement().addAttribute("xmlns:my", "http://example.com/my", "my");
 document.getRootElement().addContent(new Element("child1").setText("Child 1"));
 document.getRootElement().addContent(new Element("child2").setText("Child 2"));
 SAXOutputter.DeclHandler declHandler = new SAXOutputter.DeclHandler() {
 int declarationCount = 0;

 @Override
 public void startElementDecl(String name, String publicId, String systemId) {
 declarationCount++;
 }

 @Override
 public void endElementDecl() {
 // Empty
 }

 @Override
 public void startAttributeDecl(String eName, String qName, String type, String defaultValue, String value) {
 // Empty
 }

 @Override
 public void endAttributeDecl() {
 // Empty
 }

 @Override
 public void startEntityDecl(String name, String publicId, String systemId) {
 // Empty
 }

 @Override
 public void endEntityDecl() {
 // Empty
 }

 @Override
 public void startDTDDecl(String name, String publicId, String systemId) {
 // Empty
 }

 @Override
 public void endDTDDecl() {
 // Empty
 }

 @Override
 public void startNotationDecl(String name, String publicId, String systemId) {
 // Empty
 }

 @Override
 public void endNotationDecl() {
 // Empty
 }

 @Override
 public void startInternalSubset() {
 // Empty
 }

 @Override
 public void endInternalSubset() {
 // Empty
 }

 @Override
 public void startExternalSubset(String systemId) {
 // Empty
 }

 @Override
 public void endExternalSubset() {
 // Empty
 }

 @Override
 public void startEntity(String name) {
 // Empty
 }

 @Override
 public void endEntity(String name) {
 // Empty
 }

 @Override
 public void startProcessingInstruction(String target, String data) {
 // Empty
 }

 @Override
 public void endProcessingInstruction(String target) {
 // Empty
 }

 @Override
 public void startCDATA() {
 // Empty
 }

 @Override
 public void endCDATA() {
 // Empty
 }

 @Override
 public void startDocument() {
 // Empty
 }

 @Override
 public void endDocument() {
 // Empty
 }

 @Override
 public void startDocumentEntity(String name) {
 // Empty
 }

 @Override
 public void endDocumentEntity() {
 // Empty
 }

 @Override
 public void startDTD() {
 // Empty
 }

 @Override
 public void endDTD() {
 // Empty
 }

 @Override
 public void startProlog() {
 // Empty
 }

 @Override
 public void endProlog() {
 // Empty
 }

 @Override
 public void startEntityref(String name) {
 // Empty
 }

 @Override
 public void endEntityref() {
 // Empty
 }

 @Override
 public void startEntitySubstitution(String name) {
 // Empty
 }

 @Override
 public void endEntitySubstitution() {
 // Empty
 }

 @Override
 public void startComment(int lineNumber, String comments) {
 // Empty
 }

 @Override
 public void endComment() {
 // Empty
 }

 @Override
 public Element startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
 return null;
 }

 @Override
 public void endElement(String uri, String localName, String qName) throws SAXException {
 // Empty
 }

 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 // Empty
 }

 @Override
 public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
 // Empty
 }

 @Override
 public void processingInstruction(String target, String data) throws SAXException {
 // Empty
 }

 @Override
 public void skippedEntity(String name) throws SAXException {
 // Empty
 }

 @Override
 public void startDocument() throws SAXException {
 // Empty
 }

 @Override
 public void endDocument() throws SAXException {
 // Empty
 }

 @Override
 public void fatalError(SAXParseException exception) throws SAXException {
 // Empty
 }

 @Override
 public void warning(SAXParseException exception) throws SAXException {
 // Empty
 }

 @Override
 public void error(SAXParseException exception) throws SAXException {
 // Empty
 }

 @Override
 public void startPrefixMapping(String prefix, String uri) throws SAXException {
 // Empty
 }

 @Override
 public void endPrefixMapping(String prefix) throws SAXException {
 // Empty
 }
 };
 outputter.setDeclHandler(declHandler);
 StringWriter writer = new StringWriter();
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.setFormat(Format.getPrettyFormat());
 try {
 outputter.output(document);
 } catch (JDOMException e) {
 // Should not throw an exception for valid document output
 fail("Unexpected exception: " + e.getMessage());
 }
 int expectedDeclarationCount = 15;
 int actualDeclarationCount = declHandler.declarationCount;
 assertEquals(expectedDeclarationCount, actualDeclarationCount);
 }
}