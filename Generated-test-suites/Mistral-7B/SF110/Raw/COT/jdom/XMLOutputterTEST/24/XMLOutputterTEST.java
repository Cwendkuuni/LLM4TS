package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private static final String DEFAULT_XML = "<root><element>Text</element></root>";
 private static final String FORMATTED_XML = "<root>\n<element>Text</element>\n</root>";

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter outputter = new XMLOutputter(new Format());
 Assert.assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter outputter = new XMLOutputter(new Format(), null);
 Assert.assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter outputter = new XMLOutputter(null, new XMLOutputProcessor());
 Assert.assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter outputter = new XMLOutputter(new Format(), new XMLOutputProcessor());
 Assert.assertNotNull(outputter);
 }

 @Test
 public void testXMLOutputterClone() throws CloneNotSupportedException {
 XMLOutputter original = new XMLOutputter(new Format());
 XMLOutputter cloned = original.clone();
 Assert.assertNotSame(original, cloned);
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document document = JDOMFactory.createRootElement("root");
 document.addContent(JDOMFactory.createElement("element", "Text"));
 XMLOutputter outputter = new XMLOutputter();
 String expected = FORMATTED_XML;
 String actual = outputter.outputString(document);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocType("root", null, null, null);
 XMLOutputter outputter = new XMLOutputter();
 String expected = "<!DOCTYPE root SYSTEM \"\">\n";
 String actual = outputter.outputString(docType);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = JDOMFactory.createRootElement("root");
 element.addContent(JDOMFactory.createElement("element", "Text"));
 XMLOutputter outputter = new XMLOutputter();
 String expected = "<root>\n<element>Text</element>\n</root>";
 String actual = outputter.outputString(element);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputList() throws IOException {
 Element root = JDOMFactory.createRootElement("root");
 List<Element> elements = new ArrayList<>();
 elements.add(JDOMFactory.createElement("element1", "Text1"));
 elements.add(JDOMFactory.createElement("element2", "Text2"));
 root.addContent(elements);
 XMLOutputter outputter = new XMLOutputter();
 String expected = "<root>\n<element1>Text1</element1>\n<element2>Text2</element2>\n</root>";
 String actual = outputter.outputString(root);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Text]]>");
 XMLOutputter outputter = new XMLOutputter();
 String expected = "<![CDATA[Text]]>";
 String actual = outputter.outputString(cdata);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Text");
 XMLOutputter outputter = new XMLOutputter();
 String expected = "Text";
 String actual = outputter.outputString(text);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("Text");
 XMLOutputter outputter = new XMLOutputter();
 String expected = "<!--Text-->";
 String actual = outputter.outputString(comment);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 XMLOutputter outputter = new XMLOutputter();
 String expected = "<?xml version=\"1.0\"?>";
 String actual = outputter.outputString(pi);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity");
 XMLOutputter outputter = new XMLOutputter();
 String expected = "&entity;";
 String actual = outputter.outputString(entityRef);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = JDOMFactory.createRootElement("root");
 Text text = new Text("Text");
 element.addContent(text);
 XMLOutputter outputter = new XMLOutputter();
 String expected = "<root>\n<element>\nText\n</element>\n</root>";
 String actual = outputter.outputElementContentString(element);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullDocument() {
 XMLOutputter outputter = new XMLOutputter();
 outputter.output(null, new StringWriter());
 }

 @Test(expected = IOException.class)
 public void testOutputDocumentWithNullWriter() {
 XMLOutputter outputter = new XMLOutputter();
 outputter.output(new Document(), null);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testMakeWriterWithUnsupportedEncoding() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 Format format = new Format();
 format.setEncoding("UTF-16BE"); // Unsupported encoding
 XMLOutputter.makeWriter(outputStream, format);
 }
}