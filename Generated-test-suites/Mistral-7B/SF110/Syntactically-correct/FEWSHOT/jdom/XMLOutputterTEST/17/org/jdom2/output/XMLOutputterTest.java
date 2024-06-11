package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

 XMLOutputter xmlOut;
 Document doc;
 OutputStream out;
 Writer writer;
 Formatter formatter;

 @Before
 public void setup() {
 doc = new JDOMBuilder().buildDocument();
 xmlOut = new XMLOutputter();
 try {
 out = new ByteArrayOutputStream();
 writer = new StringWriter();
 formatter = new Formatter(writer);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testEmptyOutput() {
 xmlOut.output(doc, writer);
 String output = writer.toString();
 assertEquals("", output);
 }

 @Test
 public void testOutputDocument() {
 xmlOut.output(doc, out);
 assertEquals(doc.getRootElement().toString(), xmlOut.outputString(doc));
 }

 @Test
 public void testOutputDocType() {
 DocType docType = new DefaultDocType("root", null, null);
 xmlOut.output(docType, out);
 assertEquals(docType.asString(), xmlOut.outputString(docType));
 }

 @Test
 public void testOutputElement() {
 Element element = new Element("root");
 xmlOut.output(element, writer);
 assertEquals("<root/>", xmlOut.outputString(element));
 }

 @Test
 public void testOutputElementContent() {
 Element element = new Element("root");
 Text text = new Text("Hello World!");
 element.addContent(text);
 xmlOut.outputElementContent(element, writer);
 assertEquals("Hello World!", xmlOut.outputString(text));
 }

 @Test
 public void testOutputListOfContents() {
 List<Content> list = new ArrayList<>();
 list.add(new Text("Element 1"));
 list.add(new Text("Element 2"));
 xmlOut.output(list, writer);
 assertEquals("<text>Element 1</text><text>Element 2</text>", xmlOut.outputString(list));
 }

 @Test
 public void testOutputCDATA() {
 CDATA cdata = new CDATA("<![CDATA[Hello World!]]>");
 xmlOut.output(cdata, writer);
 assertEquals("<![CDATA[Hello World!]]>", xmlOut.outputString(cdata));
 }

 @Test
 public void testOutputText() {
 Text text = new Text("Hello World!");
 xmlOut.output(text, writer);
 assertEquals("Hello World!", xmlOut.outputString(text));
 }

 @Test
 public void testOutputComment() {
 Comment comment = new Comment("This is a comment");
 xmlOut.output(comment, writer);
 assertEquals("<!--This is a comment-->", xmlOut.outputString(comment));
 }

 @Test
 public void testOutputProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 xmlOut.output(pi, writer);
 assertEquals("<?xml version=\"1.0\"?>", xmlOut.outputString(pi));
 }

 @Test
 public void testOutputEntityRef() {
 EntityRef entityRef = new EntityRef("#pcdata");
 xmlOut.output(entityRef, writer);
 assertEquals("&pcdata;", xmlOut.outputString(entityRef));
 }

 @Test
 public void testSetFormat() {
 xmlOut.setFormat(new Format("UTF-8"));
 assertEquals("UTF-8", xmlOut.getFormat().getEncoding());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor processor = new DefaultXMLOutputProcessor();
 xmlOut.setXMLOutputProcessor(processor);
 assertTrue(xmlOut.getXMLOutputProcessor() instanceof DefaultXMLOutputProcessor);
 }

 @Test
 public void testClone() {
 XMLOutputter cloned = xmlOut.clone();
 assertEquals(xmlOut, cloned);
 }

 @Test
 public void testOutputStringDocument() {
 assertEquals(xmlOut.outputString(doc), xmlOut.outputString(doc));
 }

 @Test
 public void testOutputStringDocType() {
 assertEquals(xmlOut.outputString(docType), xmlOut.outputString(docType));
 }

 @Test
 public void testOutputStringElement() {
 assertEquals(xmlOut.outputString(element), xmlOut.outputString(element));
 }

 @Test
 public void testOutputStringText() {
 assertEquals(xmlOut.outputString(text), xmlOut.outputString(text));
 }

 @Test
 public void testOutputStringListOfContents() {
 assertEquals(xmlOut.outputString(list), xmlOut.outputString(list));
 }

 @Test
 public void testOutputStringCDATA() {
 assertEquals(xmlOut.outputString(cdata), xmlOut.outputString(cdata));
 }

 @Test
 public void testOutputStringComment() {
 assertEquals(xmlOut.outputString(comment), xmlOut.outputString(comment));
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 assertEquals(xmlOut.outputString(pi), xmlOut.outputString(pi));
 }

 @Test
 public void testOutputStringEntityRef() {
 assertEquals(xmlOut.outputString(entityRef), xmlOut.outputString(entityRef));
 }

 @Test
 public void testOutputStringElementContent() {
 assertEquals(xmlOut.outputString(element.getContent()), xmlOut.outputElementContentString(element));
 }

 @Test
 public void testSetFormatWithEncodedInput() {
 Formatter formatter = new Formatter(writer, true);
 xmlOut.setFormat(new Format("UTF-8", "ISO-8859-1"));
 xmlOut.output(doc, writer);
 assertEquals(doc.getRootElement().toString(), xmlOut.outputString(doc));
 }

 @Test
 public void testSetFormatWithRawInput() {
 Formatter formatter = new Formatter(writer, false);
 xmlOut.setFormat(Format.getRawFormat());
 xmlOut.output(doc, writer);
 assertEquals(doc.getRootElement().toString(), xmlOut.outputString(doc));
 }

 @Test
 public void testSetXMLOutputProcessorWithCustomProcessor() {
 XMLOutputProcessor customProcessor = new MyXMLOutputProcessor();
 xmlOut.setXMLOutputProcessor(customProcessor);
 assertEquals(customProcessor, xmlOut.getXMLOutputProcessor());
 }

 @Test
 public void testToString() {
 String expected = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = \r\n]";
 assertEquals(expected, xmlOut.toString());
 }

 @Test
 public void testToStringWithCustomFormat() {
 Format customFormat = new Format("UTF-8", "ISO-8859-1");
 xmlOut = new XMLOutputter(customFormat, null);
 String expected = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = \r\n]";
 assertEquals(expected, xmlOut.toString());
 }
}

class MyXMLOutputProcessor extends AbstractXMLOutputProcessor {
 // Add your custom processing logic here if needed
}