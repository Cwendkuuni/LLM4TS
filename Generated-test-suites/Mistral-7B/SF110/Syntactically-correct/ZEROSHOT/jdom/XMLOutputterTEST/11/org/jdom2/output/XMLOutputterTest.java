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
 public void testXMLOutputterWithDefaultConstructor() {
 XMLOutputter xmlo = new XMLOutputter();
 assertEquals(Format.getRawFormat(), xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterWithFormatAndProcessorNull() {
 XMLOutputter xmlo = new XMLOutputter(null, null);
 assertEquals(Format.getRawFormat(), xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterWithFormatAndProcessor() {
 Format format = new Format();
 XMLOutputProcessor processor = new DefaultXMLProcessor();
 XMLOutputter xmlo = new XMLOutputter(format, processor);
 assertEquals(format, xmlo.getFormat());
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterWithFormat() {
 Format format = new Format();
 XMLOutputter xmlo = new XMLOutputter(format);
 assertEquals(format, xmlo.getFormat());
 assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterWithProcessor() {
 XMLOutputProcessor processor = new DefaultXMLProcessor();
 XMLOutputter xmlo = new XMLOutputter(processor);
 assertEquals(Format.getRawFormat(), xmlo.getFormat());
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor processor = new DefaultXMLProcessor();
 xmlo.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(doc, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("<document/>", xml);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType doctype = new JDOMFactory().newDocType("root", null, null);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(doctype, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("<!DOCTYPE root [ ]>", xml);
 }

 @Test
 public void testOutputElement() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(root, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("<root/>", xml);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 Text text = new Text("Hello World!");
 root.addContent(text);
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.outputElementContent(root, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("Hello World!", xml);
 }

 @Test
 public void testOutputList() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 Text text1 = new Text("Text 1");
 Text text2 = new Text("Text 2");
 List<Content> list = new ArrayList<>();
 list.add(text1);
 list.add(text2);
 root.addContent(list);
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(list, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("<root><text>Text 1</text><text>Text 2</text></root>", xml);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 CDATA cdata = new CDATA("Hello World!");
 root.addContent(cdata);
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(cdata, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("<![CDATA[Hello World!]]>", xml);
 }

 @Test
 public void testOutputText() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 Text text = new Text("Hello World!");
 root.addContent(text);
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(text, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("Hello World!", xml);
 }

 @Test
 public void testOutputComment() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 Comment comment = new Comment("This is a comment.");
 root.addContent(comment);
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(comment, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("<!--This is a comment.-->", xml);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 root.addContent(pi);
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(pi, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("<?xml version=\"1.0\"?>", xml);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 EntityRef entityRef = new EntityRef("entity1");
 root.addContent(entityRef);
 doc.setRootElement(root);
 OutputStream out = new ByteArrayOutputStream();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.output(entityRef, out);
 String xml = new String((byte[]) out.toByteArray(), "UTF-8");
 assertEquals("&entity1;", xml);
 }

 @Test
 public void testOutputElementContentString() throws IOException {
 Document doc = new JDOMFactory().newDocument();
 Element root = new Element("root");
 Text text = new Text("Hello World!");
 root.addContent(text);
 doc.setRootElement(root);
 String xml = new XMLOutputter().outputElementContentString(root);
 assertEquals("Hello World!", xml);
 }
}