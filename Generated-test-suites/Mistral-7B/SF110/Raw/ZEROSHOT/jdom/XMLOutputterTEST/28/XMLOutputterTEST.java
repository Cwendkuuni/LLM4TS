package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private static final Format FORMAT = new Format();
 private static final XMLOutputProcessor DEFAULT_PROCESSOR = new DefaultXMLProcessor();

 @Test
 public void testXMLOutputterConstructor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputter cloneXmlo = xmlo.clone();
 XMLOutputter xmloWithFormat = new XMLOutputter(FORMAT);
 XMLOutputter xmloWithProcessor = new XMLOutputter(DEFAULT_PROCESSOR);
 XMLOutputter xmloWithFormatAndProcessor = new XMLOutputter(FORMAT, DEFAULT_PROCESSOR);
 XMLOutputter xmloWithNullParams = new XMLOutputter();
 XMLOutputter xmloWithOnlyFormat = new XMLOutputter(new Format());
 XMLOutputter xmloWithOnlyProcessor = new XMLOutputter(new XMLOutputProcessor());
 XMLOutputter xmloWithFormatAndNullProcessor = new XMLOutputter(FORMAT, null);

 assertEquals(xmlo, cloneXmlo);
 assertNotSame(xmlo, xmloWithFormat);
 assertNotSame(xmlo, xmloWithProcessor);
 assertNotSame(xmlo, xmloWithFormatAndProcessor);
 assertNotSame(xmlo, xmloWithNullParams);
 assertNotSame(xmlo, xmloWithOnlyFormat);
 assertNotSame(xmlo, xmloWithOnlyProcessor);
 assertNotSame(xmlo, xmloWithFormatAndNullProcessor);
 }

 @Test
 public void testXMLOutputterSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format newFormat = new Format();
 xmlo.setFormat(newFormat);
 assertEquals(newFormat, xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterGetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 assertEquals(FORMAT, xmlo.getFormat());
 }

 @Test
 public void testXMLOutputterSetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 XMLOutputProcessor newProcessor = new DefaultXMLProcessor();
 xmlo.setXMLOutputProcessor(newProcessor);
 assertEquals(newProcessor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterGetXMLOutputProcessor() {
 XMLOutputter xmlo = new XMLOutputter();
 assertEquals(DEFAULT_PROCESSOR, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterOutputDocument() throws IOException {
 Document document = new JDOMFactory().newDocument();
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(document, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<document/>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputDocType() throws IOException {
 DocType doctype = new JDOMFactory().newDocType("test", null, null, null, null);
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(doctype, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<!DOCTYPE test [ ]>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputElement() throws IOException {
 Element root = new Element("root");
 Document document = new JDOMFactory().newDocument(root);
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(root, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<root/>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputList() throws IOException {
 Element root = new Element("root");
 Document document = new JDOMFactory().newDocument(root);
 Element childElement1 = new Element("child1");
 Element childElement2 = new Element("child2");
 root.addContent(childElement1);
 root.addContent(childElement2);
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(new ArrayList<>(Arrays.asList(childElement1, childElement2)), byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<root><child1/><child2/></root>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("testCDATA");
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(cdata, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<![CDATA[testCDATA]]>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputText() throws IOException {
 Text text = new Text("testText");
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(text, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<text>testText</text>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputComment() throws IOException {
 Comment comment = new Comment("testComment");
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(comment, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<!--testComment-->";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("target", "data");
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(pi, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<xml-pi:processing-instruction name=\"target\" data=\"data\"/>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entity");
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.output(entityRef, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<!ENTITY entity SYSTEM \"\">";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testXMLOutputterOutputElementContent() throws IOException {
 Element root = new Element("root");
 Element childElement = new Element("child");
 Text textChildContent = new Text("testContent");
 childElement.addContent(textChildContent);
 root.addContent(childElement);
 XMLOutputter xmlo = new XMLOutputter();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 xmlo.outputElementContent(childElement, byteArrayOutputStream);
 String xmlString = byteArrayOutputStream.toString();
 String expectedXml = "<child><text>testContent</text></child>";
 assertEquals(expectedXml, xmlString);
 }
}