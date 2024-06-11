package org.jdom2.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private Document createTestDocument() {
 return new JDOMParser().parse(new File("src/test/resources/test.xml"));;
 }

 @Test
 public void testXMLOutputterConstructionWithFormat() {
 XMLOutputter outputter = new XMLOutputter(new Format());
 }

 @Test
 public void testXMLOutputterConstructionWithFormatAndProcessor() {
 XMLOutputter outputter = new XMLOutputter(new Format(), new DefaultXMLOutputterProcessor());
 }

 @Test
 public void testXMLOutputterConstructionWithProcessor() {
 XMLOutputter outputter = new XMLOutputter(new XMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructionWithFormatAndThat() {
 XMLOutputter outputter = new XMLOutputter(new Format(), null);
 }

 @Test
 public void testXMLOutputterConstructionWithThat() {
 XMLOutputter outputter = new XMLOutputter(null, null);
 }

 @Test
 public void testXMLOutputterConstructionWithFormatAndThatAndProcessor() {
 XMLOutputter outputter = new XMLOutputter(new Format(), null, new DefaultXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter outputter = new XMLOutputter();
 Format format = new Format();
 outputter.setFormat(format);
 assertEquals(format, outputter.getFormat());
 }

 @Test
 public void testGetFormat() {
 XMLOutputter outputter = new XMLOutputter();
 assertEquals(XMLOutputter.DEFAULTPROCESSOR.getFormat(), outputter.getFormat());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputter outputter = new XMLOutputter();
 assertEquals(XMLOutputter.DEFAULTPROCESSOR, outputter.getXMLOutputProcessor());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter outputter = new XMLOutputter();
 XMLOutputProcessor processor = new DefaultXMLOutputProcessor();
 outputter.setXMLOutputProcessor(processor);
 assertEquals(processor, outputter.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocumentToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Document document = createTestDocument();
 assertThrows(IOException.class, () -> outputter.output(document, outputStream));
 }

 @Test
 public void testOutputDocumentToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Document document = createTestDocument();
 outputter.output(document, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("<root>\n<element attr=\"value\">Value</element>\n<element>\n<subElement>\n<subElement2/>\n</subElement>\n</element>\n</root>", result);
 }

 @Test
 public void testOutputDocTypeToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 DocType doctype = new DefaultDocument().getRootElement().getDocument().getDoctype();
 assertThrows(IOException.class, () -> outputter.output(doctype, outputStream));
 }

 @Test
 public void testOutputDocTypeToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 DocType doctype = new DefaultDocument().getRootElement().getDocument().getDoctype();
 outputter.output(doctype, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("<!DOCTYPE root []\n", result);
 }

 @Test
 public void testOutputElementToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Element element = new Element("element");
 assertThrows(IOException.class, () -> outputter.output(element, outputStream));
 }

 @Test
 public void testOutputElementToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Element element = new Element("element");
 outputter.output(element, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("<element/>\n", result);
 }

 @Test
 public void testOutputElementContentToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Element element = new Element("element");
 assertThrows(IOException.class, () -> outputter.outputElementContent(element, outputStream));
 }

 @Test
 public void testOutputElementContentToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Element element = new Element("element").addContent(new Text("Value"));
 outputter.outputElementContent(element, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("Value\n", result);
 }

 @Test
 public void testOutputListToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 List<Content> list = new ArrayList<>();
 list.add(new Text("Value"));
 assertThrows(IOException.class, () -> outputter.output(list, outputStream));
 }

 @Test
 public void testOutputListToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 List<Content> list = new ArrayList<>();
 list.add(new Text("Value"));
 outputter.output(list, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("<text xmlns='http://www.w3.org/1999/xhtml'/>\n<text xmlns='http://www.w3.org/1999/xhtml'/>\n", result);
 }

 @Test
 public void testOutputCDATAToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 CDATA cdata = new CDATA("<![CDATA[Value]]>");
 assertThrows(IOException.class, () -> outputter.output(cdata, outputStream));
 }

 @Test
 public void testOutputCDATAToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 CDATA cdata = new CDATA("<![CDATA[Value]]>");
 outputter.output(cdata, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("<![CDATA[Value]]>\n", result);
 }

 @Test
 public void testOutputTextToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Text text = new Text("Value");
 assertThrows(IOException.class, () -> outputter.output(text, outputStream));
 }

 @Test
 public void testOutputTextToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Text text = new Text("Value");
 outputter.output(text, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("Value\n", result);
 }

 @Test
 public void testOutputCommentToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Comment comment = new Comment("Value");
 assertThrows(IOException.class, () -> outputter.output(comment, outputStream));
 }

 @Test
 public void testOutputCommentToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 Comment comment = new Comment("Value");
 outputter.output(comment, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("<!-\nValue\n->\n", result);
 }

 @Test
 public void testOutputProcessingInstructionToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 ProcessingInstruction pi = new ProcessingInstruction("target", "data");
 assertThrows(IOException.class, () -> outputter.output(pi, outputStream));
 }

 @Test
 public void testOutputProcessingInstructionToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 ProcessingInstruction pi = new ProcessingInstruction("target", "data");
 outputter.output(pi, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("<?target data?>\n", result);
 }

 @Test
 public void testOutputEntityRefToOutputStreamThrowsIOException() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 EntityRef entityRef = new EntityRef("entity1");
 assertThrows(IOException.class, () -> outputter.output(entityRef, outputStream));
 }

 @Test
 public void testOutputEntityRefToOutputStream() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 OutputStream outputStream = new ByteArrayOutputStream();
 EntityRef entityRef = new EntityRef("entity1");
 outputter.output(entityRef, outputStream);
 String result = new String(outputStream.toByteArray());
 assertEquals("&entity1;\n", result);
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 XMLOutputter outputter = new XMLOutputter();
 String result = outputter.outputElementContentString(new Element("element").addContent(new Text("Value")));
 assertEquals("Value\n", result);
 }

 @Test
 public void testOutputStringDocument() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(createTestDocument());
 assertEquals("<root>\n<element attr=\"value\">Value</element>\n<element>\n<subElement>\n<subElement2/>\n</subElement>\n</element>\n</root>", xml);
 }

 @Test
 public void testOutputStringDocType() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new DefaultDocument().getRootElement().getDocument().getDoctype());
 assertEquals("<!DOCTYPE root []\n", xml);
 }

 @Test
 public void testOutputStringElement() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new Element("element"));
 assertEquals("<element/>\n", xml);
 }

 @Test
 public void testOutputStringElementWithContent() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new Element("element").addContent(new Text("Value")));
 assertEquals("<element>\n<text xmlns='http://www.w3.org/1999/xhtml'/>\n</element>\n", xml);
 }

 @Test
 public void testOutputStringList() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new ArrayList<>());
 assertEquals("", xml);
 }

 @Test
 public void testOutputStringListWithContent() {
 XMLOutputter outputter = new XMLOutputter();
 List<Content> list = new ArrayList<>();
 list.add(new Text("Value1"));
 list.add(new Text("Value2"));
 String xml = outputter.outputString(list);
 assertEquals("<text xmlns='http://www.w3.org/1999/xhtml'/>\n<text xmlns='http://www.w3.org/1999/xhtml'/>\n", xml);
 }

 @Test
 public void testOutputStringCDATA() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new CDATA("<![CDATA[Value]]>"));
 assertEquals("<![CDATA[Value]]>\n", xml);
 }

 @Test
 public void testOutputStringText() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new Text("Value"));
 assertEquals("Value\n", xml);
 }

 @Test
 public void testOutputStringComment() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new Comment("Value"));
 assertEquals("<!-\nValue\n->\n", xml);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new ProcessingInstruction("target", "data"));
 assertEquals("<?target data?>\n", xml);
 }

 @Test
 public void testOutputStringEntityRef() {
 XMLOutputter outputter = new XMLOutputter();
 String xml = outputter.outputString(new EntityRef("entity1"));
 assertEquals("&entity1;\n", xml);
 }

 @Test
 public void testEscapeAttributeEntities() {
 XMLOutputter outputter = new XMLOutputter();
 String input = "&<>\t'\"";
 String expectedOutput = "&amp;&lt;&gt;&\t;&apos;&quot;";
 String output = outputter.escapeAttributeEntities(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeElementEntities() {
 XMLOutputter outputter = new XMLOutputter();
 String input = "&<>\t'\"";
 String expectedOutput = "&amp;lt;lt;amp;gt;amp;gt;amp;gt;amp;gt;amp;gt;amp;gt;";
 String output = outputter.escapeElementEntities(input);
 assertEquals(expectedOutput, output);
 }
}