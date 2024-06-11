package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 @Test
 public void testConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new Format(), new XMLOutputProcessor());
 assertEquals(Format.getDefaultFormat(), xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testConstructorWithFormat() {
 XMLOutputter xmlo = new XMLOutputter(new Format());
 assertEquals(Format.getDefaultFormat(), xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testConstructorWithProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new XMLOutputProcessor());
 assertNull(xmlo.getFormat());
 assertNotNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testConstructorWithComponentsNull() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNull(xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testConstructorWithFormatAndProcessorNull() {
 XMLOutputter xmlo = new XMLOutputter(null, null);
 assertNull(xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testConstructorWithThat() {
 XMLOutputter existing = new XMLOutputter();
 XMLOutputter xmlo = new XMLOutputter(existing);
 assertEquals(existing.getFormat(), xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
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
 assertEquals(Format.getDefaultFormat(), xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new JDOMFactory().newDocument();
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(doc, out);
 String result = new String(out.toByteArray());
 assertEquals("<document/>", result);
 }

 @Test
 public void testOutputDocType() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 DocType docType = new JDOMFactory().newDocType("root", null);
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(docType, out);
 String result = new String(out.toByteArray());
 assertEquals("<!DOCTYPE root [ ]>", result);
 }

 @Test
 public void testOutputElement() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(root, out);
 String result = new String(out.toByteArray());
 assertEquals("<root/>", result);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Text("Text content"));
 OutputStream out = new ByteArrayOutputStream();
 xmlo.outputElementContent(root, out);
 String result = new String(out.toByteArray());
 assertEquals("Text content", result);
 }

 @Test
 public void testOutputList() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Text("Text content 1"));
 root.addContent(new Text("Text content 2"));
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(root.getContent(), out);
 String result = new String(out.toByteArray());
 assertEquals("<root><Text>Text content 1</Text><Text>Text content 2</Text></root>", result);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 CDATA cdata = new CDATAParse("Text content");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(cdata, out);
 String result = new String(out.toByteArray());
 assertEquals("<![CDATA[Text content]]>", result);
 }

 @Test
 public void testOutputText() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Text text = new Text("Text content");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(text, out);
 String result = new String(out.toByteArray());
 assertEquals("Text content", result);
 }

 @Test
 public void testOutputComment() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Comment comment = new Comment("Comment content");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(comment, out);
 String result = new String(out.toByteArray());
 assertEquals("<!--Comment content-->", result);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(pi, out);
 String result = new String(out.toByteArray());
 assertEquals("<xml version=\"1.0\"/>", result);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entityRef = new EntityRef("entity1");
 OutputStream out = new ByteArrayOutputStream();
 xmlo.output(entityRef, out);
 String result = new String(out.toByteArray());
 assertEquals("&entity1;", result);
 }

 @Test
 public void testOutputElementContentString() {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Text("Text content"));
 String result = xmlo.outputString(root);
 assertEquals("<root><Text>Text content</Text></root>", result);
 }

 @Test
 public void testOutputStringDocument() {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new JDOMFactory().newDocument();
 String result = xmlo.outputString(doc);
 assertEquals("<document/>", result);
 }

 @Test
 public void testOutputStringDocType() {
 XMLOutputter xmlo = createXMLOutputter();
 DocType docType = new JDOMFactory().newDocType("root", null);
 String result = xmlo.outputString(docType);
 assertEquals("<!DOCTYPE root [ ]>", result);
 }

 @Test
 public void testOutputStringElement() {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 String result = xmlo.outputString(root);
 assertEquals("<root/>", result);
 }

 @Test
 public void testOutputStringElementContent() {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Text("Text content"));
 String result = xmlo.outputString(root);
 assertEquals("<root><Text>Text content</Text></root>", result);
 }

 @Test
 public void testOutputStringList() {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Text("Text content 1"));
 root.addContent(new Text("Text content 2"));
 String result = xmlo.outputString(root.getContent());
 assertEquals("<root><Text>Text content 1</Text><Text>Text content 2</Text></root>", result);
 }

 @Test
 public void testOutputStringCDATA() {
 XMLOutputter xmlo = createXMLOutputter();
 CDATA cdata = new CDATAParse("Text content");
 String result = xmlo.outputString(cdata);
 assertEquals("<![CDATA[Text content]]>", result);
 }

 @Test
 public void testOutputStringText() {
 XMLOutputter xmlo = createXMLOutputter();
 Text text = new Text("Text content");
 String result = xmlo.outputString(text);
 assertEquals("Text content", result);
 }

 @Test
 public void testOutputStringComment() {
 XMLOutputter xmlo = createXMLOutputter();
 Comment comment = new Comment("Comment content");
 String result = xmlo.outputString(comment);
 assertEquals("<!--Comment content-->", result);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 XMLOutputter xmlo = createXMLOutputter();
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 String result = xmlo.outputString(pi);
 assertEquals("<xml version=\"1.0\"/>", result);
 }

 @Test
 public void testOutputStringEntityRef() {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entityRef = new EntityRef("entity1");
 String result = xmlo.outputString(entityRef);
 assertEquals("&entity1;", result);
 }

 @Test
 public void testEscapeAttributeEntities() {
 XMLOutputter xmlo = createXMLOutputter();
 String input = "&amp;<tag>&lt;special&gt;<tag/>&quot;";
 String result = xmlo.escapeAttributeEntities(input);
 assertEquals("&amp;&amp;<tag>&lt;special&gt;&gt;<tag/>&quot;", result);
 }

 @Test
 public void testEscapeElementEntities() {
 XMLOutputter xmlo = createXMLOutputter();
 String input = "&amp;<tag>&lt;special&gt;<tag/>&quot;";
 String result = xmlo.escapeElementEntities(input);
 assertEquals(Format.escapeText(Format.getDefaultFormat().getEscapeStrategy(), Format.getDefaultFormat().getLineSeparator(), input), result);
 }
}