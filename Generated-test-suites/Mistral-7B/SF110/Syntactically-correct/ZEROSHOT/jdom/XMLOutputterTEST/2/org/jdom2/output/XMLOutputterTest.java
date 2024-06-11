package org.jdom2.output;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Format format;
 private XMLOutputProcessor processor;

 @Before
 public void setup() {
 format = new Format();
 processor = new XMLOutputProcessor();
 xmlo = new XMLOutputter(format, processor);
 }

 @Test
 public void testXMLOutputterDefaultConstructor() {
 XMLOutputter xmloDefault = new XMLOutputter();
 Assert.assertNull(xmloDefault.getFormat());
 Assert.assertNull(xmloDefault.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterFormatAndProcessorConstructor() {
 XMLOutputter xmloFormatAndProcessor = new XMLOutputter(format, processor);
 Assert.assertEquals(format, xmloFormatAndProcessor.getFormat());
 Assert.assertEquals(processor, xmloFormatAndProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterCopyConstructor() {
 XMLOutputter xmloCopy = new XMLOutputter(xmlo);
 Assert.assertEquals(format, xmloCopy.getFormat());
 Assert.assertEquals(processor, xmloCopy.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterFormatConstructor() {
 XMLOutputter xmloFormat = new XMLOutputter(format);
 Assert.assertEquals(format, xmloFormat.getFormat());
 Assert.assertNull(xmloFormat.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterProcessorConstructor() {
 XMLOutputter xmloProcessor = new XMLOutputter(processor);
 Assert.assertNull(xmloProcessor.getFormat());
 Assert.assertEquals(processor, xmloProcessor.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 Format newFormat = new Format();
 xmlo.setFormat(newFormat);
 Assert.assertEquals(newFormat, xmlo.getFormat());
 }

 @Test
 public void testGetFormat() {
 Assert.assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor newProcessor = new XMLOutputProcessor();
 xmlo.setXMLOutputProcessor(newProcessor);
 Assert.assertEquals(newProcessor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testGetXMLOutputProcessor() {
 Assert.assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws Exception {
 Document doc = JDOMFactory.createRootElement("Root");
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(doc, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("<Root></Root>", xml);
 }

 @Test
 public void testOutputDocType() throws Exception {
 DocType doctype = JDOMFactory.createDocType("myDocType", null, null, null);
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(doctype, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("<!DOCTYPE myDocType [ ]>", xml);
 }

 @Test
 public void testOutputElement() throws Exception {
 Element element = JDOMFactory.createRootElement("Root").addContent(JDOMFactory.createElement("Child"));
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(element, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("<Root><Child></Child></Root>", xml);
 }

 @Test
 public void testOutputElementContent() throws Exception {
 Element element = JDOMFactory.createRootElement("Root")
 .addContent(JDOMFactory.createText("Hello World"));
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.outputElementContent(element, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("Hello World", xml);
 }

 @Test
 public void testOutputList() throws Exception {
 List<Content> list = new ArrayList<>();
 list.add(JDOMFactory.createText("Hello"));
 list.add(JDOMFactory.createText("World"));
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(list, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("<text>Hello</text><text>World</text>", xml);
 }

 @Test
 public void testOutputCDATA() throws Exception {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(cdata, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("<![CDATA[Hello World]]>", xml);
 }

 @Test
 public void testOutputText() throws Exception {
 Text text = new Text("Hello World");
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(text, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("Hello World", xml);
 }

 @Test
 public void testOutputComment() throws Exception {
 Comment comment = new Comment("This is a comment");
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(comment, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("<!--This is a comment-->", xml);
 }

 @Test
 public void testOutputProcessingInstruction() throws Exception {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(pi, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("<?xml version=\"1.0\"?>", xml);
 }

 @Test
 public void testOutputEntityRef() throws Exception {
 EntityRef entityRef = new EntityRef("entity1");
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 xmlo.output(entityRef, outStream);
 String xml = outStream.toString();
 Assert.assertEquals("&entity1;", xml);
 }

 @Test
 public void testOutputStringDocument() {
 String xml = xmlo.outputString(JDOMFactory.createRootElement("Root"));
 Assert.assertEquals("<Root></Root>", xml);
 }

 @Test
 public void testOutputStringDocType() {
 String xml = xmlo.outputString(JDOMFactory.createDocType("myDocType", null, null, null));
 Assert.assertEquals("<!DOCTYPE myDocType [ ]>", xml);
 }

 @Test
 public void testOutputStringElement() {
 String xml = xmlo.outputString(JDOMFactory.createRootElement("Root")
 .addContent(JDOMFactory.createElement("Child")));
 Assert.assertEquals("<Root><Child></Child></Root>", xml);
 }

 @Test
 public void testOutputStringElementContent() {
 String xml = xmlo.outputString(JDOMFactory.createRootElement("Root")
 .addContent(JDOMFactory.createText("Hello World")));
 Assert.assertEquals("<Root><text>Hello World</text></Root>", xml);
 }

 @Test
 public void testOutputStringList() {
 List<Content> list = new ArrayList<>();
 list.add(JDOMFactory.createText("Hello"));
 list.add(JDOMFactory.createText("World"));
 String xml = xmlo.outputString(list);
 Assert.assertEquals("<text>Hello</text><text>World</text>", xml);
 }

 @Test
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 String xml = xmlo.outputString(cdata);
 Assert.assertEquals("<![CDATA[Hello World]]>", xml);
 }

 @Test
 public void testOutputStringText() {
 Text text = new Text("Hello World");
 String xml = xmlo.outputString(text);
 Assert.assertEquals("Hello World", xml);
 }

 @Test
 public void testOutputStringComment() {
 Comment comment = new Comment("This is a comment");
 String xml = xmlo.outputString(comment);
 Assert.assertEquals("<!--This is a comment-->", xml);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
 String xml = xmlo.outputString(pi);
 Assert.assertEquals("<?xml version=\"1.0\"?>", xml);
 }

 @Test
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("entity1");
 String xml = xmlo.outputString(entityRef);
 Assert.assertEquals("&entity1;", xml);
 }

 @Test
 public void testOutputStringElementContentString() {
 Element element = JDOMFactory.createRootElement("Root")
 .addContent(JDOMFactory.createText("Hello World"));
 String xml = xmlo.outputElementContentString(element);
 Assert.assertEquals("Hello World", xml);
 }
}