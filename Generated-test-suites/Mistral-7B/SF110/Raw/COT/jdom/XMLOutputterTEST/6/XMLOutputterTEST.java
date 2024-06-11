package org.jdom2.output.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.OutputFormat;
import org.jdom2.output.XMLOutputter;
import org.jdom2.processing.DefaultXMLProcessor;
import org.jdom2.processing.XMLOutputProcessor;
import org.jdom2.text.Text;

import org.junit.Test;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter(new OutputFormat());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlo = new XMLOutputter(new OutputFormat(), null);
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlo = new XMLOutputter(null, new DefaultXMLProcessor());
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new OutputFormat(), new DefaultXMLProcessor());
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterNoArguments() {
 XMLOutputter xmlo = new XMLOutputter();
 assertNotNull(xmlo);
 }

 @Test
 public void testXMLOutputterClone() throws CloneNotSupportedException {
 XMLOutputter original = new XMLOutputter(new OutputFormat(), new DefaultXMLProcessor());
 XMLOutputter clone = original.clone();
 assertNotNull(clone);
 assertEquals(original, clone);
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 OutputFormat format = new OutputFormat();
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
 Document doc = new Document();
 doc.setRootElement(new Element("root"));
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(doc, os);
 String xmlString = os.toString();
 String expectedXml = "<root/>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputDocType() throws IOException {
 Document doc = new Document();
 DocType doctype = doc.getDocumentType();
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(doctype, os);
 String xmlString = os.toString();
 String expectedXml = "<!DOCTYPE root [ ]>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputElement() throws IOException {
 Document doc = new Document();
 Element element = new Element("root");
 doc.setRootElement(element);
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(element, os);
 String xmlString = os.toString();
 String expectedXml = "<root/>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Document doc = new Document();
 Element element = new Element("root");
 Text text = new Text("Hello World");
 element.addContent(text);
 doc.setRootElement(element);
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.outputElementContent(element, os);
 String xmlString = os.toString();
 String expectedXml = "Hello World";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputList() throws IOException {
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 Element element1 = new Element("element1");
 Text text1 = new Text("Hello");
 element1.addContent(text1);
 Element element2 = new Element("element2");
 Text text2 = new Text("World");
 element2.addContent(text2);
 List<Content> list = new ArrayList<>(Arrays.asList(element1, element2));
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(list, os);
 String xmlString = os.toString();
 String expectedXml = "<element1>Hello</element1><element2>World</element2>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(cdata, os);
 String xmlString = os.toString();
 String expectedXml = "<![CDATA[Hello World]]>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputText() throws IOException {
 Document doc = new Document();
 Text text = new Text("Hello World");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(text, os);
 String xmlString = os.toString();
 String expectedXml = "Hello World";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputComment() throws IOException {
 Document doc = new Document();
 Comment comment = new Comment("Hello World");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(comment, os);
 String xmlString = os.toString();
 String expectedXml = "<!--Hello World-->";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 Document doc = new Document();
 ProcessingInstruction pi = new ProcessingInstruction("xml-version", "1.0");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(pi, os);
 String xmlString = os.toString();
 String expectedXml = "<xml-version xmlns='http://www.w3.org/1998/10/ProcessingInstruction' xml:space='preserve'>\n<![CDATA[1.0]]>\n";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 Document doc = new Document();
 EntityRef entityRef = new EntityRef("ampersand");
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(entityRef, os);
 String xmlString = os.toString();
 String expectedXml = "&amp;";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringDocument() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new Document());
 String expectedXml = "";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringDocType() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new DocType());
 String expectedXml = "";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringElement() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new Element("root"));
 String expectedXml = "<root/>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringList() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new ArrayList<Content>());
 String expectedXml = "";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringCDATA() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new CDATA("<![CDATA[Hello World]]>"));
 String expectedXml = "<![CDATA[Hello World]]>";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringText() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new Text("Hello World"));
 String expectedXml = "Hello World";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringComment() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new Comment("Hello World"));
 String expectedXml = "<!--Hello World-->";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new ProcessingInstruction("xml-version", "1.0"));
 String expectedXml = "<xml-version xmlns='http://www.w3.org/1998/10/ProcessingInstruction' xml:space='preserve'>\n<![CDATA[1.0]]>\n";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringEntityRef() {
 XMLOutputter xmlo = createXMLOutputter();
 String xmlString = xmlo.outputString(new EntityRef("ampersand"));
 String expectedXml = "&amp;";
 assertEquals(expectedXml, xmlString);
 }

 @Test
 public void testOutputStringElementContent() {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("root");
 Text text = new Text("Hello World");
 element.addContent(text);
 String xmlString = xmlo.outputElementContentString(element);
 String expectedXml = "Hello World";
 assertEquals(expectedXml, xmlString);
 }

 @Test(expected = IOException.class)
 public void testOutputWithNullDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(null, os);
 fail("Outputting a null Document should throw an IOException.");
 }

 @Test(expected = IOException.class)
 public void testOutputWithNullOutputStream() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(new Document(), null);
 fail("Outputting to a null OutputStream should throw an IOException.");
 }

 @Test(expected = IOException.class)
 public void testOutputWithNullFormat() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.setFormat(null);
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 try {
 xmlo.output(new Document(), os);
 }
 catch (CloneNotSupportedException e) {
 fail("Outputter should not throw CloneNotSupportedException when no Format is specified.");
 }
 fail("Outputting with a null Format should throw an IOException.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputWithInvalidFormat() throws IOException {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new OutputFormat();
 format.setLineSeparator(new StringReader(""));
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 try {
 xmlo.output(new Document(), os, format);
 }
 catch (CloneNotSupportedException e) {
 fail("Outputter should not throw CloneNotSupportedException when an invalid Format is specified.");
 }
 fail("Outputting with an invalid Format should throw an IllegalArgumentException.");
 }
}