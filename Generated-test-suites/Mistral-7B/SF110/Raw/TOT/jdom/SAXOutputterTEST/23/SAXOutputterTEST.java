package org.jdom2.output;

import junit.framework.TestCase;
import org.jdom2.DocumentBuilderFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.SAXOutputter;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.io.StringReader;

public class SAXOutputterTest extends TestCase {

 private Document doc;
 private SAXOutputter outputter;
 private StringWriter writer;

 @Before
 public void setUp() {
 doc = DocumentBuilderFactory.getDocumentBuilder().newDocument();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Element("child1").setText("Hello"));
 root.addContent(new Element("child2").setText("World"));
 }

 @Test
 public void testOutputDocument() {
 outputter = new SAXOutputter();
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 outputter.output(doc);
 String expected = "<root><child1>Hello</child1><child2>World</child2></root>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testOutputList() {
 outputter = new SAXOutputter();
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 outputter.output(doc.getContent().descendants("child1"));
 String expected = "<child1>Hello</child1>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testOutputElement() {
 outputter = new SAXOutputter();
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 outputter.output(doc.getRootElement());
 String expected = "<root><child1><child2>Hello</child2></child1><child2>World</child2></root>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testOutputFragmentList() {
 outputter = new SAXOutputter();
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 outputter.outputFragment(doc.getContent().descendants("child1"));
 String expected = "<child1>Hello</child1>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testOutputFragmentElement() {
 outputter = new SAXOutputter();
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 outputter.outputFragment(doc.getRootElement());
 String expected = "<root><child1><child2>Hello</child2></child1><child2>World</child2></root>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testErrorHandling() {
 outputter = new SAXOutputter();
 StringWriter writer = new StringWriter();
 outputter.setContentHandler(new ErrorHandlingContentHandler(writer));
 outputter.setErrorHandler(new ErrorHandlingErrorHandler());
 try {
 outputter.output(new Document());
 fail("Expected error to be thrown but none was");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testFormat() {
 outputter = new SAXOutputter(new Format("xml"));
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 outputter.output(doc);
 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<child1>Hello</child1>\n<child2>World</child2>\n</root>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testSAXOutputProcessor() {
 outputter = new SAXOutputter();
 outputter.setSAXOutputProcessor(new MySAXOutputProcessor());
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 outputter.output(doc);
 String expected = "<root><child1><child2>Hello</child2></child1><child2>World</child2></root>";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testSetFeatures() {
 outputter = new SAXOutputter();
 outputter.setReportDTDEvents(false);
 writer = new StringWriter();
 outputter.setContentHandler(new MyContentHandler(writer));
 try {
 outputter.output(doc);
 fail("Expected feature not set exception but none was thrown");
 } catch (SAXNotSupportedException e) {
 // Expected
 }
 }

 private static class MyContentHandler implements ContentHandler {
 private StringWriter writer;

 public MyContentHandler(StringWriter writer) {
 this.writer = writer;
 }

 @Override
 public void startDocument() throws SAXException {
 writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
 }

 @Override
 public void startElement(final String uri, final String localName, final String qName, final Attributes atts) throws SAXException {
 writer.write("<" + qName);
 for (int i = 0; i < atts.getLength(); i++) {
 writer.write(" " + atts.getLocalName(i) + "=\"" + atts.getValue(i) + "\"");
 }
 writer.write(">");
 }

 @Override
 public void endElement(final String uri, final String localName, final String qName) throws SAXException {
 writer.write("</" + qName + ">");
 }

 @Override
 public void characters(final char[] ch, final int start, final int length) throws SAXException {
 writer.write(new String(ch, start, length));
 }
 }

 private static class ErrorHandlingContentHandler implements ContentHandler {
 private StringWriter writer;

 public ErrorHandlingContentHandler(StringWriter writer) {
 this.writer = writer;
 }

 @Override
 public void startDocument() throws SAXException {
 writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
 }

 @Override
 public void startElement(final String uri, final String localName, final String qName, final Attributes atts) throws SAXException {
 writer.write("<" + qName);
 for (int i = 0; i < atts.getLength(); i++) {
 writer.write(" " + atts.getLocalName(i) + "=\"" + atts.getValue(i) + "\"");
 }
 writer.write(">");
 throw new RuntimeException("Error occurred while handling content");
 }
 }

 private static class ErrorHandlingErrorHandler implements ErrorHandler {
 @Override
 public void error(SAXParseException exception) throws SAXException {
 System.out.println("Error occurred: " + exception.getMessage());
 }
 }

 private static class MySAXOutputProcessor extends AbstractSAXOutputProcessor {
 @Override
 public void startDocument() throws SAXException {
 writer.write("<root>");
 }

 @Override
 public void endDocument() throws SAXException {
 writer.write("</root>");
 }
 }

}