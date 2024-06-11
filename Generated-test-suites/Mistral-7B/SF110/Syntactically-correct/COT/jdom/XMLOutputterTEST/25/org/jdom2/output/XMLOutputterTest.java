package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLOutputterTest {

 private XMLOutputter createXMLOutputter() {
 return new XMLOutputter();
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlo = new XMLOutputter(new Format(), null);
 Assert.assertNotNull(xmlo.getFormat());
 Assert.assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithFormat() {
 XMLOutputter xmlo = new XMLOutputter(new Format());
 Assert.assertNotNull(xmlo.getFormat());
 Assert.assertNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithProcessor() {
 XMLOutputter xmlo = new XMLOutputter(null, new XMLOutputProcessor());
 Assert.assertNull(xmlo.getFormat());
 Assert.assertNotNull(xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testXMLOutputterConstructorWithThat() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = new XMLOutputter(xmlo1);
 Assert.assertSame(xmlo1.getFormat(), xmlo2.getFormat());
 Assert.assertNull(xmlo2.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 xmlo.setFormat(format);
 Assert.assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testOutputDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new Document();
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(doc, os);
 String expectedXml = "<document/>"; // Assuming an empty 'Document'
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputDocType() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 DocType docType = new DocType("root", null, null);
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(docType, os);
 String expectedXml = "<!DOCTYPE root []>";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElement() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("root");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(element, os);
 String expectedXml = "<root/>";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Text("Hello, World!"));
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.outputElementContent(root, os);
 String expectedXml = "Hello, World!";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputList() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 List<Content> list = new ArrayList<>();
 list.add(new Text("One"));
 list.add(new Text("Two"));
 list.add(new Text("Three"));
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(list, os);
 String expectedXml = "<text>One</text><text>Two</text><text>Three</text>";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 CDATA cdata = new CDATA("<![CDATA[Hello, World!]]>");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(cdata, os);
 String expectedXml = "<![CDATA[Hello, World!]]>";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputText() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Text text = new Text("Hello, World!");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(text, os);
 String expectedXml = "Hello, World!";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputComment() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Comment comment = new Comment("This is a comment.");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(comment, os);
 String expectedXml = "<!--This is a comment.-->";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 ProcessingInstruction pi = new ProcessingInstruction("xml-mapping", "target=\"xml\"");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(pi, os);
 String expectedXml = "<?xml-mapping target=\"xml\">";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entityRef = new EntityRef("Entity1");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 xmlo.output(entityRef, os);
 String expectedXml = "&Entity1;";
 String actualXml = new String(os.toByteArray(), "UTF-8");
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringDocument() {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new Document();
 String expectedXml = "<document/>";
 String actualXml = xmlo.outputString(doc);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringDocType() {
 XMLOutputter xmlo = createXMLOutputter();
 DocType docType = new DocType("root", null, null);
 String expectedXml = "<!DOCTYPE root []>";
 String actualXml = xmlo.outputString(docType);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringElement() {
 XMLOutputter xmlo = createXMLOutputter();
 Element element = new Element("root");
 String expectedXml = "<root/>";
 String actualXml = xmlo.outputString(element);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringList() {
 XMLOutputter xmlo = createXMLOutputter();
 List<Content> list = new ArrayList<>();
 list.add(new Text("One"));
 list.add(new Text("Two"));
 list.add(new Text("Three"));
 String expectedXml = "<text>One</text><text>Two</text><text>Three</text>";
 String actualXml = xmlo.outputString(list);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringCDATA() {
 XMLOutputter xmlo = createXMLOutputter();
 CDATA cdata = new CDATA("<![CDATA[Hello, World!]]>");
 String expectedXml = "<![CDATA[Hello, World!]]>";
 String actualXml = xmlo.outputString(cdata);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringText() {
 XMLOutputter xmlo = createXMLOutputter();
 Text text = new Text("Hello, World!");
 String expectedXml = "Hello, World!";
 String actualXml = xmlo.outputString(text);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringComment() {
 XMLOutputter xmlo = createXMLOutputter();
 Comment comment = new Comment("This is a comment.");
 String expectedXml = "<!--This is a comment.-->";
 String actualXml = xmlo.outputString(comment);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 XMLOutputter xmlo = createXMLOutputter();
 ProcessingInstruction pi = new ProcessingInstruction("xml-mapping", "target=\"xml\"");
 String expectedXml = "<?xml-mapping target=\"xml\">";
 String actualXml = xmlo.outputString(pi);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringEntityRef() {
 XMLOutputter xmlo = createXMLOutputter();
 EntityRef entityRef = new EntityRef("Entity1");
 String expectedXml = "&Entity1;";
 String actualXml = xmlo.outputString(entityRef);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testOutputStringElementContent() {
 XMLOutputter xmlo = createXMLOutputter();
 Element root = new Element("root");
 root.addContent(new Text("Hello, World!"));
 String expectedXml = "Hello, World!";
 String actualXml = xmlo.outputStringElementContent(root);
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 xmlo.output(null, new OutputStreamWriter(System.out));
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws IOException {
 XMLOutputter xmlo = createXMLOutputter();
 Document doc = new Document();
 xmlo.output(doc, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutputNegativeFormatVersion() {
 XMLOutputter xmlo = createXMLOutputter();
 Format format = new Format();
 format.setVersion(-1);
 xmlo.setFormat(format);
 }
}