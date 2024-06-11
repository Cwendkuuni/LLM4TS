package org.jdom2.output.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.jdom2.*;
import org.jdom2.output.*;
import java.io.*;
import java.util.*;

public class XMLOutputterTest extends TestCase {

 private final XMLOutputter xmlOutputter;
 private final Document doc;
 private final Element root;

 @Before
 public void setUp() {
 xmlOutputter = new XMLOutputter();
 doc = new JDomBuilder().createDocument("root", null);
 root = doc.getRootElement();
 root.addContent(new Element("child1").setText("Child 1"));
 root.addContent(new Element("child2").setText("Child 2"));
 }

 // Test constructor with Format and XMLOutputProcessor
 public void testXMLOutputterWithFormatAndProcessor() {
 XMLOutputter xmlOutputterWithFormatAndProcessor = new XMLOutputter(new Format(), new XMLOutputProcessor());
 Assert.assertNotNull(xmlOutputterWithFormatAndProcessor.getFormat());
 Assert.assertNotNull(xmlOutputterWithFormatAndProcessor.getXMLOutputProcessor());
 }

 // Test constructor with null arguments
 public void testXMLOutputterWithNullArguments() {
 XMLOutputter xmlOutputterWithNullArguments = new XMLOutputter();
 Assert.assertNull(xmlOutputterWithNullArguments.getFormat());
 Assert.assertNull(xmlOutputterWithNullArguments.getXMLOutputProcessor());
 }

 // Test constructor with XMLOutputter instance
 public void testXMLOutputterWithXMLOutputterInstance() {
 XMLOutputter xmlOutputterWithXMLOutputterInstance = new XMLOutputter(xmlOutputter);
 Assert.assertSame(xmlOutputter.getFormat(), xmlOutputterWithXMLOutputterInstance.getFormat());
 Assert.assertNull(xmlOutputterWithXMLOutputterInstance.getXMLOutputProcessor());
 }

 // Test constructor with Format argument
 public void testXMLOutputterWithFormat() {
 XMLOutputter xmlOutputterWithFormat = new XMLOutputter(new Format());
 Assert.assertNotNull(xmlOutputterWithFormat.getFormat());
 Assert.assertNull(xmlOutputterWithFormat.getXMLOutputProcessor());
 }

 // Test constructor with XMLOutputProcessor argument
 public void testXMLOutputterWithXMLOutputProcessor() {
 XMLOutputter xmlOutputterWithXMLOutputProcessor = new XMLOutputter(new XMLOutputProcessor());
 Assert.assertNull(xmlOutputterWithXMLOutputProcessor.getFormat());
 Assert.assertNotNull(xmlOutputterWithXMLOutputProcessor.getXMLOutputProcessor());
 }

 // Test setFormat method
 public void testSetFormat() {
 Format format = new Format();
 xmlOutputter.setFormat(format);
 Assert.assertSame(format, xmlOutputter.getFormat());
 }

 // Test getFormat method
 public void testGetFormat() {
 Assert.assertNotNull(xmlOutputter.getFormat());
 }

 // Test getXMLOutputProcessor method
 public void testGetXMLOutputProcessor() {
 Assert.assertNull(xmlOutputter.getXMLOutputProcessor());
 }

 // Test setXMLOutputProcessor method
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlOutputter.setXMLOutputProcessor(processor);
 Assert.assertSame(processor, xmlOutputter.getXMLOutputProcessor());
 }

 // Test output method with Document argument
 public void testOutputDocument() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(doc, outputStream);
 String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 String actualXml = outputStream.toString();
 Assert.assertEquals(expectedXml, actualXml);
 }

 // Test output method with DocType argument
 public void testOutputDocType() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Element docType = doc.getDocumentType();
 xmlOutputter.output(docType, outputStream);
 String expectedDocTypeXml = "<!DOCTYPE root [ ]>";
 String actualDocTypeXml = outputStream.toString();
 Assert.assertEquals(expectedDocTypeXml, actualDocTypeXml);
 }

 // Test output method with Element argument
 public void testOutputElement() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(root, outputStream);
 String expectedElementXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 String actualElementXml = outputStream.toString();
 Assert.assertEquals(expectedElementXml, actualElementXml);
 }

 // Test outputElementContent method with Element argument
 public void testOutputElementContent() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.outputElementContent(root, outputStream);
 String expectedElementContentXml = "<child1>Child 1</child1><child2>Child 2</child2>";
 String actualElementContentXml = outputStream.toString();
 Assert.assertEquals(expectedElementContentXml, actualElementContentXml);
 }

 // Test output method with List argument
 public void testOutputList() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(new Element("item1").setText("Item 1"));
 list.add(new Element("item2").setText("Item 2"));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(list, outputStream);
 String expectedListXml = "<item1>Item 1</item1><item2>Item 2</item2>";
 String actualListXml = outputStream.toString();
 Assert.assertEquals(expectedListXml, actualListXml);
 }

 // Test output method with CDATA argument
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Hello, World!]]>");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(cdata, outputStream);
 String expectedCdataXml = "<![CDATA[Hello, World!]]>";
 String actualCdataXml = outputStream.toString();
 Assert.assertEquals(expectedCdataXml, actualCdataXml);
 }

 // Test output method with Text argument
 public void testOutputText() throws IOException {
 Text text = new Text("Hello, World!");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(text, outputStream);
 String expectedTextXml = "Hello, World!";
 String actualTextXml = outputStream.toString();
 Assert.assertEquals(expectedTextXml, actualTextXml);
 }

 // Test output method with Comment argument
 public void testOutputComment() throws IOException {
 Comment comment = new Comment("This is a comment.");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(comment, outputStream);
 String expectedCommentXml = "<!--This is a comment.-->";
 String actualCommentXml = outputStream.toString();
 Assert.assertEquals(expectedCommentXml, actualCommentXml);
 }

 // Test output method with ProcessingInstruction argument
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0' encoding='UTF-8'");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(pi, outputStream);
 String expectedPiXml = "<?xml version='1.0' encoding='UTF-8'?>";
 String actualPiXml = outputStream.toString();
 Assert.assertEquals(expectedPiXml, actualPiXml);
 }

 // Test output method with EntityRef argument
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("#pcdata");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 xmlOutputter.output(entityRef, outputStream);
 String expectedEntityRefXml = "&pcdata;";
 String actualEntityRefXml = outputStream.toString();
 Assert.assertEquals(expectedEntityRefXml, actualEntityRefXml);
 }

 // Test outputString method with Document argument
 public void testOutputStringDocument() {
 String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 String actualXml = xmlOutputter.outputString(doc);
 Assert.assertEquals(expectedXml, actualXml);
 }

 // Test outputString method with DocType argument
 public void testOutputStringDocType() {
 String expectedDocTypeXml = "<!DOCTYPE root [ ]>";
 String actualDocTypeXml = xmlOutputter.outputString(doc.getDocumentType());
 Assert.assertEquals(expectedDocTypeXml, actualDocTypeXml);
 }

 // Test outputString method with Element argument
 public void testOutputStringElement() {
 String expectedElementXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 String actualElementXml = xmlOutputter.outputString(root);
 Assert.assertEquals(expectedElementXml, actualElementXml);
 }

 // Test outputString method with List argument
 public void testOutputStringList() {
 List<Element> list = new ArrayList<>();
 list.add(new Element("item1").setText("Item 1"));
 list.add(new Element("item2").setText("Item 2"));
 String expectedListXml = "<item1>Item 1</item1><item2>Item 2</item2>";
 String actualListXml = xmlOutputter.outputString(list);
 Assert.assertEquals(expectedListXml, actualListXml);
 }

 // Test outputString method with CDATA argument
 public void testOutputStringCDATA() {
 CDATA cdata = new CDATA("<![CDATA[Hello, World!]]>");
 String expectedCdataXml = "<![CDATA[Hello, World!]]>";
 String actualCdataXml = xmlOutputter.outputString(cdata);
 Assert.assertEquals(expectedCdataXml, actualCdataXml);
 }

 // Test outputString method with Text argument
 public void testOutputStringText() {
 Text text = new Text("Hello, World!");
 String expectedTextXml = "Hello, World!";
 String actualTextXml = xmlOutputter.outputString(text);
 Assert.assertEquals(expectedTextXml, actualTextXml);
 }

 // Test outputString method with Comment argument
 public void testOutputStringComment() {
 Comment comment = new Comment("This is a comment.");
 String expectedCommentXml = "<!--This is a comment.-->";
 String actualCommentXml = xmlOutputter.outputString(comment);
 Assert.assertEquals(expectedCommentXml, actualCommentXml);
 }

 // Test outputString method with ProcessingInstruction argument
 public void testOutputStringProcessingInstruction() {
 ProcessingInstruction pi = new ProcessingInstruction("xml-declaration", "version='1.0' encoding='UTF-8'");
 String expectedPiXml = "<?xml version='1.0' encoding='UTF-8'?>";
 String actualPiXml = xmlOutputter.outputString(pi);
 Assert.assertEquals(expectedPiXml, actualPiXml);
 }

 // Test outputString method with EntityRef argument
 public void testOutputStringEntityRef() {
 EntityRef entityRef = new EntityRef("#pcdata");
 String expectedEntityRefXml = "&pcdata;";
 String actualEntityRefXml = xmlOutputter.outputString(entityRef);
 Assert.assertEquals(expectedEntityRefXml, actualEntityRefXml);
 }

 // Test escapeAttributeEntities method
 public void testEscapeAttributeEntities() {
 String input = "<tag attribute=\"<>&'\" />";
 String expectedOutput = "<tag attribute=\"&lt;>&amp;&gt;&amp;apos;&quot;\" />";
 String actualOutput = XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(input, xmlOutputter.getFormat());
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 // Test escapeElementEntities method
 public void testEscapeElementEntities() {
 String input = "<tag><child>&<&child/></tag>";
 String expectedOutput = "<tag><child>&amp;lt;&amp;lt;child&amp;gt;&amp;gt;</tag>";
 String actualOutput = XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(input, xmlOutputter.getFormat());
 Assert.assertEquals(expectedOutput, actualOutput);
 }

}