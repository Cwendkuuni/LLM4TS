package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = createXMLOutputter(new Format(), new XMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlo = createXMLOutputter(new Format());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlo = createXMLOutputter(new XMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithThat() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(xmlo1);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndThat() {
 XMLOutputter xmlo1 = new XMLOutputter(new Format());
 XMLOutputter xmlo2 = new XMLOutputter(xmlo1.getFormat());
 }

 @Test
 public void testSetFormat() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 Assert.assertSame(format, xmlo.getFormat());
 }

 @Test
 public void testGetFormat() {
 XMLOutputter xmlo = createXMLOutputter();
 Assert.assertNull(xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 Assert.assertSame(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputter xmlo = createXMLOutputter();
 Assert.assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document document = JDOMFactory.createDocument();
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(document, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "<document></document>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DocType("root", new Element[]{}, new Attribute[]{}, null);
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(docType, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "<!DOCTYPE root SYSTEM \"\">";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new Element("root");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(element, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "<root></root>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element element = new Element("root");
 element.addContent(new Text("Hello, World!"));
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.outputElementContent(element, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "Hello, World!";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputList() throws IOException {
 Element element = new Element("root");
 Text text1 = new Text("Text 1");
 Text text2 = new Text("Text 2");
 element.addContent(text1);
 element.addContent(text2);
 List<Content> list = new ArrayList<>(element.getChildren());
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(list, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "<root><text>Text 1</text><text>Text 2</text></root>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello, World!]]>");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(cdata, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "<![CDATA[Hello, World!]]>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new Text("Hello, World!");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(text, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "Hello, World!";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("This is a comment.");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(comment, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "<!--This is a comment.-->";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "<![CDATA[type=\"text/css\"; href=\"style.css\"]]>");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(pi, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "<xml-stylesheet type=\"text/css\" href=\"style.css\"/>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity1");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlo.output(entityRef, outputStream);
 String actual = new String(outputStream.toByteArray());
 String expected = "&entity1;";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringDocument() {
 Document document = JDOMFactory.createDocument();
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(document);
 String expected = "<document></document>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringDocType() {
 DocType docType = new DocType("root", new Element[]{}, new Attribute[]{}, null);
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(docType);
 String expected = "<!DOCTYPE root SYSTEM \"\">";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringElement() {
 Element element = new Element("root");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(element);
 String expected = "<root></root>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringList() {
 Element element = new Element("root");
 Text text1 = new Text("Text 1");
 Text text2 = new Text("Text 2");
 element.addContent(text1);
 element.addContent(text2);
 List<Content> list = new ArrayList<>(element.getChildren());
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(list);
 String expected = "<root><text>Text 1</text><text>Text 2</text></root>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello, World!]]>");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(cdata);
 String expected = "<![CDATA[Hello, World!]]>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringText() throws IOException {
 Text text = new Text("Hello, World!");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(text);
 String expected = "Hello, World!";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("This is a comment.");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(comment);
 String expected = "<!--This is a comment.-->";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "<![CDATA[type=\"text/css\"; href=\"style.css\"]]>");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(pi);
 String expected = "<xml-stylesheet type=\"text/css\" href=\"style.css\"/>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("entity1");
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputString(entityRef);
 String expected = "&entity1;";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testOutputElementContentString() {
 Element element = new Element("root");
 element.addContent(new Text("Hello, World!"));
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.outputElementContentString(element);
 String expected = "Hello, World!";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String str = "<hello>&world;</hello>";
 XMLOutputter xmlo = new XMLOutputter();
 String actual = xmlo.escapeAttributeEntities(str);
 String expected = "&lt;hello&gt;&amp;world;&lt;/hello&gt;";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEscapeElementEntities() {
 String str = "<hello>&world;</hello>";
 XMLOutputter xmlo = new XMLOutputter();
 String actual = xmlo.escapeElementEntities(str);
 String expected = "<hello>&lt;world&gt;</hello>";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = xmlo1.clone();
 Assert.assertNotSame(xmlo1, xmlo2);
 Assert.assertNotNull(xmlo2.getFormat());
 }

 @Test
 public void testToString() {
 XMLOutputter xmlo = createXMLOutputter();
 String actual = xmlo.toString();
 String expected = "XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = \r\n]";
 Assert.assertEquals(expected, actual);
 }
}