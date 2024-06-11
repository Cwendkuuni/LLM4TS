package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {

 private XMLOutputter xmlo;
 private Document doc;
 private Document docEmpty;
 private OutputStream os;
 private Writer writer;
 private Format format;
 private Format formatEmpty;

 @Before
 public void setUp() {
 XMLOutputter.DEFAULTPROCESSOR = new DefaultXMLProcessor();
 xmlo = new XMLOutputter();
 doc = new JDOMFactory().createDocument("root", null);
 docEmpty = new JDOMFactory().createDocument();
 try {
 os = new ByteArrayOutputStream();
 format = new Format();
 format.setEncoding("UTF-8");
 format.setOmitDeclaration(false);
 format.setLineSeparator("\r\n");
 format.setIndent("  ");
 writer = new BufferedWriter(new OutputStreamWriter(os, format.getEncoding()));
 } catch (UnsupportedEncodingException e) {
 e.printStackTrace();
 }
 }

 @After
 public void tearDown() throws IOException {
 writer.close();
 os.close();
 }

 @Test
 public void testXMLOutputter() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = xmlo1.clone();
 assertNotSame(xmlo1, xmlo2);
 }

 @Test
 public void testConstructorWithFormat() {
 XMLOutputter xmlo = new XMLOutputter(format);
 assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testConstructorWithFormatAndProcessor() {
 XMLOutputProcessor processor = new MyXMLProcessor();
 XMLOutputter xmlo = new XMLOutputter(format, processor);
 assertEquals(format, xmlo.getFormat());
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testConstructorWithOnlyFormat() {
 XMLOutputter xmlo = new XMLOutputter(format);
 assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testConstructorWithOnlyProcessor() {
 XMLOutputProcessor processor = new MyXMLProcessor();
 XMLOutputter xmlo = new XMLOutputter(processor);
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testConstructorWithOnlyThat() {
 XMLOutputter xmlo = new XMLOutputter(new XMLOutputter());
 assertEquals(format, xmlo.getFormat());
 assertEquals(XMLOutputter.DEFAULTPROCESSOR, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testSetFormat() {
 XMLOutputter xmlo = new XMLOutputter();
 Format format = new Format();
 format.setEncoding("UTF-8");
 xmlo.setFormat(format);
 assertEquals(format, xmlo.getFormat());
 }

 @Test
 public void testSetXMLOutputProcessor() {
 XMLOutputProcessor processor = new MyXMLProcessor();
 XMLOutputter xmlo = new XMLOutputter();
 xmlo.setXMLOutputProcessor(processor);
 assertEquals(processor, xmlo.getXMLOutputProcessor());
 }

 @Test
 public void testOutputDocument() throws IOException {
 xmlo.output(doc, writer);
 String xml = writer.toString();
 assertEquals("<root/>\r\n", xml);
 }

 @Test
 public void testOutputDocumentWithEmpty() throws IOException {
 xmlo.output(docEmpty, writer);
 String xml = writer.toString();
 assertEquals("", xml);
 }

 @Test
 public void testOutputDocType() throws IOException {
 Element root = new Element("root");
 DocType doctype = new DocType("root", new String[]{"elem1", "elem2"}, null);
 xmlo.output(doctype, writer);
 String xml = writer.toString();
 assertEquals("<!DOCTYPE root [ <!ELEMENT elem1 (#PCDATA)>\r\n<!ELEMENT elem2 (#PCDATA)>\r\n]>\r\n", xml);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element root = new Element("root");
 Element elem = new Element("elem");
 root.addContent(elem);
 xmlo.output(root, writer);
 String xml = writer.toString();
 assertEquals("<root><elem/></root>\r\n", xml);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Element root = new Element("root");
 Element elem = new Element("elem");
 elem.setText("test");
 root.addContent(elem);
 xmlo.outputElementContent(elem, writer);
 String xml = writer.toString();
 assertEquals("test\r\n", xml);
 }

 @Test
 public void testOutputList() throws IOException {
 Element root = new Element("root");
 Text text1 = new Text("text1");
 Text text2 = new Text("text2");
 root.addContent(text1);
 root.addContent(text2);
 xmlo.output(root.getContent(), writer);
 String xml = writer.toString();
 assertEquals("<root><text>text1</text><text>text2</text></root>\r\n", xml);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 Element root = new Element("root");
 CDATA cd = new CDATA("<![CDATA[test]]>");
 root.addContent(cd);
 xmlo.output(cd, writer);
 String xml = writer.toString();
 assertEquals("<root><![CDATA[test]]></root>\r\n", xml);
 }

 @Test
 public void testOutputText() throws IOException {
 Element root = new Element("root");
 Text text = new Text("test");
 root.addContent(text);
 xmlo.output(text, writer);
 String xml = writer.toString();
 assertEquals("<root><text>test</text></root>\r\n", xml);
 }

 @Test
 public void testOutputComment() throws IOException {
 Element root = new Element("root");
 Comment comment = new Comment("test");
 root.addContent(comment);
 xmlo.output(comment, writer);
 String xml = writer.toString();
 assertEquals("<root><!--test--></root>\r\n", xml);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 Element root = new Element("root");
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version='1.0'");
 root.addContent(pi);
 xmlo.output(pi, writer);
 String xml = writer.toString();
 assertEquals("<root<?xml version='1.0'?>>\r\n", xml);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 Element root = new Element("root");
 EntityRef ref = new EntityRef("testEntity");
 root.addContent(ref);
 xmlo.output(ref, writer);
 String xml = writer.toString();
 assertEquals("<root>&testEntity;</root>\r\n", xml);
 }

 @Test
 public void testOutputStringDocument() {
 String xml = xmlo.outputString(doc);
 assertEquals("<root/>\r\n", xml);
 }

 @Test
 public void testOutputStringDocType() {
 Element root = new Element("root");
 DocType doctype = new DocType("root", new String[]{"elem1", "elem2"}, null);
 String xml = xmlo.outputString(doctype);
 assertEquals("<!DOCTYPE root [ <!ELEMENT elem1 (#PCDATA)>\r\n<!ELEMENT elem2 (#PCDATA)>\r\n]>\r\n", xml);
 }

 @Test
 public void testOutputStringElement() {
 Element root = new Element("root");
 Element elem = new Element("elem");
 root.addContent(elem);
 String xml = xmlo.outputString(root);
 assertEquals("<root><elem/></root>\r\n", xml);
 }

 @Test
 public void testOutputStringList() {
 Element root = new Element("root");
 Text text1 = new Text("text1");
 Text text2 = new Text("text2");
 root.addContent(text1);
 root.addContent(text2);
 String xml = xmlo.outputString(root.getContent());
 assertEquals("<root><text>text1</text><text>text2</text></root>\r\n", xml);
 }

 @Test
 public void testOutputStringCDATA() {
 Element root = new Element("root");
 CDATA cd = new CDATA("<![CDATA[test]]>");
 root.addContent(cd);
 String xml = xmlo.outputString(cd);
 assertEquals("<root><![CDATA[test]]></root>\r\n", xml);
 }

 @Test
 public void testOutputStringText() {
 Element root = new Element("root");
 Text text = new Text("test");
 root.addContent(text);
 String xml = xmlo.outputString(text);
 assertEquals("<root><text>test</text></root>\r\n", xml);
 }

 @Test
 public void testOutputStringComment() {
 Element root = new Element("root");
 Comment comment = new Comment("test");
 root.addContent(comment);
 String xml = xmlo.outputString(comment);
 assertEquals("<root><!--test--></root>\r\n", xml);
 }

 @Test
 public void testOutputStringProcessingInstruction() {
 Element root = new Element("root");
 ProcessingInstruction pi = new ProcessingInstruction("xml", "version='1.0'");
 root.addContent(pi);
 String xml = xmlo.outputString(pi);
 assertEquals("<root<?xml version='1.0'?>>\r\n", xml);
 }

 @Test
 public void testOutputStringEntityRef() {
 Element root = new Element("root");
 EntityRef ref = new EntityRef("testEntity");
 root.addContent(ref);
 String xml = xmlo.outputString(ref);
 assertEquals("<root>&testEntity;</root>\r\n", xml);
 }

 @Test
 public void testOutputStringElementContent() {
 Element root = new Element("root");
 Element elem = new Element("elem");
 elem.setText("test");
 root.addContent(elem);
 String xml = xmlo.outputString(elem.getContent());
 assertEquals("test\r\n", xml);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String str = "&<>";
 String escaped = xmlo.escapeAttributeEntities(str, format);
 assertEquals("&amp;&lt;&gt;", escaped);
 }

 @Test
 public void testEscapeElementEntities() {
 String str = "&<>";
 String escaped = xmlo.escapeElementEntities(str, format);
 assertEquals("&amp;&lt;&gt;", escaped);
 }

 @Test
 public void testClone() {
 XMLOutputter xmlo1 = new XMLOutputter();
 XMLOutputter xmlo2 = xmlo1.clone();
 XMLOutputter xmlo3 = xmlo1;
 assertNotEquals(xmlo1, xmlo3);
 assertNotEquals(xmlo1, xmlo2);
 }
}

 class MyXMLProcessor extends XMLOutputProcessor {
 // Empty implementation for testing purposes
 }