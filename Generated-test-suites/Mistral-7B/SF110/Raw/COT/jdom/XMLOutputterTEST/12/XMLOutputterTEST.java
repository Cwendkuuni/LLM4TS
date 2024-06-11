package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private Document doc;
 private OutputStream out;
 private XMLOutputter xmlOut;

 @Before
 public void setUp() throws Exception {
 doc = JDOMFactory.createRootElement("Root");
 doc.addContent(JDOMFactory.createElement("Element1").setText("Content1"));
 doc.addContent(JDOMFactory.createElement("Element2").setText("Content2"));
 out = new ByteArrayOutputStream();
 xmlOut = new XMLOutputter();
 }

 @After
 public void tearDown() throws Exception {
 out.close();
 }

 @Test
 public void testXMLOutputterConstructorWithFormatAndProcessor() {
 XMLOutputter xmlOut = new XMLOutputter(new Format(), null);
 assertNotNull(xmlOut);
 }

 @Test
 public void testXMLOutputterConstructorWithFormatOnly() {
 XMLOutputter xmlOut = new XMLOutputter(new Format());
 assertNotNull(xmlOut);
 }

 @Test
 public void testXMLOutputterConstructorWithProcessorOnly() {
 XMLOutputter xmlOut = new XMLOutputter(null, new XMLOutputProcessor());
 assertNotNull(xmlOut);
 }

 @Test
 public void testXMLOutputterConstructorWithThat() {
 XMLOutputter xmlOut1 = new XMLOutputter();
 XMLOutputter xmlOut2 = new XMLOutputter(xmlOut1);
 assertNotNull(xmlOut2);
 }

 @Test
 public void testSetFormat() throws Exception {
 Format format = new Format();
 xmlOut.setFormat(format);
 assertEquals(format, xmlOut.getFormat());
 }

 @Test
 public void testGetFormat() throws Exception {
 Format format = xmlOut.getFormat();
 assertNotNull(format);
 }

 @Test
 public void testGetXMLOutputProcessor() throws Exception {
 XMLOutputProcessor processor = xmlOut.getXMLOutputProcessor();
 assertNotNull(processor);
 }

 @Test
 public void testSetXMLOutputProcessor() throws Exception {
 XMLOutputProcessor processor = new XMLOutputProcessor();
 xmlOut.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlOut.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 xmlOut.output(doc, out);
 String actualXML = out.toString();
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n</Root>";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputDocType() throws IOException {
 xmlOut.output(new DocType("Root", null, null, null), out);
 String actualXML = out.toString();
 String expectedXML = "<!DOCTYPE Root [ ]>\n";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputElement() throws IOException {
 xmlOut.output(doc.getRootElement(), out);
 String actualXML = out.toString();
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n</Root>";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 xmlOut.outputElementContent(doc.getRootElement(), out);
 String actualXML = out.toString();
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(doc.getRootElement().getChild("Element1"));
 list.add(doc.getRootElement().getChild("Element2"));
 xmlOut.output(list, out);
 String actualXML = out.toString();
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Content]]>");
 xmlOut.output(cdata, out);
 String actualXML = out.toString();
 String expectedXML = "<![CDATA[Content]]>";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = doc.getRootElement().getTextTrim();
 xmlOut.output(text, out);
 String actualXML = out.toString();
 String expectedXML = "Content1Content2";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = doc.getRootElement().getComment();
 xmlOut.output(comment, out);
 String actualXML = out.toString();
 String expectedXML = "<!-- -->\n";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-version", "1.0");
 xmlOut.output(pi, out);
 String actualXML = out.toString();
 String expectedXML = "<?xml version=\"1.0\"?>\n";
 assertEquals(expectedXML, actualXML);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = doc.getRootElement().getChild("Element1").getChild("EntityRef");
 xmlOut.output(entityRef, out);
 String actualXML = out.toString();
 String expectedXML = "<Element1>&entity;</Element1>";
 assertEquals(expectedXML, actualXML);
 }

 @Test(expected = IOException.class)
 public void testOutputNullDocument() throws IOException {
 xmlOut.output(null, out);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testOutputNullOutputStream() throws IOException {
 xmlOut.output(doc, null);
 fail("Expected IOException");
 }

 @Test
 public void testOutputStringDocument() throws IOException {
 String xmlString = xmlOut.outputString(doc);
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n</Root>";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringDocType() throws IOException {
 String xmlString = xmlOut.outputString(new DocType("Root", null, null, null));
 String expectedXML = "<!DOCTYPE Root [ ]>\n";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringElement() throws IOException {
 String xmlString = xmlOut.outputString(doc.getRootElement());
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n</Root>";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringElementContent() throws IOException {
 String xmlString = xmlOut.outputStringContent(doc.getRootElement());
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringList() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(doc.getRootElement().getChild("Element1"));
 list.add(doc.getRootElement().getChild("Element2"));
 String xmlString = xmlOut.outputString(list);
 String expectedXML = "<Root>\n<Element1>Content1</Element1>\n<Element2>Content2</Element2>\n";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringCDATA() throws IOException {
 CDATA cdata = new CDATA("<![CDATA[Content]]>");
 String xmlString = xmlOut.outputString(cdata);
 String expectedXML = "<![CDATA[Content]]>";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringText() throws IOException {
 Text text = doc.getRootElement().getTextTrim();
 String xmlString = xmlOut.outputString(text);
 String expectedXML = "Content1Content2";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringComment() throws IOException {
 Comment comment = doc.getRootElement().getComment();
 String xmlString = xmlOut.outputString(comment);
 String expectedXML = "<!-- -->\n";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new ProcessingInstruction("xml-version", "1.0");
 String xmlString = xmlOut.outputString(pi);
 String expectedXML = "<?xml version=\"1.0\"?>\n";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testOutputStringEntityRef() throws IOException {
 EntityRef entityRef = doc.getRootElement().getChild("Element1").getChild("EntityRef");
 String xmlString = xmlOut.outputString(entityRef);
 String expectedXML = "<Element1>&entity;</Element1>";
 assertEquals(expectedXML, xmlString);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String str = "<test attr=\"<tag> & & & > & &'\" />";
 String expected = "&lt;test attr=&quot;&lt;tag&gt; &amp;&amp; &amp;&amp; &gt; &amp;&amp; &apos;&quot; &gt;";
 String actual = XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(str, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testEscapeElementEntities() {
 String str = "<test>&lt;tag&gt;</test>";
 String expected = "&lt;test>&amp;lt;tag&gt;&amp;gt;</test>";
 String actual = XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(str, null);
 assertEquals(expected, actual);
 }
}