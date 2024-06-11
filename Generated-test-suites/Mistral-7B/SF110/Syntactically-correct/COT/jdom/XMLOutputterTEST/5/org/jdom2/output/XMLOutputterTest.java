package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 private XMLOutputter createXMLOutputter(Format format) {
 return new XMLOutputter(format);
 }

 private XMLOutputter createXMLOutputter(XMLOutputProcessor processor) {
 return new XMLOutputter(null, processor);
 }

 @Test
 public void testXMLOutputterConstructor() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNull(xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());

 XMLOutputter xmloWithFormat = new XMLOutputter(new Format());
 assertNotNull(xmloWithFormat.getFormat());
 assertNull(xmloWithFormat.getXMLOutputProcessor());

 XMLOutputter xmloWithProcessor = new XMLOutputter(new XMLOutputProcessor());
 assertNull(xmloWithProcessor.getFormat());
 assertNotNull(xmloWithProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNull(xmlo.getFormat());

 Format format = new Format();
 xmlo.setFormat(format);
 assertSame(format, xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNull(xmlo.getXMLOutputProcessor());

 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 assertSame(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = JDOMHelper.createDocument();
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(doc, out);
 String xml = out.toString();
 assertTrue(xml.length() > 0);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = new DefaultDocument().getRootElement().getDocument().getDocType();
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(docType, out);
 String xml = out.toString();
 assertTrue(xml.length() > 0);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = JDOMHelper.createElement("test");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(element, out);
 String xml = out.toString();
 assertTrue(xml.length() > 0);
 }

 @Test
 public void testOutputStringDocument() {
 Document doc = JDOMHelper.createDocument();
 XMLOutputter xmlo = new XMLOutputter();
 String xml = xmlo.outputString(doc);
 assertTrue(xml.length() > 0);
 }

 @Test
 public void testOutputStringDocType() {
 DocType docType = new DefaultDocument().getRootElement().getDocument().getDocType();
 XMLOutputter xmlo = new XMLOutputter();
 String xml = xmlo.outputString(docType);
 assertTrue(xml.length() > 0);
 }

 @Test
 public void testOutputStringElement() {
 Element element = JDOMHelper.createElement("test");
 XMLOutputter xmlo = new XMLOutputter();
 String xml = xmlo.outputString(element);
 assertTrue(xml.length() > 0);
 }

 @Test
 public void testOutputStringEmptyList() {
 List<Content> list = new ArrayList<>();
 XMLOutputter xmlo = new XMLOutputter();
 String xml = xmlo.outputString(list);
 assertTrue(xml.length() == 0);
 }

 @Test
 public void testOutputStringCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(cdata, out);
 String xml = out.toString();
 assertEquals("<![CDATA[Hello World]]>", xml);
 }

 @Test
 public void testOutputStringText() throws IOException {
 Text text = new Text("Hello World");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(text, out);
 String xml = out.toString();
 assertEquals("Hello World", xml);
 }

 @Test
 public void testOutputStringComment() throws IOException {
 Comment comment = new Comment("Hello World");
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(comment, out);
 String xml = out.toString();
 assertEquals("<!--Hello World-->", xml);
 }

 @Test(expected = IOException.class)
 public void testOutputStringNullElement() {
 XMLOutputter xmlo = new XMLOutputter();
 String xml = xmlo.outputString(null);
 fail("Should throw an IOException.");
 }

 @Test
 public void testOutputExceptionHandler() throws IOException {
 OutputStream out = new ByteArrayOutputStream();
 class TestIOException extends IOException {
 }
 XMLOutputter xmlo = new XMLOutputter() {
 @Override
 public void output(Element element, Writer out) throws IOException {
 throw new TestIOException();
 }
 };
 try {
 xmlo.output(new Element("test"), out);
 fail("Should throw an IOException.");
 } catch (TestIOException e) {
 // expected
 }
 String xml = out.toString();
 assertTrue(xml.length() == 0);
}

 @Test
 public void testEscapeAttributeEntities() {
 String input = "<test attr='<tag> & & &quot; & ' & &amp; & &gt;' />";
 String expectedOutput = "&lt;test attr=&apos;<tag>&apos; &amp; &quot; &amp;amp; &quot; &amp; &gt;&apos; />";
 String output = XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(input, null);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeElementEntities() {
 String input = "<test>&lt;tag&gt;</test>";
 String expectedOutput = "&lt;test>&lt;tag&gt;&gt;</test>";
 String output = XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(input, null);
 assertEquals(expectedOutput, output);
 }
}