package org.jdom2.output;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Document doc;

 @Before
 public void setup() {
 XMLOutputter.DEFAULTPROCESSOR = new DefaultXMLProcessor();
 xmlo = new XMLOutputter();
 doc = new Document();
 }

 // Test constructor with Format and XMLOutputProcessor arguments
 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 Format format = new Format();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 XMLOutputter xmloWithFormatAndProcessor = new XMLOutputter(format, processor);
 assertEquals(format, xmloWithFormatAndProcessor.getFormat());
 assertEquals(processor, xmloWithFormatAndProcessor.getXMLOutputProcessor());
 }

 // Test constructor with Format argument
 @Test
 public void testXMLOutputterConstructorWithFormat() {
 Format format = new Format();
 XMLOutputter xmloWithFormat = new XMLOutputter(format);
 assertEquals(format, xmloWithFormat.getFormat());
 assertEquals(XMLOutputter.DEFAULTPROCESSOR, xmloWithFormat.getXMLOutputProcessor());
 }

 // Test constructor with XMLOutputProcessor argument
 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 XMLOutputter xmloWithProcessor = new XMLOutputter(processor);
 assertEquals(XMLOutputter.DEFAULTFORMAT, xmloWithProcessor.getFormat());
 assertEquals(processor, xmloWithProcessor.getXMLOutputProcessor());
 }

 // Test constructor with no arguments
 @Test
 public void testXMLOutputterConstructorNoArgs() {
 XMLOutputter xmloNoArgs = new XMLOutputter();
 assertEquals(XMLOutputter.DEFAULTFORMAT, xmloNoArgs.getFormat());
 assertEquals(XMLOutputter.DEFAULTPROCESSOR, xmloNoArgs.getXMLOutputProcessor());
 }

 // Test setFormat method
 @Test
 public void testSetFormat() {
 Format format = new Format();
 xmlo.setFormat(format);
 assertEquals(format, xmlo.getFormat());
 }

 // Test getFormat method
 @Test
 public void testGetFormat() {
 Format format = new Format();
 xmlo.setFormat(format);
 assertEquals(format, xmlo.getFormat());
 }

 // Test setXMLOutputProcessor method
 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 // Test getXMLOutputProcessor method
 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 // Test outputDocument method with OutputStream
 @Test
 public void testOutputDocumentWithOutputStream() throws IOException {
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(doc, output);
 assertEquals(doc.getRootElement().toString(), new String(output.toByteArray()));
 }

 // Test outputDocument method with Writer
 @Test
 public void testOutputDocumentWithWriter() throws IOException {
 Writer writer = new StringWriter();
 xmlo.output(doc, writer);
 assertEquals(doc.getRootElement().toString(), writer.toString());
 }

 // Test outputDocType method with OutputStream
 @Test
 public void testOutputDocTypeWithOutputStream() throws IOException {
 DocType docType = new DocType("root", new Element[]{}, new AttributeMap());
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(docType, output);
 assertEquals("<!DOCTYPE root [ ]>\r\n", new String(output.toByteArray()));
 }

 // Test outputDocType method with Writer
 @Test
 public void testOutputDocTypeWithWriter() throws IOException {
 DocType docType = new DocType("root", new Element[]{}, new AttributeMap());
 Writer writer = new StringWriter();
 xmlo.output(docType, writer);
 assertEquals("<!DOCTYPE root [ ]>\n", writer.toString());
 }

 // Test outputElement method with OutputStream
 @Test
 public void testOutputElementWithOutputStream() throws IOException {
 Element element = new Element("root");
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(element, output);
 assertEquals("<root/>\r\n", new String(output.toByteArray()));
 }

 // Test outputElement method with Writer
 @Test
 public void testOutputElementWithWriter() throws IOException {
 Element element = new Element("root");
 Writer writer = new StringWriter();
 xmlo.output(element, writer);
 assertEquals("<root/>\n", writer.toString());
 }

 // Test outputElementContent method with OutputStream
 @Test
 public void testOutputElementContentWithOutputStream() throws IOException {
 Element element = new Element("root");
 element.addContent(new Text("Text content"));
 OutputStream output = new ByteArrayOutputStream();
 xmlo.outputElementContent(element, output);
 assertEquals("Text content\r\n", new String(output.toByteArray()));
 }

 // Test outputElementContent method with Writer
 @Test
 public void testOutputElementContentWithWriter() throws IOException {
 Element element = new Element("root");
 element.addContent(new Text("Text content"));
 Writer writer = new StringWriter();
 xmlo.outputElementContent(element, writer);
 assertEquals("Text content\n", writer.toString());
 }

 // Test outputList method with OutputStream
 @Test
 public void testOutputListWithOutputStream() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(new Element("item1"));
 list.add(new Element("item2"));
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(list, output);
 assertEquals("<item1/><item2/>\r\n", new String(output.toByteArray()));
 }

 // Test outputList method with Writer
 @Test
 public void testOutputListWithWriter() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(new Element("item1"));
 list.add(new Element("item2"));
 Writer writer = new StringWriter();
 xmlo.output(list, writer);
 assertEquals("<item1/><item2/>\n", writer.toString());
 }

 // Test outputCDATA method with OutputStream
 @Test
 public void testOutputCDATAWithOutputStream() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Test CDATA]]>");
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(cdata, output);
 assertEquals("<![CDATA[\r\nTest CDATA\r\n]]>\r\n", new String(output.toByteArray()));
 }

 // Test outputCDATA method with Writer
 @Test
 public void testOutputCDATAWithWriter() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Test CDATA]]>");
 Writer writer = new StringWriter();
 xmlo.output(cdata, writer);
 assertEquals("<![CDATA[\nTest CDATA\n]]>\n", writer.toString());
 }

 // Test outputText method with OutputStream
 @Test
 public void testOutputTextWithOutputStream() throws IOException {
 Text text = new Text("Test text");
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(text, output);
 assertEquals("Test text\r\n", new String(output.toByteArray()));
 }

 // Test outputText method with Writer
 @Test
 public void testOutputTextWithWriter() throws IOException {
 Text text = new Text("Test text");
 Writer writer = new StringWriter();
 xmlo.output(text, writer);
 assertEquals("Test text\n", writer.toString());
 }

 // Test outputComment method with OutputStream
 @Test
 public void testOutputCommentWithOutputStream() throws IOException {
 Comment comment = new Comment("Test comment");
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(comment, output);
 assertEquals("<!--Test comment-->\r\n", new String(output.toByteArray()));
 }

 // Test outputComment method with Writer
 @Test
 public void testOutputCommentWithWriter() throws IOException {
 Comment comment = new Comment("Test comment");
 Writer writer = new StringWriter();
 xmlo.output(comment, writer);
 assertEquals("<!--Test comment-->\n", writer.toString());
 }

 // Test outputProcessingInstruction method with OutputStream
 @Test
 public void testOutputProcessingInstructionWithOutputStream() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\"");
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(pi, output);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n", new String(output.toByteArray()));
 }

 // Test outputProcessingInstruction method with Writer
 @Test
 public void testOutputProcessingInstructionWithWriter() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\"");
 Writer writer = new StringWriter();
 xmlo.output(pi, writer);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", writer.toString());
 }

 // Test outputEntityRef method with OutputStream
 @Test
 public void testOutputEntityRefWithOutputStream() throws IOException {
 EntityRef entityRef = new EntityRef("#comment");
 OutputStream output = new ByteArrayOutputStream();
 xmlo.output(entityRef, output);
 assertEquals("<!--comment-->\r\n", new String(output.toByteArray()));
 }

 // Test outputEntityRef method with Writer
 @Test
 public void testOutputEntityRefWithWriter() throws IOException {
 EntityRef entityRef = new EntityRef("#comment");
 Writer writer = new StringWriter();
 xmlo.output(entityRef, writer);
 assertEquals("<!--comment-->\n", writer.toString());
 }

 // Test outputElementContentString method with Element
 @Test
 public void testOutputElementContentStringWithElement() {
 Element element = new Element("root");
 element.addContent(new Text("Test content"));
 String result = xmlo.outputElementContentString(element);
 assertEquals("Test content", result);
 }

 // Test escapeAttributeEntities method
 @Test
 public void testEscapeAttributeEntities() {
 String str = "<tag attribute='< & >' />";
 String escapedStr = xmlo.escapeAttributeEntities(str);
 assertEquals("&lt;tag attribute='&lt; &gt;&gt;' &gt;", escapedStr);
 }

 // Test escapeElementEntities method
 @Test
 public void testEscapeElementEntities() {
 String str = "<tag><&></tag>";
 String escapedStr = xmlo.escapeElementEntities(str);
 assertEquals("<tag>&lt;&gt;</tag>", escapedStr);
 }

 // Test clone method
 @Test
 public void testClone() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = xmlo1.clone();
 assertNotSame(xmlo1, xmlo2);
 assertEquals(xmlo1.toString(), xmlo2.toString());
 }
}