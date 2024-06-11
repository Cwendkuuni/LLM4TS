package org.jdom2.output;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 private XMLOutputter createXMLOutputter(Format format) {
 return new XMLOutputter(format);
 }

 private XMLOutputter createXMLOutputter(XMLOutputProcessor processor) {
 return new XMLOutputter(processor);
 }

 @Test
 public void testXMLOutputter() {
 XMLOutputter xmlo = createXMLOutputter();
 Assert.assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterWithFormat() {
 Format format = new Format();
 XMLOutputter xmlo = createXMLOutputter(format);
 Assert.assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterWithXMLOutputProcessor() {
 XMLOutputProcessor processor = new DefaultXMLOutputProcessor();
 XMLOutputter xmlo = createXMLOutputter(processor);
 Assert.assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(doc, out);
 String actual = new String(out.toByteArray());
 String expected = "<root/>"; // Replace with the expected XML representation of doc
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new JDOMFactory().newDocumentType("html", null, null, null);
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(docType, out);
 String actual = new String(out.toByteArray());
 String expected = "<!DOCTYPE html SYSTEM>" + System.lineSeparator();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new JDOMFactory().newElement("root", null);
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(element, out);
 String actual = new String(out.toByteArray());
 String expected = "<root/>"; // Replace with the expected XML representation of element
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Content> list = Collections.singletonList(new JDOMFactory().newText("Hello World"));
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(list, out);
 String actual = new String(out.toByteArray());
 String expected = "<text xmlns='http://www.w3.org/1999/xhtml'>Hello World</text>"; // Replace with the expected XML representation of list
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new JDOMFactory().newCDATA("<![CDATA[Hello World]]>");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(cdata, out);
 String actual = new String(out.toByteArray());
 String expected = "<![CDATA[Hello World]]>"; // Replace with the expected XML representation of cdata
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new JDOMFactory().newText("Hello World");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(text, out);
 String actual = new String(out.toByteArray());
 String expected = "Hello World"; // Replace with the expected output of text
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new JDOMFactory().newComment("Hello World");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(comment, out);
 String actual = new String(out.toByteArray());
 String expected = "<!--Hello World-->"; // Replace with the expected output of comment
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new JDOMFactory().newProcessingInstruction("xml", "version='1.0'");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(pi, out);
 String actual = new String(out.toByteArray());
 String expected = "<?xml version='1.0'?>"; // Replace with the expected output of pi
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new JDOMFactory().newEntityRef("testEntity");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 xmlo.output(entityRef, out);
 String actual = new String(out.toByteArray());
 String expected = "&testEntity;"; // Replace with the expected output of entityRef
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringDocument() {
 Document doc = new JDOMFactory().newDocument();
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(doc);
 String expected = "<root/>"; // Replace with the expected XML representation of doc
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringDocType() {
 DocType docType = new JDOMFactory().newDocumentType("html", null, null, null);
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(docType);
 String expected = "<!DOCTYPE html SYSTEM>"; // Replace with the expected XML representation of docType
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringElement() {
 Element element = new JDOMFactory().newElement("root", null);
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(element);
 String expected = "<root/>"; // Replace with the expected XML representation of element
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringList() {
 List<Content> list = Collections.singletonList(new JDOMFactory().newText("Hello World"));
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(list);
 String expected = "<text xmlns='http://www.w3.org/1999/xhtml'>Hello World</text>"; // Replace with the expected XML representation of list
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new JDOMFactory().newCDATA("<![CDATA[Hello World]]>");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(cdata);
 String expected = "<![CDATA[Hello World]]>"; // Replace with the expected XML representation of cdata
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringText() {
 Text text = new JDOMFactory().newText("Hello World");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(text);
 String expected = "Hello World"; // Replace with the expected output of text
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new JDOMFactory().newComment("Hello World");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(comment);
 String expected = "<!--Hello World-->"; // Replace with the expected output of comment
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new JDOMFactory().newProcessingInstruction("xml", "version='1.0'");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(pi);
 String expected = "<?xml version='1.0'?>"; // Replace with the expected output of pi
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new JDOMFactory().newEntityRef("testEntity");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(entityRef);
 String expected = "&testEntity;"; // Replace with the expected output of entityRef
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContentString() {
 Element element = new JDOMFactory().newElement("root", null);
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputElementContentString(element);
 String expected = ""; // Replace with the expected output of element content
 Assert.assertEquals(expected, actual);
 }
}