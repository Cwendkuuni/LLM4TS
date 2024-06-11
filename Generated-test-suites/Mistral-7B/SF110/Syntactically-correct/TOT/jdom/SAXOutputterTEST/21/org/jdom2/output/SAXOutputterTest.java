package org.jdom2.output;

import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class SAXOutputterTest {

 @Test
 public void testDefaultSAXOutputter() {
 SAXOutputter outputter = new SAXOutputter();
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Text("Hello World!"));

 StringWriter writer = new StringWriter();
 outputter.setFormat(Format.getPrettyFormat());
 outputter.output(doc);
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.output(doc);

 String expectedOutput = "<root><text>Hello World!</text></root>";
 String actualOutput = writer.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSAXOutputterWithHandlers() {
 SAXOutputter outputter = new SAXOutputter(new StringWriterContentHandler(new StringWriter()));
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Text("Hello World!"));

 outputter.setFormat(Format.getPrettyFormat());
 outputter.output(doc);

 String expectedOutput = "<root><text>Hello World!</text></root>";
 String actualOutput = outputter.getContentHandler().getOutput();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSAXOutputterWithDTDHandler() {
 SAXOutputter outputter = new SAXOutputter(new DTDHandler() {
 @Override
 public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
 if (qName.equals("root")) {
 atts.setValue("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 }
 }
 });
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Text("Hello World!"));

 StringWriter writer = new StringWriter();
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.setFormat(Format.getPrettyFormat());
 outputter.output(doc);

 String expectedOutput = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><text>Hello World!</text></root>";
 String actualOutput = writer.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSAXOutputterWithErrorHandler() {
 SAXOutputter outputter = new SAXOutputter(new ErrorHandler() {
 @Override
 public void error(SAXParseException exception) throws SAXException {
 System.out.println("Error: " + exception.getMessage());
 }
 });
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 root.addContent(new Text("Hello World!"));

 StringWriter writer = new StringWriter();
 outputter.setContentHandler(new StringWriterContentHandler(writer));
 outputter.setFormat(Format.getPrettyFormat());
 outputter.output(doc);

 String expectedOutput = "<root><text>Hello World!</text></root>";
 String actualOutput = writer.toString();
 // Intentionally causing an error
 outputter.getSAXOutputProcessor().process(outputter.buildTarget(null), outputter.getFormat(), null);

 String errorMessage = "Error: "; // Replace with the expected error message
 assertTrue(actualOutput.contains(errorMessage));
 }

 @Test
 public void testSAXOutputterWithDeclHandler() {
 SAXOutputter outputter = new SAXOutputter(new DeclHandler() {
 @Override
 public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
if (atts.getValue("xmlns") != null) {
atts.addAttribute("", "test", "test");
}
}
 });
Document doc = new Document();
Element root = new Element("root");
doc.setRootElement(root);
root.addContent(new Text("Hello World!"));

StringWriter writer = new StringWriter();
outputter.setContentHandler(new StringWriterContentHandler(writer));
outputter.setFormat(Format.getPrettyFormat());
outputter.output(doc);

String expectedOutput = "<root xmlns=\"\" xmlns:test=\"test\"><text>Hello World!</text></root>";
String actualOutput = writer.toString();
assertEquals(expectedOutput, actualOutput);
}

@Test
public void testSAXOutputterWithFeature() {
SAXOutputter outputter = new SAXOutputter();
outputter.setFeature("http://xml.org/sax/features/validation", false);
Document doc = new Document();
SAXBuilder builder = new SAXBuilder();
try {
builder.build(new StringReader("<root><text>Hello World!</text></root>"));
Element root = (Element)builder.getDocument().getRootElement();
outputter.setFormat(Format.getPrettyFormat());
outputter.setContentHandler(new StringWriterContentHandler(new StringWriter()));
outputter.output(root);

String expectedOutput = "<root><text>Hello World!</text></root>";
String actualOutput = outputter.getContentHandler().getOutput().toString();
assertEquals(expectedOutput, actualOutput);
} catch (JDOMException e) {
fail("Exception should not be thrown: " + e.getMessage());
}
}

@Test
public void testSAXOutputterWithProperty() {
SAXOutputter outputter = new SAXOutputter();
outputter.setProperty("http://xml.org/sax/properties/declaration-handler", new DeclHandler() {
@Override
public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
atts.addAttribute("", "test", "test");
}
});
Document doc = new Document();
Element root = new Element("root");
doc.setRootElement(root);
root.addContent(new Text("Hello World!"));

StringWriter writer = new StringWriter();
outputter.setContentHandler(new StringWriterContentHandler(writer));
outputter.setFormat(Format.getPrettyFormat());
outputter.output(doc);

String expectedOutput = "<root xmlns=\"\" xmlns:test=\"test\"><text>Hello World!</text></root>";
String actualOutput = writer.toString();
assertEquals(expectedOutput, actualOutput);
}
}