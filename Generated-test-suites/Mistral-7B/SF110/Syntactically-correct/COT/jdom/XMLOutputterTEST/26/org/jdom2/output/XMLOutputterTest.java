package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Format format;
 private OutputStream outputStream;
 private Writer writer;

 @Before
 public void setUp() {
 xmlo = new XMLOutputter();
 format = new Format();
 outputStream = new MemoryOutputStream();
 writer = makeWriter(outputStream, format);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmloWithFormatAndProcessor = new XMLOutputter(format, null);
 assertEquals(format, xmloWithFormatAndProcessor.getFormat());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmloWithFormat = new XMLOutputter(format);
 assertEquals(format, xmloWithFormat.getFormat());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmloWithProcessor = new XMLOutputter(null, new XMLOutputProcessor());
 assertNull(xmloWithProcessor.getFormat());
 }

 @Test
 public void testXMLOutputterConstructorWithNoArguments() {
 XMLOutputter xmloWithNoArguments = new XMLOutputter();
 assertNull(xmloWithNoArguments.getFormat());
 assertNull(xmloWithNoArguments.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format newFormat = new Format();
 xmlo.setFormat(newFormat);
 assertEquals(newFormat, xmlo.getFormat());
 }

 @Test
 public void testGetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor newProcessor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(newProcessor);
 assertEquals(newProcessor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor newProcessor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(newProcessor);
 assertEquals(newProcessor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document document = JDOMFactory.createRootElement("root");
 xmlo.output(document, writer);
 String expectedXML = "<root/>";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocType("root", null, null);
 xmlo.output(docType, writer);
 String expectedProlog = "<!DOCTYPE root [";
 String expectedEpilog = "]>";
 String expectedXML = expectedProlog + "\n" + "</root>" + expectedEpilog;
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = JDOMFactory.createRootElement("root").addContent(JDOMFactory.createElement("child"));
 xmlo.output(element, writer);
 String expectedXML = "<root><child/></root>";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = JDOMFactory.createRootElement("root");
 element.setText("Hello World!");
 xmlo.outputElementContent(element, writer);
 String expectedXML = "Hello World!";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(JDOMFactory.createRootElement("item1"));
 list.add(JDOMFactory.createRootElement("item2"));
 xmlo.output(list, writer);
 String expectedXML = "<root><item1/><item2/></root>";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello CDATA]]>");
 xmlo.output(cdata, writer);
 String expectedXML = "<![CDATA[Hello CDATA]]>";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Hello Text");
 xmlo.output(text, writer);
 String expectedXML = "Hello Text";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("Hello Comment");
 xmlo.output(comment, writer);
 String expectedXML = "<!--Hello Comment-->";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
 xmlo.output(pi, writer);
 String expectedXML = pi.getTarget() + "\n" + pi.getData();
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entity = new EntityRef("#comment");
 xmlo.output(entity, writer);
 String expectedXML = "&comment;";
 assertEquals(expectedXML, writer.toString());
 }

 @Test
 public void testOutputStringDocument() {
 String documentAsString = xmlo.outputString(JDOMFactory.createRootElement("root"));
 String expectedXML = "<root/>";
 assertEquals(expectedXML, documentAsString);
 }

 @Test
 public void testOutputStringDocType() {
 String docTypeAsString = xmlo.outputString(new DocType("root", null, null));
 String expectedProlog = "<!DOCTYPE root [";
 String expectedEpilog = "]>";
 String expectedXML = expectedProlog + "\n" + "</root>" + expectedEpilog;
 assertEquals(expectedXML, docTypeAsString);
 }

 @Test
 public void testOutputStringElement() {
 Element element = JDOMFactory.createRootElement("root").addContent(JDOMFactory.createElement("child"));
 String elementAsString = xmlo.outputString(element);
 String expectedXML = "<root><child/></root>";
 assertEquals(expectedXML, elementAsString);
 }

 @Test
 public void testOutputStringList() {
 List<Element> list = new ArrayList<>();
 list.add(JDOMFactory.createRootElement("item1"));
 list.add(JDOMFactory.createRootElement("item2"));
 String listAsString = xmlo.outputString(list);
 String expectedXML = "<root><item1/><item2/></root>";
 assertEquals(expectedXML, listAsString);
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("<![CDATA[Hello CDATA]]>");
 String cdataAsString = xmlo.outputString(cdata);
 String expectedXML = "<![CDATA[Hello CDATA]]>";
 assertEquals(expectedXML, cdataAsString);
 }

 @Test
 public void testOutputStringText() {
 Text text = new Text("Hello Text");
 String textAsString = xmlo.outputString(text);
 String expectedXML = "Hello Text";
 assertEquals(expectedXML, textAsString);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("Hello Comment");
 String commentAsString = xmlo.outputString(comment);
 String expectedXML = "<!--Hello Comment-->";
 assertEquals(expectedXML, commentAsString);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
 String piAsString = xmlo.outputString(pi);
 String expectedXML = pi.getTarget() + "\n" + pi.getData();
 assertEquals(expectedXML, piAsString);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entity = new EntityRef("#comment");
 String entityAsString = xmlo.outputString(entity);
 String expectedXML = "&comment;";
 assertEquals(expectedXML, entityAsString);
 }

 @Test
 public void testOutputStringElementContent() {
 Element element = JDOMFactory.createRootElement("root");
 element.setText("Hello World!");
 String elementContentAsString = xmlo.outputElementContentString(element);
 String expectedXML = "Hello World!";
 assertEquals(expectedXML, elementContentAsString);
 }

 @Test(expected = IOException.class)
 public void testIOExceptionThrownWhenOutputtingToInvalidOutputStream() {
 OutputStream invalidOutputStream = null;
 xmlo.output(JDOMFactory.createRootElement("root"), invalidOutputStream);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String input = "&amp;<&>\"'";
 String expectedOutput = "&amp;amp; &lt; &gt; &quot; &apos;";
 String output = xmlo.escapeAttributeEntities(input, format);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "&amp;<&>\"'";
 String expectedOutput = "&amp;lt;lt;&gt;gt;&quot;quot;&apos;apos;";
 String output = xmlo.escapeElementEntities(input, format);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testToString() {
 XMLOutputter xmlo = new XMLOutputter();
 String expectedToString = "XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = \r\n]";
 String toString = xmlo.toString();
 assertEquals(expectedToString, toString);
 }
}